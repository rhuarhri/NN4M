package com.example.nn4wchallenge

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionsHandler(private var currentActivity : Activity, private var appContext : Context) {

    private var MY_PERMISSIONS_REQUEST_INTERNET: Int = 1


    fun internetPermission() {
        val accessInternet = Manifest.permission.INTERNET

        if (isPermissionGranted(accessInternet))
        {
            //permission grant no action
        }
        else{
            val title = "internet"
            val message = "You need an internet connect"
            askForPermission(accessInternet, title, message)
        }
    }

    fun checkInternetPermission() : Boolean{
        val checkInternetPermission = Manifest.permission.INTERNET

        return isPermissionGranted(checkInternetPermission)
    }

    fun cameraPermission() {
        val accessCamera = Manifest.permission.CAMERA

        if (isPermissionGranted(accessCamera))
        {
            //permission grant no action
        }
        else{
            val title = "Camera"
            val message = "Used to save capture images of your clothes"
            askForPermission(accessCamera, title, message)
        }
    }

    fun checkCameraPermission() : Boolean{
        val checkCameraPermission = Manifest.permission.CAMERA

        return isPermissionGranted(checkCameraPermission)
    }

    fun internalStoragePermission() {
        val accessWrite = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val accessRead = Manifest.permission.READ_EXTERNAL_STORAGE

        if (isPermissionGranted(accessWrite))
        {
            //permission grant no action
        }
        else{
            val title = "Internal storage"
            val message = "This is necessary for saving your data"
            askForPermission(accessWrite, title, message)
        }

        if (isPermissionGranted(accessRead))
        {
            //permission grant no action
        }
        else{
            val title = "Internal storage"
            val message = "This is necessary for saving your data"
            askForPermission(accessRead, title, message)
        }

    }

    fun checkInternalStoragePermission() : Boolean{
        val checkWritePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val checkReadPermission = Manifest.permission.READ_EXTERNAL_STORAGE

        return isPermissionGranted(checkWritePermission) && isPermissionGranted(checkReadPermission)
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