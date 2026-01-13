/*
 * Copyright (c) 2025-2026, OpenSavvy and contributors.
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

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * This object describes the bot's menu button in a private chat.
 *
 * If a menu button other than [Default] is set for a private chat, then it is applied in the chat. Otherwise the default menu button is applied. By default, the menu button opens the list of bot commands.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#menubutton)
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
sealed class MenuButton {

	/**
	 * Represents a menu button, which opens the bot's list of commands.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#menubuttoncommands)
	 */
	@Serializable
	@SerialName("commands")
	data object Commands : MenuButton()

	/**
	 * Represents a menu button, which launches a Web App.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#menubuttonwebapp)
	 */
	@Serializable
	@SerialName("web_app")
	data class WebApp(
		/** Text on the button */
		val text: String,
		/** Description of the Web App that will be launched when the user presses the button. */
		@SerialName("web_app")
		val webApp: WebAppInfo,
	) : MenuButton()

	/**
	 * Describes that no specific value for the menu button was set.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://core.telegram.org/bots/api#menubuttondefault)
	 */
	@Serializable
	@SerialName("default")
	data object Default : MenuButton()
}
