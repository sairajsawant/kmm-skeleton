import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import data.AppDatabase
import kotlinx.coroutines.Dispatchers

object AndroidPlatform : Platform {

    var applicationContext: Context? = null

    override val name: String = "Android ${Build.VERSION.SDK_INT}"

    override fun getDatabase(): AppDatabase {
        return applicationContext?.let {
            val dbFile = it.getDatabasePath("biller.db")
            return Room.databaseBuilder<AppDatabase>(it, dbFile.absolutePath)
                .setDriver(BundledSQLiteDriver())
                .setQueryCoroutineContext(Dispatchers.IO)
                .build()
        } ?: error("app context is null")
    }

}

actual fun getPlatform(): Platform = AndroidPlatform