package com.example.nn4wchallenge.displayClothing

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.nn4wchallenge.R
import com.example.nn4wchallenge.database.internal.DatabaseCommands
import com.example.nn4wchallenge.database.internal.DatabaseManager
import com.example.nn4wchallenge.imageHandling.RetrieveImageHandler


class ClothingAdapter (private var context : Context, private var itemList : ArrayList<ClothingItem>)
    : RecyclerView.Adapter<ViewHolder>()
{
    private val imageHandler : RetrieveImageHandler = RetrieveImageHandler(context)
    val commands : DatabaseCommands = DatabaseCommands()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val myLayout = LayoutInflater.from(context)
        val foundView = myLayout.inflate(R.layout.clothing_item_layout, parent, false)
        return ViewHolder(foundView)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        imageHandler.recyclerViewImageHandler(holder.clothingIV, itemList[position].imageLocation, true)

        holder.itemNameTXT.text = itemList[position].title

        holder.itemMeasurementTXT.text = itemList[position].measurement

        holder.itemDeleteBTN.setOnClickListener {

            val input : Data = Data.Builder()
                .putString(commands.Clothing_DB, commands.Clothing_DB)
                .putString(commands.Clothing_Delete, commands.Clothing_Delete)
                .putInt(commands.Clothing_ID, itemList[position].id)
                .build()

            val deleteDataWorker = OneTimeWorkRequestBuilder<DatabaseManager>().setInputData(input).build()


            WorkManager.getInstance().enqueue(deleteDataWorker)
        }

    }


    }
