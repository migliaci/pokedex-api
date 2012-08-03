package com.ign.pokedex
import com.mongodb.casbah.Imports._
import com.mongodb.util.JSON
import javax.servlet.http.HttpServletResponse

/**
 * Created with IntelliJ IDEA.
 * User: mmigliacio
 * Date: 8/3/12
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */

object PokedexUtils {

  def connectToDB(obj_id: String, obj_val: String, obj_type: String, response: HttpServletResponse) :String =  {
    val mongoColl = MongoConnection()("pokedex")("test_data")
    var returnedItem =""

    //save to the DB

    if (obj_type == "pokemon"){
      mongoColl += PokedexTestGenerator.getTestPokemon()
    } else if (obj_type == "move") {
      mongoColl += PokedexTestGenerator.getTestMove()
    }

    mongoColl.find()


    val q = MongoDBObject("pokemonId" -> "001BulbasaurV")
    for (x <- mongoColl.find(q)) returnedItem = JSON.serialize(x)

    cleanupDB(mongoColl)
    returnedItem

  }


  def cleanupDB(m: MongoCollection) = {
    m.drop()
  }

  def executeMultipleQuery(mongoColl: MongoCollection, objectToQuery: MongoDBObject): String = {
    var returnedItem = ""
    var count = 0;
    val results = mongoColl.find(objectToQuery)

    if (results.length == 0) {
      println("Returning zero elements")
      //NOTE:  Need to discuss handling of what happens when no results are found.  Special JSON object?
      returnedItem = "{}"
      returnedItem
    } else if (results.length == 1){
      for (x <- results) {
        returnedItem += JSON.serialize(x)
      }
      returnedItem
    } else {
      for (x <- results) {
        if (count == 0) {
          returnedItem += ("[" + JSON.serialize(x) + ",")
        } else {
          returnedItem += JSON.serialize(x) + ","
        }
        count += 1;
      }

      if (returnedItem != "") {
        returnedItem = returnedItem.substring(0, returnedItem.length - 1)
        returnedItem += "]";
      }

    }

    returnedItem
  }

}
