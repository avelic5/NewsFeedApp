package etf.ri.rma.newsfeedapp.model

/**
 * Data class koja predstavlja jedan novinski članak.
 */
data class NewsItem(
    val id: String,          // Jedinstveni identifikator vijesti (koristiti kao key za lazy listu)
    val title: String,       // Naslov vijesti
    val snippet: String,     // Kratak opis ili uvod u vijest snippet-djelic
    val imageUrl: String?,   // URL slike vijesti (trenutno se ne koristi) ? znaci moze biti null
    val category: String,    // Kategorija vijesti (npr. "Politika", "Sport")
    val isFeatured: Boolean, // Da li je vijest istaknuta ili ne (Featured ili Non-featured)
    val source: String,      // Izvor vijesti (npr. "BBC", "CNN")
    val publishedDate: String // Datum objavljivanja vijesti
)
