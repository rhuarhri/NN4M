package com.example.nn4wchallenge.slideShowCode

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.nn4wchallenge.R

class slideShowViewHolder(val row : View) : RecyclerView.ViewHolder(row)
{
    var clothingIV : ImageView

    init {
        this.clothingIV = row.findViewById(R.id.clothingIV)
    }

}