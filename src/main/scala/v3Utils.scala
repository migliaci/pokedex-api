package com.ign.pokedex
import com.mongodb.casbah.MongoConnection

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

  def processPokemonEndpoint(params: Map[String, String], req: APIRequest, mongo : MongoConnection): String = {

    var resultingJSON = ""

    if (params.size == 1) {

      println("count: " + req.count)
      //ALL ENDPOINTS VERIFIED
      if (req.count != -1){ //will return filtered list of pokemon with nationalId 1 to COUNT

        //xxx/pokemon?count=[count]

        resultingJSON = QueryManager.Query_PokemonBySizeAndGenerationFiltered(req.count, 5, mongo)
      } else if (!("".equals(req.name))) { //will return all details about a pokemon with name NAME

        //xxx/pokemon?name=[name]
        println("calling QueryPokemonByName for pokemon named: " + req.name)
        resultingJSON = QueryManager.Query_PokemonByName(req.name.toLowerCase, mongo)
        println("resulting JSON: " + resultingJSON)
      } else if (!("".equals(req.nationalId))) { //will return all details about a pokemon with nationalId NATIONALID

        //xxx/pokemon?nationalId=[nationalId]

        resultingJSON = QueryManager.Query_PokemonByNationalId(req.nationalId, mongo)
      } else {
        resultingJSON = generateErrorJSON
        return resultingJSON
      }

      if (req.count == -2) {
        resultingJSON = generateErrorJSON
        return resultingJSON
      }

    } else if (params.size == 2) {

      //ALL ENDPOINTS VERIFIED
      if (req.count != -1 && req.generation != -1) {//will return filtered list of COUNT pokemon (starting at nationalId 1) of generation GENERATION

        //xxx/pokemon?count=[count]&generation=[generation]

        resultingJSON = QueryManager.Query_PokemonBySizeAndGenerationFiltered(req.count, req.generation, mongo)
      } else if (req.startIndex != -1 && req.count != -1){ //will return list of COUNT pokemon starting at nationalId STARTINDEX defaulting to generation 5

        //xxx/pokemon?startIndex=[startIndex]&count=[count]&generation=[5]

        var endIndex = req.count + req.startIndex - 1
        if (endIndex < 1) endIndex = 1
        resultingJSON = QueryManager.Query_PokemonByRange(req.startIndex, endIndex, 5, mongo)
      } else if (!("".equals(req.name)) && req.generation != -1) { //will return all details about pokemon named NAME of generation GENERATION

        //xxx/pokemon?name=[name]&generation=[generation]

        resultingJSON = QueryManager.Query_PokemonByNameAndGeneration(req.name.toLowerCase, req.generation, mongo)
      } else if (req.nationalId != -1 && req.generation != -1) { //will return all details about pokemon with nationalId NATIONALID of generation GENERATION

        //xxx/pokemon?nationalId=[nationalId]&generation=[generation]

        resultingJSON = QueryManager.Query_PokemonByNationalIdAndGeneration(req.nationalId, req.generation, mongo)
      } else if (req.count != -1 && req.expand != -1) { //will return unfiltered list of COUNT pokemon (starting at nationalId 1)

        //xxx/pokemon?count=[count]&expand=[true]

        resultingJSON = QueryManager.Query_PokemonBySizeUnfiltered(req.count, mongo)
      } else {
        resultingJSON = generateErrorJSON
      }

      if(req.startIndex == -2 || req.count == -2 || req.generation == -2) {
        resultingJSON = generateErrorJSON
      }

    } else if (params.size == 3) {
      // need query_pokemonbyrange for specified generation  (3 parameters)
      // need query_pokemonbysizeunfiltered for specified generation (3 parameters)
      //sortBy, sortOrder for V3 Compliance - is this necessary for us?
    } else {
      println("Error case!")
      resultingJSON = generateErrorJSON
    }

    resultingJSON

  }

  def processTypesEndpoint(params: Map[String, String], req: APIRequest, mongo : MongoConnection): String = {

    var resultingJSON = ""

    if(params.size == 1) {

      if (!("".equals(req.type1))) {
        //xxx/types/efficacy?type1=[type1]
        resultingJSON = QueryManager.Query_EfficacyBySingleType(req.type1.toLowerCase, mongo)
      } else {
        println("Error case!")
        resultingJSON = generateErrorJSON
      }

    } else if (params.size == 2) {
      if (!("".equals(req.type1)) && !("".equals(req.type2))) {
        //xxx/types/efficacy?type1=[type1]&type2=[type2]
        resultingJSON = QueryManager.Query_EfficacyByMultipleType(req.type1.toLowerCase, req.type2.toLowerCase, mongo)
      }  else {
        println("Error case!")
        resultingJSON = generateErrorJSON
      }
    } else {
      println("Error case!")
      resultingJSON = generateErrorJSON
    }

    resultingJSON
  }

  def processComparatorEndpoint(params: Map[String, String], req: APIRequest, mongo : MongoConnection): String = {

    var resultingJSON = ""

    if(params.size == 2) {
      if ((req.firstPokemonId != -1) && (req.secondPokemonId != -1)) {
        //xxx/comparator?firstPokemonId=[firstPokemonId]&secondPokemonId=[secondPokemonId]
        println("firstId:" + req.firstPokemonId)
        println("secondId:" + req.secondPokemonId)
        resultingJSON = QueryManager.Query_ComparatorById(req.firstPokemonId, req.secondPokemonId, mongo)
      }
    } else {
      println("Error case!")
      resultingJSON = generateErrorJSON
    }

    resultingJSON
  }

  def processEvolutionsPokemonEndpoint(params: Map[String, String], req: APIRequest, mongo : MongoConnection): String = {
    var resultingJSON = ""

    if(params.size == 1) {

      if (req.nationalId != -1) {

        //xxx/evolutions/pokemon?nationalId=[nationalId]

        resultingJSON = QueryManager.Query_EvolutionsByNationalId(req.nationalId, mongo)
      } else {
        println("Error case!")
        resultingJSON = generateErrorJSON
      }

    } else {
      println("Error case!")
      resultingJSON = generateErrorJSON
    }

    resultingJSON

  }

  def processEvolutionsChainEndpoint(params: Map[String, String], req: APIRequest, mongo : MongoConnection): String = {
    var resultingJSON = ""

    if(params.size == 1) {

      if (req.evolutionChainId != -1) {

        //xxx/evolutions/pokemon?evolutionChainId=[evolutionChainId]

        resultingJSON = QueryManager.Query_EvolutionsByChainId(req.evolutionChainId, mongo)
      } else {
        println("Error case!")
        resultingJSON = generateErrorJSON
      }

    } else {
      println("Error case!")
      resultingJSON = generateErrorJSON
    }

    resultingJSON

  }

  def processMovesEndpoint(params: Map[String, String], req: APIRequest, mongo : MongoConnection): String = {
    var resultingJSON = ""

    if(params.size == 0) {

      //xxx/moves

      resultingJSON = QueryManager.Query_AllMoves(mongo)
    } else if (params.size == 1) {


      if (!("".equals(req.moveName))) {

        //xxx/moves?moveName=[moveName]

        resultingJSON = QueryManager.Query_MovesBySingleParameter("metadata.name", req.moveName.toLowerCase, mongo)
      } else if (!("".equals(req.moveCategory))) {

        //xxx/moves?moveCategory=[moveCategory]

        resultingJSON = QueryManager.Query_MovesBySingleParameter("metadata.category", req.moveCategory.toLowerCase, mongo)
      } else if (!("".equals(req.moveType))) {

        //xxx/moves?moveType=[moveType]

        resultingJSON = QueryManager.Query_MovesBySingleParameter("metadata.type", req.moveType.toLowerCase, mongo)
      }

    } else {

    }

    resultingJSON

  }

  def processMovesPokemonEndpoint(params: Map[String, String], req: APIRequest, mongo : MongoConnection): String = {
    var resultingJSON = ""

    if(params.size == 1) {

      if (!("".equals(req.moveId))) {

      //xxx/moves/pokemon?moveId=[moveId]

      resultingJSON = QueryManager.Query_PokemonByMoveLearned(req.moveId, 5, mongo)
      }
    } else if (params.size == 2) {

      if ((!("".equals(req.moveId))) && !("".equals(req.pokemonMoveClass))) {

        //xxx/moves/pokemon?moveId=[moveId]&pokemonMoveClass=[tm]&generation=[5]
        //xxx/moves/pokemon?moveId=[moveId]&pokemonMoveClass=[hm]&generation=[5]
        //xxx/moves/pokemon?moveId=[moveId]&pokemonMoveClass=[level]&generation=[5]

        req.pokemonMoveClass match {
          case "tm" => resultingJSON = QueryManager.Query_PokemonByTMLearned(req.moveId, 5, mongo)
          case "hm" => resultingJSON = QueryManager.Query_PokemonByHMLearned(req.moveId, 5, mongo)
          case "level" => resultingJSON = QueryManager.Query_PokemonByLevelMoveLearned(req.moveId, 5, mongo)
          case _ => resultingJSON = generateErrorJSON
        }

      } else if (!("".equals(req.moveId)) && (req.generation != -1)) {

        //xxx/moves/pokemon?moveId=[moveId]&generation=[generation]

        resultingJSON = QueryManager.Query_PokemonByMoveLearned(req.moveId, req.generation, mongo)
      } else {

        resultingJSON = generateErrorJSON
      }
    } else if (params.size == 3) {

      if ((!("".equals(req.moveId))) && !("".equals(req.pokemonMoveClass)) && (req.generation != -1)) {

        //xxx/moves/pokemon?moveId=[moveId]&pokemonMoveClass=[tm]&generation=[generation]
        //xxx/moves/pokemon?moveId=[moveId]&pokemonMoveClass=[hm]&generation=[generation]
        //xxx/moves/pokemon?moveId=[moveId]&pokemonMoveClass=[level]&generation=[generation]

        req.pokemonMoveClass match {
          case "tm" => resultingJSON = QueryManager.Query_PokemonByTMLearned(req.moveId, req.generation, mongo)
          case "hm" => resultingJSON = QueryManager.Query_PokemonByHMLearned(req.moveId, req.generation, mongo)
          case "level" => resultingJSON = QueryManager.Query_PokemonByLevelMoveLearned(req.moveId, req.generation, mongo)
          case _ => resultingJSON = generateErrorJSON
        }

      }

    }

    resultingJSON
  }

  def generateErrorJSON() : String = {

    return "{}"
  }

}
