package com.example.nn4wchallenge.displayClothing

import android.content.Context
import android.content.Intent
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
import com.example.nn4wchallenge.PurchaseActivity
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


    class cartViewHolder(row : View) : viewHolder(row)
    {

        var buyBTN : Button

        init{
            this.buyBTN = row.findViewById(R.id.buyAllBTN)
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

            goToCheckOut(holder.row, itemList[position].id)
        }
    }

    private fun goToCheckOut(v : View, id : Int)
    {
        try {
            val goTo: Intent = Intent(v.context, PurchaseActivity::class.java)
                .putExtra("function", "singleAll")
                .putExtra("id", id)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            v.context.startActivity(goTo)
        }
        catch(e : Exception)
        {
            Toast.makeText(context,"intent error is ${e.toString()}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}