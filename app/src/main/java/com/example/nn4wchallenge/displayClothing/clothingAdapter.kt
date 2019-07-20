package com.example.nn4wchallenge.displayClothing

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.nn4wchallenge.R
import com.example.nn4wchallenge.database.internal.databaseCommands
import com.example.nn4wchallenge.database.internal.databaseManager
import com.example.nn4wchallenge.imageHandling.retrieveImageHandler
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class clothingAdapter (var context : Context, var itemList : ArrayList<clothingItem>)
    : RecyclerView.Adapter<viewHolder>()
{
    val imageHandler : retrieveImageHandler = retrieveImageHandler(context)
    val commands : databaseCommands = databaseCommands()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val myLayout = LayoutInflater.from(context)
        val foundView = myLayout.inflate(R.layout.clothing_item_layout, parent, false)
        return viewHolder(foundView)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        imageHandler.recyclerViewImageHandler(holder.clothingIV, itemList[position].imageLocation, true)

        holder.ItemNameTXT.text = itemList[position].title

        holder.ItemMeasurementTXT.text = itemList[position].measurement

        holder.ItemDeleteBTN.setOnClickListener {

            val input : Data = Data.Builder()
                .putString(commands.Clothing_DB, commands.Clothing_DB)
                .putString(commands.Clothing_Delete, commands.Clothing_Delete)
                .putInt(commands.Clothing_ID, itemList[position].id)
                .build()

            val deleteDataWorker = OneTimeWorkRequestBuilder<databaseManager>().setInputData(input).build()


            WorkManager.getInstance().enqueue(deleteDataWorker)
        }

    }


    }
