package com.example.nn4wchallenge

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.graphics.Palette
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        //var picture : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.test)

        //val p = Palette.from(picture).generate()

        //val vibrant = p.vibrantSwatch
// In Kotlin, check for null before accessing properties on the vibrant swatch.
        //val titleColor : Int = vibrant!!.titleTextColor

        //testTXT.setTextColor(titleColor)


    }
}
