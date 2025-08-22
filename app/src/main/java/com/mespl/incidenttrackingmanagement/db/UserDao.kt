package com.mespl.incidenttrackingmanagement.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mespl.incidenttrackingmanagement.datamodel.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)

    @Query("SELECT * FROM user_table")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM user_table WHERE name = :username AND password = :password LIMIT 1")
    suspend fun loginUser(username: String, password: String): User?

}

