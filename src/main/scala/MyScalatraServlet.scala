package com.ign.pokedex

/**
 * Created with IntelliJ IDEA.
 * User: mmigliacio
 * Date: 7/31/12
 * Time: 6:49 PM
 * To change this template use File | Settings | File Templates.
 */

import org.scalatra._
import com.mongodb.casbah.Imports._
import com.mongodb.util.JSON
import PokedexUtils._
import scala.util.control.Breaks._
import APIRequest._
import java.lang.NumberFormatException

class MyScalatraServlet extends ScalatraServlet {


  var mongo = MongoConnection()

  get("/") {
    <html>
      <body>
        <h1>Hello, Unova!</h1>
        <img src="http://www.vgblogger.com/wp-content/uploads/2006/11/30/ShowYouPokemans.jpg" alt="Pokemon" /><br></br>
        <h2>Say hello to our Pokemans.</h2>
      </body>
    </html>
  }

  get("/pokemon"){
    val req = APIRequest(params.toMap[String,String])
    val returnValue = validateResults(V3Utils.processObjectEndpointWithParameters("pokemon", params.toMap[String,String], req, mongo))
    response.getWriter.write(returnValue)
  }


  get("/pokemon/:objectId") { //changed objectId to object_id
    validateParameterLength(1, params.size)
    val id = validateObjectIdParameter("objectId")
    val returnValue = validateResults(QueryManager.Query_PokemonByObjectId(id, mongo))
    response.getWriter.write(returnValue)

  }

  get("/pokemon/national-id") { //changed nationalId to national_id
    failWithError("Required parameters do not exist.  URL is malformed.")
  }
  //don't camelcase url
  get("/pokemon/national-id/:nationalid") {   //changed nationalId to national_id
    validateParameterLength(1, params.size)
    val id = validateIntParameter("nationalid")
    val returnValue = validateResults(QueryManager.Query_PokemonByNationalId(id, mongo))
    response.getWriter.write(returnValue)
  }

  get("/pokemon/generation/:generation") {
    val gen = validateIntParameter("generation")
    val req = APIRequest(params.toMap[String,String])
    val returnValue = validateResults(V3Utils.processComplexObjectEndpointWithParameters("pokemon", "metadata.generation", gen, params.toMap[String,String], req, mongo))
    response.getWriter.write(returnValue)
  }

  get("/pokemon/slug") {
    failWithError("Required parameters do not exist.  URL is malformed.")
  }

  get("/pokemon/slug/:slug") {
    validateParameterLength(1, params.size)
    val slug:String = validateStringParameter("slug")
    val returnValue = validateResults(QueryManager.Query_PokemonByName(slug.toLowerCase, mongo))
    response.getWriter.write(returnValue)
  }

  get("/pokemon/national-id/:nationalId/generation/:generation") {
    validateParameterLength(2, params.size)
    val id:Int = validateIntParameter("nationalId")
    val generation:Int = validateIntParameter("generation")
    val returnValue = validateResults(QueryManager.Query_PokemonByNationalIdAndGeneration(id, generation, mongo))
    response.getWriter.write(returnValue)
  }

  get("/pokemon/slug/:slug/generation/:generation") {
    validateParameterLength(2, params.size)
    val slug:String = validateStringParameter("slug")
    val generation:Int = validateIntParameter("generation")
    val returnValue = validateResults(QueryManager.Query_PokemonByNameAndGeneration(slug.toLowerCase, generation, mongo))
    response.getWriter.write(returnValue)
  }

  get("/pokemon/slug/:slug/generation") {
    failWithError("Required parameters do not exist.  URL is malformed.")
  }

  get("/pokemon/slug/generation/:generation") {
    failWithError("Required parameters do not exist.  URL is malformed.")
  }

  get("/pokemon/national-id/:nationalId/generation") {
    failWithError("Required parameters do not exist.  URL is malformed.")
  }

  get("/pokemon/national-id/generation/:generation") {
    failWithError("Required parameters do not exist.  URL is malformed.")
  }

  get("/types/efficacy") {
    val req = APIRequest(params.toMap[String,String])
    val returnValue = validateResults(V3Utils.processObjectEndpointWithParameters("type", params.toMap[String,String], req, mongo))
    response.getWriter.write(returnValue)
  }

  //get("/types/efficacy/:objId") {
  //
  //}

  get("/types/efficacy/type1") {
    failWithError("Required parameters do not exist.  URL is malformed.")
  }

  get("/types/efficacy/type1/:type1/type2") {
    failWithError("Required parameters do not exist.  URL is malformed.")
  }


  get("/types/efficacy/type1/:type1") {
    validateParameterLength(1, params.size)
    var type1:String = validateStringParameter("type1")
    val returnValue = validateResults(QueryManager.Query_EfficacyBySingleType(type1, mongo))
    response.getWriter.write(returnValue)
  }

  get("/types/efficacy/type1/:type1/type2/:type2") {
    validateParameterLength(2, params.size)
    var type1:String = validateStringParameter("type1")
    var type2:String = validateStringParameter("type2")
    val returnValue = validateResults(QueryManager.Query_EfficacyByMultipleType(type1, type2, mongo))
    response.getWriter.write(returnValue)
  }


  get("/comparator") {
    validateParameterLength(1, params.size)
    val idList:List[String] = params.getOrElse("pokemonIds", halt(400)).split(",").toList
    if(idList.length == 2) {
       val idArray = idList.toArray[String]
       response.getWriter.write(QueryManager.Query_ComparatorById(idArray(0).toInt, idArray(1).toInt, mongo))
    } else {

      //BULLSHIT!
    }

  }


  //get("/evolutions/pokemon/:object_id") {
  //
  //}
  get("/evolutions") {
    val req = APIRequest(params.toMap[String,String])
    val returnValue = validateResults(V3Utils.processObjectEndpointWithParameters("evolutions", params.toMap[String,String], req, mongo))
    response.getWriter.write(returnValue)
  }

  get("/evolutions/pokemon/national-id") {
    failWithError("Required parameters do not exist.  URL is malformed.")
  }

  get("/evolutions/pokemon/national-id/:nationalId") {
    val nationalId:Int = validateIntParameter("nationalId")
    val returnValue = validateResults(QueryManager.Query_EvolutionsByNationalId(nationalId, mongo))
    response.getWriter.write(returnValue)
  }


  //get("/evolutions/chain/:object_id") {
  //
  //}

  get("/evolutions/chain/chain-id/:chainId") {
    val chainId:Int = validateIntParameter("chainId")
    val returnValue = validateResults(QueryManager.Query_EvolutionsByChainId(chainId, mongo))
    response.getWriter.write(returnValue)

  }

  get("/evolutions/chain/chain-id") {
    failWithError("Required parameters do not exist.  URL is malformed.")
  }

  def validateIntParameter(parameterName : String) : Int = {
    try {
      val parameter:Int = params.getOrElse(parameterName, halt(V3Utils.HALT_CODE)).toInt
        parameter
    } catch {
    case e:NumberFormatException => failWithError("Invalid "+ parameterName + " format detected in URL."); return -1
  }

  }

  def validateStringParameter(parameterName : String) : String = {
    try {
      val parameter:String = params.getOrElse(parameterName, halt(V3Utils.HALT_CODE)).toString
      parameter
    } catch {
      case e:Exception => failWithError( "Invalid "+ parameterName + " format detected in URL."); return ""
    }
  }

  def validateObjectIdParameter(parameterName : String) : ObjectId = {
    try {
      val parameter:ObjectId = new ObjectId(params.getOrElse(parameterName, "null").asInstanceOf[String])
      parameter
    } catch {
      case e:Exception => failWithError( "Invalid "+ parameterName + " format detected in URL."); return null
    }
  }

  def validateParameterLength(expectedLength : Int, currentLength : Int) {

    if (expectedLength != currentLength) {
      failWithError("Required parameters do not exist.  URL is malformed.")
    }

  }

  def validateResults(results : String) : String = {
    //BROKEN
    //val objectResult = JSON.parse(results).asInstanceOf[DBObject]
    //
    //val stringResult = objectResult.getOrElse("error", "null").toString
    //if (!stringResult.equals("null")) {
    //  failWithError(stringResult.toString)
    //  return stringResult
    //} else {
    //  return objectResult.toString
    //}

    //hack fix
    //we know this is terrible...we tried to use objects but didn't have time to get the casbah methods to return the objects we need.

    if (results.contains("IGNPokedexError")) {
      val stringResult = results.substring(results.indexOf(":"), results.length() - 1)
      failWithError(stringResult)
      return stringResult
    }

    return results
  }

  def failWithError(message : String) {

    halt(V3Utils.HALT_CODE, message)
  }


    get("/moves") {
      val req = APIRequest(params.toMap[String,String])
      val returnValue = validateResults(V3Utils.processObjectEndpointWithParameters("moves", params.toMap[String,String], req, mongo))
      response.getWriter.write(returnValue)
    }

    get("/moves/slug/:slug") {
      val slug:String = validateStringParameter("slug")
      val returnValue = validateResults(QueryManager.Query_MovesBySingleParameter("metadata.name", slug, mongo))
      response.getWriter.write(returnValue)
    }

    get("/moves/slug") {
      failWithError("Required parameters do not exist.  URL is malformed.")
    }

    get("/moves/category/:category") {
      val category:String = validateStringParameter("category")
      val returnValue = validateResults(QueryManager.Query_MovesBySingleParameter("metadata.category", category, mongo))
      response.getWriter.write(returnValue)
    }

    get("/moves/type/:type") {
      val moveType:String = validateStringParameter("type")
      val returnValue = validateResults(QueryManager.Query_MovesBySingleParameter("metadata.type", moveType, mongo))
      response.getWriter.write(returnValue)
    }

    get("/moves/move-id/:moveId/pokemon") {
      val moveId:String = params.getOrElse("moveId", "1").toString
      response.getWriter.write(QueryManager.Query_PokemonByMoveLearned(moveId, 5, mongo))
    }


    get("/moves/move-id/:moveId/pokemon/move-group/:moveGroup") {
      val moveId:String = params.getOrElse("moveId", "1").toString
      val moveGroup:String = params.getOrElse("moveGroup", "level")
      val returnValue = validateResults(QueryManager.Query_PokemonByMoveTypeLearned(moveGroup, moveId, 5, mongo))
      response.getWriter.write(returnValue)
      //response.getWriter.write(QueryManager.Query_PokemonByMoveTypeLearned(moveGroup, moveId, 5, mongo))


    }

    get("/moves/move-id/:moveId/pokemon/move-group/:moveGroup/generation/:generation") {
      val moveId:String = params.getOrElse("moveId", "1").toString
      val moveGroup:String = params.getOrElse("moveGroup", "level")
      val generation:Int = params.getOrElse("generation", "5").toInt
      val returnValue = validateResults(QueryManager.Query_PokemonByMoveTypeLearned(moveGroup, moveId, generation, mongo))
      response.getWriter.write(returnValue)
      //response.getWriter.write(QueryManager.Query_PokemonByMoveTypeLearned(moveGroup, moveId, generation, mongo))


    }



  before {
    PokedexTestGenerator.setupTestDatabase()
    mongo = MongoConnection()
    response.setContentType("application/json")
    //response content type
    //caching
    //mongo.getDB()

  }

  after {
    PokedexTestGenerator.deleteTestDatabase()
    mongo.close()
  }

  notFound {

    <html>
      <body>
        <h1>NOT found</h1>
      </body>
    </html>

  }

}
