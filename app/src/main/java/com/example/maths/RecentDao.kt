package com.example.maths

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecentDao {
    @Insert
    fun insert(recent: Recent)

    @Delete
    fun delete(recent: Recent)

    @Query("DELETE FROM recent")
    fun deleteAll()

    @Query("SELECT * FROM recent WHERE difficulty = :difficulty AND gameMode = :gameMode ORDER BY dateTime ASC")
    fun getHistory(difficulty: Int, gameMode: Int): List<Recent>

    @Query("DELETE FROM recent WHERE dateTime = (SELECT MIN(dateTime) FROM recent WHERE difficulty = :difficulty AND gameMode = :gameMode)")
    fun deleteLeastRecent(difficulty: Int, gameMode: Int)

    @Query("SELECT COUNT(*) FROM recent WHERE difficulty = :difficulty AND gameMode = :gameMode")
    fun getCount(difficulty:Int, gameMode: Int): Int
    

}