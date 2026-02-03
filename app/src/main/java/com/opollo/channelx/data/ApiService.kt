package com.opollo.channelx.data

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("channels")
    suspend fun getChannels(
        @Query("user") user:String = "cdnlivetv",
        @Query("plan") plan:String = "free"
    ): ChannelResponseDto

    companion object{
        const val BASE_URL = "https://api.cdn-live.tv/api/v1/"
    }
}