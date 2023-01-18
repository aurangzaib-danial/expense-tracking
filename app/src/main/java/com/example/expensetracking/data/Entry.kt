package com.example.expensetracking.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity data class represents a single row in the database.
 */
@Entity
data class Entry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val entryName: String,
    @ColumnInfo(name = "category")
    val entryCategory: String,
    @ColumnInfo(name = "date")
    val entryDate: String,
    @ColumnInfo(name = "amount")
    val entryAmount: Double,
    @ColumnInfo(name = "note")
    val entryNote: String
)