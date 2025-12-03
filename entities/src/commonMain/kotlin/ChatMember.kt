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

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import opensavvy.telegram.entity.serialization.UnixSecondsSerializer
import kotlin.time.Instant

/**
 * This object contains information about one member of a chat.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#chatmember)
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("status")
sealed class ChatMember {

	/**
	 * Represents a chat member that owns the chat and has all administrator privileges.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#chatmemberowner)
	 */
	@Serializable
	@SerialName("creator")
	data class Owner(
		val user: User,

		@SerialName("is_anonymous")
		val isAnonymous: Boolean,

		@SerialName("custom_title")
		val customTitle: String?,
	) : ChatMember()

	/**
	 * Represents a chat member that has some additional privileges.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#chatmemberadministrator)
	 */
	@Serializable
	@SerialName("administrator")
	data class Administrator(
		val user: User,

		@SerialName("can_be_edited")
		val canBeEdited: Boolean,

		@SerialName("is_anonymous")
		val isAnonymous: Boolean,

		@SerialName("can_manage_chat")
		val canManageChat: Boolean,

		@SerialName("can_delete_messages")
		val canDeleteMessages: Boolean,

		@SerialName("can_manage_video_chats")
		val canManageVideoChats: Boolean,

		@SerialName("can_restrict_members")
		val canRestrictMembers: Boolean,

		@SerialName("can_promote_members")
		val canPromoteMembers: Boolean,

		@SerialName("can_change_info")
		val canChangeInfo: Boolean,

		@SerialName("can_invite_users")
		val canInviteUsers: Boolean,

		@SerialName("can_post_stories")
		val canPostStories: Boolean,

		@SerialName("can_edit_stories")
		val canEditStories: Boolean,

		@SerialName("can_delete_stories")
		val canDeleteStories: Boolean,

		@SerialName("can_post_messages")
		val canPostMessages: Boolean?,

		@SerialName("can_edit_messages")
		val canEditMessages: Boolean?,

		@SerialName("can_pin_messages")
		val canPinMessages: Boolean?,

		@SerialName("can_manage_topics")
		val canManageTopics: Boolean?,

		@SerialName("can_manage_direct_messages")
		val canManageDirectMessages: Boolean?,

		@SerialName("custom_title")
		val customTitle: String?,
	) : ChatMember()

	/**
	 * Represents a chat member that has no additional privileges or restrictions.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#chatmembermember)
	 */
	@Serializable
	@SerialName("member")
	data class Member(
		val user: User,

		@SerialName("until_date")
		val untilDate: @Serializable(with = UnixSecondsSerializer::class) Instant?,
	) : ChatMember()

	/**
	 * Represents a chat member that is under certain restrictions in the chat. Supergroups only.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#chatmemberrestricted)
	 */
	@Serializable
	@SerialName("restricted")
	data class Restricted(
		val user: User,

		@SerialName("is_member")
		val isMember: Boolean,

		@SerialName("can_send_messages")
		val canSendMessages: Boolean,

		@SerialName("can_send_audios")
		val canSendAudios: Boolean,

		@SerialName("can_send_documents")
		val canSendDocuments: Boolean,

		@SerialName("can_send_photos")
		val canSendPhotos: Boolean,

		@SerialName("can_send_videos")
		val canSendVideos: Boolean,

		@SerialName("can_send_video_notes")
		val canSendVideoNotes: Boolean,

		@SerialName("can_send_voice_notes")
		val canSendVoiceNotes: Boolean,

		@SerialName("can_send_polls")
		val canSendPolls: Boolean,

		@SerialName("can_send_other_messages")
		val canSendOtherMessages: Boolean,

		@SerialName("can_add_web_page_previews")
		val canAddWebPagePreviews: Boolean,

		@SerialName("can_change_info")
		val canChangeInfo: Boolean,

		@SerialName("can_invite_users")
		val canInviteUsers: Boolean,

		@SerialName("can_pin_messages")
		val canPinMessages: Boolean,

		@SerialName("can_manage_topics")
		val canManageTopics: Boolean,

		@SerialName("until_date")
		val untilDate: @Serializable(with = UnixSecondsSerializer::class) Instant,
	) : ChatMember()

	/**
	 * Represents a chat member that isn't currently a member of the chat, but may join it themselves.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#chatmemberleft)
	 */
	@Serializable
	@SerialName("left")
	data class Left(
		val user: User,
	) : ChatMember()

	/**
	 * Represents a chat member that was banned in the chat and can't return to the chat or view chat messages.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#chatmemberbanned)
	 */
	@Serializable
	@SerialName("kicked")
	data class Banned(
		val user: User,

		@SerialName("until_date")
		val untilDate: @Serializable(with = UnixSecondsSerializer::class) Instant,
	) : ChatMember()
}
