package com.example.nn4wchallenge.AddActivitySpinners

class addColourItem(Title: String, ImageResource: Int,
                    redValue : Int, greenValue : Int, blueValue : Int ) : addActivityItem(Title, ImageResource) {

    var amountOfRed : Int  = 0
    var amountOfGreen : Int = 0
    var amountOfBlue : Int = 0

    init {
        amountOfRed = redValue
        amountOfGreen = greenValue
        amountOfBlue = blueValue
    }
}