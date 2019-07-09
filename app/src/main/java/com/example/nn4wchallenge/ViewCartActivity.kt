package com.example.nn4wchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.nn4wchallenge.database.internal.GetCartThreadManager
import com.example.nn4wchallenge.displayClothing.clothingAdapter
import com.example.nn4wchallenge.displayClothing.clothingItem


class ViewCartActivity : AppCompatActivity() {

    private lateinit var itemsRV : RecyclerView
    private lateinit var buyAllBTN : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_cart)

        buyAllBTN = findViewById(R.id.buyAllBTN)
        buyAllBTN.setOnClickListener {


        }

        val getDataWorker = OneTimeWorkRequestBuilder<GetCartThreadManager>().build()

        WorkManager.getInstance().enqueue(getDataWorker)

        WorkManager.getInstance().getWorkInfoByIdLiveData(getDataWorker.id).observe(this, Observer {
                workInfo ->

            if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                val id : Array<String>? = workInfo.outputData.getStringArray("id")
                val names: Array<String>? = workInfo.outputData.getStringArray("name")
                val price: Array<String>? = workInfo.outputData.getStringArray("price")
                val images: Array<String>? = workInfo.outputData.getStringArray("image")

                val itemList: ArrayList<clothingItem> = createItemListForAdapter(id, names, price, images)

                var RVAdapter: RecyclerView.Adapter<*> = clothingAdapter(applicationContext, itemList, true)

                itemsRV = findViewById<RecyclerView>(R.id.itemsRV).apply {
                    setHasFixedSize(false)
                    layoutManager = LinearLayoutManager(applicationContext)

                    adapter = RVAdapter
                }
            }

            })
    }


    private fun createItemListForAdapter(id : Array<String>?, type : Array<String>?, season : Array<String>?, image : Array<String>?)
            : ArrayList<clothingItem>
    {
        val itemList : ArrayList<clothingItem> = ArrayList()

        if (id != null && type != null && season != null && image != null) {
            for (i in 0..id.size) {

                itemList.add(clothingItem(id[i].toInt(), type[i], season[i], image[i]))

            }
        }

        return itemList
    }
}
