package com.example.nn4wchallenge.database.internal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class clothing {

    @PrimaryKey(autoGenerate = true) var id : Int = 0
    @ColumnInfo(name = "type") var clothingType : String? = null
    @ColumnInfo(name = "color") var clothingColor : String? = null
    @ColumnInfo(name = "season") var clothingSeason : String? = null
    @ColumnInfo(name = "image") var clothingImageLocation : String? = null

}