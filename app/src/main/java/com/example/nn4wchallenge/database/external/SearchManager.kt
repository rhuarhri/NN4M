package com.example.nn4wchallenge.database.external

import com.example.nn4wchallenge.clothingMatcherCode.ClothingMatcher
import com.example.nn4wchallenge.colourMatcherCode.ColourMatcher
import com.example.nn4wchallenge.database.internal.clothingDatabaseCode.Clothing
import com.example.nn4wchallenge.database.internal.userDatabaseCode.User
import java.lang.IllegalArgumentException

/*
This class exists in order to find clothes that match a particular input
How it works is by getting information from other classes on what makes a
match and then comparing that to the list of clothes from the data base removing
anything that does not match.

This functionality would have be done by a server and the results sent back to the
app. In fact I would have just sent a query to firestore which would have the same effect
as this code, however if I did I wouldn't use json files. I am using json files as they
are a common way to get information from servers which I think is something that I should
demonstrate the use of in this app.

 */

class SearchManager  {


    class MatchedPairs
    {
        var image : String = ""
        var descriptionLocation : String = ""
        var type : String = ""
        var season : String = ""

        fun createPair(Image : String, DescriptionLocation : String)
        {
            image = Image
            descriptionLocation = DescriptionLocation
        }

        fun addAditionalInfo(Type : String, Season : String)
        {
            type = Type
            season = Season
        }
    }

    private var readyToSearch : Boolean = false

    //this prevents the user from running a search without the necessary information
    private var userInfoAdded : Boolean = false
    private var UserInfo : User = User()

    private var clothingInfoAdded : Boolean = false
    private var ClothingInfo : Clothing = Clothing()

    private var redAmount : Int = 0
    private var greenAmount : Int = 0
    private var blueAmount : Int = 0

    private var type : String = ""

    //private var dataConversion : DataTranslation = DataTranslation()

    private var matchClothing : ClothingMatcher = ClothingMatcher()


    fun setupUserInfo(userInfo : User)
    {
        UserInfo = userInfo
        userInfoAdded = true
    }

    fun setupClothingInfo(clothingInfo : Clothing)
    {
        ClothingInfo = clothingInfo
        clothingInfoAdded = true
    }

    private fun readyToSearch() : Boolean
    {
        return userInfoAdded && clothingInfoAdded
    }

    fun search(ClothingItems : ArrayList<SearchItem>) : ArrayList<MatchedPairs>
    {
        val results : ArrayList<MatchedPairs> = ArrayList()

        if (!readyToSearch())
        {
            throw IllegalArgumentException("setup search must happen before searching")
        }
        else
        {

            for (item in ClothingItems)
            {
                if (item.season == ClothingInfo.clothingSeason) {
                    if (matchesColor(
                            item.colour,
                            ClothingInfo.clothingColorRed,
                            ClothingInfo.clothingColorGreen,
                            ClothingInfo.clothingColorBlue
                        )
                    ) {
                        if (matchesUserDescription(item)) {
                            if (matchClothing.matcher(ClothingInfo.clothingType, item.type)) {
                                if (doesFit(item.type, item.maxSize.toInt(), item.minSize.toInt())) {
                                    //matches with user
                                    val newPair = MatchedPairs()
                                    newPair.createPair(item.imageURL, item.descriptionURL)
                                    results.add(newPair)
                                }
                            }

                        }
                    }
                }
            }

        }

        //to ensure that the clothing data gets updated
        clothingInfoAdded = false
        return results
    }

    fun searchByColour(ClothingItems : ArrayList<SearchItem>, UserClothesColour : String) : ArrayList<MatchedPairs>
    {
        val results : ArrayList<MatchedPairs> = ArrayList()

        val dataConversion : DataTranslation = DataTranslation()
        dataConversion.stringToRGB(UserClothesColour)
        val red : Int = dataConversion.redAmount
        val green : Int = dataConversion.greenAmount
        val blue : Int = dataConversion.blueAmount

        if (!userInfoAdded)
        {
            throw IllegalArgumentException("setup search must happen before searching")
        }
        else
        {

            for (item in ClothingItems)
            {
                    if (matchesColor(item.colour, red, green, blue)) {
                        if (matchesUserDescription(item)) {
                                    val newPair = MatchedPairs()
                                    newPair.createPair(item.imageURL, item.descriptionURL)
                                    newPair.addAditionalInfo(item.type, item.season)
                                    results.add(newPair)
                        }
                    }

            }

        }

        //to ensure that the clothing data gets updated
        clothingInfoAdded = false
        return results
    }

    fun filterSearchResult(unFilteredItems : ArrayList<MatchedPairs>, type : String, season : String) : ArrayList<MatchedPairs>
    {

        val results : ArrayList<MatchedPairs> = ArrayList()

        for (item in unFilteredItems)
        {
            if(item.type != "" && item.season != "") {
                if (item.season == season) {
                    if (matchClothing.matcher(item.type, type)) {
                        results.add(item)
                    }
                }
            }
        }

        return results
    }

    //public for testing
    fun matchesColor(colour : String, matchRed : Int, matchGreen : Int, matchBlue : Int) : Boolean
    {
        val dataConversion : DataTranslation = DataTranslation()
        dataConversion.stringToRGB(colour)

        val colourM = ColourMatcher()

        colourM.matchColour(matchRed, matchGreen, matchBlue)

        return colourM.doesColourMatch(dataConversion.redAmount, dataConversion.greenAmount, dataConversion.blueAmount)

    }

    //public for testing
    fun matchesUserDescription(clothingItem : SearchItem) : Boolean
    {
        return clothingItem.age == UserInfo.userAge && clothingItem.gender == UserInfo.userGender
    }

    //public for testing
    fun doesFit(clothingItem : String, maxSize : Int, minSize : Int) : Boolean
    {

        val chestSize : Int = UserInfo.userChestMeasurement
        var matchChestSize = false

        val waistSize : Int = UserInfo.userWaistMeasurement
        var matchWaistSize = false

        val shoeSize : Int = UserInfo.userShoeSize
        var matchShoeSize = false

        when(clothingItem)
        {
            "dress" -> matchChestSize = true
            "jacket" -> matchChestSize = true
            "jumper" -> matchChestSize = true
            "shirt" -> matchChestSize = true
            "shorts" -> matchWaistSize = true
            "skirt" -> matchWaistSize = true
            "top" -> matchChestSize = true
            "trousers" -> matchWaistSize = true
            "shoes" -> matchShoeSize = true
        }

        if (matchChestSize)
        {
            return chestSize in minSize..maxSize
        }
        else if (matchWaistSize)
        {
            return waistSize in minSize..maxSize
        }
        else if (matchShoeSize)
        {
            return shoeSize in minSize..maxSize
        }

        return false
    }

}