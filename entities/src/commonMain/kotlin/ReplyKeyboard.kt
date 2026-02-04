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
 * This object represents a custom keyboard with reply options.
 *
 * Not supported in channels and for messages sent on behalf of a Telegram Business account.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#replykeyboardmarkup)
 */
@Serializable
data class ReplyKeyboardMarkup(
	/** Array of button rows, each represented by an Array of [KeyboardButton] objects. */
	val keyboard: List<List<KeyboardButton>>,

	/** Requests clients to always show the keyboard when the regular keyboard is hidden. Defaults to false. */
	@SerialName("is_persistent")
	val isPersistent: Boolean = false,

	/** Requests clients to resize the keyboard vertically for optimal fit. Defaults to false. */
	@SerialName("resize_keyboard")
	val resizeKeyboard: Boolean = false,

	/** Requests clients to hide the keyboard as soon as it's been used. Defaults to false. */
	@SerialName("one_time_keyboard")
	val oneTimeKeyboard: Boolean = false,

	/** The placeholder to be shown in the input field when the keyboard is active; 1-64 characters. */
	@SerialName("input_field_placeholder")
	val inputFieldPlaceholder: String? = null,

	/** Use this parameter if you want to show the keyboard to specific users only. */
	val selective: Boolean = false,
) : NewMessageKeyboardMarkup

/**
 * Upon receiving a message with this object, Telegram clients will remove the current custom keyboard
 * and display the default letter-keyboard.
 *
 * By default, custom keyboards are displayed until a new keyboard is sent by a bot. An exception is made
 * for one-time keyboards that are hidden immediately after the user presses a button (see [ReplyKeyboardMarkup]).
 * Not supported in channels and for messages sent on behalf of a Telegram Business account.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#replykeyboardremove)
 */
@Serializable
data class ReplyKeyboardRemove(
	/** Requests clients to remove the custom keyboard. */
	@SerialName("remove_keyboard")
	val removeKeyboard: Boolean,

	/** Use this parameter if you want to remove the keyboard for specific users only. */
	val selective: Boolean = false,
) : NewMessageKeyboardMarkup

/**
 * Upon receiving a message with this object, Telegram clients will display a reply interface to the user
 * (as if the user has selected the bot's message and tapped 'Reply'). This can be extremely useful to create
 * step-by-step flows without sacrificing privacy mode. Not supported in channels and for messages sent on behalf
 * of a Telegram Business account.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#forcereply)
 */
@Serializable
data class ForceReply(
	/** Shows reply interface to the user, as if they manually selected the bot's message and tapped 'Reply'. */
	@SerialName("force_reply")
	val forceReply: Boolean = false,

	/** The placeholder to be shown in the input field when the reply is active; 1-64 characters. */
	@SerialName("input_field_placeholder")
	val inputFieldPlaceholder: String? = null,

	/** Use this parameter if you want to force reply from specific users only. */
	val selective: Boolean? = null,
) : NewMessageKeyboardMarkup

/**
 * This object represents one button of the reply keyboard.
 *
 * At most one of the optional fields must be used to specify type of the button.
 * For simple text buttons, String can be used instead of this object to specify the button text.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#keyboardbutton)
 */
@Serializable
data class KeyboardButton(
	/** Text of the button. If none of the optional fields are used, it will be sent as a message when the button is pressed. */
	val text: String,

	/** If specified, pressing the button will open a list of suitable users. */
	@SerialName("request_users")
	val requestUsers: KeyboardButtonRequestUsers? = null,

	/** If specified, pressing the button will open a list of suitable chats. */
	@SerialName("request_chat")
	val requestChat: KeyboardButtonRequestChat? = null,

	/** If true, the user's phone number will be sent as a contact when the button is pressed. */
	@SerialName("request_contact")
	val requestContact: Boolean = false,

	/** If true, the user's current location will be sent when the button is pressed. */
	@SerialName("request_location")
	val requestLocation: Boolean = false,

	/** If specified, the user will be asked to create a poll and send it to the bot when the button is pressed. */
	@SerialName("request_poll")
	val requestPoll: KeyboardButtonPollType? = null,

	/** If specified, the described Web App will be launched when the button is pressed. */
	@SerialName("web_app")
	val webApp: WebAppInfo? = null,
)

/**
 * This object defines the criteria used to request suitable users.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#keyboardbuttonrequestusers)
 */
@Serializable
data class KeyboardButtonRequestUsers(
	/** Signed 32-bit identifier of the request that will be received back in the UsersShared object. */
	@SerialName("request_id")
	val requestId: Int,

	/** Pass true to request bots, pass false to request regular users. */
	@SerialName("user_is_bot")
	val userIsBot: Boolean? = null,

	/** Pass true to request premium users, pass false to request non-premium users. */
	@SerialName("user_is_premium")
	val userIsPremium: Boolean? = null,

	/** The maximum number of users to be selected; 1-10. Defaults to 1. */
	@SerialName("max_quantity")
	val maxQuantity: Int? = null,

	/** Pass true to request the users' first and last names. */
	@SerialName("request_name")
	val requestName: Boolean? = null,

	/** Pass true to request the users' usernames. */
	@SerialName("request_username")
	val requestUsername: Boolean? = null,

	/** Pass true to request the users' photos. */
	@SerialName("request_photo")
	val requestPhoto: Boolean? = null,
)

/**
 * Represents the rights of an administrator in a chat.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#chatadministratorrights)
 */
@Serializable
data class ChatAdministratorRights(
	/** True, if the user's presence in the chat is hidden. */
	@SerialName("is_anonymous")
	val isAnonymous: Boolean,

	/** True, if the administrator can access the chat event log, get boost list, see hidden members, report spam, ignore slow mode, and send messages without paying Stars. */
	@SerialName("can_manage_chat")
	val canManageChat: Boolean,

	/** True, if the administrator can delete messages of other users. */
	@SerialName("can_delete_messages")
	val canDeleteMessages: Boolean,

	/** True, if the administrator can manage video chats. */
	@SerialName("can_manage_video_chats")
	val canManageVideoChats: Boolean,

	/** True, if the administrator can restrict, ban or unban chat members, or access supergroup statistics. */
	@SerialName("can_restrict_members")
	val canRestrictMembers: Boolean,

	/** True, if the administrator can add new administrators with a subset of their own privileges or demote administrators that they have promoted. */
	@SerialName("can_promote_members")
	val canPromoteMembers: Boolean,

	/** True, if the user is allowed to change the chat title, photo and other settings. */
	@SerialName("can_change_info")
	val canChangeInfo: Boolean,

	/** True, if the user is allowed to invite new users to the chat. */
	@SerialName("can_invite_users")
	val canInviteUsers: Boolean,

	/** True, if the administrator can post stories to the chat. */
	@SerialName("can_post_stories")
	val canPostStories: Boolean,

	/** True, if the administrator can edit stories posted by other users, post stories to the chat page, pin chat stories, and access the chat's story archive. */
	@SerialName("can_edit_stories")
	val canEditStories: Boolean,

	/** True, if the administrator can delete stories posted by other users. */
	@SerialName("can_delete_stories")
	val canDeleteStories: Boolean,

	/** True, if the administrator can post messages in the channel, approve suggested posts, or access channel statistics; for channels only. */
	@SerialName("can_post_messages")
	val canPostMessages: Boolean? = null,

	/** True, if the administrator can edit messages of other users and can pin messages; for channels only. */
	@SerialName("can_edit_messages")
	val canEditMessages: Boolean? = null,

	/** True, if the user is allowed to pin messages; for groups and supergroups only. */
	@SerialName("can_pin_messages")
	val canPinMessages: Boolean? = null,

	/** True, if the user is allowed to create, rename, close, and reopen forum topics; for supergroups only. */
	@SerialName("can_manage_topics")
	val canManageTopics: Boolean? = null,

	/** True, if the administrator can manage direct messages of the channel and decline suggested posts; for channels only. */
	@SerialName("can_manage_direct_messages")
	val canManageDirectMessages: Boolean? = null,
)

/**
 * This object defines the criteria used to request a suitable chat.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#keyboardbuttonrequestchat)
 */
@Serializable
data class KeyboardButtonRequestChat(
	/** Signed 32-bit identifier of the request, which will be received back in the ChatShared object. */
	@SerialName("request_id")
	val requestId: Int,

	/** Pass true to request a channel chat, pass false to request a group or a supergroup chat. */
	@SerialName("chat_is_channel")
	val chatIsChannel: Boolean,

	/** Pass true to request a forum supergroup, pass false to request a non-forum chat. */
	@SerialName("chat_is_forum")
	val chatIsForum: Boolean? = null,

	/** Pass true to request a supergroup or a channel with a username, pass false to request a chat without a username. */
	@SerialName("chat_has_username")
	val chatHasUsername: Boolean? = null,

	/** Pass true to request a chat owned by the user. */
	@SerialName("chat_is_created")
	val chatIsCreated: Boolean? = null,

	/** Required administrator rights of the user in the chat. The rights must be a superset of bot_administrator_rights. */
	@SerialName("user_administrator_rights")
	val userAdministratorRights: ChatAdministratorRights? = null,

	/** Required administrator rights of the bot in the chat. The rights must be a subset of user_administrator_rights. */
	@SerialName("bot_administrator_rights")
	val botAdministratorRights: ChatAdministratorRights? = null,

	/** Pass true to request a chat with the bot as a member. */
	@SerialName("bot_is_member")
	val botIsMember: Boolean? = null,

	/** Pass true to request the chat's title. */
	@SerialName("request_title")
	val requestTitle: Boolean? = null,

	/** Pass true to request the chat's username. */
	@SerialName("request_username")
	val requestUsername: Boolean? = null,

	/** Pass true to request the chat's photo. */
	@SerialName("request_photo")
	val requestPhoto: Boolean? = null,
)

/**
 * This object represents type of a poll, which is allowed to be created and sent when the corresponding button is pressed.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#keyboardbuttonpolltype)
 */
@Serializable
data class KeyboardButtonPollType(
	/** If "quiz" is passed, only quiz polls are allowed; if "regular" is passed, only regular polls are allowed; otherwise any type. */
	val type: String? = null,
)
