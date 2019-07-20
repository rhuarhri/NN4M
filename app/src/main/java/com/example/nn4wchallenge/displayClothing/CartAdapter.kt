package com.example.nn4wchallenge.displayClothing

import android.content.Context
import android.content.Intent
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
import com.example.nn4wchallenge.database.internal.DatabaseCommands
import com.example.nn4wchallenge.database.internal.DatabaseManager
import com.example.nn4wchallenge.imageHandling.RetrieveImageHandler


class CartAdapter(private var context : Context, private var itemList : ArrayList<ClothingItem>)
    : RecyclerView.Adapter<CartAdapter.cartViewHolder>() {

    private val imageHandler: RetrieveImageHandler = RetrieveImageHandler(context)
    private val commands: DatabaseCommands = DatabaseCommands()


    class cartViewHolder(row : View) : ViewHolder(row)
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

        holder.itemNameTXT.text = itemList[position].title

        holder.itemMeasurementTXT.text = itemList[position].measurement

        holder.itemDeleteBTN.setOnClickListener {

            val input : Data = Data.Builder()
                .putString(commands.Cart_DB, commands.Cart_DB)
                .putString(commands.Cart_Delete, commands.Cart_Delete)
                .putInt(commands.Cart_ID, itemList[position].id)
                .build()

            val deleteDataWorker = OneTimeWorkRequestBuilder<DatabaseManager>().setInputData(input).build()


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