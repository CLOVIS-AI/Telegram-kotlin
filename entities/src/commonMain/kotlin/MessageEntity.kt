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

/**
 * This object represents one special entity in a text message. For example, hashtags, usernames, URLs, etc.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#messageentity)
 */
@Serializable
sealed class MessageEntity {

	abstract val offset: Int

	abstract val length: Int

	val range: IntRange
		get() = offset .. (offset + length)

	/**
	 * Example: `@user`.
	 */
	@Serializable
	@SerialName("mention")
	data class Mention(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: `#foo` or `#foo@chat`.
	 */
	@Serializable
	@SerialName("hashtag")
	data class Hashtag(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: `$USD` or `$USD@chat`.
	 */
	@Serializable
	@SerialName("cashtag")
	data class Cashtag(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: `/start` or `/start@foobot`.
	 */
	@Serializable
	@SerialName("bot_command")
	data class BotCommand(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: `https://telegram.org`.
	 */
	@Serializable
	@SerialName("url")
	data class Url(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: `foo@bar.org`.
	 */
	@Serializable
	@SerialName("email")
	data class Email(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: `+1-212-555-8123`.
	 */
	@Serializable
	@SerialName("phone_number")
	data class PhoneNumber(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: **bold text**.
	 */
	@Serializable
	@SerialName("bold")
	data class Bold(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: _italic text_.
	 */
	@Serializable
	@SerialName("italic")
	data class Italic(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: <ins>underline text</ins>.
	 */
	@Serializable
	@SerialName("underline")
	data class Underline(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: ~~strikethrough text~~.
	 */
	@Serializable
	@SerialName("strikethrough")
	data class Strikethrough(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: spoiler text.
	 */
	@Serializable
	@SerialName("spoiler")
	data class Spoiler(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	@Serializable
	@SerialName("blockquote")
	data class BlockQuote(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	@Serializable
	@SerialName("expandable_blockquote")
	data class ExpandableBlockQuote(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: `mono text` (inline).
	 */
	@Serializable
	@SerialName("code")
	data class Code(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: `mono text` (multiline).
	 */
	@Serializable
	@SerialName("pre")
	data class Pre(
		override val offset: Int,
		override val length: Int,
		@SerialName("language")
		val programmingLanguage: String?,
	) : MessageEntity()

	/**
	 * Example: [test](https://telegram.org).
	 */
	@Serializable
	@SerialName("text_link")
	data class TextLink(
		override val offset: Int,
		override val length: Int,
		val url: String,
	) : MessageEntity()

	/**
	 * Example: referring to a user that doesn't have a username.
	 */
	@Serializable
	@SerialName("text_mention")
	data class TextMention(
		override val offset: Int,
		override val length: Int,
		val user: User,
	) : MessageEntity()

	@Serializable
	@SerialName("custom_emoji")
	data class CustomEmoji(
		override val offset: Int,
		override val length: Int,
		@SerialName("custom_emoji_id")
		val emojiId: String,
	) : MessageEntity()
}
