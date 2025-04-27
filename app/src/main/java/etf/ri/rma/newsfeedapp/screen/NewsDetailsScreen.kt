package etf.ri.rma.newsfeedapp.screen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import etf.ri.rma.newsfeedapp.data.NewsData
import java.text.SimpleDateFormat
import java.util.*
@Composable
fun NewsDetailsScreen(navController: NavController, newsId: String) {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val newsItem = NewsData.getAllNews().find { it.id == newsId }
    val newsItemDate = newsItem?.publishedDate?.let { dateFormat.parse(it)?.time } ?: 0L

    val relatedNews = NewsData.getAllNews()
        .filter { it.category == newsItem?.category && it.id != newsId }
        .sortedWith(compareBy({ kotlin.math.abs(
            dateFormat.parse(it.publishedDate)?.time ?: (0L - newsItemDate)
        ) }, { it.title }))
        .take(2)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // News Details
            Text(newsItem?.title ?: "N/A", style = MaterialTheme.typography.titleLarge, modifier = Modifier.testTag("details_title"))
            Spacer(modifier = Modifier.height(8.dp))
            Text(newsItem?.snippet ?: "N/A", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.testTag("details_snippet"))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Kategorija: ${newsItem?.category ?: "N/A"}", modifier = Modifier.testTag("details_category"))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Izvor: ${newsItem?.source ?: "N/A"}", modifier = Modifier.testTag("details_source"))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Datum: ${newsItem?.publishedDate ?: "N/A"}", modifier = Modifier.testTag("details_date"))
            Spacer(modifier = Modifier.height(16.dp))

            // Related News
            Text("Povezane vijesti iz iste kategorije", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            relatedNews.forEachIndexed { index, relatedItem ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)) // Light background
                        .clickable { navController.navigate("/details/${relatedItem.id}") }
                        .testTag("related_news_title_${index + 1}")
                ) {
                    Text(
                        text = relatedItem.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(8.dp) // Padding inside the box
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("/home") },
                modifier = Modifier.testTag("details_close_button")
            ) {
                Text("Zatvori detalje")
            }
        }
    }
}