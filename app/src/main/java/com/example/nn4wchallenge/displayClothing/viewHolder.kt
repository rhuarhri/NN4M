package com.example.nn4wchallenge.displayClothing

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nn4wchallenge.R

open class viewHolder(row : View) : RecyclerView.ViewHolder(row){

    var clothingIV : ImageView
    var ItemNameTXT : TextView
    var ItemMeasurementTXT : TextView
    var ItemDeleteBTN : Button

    init
    {
        this.clothingIV = row.findViewById(R.id.clothingIV)
        this.ItemNameTXT = row.findViewById(R.id.TitleTXT)
        this.ItemMeasurementTXT = row.findViewById(R.id.measurementTXT)
        this.ItemDeleteBTN = row.findViewById(R.id.deleteBTN)
    }

}