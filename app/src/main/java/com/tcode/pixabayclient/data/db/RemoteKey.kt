package com.tcode.pixabayclient.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This table is used to store next page number not loaded for given query with the creation time.
 */
@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val query: String,
    val prevPage: Int?,
    val nextPage: Int?,
    val createdAt: Long,
)
