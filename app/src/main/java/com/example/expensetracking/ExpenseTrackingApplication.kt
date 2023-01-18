package com.example.expensetracking

import android.app.Application
import com.example.expensetracking.data.AppRoomDatabase

class ExpenseTrackingApplication : Application() {
    // Using by lazy so the database is only created when needed
    // rather than when the application starts
    val database: AppRoomDatabase by lazy { AppRoomDatabase.getDatabase(this) }
}