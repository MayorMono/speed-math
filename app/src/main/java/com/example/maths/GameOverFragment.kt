package com.example.maths

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.maths.SpeedMath.Companion.bestAudioTimeEasy
import com.example.maths.SpeedMath.Companion.bestAudioTimeEasyString
import com.example.maths.SpeedMath.Companion.bestAudioTimeHard
import com.example.maths.SpeedMath.Companion.bestAudioTimeHardString
import com.example.maths.SpeedMath.Companion.bestTimeEasy
import com.example.maths.SpeedMath.Companion.bestTimeEasyString
import com.example.maths.SpeedMath.Companion.bestTimeHard
import com.example.maths.SpeedMath.Companion.bestTimeHardString
import com.example.maths.databinding.FragmentGameOverBinding
import com.example.maths.SpeedMath.Companion.currScore
import com.example.maths.SpeedMath.Companion.gameTime
import com.example.maths.SpeedMath.Companion.formatTime
import com.example.maths.SpeedMath.Companion.difficulty
import com.example.maths.SpeedMath.Companion.db
import com.example.maths.SpeedMath.Companion.gameMode
import com.example.maths.QuestionFragment.Companion.addTimes
import com.example.maths.QuestionFragment.Companion.subTimes
import com.example.maths.QuestionFragment.Companion.mulTimes
import com.example.maths.QuestionFragment.Companion.divTimes

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

//            saveTimes()
            val gameTimeFormatted = formatTime(gameTime)

            if (gameMode == 0) {
                if (difficulty == 1 && (gameTime < bestTimeHard || bestTimeHard.compareTo(0) == 0)) {
                    bestTimeHard = gameTime
                    bestTimeHardString = gameTimeFormatted
                    saveTimes()
                } else if (difficulty == 0 && (gameTime < bestTimeEasy || bestTimeEasy.compareTo(0) == 0)) {
                    bestTimeEasy = gameTime
                    bestTimeEasyString = gameTimeFormatted
                    saveTimes()
                }
            } else {
                if (difficulty == 1 && (gameTime < bestAudioTimeHard || bestAudioTimeHard.compareTo(0) == 0)) {
                    bestAudioTimeHard = gameTime
                    bestAudioTimeHardString = gameTimeFormatted
                    saveTimes()
                } else if (difficulty == 0 && (gameTime < bestAudioTimeEasy || bestAudioTimeEasy.compareTo(0) == 0)) {
                    bestAudioTimeEasy = gameTime
                    bestAudioTimeEasyString = gameTimeFormatted
                    saveTimes()
                }
            }

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

    private fun saveTimes() {
        val recordDao = db!!.recordDao()

//        val prevFastest = recordDao.getFastestTime(difficulty, gameMode)
//        recordDao.deleteRecord(prevFastest)

        val addSpeed: Double = calculateSpeed(addTimes)
        val subSpeed: Double = calculateSpeed(subTimes)
        val mulSpeed: Double = calculateSpeed(mulTimes)
        val divSpeed: Double = calculateSpeed(divTimes)

        recordDao.insertRecord(Record(gameTime, System.currentTimeMillis(), difficulty, gameMode, addSpeed, subSpeed, mulSpeed, divSpeed))
    }

    private fun calculateSpeed(times: ArrayList<Long>): Double {
        if (times.size == 0) {
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