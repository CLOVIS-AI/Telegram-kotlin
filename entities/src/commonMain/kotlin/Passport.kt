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
 * Describes Telegram Passport data shared with the bot by the user.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#passportdata)
 */
@Serializable
data class PassportData(
	/** Array with information about documents and other Telegram Passport elements that was shared with the bot. */
	val data: List<EncryptedPassportElement>,

	/** Encrypted credentials required to decrypt the data. */
	val credentials: EncryptedCredentials,
)

/**
 * Describes documents or other Telegram Passport elements shared with the bot by the user.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#encryptedpassportelement)
 */
@Serializable
sealed class EncryptedPassportElement {

	/** Base64-encoded element hash for using in PassportElementErrorUnspecified. */
	abstract val hash: String

	/**
	 * Element type: personal_details — Base64-encoded encrypted data only.
	 */
	@Serializable
	@SerialName("personal_details")
	data class PersonalDetails(
		val data: String?,
		override val hash: String,
	) : EncryptedPassportElement()

	/**
	 * Element type: passport — encrypted data + document scans.
	 */
	@Serializable
	@SerialName("passport")
	data class Passport(
		val data: String?,

		@SerialName("front_side")
		val frontSide: PassportFile?,

		val selfie: PassportFile?,

		val translation: List<PassportFile> = emptyList(),

		override val hash: String,
	) : EncryptedPassportElement()

	/**
	 * Element type: driver_license — encrypted data + front/reverse/selfie + translation.
	 */
	@Serializable
	@SerialName("driver_license")
	data class DriverLicense(
		val data: String?,

		@SerialName("front_side")
		val frontSide: PassportFile?,

		@SerialName("reverse_side")
		val reverseSide: PassportFile?,

		val selfie: PassportFile?,

		val translation: List<PassportFile> = emptyList(),

		override val hash: String,
	) : EncryptedPassportElement()

	/**
	 * Element type: identity_card — encrypted data + front/reverse/selfie + translation.
	 */
	@Serializable
	@SerialName("identity_card")
	data class IdentityCard(
		val data: String?,

		@SerialName("front_side")
		val frontSide: PassportFile?,

		@SerialName("reverse_side")
		val reverseSide: PassportFile?,

		val selfie: PassportFile?,

		val translation: List<PassportFile> = emptyList(),

		override val hash: String,
	) : EncryptedPassportElement()

	/**
	 * Element type: internal_passport — encrypted data + front/selfie + translation.
	 */
	@Serializable
	@SerialName("internal_passport")
	data class InternalPassport(
		val data: String?,

		@SerialName("front_side")
		val frontSide: PassportFile?,

		val selfie: PassportFile?,

		val translation: List<PassportFile> = emptyList(),

		override val hash: String,
	) : EncryptedPassportElement()

	/**
	 * Element type: address — encrypted data only.
	 */
	@Serializable
	@SerialName("address")
	data class Address(
		val data: String?,
		override val hash: String,
	) : EncryptedPassportElement()

	/**
	 * Element type: utility_bill — files + optional translation.
	 */
	@Serializable
	@SerialName("utility_bill")
	data class UtilityBill(
		val files: List<PassportFile> = emptyList(),
		val translation: List<PassportFile> = emptyList(),
		override val hash: String,
	) : EncryptedPassportElement()

	/**
	 * Element type: bank_statement — files + optional translation.
	 */
	@Serializable
	@SerialName("bank_statement")
	data class BankStatement(
		val files: List<PassportFile> = emptyList(),
		val translation: List<PassportFile> = emptyList(),
		override val hash: String,
	) : EncryptedPassportElement()

	/**
	 * Element type: rental_agreement — files + optional translation.
	 */
	@Serializable
	@SerialName("rental_agreement")
	data class RentalAgreement(
		val files: List<PassportFile> = emptyList(),
		val translation: List<PassportFile> = emptyList(),
		override val hash: String,
	) : EncryptedPassportElement()

	/**
	 * Element type: passport_registration — files + optional translation.
	 */
	@Serializable
	@SerialName("passport_registration")
	data class PassportRegistration(
		val files: List<PassportFile> = emptyList(),
		val translation: List<PassportFile> = emptyList(),
		override val hash: String,
	) : EncryptedPassportElement()

	/**
	 * Element type: temporary_registration — files + optional translation.
	 */
	@Serializable
	@SerialName("temporary_registration")
	data class TemporaryRegistration(
		val files: List<PassportFile> = emptyList(),
		val translation: List<PassportFile> = emptyList(),
		override val hash: String,
	) : EncryptedPassportElement()

	/**
	 * Element type: phone_number — verified phone number only.
	 */
	@Serializable
	@SerialName("phone_number")
	data class PhoneNumber(
		@SerialName("phone_number")
		val phoneNumber: String,
		override val hash: String,
	) : EncryptedPassportElement()

	/**
	 * Element type: email — verified email address only.
	 */
	@Serializable
	@SerialName("email")
	data class Email(
		val email: String,
		override val hash: String,
	) : EncryptedPassportElement()
}

/**
 * Represents a file uploaded to Telegram Passport.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#passportfile)
 */
@Serializable
data class PassportFile(
	@SerialName("file_id")
	val fileId: Media.Id,

	@SerialName("file_unique_id")
	val fileUniqueId: Media.UniqueId,

	@SerialName("file_size")
	val fileSize: Int,

	/** Unix time when the file was uploaded. */
	@SerialName("file_date")
	@Serializable(with = UnixSecondsSerializer::class)
	val fileDate: Instant,
)

/**
 * Describes data required for decrypting and authenticating [EncryptedPassportElement].
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#encryptedcredentials)
 */
@Serializable
data class EncryptedCredentials(
	/** Base64-encoded encrypted JSON-serialized data with unique user's payload, data hashes and secrets. */
	val data: String,

	/** Base64-encoded data hash for data authentication. */
	val hash: String,

	/** Base64-encoded secret, encrypted with the bot's public RSA key, required for data decryption. */
	val secret: String,
)
