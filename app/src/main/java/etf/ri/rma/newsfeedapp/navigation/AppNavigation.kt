package etf.ri.rma.newsfeedapp.navigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import etf.ri.rma.newsfeedapp.screen.FilterScreen
import etf.ri.rma.newsfeedapp.screen.NewsDetailsScreen
import etf.ri.rma.newsfeedapp.screen.NewsFeedScreen
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.screen.onApplyFilters

@Composable
fun AppNavigation(navController: NavHostController) {
    var filters by remember { mutableStateOf(Triple("All", "", emptyList<String>())) }
    var newsItemsState by remember { mutableStateOf(emptyList<NewsItem>()) }

    NavHost(navController = navController, startDestination = "/home") {
        composable("/home") {
            NewsFeedScreen(
                navController = navController,
                filters = filters
            )
        }
        composable("/filters") {
            FilterScreen(
                navController = navController,

                onApplyFilters = { category, dateRange, unwantedWords ->
                    val filteredList = onApplyFilters(category, dateRange, unwantedWords, newsItemsState)
                    filters = Triple(category, dateRange, unwantedWords)
                    newsItemsState = filteredList
                }
            )
        }
        composable("/details/{id}") { backStackEntry ->
            val newsId = backStackEntry.arguments?.getString("id") ?: return@composable
            NewsDetailsScreen(navController = navController, newsId = newsId)
        }
    }
}