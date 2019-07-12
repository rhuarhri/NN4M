package com.example.nn4wchallenge.displayClothing

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.nn4wchallenge.R
import com.example.nn4wchallenge.imageHandling.retrieveImageHandler
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread



class cartAdapter(var context : Context, var itemList : ArrayList<clothingItem>)
    : RecyclerView.Adapter<cartAdapter.cartViewHolder>() {

    private val imageHandler: retrieveImageHandler = retrieveImageHandler(context)


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

            Toast.makeText(context, "url is ${itemList[position].imageLocation}", Toast.LENGTH_LONG).show()

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