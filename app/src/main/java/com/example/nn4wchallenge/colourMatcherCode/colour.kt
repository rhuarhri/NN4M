package com.example.nn4wchallenge.colourMatcherCode

class colour {

    var redAmount : Int = 0
    var greenAmount : Int = 0
    var blueAmount : Int = 0

    public fun createColour(red : Int, green : Int, blue : Int)
    {
        redAmount = red
        greenAmount = green
        blueAmount = blue
    }

    public fun equal(checkColour : colour) : Boolean
    {
        return redAmount == checkColour.redAmount &&
                greenAmount == checkColour.greenAmount &&
                blueAmount == checkColour.blueAmount
    }
}