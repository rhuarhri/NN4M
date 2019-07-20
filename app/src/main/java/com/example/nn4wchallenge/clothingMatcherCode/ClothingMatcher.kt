package com.example.nn4wchallenge.clothingMatcherCode

class ClothingMatcher {

    fun matcher(matchClothing : String, withClothing : String) : Boolean
    {
        var matches : ArrayList<String> = ArrayList()

        when(matchClothing)
        {
            "dress" -> matches = getDressMatches()
            "jacket" -> matches = getJacketMatches()
            "jumper" -> matches = getJumperMatches()
            "shirt" -> matches = getShirtMatches()
            "shorts" -> matches = getShortsMatches()
            "skirt" -> matches = getSkirtMatches()
            "top" -> matches = getTopMatches()
            "trousers" -> matches = getTrousersMatches()
        }

        return matches.contains(withClothing)
    }

    private fun getDressMatches() : ArrayList<String>
    {
        val dressMatches : ArrayList<String> = ArrayList()

        dressMatches.add("shoes")

        return dressMatches
    }

    private fun getJacketMatches() : ArrayList<String>
    {
        val jacketMatches : ArrayList<String> = ArrayList()

        jacketMatches.add("shirt")
        jacketMatches.add("shorts")
        jacketMatches.add("skirt")
        jacketMatches.add("top")
        jacketMatches.add("trousers")

        return jacketMatches
    }

    private fun getJumperMatches() : ArrayList<String>
    {
        val jumperMatches : ArrayList<String> = ArrayList()

        jumperMatches.add("shirt")
        jumperMatches.add("skirt")
        jumperMatches.add("top")
        jumperMatches.add("trousers")

        return jumperMatches
    }

    private fun getShirtMatches() : ArrayList<String>
    {
        val shirtMatches : ArrayList<String> = ArrayList()

        shirtMatches.add("jacket")
        shirtMatches.add("jumper")
        shirtMatches.add("shorts")
        shirtMatches.add("skirt")
        shirtMatches.add("trousers")

        return shirtMatches
    }

    private fun getShortsMatches() : ArrayList<String>
    {
        val shortsMatches : ArrayList<String> = ArrayList()

        shortsMatches.add("jacket")
        shortsMatches.add("jumper")
        shortsMatches.add("shirt")
        shortsMatches.add("top")

        return shortsMatches
    }

    private fun getSkirtMatches() : ArrayList<String>
    {
        val skirtMatches : ArrayList<String> = ArrayList()

        skirtMatches.add("jacket")
        skirtMatches.add("jumper")
        skirtMatches.add("shirt")
        skirtMatches.add("top")

        return skirtMatches
    }

    private fun getTopMatches() : ArrayList<String>
    {
        val topMatches : ArrayList<String> = ArrayList()

        topMatches.add("jacket")
        topMatches.add("jumper")
        topMatches.add("shorts")
        topMatches.add("skirt")
        topMatches.add("trousers")

        return topMatches
    }

    private fun getTrousersMatches() : ArrayList<String>
    {
        val trouserMatches : ArrayList<String> = ArrayList()

        trouserMatches.add("jacket")
        trouserMatches.add("jumper")
        trouserMatches.add("shirt")
        trouserMatches.add("top")

        return trouserMatches
    }
}