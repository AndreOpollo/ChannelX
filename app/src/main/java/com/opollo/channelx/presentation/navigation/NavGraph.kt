package com.opollo.channelx.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.opollo.channelx.domain.Channel
import com.opollo.channelx.presentation.home.HomeScreen
import com.opollo.channelx.presentation.player.PlayerScreen
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object Routes {
    const val HOME = "home"
    const val PLAYER = "player/{channelJson}"

    fun player(channel: Channel):String{
        val json = Json.encodeToString(channel)
        val encodedJson = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())

        return "player/${encodedJson}"
    }
}

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()){
    NavHost(navController, startDestination = Routes.HOME){
        composable(Routes.HOME){
            HomeScreen(onChannelClick = {
                channel->
                navController.navigate(Routes.player(channel))
            })

        }
        composable(route = Routes.PLAYER,
            arguments = listOf(
                navArgument("channelJson"){
                    type = NavType.StringType
                }
            )){backStackEntry->
            val encodedJson = backStackEntry.arguments?.getString("channelJson")
            val channelJson = URLDecoder.decode(encodedJson, StandardCharsets.UTF_8.toString())
            val channel = Json.decodeFromString<Channel>(channelJson)
            PlayerScreen(
                channel = channel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}