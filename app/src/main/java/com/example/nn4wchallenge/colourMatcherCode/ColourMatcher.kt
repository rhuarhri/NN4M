package com.example.nn4wchallenge.colourMatcherCode

import com.example.nn4wchallenge.database.external.DataTranslation

class ColourMatcher {

    /*
    Why this exists
    This code exists in order for the app to benefit from colour theory.
    In short colour theory is some basic rules that determine what colours look good
    together.
    The below code only uses two rules which are adjacent colours and contrasting colours.
    Only these two rules are implemented as any more might make the range of colours too much to
    deal with.

    rules used
    adjacent colours
    adjacent colours are next to each other on a colour wheel for example red and orange
    contrasting colours
    contrasting colours directly opposite each other on a colour wheel for example white and black

    how does this apply to fashion
    Through colour theory you can determine which colours look good together as a result two items
    of clothing paired using colour theory will look good together
     */

    lateinit var colours : ArrayList<Colour>


    fun matchColour(red : Int, green : Int, blue : Int)
    {
        colours  = ArrayList()

        val newColour = setupToBasicColour(red, green, blue)

        //any colour will look good with another version of itself
        colours.add(newColour)

        findAdjacent(newColour)
        findContrasting(newColour)

    }

    fun doesColourMatch(red : Int, green : Int, blue : Int) : Boolean
    {

        val newColor = setupToBasicColour(red, green, blue)

        return listContainsColour(newColor)
    }

    private fun listContainsColour(newColour : Colour) :Boolean
    {
        for (item in colours)
        {
            if (item.equal(newColour))
            {
                return true
            }
        }

        return false
    }

    //public for testing
    fun findAdjacent(newColor: Colour)
    {

        val totalColourValue : Int = (newColor.redAmount + newColor.greenAmount + newColor.blueAmount)

        if (totalColourValue == 255)
        {
            addColour(modifyColour(255, newColor))
        }
        else if (totalColourValue == 510)
        {
            addColour(modifyColour(0, newColor))
        }
        else
        {
            addColour(allMixColour(newColor))
        }

    }

    private fun modifyColour(modifyAmount : Int, newColor: Colour) : ArrayList<Colour>
    {
        val colour1 = Colour()
        val colour2 = Colour()

        val returnedColours : ArrayList<Colour> = ArrayList()

        if (newColor.redAmount == modifyAmount)
        {
            colour1.createColour(newColor.redAmount, modifyAmount, newColor.blueAmount)

            returnedColours.add(colour1)

            colour2.createColour(newColor.redAmount, newColor.greenAmount, modifyAmount)

            returnedColours.add(colour2)

        }
        else if (newColor.greenAmount == modifyAmount)
        {
            colour1.createColour(modifyAmount,  newColor.greenAmount, newColor.blueAmount)

            returnedColours.add(colour1)

            colour2.createColour(newColor.redAmount, newColor.greenAmount,  modifyAmount)

            returnedColours.add(colour2)

        }
        else
        {
            colour1.createColour(newColor.redAmount, modifyAmount, newColor.blueAmount)

            returnedColours.add(colour1)

            colour2.createColour(modifyAmount, newColor.greenAmount, newColor.blueAmount)

            returnedColours.add(colour2)

        }

        return returnedColours
    }

    private fun allMixColour(newColor: Colour) : ArrayList<Colour>
    {
        val foundColour = Colour()
        val returnedColours : ArrayList<Colour> = ArrayList()

        val totalColourValue = (newColor.redAmount + newColor.greenAmount + newColor.blueAmount)

        if (totalColourValue == 0)
        {
            foundColour.createColour(255,255,255)//white
            returnedColours.add(foundColour)
        }
        else
        {
            foundColour.createColour(0,0,0)//black
            returnedColours.add(foundColour)
        }

        return returnedColours
    }


    //public for testing
    fun addColour(newColours : ArrayList<Colour>)
    {
        for (colour in newColours) {
            var isInColourList = false
            val currentColour : Colour = colour

            if (colours.isEmpty()) {
                colours.add(currentColour)
            } else {
                for (savedColour in colours) {
                    if (savedColour.equal(currentColour)) {
                        isInColourList = true
                        break
                    }
                }

                if (!isInColourList) {
                    colours.add(currentColour)
                }
            }
        }
    }

    //public for testing
    fun findContrasting(newColor: Colour)
    {
        val contrastColour = Colour()

        val newRedAmount : Int = flipColourValue(newColor.redAmount)

        val newGreenAmount : Int = flipColourValue(newColor.greenAmount)

        val newBlueAmount : Int = flipColourValue(newColor.blueAmount)

        contrastColour.createColour(newRedAmount, newGreenAmount, newBlueAmount)

        val returnedColours : ArrayList<Colour> = ArrayList()
        returnedColours.add(contrastColour)

        addColour(returnedColours)
    }

    private fun flipColourValue(oldColour : Int) : Int
    {
        val newColour : Int = 255 - oldColour

        return newColour
    }

    //public for testing
    fun setupToBasicColour(redAmount : Int, greenAmount : Int, blueAmount : Int) : Colour
    {
        val modifiedColour = Colour()

        val colourConverter : DataTranslation = DataTranslation()

        colourConverter.roundColourValue(redAmount, greenAmount, blueAmount)

        modifiedColour.createColour(
            colourConverter.redAmount,
            colourConverter.greenAmount,
            colourConverter.blueAmount
        )

        return modifiedColour
    }

}