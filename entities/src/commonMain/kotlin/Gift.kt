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
 * This object describes the types of gifts that can be gifted to a user or a chat.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#acceptedgifttypes)
 */
@Serializable
data class AcceptedGiftTypes(
	@SerialName("unlimited_gifts")
	val unlimitedGifts: Boolean?,

	@SerialName("limited_gifts")
	val limitedGifts: Boolean?,

	@SerialName("unique_gifts")
	val uniqueGifts: Boolean?,

	@SerialName("premium_subscription")
	val premiumSubscription: Boolean?,
)
