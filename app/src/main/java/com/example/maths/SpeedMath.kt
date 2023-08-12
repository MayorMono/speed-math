package com.example.maths

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import java.text.DateFormat
import java.text.DateFormat.SHORT
import java.util.Date

class SpeedMath: Application() {

    companion object {

        fun formatTime(ms: Long): String {
            val minutes = ms / 1000 /60
            val seconds = ms / 1000 % 60

            // Calculate decimal part of seconds
            val doubleMinutesRaw: Double = ms.toDouble() / 1000
            val doubleMinutesString = doubleMinutesRaw.toString()
            val decimalStringHard = doubleMinutesString.substring(doubleMinutesString.indexOf("."))

            val stringMinutes = minutes.toString()

            val stringSeconds = if (seconds < 10) {
                "0".plus(seconds.toString())
            } else {
                seconds.toString()
            }
            return stringMinutes.plus(":").plus(stringSeconds).plus(decimalStringHard)
        }

        fun formatDate(ms: Long, includeTime: Boolean) : String {

            val df: DateFormat = if (includeTime) {
                DateFormat.getDateTimeInstance(SHORT, SHORT)
            } else {
                DateFormat.getDateInstance(SHORT)
            }

            val resultDate = Date(ms)
            return df.format(resultDate)
        }

        fun timestampToBeginningOfDay(ms: Long) : Long {
            val df: DateFormat = DateFormat.getDateInstance(SHORT)
            val date = Date(ms)
            val dateStr = df.format(date)

            val dt: Date? = df.parse(dateStr)
            return dt!!.time
        }

        var tts: TextToSpeech? = null
        var db: AppDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()
        var instance = this

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "sm-database"
        ).allowMainThreadQueries().build()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

}