package com.example.nn4wchallenge.slideShowCode

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nn4wchallenge.R
import com.example.nn4wchallenge.imageHandling.retrieveImageHandler

class slideShowAdapter(private val context : Context, private val images : Array<String>)
    : RecyclerView.Adapter<slideShowViewHolder>()
{

    val imageHandler : retrieveImageHandler = retrieveImageHandler(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): slideShowViewHolder {
        val myLayout = LayoutInflater.from(context)
        val foundView = myLayout.inflate(R.layout.slide_show_layout, parent, false)
        return slideShowViewHolder(foundView)
    }

    override fun onBindViewHolder(holder: slideShowViewHolder, position: Int) {

        imageHandler.recyclerViewImageHandler(holder.clothingIV, images[position], true)

    }

    override fun getItemCount(): Int {
        return images.size
    }
}