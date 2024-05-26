import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSHomeDirectory
import platform.UIKit.UIDevice

class IOSPlatform : Platform {

    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    override fun getDatabase(): AppDatabase {
        val dbFile = NSHomeDirectory() + "/biller.db"
        return Room.databaseBuilder<AppDatabase>(
            name = dbFile,
            factory = { AppDatabase::class.instantiateImpl() }
        ).setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}

actual fun getPlatform(): Platform = IOSPlatform()