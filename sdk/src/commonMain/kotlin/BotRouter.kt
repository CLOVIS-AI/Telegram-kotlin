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
import kotlinx.coroutines.selects.SelectClause0
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import opensavvy.telegram.entity.*
import opensavvy.telegram.sdk.BotRouter.HandlerContext
import kotlin.reflect.KProperty1
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

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
		 * Asynchronously wait for an update that matches [predicate].
		 *
		 * ### Example
		 *
		 * ```kotlin
		 * bot.poll {
		 *     command("/start") {
		 *         val msg = it.reply("Who will be the first to post a message?")
		 *
		 *         val nextMessage = awaitUpdate { it.message != null }.message!!
		 *
		 *         nextMessage.reply("Congrats, this message won!")
		 *     }
		 * }
		 * ```
		 */
		suspend fun awaitUpdate(
			predicate: (Update) -> Boolean,
		): Update

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
		suspend fun Message.awaitReply(): Message =
			awaitUpdate { it.message?.replyTo?.id == this.id }.message!!

		/**
		 * Asynchronously waits for one of the selected events.
		 *
		 * The first event to occur is executed. The others are not.
		 *
		 * ### Example
		 *
		 * ```kotlin
		 * bot.poll {
		 *     command("/choose") {
		 *         val announce = it.reply(
		 *             text = "Choose a pill:",
		 *             replyMarkup = InlineKeyboardMarkup(
		 *                 InlineKeyboardButton("Red", callbackData = "red"),
		 *                 InlineKeyboardButton("Blue", callbackData = "blue")
		 *             )
		 *         )
		 *
		 *         selectFirst {
		 *             timeout(2.minutes) {
		 *                 announce.edit("You took too long…")
		 *             }
		 *
		 *             announce.callbackQuery("red") {
		 *                 announce.edit("You chose red!")
		 *             }
		 *
		 *             announce.callbackQuery("blue") {
		 *                 announce.edit("You chose blue!")
		 *             }
		 *         }
		 *     }
		 * }
		 * ```
		 *
		 * @see HandlerSelectBuilder The different events that can be selected.
		 */
		suspend fun <T> selectFirst(
			builder: HandlerSelectBuilder<T>.() -> Unit,
		): T

		/**
		 * Asynchronously waits for one of the selected events, in a loop, until [stop][StoppableHandlerSelectBuilder.stop] is called.
		 *
		 * Each event is executed sequentially.
		 *
		 * ### Example
		 *
		 * ```kotlin
		 * bot.poll {
		 *     command("/counter") {
		 *         var count = 0
		 *
		 *         val buttons = InlineKeyboardMarkup(
		 *             InlineKeyboardButton("-", callbackData = "-"),
		 *             InlineKeyboardButton("+", callbackData = "+"),
		 *         )
		 *
		 *         val msg = it.reply(
		 *             text = "Counter: $count",
		 *             replyMarkup = buttons,
		 *         )
		 *
		 *         // Loops forever, until 'stop' is called
		 *         selectUntilStopped {
		 *             timeout(60.minutes) {
		 *                 stop()
		 *             }
		 *
		 *             msg.callbackQuery {
		 *                 when (query.data) {
		 *                     "-" -> count--
		 *                     "+" -> count++
		 *                 }
		 *
		 *                 msg.edit(
		 *                     text = "Counter: $count",
		 *                     replyMarkup = buttons,
		 *                 )
		 *             }
		 *         }
		 *
		 *         msg.edit(text = "Final count: $count")
		 *     }
		 * }
		 * ```
		 *
		 * @see StoppableHandlerSelectBuilder The different events that can be selected.
		 */
		suspend fun selectUntilStopped(
			builder: StoppableHandlerSelectBuilder.() -> Unit,
		)
	}

	/**
	 * The different events that can be waited for in [HandlerContext.selectFirst].
	 */
	interface HandlerSelectBuilder<T> : BotContext {

		/**
		 * Waits for [clause], from the KotlinX.Coroutines [select] DSL.
		 * This is useful to wait on arbitrary coroutines.
		 *
		 * ### Example
		 *
		 * ```kotlin
		 * bot.poll {
		 *     command("/start") {
		 *         coroutineScope {
		 *             val job = launch {
		 *                 initialzeDB()
		 *             }
		 *
		 *             selectFirst {
		 *                 on(job.onJoin) {
		 *                     // …
		 *                 }
		 *
		 *                 it.reply {
		 *                     // …
		 *                 }
		 *             }
		 *         }
		 *     }
		 * }
		 * ```
		 */
		fun on(clause: SelectClause0, handler: suspend HandlerContext.() -> T)

		/**
		 * Waits until [timeout] time has elapsed.
		 *
		 * ### Example
		 *
		 * ```kotlin
		 * bot.poll {
		 *     command("/speed") {
		 *         val announce = it.reply("Be quick!")
		 *
		 *         selectFirst {
		 *             timeout(5.seconds) {
		 *                 announce.reply("You weren't fast enough…")
		 *             }
		 *
		 *             announce.reply {
		 *                 it.reply("Congrats!")
		 *             }
		 *         }
		 *     }
		 * }
		 * ```
		 */
		fun timeout(timeout: Duration, handler: suspend HandlerContext.() -> T)

		/**
		 * Waits until an [Update] matching [predicate] is received.
		 *
		 * ### Example
		 *
		 * ```kotlin
		 * bot.poll {
		 *     command("/speed") {
		 *         val announce = it.reply("Quick! Edit a message, any message!")
		 *
		 *         selectFirst {
		 *             timeout(5.seconds) {
		 *                 announce.reply("You weren't fast enough…")
		 *             }
		 *
		 *             update({ it.editedMessage?.chat?.id == announce.chat.id }) {
		 *                 it.reply("Congrats! This was the first edited message!")
		 *             }
		 *         }
		 *     }
		 * }
		 * ```
		 */
		fun update(
			predicate: (Update) -> Boolean,
			handler: suspend HandlerContext.(Update) -> T,
		)

		/**
		 * Waits until someone replies to this message.
		 *
		 * ### Example
		 *
		 * ```kotlin
		 * bot.poll {
		 *     command("/speed") {
		 *         val optionA = it.reply("Choose a message… This one?")
		 *         val optionB = it.reply("…or this one?")
		 *
		 *         val repliedTo = selectFirst {
		 *             optionA.reply {
		 *                 it.reply("Noted, it's the first one.")
		 *                 it
		 *             }
		 *
		 *             optionB.reply {
		 *                 it.reply("Noted, it's the second one.")
		 *                 it
		 *             }
		 *         }
		 *     }
		 * }
		 * ```
		 */
		fun Message.reply(handler: suspend HandlerContext.(Message) -> T) =
			update(
				predicate = { it.message?.replyTo?.id == this.id },
				handler = { handler(it.message!!) },
			)

		/**
		 * Waits until someone clicks on any button of this message.
		 *
		 * ### Example
		 *
		 * ```kotlin
		 * bot.poll {
		 *     command("/choose") {
		 *         val announce = it.reply(
		 *             text = "Choose a pill:",
		 *             replyMarkup = InlineKeyboardMarkup(
		 *                 InlineKeyboardButton("Red", callbackData = "red"),
		 *                 InlineKeyboardButton("Blue", callbackData = "blue")
		 *             )
		 *         )
		 *
		 *         selectFirst {
		 *             timeout(2.minutes) {
		 *                 announce.edit("You took too long.")
		 *             }
		 *
		 *             announce.callbackQuery {
		 *                 announce.edit("You chose ${it.data}!")
		 *             }
		 *         }
		 *     }
		 * }
		 * ```
		 */
		fun Message.callbackQuery(handler: suspend HandlerContext.(CallbackQuery) -> T) =
			update(
				predicate = { (it.callbackQuery?.message as? Message)?.id == this.id },
				handler = { handler(it.callbackQuery!!) },
			)

		/**
		 * Waits until someone clicks on the button with the specified [data] on this message.
		 *
		 * ### Example
		 *
		 * ```kotlin
		 * bot.poll {
		 *     command("/choose") {
		 *         val announce = it.reply(
		 *             text = "Choose a pill:",
		 *             replyMarkup = InlineKeyboardMarkup(
		 *                 InlineKeyboardButton("Red", callbackData = "red"),
		 *                 InlineKeyboardButton("Blue", callbackData = "blue")
		 *             )
		 *         )
		 *
		 *         selectFirst {
		 *             timeout(2.minutes) {
		 *                 announce.edit("You took too long…")
		 *             }
		 *
		 *             announce.callbackQuery("red") {
		 *                 announce.edit("You chose red!")
		 *             }
		 *
		 *             announce.callbackQuery("blue") {
		 *                 announce.edit("You chose blue!")
		 *             }
		 *         }
		 *     }
		 * }
		 * ```
		 */
		fun Message.callbackQuery(data: String, handler: suspend HandlerContext.(CallbackQuery) -> T) =
			update(
				predicate = { (it.callbackQuery?.message as? Message)?.id == this.id && it.callbackQuery?.data == data },
				handler = { handler(it.callbackQuery!!) },
			)
	}

	/**
	 * The different events that can be waited for in [HandlerContext.selectUntilStopped].
	 */
	interface StoppableHandlerSelectBuilder : HandlerSelectBuilder<Unit> {

		/**
		 * Call this method to stop waiting for new events.
		 *
		 * For more information, see [HandlerContext.selectUntilStopped].
		 */
		suspend fun stop(): Nothing
	}
}

internal class DefaultBotRouter(
	private val handlerScope: CoroutineScope,
) : BotRouter {
	private val staticHandlers = ArrayList<Handler>()

	private val dynamicHandlers = ArrayList<Handler>()
	private val dynamicHandlersLock = Mutex()

	private suspend fun findDynamicHandler(update: Update): Handler? = dynamicHandlersLock.withLock("route-${update.id}") {
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

					update.message != null && entities?.any {
						update.message?.text(it)?.startsWith(text) ?: false
					} == true
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

		override suspend fun awaitUpdate(predicate: (Update) -> Boolean): Update {
			val result = CompletableDeferred<Update>(currentCoroutineContext().job)

			val handler = Handler(
				predicate = predicate,
				command = null,
				handle = {
					result.complete(it)
				},
				registration = result,
			)

			dynamicHandlersLock.withLock("awaitUpdate($predicate)") {
				dynamicHandlers += handler
			}

			return result.await()
		}

		override suspend fun <T> selectFirst(builder: BotRouter.HandlerSelectBuilder<T>.() -> Unit): T = coroutineScope {
			val clauseBuilder = HandlerSelectBuilderImpl<T>(this)
				.apply(builder)

			select {
				for (clauseFactory in clauseBuilder.clauses) {
					val (clause, handler) = clauseFactory()
					clause {
						handler()
					}
				}
			}
		}

		override suspend fun selectUntilStopped(builder: BotRouter.StoppableHandlerSelectBuilder.() -> Unit) = supervisorScope {
			val lifecycle = this

			val clauseBuilder = HandlerSelectBuilderImpl<Unit>(lifecycle)

			val stoppableClauseBuilder = object : BotRouter.StoppableHandlerSelectBuilder,
				BotRouter.HandlerSelectBuilder<Unit> by clauseBuilder {

				override suspend fun stop(): Nothing {
					lifecycle.coroutineContext.job.cancelChildren(CancellationException("stop() has been called"))
					currentCoroutineContext().ensureActive() // will always throw a CCE
					error("This can never happen, 'ensureActive' will always throw a CCE")
				}
			}

			stoppableClauseBuilder.builder()

			// Prevent two handlers from running concurrently
			val lock = Mutex()

			for (clauseFactory in clauseBuilder.clauses) {
				lifecycle.launch {
					while (isActive) {
						val (clause, handler) = clauseFactory()
						select {
							clause {
								lock.withLock {
									handler()
								}
							}
						}
					}
				}
				delay(10.milliseconds)
			}

			// due to the surrounding 'supervisorScope', this automatically waits for all jobs to finish cancelling
		}

		private inner class HandlerSelectBuilderImpl<T>(
			private val scope: CoroutineScope,
		) : BotRouter.HandlerSelectBuilder<T>, BotContext by botContext {

			/**
			 * A list of factories that return a clause and a handler.
			 */
			val clauses = ArrayList<() -> Pair<SelectClause0, suspend HandlerContext.() -> T>>()

			override fun on(clause: SelectClause0, handler: suspend HandlerContext.() -> T) {
				clauses += { clause to handler }
			}

			override fun timeout(timeout: Duration, handler: suspend HandlerContext.() -> T) {
				clauses += {
					val job = Job(scope.coroutineContext.job)
					scope.launch {
						delay(timeout)
						job.complete()
					}
					job.onJoin to handler
				}
			}

			override fun update(predicate: (Update) -> Boolean, handler: suspend HandlerContext.(Update) -> T) {
				clauses += {
					val deferred = scope.async {
						awaitUpdate(predicate)
					}
					val completionHandler: suspend HandlerContext.() -> T = {
						// If we reach this point, 'deferred' is guaranteed to have finished, so '.await' is free
						handler(deferred.await())
					}
					deferred.onJoin to completionHandler
				}
			}
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
