package com.example.nn4wchallenge

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class permissionsHandler(var currentActivity : Activity, var appContext : Context) {

    private var MY_PERMISSIONS_REQUEST_INTERNET: Int = 1


    public fun internetPermission() {
        var accessInternet = Manifest.permission.INTERNET

        if (isPermissionGranted(accessInternet))
        {
            //permission grant no action
        }
        else{
            var Title : String = "internet"
            var message : String = "You need an internet connect"
            askForPermission(accessInternet, Title, message)
        }
    }

    public fun checkInternetPermission() : Boolean{
        var checkInternetPermission = Manifest.permission.INTERNET

        return isPermissionGranted(checkInternetPermission)
    }

    public fun cameraPermission() {
        var accessInternet = Manifest.permission.CAMERA

        if (isPermissionGranted(accessInternet))
        {
            //permission grant no action
        }
        else{
            var Title : String = "Camera"
            var message : String = "Used to save capture images of your clothes"
            askForPermission(accessInternet, Title, message)
        }
    }

    public fun checkCameraPermission() : Boolean{
        var checkInternetPermission = Manifest.permission.CAMERA

        return isPermissionGranted(checkInternetPermission)
    }

    public fun internalStoragePermission() {
        var accessInternet = Manifest.permission.WRITE_EXTERNAL_STORAGE

        if (isPermissionGranted(accessInternet))
        {
            //permission grant no action
        }
        else{
            var Title : String = "Internal storage"
            var message : String = "This is necessary for saving your data"
            askForPermission(accessInternet, Title, message)
        }
    }

    public fun checkInternalStoragePermission() : Boolean{
        var checkInternetPermission = Manifest.permission.INTERNET

        return isPermissionGranted(checkInternetPermission)
    }

    private fun askForPermission(permission: String, Title: String, message: String) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(currentActivity, permission)) {
            showAlertMessage(Title, message, permission)
        } else {
            grantPermission(permission)
        }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(appContext, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun grantPermission(permission: String) {
        ActivityCompat.requestPermissions(
            currentActivity,
            arrayOf(permission),
            MY_PERMISSIONS_REQUEST_INTERNET
        )
    }

    private fun showAlertMessage(Title: String, message: String, permission: String) {
        AlertDialog.Builder(appContext)
            .setTitle(Title)
            .setMessage(message)
            .setPositiveButton("ok", DialogInterface.OnClickListener { dialogInterface, i ->
                grantPermission(permission)
            })
            .setNegativeButton("cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
            }).create().show()
    }
}
    /*
    public fun internetPremission() {
// Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(
                appContext,
                Manifest.permission.INTERNET
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    currentActivity,
                    Manifest.permission.INTERNET
                )
            ) {
                AlertDialog.Builder(appContext)
                    .setTitle("Internet permission")
                    .setMessage("must have")
                    .setPositiveButton("ok", DialogInterface.OnClickListener { dialogInterface, i ->
                        ActivityCompat.requestPermissions(
                            currentActivity,
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
                    currentActivity,
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
}*/