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

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import opensavvy.telegram.entity.*
import kotlin.reflect.KProperty1

interface BotRouter {

	suspend fun route(update: Update, context: BotContext)

	interface Builder {

		fun command(text: String, description: String? = null, handler: suspend BotContext.(Message) -> Unit)

		fun message(handler: suspend BotContext.(Message) -> Unit)

		fun editedMessage(handler: suspend BotContext.(Message) -> Unit)

		fun channelPost(handler: suspend BotContext.(Message) -> Unit)

		fun editedChannelPost(handler: suspend BotContext.(Message) -> Unit)

		fun businessConnection(handler: suspend BotContext.(BusinessConnection) -> Unit)

		fun businessMessage(handler: suspend BotContext.(Message) -> Unit)

		fun editedBusinessMessage(handler: suspend BotContext.(Message) -> Unit)

		fun deletedBusinessMessages(handler: suspend BotContext.(BusinessMessagesDeleted) -> Unit)

		fun chatBoost(handler: suspend BotContext.(ChatBoostUpdated) -> Unit)

		fun removedChatBoost(handler: suspend BotContext.(ChatBoostRemoved) -> Unit)

		fun callbackQuery(handler: suspend BotContext.(CallbackQuery) -> Unit)

	}
}

internal class DefaultBotRouter : BotRouter {
	private val handlers = ArrayList<Handler>()

	override suspend fun route(update: Update, context: BotContext) {
		val handler = handlers.firstOrNull { it.predicate(update) }
			?: run { println("Ignored un-handled update $update"); return }

		try {
			with(handler) {
				context.handle(update)
			}
		} catch (e: Exception) {
			currentCoroutineContext().ensureActive()
			println("Error while handling update ${update.id}\n$update\n${e.stackTraceToString()}")
		}
	}

	private class Handler(
		val predicate: (Update) -> Boolean,
		val command: BotCommand?,
		val handle: suspend BotContext.(Update) -> Unit,
	)

	private inner class Builder : BotRouter.Builder {
		private fun <HandlerType> Handler(
			field: KProperty1<Update, HandlerType?>,
			handle: suspend BotContext.(HandlerType) -> Unit,
		): Handler = Handler(
			predicate = { field.get(it) != null },
			command = null,
			handle = { handle(field.get(it)!!) },
		)

		override fun message(handler: suspend BotContext.(Message) -> Unit) {
			handlers += Handler(Update::message, handler)
		}

		override fun editedMessage(handler: suspend BotContext.(Message) -> Unit) {
			handlers += Handler(Update::editedMessage, handler)
		}

		override fun channelPost(handler: suspend BotContext.(Message) -> Unit) {
			handlers += Handler(Update::channelPost, handler)
		}

		override fun editedChannelPost(handler: suspend BotContext.(Message) -> Unit) {
			handlers += Handler(Update::editedChannelPost, handler)
		}

		override fun businessConnection(handler: suspend BotContext.(BusinessConnection) -> Unit) {
			handlers += Handler(Update::businessConnection, handler)
		}

		override fun businessMessage(handler: suspend BotContext.(Message) -> Unit) {
			handlers += Handler(Update::businessMessage, handler)
		}

		override fun editedBusinessMessage(handler: suspend BotContext.(Message) -> Unit) {
			handlers += Handler(Update::editedBusinessMessage, handler)
		}

		override fun deletedBusinessMessages(handler: suspend BotContext.(BusinessMessagesDeleted) -> Unit) {
			handlers += Handler(Update::deletedBusinessMessages, handler)
		}

		override fun chatBoost(handler: suspend BotContext.(ChatBoostUpdated) -> Unit) {
			handlers += Handler(Update::chatBoost, handler)
		}

		override fun removedChatBoost(handler: suspend BotContext.(ChatBoostRemoved) -> Unit) {
			handlers += Handler(Update::removedChatBoost, handler)
		}

		override fun callbackQuery(handler: suspend BotContext.(CallbackQuery) -> Unit) {
			handlers += Handler(Update::callbackQuery, handler)
		}

		override fun command(text: String, description: String?, handler: suspend BotContext.(Message) -> Unit) {
			handlers += Handler(
				predicate = { update ->
					val entities = update.message?.entities
						?.filterIsInstance<MessageEntity.BotCommand>()

					update.message != null && entities?.any { update.message?.text(it) == text } == true
				},
				command = BotCommand(
					command = text,
					description = description ?: "Command $text",
				),
				handle = { handler(it.message!!) }
			)
		}
	}

	internal fun builder(): BotRouter.Builder = Builder()

	/**
	 * Calls [TelegramBot.setMyCommands] for each command that has been [registered][BotRouter.Builder.command].
	 */
	internal suspend fun registerCommands(bot: TelegramBot) {
		val commands = handlers.mapNotNull { it.command }

		bot.setMyCommands(
			SetMyCommandsParams(
				commands = commands,
			)
		)
	}
}
