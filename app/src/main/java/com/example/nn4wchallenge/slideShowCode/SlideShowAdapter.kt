package com.example.nn4wchallenge.slideShowCode

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.nn4wchallenge.R
import com.example.nn4wchallenge.imageHandling.RetrieveImageHandler

class SlideShowAdapter(private val context : Context, private val images : Array<String>, private var slideShowListener : SlideShowListener)
    : RecyclerView.Adapter<SlideShowViewHolder>()
{
    private val imageHandler : RetrieveImageHandler = RetrieveImageHandler(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideShowViewHolder {
        val myLayout = LayoutInflater.from(context)
        val foundView = myLayout.inflate(R.layout.slide_show_layout, parent, false)

        //val activity : Activity = context as Activity
        //val slideShowListener : SlideShowListener = activity as SlideShowListener
        return SlideShowViewHolder(foundView)
    }

    override fun onBindViewHolder(holder: SlideShowViewHolder, position: Int) {

        holder.clothingIV.maxWidth = holder.clothingIV.height
        imageHandler.recyclerViewImageHandler(holder.clothingIV, images[position], true)
        holder.clothingIV.setOnClickListener {
            slideShowListener.onItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        return images.size
    }
}