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
import com.example.nn4wchallenge.database.QuickSearchHandler
import com.example.nn4wchallenge.slideShowCode.SlideShowAdapter

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

        val colourList : ArrayList<AddColourItem> = ArrayList()
        colourList.add(AddColourItem("black", R.color.colorBlack, 0, 0, 0))
        colourList.add(AddColourItem("blue", R.color.colorBlue, 0, 0, 255))
        colourList.add(AddColourItem("light blue", R.color.colorLightBlue, 0, 255, 255))
        colourList.add(AddColourItem("green", R.color.colorGreen, 0, 255, 0))
        colourList.add(AddColourItem("yellow", R.color.colorYellow, 255, 255, 0))
        colourList.add(AddColourItem("red", R.color.colorRed, 255, 0, 0))
        colourList.add(AddColourItem("pink", R.color.colorPink, 255, 0, 255))
        colourList.add(AddColourItem("white", R.color.colorWhite, 255, 255, 255))


        val typeList : ArrayList<AddActivityItem> = ArrayList()
        typeList.add(AddActivityItem("dress", R.drawable.dress_icon))
        typeList.add(AddActivityItem("jacket", R.drawable.jacket_icon))
        typeList.add(AddActivityItem("jumper", R.drawable.jumper_icon))
        typeList.add(AddActivityItem("shirt", R.drawable.shirt_icon))
        typeList.add(AddActivityItem("shorts", R.drawable.shorts_icon))
        typeList.add(AddActivityItem("skirt", R.drawable.skirt_icon))
        typeList.add(AddActivityItem("top", R.drawable.top_icon))
        typeList.add(AddActivityItem("trousers", R.drawable.trousers_icon))


        val seasonList : ArrayList<AddActivityItem> = ArrayList()
        seasonList.add(AddActivityItem("summer", R.drawable.summer_icon))
        seasonList.add(AddActivityItem("winter", R.drawable.winter_icon))
        seasonList.add(AddActivityItem("party", R.drawable.party_icon))
        seasonList.add(AddActivityItem("formal", R.drawable.formal_icon))

        setUpColourSpinner(colourList)
        setUpTypeSpinner(typeList)
        setUpSeasonSpinner(seasonList)


        pictureRV = findViewById(R.id.pictureRV)

        searchBTN = findViewById(R.id.searchBTN)
        searchBTN.setOnClickListener {
            search()
        }

    }

    private fun setUpColourSpinner(itemList : ArrayList<AddColourItem>)
    {
        val colourSPN : Spinner = findViewById(R.id.colourSPN)

        //item basic list is a list of a parent class converted from list of child class
        val itemBasicList : ArrayList<AddActivityItem> = itemList as ArrayList<AddActivityItem>

        val colourAdapter = AddSpinnerAdapter(applicationContext, itemBasicList)

        colourSPN.adapter = colourAdapter

        colourSPN.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                val colourItem : AddColourItem = itemList[position]
                redAmount = colourItem.amountOfRed
                greenAmount = colourItem.amountOfGreen
                blueAmount = colourItem.amountOfBlue

            }

        }

    }

    private fun setUpTypeSpinner(itemList : ArrayList<AddActivityItem>)
    {
        val typeSPN : Spinner = findViewById(R.id.typeSPN)

        val typeAdapter = AddSpinnerAdapter(applicationContext, itemList)

        typeSPN.adapter = typeAdapter

        typeSPN.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                val typeItem : AddActivityItem = itemList[position]
                type = typeItem.itemTitle

            }

        }


    }

    private fun setUpSeasonSpinner(itemList : ArrayList<AddActivityItem>)
    {
        val seasonSPN : Spinner = findViewById(R.id.seasonSPN)

        val seasonAdapter = AddSpinnerAdapter(applicationContext, itemList)

        seasonSPN.adapter = seasonAdapter

        seasonSPN.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                val seasonItem : AddActivityItem = itemList[position]
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

        val quickSearchWorker = OneTimeWorkRequestBuilder<QuickSearchHandler>().setInputData(input).build()

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

        val rvAdapter: RecyclerView.Adapter<*> = SlideShowAdapter(applicationContext, images)

        pictureRV.apply {

            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

            adapter = rvAdapter
        }


    }
}
