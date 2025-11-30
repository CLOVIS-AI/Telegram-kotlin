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
import kotlin.jvm.JvmInline

/**
 * This object represents a Telegram user or bot.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#user)
 */
@Serializable
data class User(
	val id: Id,

	@SerialName("is_bot")
	val isBot: Boolean,

	@SerialName("first_name")
	val firstName: String,

	@SerialName("last_name")
	val lastName: String?,

	@SerialName("username")
	val username: String?,

	@SerialName("language_code")
	val languageCode: LanguageCode?,

	@SerialName("is_premium")
	val isPremium: Boolean = false,

	@SerialName("added_to_attachment_menu")
	val addedToAttachmentMenu: Boolean = false,

	@SerialName("can_join_groups")
	val canJoinGroups: Boolean?,

	@SerialName("can_read_all_group_messages")
	val canReadAllGroupMessages: Boolean?,

	@SerialName("supports_inline_queries")
	val supportsInlineQueries: Boolean?,

	@SerialName("can_connect_to_business")
	val canConnectToBusiness: Boolean?,

	@SerialName("has_main_web_app")
	val hasMainWebApp: Boolean?,
) {

	@Serializable
	@JvmInline
	value class Id(val value: Long)
}

/**
 * Describes the birthdate of a user.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#birthdate)
 */
@Serializable
data class BirthDate(
	/**
	 * Day of the user's birth; 1-31
	 */
	val day: Int,
	/**
	 * Month of the user's birth; 1-12
	 */
	val month: Int,
	/**
	 * Year of the user's birth
	 */
	val year: Int?,
)

/**
 * This object represents a phone contact.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#contact)
 */
@Serializable
data class Contact(
	@SerialName("phone_number")
	val phoneNumber: String,

	@SerialName("first_name")
	val firstName: String,

	@SerialName("last_name")
	val lastName: String?,

	@SerialName("user_id")
	val user: User.Id?,

	val vcard: String?,
)

/**
 * This object contains information about a user that was shared with the bot using a KeyboardButtonRequestUsers button.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#shareduser)
 */
@Serializable
data class SharedUser(
	@SerialName("user_id")
	val userId: User.Id,

	@SerialName("first_name")
	val firstName: String?,

	@SerialName("last_name")
	val lastName: String?,

	val username: String?,

	val photo: List<PhotoSize>?,
)
