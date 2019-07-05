package com.example.nn4wchallenge.database.external

import com.example.nn4wchallenge.clothingMatcherCode.clothingMatcher
import com.example.nn4wchallenge.colourMatcherCode.colourMatcher
import com.example.nn4wchallenge.database.internal.clothing
import com.example.nn4wchallenge.database.internal.user
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

class searchManager  {

    //private var imageList : ArrayList<String> = ArrayList()
    //private var descriptionLocationList : ArrayList<String> = ArrayList()

    class matchedPairs()
    {
        var image : String = ""
        var descriptionLocation : String = ""

        fun createPair(Image : String, DescriptionLocation : String)
        {
            image = Image
            descriptionLocation = DescriptionLocation
        }
    }

    private var readyToSearch : Boolean = false

    //this prevents the user from running a search without the necessary information
    private var userInfoAdded : Boolean = false
    private var UserInfo : user = user()

    private var clothingInfoAdded : Boolean = false
    private var ClothingInfo : clothing = clothing()

    private var redAmount : Int = 0
    private var greenAmount : Int = 0
    private var blueAmount : Int = 0

    private var type : String = ""

    private var dataConversion : dataTranslation = dataTranslation()

    private var matchClothing : clothingMatcher = clothingMatcher()

    /*
    public fun setupSearch()
    {
        imageList = ArrayList()
        descriptionLocationList = ArrayList()

        readyToSearch = true
    }*/

    public fun setupUserInfo(userInfo : user)
    {
        UserInfo = userInfo
        userInfoAdded = true
    }

    public fun setupClothingInfo(clothingInfo : clothing)
    {
        ClothingInfo = clothingInfo
        clothingInfoAdded = true
    }

    private fun readyToSearch() : Boolean
    {
        return userInfoAdded && clothingInfoAdded
    }

    public fun search(ClothingItems : ArrayList<searchItem>) : ArrayList<matchedPairs>
    {
        var results : ArrayList<matchedPairs> = ArrayList()

        if (!readyToSearch())
        {
            throw IllegalArgumentException("setup search must happen before searching")
        }
        else
        {

            for (item in ClothingItems)
            {
                if (item.season == ClothingInfo.clothingSeason) {
                    if (matchesColor(item.colour)) {
                        if (matchesUserDescription(item)) {
                            if (matchClothing.matcher(ClothingInfo.clothingType, item.type)) {
                                if (doesFit(item.type, item.maxSize.toInt(), item.minSize.toInt())) {
                                    //matches with user
                                    var newPair : matchedPairs = matchedPairs()
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


    //public for testing
    public fun matchesColor(colour : String) : Boolean
    {
        dataConversion.StringToRGB(colour)

        var colourM : colourMatcher = colourMatcher()

        colourM.matchColour(ClothingInfo.clothingColorRed, ClothingInfo.clothingColorGreen, ClothingInfo.clothingColorBlue)

        return colourM.doesColourMatch(dataConversion.redAmount, dataConversion.greenAmount, dataConversion.blueAmount)

    }

    //public for testing
    public fun matchesUserDescription(clothingItem : searchItem) : Boolean
    {
        //this checks if the user is right for the clothing
        if (clothingItem.age == UserInfo.userAge)
        {
            return true
        }

        if (clothingItem.gender == UserInfo.userGender)
        {
            return true
        }

        return false
    }

    //public for testing
    public fun doesFit(clothingItem : String, maxSize : Int, minSize : Int) : Boolean
    {

        var chestSize : Int = UserInfo.userChestMeasurement
        var matchChestSize : Boolean = false

        var waistSize : Int = UserInfo.userWaistMeasurement
        var matchWaistSize : Boolean = false

        var shoeSize : Int = UserInfo.userShoeSize
        var matchShoeSize : Boolean = false

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