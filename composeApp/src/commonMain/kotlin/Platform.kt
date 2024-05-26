import data.AppDatabase

interface Platform {
    val name: String

    fun getDatabase(): AppDatabase
}

expect fun getPlatform(): Platform