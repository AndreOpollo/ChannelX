package com.opollo.channelx.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opollo.channelx.domain.ChannelStatus
import com.opollo.channelx.domain.usecases.FilterChannelsUseCase
import com.opollo.channelx.domain.usecases.GetChannelsUseCase
import com.opollo.channelx.domain.usecases.SearchChannelsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val searchChannelsUseCase: SearchChannelsUseCase,
    private val filterChannelsUseCase: FilterChannelsUseCase,
    private val getChannelsUseCase: GetChannelsUseCase
): ViewModel(){
    private val channelsFlow = getChannelsUseCase()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCountry = MutableStateFlow<String?>(null)
    val selectedCountry = _selectedCountry.asStateFlow()

    private val _selectedStatus = MutableStateFlow(ChannelStatus.ALL)
    val selectedStatus = _selectedStatus.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    val uiState: StateFlow<HomeUiState> = combine(
        channelsFlow,
        _searchQuery.debounce(300),
        _selectedCountry,
        _selectedStatus
    ){channelResult,query,country,status->
        channelResult.fold(
            onSuccess = {
                channels->
                val filtered = filterChannelsUseCase(channels,country,status)

                val searched = searchChannelsUseCase(filtered,query)

                val countries = filterChannelsUseCase.getAvailableCountries(channels)

                HomeUiState.Success(
                    channels = searched,
                    availableCountries = countries,
                    isEmpty = searched.isEmpty()
                )
            },
            onFailure = {
                error->
                HomeUiState.Error(message = error.message?:"Unknown error occurred")
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading
    )

    init {
        loadChannels()
    }

    fun loadChannels(){
        viewModelScope.launch {
            getChannelsUseCase.refresh()
        }
    }

    fun refresh(){
        viewModelScope.launch {
            _isRefreshing.value = true
            getChannelsUseCase.refresh()
            _isRefreshing.value = false
        }
    }

    fun onSearchQueryChanged(query:String){
        _searchQuery.value = query
    }

    fun onCountrySelected(countryCode:String?){
        _selectedCountry.value = countryCode
    }

    fun onStatusSelected(status: ChannelStatus){
        _selectedStatus.value = status
    }

    fun clearAllFilters(){
        _selectedStatus.value = ChannelStatus.ALL
        _selectedCountry.value = null
        _searchQuery.value = ""
    }


}