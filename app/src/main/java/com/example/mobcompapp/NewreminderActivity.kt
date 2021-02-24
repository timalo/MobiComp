package com.example.mobcompapp

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.room.Room
import com.example.mobcompapp.database.AppDatabase
import com.example.mobcompapp.database.ReminderInfo
import java.util.*

class NewreminderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newreminder)

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

            val reminderInfo = ReminderInfo(null,
                    title = nameString,
                    date = dateString,
                    hours = hourString,
                    minutes = minuteString
            )

            AsyncTask.execute {
                //save payment to room database
                val db = Room.databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java,
                        "com.example.mobcompapp"
                ).build()
                val uuid = db.ReminderDao().insert(reminderInfo).toInt()
                db.close()
            }
            finish()
            startActivity(Intent(applicationContext, MainmenuActivity::class.java))
        }
    }
}
