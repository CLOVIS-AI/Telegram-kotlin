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
 * Describes the position of a clickable area within a story.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#storyareaposition)
 */
@Serializable
data class StoryAreaPosition(
	@SerialName("x_percentage")
	val xPercentage: Float,

	@SerialName("y_percentage")
	val yPercentage: Float,

	@SerialName("width_percentage")
	val widthPercentage: Float,

	@SerialName("height_percentage")
	val heightPercentage: Float,

	@SerialName("rotation_angle")
	val rotationAngle: Float,

	@SerialName("corner_radius_percentage")
	val cornerRadiusPercentage: Float,
)

/**
 * Describes the physical address of a location.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#locationaddress)
 */
@Serializable
data class LocationAddress(
	@SerialName("country_code")
	val countryCode: String,

	val state: String?,

	val city: String?,

	val street: String?,
)

/**
 * Describes the type of a clickable area on a story.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#storyareatype)
 */
@Serializable
sealed class StoryAreaType {

	/**
	 * Describes a story area pointing to a location.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#storyareatypelocation)
	 */
	@Serializable
	@SerialName("location")
	data class Location(
		val latitude: Float,
		val longitude: Float,
		val address: LocationAddress?,
	) : StoryAreaType()

	/**
	 * Describes a story area pointing to a suggested reaction.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#storyareatypesuggestedreaction)
	 */
	@Serializable
	@SerialName("suggested_reaction")
	data class SuggestedReaction(
		@SerialName("reaction_type")
		val reactionType: ReactionType,

		@SerialName("is_dark")
		val isDark: Boolean = false,

		@SerialName("is_flipped")
		val isFlipped: Boolean = false,
	) : StoryAreaType()

	/**
	 * Describes a story area pointing to an HTTP or tg:// link.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#storyareatypelink)
	 */
	@Serializable
	@SerialName("link")
	data class Link(
		val url: String,
	) : StoryAreaType()

	/**
	 * Describes a story area containing weather information.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#storyareatypeweather)
	 */
	@Serializable
	@SerialName("weather")
	data class Weather(
		val temperature: Float,
		val emoji: String,
		@SerialName("background_color")
		val backgroundColor: Int,
	) : StoryAreaType()

	/**
	 * Describes a story area pointing to a unique gift.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#storyareatypeuniquegift)
	 */
	@Serializable
	@SerialName("unique_gift")
	data class UniqueGift(
		val name: String,
	) : StoryAreaType()
}

/**
 * Describes a clickable area on a story media.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#storyarea)
 */
@Serializable
data class StoryArea(
	val position: StoryAreaPosition,
	val type: StoryAreaType,
)
