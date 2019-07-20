package com.example.nn4wchallenge.database.external

import android.graphics.Color


class dataTranslation {

    var redAmount = 0
    var greenAmount = 0
    var blueAmount = 0

    fun StringToRGB(stringValue: String)
    {

        val chunks : ArrayList<String> = ArrayList()

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

    fun RGBToHexString(redAmount : Int, greenAmount : Int, blueAmount : Int) : String
    {
        var hexColourValue = "0x"

        if (redAmount <= 16) //i.e. between 0 and F
        {
            hexColourValue += ("0" + Integer.toHexString(redAmount))
        }
        else
        {
            hexColourValue += Integer.toHexString(redAmount)
        }

        if (greenAmount <= 16)
        {
            hexColourValue += ("0" + Integer.toHexString(greenAmount))
        }
        else
        {
            hexColourValue += Integer.toHexString(greenAmount)
        }

        if (blueAmount <= 16)
        {
            hexColourValue += ("0" + Integer.toHexString(blueAmount))
        }
        else
        {
            hexColourValue += Integer.toHexString(blueAmount)
        }

        return hexColourValue
    }


    //the threads have problems with converting array lists to arrays hence why this exists
    fun toDoubleArray(input : ArrayList<Double?>) : DoubleArray
    {
        val createArray = DoubleArray(input.size)

        for ((it, item) in input.withIndex())
        {
            if (item == null)
            {
                createArray[it] = 0.0
            }
            else
            {
                createArray[it] = item
            }

        }

        return createArray
    }


    fun toIntArray(input : ArrayList<Int?>) : IntArray
    {
        val createArray = IntArray(input.size)

        for ((it, item) in input.withIndex())
        {
            if (item == null)
            {
                createArray[it] = 0
            }
            else
            {
                createArray[it] = item
            }

        }

        return createArray
    }

    fun toStringArray(input : ArrayList<String?>) : Array<String>
    {
        val createArray : Array<String?> = arrayOfNulls(input.size)

        for ((it, item) in input.withIndex())
        {
            if (item == null)
            {
                createArray[it] = ""
            }
            else
            {
                createArray[it] = item
            }

        }

        return createArray as Array<String>
    }

}