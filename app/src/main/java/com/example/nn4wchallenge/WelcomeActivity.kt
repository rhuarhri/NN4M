package com.example.nn4wchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class WelcomeActivity : AppCompatActivity() {

    /*
    Why it exists
    This exists to welcome the user to the app so they know what
    app they have clicked on and so they just dropped into a task
    as soon as the app starts.
     */

    /*
    idea
    this section could have a link to a function that compares your
    cloths to something new so when you got shopping and see something you like
    the app will tell you if you should by it
     */

    /*
    idea
    this section could have a log in section to make buying cloths quicker
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
    }
}
