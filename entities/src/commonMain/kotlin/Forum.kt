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
 * This object represents a service message about a new forum topic created in the chat.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#forumtopiccreated)
 */
@Serializable
data class ForumTopicCreated(
	/** Name of the topic. */
	val name: String,

	/** Color of the topic icon in RGB format. */
	@SerialName("icon_color")
	val iconColor: Int,

	/**
	 * Unique identifier of the custom emoji shown as the topic icon.
	 * Optional in the Bot API; `null` means the topic icon is not a custom emoji.
	 */
	@SerialName("icon_custom_emoji_id")
	val iconCustomEmojiId: String?,
)

/**
 * This object represents a service message about an edited forum topic.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#forumtopicedited)
 */
@Serializable
data class ForumTopicEdited(
	/** New name of the topic, if it was edited. */
	val name: String?,

	/**
	 * New identifier of the custom emoji shown as the topic icon, if it was edited;
	 * an empty string if the icon was removed.
	 */
	@SerialName("icon_custom_emoji_id")
	val iconCustomEmojiId: String?,
)

/**
 * This object represents a service message about a forum topic closed in the chat.
 * Currently holds no information.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#forumtopicclosed)
 */
@Serializable
class ForumTopicClosed

/**
 * This object represents a service message about a forum topic reopened in the chat.
 * Currently holds no information.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#forumtopicreopened)
 */
@Serializable
class ForumTopicReopened

/**
 * This object represents a service message about General forum topic hidden in the chat.
 * Currently holds no information.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#generalforumtopichidden)
 */
@Serializable
class GeneralForumTopicHidden

/**
 * This object represents a service message about General forum topic unhidden in the chat.
 * Currently holds no information.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#generalforumtopicunhidden)
 */
@Serializable
class GeneralForumTopicUnhidden
