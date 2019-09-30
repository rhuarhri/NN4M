package com.example.nn4wchallenge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.nn4wchallenge.database.internal.DatabaseCommands
import com.example.nn4wchallenge.database.internal.DatabaseManager
import com.example.nn4wchallenge.displayClothing.CartAdapter
import com.example.nn4wchallenge.displayClothing.ClothingItem


class ViewCartActivity : AppCompatActivity() {

    private val commands : DatabaseCommands = DatabaseCommands()

    private var total : Double = 0.00

    private lateinit var itemsRV : RecyclerView
    private lateinit var buyAllBTN : Button
    private lateinit var totalTXT : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_cart)

        buyAllBTN = findViewById(R.id.buyAllBTN)
        buyAllBTN.setOnClickListener {

            goToCheckOut()

        }

        setupRecyclerView()

        totalTXT = findViewById(R.id.totalTXT)
        setupTotal()


    }

    private fun setupRecyclerView()
    {
        val input : Data = Data.Builder()
            .putString(commands.Cart_DB, commands.Cart_DB)
            .putString(commands.Cart_Get, commands.Cart_Get)
            .build()

        val getDataWorker = OneTimeWorkRequestBuilder<DatabaseManager>().setInputData(input).build()

        WorkManager.getInstance().enqueue(getDataWorker)

        WorkManager.getInstance().getWorkInfoByIdLiveData(getDataWorker.id).observe(this as LifecycleOwner, Observer {
                workInfo ->

            if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                val id : IntArray? = workInfo.outputData.getIntArray(commands.Cart_ID)
                val names: Array<String>? = workInfo.outputData.getStringArray(commands.Cart_name)
                val price: DoubleArray? = workInfo.outputData.getDoubleArray(commands.Cart_price)
                val images: Array<String>? = workInfo.outputData.getStringArray(commands.Cart_picture)

                val itemList: ArrayList<ClothingItem> = createItemListForAdapter(id, names, price, images)

                val rvAdapter: RecyclerView.Adapter<*> = CartAdapter(applicationContext, itemList)

                itemsRV = findViewById<RecyclerView>(R.id.itemsRV).apply {
                    setHasFixedSize(false)
                    layoutManager = LinearLayoutManager(applicationContext)

                    adapter = rvAdapter
                }

            }

        })
    }

    private fun setupTotal()
    {
        val input : Data = Data.Builder()
            .putString(commands.Cart_DB, commands.Cart_DB)
            .putString(commands.Cart_Get_Prices, commands.Cart_Get_Prices)
            .build()

        val getTotalWorker = OneTimeWorkRequestBuilder<DatabaseManager>().setInputData(input).build()


        WorkManager.getInstance().enqueue(getTotalWorker)

        WorkManager.getInstance().getWorkInfoByIdLiveData(getTotalWorker.id).observe(this, Observer {
            workInfo ->

            total = workInfo.outputData.getDouble(commands.Cart_total_price, 0.0)

            totalTXT.text = "total $total"

        })
    }

    private fun createItemListForAdapter(ids : IntArray?, name : Array<String>?, price : DoubleArray?, image : Array<String>?)
            : ArrayList<ClothingItem>
    {


        val itemList : ArrayList<ClothingItem> = ArrayList()

        if (ids != null && name != null && price != null && image != null) {
            for ((i, id) in ids.withIndex()) {

                itemList.add(ClothingItem(ids[i], name[i], price[i].toString(), image[i], ""))

            }

        }

        totalTXT.text =

        return itemList
    }

    private fun goToCheckOut()
    {
        val goTo = Intent(this, PurchaseActivity::class.java).putExtra("function", "buyAll")

        startActivity(goTo)
    }
}
