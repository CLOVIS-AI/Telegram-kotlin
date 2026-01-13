/*
 * Copyright (c) 2026, OpenSavvy and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package opensavvy.telegram.sdk

import opensavvy.telegram.entity.*
import kotlin.reflect.KProperty1

interface BotRouter {

	suspend fun route(update: Update)

	interface Builder {

		fun command(text: String, handler: (Message) -> Unit)

		fun message(handler: (Message) -> Unit)

		fun editedMessage(handler: (Message) -> Unit)

		fun channelPost(handler: (Message) -> Unit)

		fun editedChannelPost(handler: (Message) -> Unit)

		fun businessConnection(handler: (BusinessConnection) -> Unit)

		fun businessMessage(handler: (Message) -> Unit)

		fun editedBusinessMessage(handler: (Message) -> Unit)

		fun deletedBusinessMessages(handler: (BusinessMessagesDeleted) -> Unit)

		fun chatBoost(handler: (ChatBoostUpdated) -> Unit)

		fun removedChatBoost(handler: (ChatBoostRemoved) -> Unit)

	}
}

internal class DefaultBotRouter : BotRouter {
	private val handlers = ArrayList<Handler>()

	override suspend fun route(update: Update) {
		val handler = handlers.firstOrNull { it.predicate(update) }
			?: run { println("Ignored un-handled update $update"); return }

		handler.handle(update)
	}

	private class Handler(
		val predicate: (Update) -> Boolean,
		val handle: suspend (Update) -> Unit,
	)

	private inner class Builder : BotRouter.Builder {
		private fun <HandlerType> Handler(
			field: KProperty1<Update, HandlerType?>,
			handle: suspend (HandlerType) -> Unit,
		): Handler = Handler(
			predicate = { field.get(it) != null },
			handle = { handle(field.get(it)!!) },
		)

		override fun message(handler: (Message) -> Unit) {
			handlers += Handler(Update::message, handler)
		}

		override fun editedMessage(handler: (Message) -> Unit) {
			handlers += Handler(Update::editedMessage, handler)
		}

		override fun channelPost(handler: (Message) -> Unit) {
			handlers += Handler(Update::channelPost, handler)
		}

		override fun editedChannelPost(handler: (Message) -> Unit) {
			handlers += Handler(Update::editedChannelPost, handler)
		}

		override fun businessConnection(handler: (BusinessConnection) -> Unit) {
			handlers += Handler(Update::businessConnection, handler)
		}

		override fun businessMessage(handler: (Message) -> Unit) {
			handlers += Handler(Update::businessMessage, handler)
		}

		override fun editedBusinessMessage(handler: (Message) -> Unit) {
			handlers += Handler(Update::editedBusinessMessage, handler)
		}

		override fun deletedBusinessMessages(handler: (BusinessMessagesDeleted) -> Unit) {
			handlers += Handler(Update::deletedBusinessMessages, handler)
		}

		override fun chatBoost(handler: (ChatBoostUpdated) -> Unit) {
			handlers += Handler(Update::chatBoost, handler)
		}

		override fun removedChatBoost(handler: (ChatBoostRemoved) -> Unit) {
			handlers += Handler(Update::removedChatBoost, handler)
		}

		override fun command(text: String, handler: (Message) -> Unit) {
			handlers += Handler(
				predicate = { update ->
					val entities = update.message?.entities
						?.filterIsInstance<MessageEntity.BotCommand>()

					update.message != null && entities?.any { update.message?.text(it) == text } == true
				},
				handle = { handler(it.message!!) }
			)
		}

	}

	internal fun builder(): BotRouter.Builder = Builder()
}
