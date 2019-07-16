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

/*
    public class itemViewHolder(row: View) : RecyclerView.ViewHolder(row)
    {
        var clothingIV : ImageView
        var ItemNameTXT : TextView
        var ItemMeasurementTXT : TextView
        var ItemDeleteBTN : Button

        init {
            this.clothingIV = row.findViewById(R.id.clothingIV) as ImageView
            this.ItemNameTXT = row.findViewById(R.id.TitleTXT) as TextView
            this.ItemMeasurementTXT = row.findViewById(R.id.measurementTXT) as TextView
            this.ItemDeleteBTN = row.findViewById(R.id.deleteBTN) as Button
        }
    }*/

/*
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        return if (isCartScreen)
        {
            setupCartList(position, convertView, parent)
        }
        else
        {
            setupUserClothingList(position, convertView, parent)
        }

    }

    private fun setupUserClothingList(position: Int, convertView: View?, parent: ViewGroup?) : View
    {
        var returnedView : View
        var itemViewHolder : viewHolder

        if (convertView == null)
        {
            var myLayout = LayoutInflater.from(context)
            returnedView = myLayout.inflate(R.layout.clothing_item_layout, parent, false)
            itemViewHolder = viewHolder(returnedView)
            returnedView.tag = itemViewHolder
        }
        else
        {
            returnedView = convertView
            itemViewHolder = returnedView.tag as viewHolder
        }

        val foundImage : Bitmap = imageHandler.getBitmapFtomFile(
            itemList[position].imageLocation,
            itemViewHolder.clothingIV.height,
            itemViewHolder.clothingIV.width)

        itemViewHolder.clothingIV.setImageBitmap(foundImage)

        itemViewHolder.ItemNameTXT.text = itemList[position].title

        itemViewHolder.ItemMeasurementTXT.text = itemList[position].measurement

        itemViewHolder.ItemDeleteBTN.setOnClickListener {

            Toast.makeText(context, "item called ${itemList[position].title} selected", Toast.LENGTH_LONG).show()
        }

        return returnedView
    }

    private fun setupCartList(position: Int, convertView: View?, parent: ViewGroup?) : View
    {
        var returnedView : View
        var itemViewHolder : cartViewHolder

        if (convertView == null)
        {
            var myLayout = LayoutInflater.from(context)
            returnedView = myLayout.inflate(R.layout.cart_item_layout, parent, false)
            itemViewHolder = cartViewHolder(returnedView)
            returnedView.tag = itemViewHolder
        }
        else
        {
            returnedView = convertView
            itemViewHolder = returnedView.tag as cartViewHolder
        }

        val foundImage : Bitmap = imageHandler.getBitmapFromURL(
            itemList[position].imageLocation,
            itemViewHolder.clothingIV.height,
            itemViewHolder.clothingIV.width)

        itemViewHolder.clothingIV.setImageBitmap(foundImage)

        itemViewHolder.ItemNameTXT.text = itemList[position].title

        itemViewHolder.ItemMeasurementTXT.text = itemList[position].measurement

        itemViewHolder.ItemDeleteBTN.setOnClickListener {


        }

        itemViewHolder.buyBTN.setOnClickListener {


        }

        return returnedView
    }

    override fun getItem(position: Int): Any {
        return itemList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return itemList.size
    }
}*/