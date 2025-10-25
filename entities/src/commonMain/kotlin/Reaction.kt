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
 * This object describes the type of reaction.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#reactiontype)
 */
@Serializable
sealed class ReactionType {

	/**
	 * The reaction is based on an emoji.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#reactiontypeemoji)
	 */
	@Serializable
	@SerialName("emoji")
	data class Emoji(
		val emoji: String,
	) : ReactionType()

	/**
	 * The reaction is based on an emoji.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#reactiontypeemoji)
	 */
	@Serializable
	@SerialName("custom_emoji")
	data class CustomEmoji(
		@SerialName("custom_emoji_id")
		val customEmojiId: String,
	) : ReactionType()

	/**
	 * The reaction is paid.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#reactiontypepaid)
	 */
	@Serializable
	@SerialName("paid")
	data object Paid : ReactionType()
}
