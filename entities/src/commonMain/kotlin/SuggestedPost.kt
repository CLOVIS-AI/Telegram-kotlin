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
 * Describes a service message about the approval of a suggested post.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#suggestedpostapproved)
 */
@Serializable
data class SuggestedPostApproved(
	/** Message containing the suggested post. */
	@SerialName("suggested_post_message")
	val suggestedPostMessage: Message?,

	/** Amount paid for the post. */
	val price: SuggestedPostPrice?,

	/** Date when the post will be published. */
	@SerialName("send_date")
	val sendDate: @Serializable(with = UnixSecondsSerializer::class) Instant,
)

/**
 * Describes the price of a suggested post.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#suggestedpostprice)
 */
@Serializable
data class SuggestedPostPrice(
	/** Currency in which the post will be paid. Currently, one of "XTR" (Telegram Stars) or "TON" (toncoins). */
	val currency: String,

	/**
	 * The amount of the currency to be paid in the smallest units (Stars or nanotoncoins).
	 */
	val amount: Long,
)

/**
 * Contains information about a suggested post.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#suggestedpostinfo)
 */
@Serializable
data class SuggestedPostInfo(
	/** State of the suggested post. Currently: "pending", "approved", or "declined". */
	val state: State,

	/** Proposed price of the post. If omitted, the post is unpaid. */
	val price: SuggestedPostPrice?,

	/** Proposed send date of the post. If omitted, it can be published at any time within 30 days. */
	@SerialName("send_date")
	val sendDate: @Serializable(with = UnixSecondsSerializer::class) Instant?,
) {

	/** State of the suggested post. */
	@Serializable
	enum class State {
		@SerialName("pending")
		Pending,

		@SerialName("approved")
		Approved,

		@SerialName("declined")
		Declined,
	}
}

/**
 * Describes a service message about the failed approval of a suggested post.
 * Currently, only caused by insufficient user funds at the time of approval.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#suggestedpostapprovalfailed)
 */
@Serializable
data class SuggestedPostApprovalFailed(
	/** Message containing the suggested post whose approval has failed. */
	@SerialName("suggested_post_message")
	val suggestedPostMessage: Message?,

	/** Expected price of the post. */
	val price: SuggestedPostPrice,
)

/**
 * Describes a service message about the rejection of a suggested post.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#suggestedpostdeclined)
 */
@Serializable
data class SuggestedPostDeclined(
	/** Message containing the suggested post. */
	@SerialName("suggested_post_message")
	val suggestedPostMessage: Message?,

	/** Comment with which the post was declined. */
	val comment: String?,
)

/**
 * Describes an amount of Telegram Stars.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#staramount)
 */
@Serializable
data class StarAmount(
	/** Integer amount of Telegram Stars; can be negative. */
	val amount: Int,

	/** The number of 1/1_000_000_000 shares of Telegram Stars; may be negative if and only if [amount] is non-positive. */
	@SerialName("nanostar_amount")
	val nanoStarAmount: Int?,
)

/**
 * Describes a service message about a successful payment for a suggested post.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#suggestedpostpaid)
 */
@Serializable
data class SuggestedPostPaid(
	/** Message containing the suggested post. */
	@SerialName("suggested_post_message")
	val suggestedPostMessage: Message?,

	/** Currency in which the payment was made; one of "XTR" (Stars) or "TON" (toncoins). */
	val currency: String,

	/** Amount received by the channel in nanotoncoins; for toncoin payments only. */
	val amount: Long?,

	/** Amount of Telegram Stars received by the channel; for Star payments only. */
	@SerialName("star_amount")
	val starAmount: StarAmount?,
)

/**
 * Describes a service message about a payment refund for a suggested post.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#suggestedpostrefunded)
 */
@Serializable
data class SuggestedPostRefunded(
	/** Message containing the suggested post. */
	@SerialName("suggested_post_message")
	val suggestedPostMessage: Message?,

	/** Reason for the refund. */
	val reason: Reason,
) {

	/**
	 * Reason for a suggested post refund.
	 */
	@Serializable
	enum class Reason {
		@SerialName("post_deleted")
		PostDeleted,

		@SerialName("payment_refunded")
		PaymentRefunded,
	}
}
