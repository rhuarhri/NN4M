package com.example.nn4wchallenge

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.core.content.FileProvider
import com.example.nn4wchallenge.AddActivitySpinners.addActivityItem
import com.example.nn4wchallenge.AddActivitySpinners.addColourItem
import com.example.nn4wchallenge.AddActivitySpinners.addSpinnerAdapter
import com.example.nn4wchallenge.database.internal.AddClothingHandler
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddActivity : AppCompatActivity() {

    private lateinit var AddManager : AddClothingHandler



    private lateinit var cameraBTN : Button
    private lateinit var saveBTN : Button
    private lateinit var clothingPictureIV : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        AddManager = AddClothingHandler(applicationContext)


        var colourList : ArrayList<addColourItem> = ArrayList()
        colourList.add(addColourItem("black", R.color.colorBlack, 0, 0, 0))
        colourList.add(addColourItem("blue", R.color.colorBlue, 0, 0, 255))
        colourList.add(addColourItem("light blue", R.color.colorLightBlue, 0, 255, 255))
        colourList.add(addColourItem("green", R.color.colorGreen, 0, 255, 0))
        colourList.add(addColourItem("yellow", R.color.colorYellow, 255, 255, 0))
        colourList.add(addColourItem("red", R.color.colorRed, 255, 0, 0))
        colourList.add(addColourItem("pink", R.color.colorPink, 255, 0, 255))
        colourList.add(addColourItem("white", R.color.colorWhite, 255, 255, 255))


        var typeList : ArrayList<addActivityItem> = ArrayList()
        typeList.add(addActivityItem("dress", R.drawable.dress_icon))
        typeList.add(addActivityItem("jacket", R.drawable.jacket_icon))
        typeList.add(addActivityItem("jumper", R.drawable.jumper_icon))
        typeList.add(addActivityItem("shirt", R.drawable.shirt_icon))
        typeList.add(addActivityItem("shorts", R.drawable.shorts_icon))
        typeList.add(addActivityItem("skirt", R.drawable.skirt_icon))
        typeList.add(addActivityItem("top", R.drawable.top_icon))
        typeList.add(addActivityItem("trousers", R.drawable.trousers_icon))



        var seasonList : ArrayList<addActivityItem> = ArrayList()
        seasonList.add(addActivityItem("summer", R.drawable.summer_icon))
        seasonList.add(addActivityItem("winter", R.drawable.winter_icon))
        seasonList.add(addActivityItem("party", R.drawable.party_icon))
        seasonList.add(addActivityItem("formal", R.drawable.formal_icon))

        setUpColourSpinner(colourList)
        setUpTypeSpinner(typeList)
        setUpSeasonSpinner(seasonList)

        clothingPictureIV = findViewById(R.id.pictureIV)

        cameraBTN = findViewById(R.id.cameraBTN)
        cameraBTN.setOnClickListener {

            //The camera could not be tested in the emulator but it could work but unknown without testing
            Toast.makeText(applicationContext, "camera not available", Toast.LENGTH_LONG).show()

            //remove this function if camera feacture in use
            AddManager.setPicture()

            //The camera functionality would have been handled by
            //launchCamera()


        }
        saveBTN = findViewById(R.id.saveBTN)
        saveBTN.setOnClickListener {

            var error = AddManager.saveClothingItem()

            if (error == "")
            {
                error = "Saved"
            }
            Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()


            goToUserClothesActivity()
        }

    }

    private fun goToUserClothesActivity()
    {
        val goTo : Intent = Intent(applicationContext, UserClothingDisplayActivity::class.java)
        startActivity(goTo)
    }

    private fun setUpColourSpinner(itemList : ArrayList<addColourItem>)
    {
        var colourSPN : Spinner = findViewById(R.id.colourSPN)

        //item basic list is a list of a parent class converted from list of child class
        var itemBasicList : ArrayList<addActivityItem> = itemList as ArrayList<addActivityItem>

        var colourAdapter : addSpinnerAdapter = addSpinnerAdapter(applicationContext, itemBasicList)

        colourSPN.adapter = colourAdapter

        colourSPN.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                var colourItem : addColourItem = itemList.get(position)
                AddManager.setClothingColour(colourItem.amountOfRed, colourItem.amountOfGreen, colourItem.amountOfBlue)
                Toast.makeText(applicationContext, "Clothing colour updated", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun setUpTypeSpinner(itemList : ArrayList<addActivityItem>)
    {
        var typeSPN : Spinner = findViewById(R.id.typeSPN)

        var typeAdapter : addSpinnerAdapter = addSpinnerAdapter(applicationContext, itemList)

        typeSPN.adapter = typeAdapter

        typeSPN.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                var typeItem : addActivityItem = itemList.get(position)
                AddManager.setClothingType(typeItem.itemTitle)
                Toast.makeText(applicationContext, "Clothing type updated", Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun setUpSeasonSpinner(itemList : ArrayList<addActivityItem>)
    {
        var seasonSPN : Spinner = findViewById(R.id.seasonSPN)

        var seasonAdapter : addSpinnerAdapter = addSpinnerAdapter(applicationContext, itemList)

        seasonSPN.adapter = seasonAdapter

        seasonSPN.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                var seasonItem : addActivityItem = itemList.get(position)
                AddManager.setClothingSeason(seasonItem.itemTitle)
                Toast.makeText(applicationContext, "Clothing season updated", Toast.LENGTH_SHORT).show()
            }

        }

    }


    /**Code for camera
     * The purpose of the camera feature is to show how the user's clothes match
    the clothes that they might be interested in.*/
    private val REQUEST_IMAGE_CAPTURE = 1
    private fun launchCamera()
    {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            //capture image not saved
            /*takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }*/

            //capture image and save to file
            takePictureIntent.resolveActivity(packageManager)?.also {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, AddManager.getFileLocation())
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }

    }

    //captured image displayed
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras!!.get("data") as Bitmap
            clothingPictureIV.setImageBitmap(imageBitmap)
        }

    }

    /*

    private fun setUpCamera()
    {
         cameraM = cameraManager(applicationContext)



        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            /*takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }*/

            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                /*
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File

                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )*/
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraM.getFile())
                    startActivityForResult(takePictureIntent, cameraM.getRequestImageCapture())
                }
            }
        //}
    }*/






}
