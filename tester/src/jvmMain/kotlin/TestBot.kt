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
import opensavvy.telegram.sdk.TelegramBot

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
		command("/start", description = "Start discussing with this bot!") {
			println(" • ${it.from?.username} started the bot!")

			bot.sendMessage(
				chat = it.chat.id,
				text = "Hello, ${it.from?.username}!"
			)
		}

		command("/buttons", description = "Display multiple buttons") {
			bot.sendMessage(
				chat = it.chat.id,
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
		}

		message {
			println(" • ${it.from?.username} sent message: ${it.text}")
		}
	}
}
