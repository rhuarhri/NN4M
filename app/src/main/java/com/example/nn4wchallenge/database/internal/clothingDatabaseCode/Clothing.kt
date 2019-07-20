package com.example.nn4wchallenge.database.internal.clothingDatabaseCode

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Clothing {

    @PrimaryKey(autoGenerate = true) var id : Int = 0
    @ColumnInfo(name = "type") var clothingType : String = ""
    @ColumnInfo(name = "colorRedAmount") var clothingColorRed : Int = 0
    @ColumnInfo(name = "colorGreenAmount") var clothingColorGreen : Int = 0
    @ColumnInfo(name = "colorBlueAmount") var clothingColorBlue : Int = 0
    @ColumnInfo(name = "season") var clothingSeason : String = ""
    @ColumnInfo(name = "image") var clothingImageLocation : String = ""

}