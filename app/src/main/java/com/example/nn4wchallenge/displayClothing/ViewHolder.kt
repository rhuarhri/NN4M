package com.example.nn4wchallenge.displayClothing

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nn4wchallenge.R

open class ViewHolder(val row : View) : RecyclerView.ViewHolder(row){


    var clothingIV : ImageView
    var itemNameTXT : TextView
    var itemMeasurementTXT : TextView
    var itemDeleteBTN : Button

    init
    {
        this.clothingIV = row.findViewById(R.id.clothingIV)
        this.itemNameTXT = row.findViewById(R.id.TitleTXT)
        this.itemMeasurementTXT = row.findViewById(R.id.measurementTXT)
        this.itemDeleteBTN = row.findViewById(R.id.deleteBTN)
    }

}