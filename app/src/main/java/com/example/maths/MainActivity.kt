package com.example.maths

import android.app.UiModeManager.MODE_NIGHT_YES
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isInvisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.maths.databinding.ActivityMainBinding

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

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)

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
                        scoreText.text = bestTimeEasyString
                        scoreText.isInvisible = bestTimeEasy.compareTo(0) == 0
                    }
                R.id.hard ->
                    if (checked) {
                        difficulty = 1
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