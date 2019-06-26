package com.example.nn4wchallenge.database.external

import android.util.JsonReader
import android.widget.TextView
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import android.R
import android.content.Context
import android.util.JsonToken
import org.apache.http.client.methods.HttpUriRequest
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

/*
        try {
            if (reader != null) {
                getClothingList(reader)
            } else {
                testTXT.setText("reader equals null")
            }

        }
        catch(e : Exception)
        {
            testTXT.setText("READER 2 " + e.printStackTrace().toString())
        }
*/

        return getClothingList(reader)

    }

    public fun getClothingDescription(URL: String) {
        var inStream = getInputStream(URL)
    }

    @Throws(Exception::class)
    private fun getClothingList(reader: JsonReader): ArrayList<searchItem> {
        var count: Int = 0
        var jsonText: String = ""

        var clothingList: ArrayList<searchItem> = ArrayList()
        //try {
        //reader.beginArray()
        reader.beginObject()
        //reader.beginObject()

        var name: String = ""

        while (reader.hasNext()) {
        //if (reader.hasNext()) {
            //name = reader.nextName()
            //try {

            name = reader.nextName()
            if (/*reader.nextString()*/name == "clothing" && reader.peek() != JsonToken.NULL) {

                reader.beginArray()
                while (reader.hasNext()) {
                    clothingList.add(convertJsonToClass(reader))
                    //count++
                }
                reader.endArray()
            }
            /*}
            catch(e : Exception)
            {

                name = reader.toString()
                throw Exception("file location = $name")
            }*/
        }

        /*
            if (reader.hasNext())
            {
            reader.beginObject()

            jsonText = reader.nextName()*/
        reader.endObject()
        //reader.endObject()
        //reader.endArray()
        reader.close()

        //testTXT.setText("READER " + jsonText/*e.printStackTrace().toString()*/)
        /*}
        catch(e : Exception)
        {
            //testTXT.setText("READER " + count/*e.printStackTrace().toString()*/)
        }*/

        return clothingList
    }

    @Throws(IOException::class)
    private fun convertJsonToClass(reader: JsonReader): searchItem {
        var clothingItem: searchItem = searchItem()

        reader.beginObject()
        while (reader.hasNext()) {
            var name: String = reader.nextName()
            if (name == "colour") {
                //try {
                clothingItem.colour = reader.nextString()
                /*}
                catch(e : Exception)
                {
                    testTXT.setText("Colour " + e.printStackTrace().toString())
                }*/
            } else if (name == "type") {
                //try {
                clothingItem.type = reader.nextString()
                /*}
                catch(e : Exception)
                {
                    testTXT.setText("Type " + e.printStackTrace().toString())
                }*/
            } else if (name == "season") {
                //try {
                clothingItem.season = reader.nextString()
                /*}
                catch(e : Exception)
                {
                    testTXT.setText("Season " + e.printStackTrace().toString())
                }*/
            } else if (name == "gender") {
                //try{
                clothingItem.gender = reader.nextString()
                /*}
                catch(e : Exception)
                {
                    testTXT.setText("Gender " + e.printStackTrace().toString())
                }*/
            } else if (name == "age") {
                //try{
                clothingItem.age = reader.nextString()
                /*}
                catch(e : Exception)
                {
                    testTXT.setText("Age " + e.printStackTrace().toString())
                }*/
            } else if (name == "minSize") {
                //try{
                clothingItem.minSize = reader.nextString()
                /*}
                catch(e : Exception)
                {
                    testTXT.setText("Min Size " + e.printStackTrace().toString())
                }*/
            } else if (name == "maxSize") {
                //try{
                clothingItem.maxSize = reader.nextString()
                /*}
                catch(e : Exception)
                {
                    testTXT.setText("Max Size " + e.printStackTrace().toString())
                }*/
            }
            /*
            else if (name == "shoeSize")
            {
                try {
                    clothingItem.shoeSize = reader.nextInt()

            }
            catch(e : Exception)
            {
                testTXT.setText("READER " + e.printStackTrace().toString())
            }
            }*/
            else if (name == "image") {
                //try{
                clothingItem.imageURL = reader.nextString()
                /*}
                catch(e : Exception)
                {
                    testTXT.setText("Image " + e.printStackTrace().toString())
                }*/
            } else if (name == "description") {
                //try {
                clothingItem.descriptionURL = reader.nextString()
                /*}
                catch(e : Exception)
                {
                    testTXT.setText("Description " + e.printStackTrace().toString())
                }*/
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

        /*
        try {
            var httpCleint = DefaultHttpClient()
            var httpPost = HttpPost(URL)
            var httpResponse = httpCleint.execute(httpPost as HttpUriRequest?)
            var httpEntity = httpResponse.entity
            newInputStream = httpEntity.content
        } catch (e: UnsupportedEncodingException) {
            //testTXT.setText("1 " + e.printStackTrace().toString())
        } catch (e: ClientProtocolException) {
            //testTXT.setText("2 " + e.printStackTrace().toString())
            //e.printStackTrace()
        } catch (e: IOException) {
            //e.printStackTrace()
            //testTXT.setText("3 " + e.printStackTrace().toString())
        }*/

        return newInputStream!!
    }

    private fun getTestInputStream(): InputStream? {
        var newInputStream: InputStream? = null


        //newInputStream = context.getResources().openRawResource(android.R.raw.register)

        return newInputStream
    }
//}

    /**TESTER**/
    @Throws(IOException::class)
    public fun convertStreamToString(): String {

        var inStream: InputStream = getInputStream(CLOTHING_REGISTER_URL)

        val reader = BufferedReader(InputStreamReader(inStream))
        val sb = StringBuilder()


        var line: String = ""
        var okToRead = true
        //try {
        /*while (okToRead) {

            var newLine: String? = reader.readLine()
            if (reader.readLine() == null || newLine == null) {
                !okToRead
            } else {
                line = newLine as String
                sb.append(line).append('\n')
            }

        }*/

        var count = 0
        
        while (reader.readLine() != null)
        {
            count++
        }

        /*} catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            `is`.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }*/

        return "$count"
    }
}
