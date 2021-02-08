package com.example.mobcompapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener {

            val username = findViewById<EditText>(R.id.editTextTextPersonName)
            val password = findViewById<EditText>(R.id.editTextTextPassword)

            println(username.getText().toString())
            println(password.getText().toString())

            val examplePass = getString(R.string.ExamplePassword)
            val exampleUser = getString(R.string.ExampleUsername)

            println(examplePass)
            println(exampleUser)

            if(username.getText().toString() == exampleUser && password.getText().toString() == examplePass){
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

 /*   override fun onResume() {
        super.onResume()
        checkLogInStatus()
    }

    private fun checkLogInStatus() {
        val logInStatus = applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).getInt("logInStatus", 0)
        if(logInStatus==1){
            startActivity(Intent(applicationContext, MainmenuActivity::class.java))
        }
    }*/
}