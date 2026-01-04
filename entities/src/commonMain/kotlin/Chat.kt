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
import opensavvy.telegram.entity.Chat.Id
import opensavvy.telegram.entity.Chat.Type
import opensavvy.telegram.entity.serialization.DurationSecondsSerializer
import opensavvy.telegram.entity.serialization.UnixSecondsSerializer
import kotlin.jvm.JvmInline
import kotlin.time.Duration
import kotlin.time.Instant

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

/**
 * This object contains full information about a chat.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#chatfullinfo)
 */
@Serializable
data class ChatFullInfo(
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

	@SerialName("accent_color_id")
	val accentColor: AccentColor,

	@SerialName("max_reaction_count")
	val maxReactionCount: Int,

	val photo: ChatPhoto,

	@SerialName("active_usernames")
	val activeUsernames: List<String>,

	val birthDate: BirthDate,

	@SerialName("business_intro")
	val businessIntro: BusinessIntro?,

	@SerialName("business_location")
	val businessLocation: BusinessLocation?,

	@SerialName("business_opening_hours")
	val businessOpeningHours: BusinessOpeningHours?,

	@SerialName("personal_chat")
	val personalChat: Chat?,

	@SerialName("parent_chat")
	val parentChat: Chat?,

	@SerialName("available_reactions")
	val availableReactions: List<ReactionType>?,

	@SerialName("background_custom_emoji_id")
	val backgroundCustomEmojiId: String?,

	@SerialName("profile_accent_color_id")
	val profileAccentColor: AccentColor?,

	@SerialName("profile_background_custom_emoji_id")
	val profileBackgroundCustomEmojiId: String?,

	@SerialName("emoji_status_custom_emoji_id")
	val emojiStatusCustomEmojiId: String?,

	@SerialName("emoji_status_expiration_date")
	val emojiStatusExpirationDate: Int?,

	@SerialName("has_private_forwards")
	val hasPrivateForwards: Boolean = false,

	@SerialName("has_restricted_voice_and_video_messages")
	val hasRestrictedVoiceAndVideoMessages: Boolean = false,

	@SerialName("join_to_send_messages")
	val joinToSendMessages: Boolean = false,

	@SerialName("join_by_request")
	val joinByRequest: Boolean = false,

	val description: String?,

	@SerialName("invite_link")
	val inviteLink: String?,

	@SerialName("pinned_message")
	val pinnedMessage: Message?,

	val permissions: ChatPermissions?,

	@SerialName("accepted_gift_types")
	val acceptedGiftTypes: AcceptedGiftTypes,

	@SerialName("can_send_paid_media")
	val canSendPaidMedia: Boolean = false,

	@SerialName("slow_mode_delay")
	val slowModeDelay: Int?,

	@SerialName("unrestrict_boost_count")
	val unrestrictedBoostCount: Int?,

	@SerialName("message_auto_delete_time")
	val messageAutoDeleteTime: Int?,

	@SerialName("has_aggressive_anti_spam_enabled")
	val hasAggressiveAntiSpamEnabled: Boolean = false,

	@SerialName("has_hidden_members")
	val hasHiddenMembers: Boolean = false,

	@SerialName("has_protected_content")
	val hasProtectedContent: Boolean = false,

	@SerialName("has_visible_history")
	val hasVisibleHistory: Boolean = false,

	@SerialName("sticker_set_name")
	val stickerSetName: String?,

	@SerialName("can_set_sticker_set")
	val canSetStickerName: Boolean = false,

	@SerialName("custom_emoji_sticker_set_name")
	val customEmojiStickerSetName: String?,

	@SerialName("linked_chat_id")
	val linkedChat: Id?,

	val location: ChatLocation?,
)

/**
 * This object represents a chat photo.
 */
@Serializable
data class ChatPhoto(
	@SerialName("small_file_id")
	val smallFileId: String,

	@SerialName("small_file_unique_id")
	val smallFileUniqueId: String,

	@SerialName("big_file_id")
	val bigFileId: String,

	@SerialName("big_file_unique_id")
	val bigFileUniqueId: String,
)

/**
 * Describes actions that a non-administrator user is allowed to take in a chat.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#chatpermissions)
 */
@Serializable
data class ChatPermissions(
	@SerialName("can_send_messages")
	val canSendMessages: Boolean?,

	@SerialName("can_send_audios")
	val canSendAudios: Boolean?,

	@SerialName("can_send_documents")
	val canSendDocuments: Boolean?,

	@SerialName("can_send_photos")
	val canSendPhotos: Boolean?,

	@SerialName("can_send_videos")
	val canSendVideos: Boolean?,

	@SerialName("can_send_video_notes")
	val canSendVideoNotes: Boolean?,

	@SerialName("can_send_voice_notes")
	val canSendVoiceNotes: Boolean?,

	@SerialName("can_send_polls")
	val canSendPolls: Boolean?,

	@SerialName("can_send_other_messages")
	val canSendOtherMessages: Boolean?,

	@SerialName("can_add_web_page_previews")
	val canAddWebPagePreviews: Boolean?,

	@SerialName("can_change_info")
	val canChangeInfo: Boolean?,

	@SerialName("can_invite_users")
	val canInviteUsers: Boolean?,

	@SerialName("can_pin_messages")
	val canPinMessages: Boolean?,

	@SerialName("can_manage_topics")
	val canManageTopics: Boolean?,
)

/**
 * Represents a location to which a chat is connected.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#chatlocation)
 */
@Serializable
data class ChatLocation(
	val location: Location,
	val address: String,
)

/**
 * Represents an invite link for a chat.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#chatinvitelink)
 */
@Serializable
data class ChatInviteLink(
	@SerialName("invite_link")
	val inviteLink: String,

	val creator: User,

	@SerialName("creates_join_request")
	val createsJoinRequest: Boolean = false,

	@SerialName("is_primary")
	val isPrimary: Boolean = false,

	@SerialName("is_revoked")
	val isRevoked: Boolean = false,

	val name: String?,

	@SerialName("expire_date")
	val expireDate: @Serializable(with = UnixSecondsSerializer::class) Instant?,

	@SerialName("member_limit")
	val memberLimit: Int?,

	@SerialName("pending_join_request_count")
	val pendingJoinRequestCount: Int?,

	@SerialName("subscription_period")
	val subscriptionPeriod: @Serializable(with = DurationSecondsSerializer::class) Duration?,

	@SerialName("subscription_price")
	val subscriptionPrice: Int?,
)

/**
 * This object represents changes in the status of a chat member.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#chatmemberupdated)
 */
@Serializable
data class ChatMemberUpdated(
	val chat: Chat,

	val from: User,

	@Serializable(with = UnixSecondsSerializer::class)
	val date: Instant,

	@SerialName("old_chat_member")
	val oldChatMember: ChatMember,

	@SerialName("new_chat_member")
	val newChatMember: ChatMember,

	@SerialName("invite_link")
	val inviteLink: ChatInviteLink?,

	@SerialName("via_join_request")
	val viaJoinRequest: Boolean = false,

	@SerialName("via_chat_folder_invite_link")
	val viaChatFolderInviteLink: Boolean = false,
)

/**
 * Represents a join request sent to a chat.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#chatjoinrequest)
 */
@Serializable
data class ChatJoinRequest(
	val chat: Chat,

	val from: User,

	@SerialName("user_chat_id")
	val userChatId: Long,

	@Serializable(with = UnixSecondsSerializer::class)
	val date: Instant,

	val bio: String?,

	@SerialName("invite_link")
	val inviteLink: ChatInviteLink?,
)
