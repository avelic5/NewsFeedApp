package etf.ri.rma.newsfeedapp.screen
import etf.ri.rma.newsfeedapp.model.NewsItem
import java.text.SimpleDateFormat
import java.util.*
fun onApplyFilters(
    category: String,
    dateRange: String,
    unwantedWords: List<String>,
    newsItems: List<NewsItem>
): List<NewsItem> {
    var filteredList = newsItems



    if (dateRange.isNotEmpty() && dateRange.contains("-")) {
        try {
            val (startDateString, endDateString) = dateRange.split(" - ")
            val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            formatter.isLenient = false
            val startDate = formatter.parse(startDateString.trim())
            val endDate = formatter.parse(endDateString.trim())

            filteredList = filteredList.filter {
                val itemDate = formatter.parse(it.publishedDate.toString().trim())
                itemDate != null && itemDate in startDate..endDate
            }
        } catch (e: Exception) {
            println("Greška pri parsiranju datuma: ${e.message}")
        }
    }

    if (unwantedWords.isNotEmpty()) {
        filteredList = filteredList.filter { item ->
            unwantedWords.none { unwantedWord ->
                item.title.contains(unwantedWord, ignoreCase = true) ||
                        (item.snippet?.contains(unwantedWord, ignoreCase = true) ?: false)
            }
        }
    }

    return filteredList
}