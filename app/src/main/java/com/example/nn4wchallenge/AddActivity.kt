package com.example.nn4wchallenge

/*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.TextureView
import android.view.View
import android.widget.*
import androidx.camera.core.*
import com.example.nn4wchallenge.AddActivitySpinners.AddActivityItem
import com.example.nn4wchallenge.AddActivitySpinners.AddColourItem
import com.example.nn4wchallenge.AddActivitySpinners.AddSpinnerAdapter
import com.example.nn4wchallenge.database.internal.AddClothingHandler
import com.example.nn4wchallenge.imageHandling.RetrieveImageHandler
import com.example.nn4wchallenge.imageHandling.SaveImageHandler
import kotlinx.android.synthetic.main.activity_add.*
import java.io.File
import kotlin.collections.ArrayList
import com.example.nn4wchallenge.cameraCode.CameraHandler
import com.example.nn4wchallenge.imageHandling.PreviewImageHandler


class AddActivity : AppCompatActivity() {

    private val clothesData = clothesSpinnerData()

    private lateinit var AddManager : AddClothingHandler

    private lateinit var savedImage : SaveImageHandler

    private lateinit var findImage : RetrieveImageHandler

    private lateinit var previewImage : PreviewImageHandler

    private lateinit var cameraManger : CameraHandler

    private lateinit var cameraBTN : Button
    private lateinit var previewDisplayBTN : Button
    private lateinit var saveBTN : Button
    private lateinit var pictureTV : TextureView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        AddManager = AddClothingHandler(applicationContext)

        savedImage = SaveImageHandler(applicationContext)

        findImage = RetrieveImageHandler(applicationContext)

        previewImage = PreviewImageHandler(this)

        pictureTV = findViewById(R.id.pictureTV)

        val accessPermissions = PermissionsHandler(this, applicationContext)

        val accessCamera = PermissionsHandler(this, applicationContext)

        accessCamera.cameraPermission()
        cameraBTN = findViewById(R.id.cameraBTN)

        if (accessPermissions.checkCameraPermission()) {

            cameraManger = CameraHandler(this, pictureTV)

            cameraBTN.setOnClickListener {

                val photoFile = savedImage.getPhotoFile()

                cameraManger.imageCapture.takePicture(photoFile, object : ImageCapture.OnImageSavedListener
                {
                    override fun onImageSaved(file: File) {
                        previewImage.showDialog(savedImage.savedPhotoPath, "Do you like this ")
                    }

                    override fun onError(useCaseError: ImageCapture.UseCaseError, message: String, cause: Throwable?) {
                        Toast.makeText(applicationContext, "image capture failed", Toast.LENGTH_SHORT).show()
                    }

                })
            }

        } else {
            Toast.makeText(applicationContext, "camera permission not granted", Toast.LENGTH_LONG).show()

        }


        previewDisplayBTN = findViewById(R.id.previewBTN)
        previewDisplayBTN.setOnClickListener {
            
            previewImage.showDialog(savedImage.savedPhotoPath, "test")
        }


        setUpColourSpinner(clothesData.getColourList())
        setUpTypeSpinner(clothesData.getTypeList())
        setUpSeasonSpinner(clothesData.getSeasonsList())


        saveBTN = findViewById(R.id.searchBTN)
        saveBTN.setOnClickListener {

            if (File(savedImage.savedPhotoPath).exists())
            {
                AddManager.setPicture(savedImage.savedPhotoPath)
            try {
                var error = AddManager.saveClothingItem()
                goToHomeActivity()
            }
            catch (e : Exception)
            {
                Toast.makeText(applicationContext, "error ${e.toString()}", Toast.LENGTH_LONG).show()
            }
            }
            else
            {
                Toast.makeText(applicationContext, "no image captured ", Toast.LENGTH_LONG).show()
            }


        }

        if (savedInstanceState != null)
        {
            typeSPN.setSelection(with(savedInstanceState) {getInt(typeKey)})
            colourSPN.setSelection(with(savedInstanceState) {getInt(colourKey)})
            seasonSPN.setSelection(with(savedInstanceState) {getInt(seasonKey)})

            if (savedInstanceState.getString(imageKey) == null)
            {
                savedImage.savedPhotoPath = ""
            }
            else
            {
                savedImage.savedPhotoPath = savedInstanceState.getString(imageKey) as String
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

    /*
    @Throws(Exception::class)
    private fun setupCamera()
    {
        CameraX.unbindAll()

        val lensFacing = CameraX.LensFacing.BACK

        val aspectRatio = Rational (clothingPictureIV.getWidth(), clothingPictureIV.getHeight())

        val size = Size(clothingPictureIV.getWidth(), clothingPictureIV.getHeight())

        val previewConfig = PreviewConfig.Builder().apply{
            setLensFacing(lensFacing)
            setTargetAspectRatio(aspectRatio)
            setTargetResolution(size)
        }.build()

        val preview = Preview(previewConfig)


        preview.setOnPreviewOutputUpdateListener{
                previewOutput : Preview.PreviewOutput? ->


            val parent = clothingPictureIV.parent as ViewGroup
            parent.removeView(clothingPictureIV)
            parent.addView(clothingPictureIV, 0)

            clothingPictureIV.surfaceTexture = previewOutput!!.surfaceTexture

            updateTransform()

        }


        val imageCaptureConfig = ImageCaptureConfig.Builder().build()
        imageCapture = ImageCapture(imageCaptureConfig)


        cameraBTN.setOnClickListener {

                try {
                    launchCamera()
                    //val foundImage = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.no_image_icon)
                    //showImage(foundImage)

                }
                catch (e : Exception)
                {
                    Toast.makeText(applicationContext, "capture image error ${e.toString()}", Toast.LENGTH_LONG).show()
                }

        }


        CameraX.bindToLifecycle(this, preview, imageCapture)
    }

    private fun updateTransform()
    {
        val mx = Matrix()
        val w = clothingPictureIV.measuredWidth
        val h = clothingPictureIV.measuredHeight

        val cX = w / 2f
        val cY = h / 2f

        var rotationDgr : Int = 0
        val rotation : Int = clothingPictureIV.rotation.toInt()

        when (rotation)
        {

            Surface.ROTATION_0 -> rotationDgr = 0
            Surface.ROTATION_90 -> rotationDgr = 90
            Surface.ROTATION_180 -> rotationDgr = 180
            Surface.ROTATION_270 -> rotationDgr = 270
        }

        mx.postRotate(rotationDgr.toFloat(), cX, cY)
        clothingPictureIV.setTransform(mx)

    }

    @Throws(Exception::class)
    private fun launchCamera()
    {
        val photoFile = savedImage.getPhotoFile()

        imageCapture.takePicture(photoFile, object : ImageCapture.OnImageSavedListener
        {
            override fun onImageSaved(file: File) {
                Toast.makeText(applicationContext, "image captured", Toast.LENGTH_SHORT).show()
                //getBitmapImage()
                previewImage.showDialog(savedImage.savedPhotoPath, "Do you like this ")
                }

            override fun onError(useCaseError: ImageCapture.UseCaseError, message: String, cause: Throwable?) {
                Toast.makeText(applicationContext, "image capture failed", Toast.LENGTH_SHORT).show()
            }

        }
        )

    }*/

    /*
    private fun getBitmapImage()
    {
        if (savedImage.savedPhotoPath != "") {
            //doAsync {


            try {
                val foundImage: Bitmap = findImage.getBitmapFromFile(
                    savedImage.savedPhotoPath,
                    400,
                    400
                )


                try {
                    showImage(foundImage)
                } catch (e: Exception) {
                    Toast.makeText(
                        applicationContext,
                        "display image error ${e.toString()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            catch (e: Exception)
            {
                Toast.makeText(
                    applicationContext,
                    "decode image error ${e.toString()}",
                    Toast.LENGTH_LONG
                ).show()
            }

                //uiThread {
                    //previewImage.showDialog(foundImage, savedImage.savedPhotoPath, "Do you like this image?")

                    Toast.makeText(applicationContext, "Async task done", Toast.LENGTH_SHORT).show()

            *
                    try {
                        showImage(foundImage)
                    } catch (e: Exception) {
                        Toast.makeText(
                            applicationContext,
                            "display image error ${e.toString()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }*

                //}

            //}
        }
        else
        {
            Toast.makeText(applicationContext, "no image file", Toast.LENGTH_SHORT).show()
        }
    }

    @Throws(Exception::class)
    private fun showImage(image : Bitmap)
    {
        val myAlert = Dialog(this)
        myAlert.setContentView(R.layout.image_alert_layout)
        val titleTXT : TextView = myAlert.findViewById(R.id.TitleTXT)
        titleTXT.text = "Do you like this image?"

        val pictureIV : ImageView = myAlert.findViewById(R.id.capturedPictureIV)
        pictureIV.setImageBitmap(image)

        val likeBTN : Button = myAlert.findViewById(R.id.likeBTN)
        likeBTN.setOnClickListener {



            //myAlert.cancel()
        }

        val dislikeBTN : Button = myAlert.findViewById(R.id.dislikeBTN)
        dislikeBTN.setOnClickListener {

            val removeHandler = RemoveImageHandler()
            if (removeHandler.removeImage(savedImage.savedPhotoPath))
            {
                savedImage.savedPhotoPath = ""
            }

            //myAlert.cancel()

        }

        myAlert.show()

    }


    *Code for camera
     * The purpose of the camera feature is to show how the user's clothes match
    the clothes that they might be interested in.

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
    }*


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
            outState.putString(imageKey, savedImage.savedPhotoPath)
        }

        super.onSaveInstanceState(outState, outPersistentState)
    }
}
*/