package com.youknow.yts.data.local.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.youknow.yts.data.local.entity.SummaryEntity

@Dao
interface SummaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: SummaryEntity)

    @Query("SELECT * FROM SummaryEntity")
    suspend fun getAll(): List<SummaryEntity>
}