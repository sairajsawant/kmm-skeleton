package data

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert

@Entity
data class Biller(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

)

@Dao
interface BillerDao {

    @Upsert
    suspend fun insert(biller: Biller): Long

    @Query("SELECT * FROM biller")
    suspend fun getBillers(): List<Biller>

}