package com.thaariq.smartalarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.thaariq.smartalarm.data.Alarm
import com.thaariq.smartalarm.data.local.AlarmDB
import com.thaariq.smartalarm.data.local.AlarmDao
import com.thaariq.smartalarm.databinding.ActivityRepeatingAlarmBinding
import com.thaariq.smartalarm.helper.timeFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RepeatingAlarmActivity : AppCompatActivity(), TimePickerFragment.TimeDialogListener {

    private var _binding: ActivityRepeatingAlarmBinding? = null
    private val binding get() = _binding as ActivityRepeatingAlarmBinding

    private var alarmDao: AlarmDao? = null

    private var _alarmService: AlarmService? = null
    private val alarmService get() = _alarmService as AlarmService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRepeatingAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AlarmDB.getDatabase(applicationContext)
        alarmDao = db.alarmDao()

        _alarmService = AlarmService()

        initView()
    }

    private fun initView() {
        binding.apply {
            btnSetTimeOneTime.setOnClickListener {
                val timePickerFragment = TimePickerFragment()
                timePickerFragment.show(supportFragmentManager, "TimePickerDialog")
            }

            btnAddSetOneTimeAlarm.setOnClickListener {
                val time = tvOnceTime.text.toString()
                val message = editNoteOneTime.text.toString()

                if (time != "Time") {
                    CoroutineScope(Dispatchers.IO).launch {
                        alarmDao?.addAlarm(
                            Alarm(
                                1,
                                "Repeating Alarm",
                                time,
                                message
                            )
                        )
                        finish()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Please Set your Date & Time",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

        }
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        binding.tvOnceTime.text = timeFormatter(hourOfDay, minute)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}