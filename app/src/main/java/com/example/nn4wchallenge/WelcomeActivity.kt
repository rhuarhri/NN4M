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
import com.example.nn4wchallenge.database.external.GetItemDescription
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
            //setupDescription("https://firebasestorage.googleapis.com/v0/b/nn4mfashion.appspot.com/o/black%20print%20playsuit.json?alt=media&token=e341d428-e109-471c-bc81-a8d28af1d335")
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


    private fun setupDescription(descriptionURL : String)
    {

        val inputData : Data = Data.Builder().putString("url", descriptionURL).build()

        val GetDescriptionWorker = OneTimeWorkRequestBuilder<GetItemDescription>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance().enqueue(GetDescriptionWorker)

        WorkManager.getInstance().getWorkInfoByIdLiveData(GetDescriptionWorker.id)
            .observe(this, Observer { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {

                    val name : String = workInfo.outputData.getString("name").toString()


                    val description : String = workInfo.outputData.getString("description").toString()

                    val price : Double = workInfo.outputData.getDouble("cost", 0.0)


                    val reduction : Int = workInfo.outputData.getInt("reduction", 0)
                    /*if (reduction > 0)
                    {
                        reducedPriceTXT.setText("$reduction off")
                    }*/

                    val imageURLs : Array<String> = workInfo.outputData.getStringArray("images") as Array<String>

                    testTXT.setText("name: $name description: $description price: $price reduction: $reduction images amount: ${imageURLs.size}")

                }
                if (workInfo != null && workInfo.state == WorkInfo.State.FAILED)
                {
                    val error : String = workInfo.outputData.getString("error").toString()
                    testTXT.setText("thread error is $error")
                }
            })
    }

}
