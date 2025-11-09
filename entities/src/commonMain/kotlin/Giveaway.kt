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
import opensavvy.telegram.entity.serialization.UnixSecondsSerializer
import kotlin.time.Instant

/**
 * This object represents a service message about the creation of a scheduled giveaway.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#giveawaycreated)
 */
@Serializable
data class GiveawayCreated(
	/** Optional. The number of Telegram Stars to be split between giveaway winners; for Telegram Star giveaways only. */
	@SerialName("prize_star_count")
	val prizeStarCount: Int?,
)

/**
 * This object represents a message about a scheduled giveaway.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#giveaway)
 */
@Serializable
data class Giveaway(
	/** The list of chats which the user must join to participate in the giveaway. */
	val chats: List<Chat>,

	/** Point in time (Unix timestamp) when winners of the giveaway will be selected. */
	@SerialName("winners_selection_date")
	val winnersSelectionDate: @Serializable(with = UnixSecondsSerializer::class) Instant,

	/** The number of users which are supposed to be selected as winners of the giveaway. */
	@SerialName("winner_count")
	val winnerCount: Int,

	/** Optional. True, if only users who join the chats after the giveaway started should be eligible to win. */
	@SerialName("only_new_members")
	val onlyNewMembers: Boolean = false,

	/** Optional. True, if the list of giveaway winners will be visible to everyone. */
	@SerialName("has_public_winners")
	val hasPublicWinners: Boolean = false,

	/** Optional. Description of additional giveaway prize. */
	@SerialName("prize_description")
	val prizeDescription: String?,

	/**
	 * Optional. A list of two-letter ISO 3166-1 alpha-2 country codes indicating the countries from which eligible
	 * users for the giveaway must come. If empty, then all users can participate in the giveaway. Users with a phone
	 * number that was bought on Fragment can always participate in giveaways.
	 */
	@SerialName("country_codes")
	val countryCodes: List<CountryCode> = emptyList(),

	/** Optional. The number of Telegram Stars to be split between giveaway winners; for Telegram Star giveaways only. */
	@SerialName("prize_star_count")
	val prizeStarCount: Int?,

	/** Optional. The number of months the Telegram Premium subscription won will be active for; Premium giveaways only. */
	@SerialName("premium_subscription_month_count")
	val premiumSubscriptionMonthCount: Int?,
)

/**
 * This object represents a message about the completion of a giveaway with public winners.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#giveawaywinners)
 */
@Serializable
data class GiveawayWinners(
	/** The chat that created the giveaway. */
	val chat: Chat,

	/** Identifier of the message with the giveaway in the chat. */
	@SerialName("giveaway_message_id")
	val giveawayMessageId: Int,

	/** Point in time (Unix timestamp) when winners of the giveaway were selected. */
	@SerialName("winners_selection_date")
	val winnersSelectionDate: @Serializable(with = UnixSecondsSerializer::class) Instant,

	/** Total number of winners in the giveaway. */
	@SerialName("winner_count")
	val winnerCount: Int,

	/** List of up to 100 winners of the giveaway. */
	val winners: List<User>,

	/** Optional. The number of other chats the user had to join in order to be eligible for the giveaway. */
	@SerialName("additional_chat_count")
	val additionalChatCount: Int?,

	/** Optional. The number of Telegram Stars that were split between giveaway winners; Star giveaways only. */
	@SerialName("prize_star_count")
	val prizeStarCount: Int?,

	/** Optional. The number of months the Telegram Premium subscription won will be active for; Premium giveaways only. */
	@SerialName("premium_subscription_month_count")
	val premiumSubscriptionMonthCount: Int?,

	/** Optional. Number of undistributed prizes. */
	@SerialName("unclaimed_prize_count")
	val unclaimedPrizeCount: Int?,

	/** Optional. True, if only users who had joined the chats after the giveaway started were eligible to win. */
	@SerialName("only_new_members")
	val onlyNewMembers: Boolean = false,

	/** Optional. True, if the giveaway was canceled because the payment for it was refunded. */
	@SerialName("was_refunded")
	val wasRefunded: Boolean = false,

	/** Optional. Description of additional giveaway prize. */
	@SerialName("prize_description")
	val prizeDescription: String?,
)

/**
 * This object represents a service message about the completion of a giveaway without public winners.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#giveawaycompleted)
 */
@Serializable
data class GiveawayCompleted(
	/** Number of winners in the giveaway. */
	@SerialName("winner_count")
	val winnerCount: Int,

	/** Optional. Number of undistributed prizes. */
	@SerialName("unclaimed_prize_count")
	val unclaimedPrizeCount: Int?,

	/** Optional. Message with the giveaway that was completed, if it wasn't deleted. */
	@SerialName("giveaway_message")
	val giveawayMessage: Message?,

	/** Optional. True, if the giveaway is a Telegram Star giveaway. */
	@SerialName("is_star_giveaway")
	val isStarGiveaway: Boolean = false,
)
