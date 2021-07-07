package com.example.maths

import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.maths.MainActivity.Companion.bestTimeEasy
import com.example.maths.MainActivity.Companion.bestTimeHard
import com.example.maths.databinding.FragmentQuestionBinding
import java.lang.NumberFormatException
import com.example.maths.MainActivity.Companion.currScore
import com.example.maths.MainActivity.Companion.difficulty
import com.example.maths.MainActivity.Companion.gameTime

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class QuestionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null
    private var currAnswer = 0
    private var startTime: Long = 0
    private var endTime: Long = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
//    private val timer = object: CountDownTimer(20000, 1000) {
//        override fun onTick(millisUntilFinished: Long) {
//            binding.time.text = (millisUntilFinished/1000).toString()
//        }
//
//        override fun onFinish() {
//            endGame()
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
//            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            try {
                submitAnswer(view)
            } catch (e: NumberFormatException) {
                endGame()
            }
        }
        currScore = 0
        binding.currScore.text = currScore.toString().plus("/20")
        createQuestion(view)
        binding.chronometer.start()
//        startTime = System.currentTimeMillis()

//        chronometer.setOnChronometerTickListener {
//            binding.time.text = chronometer.format
//        }

//        object : CountDownTimer(20000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                binding.time.text = (millisUntilFinished/1000).toString()
//            }
//
//            override fun onFinish() {
//                endGame()
//            }
//        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createQuestion(view: View) {
        val operation = (0 until 2).random()
        if (operation == 0) {
            giveAddition(view)
        } else {
            giveMultiplication(view)
        }
    }

    private fun giveAddition(view: View) {
        var first = 0
        var second = 0

        if (difficulty == 0) {
            first = (1 until 100).random()
            second = (1 until 100).random()
        } else if (difficulty == 1) {
            first = (1 until 1000).random()
            second = (1 until 1000).random()
        }

        currAnswer = first + second
        binding.firstNumber.text = first.toString()
        binding.secondNumber.text = second.toString()
        binding.operation.text = "+"
    }

    private fun giveMultiplication(view: View) {
        var first = 0
        var second = 0

        if (difficulty == 0) {
            first = (1 until 10).random()
            second = (1 until 10).random()
        } else if (difficulty == 1) {
            first = (1 until 13).random()
            second = (1 until 13).random()
        }

        currAnswer = first * second
        binding.firstNumber.text = first.toString()
        binding.secondNumber.text = second.toString()
        binding.operation.text = "×"
    }

    private fun submitAnswer(view: View) {
        if (binding.userAnswer.text.toString().toInt() == currAnswer) {
//            val toast = Toast.makeText(context, "Correct", Toast.LENGTH_SHORT)
//            toast.show()
            currScore++
            binding.userAnswer.text.clear()
            binding.currScore.text = currScore.toString().plus("/20")
            if (currScore == 20) {
                endGame()
            } else {
                createQuestion(view)
            }
        } else {
            endGame()
        }

    }

//    private fun endGame() {
//        gameTime = SystemClock.elapsedRealtime() - binding.chronometer.base
//        binding.chronometer.stop()
//        findNavController().navigate(R.id.action_SecondFragment_to_ThirdFragment)
        // Check if player has completed 20 questions
//        if (difficulty == 1 && currScore > highScoreHard) {
//            highScoreHard = currScore
//
//        } else if (difficulty == 0 && currScore > highScoreEasy) {
//            highScoreEasy = currScore
//        }
//        val filename = "highScore"
//        val fileContents = highScoreHard.toString() + "\n" + highScoreEasy.toString()
//        context?.openFileOutput(filename, 0).use {
//            it?.write(fileContents.toByteArray())
//        }
//    }

    private fun endGame() {
        gameTime = SystemClock.elapsedRealtime() - binding.chronometer.base
        binding.chronometer.stop()
        findNavController().navigate(R.id.action_SecondFragment_to_ThirdFragment)
    }
}