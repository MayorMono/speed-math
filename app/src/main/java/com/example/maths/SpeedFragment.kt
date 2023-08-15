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
import com.example.maths.SpeedMath.Companion.timestampToBeginningOfDay
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

    private var gameMode = 0
    private var difficulty = 0

    // Offset to dateTime value to ensure earliest date is at x = 0 on graph, and scaling constant
    // to ensure number of days since first game played is scaled to 1 unit on graph
    private var dateTimeGraphOffset: Long = 0
    private var dateTimeGraphScale: Long = 1

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

        val daySpeedEntries = getDaySpeedEntries(recentGames)

        if (daySpeedEntries.isNotEmpty()) {
            dateTimeGraphOffset = daySpeedEntries[0].dateTime

            val datasets = createLineDatasets(daySpeedEntries)

            val data = LineData(datasets)

            data.setDrawValues(false)

            binding.speedGraph.data = data
            binding.speedGraph.xAxis.valueFormatter = XAxisDateFormatter(dateTimeGraphOffset, dateTimeGraphScale)
            binding.speedGraph.xAxis.labelRotationAngle = -45f

            val grey = Color.rgb(128, 128, 128)
            binding.speedGraph.axisLeft.textColor = grey
            binding.speedGraph.axisRight.textColor = grey
            binding.speedGraph.xAxis.textColor = grey
            binding.speedGraph.legend.textColor = grey
            binding.speedGraph.description.textColor = grey

            binding.speedGraph.xAxis.isGranularityEnabled = true
            binding.speedGraph.xAxis.granularity = 1f

            binding.speedGraph.description.text = "Questions Per Second"
        }

    }

    private fun getDaySpeedEntries(games: List<Recent>): ArrayList<DaySpeedEntry> {

        val speedEntries: ArrayList<DaySpeedEntry> = ArrayList()
        var prevDateTime: Long = 0

        for (game: Recent in games) {
            val roundedTimestamp: Long = timestampToBeginningOfDay(game.dateTime)

            if (roundedTimestamp.compareTo(prevDateTime) == 0) {
                if (game.addSpeed > 0.0) {
                    speedEntries[speedEntries.size - 1].addSpeedSum += game.addSpeed
                    speedEntries[speedEntries.size - 1].numAddGames++
                }

                if (game.subSpeed > 0.0) {
                    speedEntries[speedEntries.size - 1].subSpeedSum += game.subSpeed
                    speedEntries[speedEntries.size - 1].numSubGames++
                }

                if (game.mulSpeed > 0.0) {
                    speedEntries[speedEntries.size - 1].mulSpeedSum += game.mulSpeed
                    speedEntries[speedEntries.size - 1].numMulGames++
                }

                if (game.divSpeed > 0.0) {
                    speedEntries[speedEntries.size - 1].divSpeedSum += game.divSpeed
                    speedEntries[speedEntries.size - 1].numDivGames++
                }

            } else {
                val numAddGames = if (game.addSpeed > 0.0) {
                    1
                } else {
                    0
                }

                val numSubGames = if (game.subSpeed > 0.0) {
                    1
                } else {
                    0
                }

                val numMulGames = if (game.mulSpeed > 0.0) {
                    1
                } else {
                    0
                }

                val numDivGames = if (game.divSpeed > 0.0) {
                    1
                } else {
                    0
                }

                speedEntries.add(DaySpeedEntry(roundedTimestamp,
                    numAddGames, numSubGames, numMulGames, numDivGames,
                    game.addSpeed, game.subSpeed, game.mulSpeed, game.divSpeed))
                prevDateTime = roundedTimestamp
            }
        }

        return speedEntries
    }

    private fun createLineDatasets(daySpeedEntries: List<DaySpeedEntry>): ArrayList<ILineDataSet> {
        dateTimeGraphOffset = daySpeedEntries[0].dateTime

        val sets: ArrayList<ILineDataSet> = arrayListOf()

        val addSpeeds: ArrayList<Entry> = arrayListOf()
        val subSpeeds: ArrayList<Entry> = arrayListOf()
        val mulSpeeds: ArrayList<Entry> = arrayListOf()
        val divSpeeds: ArrayList<Entry> = arrayListOf()

        dateTimeGraphScale = if (daySpeedEntries.size > 1) {
            daySpeedEntries[1].dateTime - dateTimeGraphOffset
        } else {
            1
        }

        for (i in daySpeedEntries.indices) {
            val dateTime = ((daySpeedEntries[i].dateTime - dateTimeGraphOffset) / dateTimeGraphScale).toFloat()

            if (daySpeedEntries[i].numAddGames > 0) {
                addSpeeds.add(Entry(dateTime, (daySpeedEntries[i].addSpeedSum / daySpeedEntries[i].numAddGames).toFloat()))
            }

            if (daySpeedEntries[i].numSubGames > 0) {
                subSpeeds.add(Entry(dateTime, (daySpeedEntries[i].subSpeedSum / daySpeedEntries[i].numSubGames).toFloat()))
            }

            if (daySpeedEntries[i].numMulGames > 0) {
                mulSpeeds.add(Entry(dateTime, (daySpeedEntries[i].mulSpeedSum / daySpeedEntries[i].numMulGames).toFloat()))
            }

            if (daySpeedEntries[i].numDivGames > 0) {
                divSpeeds.add(Entry(dateTime, (daySpeedEntries[i].divSpeedSum / daySpeedEntries[i].numDivGames).toFloat()))
            }
        }

        val addSet = LineDataSet(addSpeeds, "Addition")
        val subSet = LineDataSet(subSpeeds, "Subtraction")
        val mulSet = LineDataSet(mulSpeeds, "Multiplication")
        val divSet = LineDataSet(divSpeeds, "Division")

        formatLine(addSet, Color.RED)
        formatLine(subSet, Color.GREEN)
        formatLine(mulSet, Color.BLUE)
        formatLine(divSet, Color.MAGENTA)

        sets.add(addSet)
        sets.add(subSet)
        sets.add(mulSet)
        sets.add(divSet)

        return sets
    }

    private fun formatLine(set: LineDataSet, color: Int) {
        val lineWidth = 3f
        val circleRadius = 5f

        set.color = color
        set.setCircleColor(color)
        set.lineWidth = lineWidth
        set.circleRadius = circleRadius
    }

    private fun onCheckedChanged(): CompoundButton.OnCheckedChangeListener {
        return CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

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

class XAxisDateFormatter(private val offset: Long, private val scale: Long): IndexAxisValueFormatter() {
    @Override
    override fun getFormattedValue(value: Float): String {
        val ms = (value.toLong() * scale) + offset
        return formatDate(ms, false)
    }
}

data class DaySpeedEntry (
    val dateTime: Long,

    // Number of games that have addition questions, subtraction questions, etc
    var numAddGames: Int,
    var numSubGames: Int,
    var numMulGames: Int,
    var numDivGames: Int,

    var addSpeedSum: Double,
    var subSpeedSum: Double,
    var mulSpeedSum: Double,
    var divSpeedSum: Double
)