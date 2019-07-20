package com.example.nn4wchallenge

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import com.example.nn4wchallenge.AddActivitySpinners.addActivityItem
import com.example.nn4wchallenge.AddActivitySpinners.addColourItem
import com.example.nn4wchallenge.AddActivitySpinners.addSpinnerAdapter
import com.example.nn4wchallenge.database.internal.AddClothingHandler
import com.example.nn4wchallenge.imageHandling.retrieveImageHandler
import com.example.nn4wchallenge.imageHandling.saveImageHandler
import kotlinx.android.synthetic.main.activity_add.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
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


        val accessCamera = permissionsHandler(this, applicationContext)

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


        }
        saveBTN = findViewById(R.id.searchBTN)
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
        val goTo = Intent(applicationContext, MainActivity::class.java)
        startActivity(goTo)
    }

    private fun setUpColourSpinner(itemList : ArrayList<addColourItem>)
    {
        val colourSPN : Spinner = findViewById(R.id.colourSPN)

        //item basic list is a list of a parent class converted from list of child class
        val itemBasicList : ArrayList<addActivityItem> = itemList as ArrayList<addActivityItem>

        val colourAdapter = addSpinnerAdapter(applicationContext, itemBasicList)

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

        val typeAdapter = addSpinnerAdapter(applicationContext, itemList)

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

        val seasonAdapter = addSpinnerAdapter(applicationContext, itemList)

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

        }

    }

    //captured image displayed
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras.get("data") as Bitmap
            clothingPictureIV.setImageBitmap(imageBitmap)

            savedImage.savePhoto(imageBitmap)
            AddManager.setPicture(savedImage.savedPhotoPath)
            imagePath = savedImage.savedPhotoPath


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
