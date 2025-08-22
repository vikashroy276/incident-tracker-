package com.mespl.incidenttrackingmanagement.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_List")
data class TaskList(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int = 0,
    val assignedBy: String,
    val task : String,
    val assignedTo : String,
    val createdDate : String,
    val status: String
)
