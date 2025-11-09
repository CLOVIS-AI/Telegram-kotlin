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
import kotlin.jvm.JvmInline
import kotlin.time.Instant

/**
 * This object contains basic information about an invoice.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#invoice)
 */
@Serializable
data class Invoice(
	val title: String,
	val description: String,

	@SerialName("start_parameter")
	val startParameter: String,

	val currency: Currency,

	@SerialName("total_amount")
	val totalAmount: CurrencyAmount,
)

/**
 * Three-letter ISO 4217 [currency code](https://core.telegram.org/bots/payments#supported-currencies), or “XTR” for payments in Telegram Stars.
 */
@Serializable
@JvmInline
value class Currency(val code: String) {

	val isTelegramStars: Boolean
		get() = code == "XTR"
}

@Serializable
@JvmInline
value class CurrencyAmount(

	/**
	 * Amount in the smallest units of the currency (integer, not float/double). For example, for a price of US$ 1.45 pass amount = 145.
	 */
	val amount: Long,
)

/**
 * This object contains basic information about a successful payment. Note that if the buyer initiates a chargeback with the relevant payment provider following this transaction, the funds may be debited from your balance. This is outside of Telegram's control.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#successfulpayment)
 */
@Serializable
data class SuccessfulPayment(
	val currency: Currency,

	@SerialName("total_amount")
	val totalAmount: CurrencyAmount,

	@SerialName("invoice_payload")
	val invoicePayload: String,

	@SerialName("subscription_expiration_date")
	val subscriptionExpirationDate: @Serializable(with = UnixSecondsSerializer::class) Instant?,

	@SerialName("is_recurring")
	val isRecurring: Boolean = false,

	@SerialName("is_first_recurring")
	val isFirstRecurring: Boolean = false,

	@SerialName("shipping_option_id")
	val shippingOptionId: String?,

	@SerialName("order_info")
	val orderInfo: OrderInfo?,

	@SerialName("telegram_payment_charge_id")
	val telegramPaymentChargeId: String,

	@SerialName("provider_payment_charge_id")
	val providerPaymentChargeId: String,
)

/**
 * This object contains basic information about a refunded payment.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#refundedpayment)
 */
@Serializable
data class RefundedPayment(
	val currency: Currency,

	@SerialName("total_amount")
	val totalAmount: CurrencyAmount,

	@SerialName("invoice_payload")
	val invoicePayload: String,

	@SerialName("telegram_payment_charge_id")
	val telegramPaymentChargeId: String,

	@SerialName("provider_payment_charge_id")
	val providerPaymentChargeId: String?,
)

/**
 * This object represents information about an order.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#orderinfo)
 */
@Serializable
data class OrderInfo(
	val name: String?,

	@SerialName("phone_number")
	val phoneNumber: String?,

	val email: String?,

	@SerialName("shipping_address")
	val shippingAddress: ShippingAddress?,
)

/**
 * This object represents a shipping address.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#shippingaddress)
 */
@Serializable
data class ShippingAddress(
	val country: CountryCode,

	val state: String?,

	val city: String,

	@SerialName("street_line1")
	val streetLine1: String,

	@SerialName("street_line2")
	val streetLine2: String,

	@SerialName("post_code")
	val postCode: String,
)
