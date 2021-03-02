package com.example.mobcompapp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Log.DEBUG
import android.widget.*
import androidx.room.Room
import com.example.mobcompapp.database.AppDatabase
import com.example.mobcompapp.database.ReminderInfo
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class EditreminderActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private lateinit var reminderCalendar: Calendar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editreminder)

        val reminderUid = intent.getStringExtra("EXTRA_UID")
        val reminderTitle = intent.getStringExtra("EXTRA_TITLE")
        val reminderDate = intent.getStringExtra("EXTRA_DATE")
        val reminderTime= intent.getStringExtra("EXTRA_TIME")

        Log.d("compapp", "Editing UID: $reminderUid, title: $reminderTitle")

        val nameTextView = findViewById<TextView>(R.id.newReminderTitle)
        val dateTextView = findViewById<TextView>(R.id.editTextTextPersonName2)
        val timeTextView = findViewById<TextView>(R.id.editTextTextPersonName4)

        nameTextView.text = reminderTitle
        dateTextView.text = reminderDate
        timeTextView.text = reminderTime

        //Prevent user from typing manually into date and text fields
        dateTextView.inputType = InputType.TYPE_NULL
        dateTextView.isClickable = true
        timeTextView.inputType = InputType.TYPE_NULL
        timeTextView.isClickable = true

        dateTextView.setOnClickListener {
            reminderCalendar = GregorianCalendar.getInstance()
            DatePickerDialog(
                    this,
                    this,
                    reminderCalendar.get(Calendar.YEAR),
                    reminderCalendar.get(Calendar.MONTH),
                    reminderCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        timeTextView.setOnClickListener {
            reminderCalendar = GregorianCalendar.getInstance()
            TimePickerDialog(
                    this,
                    this,
                    reminderCalendar.get(Calendar.HOUR),
                    reminderCalendar.get(Calendar.MINUTE),
                    true).show()
        }

        findViewById<Button>(R.id.button4).setOnClickListener {
            //cancel button returns back to main menu
            startActivity(Intent(applicationContext, MainmenuActivity::class.java))
        }

        findViewById<Button>(R.id.button3).setOnClickListener {
            val nameString = nameTextView.text.toString()
            val dateString = dateTextView.text.toString()
            val timeString = timeTextView.text.toString()

            val reminderInfo = ReminderInfo(
                uid = reminderUid.toInt(),
                title = nameString,
                date = dateString,
                time = timeString
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
            if (dateTextView.text.isEmpty() || timeTextView.text.isEmpty()) {
                Toast.makeText(
                        applicationContext,
                        "Date or time is not set",
                        Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nameString = nameTextView.text.toString()
            val dateString = dateTextView.text.toString()
            val timeString = timeTextView.text.toString()

            val reminderInfo = ReminderInfo(
                uid = reminderUid.toInt(),
                title = nameString,
                date = dateString,
                time = timeString
            )

            val reminderCalendar = GregorianCalendar.getInstance()
            val dateFormat = "yyyy-MM-dd"
            val timeFormat= "HH:mm"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val dateformatter = DateTimeFormatter.ofPattern(dateFormat)
                val date = LocalDate.parse(reminderInfo.date, dateformatter)

                val timeformatter = DateTimeFormatter.ofPattern(timeFormat)
                val time = LocalTime.parse(reminderInfo.time, timeformatter)

                reminderCalendar.set(Calendar.YEAR,date.year)
                reminderCalendar.set(Calendar.MONTH,date.monthValue-1)
                reminderCalendar.set(Calendar.DAY_OF_MONTH,date.dayOfMonth)
                reminderCalendar.set(Calendar.HOUR_OF_DAY,time.hour)
                reminderCalendar.set(Calendar.MINUTE, time.minute)
            }

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

    override fun onDateSet(view: DatePicker?, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int) {
        reminderCalendar.set(Calendar.YEAR, selectedYear)
        reminderCalendar.set(Calendar.MONTH, selectedMonth)
        reminderCalendar.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth)

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        findViewById<TextView>(R.id.editTextTextPersonName2).setText(simpleDateFormat.format(reminderCalendar.time))
    }

    override fun onTimeSet(view: TimePicker?, selectedhourOfDay: Int, selectedMinute: Int) {
        reminderCalendar.set(Calendar.HOUR_OF_DAY, selectedhourOfDay)
        reminderCalendar.set(Calendar.MINUTE, selectedMinute)
        val simpleDateFormat = SimpleDateFormat("HH:mm")
        findViewById<TextView>(R.id.editTextTextPersonName4).setText(simpleDateFormat.format(reminderCalendar.time))
    }

}