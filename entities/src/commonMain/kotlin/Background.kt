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
 * This object represents a chat background.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#chatbackground)
 */
@Serializable
data class ChatBackground(
	val type: BackgroundType,
)

/**
 * Describes the type of a background.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#backgroundtype)
 */
@Serializable
sealed class BackgroundType {

	/**
	 * The background is automatically filled based on the selected colors.
	 */
	@Serializable
	@SerialName("fill")
	data class Fill(
		val fill: BackgroundFill,

		@SerialName("dark_theme_dimming")
		val darkThemeDimming: Int,
	) : BackgroundType()

	/**
	 * The background is a wallpaper in the JPEG format.
	 */
	@Serializable
	@SerialName("wallpaper")
	data class Wallpaper(
		val document: Document,

		@SerialName("dark_theme_dimming")
		val darkThemeDimming: Int,

		@SerialName("is_blurred")
		val isBlurred: Boolean = false,

		@SerialName("is_moving")
		val isMoving: Boolean = false,
	) : BackgroundType()

	/**
	 * The background is a .PNG or .TGV pattern combined with a background fill.
	 */
	@Serializable
	@SerialName("pattern")
	data class Pattern(
		val document: Document,
		val fill: BackgroundFill,
		val intensity: Int,

		@SerialName("is_inverted")
		val isInverted: Boolean = false,

		@SerialName("is_moving")
		val isMoving: Boolean = false,
	) : BackgroundType()

	/**
	 * The background is taken directly from a built-in chat theme.
	 */
	@Serializable
	@SerialName("chat_theme")
	data class ChatTheme(
		@SerialName("theme_name")
		val themeName: String,
	) : BackgroundType()
}

/**
 * Describes the fill used in a background.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#backgroundfill)
 */
@Serializable
sealed class BackgroundFill {

	/** Solid color fill. */
	@Serializable
	@SerialName("solid")
	data class Solid(
		val color: Int,
	) : BackgroundFill()

	/** Two-color gradient fill. */
	@Serializable
	@SerialName("gradient")
	data class Gradient(
		@SerialName("top_color")
		val topColor: Int,

		@SerialName("bottom_color")
		val bottomColor: Int,

		@SerialName("rotation_angle")
		val rotationAngle: Int,
	) : BackgroundFill()

	/** Freeform gradient fill with 3 or 4 colors. */
	@Serializable
	@SerialName("freeform_gradient")
	data class FreeformGradient(
		val colors: List<Int>,
	) : BackgroundFill()
}
