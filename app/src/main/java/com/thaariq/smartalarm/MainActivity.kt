package com.thaariq.smartalarm

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thaariq.smartalarm.adapter.AlarmAdapter
import com.thaariq.smartalarm.data.local.AlarmDB
import com.thaariq.smartalarm.data.local.AlarmDao
import com.thaariq.smartalarm.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding as ActivityMainBinding

    private var alarmDao: AlarmDao? = null
    private var alarmAdapter: AlarmAdapter? = null

//    override fun onResume() {
//        super.onResume()
//        CoroutineScope(Dispatchers.IO).launch {
//            val alarm = alarmDao?.getAlarm()
//
//            withContext(Dispatchers.Main) {
//                alarmDao?.let {
//                    alarm?.let { it1 -> alarmAdapter?.setData(it1) }
//                }
//                Log.i("GetAlarm", "getAlarm : alarm with $alarm")
//            }
//        }
//    }

    override fun onResume() {
        super.onResume()
        val alarm = alarmDao?.getAlarm()
        alarm?.observe(this){
            alarmAdapter?.setData(it)
            Log.i("GetAlarm", "getAlarm : alarm with $it")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AlarmDB.getDatabase(applicationContext)
        alarmDao = db.alarmDao()

        alarmAdapter = AlarmAdapter()

        initview()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvReminderAlarm.apply {

            layoutManager = LinearLayoutManager(context)
            adapter = alarmAdapter
            swipeToDelete(this)
        }
    }

    private fun initview() {
        binding.apply {
            cvSetOneTimeAlarm.setOnClickListener {
                startActivity(Intent(this@MainActivity, OneTimeAlarmActivity::class.java))
            }

            cvSetRepeatingAlarm.setOnClickListener {
                startActivity(Intent(this@MainActivity, RepeatingAlarmActivity::class.java))
            }

        }

    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedAlarm = alarmAdapter?.listAlarm?.get(viewHolder.adapterPosition)
                CoroutineScope(Dispatchers.IO).launch {
                    deletedAlarm?.let { alarmDao?.deleteAlarm(it) }
                    Log.i("DeleteAlarm","Alarm Deleted $deletedAlarm")
                } //TODO 3 = HAPUS notifyItemRemoved
            }

        }).attachToRecyclerView(recyclerView)



    }
}

