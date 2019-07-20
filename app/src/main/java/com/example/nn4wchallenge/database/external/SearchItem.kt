package com.example.nn4wchallenge.database.external

/*
Why this exists. The app has to search through a list of clothing, however not all the information is
necessary as a result this class contains only the information necessary for searching
 */
class SearchItem {

    var colour : String = ""
    var type : String = ""
    var season : String = ""
    var gender : String = ""
    var age : String = ""
    var minSize : String = ""
    var maxSize : String = ""
    //var shoeSize : Int = 0
    var imageURL : String = "" //url to image stored online
    var descriptionURL : String = "" //link to more information about the clothing


}