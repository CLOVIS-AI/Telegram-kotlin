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
 * Contains information about the start page settings of a Telegram Business account.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#businessintro)
 */
@Serializable
data class BusinessIntro(
	val title: String?,
	val message: String?,
	val sticker: Sticker?,
)

/**
 * Contains information about the location of a Telegram Business account.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#businesslocation)
 */
@Serializable
data class BusinessLocation(
	val address: String,
	val location: Location,
)

/**
 * Describes the opening hours of a business.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#businessopeninghours)
 */
@Serializable
data class BusinessOpeningHours(
	@SerialName("time_zone_name")
	val timeZoneName: String,

	@SerialName("opening_hours")
	val openingHours: List<Interval>,
) {

	/**
	 * Describes an interval of time during which a business is open.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#businessopeninghoursinterval)
	 */
	@Serializable
	data class Interval(
		@SerialName("opening_minute")
		val openingMinute: Int,

		@SerialName("closing_minute")
		val closingMinute: Int,
	)
}
