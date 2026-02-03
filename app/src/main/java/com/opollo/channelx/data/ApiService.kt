package com.opollo.channelx.data

import retrofit2.http.GET

interface ApiService {

    @GET("channels")
    suspend fun getChannels(): ChannelResponseDto

    companion object{
        const val BASE_URL = "https://api.cdn-live.tv/api/v1/"
    }
}