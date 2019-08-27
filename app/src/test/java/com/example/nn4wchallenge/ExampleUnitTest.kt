package com.example.nn4wchallenge

import com.example.nn4wchallenge.clothingMatcherCode.ClothingMatcher
import com.example.nn4wchallenge.colourMatcherCode.*
import com.example.nn4wchallenge.database.external.*
import com.example.nn4wchallenge.database.internal.SetupManager
import com.example.nn4wchallenge.database.internal.clothingDatabaseCode.Clothing
import com.example.nn4wchallenge.database.internal.userDatabaseCode.User
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
    fun testColourMatcher()
    {
        val colourM = ColourMatcher()

        colourM.matchColour(255, 0, 0) //red

        val result = colourM.doesColourMatch(0, 255, 255) //light blue

        val expected = true //as red and light blue should be contrasting colours

        assertEquals("colour matcher test ", expected, result)
    }

    @Test
    fun findCorrectAdjacentColoursForBaseColour()
    {
        /*
        In this test a base colour is either red, green or blue
         */
        val test = ColourMatcher()
        test.colours = ArrayList()

        val testcolour = Colour()
        testcolour.createColour(0,0,255)//colour blue

        test.findAdjacent(testcolour)

        val testColour1 = Colour()
        val expectedColour : ArrayList<Colour> = ArrayList()
        testColour1.createColour(0, 255, 255)
        expectedColour.add(testColour1)//light blue
        val testColour2 = Colour()
        testColour2.createColour(255, 0, 255)
        expectedColour.add(testColour2)//pink


        val result = test.colours

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

        val test = ColourMatcher()
        test.colours = ArrayList()

        val testcolour = Colour()
        testcolour.createColour(255,255,0)//colour yellow

        test.findAdjacent(testcolour)

        val testColour1 = Colour()
        val expectedColour : ArrayList<Colour> = ArrayList()
        testColour1.createColour(255, 0, 0)
        expectedColour.add(testColour1)//green
        val testColour2 = Colour()
        testColour2.createColour(0, 255, 0)
        expectedColour.add(testColour2)//red

        val result = test.colours

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

        val test = ColourMatcher()
        test.colours = ArrayList()

        val testcolour = Colour()
        testcolour.createColour(0,0,0)//colour black

        test.findAdjacent(testcolour)

        val testColour = Colour()
        val expectedColour : ArrayList<Colour> = ArrayList()
        testColour.createColour(255, 255, 255)
        expectedColour.add(testColour)//white

        val result = test.colours

        assertEquals("red check 1", expectedColour.get(0).redAmount, result.get(0).redAmount)
        assertEquals("green check 1", expectedColour.get(0).greenAmount, result.get(0).greenAmount)
        assertEquals("blue check 1", expectedColour.get(0).blueAmount, result.get(0).blueAmount)
    }

    @Test
    fun findCorrectContrastingColours()
    {
        val test = ColourMatcher()
        test.colours = ArrayList()

        val testcolour = Colour()
        testcolour.createColour(0,255,0)//colour green

        test.findContrasting(testcolour)

        val result = test.colours

        val expectedRedAmount = 255
        assertEquals("red check", expectedRedAmount, result.get(0).redAmount)

        val expectedGreenAmount = 0
        assertEquals("green check", expectedGreenAmount, result.get(0).greenAmount)

        val expectedBlueAmount = 255
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

        val test = ColourMatcher()

        val result : Colour = test.setupToBasicColour(102, 255, 51)

        val expected = Colour()

        expected.createColour(0,255, 0)//standard green

        assertEquals("red check", expected.redAmount, result.redAmount)
        assertEquals("green check", expected.greenAmount, result.greenAmount)
        assertEquals("blue check", expected.blueAmount, result.blueAmount)
    }

    @Test
    fun createCorrectColourList()
    {
        val testColourList : ArrayList<Colour> = ArrayList()

        val testColour1 = Colour()
        testColour1.createColour(255, 0,0)//colour red
        testColourList.add(testColour1)
        val testColour2 = Colour()
        //there should be no duplicate values so this should be deleted
        testColour2.createColour(255, 0,0)//colour red
        testColourList.add(testColour2)
        val testColour3 = Colour()
        testColour3.createColour(0, 0,255)//colour blue
        testColourList.add(testColour3)
        val testColour4 = Colour()
        testColour4.createColour(0, 255,0)//colour green
        testColourList.add(testColour4)

        val test = ColourMatcher()
        test.colours = ArrayList()

        //when the colour given to colourMatcher it is first added to the
        //list of matching colours as this colour will always match itself
        val testColour5 = Colour()
        testColour5.createColour(0, 0,0)//colour black
        test.colours.add(testColour5)

        test.addColour(testColourList)

        val result : ArrayList<Colour> = test.colours


        assertEquals("check size",4, result.size)

    }

    //tests for colour matcher

    //tests for setup activity's setup procedure
    @Test
    fun testSetupProcedure()
    {
        val setupM = SetupManager()

        val expectGender = 2

        val resultGender = setupM.getTitleList(setupM.GENDER).size

        assertEquals("gender size test", expectGender, resultGender)

        val expectAge = 4

        val resultAge = setupM.getTitleList(setupM.AGE).size

        assertEquals("age size test", expectAge, resultAge)

        val expectChest = 7

        val resultChest = setupM.getTitleList(setupM.CHEST).size

        assertEquals("chest size test", expectChest, resultChest)

        val expectWaist = 7

        val resultWaist = setupM.getTitleList(setupM.WAIST).size

        assertEquals("Waist size test", expectWaist, resultWaist)

        val expectShoe = 9

        val resultShoe = setupM.getTitleList(setupM.SHOE).size

        assertEquals("gender size test", expectShoe, resultShoe)
    }

    //data translation tests
    @Test
    fun convertStringToColourTest()
    {
        val test = DataTranslation()

        val testValue = "ffff0000"

        test.stringToRGB(testValue)

        val expectedRed = 255

        val colour : Int = test.redAmount

        assertEquals("colour test", expectedRed, colour)

    }

    @Test
    fun convertRGBToHexStringTest()
    {
        val test = DataTranslation()

        val testRedAmount = 255
        val testGreenAmount = 5
        val testBlueAmount = 0

        val hexString = test.rgbToHexString(testRedAmount, testGreenAmount, testBlueAmount)

        test.stringToRGB(hexString)

        val expectedRed = 255
        val expectedGreen = 5
        val expectedBlue = 0

        val red : Int = test.redAmount
        val green : Int = test.greenAmount
        val blue : Int = test.blueAmount

        assertEquals("hex string test red", expectedRed, red)
        assertEquals("hex string test green", expectedGreen, green)
        assertEquals("hex string test blue", expectedBlue, blue)
    }

    //the threads have problems with converting array lists to arrays hence why this exists
    @Test
    fun convertDoubleArrayTest()
    {
        val input : ArrayList<Double?> = ArrayList()
        input.add(12.0)
        input.add(null)
        input.add(6.5)

        val dataConverter = DataTranslation()

        val result : DoubleArray = dataConverter.toDoubleArray(input)

        val expected1 = 12.0
        val expected2 = 0.0
        val expected3 = 6.5

        assertEquals("test 1", expected1, result[0], 0.0)
        assertEquals("test 2", expected2, result[1], 0.0)
        assertEquals("test 3", expected3, result[2], 0.0)

    }

    @Test
    fun convertIntArrayTest()
    {
        val input : ArrayList<Int?> = ArrayList()
        input.add(12)
        input.add(null)
        input.add(6)

        val dataConverter = DataTranslation()

        val result : IntArray = dataConverter.toIntArray(input)

        val expected1 = 12
        val expected2 = 0
        val expected3 = 6

        assertEquals("test 1", expected1, result[0])
        assertEquals("test 2", expected2, result[1])
        assertEquals("test 3", expected3, result[2])
    }

    @Test
    fun convertStringArrayTest()
    {
        val input : ArrayList<String?> = ArrayList()
        input.add("A")
        input.add(null)
        input.add("B")

        val dataConverter = DataTranslation()

        val result : Array<String> = dataConverter.toStringArray(input)

        val expected1 = "A"
        val expected2 = ""
        val expected3 = "B"

        assertEquals("test 1", expected1, result[0])
        assertEquals("test 2", expected2, result[1])
        assertEquals("test 3", expected3, result[2])
    }

    //end of data translation tests

    //search manager tests

    private fun setupUserInfo() : User
    {
        val testUser = User()

        testUser.userAge = "adult"
        testUser.userGender = "female"
        testUser.userShoeSize = 5
        testUser.userWaistMeasurement = 10
        testUser.userChestMeasurement = 10

        return testUser
    }

    private fun setupClothingInfo() : Clothing
    {
        val testClothing = Clothing()

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

        val clothingItems : ArrayList<SearchItem> = ArrayList()

        val item1 = SearchItem()
        item1.age = "adult"
        item1.colour = "0x000000"//colour black
        item1.gender = "female"
        item1.type = "shoes"
        item1.minSize = "3"
        item1.maxSize = "10"
        item1.season = "summer"
        item1.imageURL = "test item 1 image location"
        item1.descriptionURL = "test item 1 description location"

        clothingItems.add(item1)

        val item2 = SearchItem()
        item2.age = "adult"
        item2.colour = "0x00FFFF"//colour light blue
        item2.gender = "female"
        item2.type = "shoes"
        item2.minSize = "3"
        item2.maxSize = "10"
        item2.season = "summer"
        item2.imageURL = "test item 2 image location"
        item2.descriptionURL = "test item 2 description location"

        clothingItems.add(item2)

        val item3 = SearchItem()
        item3.age = "adult"
        item3.colour = "0x00FFFF"//colour light blue
        item3.gender = "male"
        item3.type = "shoes"
        item3.minSize = "3"
        item3.maxSize = "10"
        item3.season = "summer"
        item3.imageURL = "test item 3 image location"
        item3.descriptionURL = "test item 3 description location"

        clothingItems.add(item3)

        val testSearch = SearchManager()

        testSearch.setupUserInfo(setupUserInfo())

        testSearch.setupClothingInfo(setupClothingInfo())

        val testResult = testSearch.search(clothingItems)

        val expected = "test item 2 image location"

        val expected2 = false

        assertEquals("search result empty ", expected2, testResult.isEmpty())

        assertEquals("search item ", expected, testResult[0].image)
    }

    @Test
    fun searchManagerTestNoPossibleMatches()
    {

        val clothingItems : ArrayList<SearchItem> = ArrayList()

        val item1 = SearchItem()
        item1.age = "adult"
        item1.colour = "0x000000"//colour black
        item1.gender = "female"
        item1.type = "shoes"
        item1.minSize = "3"
        item1.maxSize = "10"
        item1.season = "summer"
        item1.imageURL = "test item 1 image location"
        item1.descriptionURL = "test item 1 description location"

        clothingItems.add(item1)

        val item2 = SearchItem()
        item2.age = "adult"
        item2.colour = "0x00FFFF"//colour light blue
        item2.gender = "female"
        item2.type = "shoes"
        item2.minSize = "3"
        item2.maxSize = "10"
        item2.season = "winter"
        item2.imageURL = "test item 2 image location"
        item2.descriptionURL = "test item 2 description location"

        clothingItems.add(item2)

        val item3 = SearchItem()
        item3.age = "adult"
        item3.colour = "0x00FFFF"//colour light blue
        item3.gender = "male"
        item3.type = "shoes"
        item3.minSize = "3"
        item3.maxSize = "10"
        item3.season = "summer"
        item3.imageURL = "test item 3 image location"
        item3.descriptionURL = "test item 3 description location"

        clothingItems.add(item3)

        val testSearch = SearchManager()

        testSearch.setupUserInfo(setupUserInfo())

        testSearch.setupClothingInfo(setupClothingInfo())

        val testResult = testSearch.search(clothingItems)

        val expected = true

        assertEquals("search result empty ", expected, testResult.isEmpty())

    }

    @Test
    fun searchManagerSearchBasedOnColour()
    {
        val clothingItems : ArrayList<SearchItem> = ArrayList()

        val item1 = SearchItem()
        item1.age = "adult"
        item1.colour = "0x000000"//colour black
        item1.gender = "female"
        item1.type = "shoes"
        item1.minSize = "3"
        item1.maxSize = "10"
        item1.season = "summer"
        item1.imageURL = "test item 1 image location"
        item1.descriptionURL = "test item 1 description location"

        clothingItems.add(item1)

        val item2 = SearchItem()
        item2.age = "adult"
        item2.colour = "0x00FFFF"//colour light blue
        item2.gender = "female"
        item2.type = "shoes"
        item2.minSize = "3"
        item2.maxSize = "10"
        item2.season = "summer"//"winter"
        item2.imageURL = "test item 2 image location"
        item2.descriptionURL = "test item 2 description location"

        clothingItems.add(item2)

        val item3 = SearchItem()
        item3.age = "adult"
        item3.colour = "0x00FFFF"//colour light blue
        item3.gender = "female"
        item3.type = "shoes"
        item3.minSize = "3"
        item3.maxSize = "10"
        item3.season = "summer"
        item3.imageURL = "test item 3 image location"
        item3.descriptionURL = "test item 3 description location"

        clothingItems.add(item3)

        val testSearch = SearchManager()

        testSearch.setupUserInfo(setupUserInfo())

        //testSearch.setupClothingInfo(setupClothingInfo())

        val testResult = testSearch.searchByColour(clothingItems, "ffff0000")

        val expected = false

        assertEquals("search by colour test result not empty ", expected, testResult.isEmpty())

        val expected2 = "test item 2 image location"

        assertEquals("search by colour image found ", expected2, testResult[0].image)

    }

    @Test
    fun searchManagerFilterTest()
    {
        val testSearch = SearchManager()

        val testItems : ArrayList<SearchManager.MatchedPairs> = ArrayList()

        val item1 : SearchManager.MatchedPairs = SearchManager.MatchedPairs()
        item1.createPair("test image 1", "test description 1")
        item1.addAditionalInfo("shoes", "summer")
        testItems.add(item1)

        val item2 : SearchManager.MatchedPairs = SearchManager.MatchedPairs()
        item2.createPair("test image 2", "test description 2")
        item2.addAditionalInfo("shoes", "winter")
        testItems.add(item2)

        val item3 : SearchManager.MatchedPairs = SearchManager.MatchedPairs()
        item3.createPair("test image 3", "test description 3")
        item3.addAditionalInfo("dress", "summer")
        testItems.add(item3)

        val item4 : SearchManager.MatchedPairs = SearchManager.MatchedPairs()
        item3.createPair("test image 4", "test description 4")
        testItems.add(item4)

        //the items above would be the result from the search by colour function
        //this may need to be filtered in order to get more accurate matches
        //Test
        //find match for summer dress with should by summer shoes

        val type : String = "dress"
        val season : String = "summer"

        val testResult = testSearch.filterSearchResult(testItems, type, season)

        val expected = false

        assertEquals("search filter test result not empty ", expected, testResult.isEmpty())

        val expected2 = "test image 1"

        assertEquals("search filter image found ", expected2, testResult[0].image)

    }

    @Test
    fun searchManagerTestMatchColour()
    {

        val item1 = SearchItem()
        item1.age = "adult"
        item1.colour = "0x00FFFF"//colour light blue
        item1.gender = "female"
        item1.type = "shoes"
        item1.minSize = "3"
        item1.maxSize = "10"
        item1.season = "summer"
        item1.imageURL = "test item 1 image location"
        item1.descriptionURL = "test item 1 description location"

        val testSearch = SearchManager()

        testSearch.setupUserInfo(setupUserInfo())

        var testClothing = setupClothingInfo()

        testSearch.setupClothingInfo(testClothing)

        val red : Int = 255
        val green : Int = 0
        val blue : Int = 0

        val result = testSearch.matchesColor(
            item1.colour, testClothing.clothingColorRed, testClothing.clothingColorGreen, testClothing.clothingColorBlue
        )

        val expected = true

        assertEquals("search manager colour matcher test ", expected, result)
    }

    @Test
    fun searchManagerTestMatchUserDescription()
    {

        val item1 = SearchItem()
        item1.age = "adult"
        item1.colour = "0x000000"//colour black
        item1.gender = "female"
        item1.type = "shoes"
        item1.minSize = "3"
        item1.maxSize = "10"
        item1.season = "summer"
        item1.imageURL = "test item 1 image location"
        item1.descriptionURL = "test item 1 description location"


        val testSearch = SearchManager()

        testSearch.setupUserInfo(setupUserInfo())

        testSearch.setupClothingInfo(setupClothingInfo())

        val result = testSearch.matchesUserDescription(item1)

        val expected = true

        assertEquals("search manager user description matcher test ", expected, result)
    }

    @Test
    fun searchManagerTestMatchUserSize()
    {

        val item1 = SearchItem()
        item1.age = "adult"
        item1.colour = "0x000000"//colour black
        item1.gender = "female"
        item1.type = "shoes"
        item1.minSize = "3"
        item1.maxSize = "10"
        item1.season = "summer"
        item1.imageURL = "test item 1 image location"
        item1.descriptionURL = "test item 1 description location"

        val testSearch = SearchManager()

        testSearch.setupUserInfo(setupUserInfo())

        testSearch.setupClothingInfo(setupClothingInfo())

        val result = testSearch.doesFit(item1.type, item1.maxSize.toInt(), item1.minSize.toInt())

        val expected = true

        assertEquals("search manager user size matcher test ", expected, result)
    }

    @Test
    fun searchManagerTestUserDescription()
    {
        val item1 = SearchItem()
        item1.age = "adult"
        item1.colour = "0x000000"//colour black
        item1.gender = "female"
        item1.type = "shoes"
        item1.minSize = "3"
        item1.maxSize = "10"
        item1.season = "summer"
        item1.imageURL = "test item 1 image location"
        item1.descriptionURL = "test item 1 description location"

        val testSearch = SearchManager()

        testSearch.setupUserInfo(setupUserInfo())

        testSearch.setupClothingInfo(setupClothingInfo())

        val result = testSearch.matchesUserDescription(item1)

        val expected = true

        assertEquals("search manager user description test", expected, result)
    }

//end of search manager tests

    //clothing type matcher tests
    @Test
    fun testClothingMatcher()
    {
        val testMatcher = ClothingMatcher()

        val result = testMatcher.matcher("dress", "shoes")

        val expected = true

        assertEquals("clothing matcher test ", expected, result)
    }

    //end of clothing matcher tests

    //item description tests
    @Test
    fun testNoReduction()
    {
        val testItem = ItemDescription()

        //no change in price so no reduction
        testItem.setReduction(22.0, 22.0)

        val expected = 0

        val result : Int = testItem.reduction

        assertEquals("no reduction test", expected, result)
    }

    @Test
    fun test10PercentReduction()
    {
        val testItem = ItemDescription()

        testItem.setReduction(90.0, 100.0)

        val expected : Int = 10

        val result : Int = testItem.reduction

        assertEquals("10 percent reduction test", expected, result)
    }


}
