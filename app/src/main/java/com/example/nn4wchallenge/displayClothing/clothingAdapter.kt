package com.example.nn4wchallenge.displayClothing

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.nn4wchallenge.R
import com.example.nn4wchallenge.imageHandling.retrieveImageHandler
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/*
This class is the adapter for the cart screen and the user clothing screen (i.e. the screen that displays what clothes the
user owns) the reason that it is two in one is because these two screen display very similar data.

 */


class clothingAdapter (var context : Context, var itemList : ArrayList<clothingItem>, var isCartScreen : Boolean)
    : RecyclerView.Adapter<viewHolder>()
{
    var imageHandler : retrieveImageHandler = retrieveImageHandler(context)

    private class cartViewHolder(row : View) : viewHolder(row)
    {

        var buyBTN : Button

        init{
            this.buyBTN = row.findViewById(R.id.buyAllBTN)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        /*return if (isCartScreen)
        {
            cartCreate(parent)
        }
        else
        {*/
            return userClothingCreate(parent)
        //}
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        /*if (isCartScreen)
        {
            cartBind(holder as cartViewHolder, position)
        }
        else
        {*/
            userClothingBind(holder, position)
        //}
    }




    private fun cartCreate(parent: ViewGroup) : cartViewHolder
    {
        var myLayout = LayoutInflater.from(context)
        var foundView = myLayout.inflate(R.layout.cart_item_layout, parent, false)
        var itemViewHolder = cartViewHolder(foundView)

        return itemViewHolder
    }


    private fun cartBind(holder: cartViewHolder, position: Int)
    {
        val foundImage : Bitmap = imageHandler.getBitmapFromURL(
            itemList[position].imageLocation,
            holder.clothingIV.height,
            holder.clothingIV.width)

        holder.clothingIV.setImageBitmap(foundImage)

        holder.ItemNameTXT.text = itemList[position].title

        holder.ItemMeasurementTXT.text = itemList[position].measurement

        holder.ItemDeleteBTN.setOnClickListener {


        }

        holder.buyBTN.setOnClickListener {


        }
    }


    private fun userClothingCreate(parent: ViewGroup) : viewHolder
    {
        var myLayout = LayoutInflater.from(context)
        var foundView = myLayout.inflate(R.layout.clothing_item_layout, parent, false)
        var itemViewHolder = viewHolder(foundView)
        return itemViewHolder
    }

    private fun userClothingBind(holder: viewHolder, position: Int)
    {
        doAsync {
            val foundImage: Bitmap = imageHandler.getBitmapFtomFile(
                itemList[position].imageLocation,
                holder.clothingIV.height,
                holder.clothingIV.width
            )

            uiThread {
                holder.clothingIV.setImageBitmap(foundImage)

            }

            holder.ItemNameTXT.text = itemList[position].title

            holder.ItemMeasurementTXT.text = itemList[position].measurement

            holder.ItemDeleteBTN.setOnClickListener {

                Toast.makeText(context, "item called ${itemList[position].title} selected", Toast.LENGTH_LONG)
                    .show()
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
    }*/
}