package com.example.nn4wchallenge.OpenCVCode


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.nn4wchallenge.database.external.DataTranslation
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.File
import java.io.FileInputStream

/*
Looked into the possibility that this could be run on it's own thread
however this proved problematic
 */
class CVHandler /*(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams)*/{


    private lateinit var usedImage : Bitmap

    private var redAmount : Int = 0
    private var greenAmount : Int = 0
    private var blueAmount : Int = 0


    private var blobColorHsv: Scalar = Scalar(255.0)
    private lateinit var blobColorRgba: Scalar

    // Lower and Upper bounds for range checking in HSV color space
    private val mLowerBound = Scalar(0.0)
    private val mUpperBound = Scalar(0.0)
    private var mContours = ArrayList<MatOfPoint>()
    private val mMinContourArea = 0.1

    // Cache
    private var mPyrDownMat = Mat()
    private var mHsvMat = Mat()
    private var mMask = Mat()
    private var mDilatedMask = Mat()
    private var mHierarchy = Mat()

    /*
    private val mLoaderCallback = object : BaseLoaderCallback(applicationContext) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    //success
                    //loadedSuccess = true
                    //openCVHandler = CVHandler()
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }*/

    /*
    override fun doWork(): Result {

        val filePath : String? = inputData.getString("location")

        if (!OpenCVLoader.initDebug()) {
            //Internal OpenCV library not found. Using OpenCV Manager for initialization
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, applicationContext, mLoaderCallback)
        } else {
            //OpenCV library found inside package. Using it!
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }

        if (filePath != null)
        {
            inputImage(filePath)

            val converter : DataTranslation = DataTranslation()

            val hexColour : String = converter.rgbToHexString(redAmount, greenAmount, blueAmount)

            val output : Data = Data.Builder()
                .putString("colour", hexColour)
                .build()
            return Result.failure(output)
        }
        else
        {
            val output : Data = Data.Builder()
                .putString("error", "no file path")
                .build()
            return Result.failure(output)
        }

    }*/


    public fun inputImage(filePath : String)
    {

        val origin: Mat = Imgcodecs.imread(filePath)

        val allImageMat = Mat()

        Imgproc.cvtColor(origin, allImageMat, Imgproc.COLOR_BGR2RGB)

        val imageCentreRect: Rect = Rect()

        imageCentreRect.x = ((allImageMat.width() / 2) - 60)
        imageCentreRect.y = ((allImageMat.height() / 2) - 60)

        imageCentreRect.width = 60
        imageCentreRect.height = 60

        val imageCentreRgba: Mat = allImageMat.submat(imageCentreRect)

        val imageCentreHsv: Mat = Mat()

        Imgproc.cvtColor(imageCentreRgba, imageCentreHsv, Imgproc.COLOR_RGB2HSV_FULL)

        //touchedRegionHsv = mDetector.mDilatedMask

        // Calculate average color
        blobColorHsv = Core.sumElems(imageCentreHsv)
        val pointCount = imageCentreRect.width * imageCentreRect.height
        for (i in 0 until blobColorHsv.`val`.size) {
            var value = blobColorHsv.`val`[i]
            value /= pointCount
            blobColorHsv.`val`.set(i, value)
        }

        blobColorRgba = convertScalarHsv2Rgba(blobColorHsv)

        redAmount = blobColorRgba.`val`[0].toInt()
        greenAmount = blobColorRgba.`val`[1].toInt()
        blueAmount = blobColorRgba.`val`[2].toInt()


    }

    public fun getColour() : String
    {
        val converter : DataTranslation = DataTranslation()

        return converter.rgbToHexString(redAmount, greenAmount, blueAmount)
    }

    /*
    private fun inputBitmapImage(filePath : String)
    {
        val imageFile = File(filePath)

        val foundImage : Bitmap? = BitmapFactory.decodeStream(FileInputStream(imageFile), null, null)

        if (foundImage != null) {
            usedImage = foundImage.copy(Bitmap.Config.ARGB_8888, true)

            val imageCentreRect: Rect = Rect()

            imageCentreRect.x = ((usedImage.width / 2) - 60)
            imageCentreRect.y = ((usedImage.height / 2) - 60)

            imageCentreRect.width = 60
            imageCentreRect.height = 60

            //convert bitmap to mat for open cv
            val allImageMat = Mat()


            Utils.bitmapToMat(usedImage, allImageMat)

            val imageCentreRgba: Mat = allImageMat.submat(imageCentreRect)

            val imageCentreHsv: Mat = Mat()

            Imgproc.cvtColor(imageCentreRgba, imageCentreHsv, Imgproc.COLOR_RGB2HSV_FULL)

            //touchedRegionHsv = mDetector.mDilatedMask

            // Calculate average color of touched region
            blobColorHsv = Core.sumElems(imageCentreHsv)
            val pointCount = imageCentreRect.width * imageCentreRect.height
            for (i in 0 until blobColorHsv.`val`.size) {
                var value = blobColorHsv.`val`[i]
                value /= pointCount
                blobColorHsv.`val`.set(i, value)
            }

            blobColorRgba = convertScalarHsv2Rgba(blobColorHsv)

            redAmount = blobColorRgba.`val`[0].toInt()
            greenAmount = blobColorRgba.`val`[1].toInt()
            blueAmount = blobColorRgba.`val`[2].toInt()
        }

    }*/

    private fun edgeDetector(rgbaImage : Mat)
    {
        Imgproc.pyrDown(rgbaImage, mPyrDownMat)
        Imgproc.pyrDown(mPyrDownMat, mPyrDownMat)

        Imgproc.cvtColor(mPyrDownMat, mHsvMat, Imgproc.COLOR_RGB2HSV_FULL)

        Core.inRange(mHsvMat, mLowerBound, mUpperBound, mMask)

        Imgproc.dilate(mMask, mDilatedMask, Mat())

        val contours : ArrayList<MatOfPoint> = ArrayList()

        Imgproc.findContours(mDilatedMask, contours, mHierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)

        // Find max contour area
        var maxArea : Double = 0.0
        for (contour in contours)
        {
            val area : Double = Imgproc.contourArea(contour)
            if (area > maxArea) {
                maxArea = area
            }
        }

        // Filter contours by area and resize to fit the original image size
        mContours.clear()
        for (contour in contours)
        {
            if (Imgproc.contourArea(contour) > (mMinContourArea * maxArea)) {
                Core.multiply(contour, Scalar(4.0, 4.0), contour)
                mContours.add(contour)
            }
        }

        blobColorHsv = Core.sumElems(mDilatedMask)

        val pointCount = usedImage.width * usedImage.height
        for (i in 0 until blobColorHsv.`val`.size) {
            var value = blobColorHsv.`val`[i]
            value /= pointCount
            blobColorHsv.`val`.set(i, value)
        }

        blobColorRgba = convertScalarHsv2Rgba(blobColorHsv)

        redAmount = blobColorRgba.`val`[0].toInt()
        greenAmount = blobColorRgba.`val`[1].toInt()
        blueAmount = blobColorRgba.`val`[2].toInt()

    }

    private fun convertScalarHsv2Rgba(hsvColor: Scalar): Scalar {
        val pointMatRgba = Mat()
        val pointMatHsv = Mat(1, 1, CvType.CV_8UC3, hsvColor)
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4)

        return Scalar(pointMatRgba.get(0, 0))
    }
}
        