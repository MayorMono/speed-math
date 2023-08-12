package com.example.maths

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recent(
    val gameTime: Long,
    val dateTime: Long,
    val difficulty: Int,
    val gameMode: Int,
    val addSpeed: Double,
    val subSpeed: Double,
    val mulSpeed: Double,
    val divSpeed: Double,
    @PrimaryKey(autoGenerate = true)
    val rid: Int = 0
)
