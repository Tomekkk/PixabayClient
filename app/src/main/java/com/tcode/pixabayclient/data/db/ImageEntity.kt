package com.tcode.pixabayclient.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "images",
    indices = [Index(value = ["query"], unique = false)],
)
data class ImageEntity(
    /**
     * Primary key required for persist correct order of images
     */
    @PrimaryKey(autoGenerate = true)
    val _id: Long,
    val imageId: Long,
    val tags: String,
    val previewURL: String,
    val previewWidth: Int,
    val previewHeight: Int,
    val largeImageURL: String,
    val imageWidth: Int,
    val imageHeight: Int,
    val downloads: Long,
    val likes: Long,
    val comments: Long,
    val user: String,
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val query: String,
)
