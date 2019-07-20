package com.example.nn4wchallenge.colourMatcherCode

class Colour {

    var redAmount : Int = 0
    var greenAmount : Int = 0
    var blueAmount : Int = 0

    fun createColour(red : Int, green : Int, blue : Int)
    {
        redAmount = red
        greenAmount = green
        blueAmount = blue
    }

    fun equal(checkColour : Colour) : Boolean
    {
        return redAmount == checkColour.redAmount &&
                greenAmount == checkColour.greenAmount &&
                blueAmount == checkColour.blueAmount
    }
}