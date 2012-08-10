package com.ign.pokedex

import com.mongodb.casbah.Imports._
import com.mongodb.util.JSON
import java.util

/**
 * Created with IntelliJ IDEA.
 * User: mmigliacio
 * Date: 8/3/12
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */

object QueryManager {

  //maybe refactor to pass query into function, make queries static in queryManager and call them.
  //val r = $or ("metadata.type" -> "fire", "metadata.type"->"fuckyou")

  def Query_MovesBySingleParameter(param_name: String, param_val: String) : String = {
    val mongoColl = PokedexTestGenerator.moveCollection
    var returnedItem =""


    //val r = $or ("metadata.type" -> "fire", "metadata.type"->"fuckyou")
    val s =  MongoDBObject(param_name -> param_val)
    returnedItem = PokedexUtils.executeMultipleQuery(mongoColl, s)

    PokedexUtils.cleanupDB(mongoColl)

    returnedItem

  }

  def Query_PokemonByMoveLearned(move_id : String, generation : Int) : String = {

    //NOTE:  THIS IS TEMPORARY
    val mongoPokemonColl = PokedexTestGenerator.pokemonCollection
    val mongoMoveColl = PokedexTestGenerator.moveCollection

    var returnedItem =""

    //get all pokemon of generation 5, then search levelMoves and machineMoves
    println("moveId:" + move_id)
    println("levelMoves."+move_id)
    val subQuery = $or(("moves.levelMoves."+move_id) -> MongoDBObject("$exists"-> true), ("moves.tutorMoves."+move_id) -> MongoDBObject("$exists" -> true), ("moves.hmMoves." + move_id) -> MongoDBObject("$exists" -> true), ("moves.tmMoves." + move_id) -> MongoDBObject("$exists" -> true))
    val allPokemonQueryObject = MongoDBObject("metadata.generation" -> generation) ++ subQuery
    //val firstQuery = mongoPokemonColl.find(allPokemonQueryObject)
    //println("LENGTH:" + firstQuery.length)
    returnedItem = PokedexUtils.executeMultipleQuery(mongoPokemonColl, allPokemonQueryObject)


    returnedItem

  }

  def Query_PokemonByLevelMoveLearned(move_id : String, generation: Int) : String = {

    //NOTE:  THIS IS TEMPORARY
    val mongoPokemonColl = PokedexTestGenerator.pokemonCollection
    var returnedItem =""
    //get all pokemon of generation 5, then search levelMoves
    val subQuery = ("moves.levelMoves."+move_id) -> MongoDBObject("$exists"-> true)
    val allPokemonQueryObject = MongoDBObject("metadata.generation" -> generation) ++ subQuery
    returnedItem = PokedexUtils.executeMultipleQuery(mongoPokemonColl, allPokemonQueryObject)

    returnedItem

  }

  def Query_PokemonByTMLearned(move_id : String, generation: Int) : String = {

    //NOTE:  THIS IS TEMPORARY
    val mongoPokemonColl = PokedexTestGenerator.pokemonCollection
    var returnedItem =""

    //get all pokemon of generation 5, then search levelMoves
    val subQuery = ("moves.tmMoves."+move_id) -> MongoDBObject("$exists"-> true)
    val allPokemonQueryObject = MongoDBObject("metadata.generation" -> generation) ++ subQuery
    returnedItem = PokedexUtils.executeMultipleQuery(mongoPokemonColl, allPokemonQueryObject)


    returnedItem

  }

  def Query_PokemonByHMLearned(move_id : String, generation: Int) : String = {

    //NOTE:  THIS IS TEMPORARY
    val mongoPokemonColl = PokedexTestGenerator.pokemonCollection
    var returnedItem =""

    //get all pokemon of generation 5, then search levelMoves
    val subQuery = ("moves.hmMoves."+move_id) -> MongoDBObject("$exists"-> true)
    val allPokemonQueryObject = MongoDBObject("metadata.generation" -> generation) ++ subQuery
    returnedItem = PokedexUtils.executeMultipleQuery(mongoPokemonColl, allPokemonQueryObject)


    returnedItem

  }

  def Query_AllMoves() : String = {

    val mongoColl = PokedexTestGenerator.moveCollection
    var returnedItem =""

    returnedItem = PokedexUtils.executeMultipleQuery(mongoColl,("moveId" $exists true))

    returnedItem
  }

  def Query_EvolutionsByNationalId(national_id : Int) : String = {

    //NOTE:  THIS IS TEMPORARY
    val mongoPokemonColl = PokedexTestGenerator.pokemonCollection
    val mongoEvolutionColl = PokedexTestGenerator.evolutionCollection

    var returnedItem =""

    val nationalIdQueryObject = MongoDBObject("metadata.nationalId" -> national_id)
    val firstQuery = mongoPokemonColl.findOne(nationalIdQueryObject)
    var evolutionChain = firstQuery.get("evolutionChain").toString

    val evolutionChainQueryObject = MongoDBObject("evolutionChain" -> evolutionChain.toInt)
    returnedItem = PokedexUtils.executeMultipleQuery(mongoEvolutionColl, evolutionChainQueryObject)

    returnedItem

  }

  def Query_EvolutionsByChainId(chain_id : Int) : String = {

    //NOTE:  THIS IS TEMPORARY
    val mongoEvolutionColl = PokedexTestGenerator.evolutionCollection

    var returnedItem =""

    val evolutionChainQueryObject = MongoDBObject("evolutionChain" -> chain_id)
    returnedItem = PokedexUtils.executeMultipleQuery(mongoEvolutionColl, evolutionChainQueryObject)

    returnedItem

  }

  def Query_EfficacyBySingleType(type1: String) : String = {
    val mongoTypeColl = PokedexTestGenerator.typeCollection
    val typeQueryObject =  mongoTypeColl.find(MongoDBObject("opposed_type" -> type1))
    var key = ""
    var value = 0.0
    var types = new util.ArrayList[String]
    var newEfficacy = new Efficacy
    var returnedItem = ""

    //hackity hack, don't talk back
    for(x <- typeQueryObject) {
      key =  x.get("current_type").toString
      value = ((x.get("damage_factor").toString.toFloat))
      newEfficacy.typeMap.put(key, value)
      types.add(key)
    }

    returnedItem = "{"
    var count = 0
    while(count < newEfficacy.typeMap.size) {
      var damageValue = newEfficacy.typeMap.getOrElse(types.get(count), 0.0)
      returnedItem += "\"" + types.get(count) + "\"" +" : "+ damageValue +","
      count += 1
    }

    returnedItem = returnedItem.substring(0, returnedItem.length -1)+"}"


    returnedItem

  }

  def Query_EfficacyByMultipleType(type1: String, type2: String) : String = {
    //if type2 == "", make easy query to find weaknesses.
    val mongoTypeColl = PokedexTestGenerator.typeCollection;
    var returnedItem = ""


       //MwC (Manish will Cry)

       //do lots of shit
      var type1QueryObject = mongoTypeColl.find(MongoDBObject("opposed_type" -> type1))
      var type2QueryObject = mongoTypeColl.find(MongoDBObject("opposed_type" -> type2))
      var type1Efficacy = new Efficacy
      var type2Efficacy = new Efficacy
      var newEfficacy = new Efficacy
      var key = ""
      var value = 0.0
      var types = new util.ArrayList[String]

      //hackity hack, don't talk back
      for(x <- type1QueryObject) {
        key =  x.get("current_type").toString
        value = ((x.get("damage_factor").toString.toFloat))
        type1Efficacy.typeMap.put(key, value)
        types.add(key)
      }

      for(y <- type2QueryObject) {
        key =  y.get("current_type").toString
        value = ((y.get("damage_factor").toString.toFloat))
        type2Efficacy.typeMap.put(key, value)
      }

      var count = 0
      //there has to be a better way, but I don't know it yet.

      /*
      while(count < type2Efficacy.typeMap.size) {

        var xValue = type1Efficacy.typeMap.getOrElse(types.get(count), 0.0)
        var yValue = type2Efficacy.typeMap.getOrElse(types.get(count), 0.0)
        var total = (xValue * yValue)/100
        newEfficacy.typeMap.put(types.get(count), total)
        count += 1
      }

      println(newEfficacy.typeMap)

      returnedItem = JSON.serialize(newEfficacy.typeMap)
      println(returnedItem)
      */

      returnedItem = "{"
      while(count < type2Efficacy.typeMap.size) {

        var xValue = type1Efficacy.typeMap.getOrElse(types.get(count), 0.0)
        var yValue = type2Efficacy.typeMap.getOrElse(types.get(count), 0.0)
        var total = (xValue * yValue)/100
        //newEfficacy.typeMap.put(types.get(count), total)
        returnedItem += "\"" + types.get(count) + "\"" +" : "+ total +","
        count += 1
      }

      returnedItem = returnedItem.substring(0, returnedItem.length -1)+"}"

    //turn the shit into json
    //return the shit

    //LEEEROY JSON!
      returnedItem

  }

}
