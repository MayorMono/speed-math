package com.example.maths

import android.os.Bundle
import android.os.CountDownTimer
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Chronometer
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import com.example.maths.databinding.ActivityMainBinding
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.lang.Float.POSITIVE_INFINITY

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    companion object {
        var bestTimeHard: Long = 0
        var bestTimeHardString: String = ""
        var bestTimeEasy: Long = 0
        var bestTimeEasyString: String = ""
        var currScore = 0
        var difficulty = 0
        var gameTime: Long = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        supportActionBar?.hide()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun setDifficulty(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            val scoreText: TextView = findViewById(R.id.high_score)
            when (view.getId()) {
                R.id.easy ->
                    if (checked) {
                        difficulty = 0
//                        scoreText.text = highScoreEasy.toString()
                        scoreText.text = bestTimeEasyString
                        scoreText.isInvisible = bestTimeEasy.compareTo(0) == 0
                    }
                R.id.hard ->
                    if (checked) {
                        difficulty = 1
//                        scoreText.text = highScoreHard.toString()
                        scoreText.text = bestTimeHardString
                        scoreText.isInvisible = bestTimeHard.compareTo(0) == 0
                    }
            }
        }
    }

    override fun onBackPressed() {
        // Do nothing
    }
}