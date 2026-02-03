package com.opollo.channelx.data.di

import com.opollo.channelx.data.ChannelRepositoryImpl
import com.opollo.channelx.domain.ChannelRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindChannelRepository(
        channelRepositoryImpl: ChannelRepositoryImpl
    ): ChannelRepository
}