package com.example.nn4wchallenge

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.TextureView
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
//import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.nn4wchallenge.cameraCode.CameraHandler
import com.example.nn4wchallenge.database.MatchColourHandler
import com.example.nn4wchallenge.database.external.DataTranslation
import com.example.nn4wchallenge.database.internal.AddClothingHandler
import com.example.nn4wchallenge.database.internal.DatabaseCommands
import com.example.nn4wchallenge.database.internal.DatabaseManager
import com.example.nn4wchallenge.fragmentCode.*
import com.example.nn4wchallenge.imageHandling.RemoveImageHandler
import com.example.nn4wchallenge.imageHandling.RetrieveImageHandler
import com.example.nn4wchallenge.imageHandling.SaveImageHandler
import com.example.nn4wchallenge.slideShowCode.SlideShowAdapter
import com.example.nn4wchallenge.slideShowCode.SlideShowListener
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File

class MatchActivity : AppCompatActivity(), fromFragment, SlideShowListener {

    private lateinit var cameraManger : CameraHandler
    private lateinit var savedImage : SaveImageHandler
    private lateinit var oldFile : String

    private lateinit var totalCostTXT : TextView
    private lateinit var userClothingBTN : Button
    private lateinit var cartBTN : Button
    private lateinit var colourBTN : Button
    private lateinit var cameraBTN : Button
    private lateinit var viewImageBTN : Button
    private lateinit var filterBTN : Button
    private lateinit var flashBTN : Button
    private lateinit var cameraTV : TextureView
    private lateinit var pictureRV : RecyclerView

    //information about products the user can buy
    private lateinit var imageURLList : Array<String?>
    private lateinit var itemDescriptionURLList : Array<String?>
    private lateinit var typeList : Array<String?>
    private lateinit var seasonList : Array<String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        val buttonAnim : Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.button_click)

        totalCostTXT = findViewById(R.id.totalTXT)
        setupTotalPrice()

        cartBTN = findViewById(R.id.cartBTN)
        cartBTN.setOnClickListener {
            val goToViewCartScreen = Intent(applicationContext, ViewCartActivity::class.java)
            startActivity(goToViewCartScreen)
        }

        userClothingBTN = findViewById(R.id.clothingBTN)
        userClothingBTN.setOnClickListener {
            val goToUserClothingDisplayScreen = Intent(applicationContext, UserClothingDisplayActivity::class.java)
            startActivity(goToUserClothingDisplayScreen)
        }

        val accessPermissions = PermissionsHandler(this, applicationContext)
        savedImage = SaveImageHandler(applicationContext)
        oldFile = savedImage.savedPhotoPath

        cameraTV = findViewById(R.id.cameraTV)
        if (accessPermissions.checkCameraPermission()) {

            cameraManger = CameraHandler(this, cameraTV)

            cameraBTN = findViewById(R.id.cameraBTN)

            cameraBTN.setOnClickListener {

                cameraBTN.startAnimation(buttonAnim)

                val photoFile = savedImage.getPhotoFile()

                cameraManger.imageCapture.takePicture(photoFile, object : ImageCapture.OnImageSavedListener
                {
                    override fun onImageSaved(file: File) {
                        if (savedImage.savedPhotoPath  != oldFile)
                        {
                            //delete file if already exists
                            val imageRemover: RemoveImageHandler = RemoveImageHandler()
                            imageRemover.removeImage(oldFile)
                        }

                        val openFragment: FragmentTransaction = supportFragmentManager.beginTransaction()
                        openFragment.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        openFragment.replace(R.id.saveInfoFRG, SaveImageNotification())
                        openFragment.commit()

                        oldFile = savedImage.savedPhotoPath

                        imageCaptureSoundPlayer()

                        findColourInImage()

                    }

                    override fun onError(useCaseError: ImageCapture.UseCaseError, message: String, cause: Throwable?) {

                    }

                })

            }
        }

        viewImageBTN = findViewById(R.id.viewBTN)
        viewImageBTN.setOnClickListener {
            if (savedImage.savedPhotoPath == "")
            {

            }
            else if (!File(savedImage.savedPhotoPath).exists())
            {

            }
            else {
                val fragIn : Bundle = Bundle()
                fragIn.putString("file", savedImage.savedPhotoPath)

                val previewImage: CapturedImagePreview = CapturedImagePreview()
                previewImage.arguments = fragIn
                val openFragment: FragmentTransaction = supportFragmentManager.beginTransaction()
                openFragment.setCustomAnimations(R.anim.slide_right_left_in, R.anim.slide_left_right_out)
                openFragment.replace(R.id.imagePreviewFRG, previewImage)
                openFragment.commit()
            }
        }

        colourBTN = findViewById(R.id.colourBTN)
        colourBTN.setOnClickListener {

            val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
            openFragment.setCustomAnimations(R.anim.slide_left_right_in, R.anim.slide_left_right_out)
            openFragment.replace(R.id.colourPickerFRG, colourPicker())
            openFragment.commit()

        }

        filterBTN = findViewById(R.id.filterBTN)
        filterBTN.setOnClickListener {
            val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
            openFragment.setCustomAnimations(R.anim.slide_right_left_in, R.anim.slide_left_right_out)
            openFragment.replace(R.id.colourPickerFRG, FilterHandler())
            openFragment.commit()
        }

        pictureRV = findViewById(R.id.productRV)

        flashBTN = findViewById(R.id.flashBTN)



        flashBTN.setOnClickListener {

            flashBTN.startAnimation(buttonAnim)

        }

    }

    private fun imageCaptureSoundPlayer()
    {
        val sound : MediaPlayer = MediaPlayer.create(applicationContext, R.raw.image_capture_sound)
        sound.start()
    }

    private fun findColourInImage()
    {

        val imageHandler : RetrieveImageHandler = RetrieveImageHandler(applicationContext)

        doAsync {

            val image : Bitmap = imageHandler.getBitmapFromFile(savedImage.savedPhotoPath, cameraTV.height, cameraTV.width)

            uiThread {

               Palette.from(image).generate(Palette.PaletteAsyncListener
               {palette ->
                   if (palette != null) {
                       val mainColour : Int = palette.dominantSwatch!!.rgb
                       colourBTN.setBackgroundColor(mainColour)
                       searchForProduct(Integer.toHexString(mainColour))
                   }
               })

            }
        }
    }


    private fun searchForProduct(colour : String)
    {

        val input: Data = Data.Builder().putString("colour", colour).build()

        val searchWorkRequest = OneTimeWorkRequestBuilder<MatchColourHandler>()
            .setInputData(input)
            .build()

        WorkManager.getInstance().enqueue(searchWorkRequest)

        WorkManager.getInstance().getWorkInfoByIdLiveData(searchWorkRequest.id)
            .observe(this, Observer { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {

                    imageURLList = workInfo.outputData.getStringArray("images")!!
                    itemDescriptionURLList = workInfo.outputData.getStringArray("descriptions")!!
                    typeList = workInfo.outputData.getStringArray("type")!!
                    seasonList = workInfo.outputData.getStringArray("season")!!

                    if (imageURLList.size == 0)
                    {
                        //Toast.makeText(applicationContext, "no result found", Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        setupRecyclerView(imageURLList as Array<String>, itemDescriptionURLList as Array<String>)
                    }

                }

                if (workInfo != null && workInfo.state == WorkInfo.State.FAILED) {
                    val error: String? = workInfo.outputData.getString("error")

                    //Toast.makeText(applicationContext, "search error : ${error.toString()}", Toast.LENGTH_LONG)
                        //.show()
                }
            })
    }

    private fun filterResult(type : String, season : String )
    {

        val input : Data = Data.Builder()
            .putStringArray("image", imageURLList)
            .putStringArray("description", itemDescriptionURLList)
            .putStringArray("type", typeList)
            .putStringArray("season", seasonList)
            .putString("userType", type)
            .putString("userSeason", season)
            .build()


        val searchWorkRequest = OneTimeWorkRequestBuilder<MatchColourHandler>()
            .setInputData(input)
            .build()

        WorkManager.getInstance().enqueue(searchWorkRequest)

        WorkManager.getInstance().getWorkInfoByIdLiveData(searchWorkRequest.id)
            .observe(this, Observer { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {

                    imageURLList = workInfo.outputData.getStringArray("images")!!
                    itemDescriptionURLList = workInfo.outputData.getStringArray("descriptions")!!
                    typeList = workInfo.outputData.getStringArray("type")!!
                    seasonList = workInfo.outputData.getStringArray("season")!!

                    if (imageURLList.size == 0)
                    {
                        //Toast.makeText(applicationContext, "no result found", Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        setupRecyclerView(imageURLList as Array<String>, itemDescriptionURLList as Array<String>)
                    }

                }

                if (workInfo != null && workInfo.state == WorkInfo.State.FAILED) {
                    val error: String? = workInfo.outputData.getString("error")

                    //Toast.makeText(applicationContext, "search error : ${error.toString()}", Toast.LENGTH_LONG)
                        //.show()
                }
            })


    }

    private fun setupTotalPrice()
    {
        val commands = DatabaseCommands()

        val input : Data = Data.Builder()
            .putString(commands.Cart_DB, commands.Cart_DB)
            .putString(commands.Cart_Get_Prices, commands.Cart_Get_Prices)
            .build()

        val getTotalWorker = OneTimeWorkRequestBuilder<DatabaseManager>().setInputData(input).build()


        WorkManager.getInstance().enqueue(getTotalWorker)

        WorkManager.getInstance().getWorkInfoByIdLiveData(getTotalWorker.id).observe(this, Observer {
                workInfo ->

            val total = workInfo.outputData.getDouble(commands.Cart_total_price, 0.0)

            totalCostTXT.text = "total $total"

        })
    }

    private fun setupRecyclerView(images : Array<String>, descriptions : Array<String>)
    {

        val rvAdapter: RecyclerView.Adapter<*> = SlideShowAdapter(applicationContext, images, descriptions, this)

        pictureRV.apply {

            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

            adapter = rvAdapter
        }

    }

//from fragment info
    private var hexColour : String = "ff000000"
    override fun onColourSelected(colourHex: String) {
        hexColour = colourHex
        colourBTN.setBackgroundColor(Color.parseColor("#$colourHex"))
        searchForProduct(colourHex)
    }

    private var type = "dress"
    private var season = "summer"
    override fun onFilterChange(type: String, season: String) {
        this.type = type
        this.season = season

        if (type == null || type == "")
        {
            //Toast.makeText(applicationContext, "search error : no type", Toast.LENGTH_LONG)
                //.show()
        }
        else if (season == null || season == "")
        {
            //Toast.makeText(applicationContext, "search error : no season", Toast.LENGTH_LONG)
                //.show()
        }
        else {
            filterResult(type, season)
        }
    }

    private lateinit var addManager : AddClothingHandler
    override fun onSaveImageGranted() {
        addManager = AddClothingHandler(applicationContext)
        if (savedImage.savedPhotoPath != "" && File(savedImage.savedPhotoPath).exists())
        {
            addManager.setPicture(savedImage.savedPhotoPath)
            addManager.setClothingType(type)
            addManager.setClothingSeason(season)
            val converter : DataTranslation = DataTranslation()
            converter.stringToRGB(hexColour)
            addManager.setClothingColour(
                converter.redAmount, converter.greenAmount, converter.blueAmount
            )
            addManager.saveClothingItem()
        }

    }

    override fun onProductSelected() {

        /*
        val descriptionURL : String? = itemDescriptionURLList[productPosition]

        if (descriptionURL != null && descriptionURL != "") {
            val goToItemDescriptionScreen = Intent(applicationContext, ItemDescriptionActivity::class.java)
            goToItemDescriptionScreen.putExtra("description", descriptionURL)
            startActivity(goToItemDescriptionScreen)
        }*/

        setupTotalPrice()
    }

    //from recyclerView
    private var productPosition : Int = 0
    override fun onItemClick(description : String) {
        //Toast.makeText(applicationContext, "position is $position", Toast.LENGTH_LONG).show()
        //productPosition = position

        //val imageURL : String? = imageURLList[productPosition]

        if(description != "") {
            val fragIn: Bundle = Bundle()
            fragIn.putString("description", description)


            val previewImage: productImagePreview = productImagePreview()
            previewImage.arguments = fragIn
            val openFragment: FragmentTransaction = supportFragmentManager.beginTransaction()
            openFragment.setCustomAnimations(R.anim.slide_bottom_top_in, R.anim.slide_bottom_top_out)
            openFragment.replace(R.id.imagePreviewFRG, previewImage)
            openFragment.commit()
        }
    }

}
