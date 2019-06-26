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
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.nn4wchallenge.database.external.onlineDatabase
import com.example.nn4wchallenge.database.external.searchItem
import com.example.nn4wchallenge.database.matchClothingHandler
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

    private var MY_PERMISSIONS_REQUEST_INTERNET : Int = 1

    private lateinit var searchBTN : Button
    private lateinit var shoppingBTN : Button

    private lateinit var testTXT : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        searchBTN = findViewById(R.id.quickSearchBTN)
        searchBTN.setOnClickListener {

            testPermissions()
        }
        shoppingBTN = findViewById(R.id.shopBTN)
        shoppingBTN.setOnClickListener {

            goToShoppingScreen()
        }

        testTXT = findViewById(R.id.testTXT)
    }

    private fun goToShoppingScreen()
    {
        var goTo = Intent(applicationContext, MainActivity::class.java)
        startActivity(goTo)
    }

    private fun testPermissions()
    {
        //var MY_PERMISSIONS_REQUEST_INTERNET : Int = 1

        //var testPermission : permissionsHandler = permissionsHandler()

        //testPermission.internetPremission(this, applicationContext)

        var accessInternet : permissionsHandler = permissionsHandler(this, applicationContext)

        accessInternet.internetPermission()

        if (accessInternet.checkInternetPermission())
        {
            testTXT.setText("permission granted")
            getJson()
        }
        else{
            testTXT.setText("permission not granted")
        }

/*
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted


        } else {

        }*/
    }

    private fun getJson()
    {
        /*
        var jsonOnline : onlineDatabase = onlineDatabase()

        try{
            var result : ArrayList<searchItem> = jsonOnline.getAvailableClothes()

            testTXT.setText("result size is ${result.size}")
        }
        catch(e : Exception)
        {
            testTXT.setText("json $e")
        }*/

        val testWorkRequest = OneTimeWorkRequestBuilder<matchClothingHandler>()
            .build()

        WorkManager.getInstance().enqueue(testWorkRequest)

        WorkManager.getInstance().getWorkInfoByIdLiveData(testWorkRequest.id)
            .observe(this, Observer { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {

                    var output : String? = workInfo.outputData.getString("message")

                    if (output == null)
                    {
                        testTXT.setText("no return value")
                    }
                    else
                    {
                        testTXT.setText(output)
                    }
                }
            })

    }

    private fun testing()
    {
        try {
            //var testOnline = onlineDatabase(testTXT, applicationContext)

            //var inStream : InputStream = applicationContext.getResources().openRawResource(R.raw.register)
            
            //var fileIn : File = File("src/main/res/raw/")

            //var inStream : InputStream = FileInputStream(fileIn)

            //var result = "" + inStream.available()

            /*
            if (inStream == 0)
            {
                result = "empty"
            }
            else{
                result = "something"
            }*/



            //var testJsonParse : jsonParser = jsonParser()

            //result = testJsonParse.convertStreamToString(inStream)

            //var testResult: ArrayList<searchItem> =
                //testOnline.getAvailableClothes(inStream)

            //var result = testResult[0].season

            //testTXT.text = result
        }
        catch(e : Exception)
        {
            testTXT.setText("Json " + e.printStackTrace().toString())
        }


    }

    public fun internetPremission() {
// Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.INTERNET
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.INTERNET
                )
            ) {
                AlertDialog.Builder(applicationContext)
                    .setTitle("Internet permission")
                    .setMessage("must have")
                    .setPositiveButton("ok", DialogInterface.OnClickListener { dialogInterface, i ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.INTERNET),
                            MY_PERMISSIONS_REQUEST_INTERNET
                        )
                    })
                    .setNegativeButton("cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                    }).create().show()
                //add alert box
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.INTERNET),
                    MY_PERMISSIONS_REQUEST_INTERNET
                )

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }



    }
}
