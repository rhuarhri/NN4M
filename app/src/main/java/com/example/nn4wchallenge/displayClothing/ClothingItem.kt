package com.example.nn4wchallenge.displayClothing

class ClothingItem(Id : Int, Title: String, Measurement : String, ImageLocation : String, colour : String) {

    var id : Int = 0
    var title : String = ""
    var measurement : String = ""
    var imageLocation : String = ""
    var itemColour : String = ""

    init
    {
        id = Id
        title = Title
        measurement = Measurement
        imageLocation = ImageLocation
        itemColour = colour
    }
}