package com.example.nn4wchallenge.database.internal

import androidx.work.*
import java.util.*
import kotlin.collections.ArrayList

class SetupManager {

    private val commands : DatabaseCommands = DatabaseCommands()

    //Code for setup procedure
    private var genderList : ArrayList<Item> = ArrayList()
    private var ageList : ArrayList<Item> = ArrayList()
    private var chestSizeList : ArrayList<Item> = ArrayList()
    private var waistSizeList : ArrayList<Item> = ArrayList()
    private var shoeSizeList : ArrayList<Item> = ArrayList()

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
    private class Item
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
        genderList = addToList("Female", "female", genderList)
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

    private fun addToList(Title : String , Description : String, list : ArrayList<Item>) : ArrayList<Item>
    {
        val returnedList : ArrayList<Item> = list

        val newItem = Item()

        newItem.createItem(Title, Description)

        returnedList.add(newItem)

        return returnedList
    }

    fun getTitleList(from : String) : ArrayList<String>
    {
        val returnedList : ArrayList<String> = ArrayList()

        var itemList : ArrayList<Item> = ArrayList()

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
    fun setGender(position : Int)
    {
        genderPosition = position
        inputData.putString(commands.User_gender, genderList[position].description)
        genderAdded = true
    }

    private var ageAdded = false
    fun setAge(position : Int)
    {
        agePosition = position
        inputData.putString(commands.User_age, ageList[position].description)
        ageAdded = true

    }

    private var chestSizeAdded = false
    fun setChest(position: Int)
    {
        chestPosition = position
        inputData.putInt(commands.User_chest, chestSizeList[position].description.toInt())
        chestSizeAdded = true
    }

    private var waistSizeAdded = false
    fun setWaist(position: Int)
    {
        waistPosition = position
        inputData.putInt(commands.User_waist, waistSizeList[position].description.toInt())
        waistSizeAdded = true
    }

    private var shoeSizeAdded = false
    fun setShoe(position: Int)
    {
        shoeSizePosition = position
        inputData.putInt(commands.User_shoe_Size, shoeSizeList[position].description.toInt())
        shoeSizeAdded = true
    }

    fun saveUserData() : String
    {
        val error = checkForErrors()

        if (error == "")
        {
            //run save user thread

            inputData.putString(commands.User_DB, commands.User_DB)
            inputData.putString(commands.User_Add, commands.User_Add)
            val newUser : Data = inputData.build()

            val saveUser = OneTimeWorkRequestBuilder<DatabaseManager>()
                .setInputData(newUser)
                .build()

            WorkManager.getInstance().enqueue(saveUser)
        }

        return error
    }

    var genderPosition : Int = 0
    var agePosition : Int = 0
    var chestPosition : Int = 0
    var waistPosition : Int = 0
    var shoeSizePosition : Int = 0

    fun getExistingUserData() : UUID
    {
        inputData.putString(commands.User_DB, commands.User_DB)
        inputData.putString(commands.User_Update, commands.User_Update)
        val existingUser = inputData.build()

        val getUserData = OneTimeWorkRequestBuilder<DatabaseManager>()
            .setInputData(existingUser)
            .build()

        WorkManager.getInstance().enqueue(getUserData)


        return getUserData.id
    }


    fun displayExistingData( workInfo: WorkInfo)
    {
        genderPosition = findPosition(genderList, workInfo.outputData.getString(commands.User_gender))
        agePosition = findPosition(ageList, workInfo.outputData.getString(commands.User_age))
        chestPosition = findPosition(chestSizeList, workInfo.outputData.getInt(commands.User_chest, 0).toString())
        waistPosition = findPosition(waistSizeList, workInfo.outputData.getInt(commands.User_waist, 0).toString())
        shoeSizePosition = findPosition(shoeSizeList, workInfo.outputData.getInt(commands.User_shoe_Size, 0).toString())
    }

    private fun findPosition(itemList : ArrayList<Item>, itemDescription : String?) : Int
    {
        if (itemDescription == null)
        {
            return 0
        }
        else {
            for ((position, item) in itemList.withIndex()) {
                if (item.description == itemDescription) {
                    return position
                }
            }

            return 0
        }
    }

    fun updateUserData()
    {

        inputData.putString(commands.User_DB, commands.User_DB)
        inputData.putString(commands.User_Update, commands.User_Update)
        val newUser : Data = inputData.build()

        val updateUser = OneTimeWorkRequestBuilder<DatabaseManager>()
            .setInputData(newUser)
            .build()

        WorkManager.getInstance().enqueue(updateUser)
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