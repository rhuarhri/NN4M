package com.example.nn4wchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.nn4wchallenge.AddActivitySpinners.*
import com.example.nn4wchallenge.database.quickSearchHandler
import com.example.nn4wchallenge.slideShowCode.slideShowAdapter

class QuickSearchActivity : AppCompatActivity() {

    /*
    The main reason that this screen exists is because it is something the app has the
    functionality to do and could be useful to the user.

    What is it for
    This is a way for the user to check if an item of clothing matches the clothes they already have.
    Take this scenario
    The user walks into a shop they find a nice skirt. They decide to use this app to
    check if the skirt matches any of the clothes they already own.
    They open the app, go to this screen and input data about the skirt.
    The app produces some matches which the user can look through.
    The user decides to buy the skirt based on what the app has told them.
     */

    private lateinit var searchBTN : Button
    private lateinit var pictureRV : RecyclerView

    private var type : String = ""
    private var season : String = ""
    private var redAmount : Int = 0
    private var greenAmount : Int = 0
    private var blueAmount : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quick_search)

        val colourList : ArrayList<addColourItem> = ArrayList()
        colourList.add(addColourItem("black", R.color.colorBlack, 0, 0, 0))
        colourList.add(addColourItem("blue", R.color.colorBlue, 0, 0, 255))
        colourList.add(addColourItem("light blue", R.color.colorLightBlue, 0, 255, 255))
        colourList.add(addColourItem("green", R.color.colorGreen, 0, 255, 0))
        colourList.add(addColourItem("yellow", R.color.colorYellow, 255, 255, 0))
        colourList.add(addColourItem("red", R.color.colorRed, 255, 0, 0))
        colourList.add(addColourItem("pink", R.color.colorPink, 255, 0, 255))
        colourList.add(addColourItem("white", R.color.colorWhite, 255, 255, 255))


        val typeList : ArrayList<addActivityItem> = ArrayList()
        typeList.add(addActivityItem("dress", R.drawable.dress_icon))
        typeList.add(addActivityItem("jacket", R.drawable.jacket_icon))
        typeList.add(addActivityItem("jumper", R.drawable.jumper_icon))
        typeList.add(addActivityItem("shirt", R.drawable.shirt_icon))
        typeList.add(addActivityItem("shorts", R.drawable.shorts_icon))
        typeList.add(addActivityItem("skirt", R.drawable.skirt_icon))
        typeList.add(addActivityItem("top", R.drawable.top_icon))
        typeList.add(addActivityItem("trousers", R.drawable.trousers_icon))


        val seasonList : ArrayList<addActivityItem> = ArrayList()
        seasonList.add(addActivityItem("summer", R.drawable.summer_icon))
        seasonList.add(addActivityItem("winter", R.drawable.winter_icon))
        seasonList.add(addActivityItem("party", R.drawable.party_icon))
        seasonList.add(addActivityItem("formal", R.drawable.formal_icon))

        setUpColourSpinner(colourList)
        setUpTypeSpinner(typeList)
        setUpSeasonSpinner(seasonList)


        pictureRV = findViewById(R.id.pictureRV)

        searchBTN = findViewById(R.id.searchBTN)
        searchBTN.setOnClickListener {
            search()
        }

    }

    private fun setUpColourSpinner(itemList : ArrayList<addColourItem>)
    {
        val colourSPN : Spinner = findViewById(R.id.colourSPN)

        //item basic list is a list of a parent class converted from list of child class
        val itemBasicList : ArrayList<addActivityItem> = itemList as ArrayList<addActivityItem>

        val colourAdapter = addSpinnerAdapter(applicationContext, itemBasicList)

        colourSPN.adapter = colourAdapter

        colourSPN.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                val colourItem : addColourItem = itemList.get(position)
                redAmount = colourItem.amountOfRed
                greenAmount = colourItem.amountOfGreen
                blueAmount = colourItem.amountOfBlue

            }

        }

    }

    private fun setUpTypeSpinner(itemList : ArrayList<addActivityItem>)
    {
        val typeSPN : Spinner = findViewById(R.id.typeSPN)

        val typeAdapter = addSpinnerAdapter(applicationContext, itemList)

        typeSPN.adapter = typeAdapter

        typeSPN.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                val typeItem : addActivityItem = itemList.get(position)
                type = typeItem.itemTitle

            }

        }


    }

    private fun setUpSeasonSpinner(itemList : ArrayList<addActivityItem>)
    {
        val seasonSPN : Spinner = findViewById(R.id.seasonSPN)

        val seasonAdapter = addSpinnerAdapter(applicationContext, itemList)

        seasonSPN.adapter = seasonAdapter

        seasonSPN.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                val seasonItem : addActivityItem = itemList.get(position)
                season = seasonItem.itemTitle

            }

        }

    }

    private fun search()
    {
        val input : Data = Data.Builder()
            .putString("type", type)
            .putString("season", season)
            .putInt("red", redAmount)
            .putInt("green", greenAmount)
            .putInt("blue", blueAmount)
            .build()

        val quickSearchWorker = OneTimeWorkRequestBuilder<quickSearchHandler>().setInputData(input).build()

        WorkManager.getInstance().enqueue(quickSearchWorker)

        WorkManager.getInstance().getWorkInfoByIdLiveData(quickSearchWorker.id).observe(this, Observer {

            workInfo ->

            if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED)
            {
                val images : Array<out String>? = workInfo.outputData.getStringArray("userClothing")

                if (images != null)
                {
                    Toast.makeText(applicationContext, "image size is ${images.size}", Toast.LENGTH_LONG).show()
                    setupRecyclerView(images as Array<String>)
                }
                else
                {
                    Toast.makeText(applicationContext, "image is null", Toast.LENGTH_LONG).show()
                }
            }
            else if (workInfo != null && workInfo.state == WorkInfo.State.FAILED)
            {

                    Toast.makeText(applicationContext, "thread failed", Toast.LENGTH_LONG).show()

            }


        })
    }

    private fun setupRecyclerView(images : Array<String>)
    {

        val RVAdapter: RecyclerView.Adapter<*> = slideShowAdapter(applicationContext, images)

        pictureRV.apply {

            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

            adapter = RVAdapter
        }


    }
}