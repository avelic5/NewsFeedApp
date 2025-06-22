package etf.ri.rma.newsfeedapp.util



import etf.ri.rma.newsfeedapp.model.*
//klasa treba da postuje S od Solid principa- ne treba da sadrzi vise poslova, kako su ovo poslovi vezani za bazu izdvojeni su u posebnu klasu
fun NewsItem.toEntity(): NewsEntity = NewsEntity(//kotlin to omogucava preko ekstenzijskih/prosirujucih funkcija
    uuid = uuid,
    title = title,
    snippet = snippet,
    imageUrl = imageUrl,
    category = category,
    isFeatured = isFeatured,
    source = source,
    publishedDate = publishedDate
)


fun NewsWithTags.toNewsItem(): NewsItem {
    return NewsItem(
        uuid = news.uuid,
        title = news.title,
        snippet = news.snippet,
        imageUrl = news.imageUrl, // <-- PROVJERI OVO!
        category = news.category,
        isFeatured = news.isFeatured,
        source = news.source,
        publishedDate = news.publishedDate,
        imageTags = ArrayList(tags) // tags su TagEntity
    )
}

