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

@Serializable
data class Update(
	@SerialName("update_id")
	val id: Id,

	val message: Message?,

	@SerialName("edited_message")
	val editedMessage: Message?,

	@SerialName("channel_post")
	val channelPost: Message?,

	@SerialName("edited_channel_post")
	val editedChannelPost: Message?,
) {

	@Serializable
	@JvmInline
	value class Id(val value: Long)
}

@Serializable
data class Message(
	@SerialName("message_id")
	val id: Id,

	val from: User?,

	@SerialName("sender_chat")
	val senderChat: Chat?,

	val chat: Chat,

	@SerialName("reply_to_message")
	val replyTo: Message?,

	val text: String?,

	val entities: List<MessageEntity>? = emptyList(),
) {

	/**
	 * Returns the text contained by the given [entity].
	 */
	fun text(entity: MessageEntity): String? =
		text?.substring(entity.range)

	@Serializable
	@JvmInline
	value class Id(val value: Long)
}
