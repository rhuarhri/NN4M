package com.example.nn4wchallenge.AddActivitySpinners

open class AddActivityItem (Title : String, ImageResource : Int) {

    var itemTitle : String = ""
    var itemImage : Int = 0

    init
    {
        itemTitle = Title
        itemImage = ImageResource
    }
}