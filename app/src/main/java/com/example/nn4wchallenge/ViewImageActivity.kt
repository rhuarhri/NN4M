package com.example.nn4wchallenge

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.FragmentTransaction
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.nn4wchallenge.database.external.DataTranslation
import com.example.nn4wchallenge.database.internal.AddClothingHandler
import com.example.nn4wchallenge.database.internal.DatabaseCommands
import com.example.nn4wchallenge.database.internal.DatabaseManager
import com.example.nn4wchallenge.fragmentCode.FilterHandler
import com.example.nn4wchallenge.fragmentCode.colourPicker
import com.example.nn4wchallenge.fragmentCode.fromFragment
import com.example.nn4wchallenge.imageHandling.RemoveImageHandler
import com.example.nn4wchallenge.imageHandling.RetrieveImageHandler
import com.example.nn4wchallenge.imageHandling.SaveImageHandler
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File

class ViewImageActivity : AppCompatActivity(), fromFragment {

    /*
    The purpose fo this screen to allow the user to edit an item of
    clothing that they have saved
     */
    private lateinit var savedImage : SaveImageHandler

    private lateinit var pictureIV : ImageView
    private lateinit var cameraBTN : Button
    private lateinit var colourBTN : Button
    private lateinit var editBTN : Button
    private lateinit var saveBTN : Button

    private var id : Int = 0
    private var imageLocation : String = ""
    private var oldLocation : String = ""
    private var colour : String = ""
    private var type : String = ""
    private var season : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)

        id = intent.getIntExtra("id", 0)
        imageLocation = intent.getStringExtra("image")
        colour = intent.getStringExtra("colour")
        type = intent.getStringExtra("type")
        season = intent.getStringExtra("season")

        savedImage = SaveImageHandler(applicationContext)
        savedImage.savedPhotoPath = imageLocation
        oldLocation = savedImage.savedPhotoPath

        pictureIV = findViewById(R.id.pictureIV)

        doAsync {

            val getImage = RetrieveImageHandler(applicationContext)

            val foundImage : Bitmap = getImage.getBitmapFromFile(imageLocation, pictureIV.height, pictureIV.width)

            uiThread {
                pictureIV.setImageBitmap(foundImage)
            }
        }

        cameraBTN = findViewById(R.id.cameraBTN)
        cameraBTN.setOnClickListener {
            launchCamera()
        }

        colourBTN = findViewById(R.id.colourBTN)
        colourBTN.setOnClickListener {
            val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
            openFragment.replace(R.id.fragLayout, colourPicker())
            openFragment.commit()
        }

        colourBTN.setBackgroundColor(Color.parseColor("#$colour"))

        editBTN = findViewById(R.id.editBTN)
        editBTN.setOnClickListener {
            val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
            openFragment.replace(R.id.fragLayout, FilterHandler())
            openFragment.commit()
        }

        saveBTN = findViewById(R.id.saveBTN)
        saveBTN.setOnClickListener {
            /*val addManager : AddClothingHandler = AddClothingHandler(applicationContext)
            if (savedImage.savedPhotoPath != "" && File(savedImage.savedPhotoPath).exists())
            {
                addManager.setPicture(savedImage.savedPhotoPath)
                addManager.setClothingType(type)
                addManager.setClothingSeason(season)
                val converter : DataTranslation = DataTranslation()
                converter.stringToRGB(colour)
                addManager.setClothingColour(
                    converter.redAmount, converter.greenAmount, converter.blueAmount
                )
                addManager.saveClothingItem()
            }*/

            updateClothing()
        }
    }

    private fun updateClothing()
    {
        val commands : DatabaseCommands = DatabaseCommands()
        val inputData : Data.Builder = Data.Builder()
        inputData.putInt(commands.Clothing_ID, id)
        inputData.putString(commands.Clothing_color, colour)
        inputData.putString(commands.Clothing_picture, imageLocation)
        inputData.putString(commands.Clothing_type, type)
        inputData.putString(commands.Clothing_season, season)
        inputData.putString(commands.Clothing_DB, commands.Clothing_DB)
        inputData.putString(commands.Clothing_Update, commands.Clothing_Update)

        val updatedClothing : Data = inputData.build()

        val updateClothingItem = OneTimeWorkRequestBuilder<DatabaseManager>()
            .setInputData(updatedClothing)
            .build()

        WorkManager.getInstance().enqueue(updateClothingItem)
    }

    /*
    Even though the app has it's own way to capture images it might not entirely suitable
    for everyone so an alternative is provided here
     */
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
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras.get("data") as Bitmap
            pictureIV.setImageBitmap(imageBitmap)

            savedImage.savePhoto(imageBitmap)
            if (savedImage.savedPhotoPath  != oldLocation)
            {
                //delete file if already exists
                val imageRemover: RemoveImageHandler = RemoveImageHandler()
                imageRemover.removeImage(oldLocation)
            }

            oldLocation = savedImage.savedPhotoPath
        }
    }

    //from fragment
    override fun onColourSelected(colourHex: String) {
        colourBTN.setBackgroundColor(Color.parseColor("#$colourHex"))
        this.colour = colourHex
    }

    override fun onFilterChange(type: String, season: String) {
        this.type = type
        this.season = season
    }

    override fun onSaveImageGranted() {
        //do nothing
    }

    override fun onProductSelected() {
        //do nothing
    }
}
