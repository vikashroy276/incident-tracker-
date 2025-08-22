package com.mespl.incidenttrackingmanagement.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mespl.incidenttrackingmanagement.datamodel.Transection

@Dao
interface TransectionDao {
    @Insert
    suspend fun insertTransection(transection: Transection)

    @Query("SELECT * FROM transection WHERE incidentId = :incidentId ORDER BY transectionId DESC")
    suspend fun getTransectionsByIncidentId(incidentId: Int): List<Transection>
}