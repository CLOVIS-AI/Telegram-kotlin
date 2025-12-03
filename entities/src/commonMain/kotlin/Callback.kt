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
 * This object represents an incoming callback query from a callback button in an inline keyboard.
 * If the button that originated the query was attached to a message sent by the bot, the field [message] will be present.
 * If the button was attached to a message sent via the bot (in inline mode), the field [inlineMessageId] will be present.
 * Exactly one of the fields [data] or [gameShortName] will be present.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#callbackquery)
 */
@Serializable
data class CallbackQuery(
	/** Unique identifier for this query. */
	val id: String,

	/** Sender. */
	@SerialName("from")
	val user: User,

	/** Message sent by the bot with the callback button that originated the query. */
	val message: MayBeInaccessibleMessage? = null,

	/** Identifier of the message sent via the bot in inline mode, that originated the query. */
	@SerialName("inline_message_id")
	val inlineMessageId: String? = null,

	/** Global identifier, uniquely corresponding to the chat to which the message with the callback button was sent. */
	@SerialName("chat_instance")
	val chatInstance: String,

	/** Data associated with the callback button. */
	val data: String? = null,

	/** Short name of a Game to be returned, serves as the unique identifier for the game. */
	@SerialName("game_short_name")
	val gameShortName: String? = null,
)
