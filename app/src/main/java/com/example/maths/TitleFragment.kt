package com.example.maths

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
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
import com.example.maths.SpeedMath.Companion.difficulty
import com.example.maths.SpeedMath.Companion.gameMode
import com.example.maths.SpeedMath.Companion.tts
import com.example.maths.databinding.FragmentTitleBinding
import java.util.Locale

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

        if (gameMode == 0) {
            binding.textMode.isChecked = true
            if (difficulty == 0) {
                binding.easy.isChecked = true
                binding.highScore.text = bestTimeEasyString
                binding.highScore.isInvisible = bestTimeEasy.compareTo(0) == 0
            } else {
                binding.hard.isChecked = true
                binding.highScore.text = bestTimeHardString
                binding.highScore.isInvisible = bestTimeHard.compareTo(0) == 0
            }
        } else {
            binding.audioMode.isChecked = true
            if (difficulty == 0) {
                binding.easy.isChecked = true
                binding.highScore.text = bestAudioTimeEasyString
                binding.highScore.isInvisible = bestAudioTimeEasy.compareTo(0) == 0
            } else {
                binding.hard.isChecked = true
                binding.highScore.text = bestAudioTimeHardString
                binding.highScore.isInvisible = bestAudioTimeHard.compareTo(0) == 0
            }
        }

        binding.buttonStart.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.buttonStats.setOnClickListener {
            val switchActivityIntent = Intent(context, StatsActivity::class.java)
            startActivity(switchActivityIntent)
            activity?.finish()
        }

        if (tts == null) {
            tts = TextToSpeech(activity, TextToSpeech.OnInitListener { status ->
                if (status != TextToSpeech.ERROR) {
                    tts!!.language = Locale.US
                    binding.gameMode.isEnabled = true
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}