package com.opollo.channelx.domain

data class Channel(
    val id: String,
    val name: String,
    val countryCode:String,
    val streamUrl:String,
    val imageUrl:String,
    val isOnline:Boolean,
    val viewers:Int
){
    fun getCountryName():String{
        return countryCode.uppercase()
    }

    fun getStatusText():String{
        return if(isOnline) "Online" else "Offline"
    }
}

enum class ChannelStatus{
    ALL,ONLINE,OFFLINE
}