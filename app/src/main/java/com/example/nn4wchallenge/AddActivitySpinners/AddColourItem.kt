package com.example.nn4wchallenge.AddActivitySpinners

class AddColourItem(Title: String, ImageResource: Int,
                    redValue : Int, greenValue : Int, blueValue : Int ) : AddActivityItem(Title, ImageResource) {

    var amountOfRed : Int  = 0
    var amountOfGreen : Int = 0
    var amountOfBlue : Int = 0

    init {
        amountOfRed = redValue
        amountOfGreen = greenValue
        amountOfBlue = blueValue
    }
}