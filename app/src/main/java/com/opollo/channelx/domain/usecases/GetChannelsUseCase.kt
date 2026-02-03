package com.opollo.channelx.domain.usecases

import com.opollo.channelx.domain.Channel
import com.opollo.channelx.domain.ChannelRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChannelsUseCase @Inject constructor(
    private val repository: ChannelRepository
) {
    operator fun invoke(): Flow<Result<List<Channel>>> {
        return repository.getChannels()

    }

    suspend fun refresh():Result<Unit>{
        return repository.refreshChannels()
    }
}