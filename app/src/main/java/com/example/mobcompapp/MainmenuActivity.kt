package com.example.mobcompapp

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.room.Room
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mobcompapp.database.AppDatabase
import com.example.mobcompapp.database.ReminderInfo
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random


class MainmenuActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    var allRemindersToggler = false
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainmenu)

        listView = findViewById<ListView>(R.id.listView)
        refreshListView()

        findViewById<Button>(R.id.newReminderBtn).setOnClickListener {
            startActivity(Intent(applicationContext, NewreminderActivity::class.java))
            finish()
        }

        findViewById<Button>(R.id.logOutButton).setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }

        findViewById<Button>(R.id.showHideFutureBtn).setOnClickListener{
            if(allRemindersToggler == false){
                Log.d("toggler", "toggled allreminders to true")
                allRemindersToggler = true
                refreshListView()
            }
            else if(allRemindersToggler == true){
                Log.d("toggler", "toggled allreminders to false")
                allRemindersToggler = false
                refreshListView()
            }
        }

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, id ->

            val selectedReminder = listView.adapter.getItem(position) as ReminderInfo
            val reminderUid = selectedReminder.uid.toString()
            val reminderTitle = selectedReminder.title
            val reminderDate = selectedReminder.date
            val reminderTime = selectedReminder.time

            val editReminderIntent = Intent(this, EditreminderActivity::class.java).apply{
                putExtra("EXTRA_UID", reminderUid)
                putExtra("EXTRA_TITLE", reminderTitle)
                putExtra("EXTRA_DATE", reminderDate)
                putExtra("EXTRA_TIME", reminderTime)
            }
            startActivity(editReminderIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshListView()
    }
    private fun refreshListView() {
        if(allRemindersToggler){
            var refreshTask = LoadReminderInfoEntries()
            refreshTask.execute()
        }
        if(!allRemindersToggler){
            var refreshTask = LoadPastReminderInfoEntries()
            refreshTask.execute()
        }

    }

    inner class LoadReminderInfoEntries : AsyncTask<String?, String?, List<ReminderInfo>>() {
        override fun doInBackground(vararg params: String?): List<ReminderInfo> {
            val db = Room
                    .databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java,
                        "com.example.mobcompapp"
                    )
                    .build()
            val reminderInfos = db.ReminderDao().getPaymentInfos()
            db.close()
            Log.d("testing", "reminderInfos: ${reminderInfos}")
            return reminderInfos
        }

        override fun onPostExecute(reminderInfos: List<ReminderInfo>?) {
            super.onPostExecute(reminderInfos)
            if (reminderInfos != null) {
                if (reminderInfos.isNotEmpty()) {
                    val adaptor = ReminderHistoryAdaptor(applicationContext, reminderInfos)
                    listView.adapter = adaptor
                } else {
                    listView.adapter = null
                    Toast.makeText(applicationContext, "There are no reminders", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    inner class LoadPastReminderInfoEntries : AsyncTask<String?, String?, List<ReminderInfo>>() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun doInBackground(vararg params: String?): List<ReminderInfo> {
            val db = Room
                .databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    "com.example.mobcompapp"
                )
                .build()

            val dateFormat = "yyyy-MM-dd"
            val timeFormat= "HH:mm"

            val dateformatter = SimpleDateFormat(dateFormat)
            val currentParsedDate = dateformatter.parse(LocalDate.now().toString())
            val currentDate = SimpleDateFormat("yyyy-MM-dd").format(currentParsedDate).toString()

            val currentTime = (LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))).toString()

            val reminderInfos = db.ReminderDao().getPastReminderInfos(currentDate, currentTime)
            db.close()
            Log.d("testing", "reminderInfos: ${reminderInfos}")
            return reminderInfos
        }

        override fun onPostExecute(reminderInfos: List<ReminderInfo>?) {
            super.onPostExecute(reminderInfos)
            if (reminderInfos != null) {
                if (reminderInfos.isNotEmpty()) {
                    val adaptor = ReminderHistoryAdaptor(applicationContext, reminderInfos)
                    listView.adapter = adaptor
                } else {
                    listView.adapter = null
                    Toast.makeText(applicationContext, "There are no reminders", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        //val paymenthistoryList = mutableListOf<PaymentInfo>()

        fun showNotification(context: Context, message: String) {

            val CHANNEL_ID = "REMINDER_APP_NOTIFICATION_CHANNEL"
            var notificationId = Random.nextInt(10, 1000) + 5
            // notificationId += Random(notificationId).nextInt(1, 500)

            //Defining a notification sound
            val soundUri = Uri.parse("android.resource://com.example.mobcompapp/res" + R.raw.mobcompalert)

            var notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_alarm_24)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroup(CHANNEL_ID)
                .setSound(soundUri)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Notification channel needed since Android 8
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    context.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = context.getString(R.string.app_name)
                }
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(notificationId, notificationBuilder.build())
        }

        fun setReminderWithWorkManager(
            context: Context,
            uid: Int,
            timeInMillis: Long,
            message: String
        ) {

            val reminderParameters = Data.Builder()
                .putString("message", message)
                .putInt("uid", uid)
                .build()

            // get minutes from now until reminder
            var minutesFromNow = 0L
            if (timeInMillis > System.currentTimeMillis())
                minutesFromNow = timeInMillis - System.currentTimeMillis()
            //Log.d("mobcompapp", "minutes or millis ?? from now: ${minutesFromNow}")

            val reminderRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                .setInputData(reminderParameters)
                .setInitialDelay(minutesFromNow, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(context).enqueue(reminderRequest)
        }

        @RequiresApi(Build.VERSION_CODES.KITKAT)
        fun setReminder(context: Context, uid: Int, timeInMillis: Long, message: String) {
            val intent = Intent(context, ReminderReceiver::class.java)
            intent.putExtra("uid", uid)
            intent.putExtra("message", message)

            // create a pending intent to a  future action with a unique request code i.e uid
            val pendingIntent =
                PendingIntent.getBroadcast(context, uid, intent, PendingIntent.FLAG_ONE_SHOT)

            //create a service to monitor and execute the action.
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(AlarmManager.RTC, timeInMillis, pendingIntent)
        }

        fun cancelReminder(context: Context, pendingIntentId: Int) {

            val intent = Intent(context, ReminderReceiver::class.java)
            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    pendingIntentId,
                    intent,
                    PendingIntent.FLAG_ONE_SHOT
                )
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }
    }
}