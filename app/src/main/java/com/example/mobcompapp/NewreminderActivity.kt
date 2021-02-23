package com.example.mobcompapp

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class NewreminderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newreminder)

        findViewById<Button>(R.id.button4).setOnClickListener {
            startActivity(Intent(applicationContext, MainmenuActivity::class.java))
        }

        findViewById<Button>(R.id.button3).setOnClickListener {
            println("Accept button clicked")
        }
    }
}
