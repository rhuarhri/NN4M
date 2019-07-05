package com.example.nn4wchallenge

import com.example.nn4wchallenge.clothingMatcherCode.clothingMatcher
import com.example.nn4wchallenge.colourMatcherCode.*
import com.example.nn4wchallenge.database.external.dataTranslation
import com.example.nn4wchallenge.database.external.onlineDatabase
import com.example.nn4wchallenge.database.external.searchItem
import com.example.nn4wchallenge.database.external.searchManager
import com.example.nn4wchallenge.database.internal.SetupManager
import com.example.nn4wchallenge.database.internal.clothing
import com.example.nn4wchallenge.database.internal.user
import org.junit.Test

import org.junit.Assert.*
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    //tests for colour matcher

    @Test
    fun testColourMatcher()
    {
        var colourM : colourMatcher = colourMatcher()

        colourM.matchColour(255, 0, 0) //red

        var result = colourM.doesColourMatch(0, 255, 255) //light blue

        var expected = true //as red and light blue should be contrasting colours

        assertEquals("colour matcher test ", expected, result)
    }

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

    //tests for setup activity's setup procedure
    @Test
    fun testSetupProcedure()
    {
        var setupM : SetupManager = SetupManager()

        val expectGender : Int = 2

        var resultGender = setupM.getTitleList(setupM.GENDER).size

        assertEquals("gender size test", expectGender, resultGender)

        val expectAge : Int = 4

        var resultAge = setupM.getTitleList(setupM.AGE).size

        assertEquals("age size test", expectAge, resultAge)

        val expectChest : Int = 7

        var resultChest = setupM.getTitleList(setupM.CHEST).size

        assertEquals("chest size test", expectChest, resultChest)

        val expectWaist : Int = 7

        var resultWaist = setupM.getTitleList(setupM.WAIST).size

        assertEquals("Waist size test", expectWaist, resultWaist)

        val expectShoe : Int = 9

        var resultShoe = setupM.getTitleList(setupM.SHOE).size

        assertEquals("gender size test", expectShoe, resultShoe)
    }

    //data translation tests
    @Test
    fun convertStringToColourTest()
    {
        var test : dataTranslation = dataTranslation()

        var testValue : String = "0x0000FF"

        test.StringToRGB(testValue)

        var expectedBlue : Int = 255

        var colour : Int = test.blueAmount

        assertEquals("colour test", expectedBlue, colour)

    }

    //end of data translation tests

    //search manager tests

    public fun setupUserInfo() : user
    {
        var testUser : user = user()

        testUser.userAge = "adult"
        testUser.userGender = "female"
        testUser.userShoeSize = 5
        testUser.userWaistMeasurement = 10
        testUser.userChestMeasurement = 10

        return testUser
    }

    public fun setupClothingInfo() : clothing
    {
        var testClothing : clothing = clothing()

        testClothing.clothingType = "dress"
        testClothing.clothingSeason = "summer"
        testClothing.clothingColorBlue = 0
        testClothing.clothingColorGreen = 0
        testClothing.clothingColorRed = 255
        testClothing.clothingImageLocation = "testImage"

        return testClothing
    }

    @Test
    fun searchManagerTest()
    {

        var clothingItems : ArrayList<searchItem> = ArrayList()

        var item1 : searchItem = searchItem()
        item1.age = "adult"
        item1.colour = "0x000000"//colour black
        item1.gender = "female"
        item1.type = "shoe"
        item1.minSize = "3"
        item1.maxSize = "10"
        item1.season = "summer"
        item1.imageURL = "test item 1 image location"
        item1.descriptionURL = "test item 1 description location"

        clothingItems.add(item1)

        var item2 : searchItem = searchItem()
        item2.age = "adult"
        item2.colour = "0x00FFFF"//colour light blue
        item2.gender = "female"
        item2.type = "shoe"
        item2.minSize = "3"
        item2.maxSize = "10"
        item2.season = "summer"
        item2.imageURL = "test item 2 image location"
        item2.descriptionURL = "test item 2 description location"

        clothingItems.add(item2)

        var item3 : searchItem = searchItem()
        item3.age = "adult"
        item3.colour = "0x00FFFF"//colour light blue
        item3.gender = "male"
        item3.type = "shoe"
        item3.minSize = "3"
        item3.maxSize = "10"
        item3.season = "summer"
        item3.imageURL = "test item 3 image location"
        item3.descriptionURL = "test item 3 description location"

        clothingItems.add(item3)

        var testSearch : searchManager = searchManager()

        testSearch.setupUserInfo(setupUserInfo())

        testSearch.setupClothingInfo(setupClothingInfo())

        var testResult = testSearch.search(clothingItems)

        val expected = "test item 2 image location"

        val expected2 = false

        assertEquals("search result empty ", expected2, testResult.isEmpty())

        assertEquals("search item ", expected, testResult[0].image)
    }

    @Test
    fun searchManagerTestNoPossibleMatches()
    {

        var clothingItems : ArrayList<searchItem> = ArrayList()

        var item1 : searchItem = searchItem()
        item1.age = "adult"
        item1.colour = "0x000000"//colour black
        item1.gender = "female"
        item1.type = "shoe"
        item1.minSize = "3"
        item1.maxSize = "10"
        item1.season = "summer"
        item1.imageURL = "test item 1 image location"
        item1.descriptionURL = "test item 1 description location"

        clothingItems.add(item1)

        var item2 : searchItem = searchItem()
        item2.age = "adult"
        item2.colour = "0x00FFFF"//colour light blue
        item2.gender = "female"
        item2.type = "shoe"
        item2.minSize = "3"
        item2.maxSize = "10"
        item2.season = "winter"
        item2.imageURL = "test item 2 image location"
        item2.descriptionURL = "test item 2 description location"

        clothingItems.add(item2)

        var item3 : searchItem = searchItem()
        item3.age = "adult"
        item3.colour = "0x00FFFF"//colour light blue
        item3.gender = "male"
        item3.type = "shoe"
        item3.minSize = "3"
        item3.maxSize = "10"
        item3.season = "summer"
        item3.imageURL = "test item 3 image location"
        item3.descriptionURL = "test item 3 description location"

        clothingItems.add(item3)

        var testSearch : searchManager = searchManager()

        testSearch.setupUserInfo(setupUserInfo())

        testSearch.setupClothingInfo(setupClothingInfo())

        var testResult = testSearch.search(clothingItems)


        val expected = false

        assertEquals("search result empty ", expected, testResult.isEmpty())


    }

    @Test
    fun searchManagerTestMatchColour()
    {

        var item1 : searchItem = searchItem()
        item1.age = "adult"
        item1.colour = "0x00FFFF"//colour light blue
        item1.gender = "female"
        item1.type = "shoe"
        item1.minSize = "3"
        item1.maxSize = "10"
        item1.season = "summer"
        item1.imageURL = "test item 1 image location"
        item1.descriptionURL = "test item 1 description location"

        var testSearch : searchManager = searchManager()

        testSearch.setupUserInfo(setupUserInfo())

        testSearch.setupClothingInfo(setupClothingInfo())

        var result = testSearch.matchesColor(item1.colour)

        val expected = true

        assertEquals("search manager colour matcher test ", expected, result)
    }

    @Test
    fun searchManagerTestMatchUserDescription()
    {

        var item1 : searchItem = searchItem()
        item1.age = "adult"
        item1.colour = "0x000000"//colour black
        item1.gender = "female"
        item1.type = "shoe"
        item1.minSize = "3"
        item1.maxSize = "10"
        item1.season = "summer"
        item1.imageURL = "test item 1 image location"
        item1.descriptionURL = "test item 1 description location"


        var testSearch : searchManager = searchManager()

        testSearch.setupUserInfo(setupUserInfo())

        testSearch.setupClothingInfo(setupClothingInfo())

        var result = testSearch.matchesUserDescription(item1)

        val expected = true

        assertEquals("search manager user description matcher test ", expected, result)
    }

    @Test
    fun searchManagerTestMatchUserSize()
    {

        var item1 : searchItem = searchItem()
        item1.age = "adult"
        item1.colour = "0x000000"//colour black
        item1.gender = "female"
        item1.type = "shoe"
        item1.minSize = "3"
        item1.maxSize = "10"
        item1.season = "summer"
        item1.imageURL = "test item 1 image location"
        item1.descriptionURL = "test item 1 description location"


        var testSearch : searchManager = searchManager()

        testSearch.setupUserInfo(setupUserInfo())

        testSearch.setupClothingInfo(setupClothingInfo())

        var result = testSearch.doesFit(item1.type, item1.maxSize.toInt(), item1.minSize.toInt())

        val expected = true

        assertEquals("search manager user size matcher test ", expected, result)
    }

    @Test
    fun searchManagerTestUserDescription()
    {
        var item1 : searchItem = searchItem()
        item1.age = "adult"
        item1.colour = "0x000000"//colour black
        item1.gender = "female"
        item1.type = "shoe"
        item1.minSize = "3"
        item1.maxSize = "10"
        item1.season = "summer"
        item1.imageURL = "test item 1 image location"
        item1.descriptionURL = "test item 1 description location"


        var testSearch : searchManager = searchManager()

        testSearch.setupUserInfo(setupUserInfo())

        testSearch.setupClothingInfo(setupClothingInfo())

        var result = testSearch.matchesUserDescription(item1)

        val expected = true

        assertEquals("search manager user description test", expected, result)
    }

//end of search manager tests

    //clothing type matcher tests
    @Test
    fun testClothingMatcher()
    {
        var testMatcher : clothingMatcher = clothingMatcher()

        var result = testMatcher.matcher("dress", "shoe")

        var expected = true

        assertEquals("clothing matcher test ", expected, result)
    }

}
