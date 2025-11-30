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
import opensavvy.telegram.entity.serialization.DurationSecondsSerializer
import opensavvy.telegram.entity.serialization.UnixSecondsSerializer
import kotlin.time.Duration
import kotlin.time.Instant

/**
 * Represents the content of a service message, sent whenever a user in the chat triggers a proximity alert set by another user.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#proximityalerttriggered)
 */
@Serializable
data class ProximityAlertTriggered(
	val traveler: User,
	val watcher: User,
	val distance: Int,
)

/**
 * Represents a service message about a user boosting a chat.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#chatboostadded)
 */
@Serializable
data class ChatBoostAdded(
	@SerialName("boost_count")
	val boostCount: Int,
)

/**
 * Describes a service message about a change in the price of paid messages within a chat.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#paidmessagepricechanged)
 */
@Serializable
data class PaidMessagePriceChanged(
	/** The new number of Telegram Stars that must be paid by non-administrator users of the supergroup chat for each sent message. */
	@SerialName("paid_message_star_count")
	val paidMessageStarCount: Int,
)

/**
 * This object represents a service message about a video chat scheduled in the chat.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#videochatscheduled)
 */
@Serializable
data class VideoChatScheduled(
	/** Point in time when the video chat is supposed to be started by a chat administrator. */
	@SerialName("start_date")
	val startDate: @Serializable(with = UnixSecondsSerializer::class) Instant,
)

/**
 * This object represents a service message about a video chat started in the chat.
 * Currently holds no information.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#videochatstarted)
 */
@Serializable
class VideoChatStarted

/**
 * This object represents a service message about a video chat ended in the chat.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#videochatended)
 */
@Serializable
data class VideoChatEnded(
	/** Video chat duration. */
	val duration: @Serializable(with = DurationSecondsSerializer::class) Duration,
)

/**
 * This object represents a service message about new members invited to a video chat.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#videochatparticipantsinvited)
 */
@Serializable
data class VideoChatParticipantsInvited(
	/** New members that were invited to the video chat. */
	val users: List<User>,
)
