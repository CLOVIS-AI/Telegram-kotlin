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

package opensavvy.telegram.sdk

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import opensavvy.telegram.entity.*
import opensavvy.telegram.entity.serialization.TelegramJson
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TelegramBot internal constructor(
	private val client: HttpClient,
) {

	private suspend inline fun <reified T> HttpResponse.bodyOrThrow(
		isSuccess: (Response<T>) -> Boolean = { true },
	): T {
		val response = body<Response<T>>()

		if (response.ok && isSuccess(response)) {
			return response.result!!
		} else {
			throw FailedRequestException(buildString {
				appendLine(response.description ?: "No description provided")
				appendLine(this@bodyOrThrow.bodyAsText())
			})
		}
	}

	private suspend fun HttpResponse.trueOrThrow() {
		bodyOrThrow<Boolean> { it.result == true }
	}

	suspend fun getMe(): User =
		client.get("getMe").bodyOrThrow()

	suspend fun getUpdates(
		offset: Update.Id? = null,
		limit: Int? = null,
		timeout: Duration? = null,
		allowedUpdates: List<String>? = null,
	): List<Update> =
		client.get("getUpdates") {
			if (offset != null)
				parameter("offset", offset.value)

			if (limit != null)
				parameter("limit", limit)

			if (timeout != null)
				parameter("timeout", timeout.inWholeSeconds)

			if (allowedUpdates != null)
				parameter("allowed_updates", allowedUpdates.joinToString(separator = "\",\"", prefix = "[\"", postfix = "\"]"))
		}.bodyOrThrow()

	suspend fun setMyCommands(commands: SetMyCommandsParams) =
		client.post("setMyCommands") {
			setBody(commands)
		}.trueOrThrow()

	suspend fun sendMessage(
		message: NewMessage,
	): Message =
		client.post("sendMessage") {
			setBody(message)
		}.bodyOrThrow()

	suspend fun sendMessage(
		chat: Chat.Id,
		text: String,
		topic: String? = null,
		parseMode: String? = null,
		entities: List<MessageEntity> = emptyList(),
		linkPreviewOptions: LinkPreviewOptions? = null,
		disableNotifications: Boolean = false,
		protectContent: Boolean? = null,
		allowPaidBroadcast: Boolean? = null,
		messageEffectId: String? = null,
		suggestedPostParameters: SuggestedPostParameters? = null,
		replyParameters: ReplyParameters? = null,
		replyMarkup: NewMessageKeyboardMarkup? = null,
		thread: Message.Id? = null,
		businessConnectionId: BusinessConnection.Id? = null,
	): Message = sendMessage(
		NewMessage(
			chat = chat,
			text = text,
			topic = topic,
			parseMode = parseMode,
			entities = entities,
			linkPreviewOptions = linkPreviewOptions,
			disableNotifications = disableNotifications,
			protectContent = protectContent,
			allowPaidBroadcast = allowPaidBroadcast,
			messageEffectId = messageEffectId,
			suggestedPostParameters = suggestedPostParameters,
			replyParameters = replyParameters,
			replyMarkup = replyMarkup,
			thread = thread,
			businessConnectionId = businessConnectionId,
		)
	)

	suspend fun poll(block: BotRouter.Builder.() -> Unit) {
		val router = DefaultBotRouter()
		router.builder().apply(block)

		router.registerCommands(this)

		var lastUpdateId: Update.Id? = null
		while (currentCoroutineContext().isActive) {
			val updates = try {
				getUpdates(offset = lastUpdateId?.plus(1), timeout = 60.seconds)
			} catch (_: HttpRequestTimeoutException) {
				continue // No events while polling, just request again
			}

			for (update in updates) {
				lastUpdateId = update.id
				router.route(update)
			}
		}
	}

	companion object {

		fun create(token: String) = TelegramBot(
			HttpClient {
				install(DefaultRequest) {
					url("https://api.telegram.org/bot$token/")
					contentType(ContentType.Application.Json)
				}

				// install(Logging) {
				// 	logger = Logger.SIMPLE
				// 	level = LogLevel.INFO
				// }

				install(ContentNegotiation) {
					json(TelegramJson)
				}
			}
		)
	}
}
