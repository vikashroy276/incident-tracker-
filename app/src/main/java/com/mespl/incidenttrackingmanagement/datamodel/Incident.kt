package com.mespl.incidenttrackingmanagement.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incident_table")
data class Incident(
    @PrimaryKey(autoGenerate = true)
    val incidenceId: Int = 0,
    val incidence: String,
    val description: String,
    val userName : String,
    val email : String,
    val date : String,
    val createdBy: String,
    val status: String,
    val remarks: String
)

