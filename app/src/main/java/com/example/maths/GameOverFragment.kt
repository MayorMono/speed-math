package com.example.maths

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.maths.QuestionFragment.Companion.addTimes
import com.example.maths.QuestionFragment.Companion.divTimes
import com.example.maths.QuestionFragment.Companion.mulTimes
import com.example.maths.QuestionFragment.Companion.subTimes
import com.example.maths.MainActivity.Companion.currScore
import com.example.maths.SpeedMath.Companion.db
import com.example.maths.MainActivity.Companion.difficulty
import com.example.maths.SpeedMath.Companion.formatTime
import com.example.maths.MainActivity.Companion.gameMode
import com.example.maths.MainActivity.Companion.gameTime
import com.example.maths.databinding.FragmentGameOverBinding

class GameOverFragment: Fragment() {
    private var _binding: FragmentGameOverBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGameOverBinding.inflate(inflater, container, false)

        if (currScore == 20) {
            binding.scoreText.isInvisible = false
            binding.highScore.isInvisible = false

            updateDatabase()

            val gameTimeFormatted = formatTime(gameTime)
            binding.highScore.text = gameTimeFormatted

            if (difficulty == 0) {
                binding.titleText.text = getString(R.string.easy_complete)
            } else {
                binding.titleText.text = getString(R.string.hard_complete)
            }

        } else {
            binding.scoreText.isInvisible = true
            binding.highScore.isInvisible = true
        }
        return binding.root
    }

    private fun updateDatabase() {
        val recordDao = db!!.recordDao()
        val recentDao = db!!.recentDao()

        val addSpeed: Double = calculateSpeed(addTimes)
        val subSpeed: Double = calculateSpeed(subTimes)
        val mulSpeed: Double = calculateSpeed(mulTimes)
        val divSpeed: Double = calculateSpeed(divTimes)

        val currentDateTimeMs = System.currentTimeMillis()

        if (recentDao.getCount(difficulty, gameMode).compareTo(100) == 0) {
            recentDao.deleteLeastRecent(difficulty, gameMode)
        }

        recentDao.insert(Recent(gameTime, currentDateTimeMs, difficulty, gameMode, addSpeed, subSpeed, mulSpeed, divSpeed))

        val currentFastestTime: Long = recordDao.getFastestTime(difficulty, gameMode).gameTime

        if (gameTime < currentFastestTime || currentFastestTime.compareTo(0) == 0) {
            recordDao.insertRecord(Record(gameTime, currentDateTimeMs, difficulty, gameMode, addSpeed, subSpeed, mulSpeed, divSpeed))
        }
    }

    private fun calculateSpeed(times: ArrayList<Long>): Double {
        if (times.isEmpty()) {
            return 0.0
        }

        var sum: Double = 0.0
        for (time: Long in times) {
            sum += time.toDouble() / 1000
        }

        return times.size / sum
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRetry.setOnClickListener {
            findNavController().navigate(R.id.action_ThirdFragment_to_SecondFragment)
        }

        binding.buttonQuit.setOnClickListener {
            findNavController().navigate(R.id.action_ThirdFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}