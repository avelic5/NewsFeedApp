package etf.ri.rma.newsfeedapp.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.data.network.NewsDAO
import etf.ri.rma.newsfeedapp.data.network.ImagaDAO
import etf.ri.rma.newsfeedapp.data.network.api.TheNewsAPIService
import etf.ri.rma.newsfeedapp.data.network.api.ImagaAPIService
import etf.ri.rma.newsfeedapp.screen.FilterScreen
import etf.ri.rma.newsfeedapp.screen.NewsDetailsScreen
import etf.ri.rma.newsfeedapp.screen.NewsFeedScreen
import etf.ri.rma.newsfeedapp.screen.onApplyFilters
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
@Composable
fun AppNavigation(navController: NavHostController) {
    var filters by remember { mutableStateOf(Triple("all", "", emptyList<String>())) }
    var newsItemsState by remember { mutableStateOf<List<NewsItem>>(emptyList()) }

    val coroutineScope = rememberCoroutineScope()

    val apiService = remember {
        Retrofit.Builder()
            .baseUrl("https://api.thenewsapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheNewsAPIService::class.java)
    }

    val newsDAO = remember { NewsDAO(apiService) }

    val imaggaApiService = remember {
        Retrofit.Builder()
            .baseUrl("https://api.imagga.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImagaAPIService::class.java)
    }

    val imaggaDAO = remember { ImagaDAO(imaggaApiService) }

    // osvježavanje kad se vrati sa detalja
    val currentBackStack = navController.currentBackStackEntryAsState().value
    val refresh = currentBackStack?.arguments?.getString("refresh") == "true"

    LaunchedEffect(refresh) {
        newsItemsState = newsDAO.getAllStories()
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
                        newsItemsState = newsDAO.getTopStoriesByCategory(category)
                    }
                },
                newsDAO = newsDAO
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
                },
                onCategoryChanged = { category ->
                    val updatedNews = if (category.lowercase() == "all") {
                        newsDAO.getAllStories()
                    } else {
                        newsDAO.getTopStoriesByCategory(category)
                    }
                    newsItemsState = updatedNews
                }
            )
        }


        composable("/details/{uuid}") { backStackEntry ->
            val newsId = backStackEntry.arguments?.getString("uuid") ?: return@composable
            NewsDetailsScreen(
                navController = navController,
                newsId = newsId,
                newsDAO = newsDAO,
                imaggaDAO = imaggaDAO
            )
        }
    }
}
