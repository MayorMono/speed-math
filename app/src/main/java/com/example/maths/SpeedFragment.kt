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
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.util.Date

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SpeedFragment : Fragment() {

    private var gameMode = 0
    private var difficulty = 0

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

        binding.speedGraph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
            override fun formatLabel(value: Double, isValueX: Boolean): String {
                if (isValueX) {
                    return formatDate(value.toLong())
                }
                return super.formatLabel(value, isValueX)
            }
        }

        constructGraph(0, 0)

        binding.speedGraph.legendRenderer.isVisible = true
        binding.speedGraph.legendRenderer.align = LegendRenderer.LegendAlign.TOP

//        binding.speedGraph.viewport.isScalable = true
//        binding.speedGraph.viewport.isScrollable = true
        binding.speedGraph.gridLabelRenderer.setHorizontalLabelsAngle(90)

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
        val recordDao = SpeedMath.db!!.recordDao()
        val seriesList = createSeries(recordDao.getFastestTimeHistory(difficulty, gameMode))

        seriesList[0].title = "Addition"
        seriesList[0].color = Color.RED

        seriesList[1].title = "Subtraction"
        seriesList[1].color = Color.GREEN

        seriesList[2].title = "Multiplication"
        seriesList[2].color = Color.BLUE

        seriesList[3].title = "Division"
        seriesList[3].color = Color.MAGENTA

        for (series: LineGraphSeries<DataPoint> in seriesList) {
            binding.speedGraph.addSeries(series)
        }
    }

    private fun createSeries(records: List<Record>): ArrayList<LineGraphSeries<DataPoint>> {
        var seriesList: ArrayList<LineGraphSeries<DataPoint>> = arrayListOf()

        val addSpeeds: ArrayList<DataPoint> = arrayListOf()
        val subSpeeds: ArrayList<DataPoint> = arrayListOf()
        val mulSpeeds: ArrayList<DataPoint> = arrayListOf()
        val divSpeeds: ArrayList<DataPoint> = arrayListOf()

        for (i in records.lastIndex downTo 0) {
            addSpeeds.add(DataPoint(Date(records[i].dateTime), records[i].addSpeed))
            subSpeeds.add(DataPoint(Date(records[i].dateTime), records[i].subSpeed))
            mulSpeeds.add(DataPoint(Date(records[i].dateTime), records[i].mulSpeed))
            divSpeeds.add(DataPoint(Date(records[i].dateTime), records[i].divSpeed))
        }

        val addSeries = LineGraphSeries(addSpeeds.toTypedArray())
        val subSeries = LineGraphSeries(subSpeeds.toTypedArray())
        val mulSeries = LineGraphSeries(mulSpeeds.toTypedArray())
        val divSeries = LineGraphSeries(divSpeeds.toTypedArray())

        seriesList.add(addSeries)
        seriesList.add(subSeries)
        seriesList.add(mulSeries)
        seriesList.add(divSeries)

        return seriesList
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

            binding.speedGraph.removeAllSeries()
            constructGraph(difficulty, gameMode)
        }
    }
}