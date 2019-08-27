package com.example.nn4wchallenge.displayClothing

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.nn4wchallenge.R
import com.example.nn4wchallenge.database.internal.DatabaseCommands
import com.example.nn4wchallenge.database.internal.DatabaseManager
import com.example.nn4wchallenge.imageHandling.RetrieveImageHandler


class ClothingAdapter (
    private var context : Context, private var itemList : ArrayList<ClothingItem>, private val clothingListener : ClothingListListener
    )
    : RecyclerView.Adapter<ClothingAdapter.clothingViewHolder>()
{
    private val imageHandler : RetrieveImageHandler = RetrieveImageHandler(context)
    val commands : DatabaseCommands = DatabaseCommands()

    class clothingViewHolder(row : View) : ViewHolder(row)
    {
        var itemColourTXT : TextView

        init{
            this.itemColourTXT = row.findViewById(R.id.colourTXT)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): clothingViewHolder {
        val myLayout = LayoutInflater.from(context)
        val foundView = myLayout.inflate(R.layout.clothing_item_layout, parent, false)
        return clothingViewHolder(foundView)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: clothingViewHolder, position: Int) {

        imageHandler.recyclerViewImageHandler(holder.clothingIV, itemList[position].imageLocation, true)

        holder.clothingIV.setOnClickListener {
            val id = itemList[position].id
            val type = itemList[position].title
            val season = itemList[position].measurement
            val image = itemList[position].imageLocation
            val colour = itemList[position].itemColour

            clothingListener.onEditClothing(id, type, season, image, colour)
        }

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

            holder.itemColourTXT.setBackgroundColor(Color.parseColor("#${itemList[position].itemColour}"))

    }


    }
