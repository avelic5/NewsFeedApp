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
fun StandardNewsCard(newsItem: NewsItem) {
    Card(modifier = Modifier.padding(8.dp)) {
        Row(modifier = Modifier.padding(8.dp)) {
            // Slika
            Image(
                painter = painterResource(id = R.drawable.vijesti),
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Tekstualni sadržaj
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = newsItem.title,
                    fontSize = 16.sp,
                    maxLines = 2 // Prikaz naslova u dva reda ako je naslov predug
                )
                Text(
                    text = newsItem.snippet,
                    fontSize = 14.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${newsItem.source} • ${newsItem.publishedDate}",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}