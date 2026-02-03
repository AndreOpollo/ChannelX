package com.opollo.channelx.data

import com.opollo.channelx.domain.Channel
import com.opollo.channelx.domain.ChannelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): ChannelRepository {

    private val _channelsFlow = MutableStateFlow<Result<List<Channel>>>(Result.success((emptyList())))
    override fun getChannels(): Flow<Result<List<Channel>>> {
        return _channelsFlow.asStateFlow()
    }

    override suspend fun refreshChannels(): Result<Unit> {
       return try {
            val response = apiService.getChannels()
            val channels = response.channels.map { it.toDomain() }
            _channelsFlow.value = Result.success(channels)
            Result.success(Unit)
        } catch (e: Exception) {
            val error = Result.failure<List<Channel>>(e)
            _channelsFlow.value = error
            Result.failure(e)

        }
    }

    private fun ChannelDto.toDomain(): Channel {
        return Channel(
            id = "${name}_$countryCode".replace(" ", "_").lowercase(),
            name = name,
            countryCode = countryCode,
            streamUrl = streamUrl,
            imageUrl = imageUrl,
            isOnline = status.equals("online", ignoreCase = true),
            viewers = viewers
        )
    }
}