package com.example.maths

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.CompoundButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.example.maths.databinding.FragmentTimeBinding
import com.example.maths.SpeedMath.Companion.formatTime
import java.text.DateFormat
import java.util.Date

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TimeFragment : Fragment() {

    private var gameMode = 0
    private var difficulty = 0
    private lateinit var timeAdapter: TimeAdapter

    private var _binding: FragmentTimeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTimeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recordDao = SpeedMath.db!!.recordDao()
        val records = recordDao.getFastestTimeHistory(0, 0)

        timeAdapter = TimeAdapter(requireContext(), records)

        binding.timeHistory.adapter = timeAdapter
        binding.modeSwitch.setOnCheckedChangeListener(onCheckedChanged())
        binding.difficultySwitch.setOnCheckedChangeListener(onCheckedChanged())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

            val recordDao = SpeedMath.db!!.recordDao()
            val newRecords = recordDao.getFastestTimeHistory(difficulty, gameMode)
            timeAdapter.updateData(newRecords)
        }
    }
}

class TimeAdapter(private val context: Context, private val data: List<Record>): BaseAdapter() {
    private var records = data

    override fun getCount(): Int {
        return records.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var cv = convertView
        if (cv == null) {
            cv = LayoutInflater.from(context).inflate(R.layout.time, parent, false)
        }
        val gameTime: TextView? = cv?.findViewById(R.id.gameTime)
        val dateTime: TextView? = cv?.findViewById(R.id.dateTime)

        val entry = records[position]

        gameTime!!.text = formatTime(entry.gameTime)
        dateTime!!.text = formatDate(entry.dateTime)

        return cv
    }

    private fun formatDate(ms: Long) : String {
        val df: DateFormat = DateFormat.getDateInstance()
        val resultDate = Date(ms)
        return df.format(resultDate)
    }

    fun updateData(newRecords: List<Record>) {
        records = newRecords
        notifyDataSetChanged()
    }
}