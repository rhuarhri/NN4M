package com.example.nn4wchallenge

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    //tests for colour matcher
    @Test
    fun findCorrectAdjacentColoursForBaseColour()
    {
        /*
        In this test a base colour is either red, green or blue
         */
        var test : colourMatcher = colourMatcher()
        test.colours = ArrayList()

        var testcolour : colour = colour()
        testcolour.createColour(0,0,255)//colour blue

        test.findAdjacent(testcolour)

        var testColour1 : colour = colour()
        var expectedColour : ArrayList<colour> = ArrayList()
        testColour1.createColour(0, 255, 255)
        expectedColour.add(testColour1)//light blue
        var testColour2 : colour = colour()
        testColour2.createColour(255, 0, 255)
        expectedColour.add(testColour2)//pink


        var result = test.colours

        //there is probably a better way to test this instead of have a lot of asserts
        //however it is like this as it shows me exactly what failed
        assertEquals("red check 1", expectedColour.get(0).redAmount, result.get(0).redAmount)
        assertEquals("green check 1", expectedColour.get(0).greenAmount, result.get(0).greenAmount)
        assertEquals("blue check 1", expectedColour.get(0).blueAmount, result.get(0).blueAmount)

        assertEquals("red check 2", expectedColour.get(1).redAmount, result.get(1).redAmount)
        assertEquals("green check 2", expectedColour.get(1).greenAmount, result.get(1).greenAmount)
        assertEquals("blue check 2", expectedColour.get(1).blueAmount, result.get(1).blueAmount)

    }

    @Test
    fun findCorrectAdjacentColoursForPartMixColours()
    {
        /*
        In this test part mix colours are colours that consist of two of three colours
        for example yellow is a mix of green and red
         */

        var test : colourMatcher = colourMatcher()
        test.colours = ArrayList()

        var testcolour : colour = colour()
        testcolour.createColour(255,255,0)//colour yellow

        test.findAdjacent(testcolour)

        var testColour1 : colour = colour()
        var expectedColour : ArrayList<colour> = ArrayList()
        testColour1.createColour(255, 0, 0)
        expectedColour.add(testColour1)//green
        var testColour2 : colour = colour()
        testColour2.createColour(0, 255, 0)
        expectedColour.add(testColour2)//red

        var result = test.colours

        assertEquals("red check 1", expectedColour.get(0).redAmount, result.get(0).redAmount)
        assertEquals("green check 1", expectedColour.get(0).greenAmount, result.get(0).greenAmount)
        assertEquals("blue check 1", expectedColour.get(0).blueAmount, result.get(0).blueAmount)

        assertEquals("red check 2", expectedColour.get(1).redAmount, result.get(1).redAmount)
        assertEquals("green check 2", expectedColour.get(1).greenAmount, result.get(1).greenAmount)
        assertEquals("blue check 2", expectedColour.get(1).blueAmount, result.get(1).blueAmount)
    }

    @Test
    fun findCorrectAdjacentColourForAllColourMix()
    {
        /*
        In this test all colour mix is the result of mixing all red, green and blue together
        which will be black and white
         */

        var test : colourMatcher = colourMatcher()
        test.colours = ArrayList()

        var testcolour : colour = colour()
        testcolour.createColour(0,0,0)//colour black

        test.findAdjacent(testcolour)

        var testColour : colour = colour()
        var expectedColour : ArrayList<colour> = ArrayList()
        testColour.createColour(255, 255, 255)
        expectedColour.add(testColour)//white

        var result = test.colours

        assertEquals("red check 1", expectedColour.get(0).redAmount, result.get(0).redAmount)
        assertEquals("green check 1", expectedColour.get(0).greenAmount, result.get(0).greenAmount)
        assertEquals("blue check 1", expectedColour.get(0).blueAmount, result.get(0).blueAmount)
    }

    @Test
    fun findCorrectContrastingColours()
    {
        var test : colourMatcher = colourMatcher()
        test.colours = ArrayList()

        var testcolour : colour = colour()
        testcolour.createColour(0,255,0)//colour green

        test.findContrasting(testcolour)

        var result = test.colours

        var expectedRedAmount = 255
        assertEquals("red check", expectedRedAmount, result.get(0).redAmount)

        var expectedGreenAmount = 0
        assertEquals("green check", expectedGreenAmount, result.get(0).greenAmount)

        var expectedBlueAmount = 255
        assertEquals("blue check", expectedBlueAmount, result.get(0).blueAmount)
    }

    @Test
    fun findCorrectBaseColour()
    {
        /*
        In this test a base colour is the simplest RGB value for a colour
        for example #66ff33 is the hex value of a kind of light green
        this would be simplified to #00ff00 which is green but with a simple
        hex value
         */

        var test : colourMatcher = colourMatcher()


        var result : colour = test.setupToBasicColour(102, 255, 51)

        var expected : colour = colour()

        expected.createColour(0,255, 0)//standard green

        assertEquals("red check", expected.redAmount, result.redAmount)
        assertEquals("green check", expected.greenAmount, result.greenAmount)
        assertEquals("blue check", expected.blueAmount, result.blueAmount)
    }

    @Test
    fun createCorrectColourList()
    {
        var testColourList : ArrayList<colour> = ArrayList()

        var testColour1 : colour = colour()
        testColour1.createColour(255, 0,0)//colour red
        testColourList.add(testColour1)
        var testColour2 : colour = colour()
        //there should be no duplicate values so this should be deleted
        testColour2.createColour(255, 0,0)//colour red
        testColourList.add(testColour2)
        var testColour3 : colour = colour()
        testColour3.createColour(0, 0,255)//colour blue
        testColourList.add(testColour3)
        var testColour4 : colour = colour()
        testColour4.createColour(0, 255,0)//colour green
        testColourList.add(testColour4)

        var test : colourMatcher = colourMatcher()
        test.colours = ArrayList()

        //when the colour given to colourMatcher it is first added to the
        //list of matching colours as this colour will always match itself
        var testColour5 : colour = colour()
        testColour5.createColour(0, 0,0)//colour black
        test.colours.add(testColour5)

        test.addColour(testColourList)

        var result : ArrayList<colour> = test.colours


        assertEquals("check size",4, result.size)


    }

    //tests for colour matcher

}
