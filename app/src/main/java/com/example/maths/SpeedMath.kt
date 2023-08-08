package com.example.maths

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.room.Room
import java.io.FileNotFoundException
import java.text.DateFormat
import java.text.DateFormat.SHORT
import java.util.Date

class SpeedMath: Application() {

    companion object {
        var bestTimeHard: Long = 0
        var bestTimeHardString: String = ""
        var bestTimeEasy: Long = 0
        var bestTimeEasyString: String = ""
        var bestAudioTimeHard: Long = 0
        var bestAudioTimeEasy: Long = 0
        var bestAudioTimeHardString: String = ""
        var bestAudioTimeEasyString: String = ""
        var currScore = 0
        var difficulty = 0
        var gameMode = 0
        var gameTime: Long = 0

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

        fun formatDate(ms: Long) : String {
            val df: DateFormat = DateFormat.getDateTimeInstance(SHORT, SHORT)
            val resultDate = Date(ms)
            return df.format(resultDate)
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

        try {
            loadHighScore()
        } catch (e: FileNotFoundException) {
            // File not found
        }
    }

    private fun loadHighScore() {
        val recordDao = db!!.recordDao()
        val scores = recordDao.getAllFastestTimes()
        for (score in scores) {
            if (score.gameMode == 0) {
                if (score.difficulty == 0) {
                    bestTimeEasy = score.gameTime
                } else {
                    bestTimeHard = score.gameTime
                }
            } else {
                if (score.difficulty == 0) {
                    bestAudioTimeEasy = score.gameTime
                } else{
                    bestAudioTimeHard = score.gameTime
                }
            }
            bestTimeHardString = formatTime(bestTimeHard)
            bestTimeEasyString = formatTime(bestTimeEasy)
            bestAudioTimeEasyString = formatTime(bestAudioTimeEasy)
            bestAudioTimeHardString = formatTime(bestAudioTimeHard)
        }
    }


}