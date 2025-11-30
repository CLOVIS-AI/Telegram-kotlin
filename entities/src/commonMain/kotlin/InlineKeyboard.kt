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
 * This object represents an inline keyboard that appears right next to the message it belongs to.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#inlinekeyboardmarkup)
 */
@Serializable
data class InlineKeyboardMarkup(
	/** Array of button rows, each represented by an array of [InlineKeyboardButton] objects. */
	@SerialName("inline_keyboard")
	val inlineKeyboard: List<List<InlineKeyboardButton>>,
)

/**
 * This object represents one button of an inline keyboard. Exactly one of the optional fields must be used
 * to specify the type of the button.
 *
 * Note: this data class does not enforce the "exactly one" constraint at runtime; callers should ensure it.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#inlinekeyboardbutton)
 */
@Serializable
data class InlineKeyboardButton(
	/** Label text on the button. */
	val text: String,

	/** HTTP or tg:// URL to be opened when the button is pressed. */
	val url: String? = null,

	/** Data to be sent in a callback query to the bot when the button is pressed, 1-64 bytes. */
	@SerialName("callback_data")
	val callbackData: String? = null,

	/** Description of the Web App that will be launched when the user presses the button. */
	@SerialName("web_app")
	val webApp: WebAppInfo? = null,

	/** An HTTPS URL used to automatically authorize the user. */
	@SerialName("login_url")
	val loginUrl: LoginUrl? = null,

	/** If set, pressing the button will prompt the user to choose a chat and insert an inline query. */
	@SerialName("switch_inline_query")
	val switchInlineQuery: String? = null,

	/** If set, pressing the button will insert the bot's username and the specified inline query in the current chat. */
	@SerialName("switch_inline_query_current_chat")
	val switchInlineQueryCurrentChat: String? = null,

	/** If set, pressing the button will prompt the user to select a chat of the specified type. */
	@SerialName("switch_inline_query_chosen_chat")
	val switchInlineQueryChosenChat: SwitchInlineQueryChosenChat? = null,

	/** Description of the button that copies the specified text to the clipboard. */
	@SerialName("copy_text")
	val copyText: CopyTextButton? = null,

	/** Description of the game that will be launched when the user presses the button. */
	@SerialName("callback_game")
	val callbackGame: CallbackGame? = null,

	/** Specify true to send a Pay button. */
	val pay: Boolean = false,
)

/**
 * This object represents a parameter of the inline keyboard button used to automatically authorize a user.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#loginurl)
 */
@Serializable
data class LoginUrl(
	/** An HTTPS URL to be opened with user authorization data added to the query string. */
	val url: String,

	/** New text of the button in forwarded messages. */
	@SerialName("forward_text")
	val forwardText: String? = null,

	/** Username of a bot to be used for user authorization. */
	@SerialName("bot_username")
	val botUsername: String? = null,

	/** Pass true to request permission for the bot to send messages to the user. */
	@SerialName("request_write_access")
	val requestWriteAccess: Boolean = false,
)

/**
 * This object represents an inline button that switches the current user to inline mode in a chosen chat,
 * with an optional default inline query.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#switchinlinequerychosenchat)
 */
@Serializable
data class SwitchInlineQueryChosenChat(
	/** The default inline query to be inserted in the input field. */
	val query: String? = null,

	/** True, if private chats with users can be chosen. */
	@SerialName("allow_user_chats")
	val allowUserChats: Boolean = false,

	/** True, if private chats with bots can be chosen. */
	@SerialName("allow_bot_chats")
	val allowBotChats: Boolean = false,

	/** True, if group and supergroup chats can be chosen. */
	@SerialName("allow_group_chats")
	val allowGroupChats: Boolean = false,

	/** True, if channel chats can be chosen. */
	@SerialName("allow_channel_chats")
	val allowChannelChats: Boolean = false,
)

/**
 * This object represents an inline keyboard button that copies specified text to the clipboard.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#copytextbutton)
 */
@Serializable
data class CopyTextButton(
	/** The text to be copied to the clipboard; 1-256 characters. */
	val text: String,
)

/**
 * A placeholder, currently holds no information. Use BotFather to set up your game.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#callbackgame)
 */
@Serializable
class CallbackGame
