package com.ign.pokedex
import com.mongodb.casbah.MongoConnection
import org.scalatra.ScalatraServlet

/**
 * Created with IntelliJ IDEA.
 * User: mmigliacio
 * Date: 8/11/12
 * Time: 11:34 AM
 * To change this template use File | Settings | File Templates.
 */

object V3Utils {

  val INVALID_INT_PARAMETER = -1
  val INVALID_STRING_PARAMETER = ""
  val INVALID_NUMBER_FORMAT_PARAMETER = -2
  val HALT_CODE = 404
  val PARMS_DO_NOT_EXIST_MESSAGE = "Required parameters do not exist.  URL is malformed."
  val MINIMUM_COUNT = 20
  val MINIMUM_START_INDEX = 0

  def processObjectEndpointWithParameters(objectString: String, params: Map[String, String], req: APIRequest, mongo:MongoConnection): String = {

    var count = 0
    var startIndex = 0
    var resultingJSON = ""

    if(req.count <= INVALID_INT_PARAMETER || req.startIndex <= INVALID_INT_PARAMETER) {
      resultingJSON = generateErrorJSON("Invalid parameters detected. URL is malformed.")
      return resultingJSON
    }

    if(req.count != INVALID_INT_PARAMETER && req.count != INVALID_NUMBER_FORMAT_PARAMETER && req.count != 0) {
      count = req.count
    } else {
      count = MINIMUM_COUNT
    }

    if (req.startIndex != INVALID_INT_PARAMETER && req.count != INVALID_NUMBER_FORMAT_PARAMETER) {
      startIndex = req.startIndex
    }  else {
      startIndex = MINIMUM_START_INDEX
    }

    resultingJSON = QueryManager.Query_ObjectByParameters(objectString, startIndex, count, req.fields, req.sortBy, req.sortOrder, mongo)
    resultingJSON
  }



  def processComplexObjectEndpointWithParameters(objectString:String, queryParm : String, queryParmValue: Int, params: Map[String, String], req: APIRequest, mongo:MongoConnection): String = {

    var count = 0
    var startIndex = 0
    var resultingJSON = ""

    if(req.count <= INVALID_INT_PARAMETER || req.startIndex <= INVALID_INT_PARAMETER) {
      resultingJSON = generateErrorJSON("Invalid parameters detected. URL is malformed.")
      return resultingJSON
    }

    if(req.count != INVALID_INT_PARAMETER && req.count != INVALID_NUMBER_FORMAT_PARAMETER && req.count != 0) {
      count = req.count
    } else {
      count = MINIMUM_COUNT
    }

    if (req.startIndex != INVALID_INT_PARAMETER && req.count != INVALID_NUMBER_FORMAT_PARAMETER) {
      startIndex = req.startIndex
    }  else {
      startIndex = MINIMUM_START_INDEX
    }

    resultingJSON = QueryManager.Query_ComplexObjectByParameters(objectString, queryParm, queryParmValue, startIndex, count, req.fields, req.sortBy, req.sortOrder, mongo)
    resultingJSON
  }

  def generateErrorJSON(message : String) : String = {
    return  "{ " + "\"" + "IGNPokedexError" + "\"" + ":" + "\"" + message + "\"" + " }"
  }

}
