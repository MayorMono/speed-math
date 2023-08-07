package com.example.maths

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.maths.databinding.FragmentSpeedBinding
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.util.ArrayList
import java.util.Date

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

//        binding.buttonSecond.setOnClickListener {
//            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
//        }
//        val test: ArrayList<DataPoint> = arrayListOf(
//            DataPoint(0.0, 1.0),
//            DataPoint(1.0, 5.0),
//            DataPoint(2.0, 3.0),
//            DataPoint(3.0, 2.0),
//            DataPoint(4.0, 6.0)
//        )
//
//        val test2: ArrayList<DataPoint> = arrayListOf(
//            DataPoint(0.0, 2.0),
//            DataPoint(1.0, 6.0),
//            DataPoint(2.0, 7.0),
//            DataPoint(3.0, 9.0),
//            DataPoint(8.0, 15.0)
//        )
//
//        val series1 = LineGraphSeries(test.toTypedArray())
//        val series2 = LineGraphSeries(test2.toTypedArray())
//
//        series1.title = "Series 1"
//        series2.title = "Series 2"
//
//        series1.color = Color.RED
//        series2.color = Color.BLUE
//
//        binding.speedGraph.addSeries(series1)
//        binding.speedGraph.addSeries(series2)

        val recordDao = SpeedMath.db!!.recordDao()
        val seriesList = createSeries(recordDao.getFastestTimeHistory(0, 0))

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

        binding.speedGraph.legendRenderer.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
}