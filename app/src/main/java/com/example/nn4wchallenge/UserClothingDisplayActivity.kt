package com.example.nn4wchallenge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.nn4wchallenge.database.internal.GetClothingThreadManager
import com.example.nn4wchallenge.displayClothing.clothingAdapter
import com.example.nn4wchallenge.displayClothing.clothingItem
import kotlinx.android.synthetic.*

class UserClothingDisplayActivity : AppCompatActivity() {

    private lateinit var settingsBTN : Button
    private lateinit var accountBTN : Button
    private lateinit var addBTN : Button

    private lateinit var clothingRV : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_clothing_display)

        settingsBTN = findViewById(R.id.settingsBTN)
        settingsBTN.setOnClickListener {

            Toast.makeText(applicationContext, "Settings not available", Toast.LENGTH_SHORT).show()
        }

        accountBTN = findViewById(R.id.accountBTN)
        accountBTN.setOnClickListener {

            val goToAccountScreen : Intent = Intent(applicationContext, SetupActivity::class.java)
            startActivity(goToAccountScreen)

        }

        addBTN = findViewById(R.id.addBTN)
        addBTN.setOnClickListener {

            val goToAddScreen : Intent = Intent(applicationContext, AddActivity::class.java)
            startActivity(goToAddScreen)
        }


        val getDataWorker = OneTimeWorkRequestBuilder<GetClothingThreadManager>().build()

        WorkManager.getInstance().enqueue(getDataWorker)

        WorkManager.getInstance().getWorkInfoByIdLiveData(getDataWorker.id).observe(this, Observer {
            workInfo ->

            if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED)
            {

                val ids : IntArray? = workInfo.outputData.getIntArray("id")
                val types : Array<String>? = workInfo.outputData.getStringArray("type")
                val seasons : Array<String>? = workInfo.outputData.getStringArray("season")
                val images : Array<String>? = workInfo.outputData.getStringArray("image")





                if (images == null)
                {
                    Toast.makeText(applicationContext, "no images", Toast.LENGTH_LONG).show()
                }
                else if (types == null)
                {
                    Toast.makeText(applicationContext, "no types", Toast.LENGTH_LONG).show()
                }
                else if (seasons == null)
                {
                    Toast.makeText(applicationContext, "no seasons", Toast.LENGTH_LONG).show()
                }
                else if (ids == null)
                {
                    Toast.makeText(applicationContext, "no ids", Toast.LENGTH_LONG).show()
                }
                else
                {

                    try {
                        var itemList: ArrayList<clothingItem> = createItemListForAdapter(ids, types, seasons, images)
                        Toast.makeText(
                            applicationContext,
                            "size is ${itemList.size} id size is ${ids.size}",
                            Toast.LENGTH_LONG
                        ).show()

                        var RVAdapter : RecyclerView.Adapter<*> = clothingAdapter(applicationContext, itemList, false)

                        clothingRV = findViewById<RecyclerView>(R.id.clothingRV).apply{
                            setHasFixedSize(false)
                            layoutManager = LinearLayoutManager(applicationContext)

                            adapter = RVAdapter
                        }
                    }
                    catch (e : Exception)
                    {
                        Toast.makeText(applicationContext, "error is ${e.toString()}", Toast.LENGTH_LONG).show()
                    }
                    /*
                    try {
                        Toast.makeText(
                            applicationContext,
                            "type: ${itemList[0].title} seasons: ${itemList[0].measurement} id: ${itemList[0].id}",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                    catch(e : Exception)
                    {
                        Toast.makeText(applicationContext, "display error: ${e.toString()}", Toast.LENGTH_LONG).show()
                    }*/
                }

                /*
                var RVAdapter : RecyclerView.Adapter<*> = clothingAdapter(applicationContext, itemList, false)

                clothingRV = findViewById<RecyclerView>(R.id.clothingRV).apply{
                    setHasFixedSize(false)
                    layoutManager = LinearLayoutManager(applicationContext)

                    adapter = RVAdapter
                }*/

            }

        })


    }

    private fun createItemListForAdapter(ids : IntArray?, type : Array<String>?, season : Array<String>?, image : Array<String>?)
    : ArrayList<clothingItem>
    {
        val itemList : ArrayList<clothingItem> = ArrayList()

        if (ids != null && type != null && season != null && image != null) {
            for ((i, id) in ids!!.withIndex()) {

                    itemList.add(clothingItem(id, type[i], season[i], image[i]))

            }
        }

        return itemList
    }
}
