/*
 * Copyright (c) 2026, OpenSavvy and contributors.
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

package opensavvy.telegram.sdk

import opensavvy.telegram.entity.*

/**
 * Type used as a receiver in [BotRouter] handlers.
 */
interface BotContext {

	/**
	 * The bot which made the request.
	 */
	val bot: TelegramBot

	/**
	 * Convenience function to reply to a message.
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * bot.poll {
	 *     command("/ping") {
	 *         it.reply("Pong!")
	 *     }
	 * }
	 * ```
	 * is the same as:
	 * ```kotlin
	 * bot.poll {
	 *     command("/ping") {
	 *         bot.sendMessage(
	 *             chat = it.chat.id,
	 *             text = "Pong!",
	 *             reply = ReplyParameters(it.id),
	 *         )
	 *     }
	 * }
	 * ```
	 *
	 * @see TelegramBot.sendMessage
	 */
	@IgnorableReturnValue
	suspend fun Message.reply(
		text: String,
		topic: String? = null,
		parseMode: String? = null,
		entities: List<MessageEntity> = emptyList(),
		linkPreviewOptions: LinkPreviewOptions? = null,
		disableNotifications: Boolean = false,
		protectContent: Boolean? = null,
		allowPaidBroadcast: Boolean? = null,
		messageEffectId: String? = null,
		suggestedPostParameters: SuggestedPostParameters? = null,
		replyMarkup: NewMessageKeyboardMarkup? = null,
		businessConnectionId: BusinessConnection.Id? = null,
	): Message {
		return bot.sendMessage(
			chat = this.chat.id,
			text = text,
			topic = topic,
			parseMode = parseMode,
			entities = entities,
			linkPreviewOptions = linkPreviewOptions,
			disableNotifications = disableNotifications,
			protectContent = protectContent,
			allowPaidBroadcast = allowPaidBroadcast,
			messageEffectId = messageEffectId,
			suggestedPostParameters = suggestedPostParameters,
			reply = ReplyParameters(this.id, ReplyParameters.ChatIdentifier.Id(this.chat.id)),
			replyMarkup = replyMarkup,
			thread = this.messageThreadId?.toLong()?.let(Message::Id),
			businessConnectionId = businessConnectionId,
		)
	}

}
