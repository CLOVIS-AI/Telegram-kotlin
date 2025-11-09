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

@Serializable
data class Sticker(
	@SerialName("file_id")
	override val id: Media.Id,

	@SerialName("file_unique_id")
	override val uniqueId: Media.UniqueId,

	val type: Type,

	val width: Int,

	val height: Int,

	@SerialName("is_animated")
	val isAnimated: Boolean = false,

	@SerialName("is_video")
	val isVideo: Boolean = false,

	val thumbnail: List<PhotoSize>?,

	val emoji: String?,

	@SerialName("set_name")
	val setName: String?,

	@SerialName("premium_animation")
	val premiumAnimation: File?,

	@SerialName("mask_position")
	val maskPosition: MaskPosition?,

	@SerialName("custom_emoji_id")
	val customEmojiId: String?,

	@SerialName("needs_repainting")
	val needsRepainting: Boolean = false,

	@SerialName("file_size")
	val fileSize: Int?,
) : Media {

	enum class Type {
		@SerialName("regular")
		Regular,

		@SerialName("mask")
		Mask,

		@SerialName("custom_emoji")
		CustomEmoji,
	}
}

/**
 * This object describes the position on faces where a mask should be placed by default.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#maskposition)
 */
@Serializable
data class MaskPosition(
	val point: String,

	@SerialName("x_shift")
	val xShift: Float,

	@SerialName("y_shift")
	val yShift: Float,

	val scale: Float,
)
