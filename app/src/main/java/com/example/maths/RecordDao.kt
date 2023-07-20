package com.example.maths

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface RecordDao {
    @Upsert
    fun upsertRecord(record: Record)

    @Delete
    fun deleteRecord(record: Record)

    @Query("DELETE FROM record")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM record WHERE difficulty = :difficulty AND gameMode = :gameMode")
    fun countTimes(difficulty:Int, gameMode: Int): Int

    @Query("SELECT MAX(gameTime) as gameTime, dateTime, difficulty, gameMode, rid FROM record WHERE difficulty = :difficulty AND gameMode = :gameMode")
    fun getSlowestTime(difficulty: Int, gameMode: Int): Record

    @Query("SELECT MIN(gameTime) as gameTime, dateTime, difficulty, gameMode, rid FROM record WHERE difficulty = :difficulty AND gameMode = :gameMode")
    fun getFastestTime(difficulty: Int, gameMode: Int): Record

    @Query("SELECT MIN(gameTime) as gameTime, dateTime, difficulty, gameMode, rid FROM record GROUP BY gameMode, difficulty")
    fun getAllFastestTimes(): List<Record>
}