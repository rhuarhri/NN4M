package com.example.nn4wchallenge.database.external

import kotlin.math.roundToInt


class ItemDescription {

    var name : String = ""
    var description : String = ""
    var cost : Double = 0.0
    var reduction : Int = 0
    var images : ArrayList<String> = ArrayList()

    fun createDescription(Name : String, Description : String, Cost : Double, wasCost : Double, Images : ArrayList<String>)
    {
    name = Name
        description = Description
        cost = Cost
        reduction = calculateReduction(Cost , wasCost)
        images = Images
    }

    private fun calculateReduction(cost : Double, wasCost: Double) : Int
    {
        return if ((wasCost - cost) == 0.0)
        {
            0
        }
        else
        {
            (100 / (wasCost - cost)).roundToInt()
        }
    }

    fun setReduction(cost : Double, wasCost: Double)
    {
        reduction = calculateReduction(cost , wasCost)
    }
}