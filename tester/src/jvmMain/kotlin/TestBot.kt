/*
 * Copyright (c) 2025-2026, OpenSavvy and contributors.
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

package opensavvy.telegram.tester

import kotlinx.coroutines.runBlocking
import opensavvy.telegram.entity.InlineKeyboardButton
import opensavvy.telegram.entity.InlineKeyboardMarkup
import opensavvy.telegram.entity.Message
import opensavvy.telegram.sdk.TelegramBot
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

fun main() = runBlocking {
	val bot = TelegramBot.create(
		javaClass.getResourceAsStream("/opensavvy/telegram/tester/bot.properties")
			?.use { stream ->
				java.util.Properties().apply { load(stream) }.getProperty("token")
			}
			?.takeIf { it.isNotBlank() }
			?: error("Could not load token from bot.properties. Did you fill in your token?")
	)

	println(bot.getMe())

	bot.poll {
		command("/start", description = "Start discussing with this bot!") { msg ->
			println(" • ${msg.from?.username} started the bot!")

			val hello = msg.reply(
				text = "Hello, ${msg.from?.username}!\n\nReply to this message to get a surprise…",
			)

			hello.awaitReply().reply(
				text = "Surprise!",
			)
		}

		command("/buttons", description = "Display multiple buttons") { msg ->
			msg.reply(
				text = "Here are a few buttons!",
				replyMarkup = InlineKeyboardMarkup(
					listOf(
						listOf(InlineKeyboardButton("1\uFE0F⃣", callbackData = "1"), InlineKeyboardButton("2\uFE0F⃣", callbackData = "2"), InlineKeyboardButton("3\uFE0F⃣", callbackData = "3")),
						listOf(InlineKeyboardButton("4\uFE0F⃣", callbackData = "4"), InlineKeyboardButton("5\uFE0F⃣", callbackData = "5"), InlineKeyboardButton("6\uFE0F⃣", callbackData = "6")),
						listOf(InlineKeyboardButton("7\uFE0F⃣", callbackData = "7"), InlineKeyboardButton("8\uFE0F⃣", callbackData = "8"), InlineKeyboardButton("9\uFE0F⃣", callbackData = "9")),
					)
				)
			)
		}

		callbackQuery {
			println(" • ${it.user.username} pressed the button ${it.data}")
			(it.message as Message).edit(
				text = "Well done, you clicked the button!"
			)
		}

		command("/speed_test") { msg ->
			val announce = msg.reply(
				text = "Quick, you have 5 seconds to reply to this message!",
				replyMarkup = InlineKeyboardMarkup(InlineKeyboardButton("Stop", callbackData = "stop")),
			)
			val start = TimeSource.Monotonic.markNow()

			selectFirst {
				timeout(5.seconds) {
					msg.reply("Oh no, you failed :/")
				}

				announce.reply {
					it.reply("Congrats! You replied in ${start.elapsedNow()}.")
				}

				announce.callbackQuery("stop") {
					announce.edit("Cancelled by ${it.user.firstName}.")
				}
			}
		}

		command("/counter") {
			var count = 0

			val buttons = InlineKeyboardMarkup(
				listOf(
					listOf(
						InlineKeyboardButton("-", callbackData = "-"),
						InlineKeyboardButton("+", callbackData = "+"),
					),
					listOf(
						InlineKeyboardButton("Stop", callbackData = "stop"),
					),
				)
			)

			val msg = bot.sendMessage(
				chat = it.chat.id,
				text = "0",
				replyMarkup = buttons,
			)

			selectUntilStopped {
				timeout(60.minutes) {
					stop()
				}

				msg.callbackQuery("stop") {
					println("'stop' button pressed")
					stop()
				}

				msg.callbackQuery { query ->
					println("'${query.data}' pressed")

					when (query.data) {
						"-" -> count--
						"+" -> count++
					}

					msg.edit(
						text = count.toString(),
						replyMarkup = buttons,
					)
				}
			}

			println("Done!")
			msg.edit(text = "Final count: $count")
		}

		message {
			println(" • ${it.from?.username} sent message: ${it.text}")
		}
	}
}
