package com.mespl.incidenttrackingmanagement.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mespl.incidenttrackingmanagement.datamodel.Incident
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(incident: Incident)

    @Query("SELECT * FROM incident_table")
    suspend fun getAll(): List<Incident>

    @Query("SELECT COUNT(*) FROM incident_table")
    suspend fun getIncidentCount(): Int

    @Query("SELECT COUNT(*) FROM incident_table WHERE status = :status")
    suspend fun getCountByStatus(status: String): Int

    @Query("SELECT * FROM incident_table WHERE status = :status")
    suspend fun getIncidentsByStatus(status: String): List<Incident>

    @Query("SELECT * FROM incident_table WHERE incidenceId = :id LIMIT 1")
    suspend fun getIncidentById(id: Int): Incident

    @Query("UPDATE incident_table SET status = :newStatus WHERE incidenceId = :id")
    suspend fun updateStatus(id: Int, newStatus: String)

    @Query("UPDATE incident_table SET status = :status, remarks = :remarks WHERE incidenceId = :id")
    suspend fun updateStatusAndRemarks(id: Int, status: String, remarks: String)

    @Query("SELECT COUNT(*) FROM incident_table")
    fun getIncidentCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM incident_table WHERE status = :status")
    fun getCountByStatusFlow(status: String): Flow<Int>

    @Query("SELECT * FROM incident_table WHERE status = :status AND userName = :userName")
    suspend fun getIncidentsByStatusAndUser(status: String, userName: String): List<Incident>

}