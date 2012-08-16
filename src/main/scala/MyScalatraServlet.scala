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
    response.setContentType("application/json")
    val req = APIRequest(params.toMap[String,String])
    val returnValue = validateResults(V3Utils.processPokemonEndpointWithParameters(params.toMap[String,String], req, mongo))
    response.getWriter.write(returnValue)
  }

  /*
  get("/pokemon/:objectId") {
    response.setContentType("application/json")
    validateParameterLength(1, params.size)
    val id = new ObjectId(params.getOrElse("objectId", "null").asInstanceOf[String])
    println("in the objectId logic")
    val returnValue = validateResults(QueryManager.Query_PokemonByObjectId(id, mongo))
    response.getWriter.write(returnValue)

  }
  */
  get("/pokemon/nationalId") {
    response.setContentType("application/json")
    failWithError("Required parameters do not exist.  URL is malformed.")
  }

  get("/pokemon/nationalId/:nationalId") {
    response.setContentType("application/json")
    validateParameterLength(1, params.size)
    val id = validateIntParameter("nationalId")
    val returnValue = validateResults(QueryManager.Query_PokemonByNationalId(id, mongo))
    response.getWriter.write(returnValue)
  }

  get("/pokemon/generation/:generation") {
    response.setContentType("application/json")
    val gen = validateIntParameter("generation")
    val req = APIRequest(params.toMap[String,String])
    val returnValue = validateResults(V3Utils.processComplexPokemonEndpointWithParameters("metadata.generation", gen, params.toMap[String,String], req, mongo))
    response.getWriter.write(returnValue)
  }

  get("/pokemon/slug") {
    response.setContentType("application/json")
    failWithError("Required parameters do not exist.  URL is malformed.")
  }

  get("/pokemon/slug/:slug") {
    response.setContentType("application/json")
    validateParameterLength(1, params.size)
    val slug:String = validateStringParameter("slug")
    val returnValue = validateResults(QueryManager.Query_PokemonByName(slug.toLowerCase, mongo))
    response.getWriter.write(returnValue)
  }

  get("/pokemon/nationalId/:nationalId/generation/:generation") {
    response.setContentType("application/json")
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
    response.setContentType("application/json")
    val returnValue = validateResults(QueryManager.Query_PokemonByNameAndGeneration(slug.toLowerCase, generation, mongo))
    response.getWriter.write(returnValue)
  }

  get("/pokemon/slug/:slug/generation") {
    response.setContentType("application/json")
    failWithError("Required parameters do not exist.  URL is malformed.")
  }

  get("/pokemon/slug/generation/:generation") {
    response.setContentType("application/json")
    println("fell into the badness")
    failWithError("Required parameters do not exist.  URL is malformed.")
  }

  get("/pokemon/nationalId/:nationalId/generation") {
    response.setContentType("application/json")
    failWithError("Required parameters do not exist.  URL is malformed.")
  }

  get("/pokemon/nationalId/generation/:generation") {
    response.setContentType("application/json")
    println("fell into the badness")
    failWithError("Required parameters do not exist.  URL is malformed.")
  }

  get("/types/efficacy") {
    response.setContentType("application/json")
    val req = APIRequest(params.toMap[String,String])
    val returnValue = validateResults(V3Utils.processTypesEndpointWithParameters(params.toMap[String,String], req, mongo))
    response.getWriter.write(returnValue)
  }

  //get("/types/efficacy/:objId") {
  //
  //}

  get("/types/efficacy/type1") {
    response.setContentType("application/json")
    failWithError("Required parameters do not exist.  URL is malformed.")
  }

  get("/types/efficacy/type1/:type1/type2") {
    response.setContentType("application/json")
    failWithError("Required parameters do not exist.  URL is malformed.")
  }


  get("/types/efficacy/type1/:type1") {
    response.setContentType("application/json")
    validateParameterLength(1, params.size)
    var type1:String = validateStringParameter("type1")
    val returnValue = validateResults(QueryManager.Query_EfficacyBySingleType(type1, mongo))
    response.getWriter.write(returnValue)
  }

  get("/types/efficacy/type1/:type1/type2/:type2") {
    response.setContentType("application/json")
    validateParameterLength(2, params.size)
    var type1:String = validateStringParameter("type1")
    var type2:String = validateStringParameter("type2")
    val returnValue = validateResults(QueryManager.Query_EfficacyByMultipleType(type1, type2, mongo))
    response.getWriter.write(returnValue)
  }


  get("/comparator") {
    response.setContentType("application/json")
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

    response.setContentType("application/json")
    val req = APIRequest(params.toMap[String,String])
    val returnValue = validateResults(V3Utils.processEvolutionsEndpointWithParameters(params.toMap[String,String], req, mongo))
    response.getWriter.write(returnValue)
  }

  get("/evolutions/pokemon/nationalId") {
    response.setContentType("application/json")
    failWithError("Required parameters do not exist.  URL is malformed.")
  }

  get("/evolutions/pokemon/nationalId/:nationalId") {
    response.setContentType("application/json")
    val nationalId:Int = validateIntParameter("nationalId")
    val returnValue = validateResults(QueryManager.Query_EvolutionsByNationalId(nationalId, mongo))
    response.getWriter.write(returnValue)
  }


  //get("/evolutions/chain/:object_id") {
  //
  //}

  get("/evolutions/chain/chainId/:chain_id") {
    response.setContentType("application/json")
    val chainId:Int = validateIntParameter("chain_id")
    val returnValue = validateResults(QueryManager.Query_EvolutionsByChainId(chainId, mongo))
    response.getWriter.write(returnValue)

  }

  get("/evolutions/chain/chainId") {
    response.setContentType("application/json")
    failWithError("Required parameters do not exist.  URL is malformed.")
  }

  def validateIntParameter(parameterName : String) : Int = {
    try {
      println("parameterName: " +parameterName)
      val parameter:Int = params.getOrElse(parameterName, halt(V3Utils.HALT_CODE)).toInt
      println("parameter: " + parameter)
        parameter
    } catch {
    case e:NumberFormatException => failWithError("Invalid "+ parameterName + "format detected in URL."); return -1
  }

  }

  def validateStringParameter(parameterName : String) : String = {
    try {
      val parameter:String = params.getOrElse(parameterName, halt(V3Utils.HALT_CODE)).toString
      parameter
    } catch {
      case e:Exception => failWithError( "Invalid "+ parameterName + "format detected in URL."); return ""
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
      response.setContentType("application/json")
      val req = APIRequest(params.toMap[String,String])
      val returnValue = validateResults(V3Utils.processMovesEndpointWithParameters(params.toMap[String,String], req, mongo))
      response.getWriter.write(returnValue)
    }

    get("/moves/slug/:slug") {
      response.setContentType("application/json")
      val slug:String = validateStringParameter("slug")
      val returnValue = validateResults(QueryManager.Query_MovesBySingleParameter("metadata.name", slug, mongo))
      response.getWriter.write(returnValue)
    }

    get("/moves/slug") {
      response.setContentType("application/json")
      failWithError("Required parameters do not exist.  URL is malformed.")
    }

    get("/moves/category/:category") {
      response.setContentType("application/json")
      val category:String = validateStringParameter("category")
      val returnValue = validateResults(QueryManager.Query_MovesBySingleParameter("metadata.category", category, mongo))
      response.getWriter.write(returnValue)
    }

    get("/moves/type/:type") {
      response.setContentType("application/json")
      val moveType:String = validateStringParameter("type")
      val returnValue = validateResults(QueryManager.Query_MovesBySingleParameter("metadata.type", moveType, mongo))
      response.getWriter.write(returnValue)
    }

    get("/moves/moveId/:moveId/pokemon") {
      response.setContentType("application/json")
      val moveId:String = params.getOrElse("id", "1").toString
      println("inside move query for pokemon")
      response.getWriter.write(QueryManager.Query_PokemonByMoveLearned(moveId, 5, mongo))
    }

    get("/moves/moveId/:moveId/pokemon/moveGroup/:moveGroup") {
      response.setContentType("application/json")
      val moveId:String = params.getOrElse("moveId", "1").toString
      val moveGroup:String = params.getOrElse("moveGroup", "hm")

      moveGroup match {
        case "tm" => response.getWriter.write(QueryManager.Query_PokemonByTMLearned(moveId, 5, mongo))
        case "hm" => response.getWriter.write(QueryManager.Query_PokemonByHMLearned(moveId, 5, mongo))
        case "level" => response.getWriter.write(QueryManager.Query_PokemonByLevelMoveLearned(moveId, 5, mongo))
        case _ => V3Utils.generateErrorJSON("bad news")
      }

    }

    get("/moves/moveId/:moveId/pokemon/moveGroup/:moveGroup/generation/:generation") {
      response.setContentType("application/json")
      val moveId:String = params.getOrElse("moveId", "1").toString
      val moveGroup:String = params.getOrElse("moveGroup", "hm")
      val generation:Int = params.getOrElse("generation", "5").toInt

      moveGroup match {
        case "tm" => response.getWriter.write(QueryManager.Query_PokemonByTMLearned(moveId, generation, mongo))
        case "hm" => response.getWriter.write(QueryManager.Query_PokemonByHMLearned(moveId, generation, mongo))
        case "level" => response.getWriter.write(QueryManager.Query_PokemonByLevelMoveLearned(moveId, generation, mongo))
        case _ => V3Utils.generateErrorJSON("bad news")
      }

    }



  before {
    PokedexTestGenerator.setupTestDatabase()
    mongo = MongoConnection()

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
