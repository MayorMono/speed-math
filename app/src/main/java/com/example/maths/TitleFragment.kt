package com.example.maths

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.navigation.fragment.findNavController
import com.example.maths.MainActivity.Companion.bestTimeEasy
import com.example.maths.MainActivity.Companion.bestTimeEasyString
import com.example.maths.MainActivity.Companion.bestTimeHard
import com.example.maths.MainActivity.Companion.bestTimeHardString
import com.example.maths.MainActivity.Companion.difficulty
import com.example.maths.databinding.FragmentTitleBinding
import java.io.FileNotFoundException

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TitleFragment : Fragment() {

    private var _binding: FragmentTitleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTitleBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            loadHighScore()
        } catch (e: FileNotFoundException) {
            // File not found
        }

//        binding.highScore.text = highScoreEasy.toString()


//        binding.highScore.text = bestTimeEasyString
        if (difficulty == 0) {
            binding.easy.isChecked = true
            binding.highScore.text = bestTimeEasyString
            binding.highScore.isInvisible = bestTimeEasy.compareTo(0) == 0
        } else {
            binding.hard.isChecked = true
            binding.highScore.text = bestTimeHardString
            binding.highScore.isInvisible = bestTimeHard.compareTo(0) == 0
        }

        binding.buttonStart.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

//        binding.score.text = highScore.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadHighScore() {
        val score: List<String>? =
            context?.openFileInput("highScore")?.bufferedReader()?.useLines {
            it.toList()
        }
        bestTimeHard = score?.get(0)?.toLong()!!
        bestTimeEasy = score?.get(1)?.toLong()!!

        val minutesHard = bestTimeHard / 1000 /60
        var stringMinutesHard = ""
        val secondsHard = bestTimeHard / 1000 % 60
        var stringSecondsHard = ""

        // Calculate decimal part of seconds
        val doubleMinutesHardRaw: Double = bestTimeHard.toDouble() / 1000
        val doubleMinutesHardString = doubleMinutesHardRaw.toString()
        val decimalStringHard = doubleMinutesHardString.substring(doubleMinutesHardString.indexOf("."))

        stringMinutesHard = minutesHard.toString()

        stringSecondsHard = if (secondsHard < 10) {
            "0".plus(secondsHard.toString())
        } else {
            secondsHard.toString()
        }
        bestTimeHardString = stringMinutesHard.plus(":").plus(stringSecondsHard).plus(decimalStringHard)

        val minutesEasy = bestTimeEasy / 1000 /60
        var stringMinutesEasy = ""
        val secondsEasy = bestTimeEasy / 1000 % 60
        var stringSecondsEasy = ""

        // Calculate decimal part of seconds
        val doubleMinutesEasyRaw: Double = bestTimeEasy.toDouble() / 1000
        val doubleMinutesEasyString = doubleMinutesEasyRaw.toString()
        val decimalStringEasy = doubleMinutesEasyString.substring(doubleMinutesEasyString.indexOf("."))

        stringMinutesEasy = minutesEasy.toString()

        stringSecondsEasy = if (secondsEasy < 10) {
            "0".plus(secondsEasy.toString())
        } else {
            secondsEasy.toString()
        }
        bestTimeEasyString = stringMinutesEasy.plus(":").plus(stringSecondsEasy).plus(decimalStringEasy)
    }

//    private fun loadHighScore() {
//        val score: List<String>? =
//            context?.openFileInput("highScore")?.bufferedReader()?.useLines {
//                it.toList()
//            }
//        hardTime = score?.get(1).toString()
//        easyTime = score?.get(3).toString()
//    }

    private fun calculateTime() {

    }
}