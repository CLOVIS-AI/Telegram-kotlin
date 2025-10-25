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

package opensavvy.telegram.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

/**
 * This object represents a chat.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#chat)
 */
@Serializable
data class Chat(
	val id: Id,
	val type: Type,
	val title: String?,
	val username: String?,
	@SerialName("first_name")
	val firstName: String?,
	@SerialName("last_name")
	val lastName: String?,
	@SerialName("is_forum")
	val isForum: Boolean? = false,
	@SerialName("is_direct_messages")
	val isDirectMessages: Boolean? = false,
) {

	@Serializable
	@JvmInline
	value class Id(val value: Long)

	@Serializable
	enum class Type {
		@SerialName("private")
		Private,

		@SerialName("group")
		Group,

		@SerialName("supergroup")
		Supergroup,

		@SerialName("channel")
		Channel,
	}
}
