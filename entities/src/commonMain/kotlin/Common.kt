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
 * [IETF language tag](https://en.wikipedia.org/wiki/IETF_language_tag).
 */
@Serializable
@JvmInline
value class LanguageCode(val code: String)

/**
 * Two-letter [ISO 3166-1 alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) country code
 */
@Serializable
@JvmInline
value class CountryCode(val code: String)

/**
 * Accent colors.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#accent-colors)
 */
@Serializable
@JvmInline
value class AccentColor(val id: Int)

/**
 * This object represents a point on the map.
 *
 * ### External resources
 *
 * - [Official resources](https://core.telegram.org/bots/api#location)
 */
@Serializable
data class Location(
	val latitude: Float,
	val longitude: Float,

	@SerialName("horizontal_accuracy")
	val horizontalAccuracy: Float?,

	@SerialName("live_period")
	val livePeriod: Int?,

	val heading: Int?,

	@SerialName("proximity_alert_radius")
	val proximityAlertRadius: Int?,
)

@Serializable
@JvmInline
value class Rarity(val permille: Int) {

	val percent: Int get() = permille / 10
}
