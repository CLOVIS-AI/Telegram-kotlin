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

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import opensavvy.telegram.entity.serialization.UnixSecondsSerializer
import kotlin.time.Instant

/**
 * This object describes the source of a chat boost.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#chatboostsource)
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("source")
sealed interface ChatBoostSource {
	val user: User?

	/**
	 * The boost was obtained by subscribing to Telegram Premium or by gifting a Telegram Premium subscription to another user.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#chatboostsourcepremium)
	 */
	@Serializable
	@SerialName("premium")
	data class Premium(
		/** User that boosted the chat */
		override val user: User,
	) : ChatBoostSource

	/**
	 * The boost was obtained by the creation of Telegram Premium gift codes to boost a chat. Each such code boosts the chat 4 times for the duration of the corresponding Telegram Premium subscription.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#chatboostsourcegiftcode)
	 */
	@Serializable
	@SerialName("gift_code")
	data class GiftCode(
		/** User for which the gift code was created */
		override val user: User,
	) : ChatBoostSource

	/**
	 * The boost was obtained by the creation of a Telegram Premium or a Telegram Star giveaway. This boosts the chat 4 times for the duration of the corresponding Telegram Premium subscription for Telegram Premium giveaways and prize_star_count / 500 times for one year for Telegram Star giveaways.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#chatboostsourcegiveaway)
	 */
	@Serializable
	@SerialName("giveaway")
	data class Giveaway(
		/** Identifier of a message in the chat with the giveaway; the message could have been deleted already. May be 0 if the message isn't sent yet. */
		@SerialName("giveaway_message_id")
		val giveawayMessageId: Int,

		/** Optional. User that won the prize in the giveaway if any; for Telegram Premium giveaways only */
		override val user: User?,

		/** Optional. The number of Telegram Stars to be split between giveaway winners; for Telegram Star giveaways only */
		@SerialName("prize_star_count")
		val prizeStarCount: Int?,

		/** Optional. True, if the giveaway was completed, but there was no user to win the prize */
		@SerialName("is_unclaimed")
		val isUnclaimed: Boolean = false,
	) : ChatBoostSource
}

/**
 * This object contains information about a chat boost.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#chatboost)
 */
@Serializable
data class ChatBoost(
	/** Unique identifier of the boost */
	@SerialName("boost_id")
	val boostId: String,

	/** Point in time (Unix timestamp) when the chat was boosted */
	@SerialName("add_date")
	val addDate: @Serializable(with = UnixSecondsSerializer::class) Instant,

	/** Point in time (Unix timestamp) when the boost will automatically expire, unless the booster's Telegram Premium subscription is prolonged */
	@SerialName("expiration_date")
	val expirationDate: @Serializable(with = UnixSecondsSerializer::class) Instant,

	/** Source of the added boost */
	val source: ChatBoostSource,
)

/**
 * This object represents a boost added to a chat or changed.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#chatboostupdated)
 */
@Serializable
data class ChatBoostUpdated(
	/** Chat which was boosted */
	val chat: Chat,

	/** Information about the chat boost */
	val boost: ChatBoost,
)

/**
 * This object represents a boost removed from a chat.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#chatboostremoved)
 */
@Serializable
data class ChatBoostRemoved(
	/** Chat which was boosted */
	val chat: Chat,

	/** Unique identifier of the boost */
	@SerialName("boost_id")
	val boostId: String,

	/** Point in time (Unix timestamp) when the boost was removed */
	@SerialName("remove_date")
	val removeDate: @Serializable(with = UnixSecondsSerializer::class) Instant,

	/** Source of the removed boost */
	val source: ChatBoostSource,
)

/**
 * This object represents a list of boosts added to a chat by a user.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#userchatboosts)
 */
@Serializable
data class UserChatBoosts(
	/** The list of boosts added to the chat by the user */
	val boosts: List<ChatBoost>,
)
