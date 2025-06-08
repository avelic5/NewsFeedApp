package etf.ri.rma.newsfeedapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tags")
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val tagId: Long = 0,
    val value: String
)