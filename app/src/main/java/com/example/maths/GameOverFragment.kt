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
import com.example.maths.MainActivity.Companion.formatTime

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

            saveTimes()

            val gameTimeFormatted = formatTime(gameTime)

            binding.highScore.text = gameTimeFormatted

            if (MainActivity.difficulty == 0) {
                binding.titleText.text = getString(R.string.easy_complete)
                bestTimeEasyString = gameTimeFormatted
            } else {
                binding.titleText.text = getString(R.string.hard_complete)
                bestTimeHardString = gameTimeFormatted
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