package com.opollo.channelx.presentation.home

import com.opollo.channelx.domain.Channel

sealed interface HomeUiState {
    data object Loading: HomeUiState
    data class Success(
        val channels:List<Channel>,
        val availableCountries:List<String>,
        val isEmpty:Boolean
    ): HomeUiState

    data class Error(val message:String): HomeUiState
}