package com.opollo.channelx.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.opollo.channelx.domain.Channel
import com.opollo.channelx.presentation.home.components.ChannelCard
import com.opollo.channelx.presentation.home.components.FilterChipsSection
import com.opollo.channelx.presentation.home.components.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onChannelClick: (Channel) -> Unit,
    viewModel: HomeViewModel  = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCountry by viewModel.selectedCountry.collectAsStateWithLifecycle()
    val selectedStatus by viewModel.selectedStatus.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                Text(text = "TV Channels",
                    style = MaterialTheme.typography.headlineMedium)
            },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
                )
        },
    ){paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {viewModel.refresh()},
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()){
                SearchBar(query = searchQuery, onQueryChange = viewModel::onSearchQueryChanged)
                when(val state = uiState){
                    is HomeUiState.Error -> ErrorContent(message = state.message,
                        onRetry = {viewModel.refresh()})
                    HomeUiState.Loading -> LoadingContent()
                    is HomeUiState.Success -> {
                        if(state.channels.isEmpty() && searchQuery.isEmpty() && selectedCountry == null){
                            EmptyContent(
                                message = "No channels available",
                                icon = Icons.Default.ErrorOutline
                            )
                        } else {
                            Column(modifier = Modifier.fillMaxSize()){
                                FilterChipsSection(
                                    availableCountries = state.availableCountries,
                                    selectedCountry = selectedCountry,
                                    selectedStatus = selectedStatus,
                                    onCountrySelected = viewModel::onCountrySelected,
                                    onStatusSelected = viewModel::onStatusSelected,
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                if(state.isEmpty){
                                    EmptyContent(message = "No channels found matching your criteria",
                                        icon = Icons.Default.ErrorOutline)
                                } else {
                                    ChannelGrid(channels = state.channels, onChannelClick = onChannelClick)
                                }

                            }
                        }
                    }
                }

            }
        }

    }
}

@Composable
private fun ChannelGrid(
    channels: List<Channel>,
    onChannelClick:(Channel)->Unit,
    modifier: Modifier = Modifier
){
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        items(channels, key = {it.id}){
            channel->
            ChannelCard(channel,{onChannelClick(channel)})

        }
    }

}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier){
    Box(modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            CircularProgressIndicator()
            Text(
                text = "Loading channels...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        }
    }

}

@Composable
private fun EmptyContent(
    message: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
){
    Box(modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ){
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message:String,
    onRetry:()->Unit,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ){
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(onClick = onRetry) {
                Text("Retry")
            }

        }

    }
}