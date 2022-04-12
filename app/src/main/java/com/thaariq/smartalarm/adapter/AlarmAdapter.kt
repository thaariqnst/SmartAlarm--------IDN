package com.thaariq.smartalarm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thaariq.smartalarm.data.Alarm
import com.thaariq.smartalarm.databinding.RowItemAlarmBinding

class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.MyViewHolder>() {
    val listAlarm : ArrayList<Alarm> = arrayListOf()


    // Inner agar cuma bisa diakses oleh class parent
    inner class MyViewHolder(val binding: RowItemAlarmBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        RowItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent,false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val alarm = listAlarm[position]
        holder.binding.apply {
            itemDateAlarm.text = listAlarm[position].date //tanpa menggunakan val tambahan seperti diatas
            itemTimeAlarm.text = alarm.time
            itemNoteAlarm.text = alarm.message
        }
    }

    override fun getItemCount()= listAlarm.size

    //TODO 2 = PERBARUI KODE DARI GITHUB
    fun setData(list: List<Alarm>) {
        val alarmDiffUtil = AlarmDiffUtil(listAlarm, list)
        val alarmDiffUtilResult = DiffUtil.calculateDiff(alarmDiffUtil)
        listAlarm.clear()
        listAlarm.addAll(list)
        alarmDiffUtilResult.dispatchUpdatesTo(this)
    }

}