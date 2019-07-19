package com.example.nn4wchallenge

import android.content.Intent
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
import com.example.nn4wchallenge.database.internal.databaseCommands
import com.example.nn4wchallenge.database.internal.databaseManager
import com.example.nn4wchallenge.displayClothing.clothingAdapter
import com.example.nn4wchallenge.displayClothing.clothingItem

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

            val goToAccountScreen = Intent(applicationContext, SetupActivity::class.java)
                .putExtra("function", "update")
            startActivity(goToAccountScreen)

        }

        addBTN = findViewById(R.id.addBTN)
        addBTN.setOnClickListener {

            val goToAddScreen = Intent(applicationContext, AddActivity::class.java)
            startActivity(goToAddScreen)
        }


        val commands = databaseCommands()
        val input : Data = Data.Builder()
            .putString(commands.Clothing_DB, commands.Clothing_DB)
            .putString(commands.Clothing_Get, commands.Clothing_Get)
            .build()

        val getDataWorker = OneTimeWorkRequestBuilder<databaseManager>().setInputData(input).build()

        WorkManager.getInstance().enqueue(getDataWorker)

        WorkManager.getInstance().getWorkInfoByIdLiveData(getDataWorker.id).observe(this, Observer {
            workInfo ->

            if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED)
            {

                val ids : IntArray? = workInfo.outputData.getIntArray(commands.Clothing_ID)
                val types : Array<String>? = workInfo.outputData.getStringArray(commands.Clothing_type)
                val seasons : Array<String>? = workInfo.outputData.getStringArray(commands.Clothing_season)
                val images : Array<String>? = workInfo.outputData.getStringArray(commands.Clothing_picture)


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
                        val itemList: ArrayList<clothingItem> = createItemListForAdapter(ids, types, seasons, images)

                        val RVAdapter : RecyclerView.Adapter<*> = clothingAdapter(applicationContext, itemList)

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

                }


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
