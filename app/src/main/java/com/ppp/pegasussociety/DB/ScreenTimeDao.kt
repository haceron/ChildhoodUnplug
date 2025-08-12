//package com.ppp.pegasussociety.DB

/*

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ppp.pegasussociety.Model.ScreenTimeEntry
import kotlinx.coroutines.flow.Flow


@Dao
interface ScreenTimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: ScreenTimeEntry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<ScreenTimeEntry>)

    @Query("SELECT * FROM screen_time_entries ORDER BY dateTime DESC")
    fun getAllEntriesFlow(): Flow<List<ScreenTimeEntry>>

    @Query("SELECT * FROM screen_time_entries ORDER BY dateTime DESC")
    suspend fun getAllEntries(): List<ScreenTimeEntry>

    @Query("DELETE FROM screen_time_entries")
    suspend fun clearAll()
}
*/
