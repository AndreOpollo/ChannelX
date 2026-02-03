package com.opollo.channelx.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelResponseDto(
    @SerialName("total_channels")
    val totalChannels: Int,
    @SerialName("channels")
    val channels: List<ChannelDto>
)

@Serializable
data class ChannelDto(
    @SerialName("name")
    val name: String,
    @SerialName("code")
    val countryCode: String,
    @SerialName("url")
    val streamUrl: String,
    @SerialName("image")
    val imageUrl: String,
    @SerialName("status")
    val status: String,
    @SerialName("viewers")
    val viewers: Int


)