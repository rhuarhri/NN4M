package com.example.nn4wchallenge.displayClothing

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
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



class cartAdapter(var context : Context, var itemList : ArrayList<clothingItem>)
    : RecyclerView.Adapter<cartAdapter.cartViewHolder>() {

    private val imageHandler: retrieveImageHandler = retrieveImageHandler(context)
    private val commands: databaseCommands = databaseCommands()


    public class cartViewHolder(row : View) : viewHolder(row)
    {

        var buyBTN : Button

        init{
            this.buyBTN = row.findViewById(R.id.buyAllBTN)
        }

        public fun loadImage(location : String, context : Context) {
            doAsync {
                val imageHandler: retrieveImageHandler = retrieveImageHandler(context)
                val foundImage: Bitmap = imageHandler.getBitmapFromURL(
                    location,
                    this@cartViewHolder.clothingIV.height,
                    this@cartViewHolder.clothingIV.width
                )
                this@cartViewHolder.clothingIV.setImageBitmap(foundImage)
                uiThread {

                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartViewHolder {
        val myLayout = LayoutInflater.from(context)
        val foundView = myLayout.inflate(R.layout.cart_item_layout, parent, false)
        return cartViewHolder(foundView)
    }

    override fun onBindViewHolder(holder: cartViewHolder, position: Int) {

        imageHandler.recyclerViewImageHandler(holder.clothingIV, itemList[position].imageLocation, false)

        holder.ItemNameTXT.text = itemList[position].title

        holder.ItemMeasurementTXT.text = itemList[position].measurement

        holder.ItemDeleteBTN.setOnClickListener {

            val input : Data = Data.Builder()
                .putString(commands.Cart_DB, commands.Cart_DB)
                .putString(commands.Cart_Delete, commands.Cart_Delete)
                .putInt(commands.Cart_ID, itemList[position].id)
                .build()

            val deleteDataWorker = OneTimeWorkRequestBuilder<databaseManager>().setInputData(input).build()


            WorkManager.getInstance().enqueue(deleteDataWorker)

        }

        holder.buyBTN.setOnClickListener {


        }
    }



    override fun getItemCount(): Int {
        return itemList.size
    }

    /*
    private class imageLoader(context : Context, val clothingIV : ImageView)
    {
        val imageHandler : retrieveImageHandler = retrieveImageHandler(context)

        public fun loadImage(location : String, context : Context)
        {
        doAsync {
            val imageHandler : retrieveImageHandler = retrieveImageHandler(context)
            val foundImage: Bitmap = imageHandler.getBitmapFromURL(
                location,
                clothingIV.height,
                clothingIV.width
            )
            uiThread {
                clothingIV.setImageBitmap(foundImage)
            }
        }
    }
    }*/
}