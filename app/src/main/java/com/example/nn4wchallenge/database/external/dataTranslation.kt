package com.example.nn4wchallenge.database.external

import android.graphics.Color


class dataTranslation {

    public var redAmount = 0
    public var greenAmount = 0
    public var blueAmount = 0


    public fun StringToRGB(stringValue: String)
    {

        var chunks : ArrayList<String> = ArrayList()

        var firstLetter : Char? = null
        var secondLetter : Char? = null

        for (item in 0 until stringValue.length)
        {
            if (firstLetter == null)
            {
                firstLetter = stringValue[item]
            }
            else if (secondLetter == null)
            {
                secondLetter = stringValue[item]

                chunks.add("$firstLetter$secondLetter")

                firstLetter = null
                secondLetter = null
            }
        }

        redAmount = Integer.parseInt(chunks[1], 16)
        greenAmount = Integer.parseInt(chunks[2], 16)
        blueAmount = Integer.parseInt(chunks[3], 16)
    }
}