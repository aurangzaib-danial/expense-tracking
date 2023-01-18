package com.example.expensetracking.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the entries from the database
 */
@Dao
interface EntryDao {
    @Query("SELECT * from entry")
    fun getEntries(): Flow<List<Entry>>

    @Query("SELECT * FROM entry WHERE id = :id")
    fun getEntry(id: Int): Flow<Entry>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entry: Entry)

    @Update
    suspend fun update(entry: Entry)

    @Delete
    suspend fun delete(entry: Entry)
}