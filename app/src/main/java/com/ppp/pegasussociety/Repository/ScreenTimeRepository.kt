/*
package com.ppp.pegasussociety.Repository

import android.util.Log
import com.ppp.pegasussociety.ApiInterface.AllApi
import com.ppp.pegasussociety.DB.ScreenTimeDao
import com.ppp.pegasussociety.Model.ScreenTimeEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScreenTimeRepository @Inject constructor(
    private val api: AllApi,
    private val dao: ScreenTimeDao
) {
    suspend fun addEntry(entry: ScreenTimeEntry) {
        Log.d("RepoAllapi", "Inserting entry: $entry")

        api.postScreenTimeEntry(entry)
        dao.insert(entry)
    }

    suspend fun fetchAllEntries(): List<ScreenTimeEntry> {
        return try {
            val remote = api.getAllScreenTimeEntries()
            dao.clearAll()
            dao.insertAll(remote)
            remote
        } catch (e: Exception) {
            dao.getAllEntries() // Fallback to local DB
        }
    }

    fun getLocalEntries(): Flow<List<ScreenTimeEntry>> = dao.getAllEntriesFlow()
}
*/
