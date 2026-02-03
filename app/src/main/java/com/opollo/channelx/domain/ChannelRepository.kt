package com.opollo.channelx.domain

import kotlinx.coroutines.flow.Flow

interface ChannelRepository {

    fun getChannels(): Flow<Result<List<Channel>>>
    suspend fun refreshChannels():Result<Unit>
}