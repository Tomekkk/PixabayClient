package com.tcode.pixabayclient.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKey>)

    @Query("SELECT * FROM remote_keys WHERE id = :id LIMIT 1")
    suspend fun getKey(id: Long): RemoteKey?

    @Query("SELECT * FROM remote_keys WHERE `query` = :query ORDER BY id DESC LIMIT 1")
    suspend fun getLatestKey(query: String): RemoteKey?

    @Update
    fun update(remoteKey: RemoteKey)

    @Query("DELETE FROM remote_keys WHERE `query` = :query")
    suspend fun delete(query: String)

    @Query("SELECT createdAt FROM remote_keys WHERE `query` = :query ORDER BY createdAt ASC LIMIT 1")
    suspend fun getOldestCreationTime(query: String): Long?

    @Query("SELECT `query` FROM remote_keys GROUP BY `query` ORDER BY createdAt DESC")
    fun getStoredQueriesStream(): Flow<List<String>>
}
