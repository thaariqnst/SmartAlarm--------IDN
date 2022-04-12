package com.thaariq.smartalarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import java.util.*

class AlarmService : BroadcastReceiver() {

    fun setRepeatingAlarm(context: Context, type: Int, time: String, message: String){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context,AlarmService::class.java)
        intent.putExtra("message",message)
        intent.putExtra("type", type)

        val timeArray = time.split(":").toTypedArray()

        val calendar = Calendar.getInstance()

        //Time
        calendar.set(Calendar.HOUR, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context,101,intent,0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(context,"Success set repeating alarm",Toast.LENGTH_SHORT).show()
        Log.i("AlarmSetRepeat", "setRepeatingAlarm: Alarm Set on : ${calendar.time}")
    }

    fun setOneTimeAlarm(context: Context, type : Int, date : String, time : String, message : String){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context,AlarmService::class.java)
        intent.putExtra("message",message)
        intent.putExtra("type", type)

        // data dari date -> 2-2-2022
        // setelah di split -> 2  2  2022
        // convert jadi array -> [2, 2, 2022]
        val dateArray = date.split("-").toTypedArray()
        val timeArray = time.split(":").toTypedArray()

        val calendar = Calendar.getInstance()
        //Date
        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[0]))
        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[0])-1)
        calendar.set(Calendar.DATE, Integer.parseInt(dateArray[0]))
        //Time
        calendar.set(Calendar.HOUR, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context,101,intent,0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(context,"Success set one time alarm",Toast.LENGTH_SHORT).show()
        Log.i("AlarmSetOneTime", "setOneTimeAlarm: Alarm Set on : ${calendar.time}")
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra(EXTRA_MESSAGE)
        intent?.getStringExtra(EXTRA_MESSAGE)
        intent?.getIntExtra(EXTRA_TYPE, 0)


        if (context != null) {
            if (message != null) {
                showNotificationAlarm(context,"Alarm oi", message,101)
            }
        }
    }

    private fun showNotificationAlarm(

        context: Context,
        title : String,
        message : String,
        notificationid : Int,
        channelName: String = "Smart Alarm"
    ) {
        val channelId = "smart_alarm"

        val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context,channelId)
            .setSmallIcon(R.drawable.ic_one_time)
            .setContentTitle(title)
            .setContentText(message)
            .setSound(ringtone)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000,1000,1000,1000)
            builder.setChannelId(channelId)
            notificationManager.createNotificationChannel(channel)
        }
        val notif = builder.build()
        notificationManager.notify(notificationid,notif )
    }

    companion object{
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"

        const val NOTIF_ID_ONE_TIME = 101
        const val NOTID_ID_REPEATING = 102
    }
}