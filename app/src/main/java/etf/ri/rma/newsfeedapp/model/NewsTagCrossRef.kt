package etf.ri.rma.newsfeedapp.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(primaryKeys = ["newsId", "tagsId"],
    tableName = "NewsTags")
data class NewsTagCrossRef(
    val newsId: Long,
    val tagsId: Long
)