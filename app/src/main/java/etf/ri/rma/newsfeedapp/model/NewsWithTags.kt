package etf.ri.rma.newsfeedapp.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
//ovo model iz baze spojen, radit cemo konverziju
data class NewsWithTags(
    @Embedded val news: NewsEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = NewsTagCrossRef::class,
            parentColumn = "newsId",
            entityColumn = "tagsId"
        )
    )
    val tags: List<TagEntity>
)