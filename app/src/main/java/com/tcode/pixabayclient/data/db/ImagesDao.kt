package com.tcode.pixabayclient.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImagesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(images: List<ImageEntity>): List<Long>

    @Query("SELECT * FROM images WHERE `query`=:query ORDER BY id ASC")
    fun getAll(query: String): PagingSource<Int, ImageEntity>

    @Query("DELETE FROM images WHERE `query`=:query")
    suspend fun delete(query: String)

    @Query("SELECT * FROM images WHERE imageId=:id")
    suspend fun getImage(id: Long): ImageEntity?
}
