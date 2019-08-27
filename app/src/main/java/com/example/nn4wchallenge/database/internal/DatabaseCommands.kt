package com.example.nn4wchallenge.database.internal

class DatabaseCommands {

    /*
    This is a last of key values used when manipulating the internal data base
    it exists to make the database manipulation easier
     */

    //database id
    val Clothing_DB = "clothingDB"
    //database actions
    val Clothing_Add = "clothingAdd"
    val Clothing_Delete = "clothingDelete"
    val Clothing_Update = "clothingUpdate"
    val Clothing_Get = "clothingGet"
    //names of values store in the database
    val Clothing_ID = "clothingId"
    val Clothing_type = "clothingType"
    val Clothing_season = "clothingSeason"
    val Clothing_picture = "clothingPicture"
    val Clothing_red_color = "clothingRed"
    val Clothing_blue_color = "clothingBlue"
    val Clothing_green_color = "clothingGreen"
    val Clothing_color = "clothingColor"

    //database id
    val Cart_DB = "cartDB"
    //database actions
    val Cart_Add = "cartAdd"
    val Cart_Delete = "cartDelete"
    val Cart_Update = "cartUpdate"
    val Cart_Get = "cartGet"
    val Cart_Get_Prices = "cartGetPrice"
    val Cart_Get_Single_Item = "cartGetItem"
    //names of values store in the database
    val Cart_ID = "cartId"
    val Cart_name = "cartName"
    val Cart_price = "cartPrice"
    val Cart_total_price = "cartTotal"
    val Cart_picture = "cartPicture"

    //database id
    val User_DB = "userDB"
    //database actions
    val User_Add = "userAdd"
    val User_Delete = "userDelete"
    val User_Update = "userUpdate"
    val User_Get = "userGet"
    //names of values store in the database
    val User_ID = "userId"
    val User_name = "userName"
    val User_password = "userPassword"
    val User_gender = "userGender"
    val User_age = "userAge"
    val User_waist = "userWaist"
    val User_chest = "userChest"
    val User_shoe_Size = "userShoe"
    val User_card_number = "userCard"
    val User_sort_code = "userCode"


}