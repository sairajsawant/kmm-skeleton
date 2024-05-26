package data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Biller::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun billerDao(): BillerDao

}