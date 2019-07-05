package com.example.nn4wchallenge.database.external

import android.util.JsonReader
import android.util.JsonToken
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


/*
The gets the data from the json files and converts it into something the
app can use
 */

class onlineDatabase () {

    private val CLOTHING_REGISTER_URL: String =
        "https://firebasestorage.googleapis.com/v0/b/nn4mfashion.appspot.com/o/clothing%20catalog.json?alt=media&token=7b84a00d-78f3-4a98-848e-504cd9dca636"

    private lateinit var reader: JsonReader

    @Throws(IOException::class)
    public fun getAvailableClothes(): ArrayList<searchItem> {
        var inStream: InputStream = getInputStream(CLOTHING_REGISTER_URL)

        reader = JsonReader(InputStreamReader(inStream, "UTF-8"))

        return getClothingList(reader)
    }

    @Throws(Exception::class)
    private fun getClothingList(reader: JsonReader): ArrayList<searchItem> {

        var clothingList: ArrayList<searchItem> = ArrayList()

        reader.beginObject()

        var name: String = ""

        while (reader.hasNext()) {

            name = reader.nextName()
            if (name == "clothing" && reader.peek() != JsonToken.NULL) {

                reader.beginArray()
                while (reader.hasNext()) {
                    clothingList.add(convertJsonToClass(reader))

                }
                reader.endArray()
            }

        }

        reader.endObject()
        reader.close()

        return clothingList
    }

    @Throws(IOException::class)
    private fun convertJsonToClass(reader: JsonReader): searchItem {
        var clothingItem: searchItem = searchItem()

        reader.beginObject()
        while (reader.hasNext()) {
            var name: String = reader.nextName()
            if (name == "colour") {

                clothingItem.colour = reader.nextString()

            } else if (name == "type") {

                clothingItem.type = reader.nextString()

            } else if (name == "season") {

                clothingItem.season = reader.nextString()

            } else if (name == "gender") {

                clothingItem.gender = reader.nextString()

            } else if (name == "age") {

                clothingItem.age = reader.nextString()

            } else if (name == "minSize") {

                clothingItem.minSize = reader.nextString()

            } else if (name == "maxSize") {

                clothingItem.maxSize = reader.nextString()

            }
            else if (name == "image") {

                clothingItem.imageURL = reader.nextString()

            } else if (name == "description") {

                clothingItem.descriptionURL = reader.nextString()

            } else {
                reader.skipValue()
            }
        }
        reader.endObject()

        return clothingItem
    }

    @Throws (Exception::class)
    private fun getInputStream(jsonURL: String): InputStream {
        var newInputStream: InputStream? = null

        val url = URL(jsonURL)
        var connection : HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.connect()

        newInputStream = connection.getInputStream()

        return newInputStream!!
    }

    @Throws(IOException::class)
    public fun isStreamEmpty(inStream: InputStream): Boolean {

        val reader = BufferedReader(InputStreamReader(inStream))

        var count = 0
        
        while (reader.readLine() != null)
        {
            count++
        }

        return count <= 1
        
    }

    //get item description
    @Throws (Exception::class)
    public fun getItemDescription(descriptionURL : String) : itemDescription
    {
        var returnedItemDescription : itemDescription = itemDescription()

        val userLocation = getLocation()

        var inStream: InputStream = getInputStream(descriptionURL)

        reader = JsonReader(InputStreamReader(inStream, "UTF-8"))


        reader.beginObject()

        while (reader.hasNext())
        {
            var name: String = reader.nextName()

            var notFound = true

            if(name == "name")
            {
                returnedItemDescription.name = reader.nextString()
                notFound = false
            }
            else if(name == "description")
            {
                returnedItemDescription.description = reader.nextString()
                notFound = false
            }

            /*these values would be double as that represents a price better than an int
             (i.e. £9.99), however all of the cost values
            don't have a decimal place which may cause errors if the app
            is looking for double but is only finding ints*/
            var cost : Int = 0
            var wasCost : Int = 0

            when (userLocation) {
                "UK" -> {
                    if (name == "cost")
                    {
                        cost = reader.nextInt()
                        returnedItemDescription.cost = cost.toDouble()
                        notFound = false
                    }
                    else if (name == "wascost")
                    {
                        //the value is often empty
                        var fromFile = reader.nextString()
                        if (fromFile == "")
                        {
                            wasCost = cost
                        }
                        else
                        {
                            wasCost = fromFile.toInt()
                        }

                        returnedItemDescription.setReduction(cost.toDouble(), wasCost.toDouble())
                        notFound = false
                    }
                }
                "EU" -> {
                    if (name == "costEUR")
                    {
                        cost = reader.nextInt()
                        returnedItemDescription.cost = cost.toDouble()
                        notFound = false
                    }
                    else if (name == "wascostEUR")
                    {
                        //the value is often empty
                        var fromFile = reader.nextString()
                        if (fromFile == "")
                        {
                            wasCost = cost
                        }
                        else
                        {
                            wasCost = fromFile.toInt()
                        }

                        returnedItemDescription.setReduction(cost.toDouble(), wasCost.toDouble())
                        notFound = false
                    }
                }
                "DEN"/*Denmark*/ ->
                {
                    //I have not idea what WER stands for
                    if (name == "costWER")
                    {
                        cost = reader.nextInt()
                        returnedItemDescription.cost = cost.toDouble()
                        notFound = false
                    }
                    else if (name == "wascostWER")
                    {
                        //the value is often empty
                        var fromFile = reader.nextString()
                        if (fromFile == "")
                        {
                            wasCost = cost
                        }
                        else
                        {
                            wasCost = fromFile.toInt()
                        }

                        returnedItemDescription.setReduction(cost.toDouble(), wasCost.toDouble())
                        notFound = false
                    }
                }
                "US" -> {
                    if (name == "costUSD")
                    {
                        cost = reader.nextInt()
                        returnedItemDescription.cost = cost.toDouble()
                        notFound = false
                    }
                    else if (name == "wascostUSD")
                    {
                        //the value is often empty
                        var fromFile = reader.nextString()
                        if (fromFile == "")
                        {
                            wasCost = cost
                        }
                        else
                        {
                            wasCost = fromFile.toInt()
                        }

                        returnedItemDescription.setReduction(cost.toDouble(), wasCost.toDouble())
                        notFound = false
                    }
                }
                "AU" -> {
                    if (name == "costAUD")
                    {
                        cost = reader.nextInt()
                        returnedItemDescription.cost = cost.toDouble()
                    }
                    else if (name == "wascostAUD")
                    {
                        //the value is often empty
                        var fromFile = reader.nextString()
                        if (fromFile == "")
                        {
                            wasCost = cost
                        }
                        else
                        {
                            wasCost = fromFile.toInt()
                        }

                        returnedItemDescription.setReduction(cost.toDouble(), wasCost.toDouble())
                        notFound = false
                    }
                }
                "SW"//i.e. sweden
                -> {
                    if (name == "costSEK")
                    {
                        cost = reader.nextInt()
                        returnedItemDescription.cost = cost.toDouble()
                        notFound = false
                    }
                    else if (name == "wascostSEK")
                    {
                        //the value is often empty
                        var fromFile = reader.nextString()
                        if (fromFile == "")
                        {
                            wasCost = cost
                        }
                        else
                        {
                            wasCost = fromFile.toInt()
                        }

                        returnedItemDescription.setReduction(cost.toDouble(), wasCost.toDouble())
                        notFound = false
                    }
                }
                else -> {
                    if (name == "costWEK")
                    {
                        cost = reader.nextInt()
                        returnedItemDescription.cost = cost.toDouble()
                        notFound = false
                    }
                    else if (name == "wascostWEK")
                    {
                        //the value is often empty
                        var fromFile = reader.nextString()
                        if (fromFile == "")
                        {
                            wasCost = cost
                        }
                        else
                        {
                            wasCost = fromFile.toInt()
                        }

                        returnedItemDescription.setReduction(cost.toDouble(), wasCost.toDouble())
                        notFound = false
                    }
                }
            }

            if (name == "allImages")
            {
                returnedItemDescription.images = getAllImages(reader)
                notFound = false
            }

            if (notFound)
            {
                //value in json file does not match any of the above
                reader.skipValue()
            }

        }

        reader.endObject()
        reader.close()

        return returnedItemDescription
    }

    private fun getAllImages(reader: JsonReader) : ArrayList<String>
    {
        var imageList : ArrayList<String> = ArrayList()

        reader.beginArray()

        while (reader.hasNext())
        {
            imageList.add(reader.nextString())
        }

        reader.endArray()

        return imageList
    }

    private fun getLocation() : String
    {
        /*
        This function would identify which country the user is in
        in order to change the currency value to match i.e. pound if in
        uk euro in france.
        This was not implemented as it would be out of scope for this
        prototype project.
         */

        return "UK"
    }


}
