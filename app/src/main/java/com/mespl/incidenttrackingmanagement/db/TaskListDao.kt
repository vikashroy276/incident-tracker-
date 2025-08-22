package com.mespl.incidenttrackingmanagement.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mespl.incidenttrackingmanagement.datamodel.TaskList
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskList)

    @Query("SELECT COUNT(*) FROM task_list WHERE assignedTo = :userName")
    fun getTaskCountFlow(userName: String): Flow<Int>

    // Get task count by status for a specific user
    @Query("SELECT COUNT(*) FROM task_list WHERE assignedTo = :userName AND status = :status")
    fun getTaskCountByStatusFlow(userName: String, status: String): Flow<Int>

    @Query("SELECT * FROM task_List WHERE status = :status AND assignedTo = :userName")
    suspend fun getIncidentsByStatusAndUser(status: String, userName: String): List<TaskList>

    @Query("UPDATE task_list SET status = :status WHERE taskId = :id")
    suspend fun updateTaskStatus(id: Int, status: String, )

    @Query("SELECT * FROM task_list WHERE taskId = :incidentId LIMIT 1")
    fun getTaskByIncidentId(incidentId: Int): TaskList?
}