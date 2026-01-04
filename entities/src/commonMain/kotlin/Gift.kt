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
	val unlimitedGifts: Boolean,

	@SerialName("limited_gifts")
	val limitedGifts: Boolean,

	@SerialName("unique_gifts")
	val uniqueGifts: Boolean,

	@SerialName("premium_subscription")
	val premiumSubscription: Boolean,

	@SerialName("gifts_from_channels")
	val giftsFromChannels: Boolean,
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

	@SerialName("is_upgrade_separate")
	val isUpgradeSeparate: Boolean = false,

	@SerialName("can_be_upgraded")
	val canBeUpgraded: Boolean = false,

	val text: String?,

	val entities: List<MessageEntity>?,

	@SerialName("is_private")
	val isPrivate: Boolean = false,

	@SerialName("unique_gift_number")
	val uniqueGiftNumber: Int?,
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

	@SerialName("is_premium")
	val isPremium: Boolean = false,

	@SerialName("has_colors")
	val hasColors: Boolean = false,

	@SerialName("total_count")
	val totalCount: Int?,

	@SerialName("remaining_count")
	val remainingCount: Int?,

	@SerialName("personal_total_count")
	val personalTotalCount: Int?,

	@SerialName("personal_remaining_count")
	val personalRemainingCount: Int?,

	val background: GiftBackground?,

	@SerialName("unique_gift_variant_count")
	val uniqueGiftVariantCount: Int?,

	@SerialName("publisher_chat")
	val publisherChat: Chat?,
)

/**
 * This object describes the background of a gift.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#giftbackground)
 */
@Serializable
data class GiftBackground(
	@SerialName("center_color")
	val centerColor: Int,

	@SerialName("edge_color")
	val edgeColor: Int,

	@SerialName("text_color")
	val textColor: Int,
)

/**
 * This object represent a list of gifts.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#gifts)
 */
@Serializable
data class Gifts(
	val gifts: List<Gift>,
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

	@SerialName("last_resale_currency")
	val lastResaleCurrency: String?,

	@SerialName("last_resale_amount")
	val lastResaleAmount: Long?,

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
	@SerialName("gift_id")
	val giftId: String,

	@SerialName("base_name")
	val baseName: String,

	val name: String,
	val number: Int,
	val model: UniqueGiftModel,
	val symbol: UniqueGiftSymbol,
	val backdrop: UniqueGiftBackdrop,

	@SerialName("is_premium")
	val isPremium: Boolean = false,

	@SerialName("is_from_blockchain")
	val isFromBlockchain: Boolean = false,

	val colors: UniqueGiftColors?,

	@SerialName("publisher_chat")
	val publisherChat: Chat?,
)

/**
 * This object describes a gift received and owned by a user or a chat.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#ownedgift)
 */
@Serializable
sealed class OwnedGift {

	/**
	 * Describes a regular gift owned by a user or a chat.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#ownedgiftregular)
	 */
	@Serializable
	@SerialName("regular")
	data class Regular(
		val gift: Gift,

		@SerialName("owned_gift_id")
		val ownedGiftId: String?,

		@SerialName("sender_user")
		val senderUser: User?,

		@SerialName("send_date")
		@Serializable(with = UnixSecondsSerializer::class)
		val sendDate: Instant,

		val text: String?,

		val entities: List<MessageEntity>?,

		@SerialName("is_private")
		val isPrivate: Boolean = false,

		@SerialName("is_saved")
		val isSaved: Boolean = false,

		@SerialName("can_be_upgraded")
		val canBeUpgraded: Boolean = false,

		@SerialName("was_refunded")
		val wasRefunded: Boolean = false,

		@SerialName("convert_star_count")
		val convertStarCount: Int?,

		@SerialName("prepaid_upgrade_star_count")
		val prepaidUpgradeStarCount: Int?,

		@SerialName("is_upgrade_separate")
		val isUpgradeSeparate: Boolean = false,

		@SerialName("unique_gift_number")
		val uniqueGiftNumber: Int?,
	) : OwnedGift()

	/**
	 * Describes a unique gift received and owned by a user or a chat.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#ownedgiftunique)
	 */
	@Serializable
	@SerialName("unique")
	data class Unique(
		val gift: UniqueGift,

		@SerialName("owned_gift_id")
		val ownedGiftId: String?,

		@SerialName("sender_user")
		val senderUser: User?,

		@SerialName("send_date")
		@Serializable(with = UnixSecondsSerializer::class)
		val sendDate: Instant,

		@SerialName("is_saved")
		val isSaved: Boolean = false,

		@SerialName("can_be_transferred")
		val canBeTransferred: Boolean = false,

		@SerialName("transfer_star_count")
		val transferStarCount: Int?,

		@SerialName("next_transfer_date")
		@Serializable(with = UnixSecondsSerializer::class)
		val nextTransferDate: Instant?,
	) : OwnedGift()
}

/**
 * Contains the list of gifts received and owned by a user or a chat.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#ownedgifts)
 */
@Serializable
data class OwnedGifts(
	@SerialName("total_count")
	val totalCount: Int,

	val gifts: List<OwnedGift>,

	@SerialName("next_offset")
	val nextOffset: String?,
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

/**
 * This object contains information about the color scheme for a user's name, message replies and link previews based on a unique gift.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#uniquegiftcolors)
 */
@Serializable
data class UniqueGiftColors(
	@SerialName("model_custom_emoji_id")
	val modelCustomEmojiId: String,

	@SerialName("symbol_custom_emoji_id")
	val symbolCustomEmojiId: String,

	@SerialName("light_theme_main_color")
	val lightThemeMainColor: Int,

	@SerialName("light_theme_other_colors")
	val lightThemeOtherColors: List<Int>,

	@SerialName("dark_theme_main_color")
	val darkThemeMainColor: Int,

	@SerialName("dark_theme_other_colors")
	val darkThemeOtherColors: List<Int>,
)
