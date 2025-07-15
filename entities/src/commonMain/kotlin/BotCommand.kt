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

@Serializable
data class BotCommand(
	val command: String,
	val description: String,
)

@Serializable
sealed class BotCommandScope {

	@Serializable
	@SerialName("default")
	data object Default : BotCommandScope()

	@Serializable
	@SerialName("all_private_chats")
	data object AllPrivateChats : BotCommandScope()

	@Serializable
	@SerialName("all_group_chats")
	data object AllGroupChats : BotCommandScope()

	@Serializable
	@SerialName("all_chat_administrators")
	data object AllChatAdministrators : BotCommandScope()

	@Serializable
	@SerialName("chat")
	data class SpecificChat(
		@SerialName("chat_id")
		val chatId: Int,
	) : BotCommandScope()

	@Serializable
	@SerialName("chat_administrators")
	data class SpecificChatAdministrators(
		@SerialName("chat_id")
		val chatId: Int,
	)

	@Serializable
	@SerialName("chat_member")
	data class ChatMember(
		@SerialName("chat_id")
		val chatId: Int,
		@SerialName("user_id")
		val userId: Int,
	)
}

@Serializable
data class SetMyCommandsParams(
	val commands: List<BotCommand>,
	val scope: BotCommandScope? = null,
	val languageCode: String? = null,
)
