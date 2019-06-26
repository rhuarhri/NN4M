package com.example.nn4wchallenge

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.json.JSONException
import org.json.JSONObject

class jsonParser {

    private var TAG = "JsonParser"

    private var InStream : InputStream? = null
    private var JSObject : JSONObject? = null
    private var json : String = ""

@Throws (IOException::class)
    public fun convertStreamToString(`is`: InputStream): String {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()


        var line: String = ""
        var okToRead = true
        //try {
            while (okToRead) {

                var newLine : String? = reader.readLine()
                if (reader.readLine() == null || newLine == null)
                {
                    !okToRead
                }
                else{
                    line = newLine as String
                    sb.append(line).append('\n')
                }

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

        return sb.toString()
    }

    /*
    fun getJsonObject(URL : String) : JSONObject?
    {
        try{
            var httpCliet = DefaultHttpClient()
            var httpPost = HttpPost(URL)
            var httpResponse = httpCliet.execute(httpPost)
            var httpEntity = httpResponse.entity
            InStream = httpEntity.content
        }
        catch ( e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch ( e : ClientProtocolException) {
            e.printStackTrace()
        } catch ( e : IOException) {
            e.printStackTrace()
        }

        try
        {
            var reader = BufferedReader( InputStreamReader(InStream, "iso-8859-1"), 8)
            var stringBuilder = StringBuilder()
            var line : String? = null

            while ((reader.readLine()) != null)
            {
                stringBuilder.append(reader.readLine() + "\n")
            }

            InStream!!.close()

            json = stringBuilder.toString()
        }
        catch (e : Exception) {

        }

        try
        {
            JSObject = JSONObject(json)

        }
     catch ( e : JSONException)
    {

    }

        return JSObject
    }*/


}