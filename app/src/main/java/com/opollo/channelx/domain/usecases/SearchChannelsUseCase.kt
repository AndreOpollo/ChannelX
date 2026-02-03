package com.opollo.channelx.domain.usecases

import com.opollo.channelx.domain.Channel

class SearchChannelsUseCase {
    operator fun invoke(channels:List<Channel>,query:String):List<Channel>{
        if(query.isBlank()){
            return channels
        }
        val normalizedQuery = query.trim().lowercase()

        return channels.filter { channel ->
            channel.name.lowercase().contains(normalizedQuery)
        }
    }
}