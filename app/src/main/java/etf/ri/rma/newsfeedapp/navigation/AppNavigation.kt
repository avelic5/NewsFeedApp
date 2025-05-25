package etf.ri.rma.newsfeedapp.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.repository.NewsDAO
import etf.ri.rma.newsfeedapp.screen.FilterScreen
import etf.ri.rma.newsfeedapp.screen.NewsDetailsScreen
import etf.ri.rma.newsfeedapp.screen.NewsFeedScreen
import etf.ri.rma.newsfeedapp.screen.onApplyFilters
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(navController: NavHostController) {
    // Filteri: kategorija, datum, nepoželjne riječi
    var filters by remember { mutableStateOf(Triple("all", "", emptyList<String>())) }

    // Stanje vijesti
    var newsItemsState by remember { mutableStateOf<List<NewsItem>>(emptyList()) }

    // Korutina za poziv `getAllStories`
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        newsItemsState = NewsDAO.getAllStories()
    }

    NavHost(navController = navController, startDestination = "/home") {
        composable("/home") {
            NewsFeedScreen(
                navController = navController,
                filters = filters,
                newsItems = newsItemsState,
                onCategorySelected = { category ->
                    filters = filters.copy(first = category)
                    coroutineScope.launch {
                        newsItemsState = NewsDAO.getTopStoriesByCategory(category)
                    }
                }
            )
        }

        composable("/filters") {
            FilterScreen(
                navController = navController,
                selectedCategory = filters.first,
                dateRange = filters.second,
                unwantedWords = filters.third,
                onApplyFilters = { category, dateRange, unwantedWords ->
                    val filteredList = onApplyFilters(category, dateRange, unwantedWords, newsItemsState)
                    filters = Triple(category, dateRange, unwantedWords)
                    newsItemsState = filteredList
                }
            )
        }

        composable("/details/{uuid}") { backStackEntry ->
            val newsId = backStackEntry.arguments?.getString("uuid") ?: return@composable
            NewsDetailsScreen(navController = navController, newsId = newsId)
        }
    }
}
