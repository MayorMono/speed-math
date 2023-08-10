package com.example.maths

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.maths.SpeedMath.Companion.formatDate
import com.example.maths.databinding.FragmentSpeedBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SpeedFragment : Fragment() {

    private var _binding: FragmentSpeedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSpeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.modeSwitch.setOnCheckedChangeListener(onCheckedChanged())
        binding.difficultySwitch.setOnCheckedChangeListener(onCheckedChanged())

        constructGraph(0,0)

        binding.goToTime.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        binding.goToTitle.setOnClickListener {
            val switchActivityIntent = Intent(context, MainActivity::class.java)
            startActivity(switchActivityIntent)
            activity?.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun constructGraph(difficulty: Int, gameMode: Int) {
        val recentDao = SpeedMath.db!!.recentDao()
        val recentGames = recentDao.getHistory(difficulty, gameMode)
        val datasets = createLineDatasets(recentGames)

        val data = LineData(datasets)

        data.setDrawValues(false)

        binding.speedGraph.data = data
        binding.speedGraph.xAxis.valueFormatter = XAxisDateFormatter()
        binding.speedGraph.xAxis.labelRotationAngle = -45f

        val grey = Color.rgb(128, 128, 128)
        binding.speedGraph.axisLeft.textColor = grey
        binding.speedGraph.axisRight.textColor = grey
        binding.speedGraph.xAxis.textColor = grey
        binding.speedGraph.legend.textColor = grey
        binding.speedGraph.description.textColor = grey

        binding.speedGraph.description.text = "Questions Per Second"
    }

    private fun createLineDatasets(games: List<Recent>): ArrayList<ILineDataSet> {
        val sets: ArrayList<ILineDataSet> = arrayListOf()

        val addSpeeds: ArrayList<Entry> = arrayListOf()
        val subSpeeds: ArrayList<Entry> = arrayListOf()
        val mulSpeeds: ArrayList<Entry> = arrayListOf()
        val divSpeeds: ArrayList<Entry> = arrayListOf()

        for (game: Recent in games) {
            val dateTime = game.dateTime.toFloat()

            addSpeeds.add(Entry(dateTime, game.addSpeed.toFloat()))
            subSpeeds.add(Entry(dateTime, game.subSpeed.toFloat()))
            mulSpeeds.add(Entry(dateTime, game.mulSpeed.toFloat()))
            divSpeeds.add(Entry(dateTime, game.divSpeed.toFloat()))
        }

        val lineWidth = 3f

        val addSet = LineDataSet(addSpeeds, "Addition")
        addSet.color = Color.RED
        addSet.lineWidth = lineWidth

        val subSet = LineDataSet(subSpeeds, "Subtraction")
        subSet.color = Color.GREEN
        subSet.lineWidth = lineWidth

        val mulSet = LineDataSet(mulSpeeds, "Multiplication")
        mulSet.color = Color.BLUE
        mulSet.lineWidth = lineWidth

        val divSet = LineDataSet(divSpeeds, "Subtraction")
        divSet.color = Color.MAGENTA
        divSet.lineWidth = lineWidth

        sets.add(addSet)
        sets.add(subSet)
        sets.add(mulSet)
        sets.add(divSet)

        return sets
    }

    private fun onCheckedChanged(): CompoundButton.OnCheckedChangeListener {
        return CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            var gameMode = 0
            var difficulty = 0

            when (buttonView.id) {
                R.id.mode_switch -> {
                     gameMode = if (isChecked) {
                        1
                    } else {
                        0
                    }
                }
                R.id.difficulty_switch -> {
                    difficulty = if (isChecked) {
                        1
                    } else {
                        0
                    }
                }
            }

            binding.speedGraph.invalidate()
            binding.speedGraph.clear()
            constructGraph(difficulty, gameMode)
        }
    }
}

class XAxisDateFormatter: IndexAxisValueFormatter() {
    @Override
    override fun getFormattedValue(value: Float): String {
        val ms = value.toLong()
        return formatDate(ms)
    }
}