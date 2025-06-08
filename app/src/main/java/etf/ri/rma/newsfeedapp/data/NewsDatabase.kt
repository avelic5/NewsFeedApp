package etf.ri.rma.newsfeedapp.data


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import etf.ri.rma.newsfeedapp.model.*

@Database(
    entities = [NewsEntity::class, TagEntity::class, NewsTagCrossRef::class],
    version = 1
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun savedNewsDAO(): SavedNewsDAO//Room koristi da bi mogao generisati dao instance

    companion object {
        @Volatile private var INSTANCE: NewsDatabase? = null//samo jednom niti mogu pristupiti

        fun getInstance(context: Context): NewsDatabase {
            return INSTANCE ?: synchronized(this) {//samo da jednom bude kreirana instanca
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java,
                    "news-db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}