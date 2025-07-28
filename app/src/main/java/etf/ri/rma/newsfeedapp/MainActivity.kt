package etf.ri.rma.newsfeedapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import etf.ri.rma.newsfeedapp.data.NewsData
import etf.ri.rma.newsfeedapp.screen.NewsFeedScreen

import etf.ri.rma.newsfeedapp.ui.theme.NewsFeedAppTheme

import androidx.navigation.compose.rememberNavController
import etf.ri.rma.newsfeedapp.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppNavigation(navController = navController)
        }
    }
}