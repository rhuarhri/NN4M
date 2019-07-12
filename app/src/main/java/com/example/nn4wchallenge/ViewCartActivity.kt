package com.example.nn4wchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.nn4wchallenge.database.internal.databaseChecker
import com.example.nn4wchallenge.database.internal.databaseCommands
import com.example.nn4wchallenge.database.internal.databaseManager
import com.example.nn4wchallenge.displayClothing.cartAdapter
import com.example.nn4wchallenge.displayClothing.clothingAdapter
import com.example.nn4wchallenge.displayClothing.clothingItem


class ViewCartActivity : AppCompatActivity() {

    private val commands : databaseCommands = databaseCommands()

    private lateinit var itemsRV : RecyclerView
    private lateinit var buyAllBTN : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_cart)

        buyAllBTN = findViewById(R.id.buyAllBTN)
        buyAllBTN.setOnClickListener {


            val input: Data = Data.Builder().putString("database", "cart").build()

            val checkUserDatabaseWorker = OneTimeWorkRequestBuilder<databaseChecker>()
                .setInputData(input)
                .build()

            WorkManager.getInstance().enqueue(checkUserDatabaseWorker)
            WorkManager.getInstance().getWorkInfoByIdLiveData(checkUserDatabaseWorker.id)
                .observe(this, Observer { workInfo ->
                    if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED)
                    {
                        var output : Boolean = workInfo.outputData.getBoolean("empty", true)

                        if (output)
                        {
                            //setup required
                           Toast.makeText(applicationContext, "cart data base empty", Toast.LENGTH_LONG).show()
                        }
                        else
                        {
                            Toast.makeText(applicationContext, "cart data base full", Toast.LENGTH_LONG).show()
                        }
                    }
                    //thread that check if data base is empty currently running or waiting to run
                    else if (workInfo != null && (workInfo.state == WorkInfo.State.RUNNING || workInfo.state == WorkInfo.State.ENQUEUED))
                    {
                        //do nothing this prevents the user doing anything without information from the thread
                    }
                })
        }


        val input : Data = Data.Builder()
            .putString(commands.Cart_DB, commands.Cart_DB)
            .putString(commands.Cart_Get, commands.Cart_Get)
            .build()

        val getDataWorker = OneTimeWorkRequestBuilder<databaseManager>().setInputData(input).build()


        WorkManager.getInstance().enqueue(getDataWorker)

        WorkManager.getInstance().getWorkInfoByIdLiveData(getDataWorker.id).observe(this, Observer {
                workInfo ->

            if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                val id : IntArray? = workInfo.outputData.getIntArray(commands.Cart_ID)
                val names: Array<String>? = workInfo.outputData.getStringArray(commands.Cart_name)
                val price: DoubleArray? = workInfo.outputData.getDoubleArray(commands.Cart_price)
                val images: Array<String>? = workInfo.outputData.getStringArray(commands.Cart_picture)

                val itemList: ArrayList<clothingItem> = createItemListForAdapter(id, names, price, images)

                var RVAdapter: RecyclerView.Adapter<*> = cartAdapter(applicationContext, itemList)

                itemsRV = findViewById<RecyclerView>(R.id.itemsRV).apply {
                    setHasFixedSize(false)
                    layoutManager = LinearLayoutManager(applicationContext)

                    adapter = RVAdapter
                }
            }

            })
    }


    private fun createItemListForAdapter(ids : IntArray?, name : Array<String>?, price : DoubleArray?, image : Array<String>?)
            : ArrayList<clothingItem>
    {
        val itemList : ArrayList<clothingItem> = ArrayList()

        if (ids != null && name != null && price != null && image != null) {
            for ((i, id) in ids!!.withIndex()) {

                itemList.add(clothingItem(ids[i], name[i], price[i].toString(), image[i]))

            }

        }

        return itemList
    }
}
