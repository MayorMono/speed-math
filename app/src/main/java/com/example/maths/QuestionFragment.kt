package com.example.maths

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.maths.MainActivity.Companion.currScore
import com.example.maths.MainActivity.Companion.difficulty
import com.example.maths.MainActivity.Companion.gameMode
import com.example.maths.MainActivity.Companion.gameTime
import com.example.maths.MainActivity.Companion.tts
import com.example.maths.databinding.FragmentQuestionBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class QuestionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null
    private var startTime: Long = 0
    private var endTime: Long = 0

    companion object {
        private var first = 0
        private var second = 0
        private var operation: String = "+"
        private var currAnswer = 0

        private var questionStartTime: Long = 0

        lateinit var addTimes: ArrayList<Long>
        lateinit var subTimes: ArrayList<Long>
        lateinit var mulTimes: ArrayList<Long>
        lateinit var divTimes: ArrayList<Long>
    }

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

        addTimes = arrayListOf()
        subTimes = arrayListOf()
        mulTimes = arrayListOf()
        divTimes = arrayListOf()

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
        if (gameMode == 0) {
            binding.firstNumber.isVisible = true
            binding.secondNumber.isVisible = true
            binding.operation.isVisible = true
            binding.repeatQuestion.isVisible = false
        } else {
            binding.firstNumber.isVisible = false
            binding.secondNumber.isVisible = false
            binding.operation.isVisible = false
            binding.repeatQuestion.isVisible = true

            binding.repeatQuestion.setOnClickListener {
                if (!tts!!.isSpeaking) {
                    speakQuestion()
                }
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
        when ((0 until 4).random()) {
            0 -> giveAddition(view)
            1 -> giveMultiplication(view)
            2 -> giveSubtraction(view)
            3 -> giveDivision(view)
        }

        binding.firstNumber.text = first.toString()
        binding.secondNumber.text = second.toString()
        binding.operation.text = operation

        questionStartTime = SystemClock.elapsedRealtime()

        if (gameMode == 1) {
            speakQuestion()
            binding.firstNumber.text = ""
            binding.secondNumber.text = ""
            binding.operation.text = ""
        }
    }

    private fun giveAddition(view: View) {
        if (difficulty == 0) {
            first = (0 until 100).random()
            second = (0 until 100).random()
        } else if (difficulty == 1) {
            first = (0 until 1000).random()
            second = (0 until 1000).random()
        }
        currAnswer = first + second
        operation = "+"
    }

    private fun giveMultiplication(view: View) {
        if (difficulty == 0) {
            first = (0 until 10).random()
            second = (0 until 10).random()
        } else if (difficulty == 1) {
            first = (0 until 13).random()
            second = (0 until 13).random()
        }
        currAnswer = first * second
        operation = "x"
    }

    private fun giveSubtraction(view: View) {
        if (difficulty == 0) {
            first = (0 until 199).random()
            second = (0 until first + 1).random()
        } else if (difficulty == 1) {
            first = (0 until 1998).random()
            second = (0 until first + 1).random()
        }
        currAnswer = first - second
        operation = "-"
    }

    private fun giveDivision(view: View) {
        if (difficulty == 0) {
            second = (1 until 10).random()
            currAnswer = (0 until 10).random()
        } else if (difficulty == 1) {
            second = (1 until 13).random()
            currAnswer = (0 until 13).random()
        }
        first = currAnswer * second
        operation = "÷"
    }

    private fun speakQuestion() {
        val ttsOperator = when (operation) {
            "x" -> {
                "times"
            }
            "+" -> {
                "plus"
            }
            "-" -> {
                "minus"
            }
            else -> {
                "divided by"
            }
        }
        tts!!.speak("$first $ttsOperator $second", TextToSpeech.QUEUE_ADD, null)
    }

    private fun submitAnswer(view: View) {
        if (binding.userAnswer.text.toString().toInt() == currAnswer) {
            val questionTime = SystemClock.elapsedRealtime() - questionStartTime

            when (operation) {
                "x" -> {
                    mulTimes.add(questionTime)
                }
                "+" -> {
                    addTimes.add(questionTime)
                }
                "-" -> {
                    subTimes.add(questionTime)
                }
                else -> {
                    divTimes.add(questionTime)
                }
            }

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
        if (tts != null) {
            tts!!.stop()
        }
        findNavController().navigate(R.id.action_SecondFragment_to_ThirdFragment)
    }
}