package etf.ri.rma.newsfeedapp.screen

import etf.ri.rma.newsfeedapp.model.NewsItem

fun onApplyFilters(
    category: String,
    dateRange: String,
    unwantedWords: List<String>,
    newsItems: List<NewsItem>
): List<NewsItem> {
    var filteredList = newsItems

    if (category != "All") {
        filteredList = filteredList.filter { it.category == category }
    }

    if (dateRange.isNotEmpty()) {
        val (startDateString, endDateString) = dateRange.split(";")
        val formatter = java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault())
        val startDate = formatter.parse(startDateString)
        val endDate = formatter.parse(endDateString)

        filteredList = filteredList.filter {
            val itemDate = formatter.parse(it.publishedDate)
            itemDate in startDate..endDate
        }
    }

    if (unwantedWords.isNotEmpty()) {
        filteredList = filteredList.filter { item ->
            unwantedWords.none { unwantedWord ->
                item.title.contains(unwantedWord, ignoreCase = true) ||
                        item.snippet.contains(unwantedWord, ignoreCase = true)
            }
        }
    }

    return filteredList
}