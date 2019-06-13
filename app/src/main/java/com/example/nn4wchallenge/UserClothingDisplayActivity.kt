package com.example.nn4wchallenge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.*

class UserClothingDisplayActivity : AppCompatActivity() {

    private lateinit var settingsBTN : Button
    private lateinit var accountBTN : Button
    private lateinit var addBTN : Button

    private lateinit var clothingRV : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_clothing_display)

        settingsBTN = findViewById(R.id.settingsBTN)
        settingsBTN.setOnClickListener {

            Toast.makeText(applicationContext, "Settings not available", Toast.LENGTH_SHORT).show()
        }

        accountBTN = findViewById(R.id.accountBTN)
        accountBTN.setOnClickListener {

            val goToAccountScreen : Intent = Intent(applicationContext, SetupActivity::class.java)
            startActivity(goToAccountScreen)

        }

        addBTN = findViewById(R.id.addBTN)
        addBTN.setOnClickListener {

            val goToAddScreen : Intent = Intent(applicationContext, AddActivity::class.java)
            startActivity(goToAddScreen)
        }
    }
}
