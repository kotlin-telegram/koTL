# koTL

[![GitHub stars](https://img.shields.io/github/stars/kotlin-telegram/koTL.svg)](https://github.com/kotlin-telegram/koTL/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/kotlin-telegram/koTL.svg)](https://github.com/kotlin-telegram/koTL/network)
[![License](https://img.shields.io/github/license/kotlin-telegram/koTL.svg)](LICENSE.md)

⚠️ ALL OF THIS IS WORK IN PROGRESS ⚠️

`koTL` is a Kotlin library that simplifies working with Telegram's Type Language (TL) by providing seamless integration with kotlinx.serialization. It offers tools and utilities for managing TL schema, serializing and deserializing TL objects, and extending kotlinx.serialization capabilities for Telegram-related applications.

## Features

- TL schema parsing and management.
- Serialization and deserialization of TL objects.
- Seamless integration with kotlinx.serialization.
- Kotlin DSL for constructing TL objects

## Example of Usage

```kotlin
@Serializable
@TLFunction(crc32 = 0x2d84d5f5_u)
data class GetUserRequest(
    val ids: List<InputUserType>
)

@Serializable
@TLType
sealed interface InputUserType

@Serializable
@TLFunction(crc32 = 0xb98886cf_u)
data object InputUserEmpty : InputUserType

@Serializable
@TLFunction(crc32 = 0xf7c1b13f_u)
data object InputUserSelf : InputUserType

@Serializable
@TLFunction(crc32 = 0xf21158c6_u)
data class InputUser(
    val userId: Long,
    val accessHash: Long
) : InputUserType
```
