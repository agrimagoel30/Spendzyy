package com.agrima.spendzyy.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.agrima.spendzyy.data.local.dao.ExpenseDao
import com.agrima.spendzyy.data.local.dao.NoteDao
import com.agrima.spendzyy.data.local.entity.ExpenseEntity
import com.agrima.spendzyy.data.local.entity.NoteEntity

@Database(
    entities = [ExpenseEntity::class,
        NoteEntity::class],
    version = 2,
    exportSchema = false
)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao
    abstract fun noteDao(): NoteDao

    companion object {

        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        fun getInstance(context: Context): ExpenseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_db"
                ).fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
