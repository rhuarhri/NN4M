package com.example.nn4wchallenge.imageHandling

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.nn4wchallenge.R
import java.io.InputStream
import java.net.HttpURLConnection

/*
This could be easily done with glide an image processing library but it
was done like this to show that I know how it can be done
 */

class retrieveImageHandler(private val context : Context) {

    private fun imageOptions(height : Int, width : Int) : BitmapFactory.Options
    {

        var options = BitmapFactory.Options().apply {

            inJustDecodeBounds = true

            //BitmapFactory.decodeFile(currentPhotoPath, this)
            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.min(photoW / width, photoH / height)

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor

        }


        return options
    }


    public fun getBitmapFtomFile(filePath : String?, height : Int, width : Int) : Bitmap
    {
        return if (filePath == "" || filePath == null)
        {
            setDefault(height, width)
        }else{
            BitmapFactory.decodeFile(filePath, imageOptions(height, width))
        }


    }

    public fun getBitmapFromURL(imageURL : String?, height : Int, width : Int) : Bitmap
    {
        return if (imageURL == "" || imageURL == null) {
            setDefault(height, width)
        } else {

            val url = java.net.URL(imageURL)
            var connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.connect()

            val inStream: InputStream = connection.inputStream

            BitmapFactory.decodeStream(inStream, null, imageOptions(height, width))
        }
    }

    //returns default image if file or input stream is empty
    public fun setDefault(height : Int, width : Int) : Bitmap
    {
        return BitmapFactory.decodeResource(context.resources, R.drawable.no_image_icon, imageOptions(height, width))
    }

}