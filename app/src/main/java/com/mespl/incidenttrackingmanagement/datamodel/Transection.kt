package com.mespl.incidenttrackingmanagement.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transection")
data class Transection(
    @PrimaryKey(autoGenerate = true)
    val transectionId: Int = 0,
    val incidentId: Int,
    val remarks: String,
    val status: String,
    val updatedBy: String,
    val dateTime: String
)
