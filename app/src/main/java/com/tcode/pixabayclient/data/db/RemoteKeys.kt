package com.tcode.pixabayclient.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val query: String,
    val page: Int,
    val nextKey: Int?,
)
