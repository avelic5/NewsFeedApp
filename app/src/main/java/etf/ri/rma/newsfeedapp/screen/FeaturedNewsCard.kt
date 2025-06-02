package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.R

@Composable
fun FeaturedNewsCard(newsItem: NewsItem, onClick: () -> Unit) {
    Card(modifier = Modifier.padding(10.dp).clickable{ onClick() }) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Banner slika
            AsyncImage(
                model = newsItem.imageUrl,
                error = painterResource(id = R.drawable.vijesti),
                contentDescription = "Slika vijesti",
                modifier = Modifier.fillMaxWidth().height(170.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Tekstualni sadržaj
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = newsItem.title,
                    fontSize = 19.sp,
                    maxLines = 2 // Prikaz naslova u dva reda ako je naslov predug
                )
                Text(
                    text = newsItem.snippet,
                    fontSize = 15.sp,
                    maxLines = 2,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "${newsItem.source} • ${newsItem.publishedDate}",
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}