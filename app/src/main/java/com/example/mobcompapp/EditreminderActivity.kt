package com.example.mobcompapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.DEBUG
import android.widget.Button
import android.widget.TextView
import androidx.room.Room
import com.example.mobcompapp.database.AppDatabase
import com.example.mobcompapp.database.ReminderInfo

class EditreminderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editreminder)

        val reminderUid = intent.getStringExtra("EXTRA_UID")
        val reminderTitle = intent.getStringExtra("EXTRA_TITLE")
        val reminderDate = intent.getStringExtra("EXTRA_DATE")
        val reminderHours = intent.getStringExtra("EXTRA_HOURS")
        val reminderMinutes = intent.getStringExtra("EXTRA_MINUTES")

        Log.d("compapp", "Editing UID: $reminderUid, title: $reminderTitle")

        val reminderTextTitle = findViewById<TextView>(R.id.textView19)
        val reminderTextDate = findViewById<TextView>(R.id.textView21)
        val reminderTextTime = findViewById<TextView>(R.id.textView20)

        reminderTextTitle.text = reminderTitle
        reminderTextDate.text = reminderDate
        reminderTextTime.text = "$reminderHours:$reminderMinutes"

        val nameTextView = findViewById<TextView>(R.id.newReminderTitle)
        val dateTextView = findViewById<TextView>(R.id.editTextTextPersonName2)
        val hourTextView = findViewById<TextView>(R.id.editTextTextPersonName4)
        val minuteTextView = findViewById<TextView>(R.id.editTextTextPersonName5)

        findViewById<Button>(R.id.button4).setOnClickListener {
            startActivity(Intent(applicationContext, MainmenuActivity::class.java))
        }

        findViewById<Button>(R.id.button3).setOnClickListener {
            val nameString = nameTextView.text.toString()
            val dateString = dateTextView.text.toString()
            val hourString = hourTextView.text.toString()
            val minuteString = minuteTextView.text.toString()

            val reminderInfo = ReminderInfo(
                uid = reminderUid.toInt(),
                title = nameString,
                date = dateString,
                hours = hourString,
                minutes = minuteString
            )
            AsyncTask.execute {
                //delete previous reminder from room database
                val db = Room
                    .databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java,
                        "com.example.mobcompapp"
                    )
                    .build()
                db.ReminderDao().delete(reminderInfo.uid!!)
                //insert new edited reminder to the place where previous was
                db.ReminderDao().insert(reminderInfo)
                db.close()
            }
            finish()
            startActivity(Intent(applicationContext, MainmenuActivity::class.java))
        }

        findViewById<Button>(R.id.deleteButton).setOnClickListener {
            val nameString = nameTextView.text.toString()
            val dateString = dateTextView.text.toString()
            val hourString = hourTextView.text.toString()
            val minuteString = minuteTextView.text.toString()

            val reminderInfo = ReminderInfo(
                uid = reminderUid.toInt(),
                title = nameString,
                date = dateString,
                hours = hourString,
                minutes = minuteString
            )
            //Delete the current reminder from the database
            println("Pressed delete, deletableUID: ${reminderInfo.uid}")
            AsyncTask.execute {
                val db = Room
                    .databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java,
                        "com.example.mobcompapp"
                    )
                    .build()
                println("Deleting UID ${reminderInfo.uid!!} now")
                db.ReminderDao().delete(reminderInfo.uid!!)
                db.close()
                startActivity(Intent(applicationContext, MainmenuActivity::class.java))
            }
        }
    }

}