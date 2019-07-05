package com.example.nn4wchallenge

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.nn4wchallenge.database.external.onlineDatabase
import com.example.nn4wchallenge.database.external.searchItem
import com.example.nn4wchallenge.database.internal.AddClothingHandler
import com.example.nn4wchallenge.database.internal.SetupManager
import com.example.nn4wchallenge.database.internal.databaseChecker
import com.example.nn4wchallenge.database.matchClothingHandler
import com.example.nn4wchallenge.database.matchClothingInterface
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

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

    private lateinit var testTXT: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        testTXT = findViewById(R.id.testTXT)
        checkSetup()

        searchBTN = findViewById(R.id.quickSearchBTN)
        searchBTN.setOnClickListener {

            WorkManager.getInstance().getWorkInfoByIdLiveData(checkUserDatabaseWorker.id)
                .observe(this, Observer { workInfo ->
                    if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED)
                    {
                        var output : Boolean = workInfo.outputData.getBoolean("empty", true)

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
                        var output : Boolean = workInfo.outputData.getBoolean("empty", true)

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
                testTXT.setText("internet access granted")
            } else {
                testTXT.setText("internet access ")
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


    private fun testData()
    {
        var addClothing : AddClothingHandler = AddClothingHandler(applicationContext)
        addClothing.setClothingSeason("summer")
        addClothing.setClothingType("dress")
        addClothing.setClothingColour(0, 0, 0)
        addClothing.setPicture()

        try {
            addClothing.saveClothingItem()
        }
        catch(e : Exception)
        {
            testTXT.setText("error clothing data base ${e.toString()}")
        }


        var setupUser : SetupManager = SetupManager()

        setupUser.setAge(3)
        setupUser.setGender(1)
        setupUser.setChest(3)
        setupUser.setWaist(3)
        setupUser.setShoe(1)
        setupUser.saveUserData()


        testTXT.setText("data added")
    }


    private fun testSearch()
    {

        val testSearch : matchClothingInterface = matchClothingInterface()

        val thread = testSearch.search()

        WorkManager.getInstance().getWorkInfoByIdLiveData(thread)
            .observe(this, Observer { workInfo ->
                /*try{
                    val size = testSearch.handleOutput(workInfo).size

                    testTXT.setText("size $size")
                }
                catch(e: Exception)
                {
                    testTXT.setText("error ${e.cause.toString()}")
                }*/

                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    //var clothingImages : Array<String> = workInfo.outputData.getStringArray("images") as Array<String>
                    //var clothingDescriptions : Array<String> = workInfo.outputData.getStringArray("descriptions") as Array<String>
                    //var userClothingImage : Array<String> = workInfo.outputData.getStringArray("userClothing") as Array<String>
                    var error : String? = workInfo.outputData.getString("error")

                    testTXT.setText("error $error ")
                    
                }

                /*
                if (workInfo != null && workInfo.state == WorkInfo.State.FAILED)
                {
                    var error : String? = workInfo.outputData.getString("error")

                    testTXT.setText("error $error ")
                }*/

            })


/*
        val testDatabase = OneTimeWorkRequestBuilder<testWorker>()
            .build()

        WorkManager.getInstance().enqueue(testDatabase)

        WorkManager.getInstance().getWorkInfoByIdLiveData(testDatabase.id)
            .observe(this, Observer { workInfo ->

                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED)
                {
                    testTXT.setText("done")

                    var output : String? = workInfo.outputData.getString("result")

                    if (output == null)
                    {
                        testTXT.setText("no result found")
                    }
                    else
                    {
                        testTXT.setText("result $output")
                    }
                }

            })
            */
    }
}
