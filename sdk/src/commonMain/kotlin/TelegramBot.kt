/*
 * Copyright (c) 2025, OpenSavvy and contributors.
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
import io.ktor.client.call.body
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import opensavvy.telegram.entity.Response
import opensavvy.telegram.entity.SetMyCommandsParams
import opensavvy.telegram.entity.Update
import opensavvy.telegram.entity.User

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

	suspend fun getUpdates(): List<Update> =
		client.get("getUpdates").bodyOrThrow()

	suspend fun setMyCommands(commands: SetMyCommandsParams) =
		client.post("setMyCommands") {
			setBody(commands)
		}.trueOrThrow()

	companion object {

		fun create(token: String) = TelegramBot(
			HttpClient {
				install(DefaultRequest) {
					url("https://api.telegram.org/bot$token/")
					contentType(ContentType.Application.Json)
				}

				install(Logging) {
					logger = Logger.SIMPLE
					level = LogLevel.ALL // TODO in the future: spam less
				}

				install(ContentNegotiation) {
					json(Json {
						ignoreUnknownKeys = true
						explicitNulls = false
					})
				}
			}
		)
	}
}
