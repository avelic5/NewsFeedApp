package etf.ri.rma.newsfeedapp.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import etf.ri.rma.newsfeedapp.data.NewsData
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.screen.FilterScreen
import etf.ri.rma.newsfeedapp.screen.NewsDetailsScreen
import etf.ri.rma.newsfeedapp.screen.NewsFeedScreen
import etf.ri.rma.newsfeedapp.screen.onApplyFilters
@Composable
fun AppNavigation(navController: NavHostController) {
    var filters by remember { mutableStateOf(Triple("All", "", emptyList<String>())) } // Default to "All"
    val allNewsItems = remember { NewsData.getAllNews() }
    var newsItemsState by remember { mutableStateOf(allNewsItems) }

    NavHost(navController = navController, startDestination = "/home") {
        composable("/home") {
            NewsFeedScreen(
                navController = navController,
                filters = filters,
                newsItems = newsItemsState
            )
        }
        composable("/filters") {
            FilterScreen(
                navController = navController,
                selectedCategory = filters.first,
                dateRange = filters.second,
                unwantedWords = filters.third,
                onApplyFilters = { category, dateRange, unwantedWords ->
                    val filteredList = onApplyFilters(category, dateRange, unwantedWords, allNewsItems)
                    filters = Triple(category, dateRange, unwantedWords)
                    newsItemsState = filteredList // Update filtered news
                }
            )
        }
        composable("/details/{id}") { backStackEntry ->
            val newsId = backStackEntry.arguments?.getString("id") ?: return@composable
            NewsDetailsScreen(navController = navController, newsId = newsId)
        }
    }
}