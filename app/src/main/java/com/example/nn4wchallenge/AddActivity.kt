package com.example.nn4wchallenge

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import com.example.nn4wchallenge.AddActivitySpinners.AddActivityItem
import com.example.nn4wchallenge.AddActivitySpinners.AddColourItem
import com.example.nn4wchallenge.AddActivitySpinners.AddSpinnerAdapter
import com.example.nn4wchallenge.database.internal.AddClothingHandler
import com.example.nn4wchallenge.imageHandling.RetrieveImageHandler
import com.example.nn4wchallenge.imageHandling.SaveImageHandler
import kotlinx.android.synthetic.main.activity_add.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.collections.ArrayList

class AddActivity : AppCompatActivity() {

    private val clothesData = clothesSpinnerData()

    private lateinit var AddManager : AddClothingHandler

    private lateinit var savedImage : SaveImageHandler

    private lateinit var findImage : RetrieveImageHandler

    private lateinit var cameraBTN : Button
    private lateinit var saveBTN : Button
    private lateinit var clothingPictureIV : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        AddManager = AddClothingHandler(applicationContext)

        savedImage = SaveImageHandler(applicationContext)

        findImage = RetrieveImageHandler(applicationContext)


        setUpColourSpinner(clothesData.getColourList())
        setUpTypeSpinner(clothesData.getTypeList())
        setUpSeasonSpinner(clothesData.getSeasonsList())


        clothingPictureIV = findViewById(R.id.pictureIV)


        val accessCamera = PermissionsHandler(this, applicationContext)

        accessCamera.cameraPermission()
        cameraBTN = findViewById(R.id.cameraBTN)
        cameraBTN.setOnClickListener {

            if (savedImage.savedPhotoPath == "")
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
                AddManager.setPicture(savedImage.savedPhotoPath)
                doAsync {

                    val foundImage : Bitmap = findImage.getBitmapFromFile(
                        savedImage.savedPhotoPath,
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

    private fun setUpColourSpinner(itemList : ArrayList<AddColourItem>)
    {
        val colourSPN : Spinner = findViewById(R.id.colourSPN)

        //item basic list is a list of a parent class converted from list of child class
        val itemBasicList : ArrayList<AddActivityItem> = itemList as ArrayList<AddActivityItem>

        val colourAdapter = AddSpinnerAdapter(applicationContext, itemBasicList)

        colourSPN.adapter = colourAdapter

        colourSPN.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                colourCurrentPosition = position
                val colourItem : AddColourItem = itemList[position]
                AddManager.setClothingColour(colourItem.amountOfRed, colourItem.amountOfGreen, colourItem.amountOfBlue)

            }

        }

    }

    private fun setUpTypeSpinner(itemList : ArrayList<AddActivityItem>)
    {
        val typeSPN : Spinner = findViewById(R.id.typeSPN)

        val typeAdapter = AddSpinnerAdapter(applicationContext, itemList)

        typeSPN.adapter = typeAdapter

        typeSPN.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                typeCurrentPosition = position
                val typeItem : AddActivityItem = itemList[position]
                AddManager.setClothingType(typeItem.itemTitle)

            }

        }


    }

    private fun setUpSeasonSpinner(itemList : ArrayList<AddActivityItem>)
    {
        val seasonSPN : Spinner = findViewById(R.id.seasonSPN)

        val seasonAdapter = AddSpinnerAdapter(applicationContext, itemList)

        seasonSPN.adapter = seasonAdapter

        seasonSPN.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                seasonCurrentPosition = position
                val seasonItem : AddActivityItem = itemList[position]
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
