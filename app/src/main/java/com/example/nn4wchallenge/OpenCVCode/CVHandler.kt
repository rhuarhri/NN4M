package com.example.nn4wchallenge.OpenCVCode

/*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.File
import java.io.FileInputStream

class CVHandler {

    private lateinit var usedImage : Bitmap

     var redAmount = 0
     var greenAmount = 0
     var blueAmount = 0

    private var mBlobColorHsv: Scalar = Scalar(255.0)
    private lateinit var mBlobColorRgba: Scalar

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

    fun inputImage(filePath : String)
    {
        val imageFile = File(filePath)

        val foundImage : Bitmap? = BitmapFactory.decodeStream(FileInputStream(imageFile), null, null)

        if (foundImage != null) {
            usedImage = foundImage

            val origin: Mat = Imgcodecs.imread(filePath)

            val imgRGB: Mat = Mat()

            Imgproc.cvtColor(origin, imgRGB, Imgproc.COLOR_BGR2BGRA)

            edgeDetector(imgRGB)
        }
    }

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

        mBlobColorHsv = Core.sumElems(mDilatedMask)

        val pointCount = usedImage.width * usedImage.height
        for (i in 0 until mBlobColorHsv.`val`.size) {
            var value = mBlobColorHsv.`val`[i]
            value /= pointCount
            mBlobColorHsv.`val`.set(i, value)
        }

        mBlobColorRgba = convertScalarHsv2Rgba(mBlobColorHsv)

        redAmount = mBlobColorRgba.`val`[0].toInt()
        greenAmount = mBlobColorRgba.`val`[1].toInt()
        blueAmount = mBlobColorRgba.`val`[2].toInt()

    }

    private fun convertScalarHsv2Rgba(hsvColor: Scalar): Scalar {
        val pointMatRgba = Mat()
        val pointMatHsv = Mat(1, 1, CvType.CV_8UC3, hsvColor)
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4)

        return Scalar(pointMatRgba.get(0, 0))
    }
}
        */