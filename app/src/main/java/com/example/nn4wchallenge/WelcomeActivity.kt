package com.example.nn4wchallenge


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.nn4wchallenge.database.internal.databaseChecker

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

    private lateinit var checkUserDatabaseWorker: OneTimeWorkRequest

    private lateinit var searchBTN: Button
    private lateinit var shoppingBTN: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        checkSetup()

        searchBTN = findViewById(R.id.quickSearchBTN)
        searchBTN.setOnClickListener {

            WorkManager.getInstance().getWorkInfoByIdLiveData(checkUserDatabaseWorker.id)
                .observe(this, Observer { workInfo ->
                    if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED)
                    {
                        val output : Boolean = workInfo.outputData.getBoolean("empty", true)

                        if (output)
                        {
                            //setup required
                            goToSetupScreen()
                        }
                        else
                        {
                            goToQuickSearch()
                        }
                    }
                    //thread that check if data base is empty currently running or waiting to run
                    else if (workInfo != null && (workInfo.state == WorkInfo.State.RUNNING || workInfo.state == WorkInfo.State.ENQUEUED))
                    {
                        //do nothing this prevents the user doing anything without information from the thread
                    }
                })

        }
        shoppingBTN = findViewById(R.id.shopBTN)
        shoppingBTN.setOnClickListener {

            WorkManager.getInstance().getWorkInfoByIdLiveData(checkUserDatabaseWorker.id)
                .observe(this, Observer { workInfo ->
                    if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED)
                    {
                        val output : Boolean = workInfo.outputData.getBoolean("empty", true)

                        if (output)
                        {
                            //setup required
                            goToSetupScreen()
                        }
                        else
                        {
                            goToShoppingScreen()
                        }
                    }
                    //thread that check if data base is empty currently running or waiting to run
                    else if (workInfo != null && (workInfo.state == WorkInfo.State.RUNNING || workInfo.state == WorkInfo.State.ENQUEUED))
                    {
                        //do nothing this prevents the user doing anything without information from the thread
                    }
                })
            }

    }

    private fun goToShoppingScreen() {
        val goTo = Intent(applicationContext, MainActivity::class.java)
        startActivity(goTo)
    }

    private fun goToSetupScreen() {
        val goTo = Intent(applicationContext, SetupActivity::class.java)
        startActivity(goTo)
    }

    private fun goToQuickSearch() {
        //might be implemented in the future
        Toast.makeText(applicationContext, "Functionality not available", Toast.LENGTH_SHORT).show()
    }

    private fun checkSetup() {

        val accessPermissions: permissionsHandler = permissionsHandler(this, applicationContext)

            accessPermissions.internetPermission()

            if (accessPermissions.checkInternetPermission()) {

            } else {
                Toast.makeText(applicationContext, "internet permission not granted", Toast.LENGTH_LONG).show()
            }

        accessPermissions.internalStoragePermission()

        if (accessPermissions.checkInternetPermission()) {

            val input: Data = Data.Builder().putString("database", "user").build()

            checkUserDatabaseWorker = OneTimeWorkRequestBuilder<databaseChecker>()
                .setInputData(input)
                .build()

            WorkManager.getInstance().enqueue(checkUserDatabaseWorker)

        }

    }

}
