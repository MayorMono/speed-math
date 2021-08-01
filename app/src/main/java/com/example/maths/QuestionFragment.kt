package com.example.maths

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.maths.MainActivity.Companion.currScore
import com.example.maths.MainActivity.Companion.difficulty
import com.example.maths.MainActivity.Companion.gameTime
import com.example.maths.databinding.FragmentQuestionBinding

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
            try {
                submitAnswer(view)
            } catch (e: NumberFormatException) {
                endGame()
            }
        }
        currScore = 0
        binding.currScore.text = currScore.toString().plus("/20")
        binding.userAnswer.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                try {
                    submitAnswer(view)
                } catch (e: NumberFormatException) {
                    endGame()
                }
                true
            } else {
                false
            }
        }

        showKeyboard()
        createQuestion(view)
        binding.chronometer.start()
    }

    private fun showKeyboard() {
        binding.userAnswer.requestFocus()
        val imm: InputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.userAnswer, InputMethodManager.SHOW_IMPLICIT)
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
            first = (0 until 100).random()
            second = (0 until 100).random()
        } else if (difficulty == 1) {
            first = (0 until 1000).random()
            second = (0 until 1000).random()
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
            first = (0 until 10).random()
            second = (0 until 10).random()
        } else if (difficulty == 1) {
            first = (0 until 13).random()
            second = (0 until 13).random()
        }

        currAnswer = first * second
        binding.firstNumber.text = first.toString()
        binding.secondNumber.text = second.toString()
        binding.operation.text = "×"
    }

    private fun submitAnswer(view: View) {
        if (binding.userAnswer.text.toString().toInt() == currAnswer) {
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

    private fun endGame() {
        gameTime = SystemClock.elapsedRealtime() - binding.chronometer.base
        binding.chronometer.stop()
        findNavController().navigate(R.id.action_SecondFragment_to_ThirdFragment)
    }
}