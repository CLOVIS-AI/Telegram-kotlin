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
import opensavvy.telegram.entity.serialization.UnixSecondsSerializer
import kotlin.time.Instant

/**
 * Contains information about the start page settings of a Telegram Business account.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#businessintro)
 */
@Serializable
data class BusinessIntro(
	val title: String?,
	val message: String?,
	val sticker: Sticker?,
)

/**
 * Contains information about the location of a Telegram Business account.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#businesslocation)
 */
@Serializable
data class BusinessLocation(
	val address: String,
	val location: Location?,
)

/**
 * Describes the opening hours of a business.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#businessopeninghours)
 */
@Serializable
data class BusinessOpeningHours(
	@SerialName("time_zone_name")
	val timeZoneName: String,

	@SerialName("opening_hours")
	val openingHours: List<BusinessOpeningHoursInterval>,
)

/**
 * Describes an interval of time during which a business is open.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#businessopeninghoursinterval)
 */
@Serializable
data class BusinessOpeningHoursInterval(
	@SerialName("opening_minute")
	val openingMinute: Int,

	@SerialName("closing_minute")
	val closingMinute: Int,
)

/**
 * Represents the rights of a business bot.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#businessbotrights)
 */
@Serializable
data class BusinessBotRights(
	/** Optional. True, if the bot can send and edit messages in the private chats that had incoming messages in the last 24 hours */
	@SerialName("can_reply")
	val canReply: Boolean = false,

	/** Optional. True, if the bot can mark incoming private messages as read */
	@SerialName("can_read_messages")
	val canReadMessages: Boolean = false,

	/** Optional. True, if the bot can delete messages sent by the bot */
	@SerialName("can_delete_sent_messages")
	val canDeleteSentMessages: Boolean = false,

	/** Optional. True, if the bot can delete all private messages in managed chats */
	@SerialName("can_delete_all_messages")
	val canDeleteAllMessages: Boolean = false,

	/** Optional. True, if the bot can edit the first and last name of the business account */
	@SerialName("can_edit_name")
	val canEditName: Boolean = false,

	/** Optional. True, if the bot can edit the bio of the business account */
	@SerialName("can_edit_bio")
	val canEditBio: Boolean = false,

	/** Optional. True, if the bot can edit the profile photo of the business account */
	@SerialName("can_edit_profile_photo")
	val canEditProfilePhoto: Boolean = false,

	/** Optional. True, if the bot can edit the username of the business account */
	@SerialName("can_edit_username")
	val canEditUsername: Boolean = false,

	/** Optional. True, if the bot can change the privacy settings pertaining to gifts for the business account */
	@SerialName("can_change_gift_settings")
	val canChangeGiftSettings: Boolean = false,

	/** Optional. True, if the bot can view gifts and the amount of Telegram Stars owned by the business account */
	@SerialName("can_view_gifts_and_stars")
	val canViewGiftsAndStars: Boolean = false,

	/** Optional. True, if the bot can convert regular gifts owned by the business account to Telegram Stars */
	@SerialName("can_convert_gifts_to_stars")
	val canConvertGiftsToStars: Boolean = false,

	/** Optional. True, if the bot can transfer and upgrade gifts owned by the business account */
	@SerialName("can_transfer_and_upgrade_gifts")
	val canTransferAndUpgradeGifts: Boolean = false,

	/** Optional. True, if the bot can transfer Telegram Stars received by the business account to its own account, or use them to upgrade and transfer gifts */
	@SerialName("can_transfer_stars")
	val canTransferStars: Boolean = false,

	/** Optional. True, if the bot can post, edit and delete stories on behalf of the business account */
	@SerialName("can_manage_stories")
	val canManageStories: Boolean = false,
)

/**
 * Describes the connection of the bot with a business account.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#businessconnection)
 */
@Serializable
data class BusinessConnection(
	/** Unique identifier of the business connection */
	val id: String,

	/** Business account user that created the business connection */
	val user: User,

	/**
	 * Identifier of a private chat with the user who created the business connection. This number may have more than
	 * 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it.
	 * But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing
	 * this identifier.
	 */
	@SerialName("user_chat_id")
	val userChatId: Long,

	/** Date the connection was established in Unix time */
	val date: @Serializable(with = UnixSecondsSerializer::class) Instant,

	/** Optional. Rights of the business bot */
	val rights: BusinessBotRights?,

	/** True, if the connection is active */
	@SerialName("is_enabled")
	val isEnabled: Boolean,
)

/**
 * This object is received when messages are deleted from a connected business account.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#businessmessagesdeleted)
 */
@Serializable
data class BusinessMessagesDeleted(
	/** Unique identifier of the business connection */
	@SerialName("business_connection_id")
	val businessConnectionId: String,

	/** Information about a chat in the business account. The bot may not have access to the chat or the corresponding user. */
	val chat: Chat,

	/** The list of identifiers of deleted messages in the chat of the business account */
	@SerialName("message_ids")
	val messageIds: List<Message.Id>,
)
