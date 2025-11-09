# Converting from the Telegram documentation to Kotlin serializable classes

|               |                                                                                                                                                                                |
|---------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Status        | [![Status: active](https://badgen.net/static/Status/active/purple)](https://gitlab.com/opensavvy/playgrounds/baseline/-/blob/main/docs/design/README.md#creating-a-new-record) |
| Discussion    |                                                                                                                                                                                |
| Superseded by |                                                                                                                                                                                |
| Relates to    |                                                                                                                                                                                |

This document describes how we create Kotlin classes for entities described in the [Telegram bot API](https://core.telegram.org/bots/api).

[TOC]

## The architecture

Telegram entities are declared in the package `opensavvy.telegram.entity` in the module `:entities`.

Entities are grouped by similarity in files. For example, all message-related classes are in `Message.kt`, all media-related classes are in `Media.kt`, etc.

## The mapping

The Telegram bot API describes entities as tables. For example:

> **ShippingOption**
>
> This object represents one shipping option.
>
> | Field      | Type                   | Description                                                                 |
> |------------|------------------------|-----------------------------------------------------------------------------|
> | id         | String                 | Shipping option identifier                                                  |
> | title      | String                 | Optional. Option title                                                      |
> | all_prices | Array of LabelledPrice | List of price portions                                                      |
> | isPremium  | True                   | Optional. True if this address is available only to Telegram Premium users. |

To convert this into a Kotlin class, follow these rules:

- Fields are named in `camelCase`, following the Kotlin coding style. (e.g.: the field `all_prices` is named `allPrices`). Do so using `@SerialName` unless the name is unchanged.
- Keep the fields in the same order in the Kotlin class and in the documentation (to simplify review).
- The types are mapped as follows:
	- If an identical Kotlin type exists, use it. (e.g.: `String` → `kotlin.String`, `Boolean` → `kotlin.Boolean`)
	- If unspecified, integers are mapped to `Int`. If the description mentions that values larger than 32 bits may appear, integers are mapped to `Long` instead.
	- Fields with a description that mentions them as Optional should be marked as nullable in Kotlin, without a default value.
	- Arrays are mapped to `kotlin.List`. If they are marked as optional, they are declared with a default value of `emptyList()`.
	- The type `True` is mapped to non-nullable `Boolean` with a default value of `false`, even if the type is declared optional. This is because Telegram doesn't emit these fields at all when they are `false`.
	- If the type is constrained with a common constraint, represent it as a `value class`. For example, `Chat.Id`, `Message.Id`, `LanguageCode`, etc. The value class is nested if it represents something specific to that entity (e.g. `Chat.Id`) and is top-level if it can be reused in many unrelated entities (e.g. `CountryCode`).
	- If the type has an exhaustive list of possible values explicitly mentioned in the documentation, use an `enum`.
	- If the type has multiple possible cases that are differentiated by a field named `type`, use a sealed class or sealed interface.
	- If the type is a duration in seconds, use the type `@Serializable(with = DurationSecondsSerializer::class) Duration` (nullable if the field is optional)
	- If the type is a timestamp in UNIX seconds, use the type `@Serializable(with = UnixSecondsSerializer::class) Instant` (nullable if the field is optional)
- The description of the entity is copied to the first line of its KDoc comment.
- Add an 'external resources' section with the link to the Telegram bot API.

Therefore, in the example above, we would generate the class:

```kotlin
/**
 * This object represents one shipping option.
 *
 * ### External resources
 *
 * - [Official documentation](https://core.telegram.org/bots/api#shippingoption)
 */
@Serializable
data class ShippingOption(
	val id: Id,

	val title: String?,

	@SerialName("all_prices")
	val allPrices: List<LabelledPrice>,

	@SerialName("is_premium")
	val isPremium: Boolean = false,
) {

	@Serializable
	@JvmInline
	value class Id(val id: String)
}
```

When in doubt, analyze how similar patterns have been mapped elsewhere in the codebase, or ask a maintainer.
