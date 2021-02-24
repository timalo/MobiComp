package com.example.mobcompapp

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import com.example.mobcompapp.database.AppDatabase
import com.example.mobcompapp.database.ReminderInfo

class MainmenuActivity : AppCompatActivity() {
    private lateinit var listView: ListView
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

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, id ->

            val selectedReminder = listView.adapter.getItem(position) as ReminderInfo
            val reminderUid = selectedReminder.uid.toString()
            val reminderTitle = selectedReminder.title
            val reminderDate = selectedReminder.date
            val reminderHours = selectedReminder.hours
            val reminderMinutes = selectedReminder.minutes

            val editReminderIntent = Intent(this, EditreminderActivity::class.java).apply{
                putExtra("EXTRA_UID", reminderUid)
                putExtra("EXTRA_TITLE", reminderTitle)
                putExtra("EXTRA_DATE", reminderDate)
                putExtra("EXTRA_HOURS", reminderHours)
                putExtra("EXTRA_MINUTES", reminderMinutes)
            }
            startActivity(editReminderIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshListView()
    }
    private fun refreshListView() {
        var refreshTask = LoadReminderInfoEntries()
        refreshTask.execute()
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
                    Toast.makeText(applicationContext, "No items now", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}