package com.example.nn4wchallenge.displayClothing

interface ClothingListListener {

    fun onEditClothing(id : Int, type : String, season : String, image : String, colour : String)
}