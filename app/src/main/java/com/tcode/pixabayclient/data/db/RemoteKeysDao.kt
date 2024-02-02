package com.tcode.pixabayclient.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: RemoteKeys)

    @Query("SELECT * FROM remote_keys WHERE `query` = :query")
    suspend fun get(query: String): RemoteKeys?

    @Query("DELETE FROM remote_keys WHERE `query` = :query")
    suspend fun clear(query: String)
}
