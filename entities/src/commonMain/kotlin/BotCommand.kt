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

/**
 * This object represents a bot command.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#botcommand)
 */
@Serializable
data class BotCommand(
	val command: String,
	val description: String,
)

/**
 * This object represents the scope to which bot commands are applied.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#botcommandscope)
 */
@Serializable
sealed class BotCommandScope {

	/**
	 * Represents the default scope of bot commands. Default commands are used if no commands with a narrower scope are specified for the user.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#botcommandscopedefault)
	 */
	@Serializable
	@SerialName("default")
	data object Default : BotCommandScope()

	/**
	 * Represents the scope of bot commands, covering all private chats.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#botcommandscopeallprivatechats)
	 */
	@Serializable
	@SerialName("all_private_chats")
	data object AllPrivateChats : BotCommandScope()

	/**
	 * Represents the scope of bot commands, covering all group and supergroup chats.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#botcommandscopeallgroupchats)
	 */
	@Serializable
	@SerialName("all_group_chats")
	data object AllGroupChats : BotCommandScope()

	/**
	 * Represents the scope of bot commands, covering all group and supergroup chat administrators.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#botcommandscopeallchatadministrators)
	 */
	@Serializable
	@SerialName("all_chat_administrators")
	data object AllChatAdministrators : BotCommandScope()

	/**
	 * Represents the scope of bot commands, covering a specific chat.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#botcommandscopechat)
	 */
	@Serializable
	@SerialName("chat")
	data class SpecificChat(
		@SerialName("chat_id")
		val chatId: Chat.Id,
	) : BotCommandScope()

	/**
	 * Represents the scope of bot commands, covering all administrators of a specific group or supergroup chat.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#botcommandscopechatadministrators)
	 */
	@Serializable
	@SerialName("chat_administrators")
	data class SpecificChatAdministrators(
		@SerialName("chat_id")
		val chatId: Chat.Id,
	)

	/**
	 * Represents the scope of bot commands, covering a specific member of a group or supergroup chat.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#botcommandscopechatmember)
	 */
	@Serializable
	@SerialName("chat_member")
	data class ChatMember(
		@SerialName("chat_id")
		val chatId: Chat.Id,
		@SerialName("user_id")
		val userId: User.Id,
	)
}

/**
 * Parameters of the `/setMyCommands` endpoint.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#setmycommands)
 */
@Serializable
data class SetMyCommandsParams(
	val commands: List<BotCommand>,
	val scope: BotCommandScope? = null,
	val languageCode: LanguageCode? = null,
)
