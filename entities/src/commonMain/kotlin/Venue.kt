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
 * This object represents a venue.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#venue)
 */
@Serializable
data class Venue(
	val location: Location,

	val title: String,

	val address: String,

	@SerialName("foursquare_id")
	val foursquareId: String?,

	@SerialName("foursquare_type")
	val foursquareType: String?,

	@SerialName("google_place_id")
	val googlePlaceId: String?,

	@SerialName("google_place_type")
	val googlePlaceType: String?,
)
