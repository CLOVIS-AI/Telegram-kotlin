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

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import opensavvy.telegram.entity.serialization.DurationSecondsSerializer
import opensavvy.telegram.entity.serialization.TelegramJson
import opensavvy.telegram.entity.serialization.UnixSecondsSerializer
import kotlin.jvm.JvmInline
import kotlin.time.Duration
import kotlin.time.Instant

/**
 * This object describes a message that can be inaccessible to the bot.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#maybeinaccessiblemessage)
 */
@Serializable(with = MayBeInaccessibleMessage.Serializer::class)
sealed interface MayBeInaccessibleMessage {

	object Serializer : KSerializer<MayBeInaccessibleMessage> {
		override val descriptor: SerialDescriptor
			get() = SerialDescriptor("opensavvy.telegram.entity.MayBeInaccessibleMessage", JsonObject.serializer().descriptor)

		override fun serialize(encoder: Encoder, value: MayBeInaccessibleMessage) {
			val json = when (value) {
				is InaccessibleMessage -> {
					val result = TelegramJson.encodeToJsonElement(value) as JsonObject
					JsonObject(result + ("date" to JsonPrimitive(0)))
				}

				is Message -> TelegramJson.encodeToJsonElement(value) as JsonObject
			}

			encoder.encodeSerializableValue(JsonObject.serializer(), json)
		}

		override fun deserialize(decoder: Decoder): MayBeInaccessibleMessage {
			val json = decoder.decodeSerializableValue(JsonObject.serializer())

			return if (json["date"] == JsonPrimitive(0)) {
				TelegramJson.decodeFromJsonElement<InaccessibleMessage>(json)
			} else {
				TelegramJson.decodeFromJsonElement<Message>(json)
			}
		}
	}
}

/**
 * This object represents a unique message identifier.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#messageid)
 */
@Serializable
data class MessageId(
	@SerialName("message_id")
	val id: Message.Id,
)

/**
 * This object represents a message.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#message)
 */
@Serializable
data class Message(
	@SerialName("message_id")
	val id: Id,

	@SerialName("message_thread_id")
	val messageThreadId: Int?,

	@SerialName("direct_messages_topic")
	val directMessagesTopic: DirectMessagesTopic?,

	val from: User?,

	@SerialName("sender_chat")
	val senderChat: Chat?,

	@SerialName("sender_boost_count")
	val senderBoostCount: Int?,

	@SerialName("sender_business_bot")
	val senderBusinessBot: User?,

	@Serializable(with = UnixSecondsSerializer::class)
	val date: Instant,

	@SerialName("business_connection_id")
	val businessConnectionId: String?,

	val chat: Chat,

	@SerialName("forward_origin")
	val forwardOrigin: MessageOrigin?,

	@SerialName("is_topic_message")
	val isTopicMessage: Boolean = false,

	@SerialName("is_automatic_forward")
	val isAutomaticForward: Boolean = false,

	@SerialName("reply_to_message")
	val replyTo: Message?,

	@SerialName("external_reply")
	val externalReply: ExternalReplyInfo?,

	/** For replies that quote part of the original message, the quoted part of the message. */
	@SerialName("quote")
	val quote: TextQuote?,

	/** For replies to a story, the original story. */
	@SerialName("reply_to_story")
	val replyToStory: Story?,

	/** Identifier of the specific checklist task that is being replied to. */
	@SerialName("reply_to_checklist_task_id")
	val replyToChecklistTaskId: Int?,

	/** Bot through which the message was sent. */
	@SerialName("via_bot")
	val viaBot: User?,

	/** Date the message was last edited in Unix time. */
	@SerialName("edit_date")
	@Serializable(with = UnixSecondsSerializer::class)
	val editDate: Instant?,

	/** True, if the message can't be forwarded. */
	@SerialName("has_protected_content")
	val hasProtectedContent: Boolean = false,

	/** True, if the message was sent by an implicit action (away/greeting/scheduled). */
	@SerialName("is_from_offline")
	val isFromOffline: Boolean = false,

	/** True, if the message is a paid post. */
	@SerialName("is_paid_post")
	val isPaidPost: Boolean = false,

	/** The unique identifier of a media message group this message belongs to. */
	@SerialName("media_group_id")
	val mediaGroupId: String?,

	/** Signature of the post author for messages in channels, or the custom title of an anonymous group administrator. */
	@SerialName("author_signature")
	val authorSignature: String?,

	/** The number of Telegram Stars that were paid by the sender of the message to send it. */
	@SerialName("paid_star_count")
	val paidStarCount: Int?,

	val text: String?,

	val entities: List<MessageEntity>? = emptyList(),

	@SerialName("link_preview_options")
	val linkPreviewOptions: LinkPreviewOptions?,

	/** Information about suggested post parameters if the message is a suggested post in a channel direct messages chat. */
	@SerialName("suggested_post_info")
	val suggestedPostInfo: SuggestedPostInfo?,

	@SerialName("effect_id")
	val effectId: String?,

	val animation: Animation?,

	val audio: Audio?,

	val document: Document?,

	@SerialName("paid_media")
	val paidMedia: PaidMediaInfo?,

	val photo: List<PhotoSize>?,

	val sticker: Sticker?,

	val story: Story?,

	val video: Video?,

	@SerialName("video_note")
	val videoNote: VideoNote?,

	val voice: Voice?,

	val caption: String?,

	@SerialName("caption_entities")
	val captionEntities: List<MessageEntity>? = emptyList(),

	@SerialName("show_caption_above_media")
	val showCaptionAboveMedia: Boolean = false,

	@SerialName("has_media_spoiler")
	val hasMediaSpoiler: Boolean = false,

	val checklist: Checklist?,

	val contact: Contact?,

	val dice: Dice?,

	val game: Game?,

	val poll: Poll?,

	val venue: Venue?,

	val location: Location?,

	@SerialName("new_chat_members")
	val newChatMembers: List<User> = emptyList(),

	@SerialName("left_chat_member")
	val leftChatMember: User?,

	@SerialName("new_chat_title")
	val newChatTitle: String?,

	@SerialName("new_chat_photo")
	val newChatPhoto: List<PhotoSize> = emptyList(),

	@SerialName("delete_chat_photo")
	val deletedChatPhoto: Boolean = false,

	@SerialName("group_chat_created")
	val groupChatCreated: Boolean = false,

	@SerialName("supergroup_chat_created")
	val superGroupChatCreated: Boolean = false,

	@SerialName("channel_chat_created")
	val channelChatCreated: Boolean = false,

	@SerialName("message_auto_delete_timer_changed")
	val messageAutoDeleteTimerChanged: MessageAutoDeleteTimerChanged?,

	@SerialName("migrate_to_chat_id")
	val migrateToChat: Chat.Id?,

	@SerialName("migrate_from_chat_id")
	val migrateFromChat: Chat.Id?,

	@SerialName("pinned_message")
	val pinnedMessage: MayBeInaccessibleMessage?,

	val invoice: Invoice?,

	@SerialName("successful_payment")
	val successfulPayment: SuccessfulPayment?,

	@SerialName("refunded_payment")
	val refundedPayment: RefundedPayment?,

	@SerialName("users_shared")
	val usersShared: UsersShared?,

	@SerialName("chat_shared")
	val chatShared: ChatShared?,

	val gift: GiftInfo?,

	@SerialName("unique_gift")
	val uniqueGift: UniqueGiftInfo?,

	@SerialName("connected_website")
	val connectedWebsite: String?,

	@SerialName("write_access_allowed")
	val writeAccess: WriteAccessAllowed?,

	@SerialName("passport_data")
	val passportData: PassportData?,

	@SerialName("proximity_alert_triggered")
	val proximityAlertTriggered: ProximityAlertTriggered?,

	@SerialName("boost_added")
	val boostAdded: ChatBoostAdded?,

	@SerialName("chat_background_set")
	val chatBackgroundSet: ChatBackground?,

	@SerialName("checklist_tasks_done")
	val checklistTasksDone: ChecklistTasksDone?,

	@SerialName("checklist_tasks_added")
	val checklistTasksAdded: ChecklistTasksAdded?,

	@SerialName("direct_message_price_changed")
	val directMessagePriceChanged: DirectMessagePriceChanged?,

	@SerialName("forum_topic_created")
	val forumTopicCreated: ForumTopicCreated?,

	@SerialName("forum_topic_edited")
	val forumTopicEdited: ForumTopicEdited?,

	@SerialName("forum_topic_closed")
	val forumTopicClosed: ForumTopicClosed?,

	@SerialName("forum_topic_reopened")
	val forumTopicReopened: ForumTopicReopened?,

	@SerialName("general_forum_topic_hidden")
	val generalForumTopicHidden: GeneralForumTopicHidden?,

	@SerialName("general_forum_topic_unhidden")
	val generalForumTopicUnhidden: GeneralForumTopicUnhidden?,

	@SerialName("giveaway_created")
	val giveawayCreated: GiveawayCreated?,

	val giveaway: Giveaway?,

	@SerialName("giveaway_winners")
	val giveawayWinners: GiveawayWinners?,

	@SerialName("giveaway_completed")
	val giveawayCompleted: GiveawayCompleted?,

	@SerialName("paid_message_price_changed")
	val paidMessagePriceChanged: PaidMessagePriceChanged?,

	@SerialName("suggested_post_approved")
	val suggestedPostApproved: SuggestedPostApproved?,

	@SerialName("suggested_post_approval_failed")
	val suggestedPostApprovalFailed: SuggestedPostApprovalFailed?,

	@SerialName("suggested_post_declined")
	val suggestedPostDeclined: SuggestedPostDeclined?,

	@SerialName("suggested_post_paid")
	val suggestedPostPaid: SuggestedPostPaid?,

	@SerialName("suggested_post_refunded")
	val suggestedPostRefunded: SuggestedPostRefunded?,

	@SerialName("video_chat_scheduled")
	val videoChatScheduled: VideoChatScheduled?,

	@SerialName("video_chat_started")
	val videoChatStarted: VideoChatStarted?,

	@SerialName("video_chat_ended")
	val videoChatEnded: VideoChatEnded?,

	@SerialName("video_chat_participants_invited")
	val videoChatParticipantsInvited: VideoChatParticipantsInvited?,

	@SerialName("web_app_data")
	val webAppData: WebAppData?,

	@SerialName("reply_markup")
	val replyMarkup: InlineKeyboardMarkup?,
) : MayBeInaccessibleMessage {

	/**
	 * Returns the text contained by the given [entity].
	 */
	fun text(entity: MessageEntity): String? =
		text?.substring(entity.range)

	@Serializable
	@JvmInline
	value class Id(val value: Long)
}

/**
 * This object represents one special entity in a text message. For example, hashtags, usernames, URLs, etc.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#messageentity)
 */
@Serializable
sealed class MessageEntity {

	abstract val offset: Int

	abstract val length: Int

	val range: IntRange
		get() = offset..(offset + length)

	/**
	 * Example: `@user`.
	 */
	@Serializable
	@SerialName("mention")
	data class Mention(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: `#foo` or `#foo@chat`.
	 */
	@Serializable
	@SerialName("hashtag")
	data class Hashtag(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: `$USD` or `$USD@chat`.
	 */
	@Serializable
	@SerialName("cashtag")
	data class Cashtag(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: `/start` or `/start@foobot`.
	 */
	@Serializable
	@SerialName("bot_command")
	data class BotCommand(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: `https://telegram.org`.
	 */
	@Serializable
	@SerialName("url")
	data class Url(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: `foo@bar.org`.
	 */
	@Serializable
	@SerialName("email")
	data class Email(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: `+1-212-555-8123`.
	 */
	@Serializable
	@SerialName("phone_number")
	data class PhoneNumber(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: **bold text**.
	 */
	@Serializable
	@SerialName("bold")
	data class Bold(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: _italic text_.
	 */
	@Serializable
	@SerialName("italic")
	data class Italic(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: <ins>underline text</ins>.
	 */
	@Serializable
	@SerialName("underline")
	data class Underline(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: ~~strikethrough text~~.
	 */
	@Serializable
	@SerialName("strikethrough")
	data class Strikethrough(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: spoiler text.
	 */
	@Serializable
	@SerialName("spoiler")
	data class Spoiler(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	@Serializable
	@SerialName("blockquote")
	data class BlockQuote(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	@Serializable
	@SerialName("expandable_blockquote")
	data class ExpandableBlockQuote(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: `mono text` (inline).
	 */
	@Serializable
	@SerialName("code")
	data class Code(
		override val offset: Int,
		override val length: Int,
	) : MessageEntity()

	/**
	 * Example: `mono text` (multiline).
	 */
	@Serializable
	@SerialName("pre")
	data class Pre(
		override val offset: Int,
		override val length: Int,
		@SerialName("language")
		val programmingLanguage: String?,
	) : MessageEntity()

	/**
	 * Example: [test](https://telegram.org).
	 */
	@Serializable
	@SerialName("text_link")
	data class TextLink(
		override val offset: Int,
		override val length: Int,
		val url: String,
	) : MessageEntity()

	/**
	 * Example: referring to a user that doesn't have a username.
	 */
	@Serializable
	@SerialName("text_mention")
	data class TextMention(
		override val offset: Int,
		override val length: Int,
		val user: User,
	) : MessageEntity()

	@Serializable
	@SerialName("custom_emoji")
	data class CustomEmoji(
		override val offset: Int,
		override val length: Int,
		@SerialName("custom_emoji_id")
		val emojiId: String,
	) : MessageEntity()
}

/**
 * This object contains information about the quoted part of a message that is replied to by the given message.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#textquote)
 */
@Serializable
data class TextQuote(
	/** Text of the quoted part of a message that is replied to by the given message. */
	val text: String,

	/**
	 * Optional. Special entities that appear in the quote. Currently, only bold, italic, underline,
	 * strikethrough, spoiler, and custom_emoji entities are kept in quotes.
	 */
	val entities: List<MessageEntity>? = emptyList(),

	/** Approximate quote position in the original message in UTF-16 code units as specified by the sender. */
	val position: Int,

	/** True, if the quote was chosen manually by the message sender. Otherwise, the quote was added automatically by the server. */
	@SerialName("is_manual")
	val isManual: Boolean = false,
)

/**
 * Describes a topic of a direct messages chat.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#directmessagestopic)
 */
@Serializable
data class DirectMessagesTopic(
	@SerialName("topic_id")
	val id: Long,

	val user: User?,
)

/**
 * This object describes the origin of a message.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#messageorigin)
 */
@Serializable
sealed class MessageOrigin {

	abstract val date: Instant

	/**
	 * The message was originally sent by a known user.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#messageoriginuser)
	 */
	@Serializable
	@SerialName("user")
	data class User(
		@Serializable(with = UnixSecondsSerializer::class)
		override val date: Instant,

		@SerialName("sender_user")
		val sender: opensavvy.telegram.entity.User,
	) : MessageOrigin()

	/**
	 * The message was originally sent by an unknown user.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#messageoriginhiddenuser)
	 */
	@Serializable
	@SerialName("hidden_user")
	data class HiddenUser(
		@Serializable(with = UnixSecondsSerializer::class)
		override val date: Instant,

		@SerialName("sender_user_name")
		val senderUserName: String,
	) : MessageOrigin()

	/**
	 * The message was originally sent on behalf of a chat to a group chat.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#messageoriginchat)
	 */
	@Serializable
	@SerialName("chat")
	data class Chat(
		@Serializable(with = UnixSecondsSerializer::class)
		override val date: Instant,

		@SerialName("sender_chat")
		val sender: opensavvy.telegram.entity.Chat,

		@SerialName("author_signature")
		val authorSignature: String?,
	) : MessageOrigin()

	/**
	 * The message was originally sent to a channel chat.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#messageoriginchannel)
	 */
	@Serializable
	@SerialName("channel")
	data class Channel(
		@Serializable(with = UnixSecondsSerializer::class)
		override val date: Instant,

		val chat: opensavvy.telegram.entity.Chat,

		@SerialName("message_id")
		val message: Message.Id,

		@SerialName("author_signature")
		val authorSignature: String?,
	) : MessageOrigin()
}

/**
 * This object contains information about a message that is being replied to, which may come from another chat or forum topic.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#externalreplyinfo)
 */
@Serializable
data class ExternalReplyInfo(
	val origin: MessageOrigin,
	val chat: Chat?,

	@SerialName("message_id")
	val message: Message.Id?,

	@SerialName("link_preview_options")
	val linkPreviewOptions: LinkPreviewOptions?,

	val animation: Animation?,

	val audio: Audio?,

	val document: Document?,

	@SerialName("paid_media")
	val paidMedia: PaidMediaInfo?,

	val photo: List<PhotoSize>?,

	val sticker: Sticker?,

	val story: Story?,

	val video: Video?,

	@SerialName("video_note")
	val videoNote: VideoNote?,

	val voice: Voice?,

	@SerialName("has_media_spoiler")
	val hasMediaSpoiler: Boolean = false,

	val checklist: Checklist?,

	val contact: Contact?,

	val dice: Dice?,

	val game: Game?,

	// Fields allowed in ExternalReplyInfo per spec
	val giveaway: Giveaway?,

	@SerialName("giveaway_winners")
	val giveawayWinners: GiveawayWinners?,

	val invoice: Invoice?,

	val location: Location?,

	val poll: Poll?,

	val venue: Venue?,
)

/**
 * Describes the options used for link preview generation.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#linkpreviewoptions)
 */
@Serializable
data class LinkPreviewOptions(
	@SerialName("is_disabled")
	val isDisabled: Boolean = false,

	val url: String?,

	@SerialName("prefer_small_media")
	val preferSmallMedia: Boolean = false,

	@SerialName("prefer_large_media")
	val preferLargeMedia: Boolean = false,

	@SerialName("show_above_text")
	val showAboveText: Boolean = false,
)

/**
 * This object represents a service message about a change in auto-delete timer settings.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#messageautodeletetimerchanged)
 */
@Serializable
data class MessageAutoDeleteTimerChanged(
	@SerialName("message_auto_delete_time")
	val duration: @Serializable(with = DurationSecondsSerializer::class) Duration,
)

/**
 * This object contains information about the users whose identifiers were shared with the bot using a KeyboardButtonRequestUsers button.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#usersshared)
 */
@Serializable
data class UsersShared(
	@SerialName("request_id")
	val requestId: Int,

	val users: List<SharedUser>,
)

/**
 * This object contains information about a chat that was shared with the bot using a KeyboardButtonRequestChat button.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#chatshared)
 */
@Serializable
data class ChatShared(
	@SerialName("request_id")
	val requestId: Int,

	@SerialName("chat_id")
	val chatId: Chat.Id,

	val title: String?,

	val username: String?,

	val photo: List<PhotoSize>?,
)

/**
 * This object represents a service message about a user allowing a bot to write messages after adding it to the attachment menu,
 * launching a Web App from a link, or accepting an explicit request from a Web App sent by the method requestWriteAccess.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#writeaccessallowed)
 */
@Serializable
data class WriteAccessAllowed(
	@SerialName("from_request")
	val fromRequest: Boolean = false,

	@SerialName("web_app_name")
	val webAppName: String?,

	@SerialName("from_attachment_menu")
	val fromAttachmentMenu: Boolean = false,
)

/**
 * This object describes a message that was deleted or is otherwise inaccessible to the bot.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#inaccessiblemessage)
 */
@Serializable
data class InaccessibleMessage(
	val chat: Chat,

	@SerialName("message_id")
	val id: Message.Id,
) : MayBeInaccessibleMessage

/**
 * Describes a service message about checklist tasks marked as done or not done.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#checklisttasksdone)
 */
@Serializable
data class ChecklistTasksDone(
	/**
	 * Message containing the checklist whose tasks were marked as done or not done.
	 * Note: the embedded [Message] will not contain the `reply_to_message` field even if it is itself a reply.
	 */
	@SerialName("checklist_message")
	val checklistMessage: Message?,

	/** Identifiers of the tasks that were marked as done. */
	@SerialName("marked_as_done_task_ids")
	val markedAsDoneTaskIds: List<Int> = emptyList(),

	/** Identifiers of the tasks that were marked as not done. */
	@SerialName("marked_as_not_done_task_ids")
	val markedAsNotDoneTaskIds: List<Int> = emptyList(),
)

/**
 * Describes a service message about tasks added to a checklist.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#checklisttasksadded)
 */
@Serializable
data class ChecklistTasksAdded(
	/**
	 * Message containing the checklist to which the tasks were added.
	 * Note: the embedded [Message] will not contain the `reply_to_message` field even if it is itself a reply.
	 */
	@SerialName("checklist_message")
	val checklistMessage: Message?,

	/** List of tasks added to the checklist. */
	val tasks: List<ChecklistTask>,
)

/**
 * Describes a service message about a change in the price of direct messages sent to a channel chat.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#directmessagepricechanged)
 */
@Serializable
data class DirectMessagePriceChanged(
	/** True, if direct messages are enabled for the channel chat; false otherwise. */
	@SerialName("are_direct_messages_enabled")
	val areDirectMessagesEnabled: Boolean,

	/**
	 * The new number of Telegram Stars that must be paid by users for each direct message sent to the channel.
	 * Does not apply to users who have been exempted by administrators. Defaults to 0.
	 */
	@SerialName("direct_message_star_count")
	val directMessageStarCount: Int?,
)
