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

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import opensavvy.telegram.entity.*
import opensavvy.telegram.sdk.BotRouter.HandlerContext
import kotlin.reflect.KProperty1

interface BotRouter {

	suspend fun route(update: Update, context: BotContext)

	interface Builder {

		fun command(text: String, description: String? = null, handler: suspend HandlerContext.(Message) -> Unit)

		fun message(handler: suspend HandlerContext.(Message) -> Unit)

		fun editedMessage(handler: suspend HandlerContext.(Message) -> Unit)

		fun channelPost(handler: suspend HandlerContext.(Message) -> Unit)

		fun editedChannelPost(handler: suspend HandlerContext.(Message) -> Unit)

		fun businessConnection(handler: suspend HandlerContext.(BusinessConnection) -> Unit)

		fun businessMessage(handler: suspend HandlerContext.(Message) -> Unit)

		fun editedBusinessMessage(handler: suspend HandlerContext.(Message) -> Unit)

		fun deletedBusinessMessages(handler: suspend HandlerContext.(BusinessMessagesDeleted) -> Unit)

		fun chatBoost(handler: suspend HandlerContext.(ChatBoostUpdated) -> Unit)

		fun removedChatBoost(handler: suspend HandlerContext.(ChatBoostRemoved) -> Unit)

		fun callbackQuery(handler: suspend HandlerContext.(CallbackQuery) -> Unit)
	}

	interface HandlerContext : BotContext {

		/**
		 * Asynchronously wait for a user to reply to the message, and returns their reply.
		 *
		 * ### Example
		 *
		 * ```kotlin
		 * bot.poll {
		 *     command("/start") {
		 *         val msg = it.reply("Reply to me!")
		 *
		 *         // Wait for the user to reply to 'msg'
		 *         msg.awaitReply()
		 *         //  Reply to their new message
		 *             .reply("Ahah, thanks for replying!")
		 *     }
		 * }
		 * ```
		 */
		suspend fun Message.awaitReply(): Message
	}
}

internal class DefaultBotRouter(
	private val handlerScope: CoroutineScope,
) : BotRouter {
	private val staticHandlers = ArrayList<Handler>()

	private val dynamicHandlers = HashSet<Handler>()
	private val dynamicHandlersLock = Mutex()

	private suspend fun findDynamicHandler(update: Update): Handler? = dynamicHandlersLock.withLock("route-${update.id}") {
		println("Trying to route update $update\n  with dynamic handlers: $dynamicHandlers")

		val iter = dynamicHandlers.iterator()
		while (iter.hasNext()) {
			val handler = iter.next()

			if (handler.registration?.isActive == false) {
				iter.remove()
			}

			if (handler.predicate(update)) {
				iter.remove()
				return handler
			}
		}
		null
	}

	private fun findStaticHandlerFor(update: Update): Handler? =
		staticHandlers.firstOrNull { it.predicate(update) }

	override suspend fun route(update: Update, context: BotContext) {
		val handler = findDynamicHandler(update)
			?: findStaticHandlerFor(update)
			?: run { println("Ignored un-handled update $update"); return }

		val handlerContext = HandlerContextImpl(context)

		handlerScope.launch {
			try {
				with(handler) {
					handlerContext.handle(update)
				}
			} catch (e: Exception) {
				currentCoroutineContext().ensureActive()
				println("Error while handling update ${update.id}\n$update\n${e.stackTraceToString()}")
			}
		}
	}

	private class Handler(
		val predicate: (Update) -> Boolean,
		val command: BotCommand?,
		val handle: suspend HandlerContext.(Update) -> Unit,
		val registration: Job? = null,
	)

	private inner class Builder : BotRouter.Builder {
		private fun <HandlerType> Handler(
			field: KProperty1<Update, HandlerType?>,
			handle: suspend HandlerContext.(HandlerType) -> Unit,
		): Handler = Handler(
			predicate = { field.get(it) != null },
			command = null,
			handle = { handle(field.get(it)!!) },
		)

		override fun message(handler: suspend HandlerContext.(Message) -> Unit) {
			staticHandlers += Handler(Update::message, handler)
		}

		override fun editedMessage(handler: suspend HandlerContext.(Message) -> Unit) {
			staticHandlers += Handler(Update::editedMessage, handler)
		}

		override fun channelPost(handler: suspend HandlerContext.(Message) -> Unit) {
			staticHandlers += Handler(Update::channelPost, handler)
		}

		override fun editedChannelPost(handler: suspend HandlerContext.(Message) -> Unit) {
			staticHandlers += Handler(Update::editedChannelPost, handler)
		}

		override fun businessConnection(handler: suspend HandlerContext.(BusinessConnection) -> Unit) {
			staticHandlers += Handler(Update::businessConnection, handler)
		}

		override fun businessMessage(handler: suspend HandlerContext.(Message) -> Unit) {
			staticHandlers += Handler(Update::businessMessage, handler)
		}

		override fun editedBusinessMessage(handler: suspend HandlerContext.(Message) -> Unit) {
			staticHandlers += Handler(Update::editedBusinessMessage, handler)
		}

		override fun deletedBusinessMessages(handler: suspend HandlerContext.(BusinessMessagesDeleted) -> Unit) {
			staticHandlers += Handler(Update::deletedBusinessMessages, handler)
		}

		override fun chatBoost(handler: suspend HandlerContext.(ChatBoostUpdated) -> Unit) {
			staticHandlers += Handler(Update::chatBoost, handler)
		}

		override fun removedChatBoost(handler: suspend HandlerContext.(ChatBoostRemoved) -> Unit) {
			staticHandlers += Handler(Update::removedChatBoost, handler)
		}

		override fun callbackQuery(handler: suspend HandlerContext.(CallbackQuery) -> Unit) {
			staticHandlers += Handler(Update::callbackQuery, handler)
		}

		override fun command(text: String, description: String?, handler: suspend HandlerContext.(Message) -> Unit) {
			staticHandlers += Handler(
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

	private inner class HandlerContextImpl(
		private val botContext: BotContext,
	) : HandlerContext, BotContext by botContext {

		override suspend fun Message.awaitReply(): Message {
			val result = CompletableDeferred<Message>(currentCoroutineContext().job)

			val handler = Handler(
				predicate = { it.message?.replyTo?.id == this.id },
				command = null,
				handle = {
					result.complete(it.message!!)
				},
				registration = result,
			)

			dynamicHandlersLock.withLock("reply-${this.chat.id}-${this.id}") {
				println("Registering dynamic handler: $handler")
				dynamicHandlers += handler
			}

			return result.await()
		}
	}

	internal fun builder(): BotRouter.Builder = Builder()

	/**
	 * Calls [TelegramBot.setMyCommands] for each command that has been [registered][BotRouter.Builder.command].
	 */
	internal suspend fun registerCommands(bot: TelegramBot) {
		val commands = staticHandlers.mapNotNull { it.command }

		bot.setMyCommands(
			SetMyCommandsParams(
				commands = commands,
			)
		)
	}
}
