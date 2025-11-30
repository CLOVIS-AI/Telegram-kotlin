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
import kotlin.jvm.JvmInline
import kotlin.time.Duration
import kotlin.time.Instant

interface Media {

	val id: Id

	val uniqueId: UniqueId

	@Serializable
	@JvmInline
	value class Id(val id: String)

	@Serializable
	@JvmInline
	value class UniqueId(val id: String)
}

/**
 * This object represents a file ready to be downloaded.
 *
 * The file can be downloaded via the link `https://api.telegram.org/file/bot<token>/<file_path>`.
 * It is guaranteed that the link will be valid for at least 1 hour. When the link expires, a new one can be requested by calling `getFile`.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#file)
 */
@Serializable
data class File(
	@SerialName("file_id")
	override val id: Media.Id,

	@SerialName("file_unique_id")
	override val uniqueId: Media.UniqueId,

	val fileSize: Long?,

	val filePath: String?,
) : Media

/**
 * This object represents an animation file (GIF or H.264/MPEG-4 AVC video without sound).
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#animation)
 */
@Serializable
data class Animation(
	@SerialName("file_id")
	override val id: Media.Id,

	@SerialName("file_unique_id")
	override val uniqueId: Media.UniqueId,

	val width: Int,

	val height: Int,

	@Serializable(with = DurationSecondsSerializer::class)
	val duration: Duration,

	val thumbnail: PhotoSize?,

	@SerialName("file_name")
	val name: String?,

	@SerialName("mime_type")
	val mimeType: String?,

	@SerialName("file_size")
	val fileSize: Long?,
) : Media

/**
 * This object represents one size of a photo or a file / sticker thumbnail.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#photosize)
 */
@Serializable
data class PhotoSize(
	@SerialName("file_id")
	override val id: Media.Id,

	@SerialName("file_unique_id")
	override val uniqueId: Media.UniqueId,

	val width: Int,

	val height: Int,

	@SerialName("file_size")
	val fileSize: Int?,
) : Media

/**
 * This object represents an audio file to be treated as music by the Telegram clients.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#audio)
 */
@Serializable
data class Audio(
	@SerialName("file_id")
	override val id: Media.Id,

	@SerialName("file_unique_id")
	override val uniqueId: Media.UniqueId,

	@Serializable(with = DurationSecondsSerializer::class)
	val duration: Duration,

	val performer: String?,

	val title: String?,

	@SerialName("file_name")
	val fileName: String?,

	@SerialName("mime_type")
	val mimeType: String?,

	@SerialName("file_size")
	val fileSize: Long?,

	val thumbnail: PhotoSize?,
) : Media

/**
 * This object represents a general file (as opposed to photos, voice messages and audio files).
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#document)
 */
@Serializable
data class Document(
	@SerialName("file_id")
	override val id: Media.Id,

	@SerialName("file_unique_id")
	override val uniqueId: Media.UniqueId,

	val thumbnail: PhotoSize?,

	@SerialName("file_name")
	val fileName: String?,

	@SerialName("mime_type")
	val mimeType: String?,

	@SerialName("file_size")
	val fileSize: Long?,
) : Media

/**
 * Describes the paid media added to a message.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#paidmediainfo)
 */
@Serializable
data class PaidMediaInfo(
	@SerialName("star_count")
	val starCount: Int,

	@SerialName("paid_media")
	val paidMedia: List<PaidMedia>,
)

/**
 * This object describes paid media.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#paidmedia)
 */
@Serializable
sealed class PaidMedia {

	/**
	 * The paid media isn't available before the payment.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#paidmediapreview)
	 */
	@Serializable
	@SerialName("preview")
	data class Preview(
		val width: Int?,
		val height: Int?,

		@Serializable(with = DurationSecondsSerializer::class)
		val duration: Duration?,
	) : PaidMedia()

	/**
	 * The paid media is a photo.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#paidmediaphoto)
	 */
	@Serializable
	@SerialName("photo")
	data class Photo(
		val photo: List<PhotoSize>,
	)

	/**
	 * The paid media is a video.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#paidmediavideo)
	 */
	@Serializable
	@SerialName("video")
	data class Video(
		val video: opensavvy.telegram.entity.Video,
	)

}

/**
 * This object represents a video file.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#video)
 */
@Serializable
data class Video(
	@SerialName("file_id")
	override val id: Media.Id,

	@SerialName("file_unique_id")
	override val uniqueId: Media.UniqueId,

	val width: Int,

	val height: Int,

	@Serializable(with = DurationSecondsSerializer::class)
	val duration: Duration,

	val thumbnail: PhotoSize?,

	val cover: List<PhotoSize>?,

	@Serializable(with = DurationSecondsSerializer::class)
	@SerialName("start_timestamp")
	val startTimestamp: Duration?,

	@SerialName("file_name")
	val fileName: String?,

	@SerialName("mime_type")
	val mimeType: String?,

	@SerialName("file_size")
	val fileSize: Long?,
) : Media

/**
 * This object represents a video message (available in Telegram apps as of v.4.0).
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#videonote)
 */
@Serializable
data class VideoNote(
	@SerialName("file_id")
	override val id: Media.Id,

	@SerialName("file_unique_id")
	override val uniqueId: Media.UniqueId,

	val length: Int,

	@Serializable(with = DurationSecondsSerializer::class)
	val duration: Duration,

	val thumbnail: PhotoSize?,

	@SerialName("file_size")
	val fileSize: Int?,
) : Media

/**
 * This object represents a voice note.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#voice)
 */
@Serializable
data class Voice(
	@SerialName("file_id")
	override val id: Media.Id,

	@SerialName("file_unique_id")
	override val uniqueId: Media.UniqueId,

	@Serializable(with = DurationSecondsSerializer::class)
	val duration: Duration,

	@SerialName("mime_type")
	val mimeType: String?,

	@SerialName("file_size")
	val fileSize: Long?,
) : Media

/**
 * This object represents a story.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#story)
 */
@Serializable
data class Story(
	val chat: Chat,

	val id: Int,
)

/**
 * Describes a checklist.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#checklist)
 */
@Serializable
data class Checklist(
	val title: String,

	@SerialName("title_entities")
	val titleEntities: List<MessageEntity> = emptyList(),

	val tasks: List<ChecklistTask>,

	@SerialName("others_can_add_tasks")
	val othersCanAddTasks: Boolean = false,

	@SerialName("others_can_mark_tasks_as_done")
	val othersCanMarkTasksAsDone: Boolean = false,
)

/**
 * Describes a task in a checklist.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#checklisttask)
 */
@Serializable
data class ChecklistTask(
	val id: Id,

	val text: String,

	@SerialName("text_entities")
	val textEntities: List<MessageEntity> = emptyList(),

	@SerialName("completed_by_user")
	val completedByUser: User?,

	@SerialName("completion_date")
	@Serializable(with = UnixSecondsSerializer::class)
	val completionDate: Instant?,
) {

	@Serializable
	@JvmInline
	value class Id(val id: Int)
}

/**
 * Describes a task to add to a checklist.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#inputchecklisttask)
 */
@Serializable
data class InputChecklistTask(
	val id: ChecklistTask.Id,

	val text: String,

	@SerialName("parse_mode")
	val parseMode: String? = null,

	@SerialName("text_entities")
	val textEntities: List<MessageEntity>? = null,
)

/**
 * Describes a checklist to create.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#inputchecklist)
 */
@Serializable
data class InputChecklist(
	val title: String,

	@SerialName("parse_mode")
	val parseMode: String? = null,

	@SerialName("title_entities")
	val titleEntities: List<MessageEntity>? = null,

	val tasks: List<InputChecklistTask>,

	@SerialName("others_can_add_tasks")
	val othersCanAddTasks: Boolean? = null,

	@SerialName("others_can_mark_tasks_as_done")
	val othersCanMarkTasksAsDone: Boolean? = null,
)

/**
 * Describes a service message about checklist tasks marked as done or not done.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#checklisttasksdone)
 */
@Serializable
data class ChecklistTasksDone(
	@SerialName("checklist_message")
	val checklistMessage: Message?,

	@SerialName("marked_as_done_task_ids")
	val markedAsDoneTaskIds: List<ChecklistTask.Id> = emptyList(),

	@SerialName("marked_as_not_done_task_ids")
	val markedAsNotDoneTaskIds: List<ChecklistTask.Id>,
)

/**
 * Value of the dice, 1-6 for “🎲”, “🎯” and “🎳” base emoji, 1-5 for “🏀” and “⚽” base emoji, 1-64 for “🎰” base emoji
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#dice)
 */
@Serializable
data class Dice(
	val emoji: String,

	/**
	 * Value of the dice, 1-6 for “🎲”, “🎯” and “🎳” base emoji, 1-5 for “🏀” and “⚽” base emoji, 1-64 for “🎰” base emoji
	 */
	val value: Int,
)

/**
 * This object contains information about a poll.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#poll)
 */
@Serializable
data class Poll(
	val id: Id,
	val question: String,

	@SerialName("question_entities")
	val questionEntities: List<MessageEntity> = emptyList(),

	val options: List<Option>,

	@SerialName("total_voter_count")
	val totalVoterCount: Int,

	@SerialName("is_closed")
	val isClosed: Boolean,

	@SerialName("is_anonymous")
	val isAnonymous: Boolean,

	val type: Type,

	@SerialName("allows_multiple_answers")
	val allowsMultipleAnswers: Boolean,

	@SerialName("correct_option_id")
	val correctOptionId: Int?,

	val explanation: String?,

	@SerialName("explanation_entities")
	val explanationEntities: List<MessageEntity> = emptyList(),

	@SerialName("open_period")
	val openDuration: @Serializable(with = DurationSecondsSerializer::class) Duration?,

	@SerialName("close_date")
	val closeDate: @Serializable(with = UnixSecondsSerializer::class) Instant?,
) {

	@Serializable
	@JvmInline
	value class Id(val id: String)

	/**
	 * This object contains information about one answer option in a poll.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#polloption)
	 */
	@Serializable
	data class Option(
		val text: String,

		@SerialName("text_entities")
		val textEntities: List<MessageEntity> = emptyList(),

		@SerialName("voter_count")
		val voterCount: Int,
	)

	@Serializable
	enum class Type {
		@SerialName("regular")
		Regular,

		@SerialName("quiz")
		Quiz,
	}
}

/**
 * This object contains information about one answer option in a poll to be sent.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#inputpolloption)
 */
@Serializable
data class InputPollOption(
	val text: String,

	@SerialName("text_parse_mode")
	val textParseMode: String? = null,

	@SerialName("text_entities")
	val textEntities: List<MessageEntity>? = null,
)

/**
 * This object represents an answer of a user in a non-anonymous poll.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#pollanswer)
 */
@Serializable
data class PollAnswer(
	@SerialName("poll_id")
	val pollId: Poll.Id,

	@SerialName("voter_chat")
	val voterChat: Chat?,

	val user: User?,

	@SerialName("option_ids")
	val optionIds: List<Int>,
)
