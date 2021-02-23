package com.example.mobcompapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private val myPrefs = "MyPREFERENCES"
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPrefs : SharedPreferences = getSharedPreferences(myPrefs, 0)

        findViewById<Button>(R.id.button).setOnClickListener {

            val enteredUsername = findViewById<EditText>(R.id.editTextTextPersonName)
            val enteredPassword = findViewById<EditText>(R.id.editTextTextPassword)

            val corrUser : String = sharedPrefs.getString("Name", "asd") ?: "asd"
            val corrPass : String = sharedPrefs.getString("Password", "1234") ?: "1234"

            if(enteredUsername.getText().toString() == corrUser && enteredPassword.getText().toString() == corrPass){
                startActivity(Intent(applicationContext, MainmenuActivity::class.java))
            }
            else{
                val text = "Incorrect login credentials."
                val toast = Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT)
                toast.show()
            }
            //applicationContext.getSharedPreferences(getString(R.string.sharedPreference),Context.MODE_PRIVATE).edit().putInt("logInStatus",1).apply()
        }
    }
}
