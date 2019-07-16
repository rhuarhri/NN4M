package com.example.nn4wchallenge

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.work.Data
import com.example.nn4wchallenge.AddActivitySpinners.addActivityItem
import com.example.nn4wchallenge.AddActivitySpinners.addColourItem
import com.example.nn4wchallenge.AddActivitySpinners.addSpinnerAdapter
import com.example.nn4wchallenge.database.internal.AddClothingHandler
import com.example.nn4wchallenge.imageHandling.retrieveImageHandler
import com.example.nn4wchallenge.imageHandling.saveImageHandler
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.cart_item_layout.*
import kotlinx.android.synthetic.main.cart_item_layout.clothingIV
import kotlinx.android.synthetic.main.clothing_item_layout.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import kotlin.collections.ArrayList

class AddActivity : AppCompatActivity() {

    private lateinit var AddManager : AddClothingHandler

    private lateinit var savedImage : saveImageHandler

    private lateinit var findImage : retrieveImageHandler

    private lateinit var cameraBTN : Button
    private lateinit var saveBTN : Button
    private lateinit var clothingPictureIV : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        AddManager = AddClothingHandler(applicationContext)

        savedImage = saveImageHandler(applicationContext)

        findImage = retrieveImageHandler(applicationContext)


        val colourList : ArrayList<addColourItem> = ArrayList()
        colourList.add(addColourItem("black", R.color.colorBlack, 0, 0, 0))
        colourList.add(addColourItem("blue", R.color.colorBlue, 0, 0, 255))
        colourList.add(addColourItem("light blue", R.color.colorLightBlue, 0, 255, 255))
        colourList.add(addColourItem("green", R.color.colorGreen, 0, 255, 0))
        colourList.add(addColourItem("yellow", R.color.colorYellow, 255, 255, 0))
        colourList.add(addColourItem("red", R.color.colorRed, 255, 0, 0))
        colourList.add(addColourItem("pink", R.color.colorPink, 255, 0, 255))
        colourList.add(addColourItem("white", R.color.colorWhite, 255, 255, 255))


        val typeList : ArrayList<addActivityItem> = ArrayList()
        typeList.add(addActivityItem("dress", R.drawable.dress_icon))
        typeList.add(addActivityItem("jacket", R.drawable.jacket_icon))
        typeList.add(addActivityItem("jumper", R.drawable.jumper_icon))
        typeList.add(addActivityItem("shirt", R.drawable.shirt_icon))
        typeList.add(addActivityItem("shorts", R.drawable.shorts_icon))
        typeList.add(addActivityItem("skirt", R.drawable.skirt_icon))
        typeList.add(addActivityItem("top", R.drawable.top_icon))
        typeList.add(addActivityItem("trousers", R.drawable.trousers_icon))



        val seasonList : ArrayList<addActivityItem> = ArrayList()
        seasonList.add(addActivityItem("summer", R.drawable.summer_icon))
        seasonList.add(addActivityItem("winter", R.drawable.winter_icon))
        seasonList.add(addActivityItem("party", R.drawable.party_icon))
        seasonList.add(addActivityItem("formal", R.drawable.formal_icon))

        setUpColourSpinner(colourList)
        setUpTypeSpinner(typeList)
        setUpSeasonSpinner(seasonList)


        clothingPictureIV = findViewById(R.id.pictureIV)


        val accessCamera: permissionsHandler = permissionsHandler(this, applicationContext)

        accessCamera.cameraPermission()
        cameraBTN = findViewById(R.id.cameraBTN)
        cameraBTN.setOnClickListener {

            if (savedImage.photoLocation == "")
            {
                launchCamera()
            }
            else
            {
                Toast.makeText(applicationContext, "image already added", Toast.LENGTH_LONG).show()
            }


            //The camera could not be tested in the emulator but it could work but unknown without testing
            //Toast.makeText(applicationContext, "camera not available", Toast.LENGTH_LONG).show()

            //remove this function if camera feature in use
            //AddManager.setPicture()
            //imagePath = "test"

            //The camera functionality would have been handled by
            //


            /*
            if (accessCamera.checkCameraPermission()) {
                Toast.makeText(applicationContext, "camera access granted", Toast.LENGTH_SHORT).show()
                launchCamera()
            }
            else
            {
                Toast.makeText(applicationContext, "camera access not granted", Toast.LENGTH_SHORT).show()
                accessCamera.cameraPermission()
            }*/

            //imageTest()

            /*
            var savedImage = saveImageHandler(applicationContext)
            var fileLocation : String = ""


            try {
                var file = savedImage.createImageFile()
                fileLocation = "file location is ${savedImage.savedPhotoPath}"
            }
            catch(e : Exception)
            {
                fileLocation = "error ${e.toString()}"
            }

*/

        }
        saveBTN = findViewById(R.id.saveBTN)
        saveBTN.setOnClickListener {

            try {
                var error = AddManager.saveClothingItem()
                goToHomeActivity()
            }
            catch (e : Exception)
            {
                Toast.makeText(applicationContext, "error ${e.toString()}", Toast.LENGTH_LONG).show()
            }

        }

        if (savedInstanceState != null)
        {
            typeSPN.setSelection(with(savedInstanceState) {getInt(typeKey)})
            colourSPN.setSelection(with(savedInstanceState) {getInt(colourKey)})
            seasonSPN.setSelection(with(savedInstanceState) {getInt(seasonKey)})

            if (with(savedInstanceState) {getString(imageKey)} != "")
            {
                AddManager.setPicture(savedImage.photoLocation)
                doAsync {

                    val foundImage : Bitmap = findImage.getBitmapFromFile(
                        savedImage.photoLocation,
                        clothingPictureIV.height,
                        clothingPictureIV.width)

                    uiThread {
                        clothingPictureIV.setImageBitmap(foundImage)
                    }
                }
            }
        }


    }

    private fun goToHomeActivity()
    {
        val goTo : Intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(goTo)
    }

    private fun setUpColourSpinner(itemList : ArrayList<addColourItem>)
    {
        val colourSPN : Spinner = findViewById(R.id.colourSPN)

        //item basic list is a list of a parent class converted from list of child class
        val itemBasicList : ArrayList<addActivityItem> = itemList as ArrayList<addActivityItem>

        val colourAdapter : addSpinnerAdapter = addSpinnerAdapter(applicationContext, itemBasicList)

        colourSPN.adapter = colourAdapter

        colourSPN.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                colourCurrentPosition = position
                val colourItem : addColourItem = itemList.get(position)
                AddManager.setClothingColour(colourItem.amountOfRed, colourItem.amountOfGreen, colourItem.amountOfBlue)

            }

        }

    }

    private fun setUpTypeSpinner(itemList : ArrayList<addActivityItem>)
    {
        val typeSPN : Spinner = findViewById(R.id.typeSPN)

        val typeAdapter : addSpinnerAdapter = addSpinnerAdapter(applicationContext, itemList)

        typeSPN.adapter = typeAdapter

        typeSPN.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                typeCurrentPosition = position
                val typeItem : addActivityItem = itemList.get(position)
                AddManager.setClothingType(typeItem.itemTitle)

            }

        }


    }

    private fun setUpSeasonSpinner(itemList : ArrayList<addActivityItem>)
    {
        val seasonSPN : Spinner = findViewById(R.id.seasonSPN)

        val seasonAdapter : addSpinnerAdapter = addSpinnerAdapter(applicationContext, itemList)

        seasonSPN.adapter = seasonAdapter

        seasonSPN.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                seasonCurrentPosition = position
                val seasonItem : addActivityItem = itemList.get(position)
                AddManager.setClothingSeason(seasonItem.itemTitle)

            }

        }

    }

    /**
     * test code for retriving image from url
     * */
    /*
    private fun imageTest()
    {
        val input : Data = Data.Builder()
            .putInt("height", clothingPictureIV.height)
            .putInt("width", clothingPictureIV.width)
            .putString("url", "https://images.riverisland.com//is//image//RiverIsland//739288_rollover")
            .putString("file", "")
            .build()

        var requiredHeight : Int = clothingPictureIV.height
        var requiredWidth : Int = clothingPictureIV.width
        var imageURL : String = "https://images.riverisland.com//is//image//RiverIsland//739288_rollover"
        var appContext : Context = applicationContext


    }*/


    /**Code for camera
     * The purpose of the camera feature is to show how the user's clothes match
    the clothes that they might be interested in.*/
    private val REQUEST_IMAGE_CAPTURE = 1
    private fun launchCamera()
    {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            //capture image not saved
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }

            /*
            //capture image and save to file
            takePictureIntent.resolveActivity(packageManager)?.also {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, savedImage.getFileLocation())
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }*/


        }

    }

    //captured image displayed
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        /*Toast.makeText(applicationContext, "picture added", Toast.LENGTH_LONG).show()
        AddManager.setPicture(savedImage.photoLocation)
        imagePath = savedImage.photoLocation

        doAsync {

            val foundImage : Bitmap = findImage.getBitmapFromFile(imagePath, clothingPictureIV.height, clothingPictureIV.width)

            uiThread {
                clothingPictureIV.setImageBitmap(foundImage)
            }
        }

        Toast.makeText(applicationContext, "picture path is $imagePath", Toast.LENGTH_LONG).show()

        */

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras.get("data") as Bitmap
            clothingPictureIV.setImageBitmap(imageBitmap)

            savedImage.savePhoto(imageBitmap)
            AddManager.setPicture(savedImage.savedPhotoPath)
            imagePath = savedImage.savedPhotoPath



            //Toast.makeText(applicationContext, "picture path is ${savedImage.savedPhotoPath}", Toast.LENGTH_LONG).show()

            /*
            val imgFile = File(savedImage.savedPhotoPath)
            if (imgFile.exists()) {
                clothingPictureIV.setImageURI(Uri.fromFile(imgFile))
            }*/
        }
    }


    //save state code
    private val typeKey : String = "type"
    private var typeCurrentPosition : Int = 0
    private val colourKey : String = "colour"
    private var colourCurrentPosition : Int = 0
    private val seasonKey : String = "season"
    private var seasonCurrentPosition : Int = 0
    private val imageKey : String = "image"
    private var imagePath : String = ""

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {

        if (outState != null)
        {
            outState.putInt(typeKey, typeCurrentPosition)
            outState.putInt(colourKey, colourCurrentPosition)
            outState.putInt(seasonKey, seasonCurrentPosition)
            outState.putString(imageKey, imagePath)
        }

        super.onSaveInstanceState(outState, outPersistentState)
    }
}
