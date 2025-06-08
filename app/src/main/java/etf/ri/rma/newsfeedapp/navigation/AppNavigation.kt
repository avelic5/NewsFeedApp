package etf.ri.rma.newsfeedapp.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import etf.ri.rma.newsfeedapp.data.SavedNewsDAO
import etf.ri.rma.newsfeedapp.data.hasInternetConnection
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


import android.content.Context


@Composable
fun AppNavigation(navController: NavHostController, context: Context, savedNewsDAO: SavedNewsDAO) {
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

    val currentBackStack = navController.currentBackStackEntryAsState().value
    val refresh = currentBackStack?.arguments?.getString("refresh") == "true"

    LaunchedEffect(refresh) {
        newsItemsState = if (!hasInternetConnection(context)) {
            savedNewsDAO.allNews()
        } else {
            val freshNews = newsDAO.getAllStories()
            freshNews.forEach { savedNewsDAO.saveNews(it) }
            freshNews
        }
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
                        newsItemsState = if (!hasInternetConnection(context)) {
                            savedNewsDAO.getNewsWithCategory(category)
                        } else {
                            val topNews = newsDAO.getTopStoriesByCategory(category)
                            topNews.forEach { savedNewsDAO.saveNews(it) }
                            topNews
                        }
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
                    coroutineScope.launch {
                        val updatedNews = if (!hasInternetConnection(context)) {
                            savedNewsDAO.getNewsWithCategory(category)
                        } else {
                            val topNews = newsDAO.getTopStoriesByCategory(category)
                            topNews.forEach { savedNewsDAO.saveNews(it) }
                            topNews
                        }
                        newsItemsState = updatedNews
                    }
                }
            )
        }

        composable("/details/{uuid}") { backStackEntry ->
            val newsId = backStackEntry.arguments?.getString("uuid") ?: return@composable
            NewsDetailsScreen(
                navController = navController,
                newsId = newsId,
                newsDAO = newsDAO,
                imaggaDAO = imaggaDAO,
                savedNewsDAO = savedNewsDAO,
                context = context
            )
        }
    }
}

