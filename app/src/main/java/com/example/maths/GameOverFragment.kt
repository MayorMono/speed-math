package com.example.maths

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.maths.MainActivity.Companion.bestTimeEasy
import com.example.maths.MainActivity.Companion.bestTimeEasyString
import com.example.maths.MainActivity.Companion.bestTimeHard
import com.example.maths.MainActivity.Companion.bestTimeHardString
import com.example.maths.databinding.FragmentGameOverBinding
import com.example.maths.MainActivity.Companion.currScore
import com.example.maths.MainActivity.Companion.gameTime

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
//        binding.highScore.text = currScore.toString()
        if (currScore == 20) {
            binding.scoreText.isInvisible = false
            binding.highScore.isInvisible = false

            saveTimes()

            val minutes = gameTime / 1000 /60
            var stringMinutes = ""
            val seconds = gameTime / 1000 % 60
            var stringSeconds = ""

            val doubleMinutesRaw: Double = gameTime.toDouble() / 1000
            val doubleMinutesString = doubleMinutesRaw.toString()
            val decimalString = doubleMinutesString.substring(doubleMinutesString.indexOf("."))

            stringMinutes = minutes.toString()

            stringSeconds = if (seconds < 10) {
                "0".plus(seconds.toString())
            } else {
                seconds.toString()
            }
            val result: String = stringMinutes.plus(":").plus(stringSeconds).plus(decimalString)

            binding.highScore.text = result

            if (MainActivity.difficulty == 0) {
                binding.titleText.text = "Easy Mode Complete"
                bestTimeEasyString = result
            } else {
                binding.titleText.text = "Hard Mode Complete"
                bestTimeHardString = result
            }

        } else {
            binding.scoreText.isInvisible = true
            binding.highScore.isInvisible = true
        }
        return binding.root
    }

    private fun saveTimes() {
        if (MainActivity.difficulty == 1 && (gameTime < bestTimeHard || bestTimeHard.compareTo(0) == 0)) {
            bestTimeHard = gameTime
        } else if (MainActivity.difficulty == 0 && (gameTime < bestTimeEasy || bestTimeEasy.compareTo(0) == 0)) {
            bestTimeEasy = gameTime
        }
        val filename = "highScore"
        val fileContents = bestTimeHard.toString() + "\n" + bestTimeEasy.toString()
        context?.openFileOutput(filename, 0).use {
            it?.write(fileContents.toByteArray())
        }
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