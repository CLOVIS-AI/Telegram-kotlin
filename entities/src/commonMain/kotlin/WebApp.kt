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
 * Describes data sent from a Web App to the bot.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#webappdata)
 */
@Serializable
data class WebAppData(
	/** The data. Be aware that a bad client can send arbitrary data in this field. */
	val data: String,

	/** Text of the web_app keyboard button from which the Web App was opened. Be aware that a bad client can send arbitrary data in this field. */
	@SerialName("button_text")
	val buttonText: String,
)

/**
 * Describes a Web App.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#webappinfo)
 */
@Serializable
data class WebAppInfo(
	/** An HTTPS URL of a Web App to be opened with additional data as specified in Initializing Web Apps. */
	val url: String,
)
