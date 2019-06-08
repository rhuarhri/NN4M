package com.example.nn4wchallenge

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class WelcomeActivity : AppCompatActivity() {

    /*
    Why it exists
    This exists to welcome the user to the app so they know what
    app they have clicked on and so they just dropped into a task
    as soon as the app starts.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
    }
}
