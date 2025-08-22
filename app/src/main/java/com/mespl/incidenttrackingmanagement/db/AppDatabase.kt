package com.mespl.incidenttrackingmanagement.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mespl.incidenttrackingmanagement.datamodel.Incident
import com.mespl.incidenttrackingmanagement.datamodel.TaskList
import com.mespl.incidenttrackingmanagement.datamodel.Transection
import com.mespl.incidenttrackingmanagement.datamodel.User

@Database(entities = [Incident::class, User::class, TaskList::class, Transection::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun incidentDao(): IncidentDao
    abstract fun userDao(): UserDao
    abstract fun taskListDao(): TaskListDao
    abstract fun transectionDao(): TransectionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                   AppDatabase::class.java,
                    "incidence_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}