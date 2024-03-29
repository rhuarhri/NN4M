package com.example.nn4wchallenge.database.internal.userDatabaseCode

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User {

    @PrimaryKey(autoGenerate = true) var id : Int = 0
    @ColumnInfo(name = "gender") var userGender : String = ""
    @ColumnInfo(name = "age") var userAge : String = ""
    @ColumnInfo(name = "chest") var userChestMeasurement : Int = 0
    @ColumnInfo(name = "waist") var userWaistMeasurement : Int = 0
    @ColumnInfo(name = "shoeSize") var userShoeSize : Int = 0

    //information below will be encrypted or hashed if used
    @ColumnInfo(name = "name") var userName : Int = 0 //hashed
    @ColumnInfo(name = "password") var userPassword : Int = 0 //hashed
    @ColumnInfo(name = "cardNumber") var userCardNumber : String = "" //encrypted
    @ColumnInfo(name = "cardSortCode") var userCardSortCode : String = "" //encrypted
}