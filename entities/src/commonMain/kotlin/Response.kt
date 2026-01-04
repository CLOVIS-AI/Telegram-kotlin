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

package opensavvy.telegram.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The response Telegram sends after any request.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#authorizing-your-bot)
 */
@Serializable
data class Response<T>(
	val ok: Boolean,
	val description: String?,
	val result: T? = null,

	@SerialName("error_code")
	val errorCode: Int?,

	val parameters: ResponseParameters?,
)

/**
 * Describes why a request was unsuccessful.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#responseparameters)
 */
@Serializable
data class ResponseParameters(
	/**
	 * Optional. The group has been migrated to a supergroup with the specified identifier. This number may have more
	 * than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it.
	 * But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for
	 * storing this identifier.
	 */
	@SerialName("migrate_to_chat_id")
	val migrateToChatId: Long?,

	/** Optional. In case of exceeding flood control, the number of seconds left to wait before the request can be repeated. */
	@SerialName("retry_after")
	val retryAfter: Int?,
)
