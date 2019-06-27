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
}
