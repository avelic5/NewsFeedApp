package etf.ri.rma.newsfeedapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import etf.ri.rma.newsfeedapp.navigation.AppNavigation

import androidx.room.Room
import etf.ri.rma.newsfeedapp.data.NewsDatabase


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = NewsDatabase.getInstance(applicationContext)
        val savedNewsDAO = database.savedNewsDAO()

        setContent {
            val navController = rememberNavController()
            AppNavigation(
                context = this,
                navController = navController,
                savedNewsDAO = savedNewsDAO
            )
        }
    }

}