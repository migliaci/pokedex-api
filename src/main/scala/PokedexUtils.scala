package com.ign.pokedex
import com.mongodb.casbah.Imports._
import com.mongodb.QueryBuilder
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

  val HP_NAME = ""
  val ATK_NAME = ""
  val DEF_NAME =""
  val SPATK_NAME = ""
  val SPDEF_NAME = ""

  def executeMultipleQuery(mongoColl: MongoCollection, objectToQuery: MongoDBObject): String = {
    var returnedItem = ""
    val results = mongoColl.find(objectToQuery)

    returnedItem = this.computeJSON(results)
    returnedItem
  }

  def computeJSON(results : MongoCursor): String = {
    var returnedItem = ""
    var count = 0;
    if (results.length == 0) {
      returnedItem = V3Utils.generateErrorJSON("Query returned zero elements.  Invalid parameter specified.")
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
