package com.example.nn4wchallenge.clothingMatcherCode

class clothingMatcher {

    private var ChestSize : Int = 0
    private var matchChestSize : Boolean = false

    private var WaistSize : Int = 0
    private var matchWaistSize : Boolean = false

    private var ShoeSize : Int = 0
    private var matchShoeSize : Boolean = false

    public fun matcher(matchClothing : String, withClothing : String) : Boolean
    {
        var matches : ArrayList<String> = ArrayList()

        when(matchClothing)
        {
            "dress" -> matches = getDressMatches()
            "jacket" -> matches = getJacketMatches()
            "jumper" -> matches = getJumperMatches()
            "shirt" -> matches = getShirtMatches()
            "shorts" -> matches = getShortstMatches()
            "skirt" -> matches = getSkirtMatches()
            "top" -> matches = getTopMatches()
            "trousers" -> matches = getTrousersMatches()
        }

        return matches.contains(withClothing)
    }

    public fun setUserSize(chestSize : Int, waistSize : Int, shoeSize : Int)
    {
        ChestSize = chestSize
        WaistSize = waistSize
        ShoeSize = shoeSize
    }



    private fun getDressMatches() : ArrayList<String>
    {
        var dressMatches : ArrayList<String> = ArrayList()

        dressMatches.add("shoe")

        return dressMatches
    }

    private fun getJacketMatches() : ArrayList<String>
    {
        var jacketMatches : ArrayList<String> = ArrayList()

        jacketMatches.add("shirt")
        jacketMatches.add("shorts")
        jacketMatches.add("skirt")
        jacketMatches.add("top")
        jacketMatches.add("trousers")

        return jacketMatches
    }

    private fun getJumperMatches() : ArrayList<String>
    {
        var jumperMatches : ArrayList<String> = ArrayList()

        jumperMatches.add("shirt")
        jumperMatches.add("skirt")
        jumperMatches.add("top")
        jumperMatches.add("trousers")

        return jumperMatches
    }

    private fun getShirtMatches() : ArrayList<String>
    {
        var shirtMatches : ArrayList<String> = ArrayList()

        shirtMatches.add("jacket")
        shirtMatches.add("jumper")
        shirtMatches.add("shorts")
        shirtMatches.add("skirt")
        shirtMatches.add("trousers")

        return shirtMatches
    }

    private fun getShortstMatches() : ArrayList<String>
    {
        var shortsMatches : ArrayList<String> = ArrayList()

        shortsMatches.add("jacket")
        shortsMatches.add("jumper")
        shortsMatches.add("shirt")
        shortsMatches.add("top")

        return shortsMatches
    }

    private fun getSkirtMatches() : ArrayList<String>
    {
        var skirtMatches : ArrayList<String> = ArrayList()

        skirtMatches.add("jacket")
        skirtMatches.add("jumper")
        skirtMatches.add("shirt")
        skirtMatches.add("top")

        return skirtMatches
    }

    private fun getTopMatches() : ArrayList<String>
    {
        var topMatches : ArrayList<String> = ArrayList()

        topMatches.add("jacket")
        topMatches.add("jumper")
        topMatches.add("shorts")
        topMatches.add("skirt")
        topMatches.add("trousers")

        return topMatches
    }

    private fun getTrousersMatches() : ArrayList<String>
    {
        var trouserMatches : ArrayList<String> = ArrayList()

        trouserMatches.add("jacket")
        trouserMatches.add("jumper")
        trouserMatches.add("shirt")
        trouserMatches.add("top")

        return trouserMatches
    }
}