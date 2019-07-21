package com.example.nn4wchallenge

import com.example.nn4wchallenge.AddActivitySpinners.AddActivityItem
import com.example.nn4wchallenge.AddActivitySpinners.AddColourItem

class clothesSpinnerData {


    fun getColourList() : ArrayList<AddColourItem>
    {
        val colourList: ArrayList<AddColourItem> = ArrayList()
        colourList.add(AddColourItem("black", R.color.colorBlack, 0, 0, 0))
        colourList.add(AddColourItem("blue", R.color.colorBlue, 0, 0, 255))
        colourList.add(AddColourItem("light blue", R.color.colorLightBlue, 0, 255, 255))
        colourList.add(AddColourItem("green", R.color.colorGreen, 0, 255, 0))
        colourList.add(AddColourItem("yellow", R.color.colorYellow, 255, 255, 0))
        colourList.add(AddColourItem("red", R.color.colorRed, 255, 0, 0))
        colourList.add(AddColourItem("pink", R.color.colorPink, 255, 0, 255))
        colourList.add(AddColourItem("white", R.color.colorWhite, 255, 255, 255))
        return colourList
    }


    fun getTypeList() : ArrayList<AddActivityItem> {
        val typeList: ArrayList<AddActivityItem> = ArrayList()
        typeList.add(AddActivityItem("dress", R.drawable.dress_icon))
        typeList.add(AddActivityItem("jacket", R.drawable.jacket_icon))
        typeList.add(AddActivityItem("jumper", R.drawable.jumper_icon))
        typeList.add(AddActivityItem("shirt", R.drawable.shirt_icon))
        typeList.add(AddActivityItem("shorts", R.drawable.shorts_icon))
        typeList.add(AddActivityItem("skirt", R.drawable.skirt_icon))
        typeList.add(AddActivityItem("top", R.drawable.top_icon))
        typeList.add(AddActivityItem("trousers", R.drawable.trousers_icon))
        typeList.add(AddActivityItem("shoes", R.drawable.shoe_icon))
        return typeList
    }

    /*
    season mainly refers to the kind of scenario the item of clothing
    will be used in for example you would not wear suit which si formal
    on the beach which is a summer time scenario
     */
    fun getSeasonsList() : ArrayList<AddActivityItem> {
        val seasonList: ArrayList<AddActivityItem> = ArrayList()
        seasonList.add(AddActivityItem("summer", R.drawable.summer_icon))
        seasonList.add(AddActivityItem("winter", R.drawable.winter_icon))
        seasonList.add(AddActivityItem("party", R.drawable.party_icon))
        seasonList.add(AddActivityItem("formal", R.drawable.formal_icon))
        return seasonList
    }

}