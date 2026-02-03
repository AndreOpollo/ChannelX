package com.opollo.channelx.domain.usecases

import com.opollo.channelx.domain.Channel
import com.opollo.channelx.domain.ChannelStatus
import javax.inject.Inject


class FilterChannelsUseCase @Inject constructor() {

    operator fun invoke(
        channels:List<Channel>,
        countryCode: String?,
        status: ChannelStatus
    ):List<Channel>{
        var filtered = channels

        if(countryCode!=null){
            filtered = filtered.filter { it.countryCode == countryCode }
        }

        filtered = when(status){
            ChannelStatus.ALL -> filtered
            ChannelStatus.ONLINE -> filtered.filter { it.isOnline }
            ChannelStatus.OFFLINE -> filtered.filter { !it.isOnline }
        }

        return filtered
    }

    fun getAvailableCountries(channels:List<Channel>):List<String>{
        return channels.map{it.countryCode}.distinct().sorted()
    }
}