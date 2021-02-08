package com.example.mobcompapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class MainmenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainmenu)

        val listView = findViewById<ListView>(R.id.listView)
        val reminderLista = listOf("Reminder1", "Reminder2", "Reminder3", "Reminder4")

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, reminderLista
        )

        listView.adapter = arrayAdapter

        findViewById<Button>(R.id.logOutButton).setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
    }




}