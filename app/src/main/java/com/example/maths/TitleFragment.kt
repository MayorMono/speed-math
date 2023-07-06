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
import com.example.maths.MainActivity.Companion.formatTime
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

        bestTimeHardString = formatTime(bestTimeHard)
        bestTimeEasyString = formatTime(bestTimeEasy)
    }
}