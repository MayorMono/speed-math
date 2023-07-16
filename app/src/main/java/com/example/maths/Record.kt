package com.example.maths

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Record(
    val gameTime: Long,
    val dateTime: Long,
    val difficulty: Int,
    val gameMode: Int,
    @PrimaryKey(autoGenerate = true)
    val rid: Int = 0
)
