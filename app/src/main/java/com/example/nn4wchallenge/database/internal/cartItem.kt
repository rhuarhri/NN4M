package com.example.nn4wchallenge.database.internal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class cartItem {

    @PrimaryKey(autoGenerate = true) var id : Int = 0
    @ColumnInfo(name = " name") var itemName : String = ""
    @ColumnInfo(name = "price") var itemprice : Double = 0.0
    @ColumnInfo(name = "image") var itemImage : String = ""

}