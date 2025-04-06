package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.R

@Composable
fun FeaturedNewsCard(newsItem: NewsItem) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Banner slika
            Image(
                painter = painterResource(id = R.drawable.vijesti), // Zamijenite sa stvarnim resursom
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(180.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tekstualni sadržaj
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = newsItem.title,
                    fontSize = 20.sp,
                    maxLines = 2 // Prikaz naslova u dva reda ako je naslov predug
                )
                Text(
                    text = newsItem.snippet,
                    fontSize = 16.sp,
                    maxLines = 2,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${newsItem.source} • ${newsItem.publishedDate}",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}