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

        var listView : ListView = findViewById<ListView>(R.id.listView)



/*
        listView.adapter = arrayAdapter

        findViewById<Button>(R.id.logOutButton).setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }

        findViewById<Button>(R.id.newReminderBtn).setOnClickListener {
            startActivity(Intent(applicationContext, Newreminder::class.java))
        }*/
    }
}