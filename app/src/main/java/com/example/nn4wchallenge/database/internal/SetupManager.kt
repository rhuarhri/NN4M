package com.example.nn4wchallenge.database.internal

import android.content.Context
import android.provider.ContactsContract
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf

class SetupManager (/*var context: Context?*/){

    //Code for setup procedure
    private var genderList : ArrayList<item> = ArrayList()
    private var ageList : ArrayList<item> = ArrayList()
    private var chestSizeList : ArrayList<item> = ArrayList()
    private var waistSizeList : ArrayList<item> = ArrayList()
    private var shoeSizeList : ArrayList<item> = ArrayList()

    //values used to identify function
    val GENDER : String = "gender"
    val AGE : String = "age"
    val CHEST : String = "chest"
    val WAIST : String = "waist"
    val SHOE : String = "shoe"


    /*
    This class exists in order to make the deciphering of a spinner item easy and
    ensure that the user has the necessary information to make a decision
     */
    private class item()
    {
        //displayed on a spinner
        var title : String = ""
        //used by the app
        var description : String = ""

        fun createItem(newTitle : String, newDescription : String)
        {
            title = newTitle
            description = newDescription
        }
    }

    init {
        setupGender()
        setupAge()
        setupChestSize()
        setupWaistSize()
        setupShoeSize()
    }

    private fun setupGender()
    {
        genderList = addToList("Male", "male", genderList)
        genderList = addToList("female", "female", genderList)
    }

    private fun setupAge()
    {
        ageList = addToList("baby (0 - 3)", "baby", ageList)
        ageList = addToList("child (4 - 12", "child", ageList)
        ageList = addToList("teenager (13 - 18", "teenager", ageList)
        ageList = addToList("adult (above 18)", "adult", ageList)
    }

    private fun setupWaistSize()
    {
        waistSizeList = addToList("XXS(uk) 0(eu) 61-66(cm)", "6", waistSizeList)
        waistSizeList = addToList("XS(uk) 1(eu) 69-74(cm)", "8", waistSizeList)
        waistSizeList = addToList("S(uk) 2(eu) 76-81(cm)", "10", waistSizeList)
        waistSizeList = addToList("M(uk) 3(eu) 84-89(cm)", "12", waistSizeList)
        waistSizeList = addToList("L(uk) 4(eu) 91-97(cm)", "14", waistSizeList)
        waistSizeList = addToList("XL(uk) 5(eu) 99-104(cm)", "16", waistSizeList)
        waistSizeList = addToList("XXL(uk) 6(eu) 107-112(cm)", "18", waistSizeList)
    }

    private fun setupChestSize()
    {
        chestSizeList = addToList("XXS(uk) 0(eu) 78-82(cm)", "6", chestSizeList)
        chestSizeList = addToList("XS(uk) 1(eu) 81-86(cm)", "8", chestSizeList)
        chestSizeList = addToList("S(uk) 2(eu) 89-94(cm)", "10", chestSizeList)
        chestSizeList = addToList("M(uk) 3(eu) 97-102(cm)", "12", chestSizeList)
        chestSizeList = addToList("L(uk) 4(eu) 104-109(cm)", "14", chestSizeList)
        chestSizeList = addToList("XL(uk) 5(eu) 112-117(cm)", "16", chestSizeList)
        chestSizeList = addToList("XXL(uk) 6(eu) 119-124(cm)", "18", chestSizeList)
    }

    private fun setupShoeSize()
    {
        shoeSizeList = addToList("5(uk) 39(eu) 6(us)", "5", shoeSizeList)
        shoeSizeList = addToList("6(uk) 40(eu) 7(us)", "6", shoeSizeList)
        shoeSizeList = addToList("7(uk) 41(eu) 8(us)", "7", shoeSizeList)
        shoeSizeList = addToList("8(uk) 42(eu) 9(us)", "8", shoeSizeList)
        shoeSizeList = addToList("9(uk) 43(eu) 10(us)", "9", shoeSizeList)
        shoeSizeList = addToList("10(uk) 44(eu) 11(us)", "10", shoeSizeList)
        shoeSizeList = addToList("11(uk) 45(eu) 12(us)", "11", shoeSizeList)
        shoeSizeList = addToList("12(uk) 46(eu) 13(us)", "12", shoeSizeList)
        shoeSizeList = addToList("13(uk) 47(eu) 14(us)", "13", shoeSizeList)

    }

    private fun addToList(Title : String , Description : String, list : ArrayList<item>) : ArrayList<item>
    {
        var returnedList : ArrayList<item> = list

        var newItem : item = item()

        newItem.createItem(Title, Description)

        returnedList.add(newItem)

        return returnedList
    }

    public fun getTitleList(from : String) : ArrayList<String>
    {
        var returnedList : ArrayList<String> = ArrayList()

        var itemList : ArrayList<item> = ArrayList()

        if (from == GENDER)
        {
            itemList = genderList
        }
        else if (from == AGE)
        {
            itemList = ageList
        }
        else if (from == CHEST)
        {
            itemList = chestSizeList
        }
        else if (from == WAIST)
        {
            itemList = waistSizeList
        }
        else if (from == SHOE)
        {
            itemList = shoeSizeList
        }
        else
        {
            return returnedList
        }

        for (item in itemList)
        {
            returnedList.add(item.title)
        }

        return returnedList
    }

    //end of code related to setup procedure
    //code related to saving information

    private var inputData : Data.Builder = Data.Builder()

    private var genderAdded = false
    public fun setGender(position : Int)
    {
        inputData.putString("gender", genderList[position].description)
        genderAdded = true
    }

    private var ageAdded = false
    public fun setAge(position : Int)
    {
        inputData.putString("age", ageList[position].description)
        ageAdded = true

    }

    private var chestSizeAdded = false
    public fun setChest(position: Int)
    {
        inputData.putInt("chest", chestSizeList[position].description.toInt())
        chestSizeAdded = true
    }

    private var waistSizeAdded = false
    public fun setWaist(position: Int)
    {
        inputData.putInt("waist", waistSizeList[position].description.toInt())
        waistSizeAdded = true
    }

    private var shoeSizeAdded = false
    public fun setShoe(position: Int)
    {
        inputData.putInt("shoe", shoeSizeList[position].description.toInt())
        shoeSizeAdded = true
    }

    public fun saveUserData() : String
    {
        var error = checkForErrors()

        if (error == "")
        {
            //run save user thread

            val newUser : Data = inputData.build()

            val saveUser = OneTimeWorkRequestBuilder<AddUserThreadManager>()
                .setInputData(newUser)
                .build()

            WorkManager.getInstance().enqueue(saveUser)
        }

        return error
    }

    private fun checkForErrors() : String
    {
        if (!genderAdded)
        {
            return "user gender not set"
        }
        else if (!ageAdded)
        {
            return "user age not set"
        }
        else if (!chestSizeAdded)
        {
            return "user chest size not set"
        }
        else if (!waistSizeAdded)
        {
            return "user waist size not set"
        }
        else if (!shoeSizeAdded)
        {
            return "user shoe size not set"
        }
        else
        {
            return ""
        }
    }

}