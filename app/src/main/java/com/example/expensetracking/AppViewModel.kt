package com.example.expensetracking

import android.content.ClipData
import androidx.lifecycle.*
import com.example.expensetracking.data.Entry
import com.example.expensetracking.data.EntryDao
import kotlinx.coroutines.launch

/**
 * View Model to keep a reference to the Expense Tracking repository and an up-to-date list of all entries.
 *
 */
class AppViewModel(private val entryDao: EntryDao) : ViewModel() {
    // Cache all entries form the database using LiveData.
    val allEntries: LiveData<List<Entry>> = entryDao.getEntries().asLiveData()


    /**
     * Returns true if the EditTexts are not empty
     */
    fun isEntryValid(entryName: String, entryCategory: String, entryDate: String, entryAmount: String): Boolean {
        if (entryName.isBlank() || entryCategory.isBlank() || entryDate.isBlank() || entryAmount.isBlank()) {
            return false
        }
        return true
    }

    /**
     * Inserts the new Entry into the database.
     */
    fun addNewEntry(entryName: String, entryCategory: String, entryDate: String, entryAmount: String, entryNote: String) {
        val newEntry = getNewEntry(entryName, entryCategory, entryDate, entryAmount, entryNote)
        insertEntry(newEntry)
    }

    /**
     * Retrieve an item from the repository.
     */
    fun retrieveEntry(id: Int): LiveData<Entry> {
        return entryDao.getEntry(id).asLiveData()
    }

    /**
     * Updates an existing Entry in the database.
     */
    fun updateEntry(
        entryId: Int,
        entryName: String,
        entryCategory: String,
        entryDate: String,
        entryAmount: String,
        entryNote: String
    ) {
        val updatedEntry = getUpdatedEntry(entryId, entryName, entryCategory, entryDate, entryAmount, entryNote)
        updateEntry(updatedEntry)
    }

    /**
     * Launching a new coroutine to delete an entry in a non-blocking way
     */
    fun deleteEntry(entry: Entry) {
        viewModelScope.launch {
            entryDao.delete(entry)
        }
    }



    /**
     * Returns an instance of the [Entry] entity class with the entry info entered by the user.
     * This will be used to add a new entry to the App database.
     */
    private fun getNewEntry(entryName: String, entryCategory: String, entryDate: String, entryAmount: String, entryNote: String) : Entry {
        return Entry(
            entryName = entryName,
            entryCategory = entryCategory,
            entryDate = entryDate,
            entryAmount = entryAmount.toDouble(),
            entryNote = entryNote
        )
    }

    /**
     * Launching a new coroutine to insert an entry in a non-blocking way
     */
    private fun insertEntry(entry: Entry) {
        viewModelScope.launch {
            entryDao.insert(entry)
        }
    }

    /**
     * Called to update an existing entry in the database.
     * Returns an instance of the [Entry] entity class with the entry info updated by the user.
     */
    private fun getUpdatedEntry(entryId: Int, entryName: String, entryCategory: String, entryDate: String, entryAmount: String, entryNote: String) : Entry {
        return Entry(
            id = entryId,
            entryName = entryName,
            entryCategory = entryCategory,
            entryDate = entryDate,
            entryAmount = entryAmount.toDouble(),
            entryNote = entryNote
        )
    }

    private fun updateEntry(entry: Entry) {
        viewModelScope.launch {
            entryDao.update(entry)
        }
    }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */

class AppViewModelFactory(private val entryDao: EntryDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(entryDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}