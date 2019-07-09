package com.example.nn4wchallenge.displayClothing

class clothingItem(Id : Int, Title: String, Measurement : String, ImageLocation : String) {

    var id : Int = 0
    var title : String = ""
    var measurement : String = ""
    var imageLocation : String = ""

    init
    {
        id = Id
        title = Title
        measurement = Measurement
        imageLocation = ImageLocation
    }
}