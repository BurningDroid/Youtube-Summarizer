package com.youknow.yts.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.youknow.yts.ui.history.HistoryPane
import com.youknow.yts.ui.nav.Route.History
import com.youknow.yts.ui.nav.Route.Profile
import com.youknow.yts.ui.nav.Route.Settings
import com.youknow.yts.ui.nav.Route.Summarize
import com.youknow.yts.ui.settings.SettingsPane
import com.youknow.yts.ui.summarize.SummarizePane

@Composable
fun MainPane(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Summarize
    ) {
        composable<Summarize> {
            SummarizePane {
                navController.navigate(it)
            }
//            HomeScreen(onNavigateToProfile = { id ->
//                navController.navigate(Profile(id))
//            })
        }
        composable<Settings> {
            SettingsPane {
                navController.popBackStack()
            }
        }
        composable<History> {
            HistoryPane {
                navController.popBackStack()
            }
        }
        composable<Profile> { backStackEntry ->
//            val profile: Profile = backStackEntry.toRoute()
//            ProfileScreen(profile.id)
        }
    }
}