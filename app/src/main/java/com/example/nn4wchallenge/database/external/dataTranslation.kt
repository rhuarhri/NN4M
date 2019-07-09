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


    //the threads have problems with converting array lists to arrays hence why this exists
    public fun toDoubleArray(input : ArrayList<Double?>) : DoubleArray
    {
        val createArray : DoubleArray = DoubleArray(input.size)

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


    public fun toIntArray(input : ArrayList<Int?>) : IntArray
    {
        val createArray : IntArray = IntArray(input.size)

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

    public fun toStringArray(input : ArrayList<String?>) : Array<String>
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

    private fun convertToArray(input : ArrayList<*>, default : Any) : Array<*>
    {
        var resultArray : Array<Any?> = arrayOfNulls(input.size)
        var iterator : Int = 0
        for (item in input)
        {
            if (item != null)
            {
                resultArray[iterator] = default
            }
            else
            {
                resultArray[iterator] = item
            }

        }

        return resultArray
    }
}