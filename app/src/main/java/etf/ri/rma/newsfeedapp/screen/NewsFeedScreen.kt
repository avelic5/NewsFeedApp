package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import etf.ri.rma.newsfeedapp.model.NewsItem

@Composable
fun NewsFeedScreen(newsItems: List<NewsItem>) {
    var selectedCategory by remember { mutableStateOf("All") }//pocetno stanje

   Surface( modifier = Modifier.fillMaxSize(),
       color = MaterialTheme.colorScheme.background)  { Column(modifier = Modifier.fillMaxSize()) {
        FilterChips( //moja funkcija
            selectedCategory = selectedCategory,//kategorija kao state
            onCategorySelected = { category -> selectedCategory = category }//------
        )
        NewsList(//moja funkcija
            newsItems = newsItems.filter {
                it.category == selectedCategory || selectedCategory == "All"
            },
            selectedCategory = selectedCategory
            //filter { ... } prolazi kroz sve elemente liste newsItems i zadržava samo one elemente za koje je uslov unutar {} true.
        )
    }}
}