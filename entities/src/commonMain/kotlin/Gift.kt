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
 * This object describes the types of gifts that can be gifted to a user or a chat.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#acceptedgifttypes)
 */
@Serializable
data class AcceptedGiftTypes(
	@SerialName("unlimited_gifts")
	val unlimitedGifts: Boolean?,

	@SerialName("limited_gifts")
	val limitedGifts: Boolean?,

	@SerialName("unique_gifts")
	val uniqueGifts: Boolean?,

	@SerialName("premium_subscription")
	val premiumSubscription: Boolean?,
)

/**
 * Describes a service message about a regular gift that was sent or received.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#giftinfo)
 */
@Serializable
data class GiftInfo(
	val gift: Gift,

	@SerialName("owned_gift_id")
	val ownedGiftId: String?,

	@SerialName("convert_star_count")
	val convertStarCount: Int?,

	@SerialName("prepaid_upgrade_star_count")
	val prepaidUpgradeStarCount: Int?,

	@SerialName("can_be_upgraded")
	val canBeUpgraded: Boolean = false,

	val text: String?,

	val entities: List<MessageEntity>?,

	@SerialName("is_private")
	val isPrivate: Boolean = false,
)

/**
 * This object represents a gift that can be sent by the bot.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#gift)
 */
@Serializable
data class Gift(
	val id: String,
	val sticker: Sticker,

	@SerialName("star_count")
	val starCount: Int,

	@SerialName("upgrade_star_count")
	val upgradeStarCount: Int?,

	@SerialName("total_count")
	val totalCount: Int?,

	@SerialName("remaining_count")
	val remainingCount: Int?,

	@SerialName("publisher_chat")
	val publisherChat: Chat?,
)

/**
 * Describes a service message about a unique gift that was sent or received.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#uniquegiftinfo)
 */
@Serializable
data class UniqueGiftInfo(
	val gift: UniqueGift,
	val origin: String,

	@SerialName("last_resale_star_count")
	val lastResaleStarCount: Int?,

	@SerialName("owned_gift_id")
	val ownedGiftId: String?,

	@SerialName("transfer_star_count")
	val transferStarCount: Int?,

	@SerialName("next_transfer_date")
	val nextTransferDate: @Serializable(with = UnixSecondsSerializer::class) Instant?,
)

/**
 * This object describes a unique gift that was upgraded from a regular gift.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#uniquegift)
 */
@Serializable
data class UniqueGift(
	@SerialName("base_name")
	val baseName: String,

	val name: String,
	val number: Int,
	val model: UniqueGiftModel,
	val symbol: UniqueGiftSymbol,
	val backdrop: UniqueGiftBackdrop,

	@SerialName("publisher_chat")
	val publisherChat: Chat?,
)

/**
 * This object describes the model of a unique gift.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#uniquegiftmodel)
 */
@Serializable
data class UniqueGiftModel(
	val name: String,
	val sticker: Sticker,

	@SerialName("rarity_per_mille")
	val rarity: Rarity,
)

/**
 * This object describes the symbol shown on the pattern of a unique gift.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#uniquegiftsymbol)
 */
@Serializable
data class UniqueGiftSymbol(
	val name: String,
	val sticker: Sticker,

	@SerialName("rarity_per_mille")
	val rarity: Rarity,
)

/**
 * This object describes the backdrop of a unique gift.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#uniquegiftbackdrop)
 */
@Serializable
data class UniqueGiftBackdrop(
	val name: String,
	val colors: UniqueGiftBackdropColors,

	@SerialName("rarity_per_mille")
	val rarity: Rarity,
)

/**
 * This object describes the colors of the backdrop of a unique gift.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#uniquegiftbackdropcolors)
 */
@Serializable
data class UniqueGiftBackdropColors(
	@SerialName("center_color")
	val centerColor: Int,

	@SerialName("edge_color")
	val edgeColor: Int,

	@SerialName("symbol_color")
	val symbolColor: Int,

	@SerialName("text_color")
	val textColor: Int,
)
