package com.ign.pokedex

import com.mongodb.casbah.Imports._

/**
 * Created with IntelliJ IDEA.
 * User: mmigliacio
 * Date: 8/3/12
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */

object QueryManager {


  def connectToDB_MoveSingleParameterQuery(param_name: String, param_val: String) : String = {

    val mongoColl = MongoConnection()("pokedex")("test_data")
    var returnedItem =""

    //save to the DB
    mongoColl += PokedexTestGenerator.getTestMove1()
    mongoColl += PokedexTestGenerator.getTestMove2()
    mongoColl.find()

    returnedItem = PokedexUtils.executeMultipleQuery(mongoColl, MongoDBObject(param_name->param_val))

    PokedexUtils.cleanupDB(mongoColl)

    returnedItem
  }

  //maybe refactor to pass query into function, make queries static in queryManager and call them.

  def connectToDB_MoveMultipleParameterQueryTest(param_name: String, param_val: String) : String = {

    val mongoColl = MongoConnection()("pokedex")("test_data")
    var returnedItem =""

    //save to the DB
    mongoColl += PokedexTestGenerator.getTestMove1()
    mongoColl += PokedexTestGenerator.getTestMove2()
    mongoColl.find()

    val r = $or ("metadata.type" -> "fire", "metadata.type"->"fuckyou")

    returnedItem = PokedexUtils.executeMultipleQuery(mongoColl, r)

    PokedexUtils.cleanupDB(mongoColl)

    returnedItem
  }

  def Query_MovesBySingleParameter(param_name: String, param_val: String) : String = {
    val mongoColl = MongoConnection()("pokedex")("moves")
    var returnedItem =""


    //val r = $or ("metadata.type" -> "fire", "metadata.type"->"fuckyou")
    val s =  MongoDBObject(param_name -> param_val)
    returnedItem = PokedexUtils.executeMultipleQuery(mongoColl, s)

    PokedexUtils.cleanupDB(mongoColl)

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

  def Query_AllMoves() : String = {

    val mongoColl = MongoConnection()("pokedex")("moves")
    var returnedItem =""

    returnedItem = PokedexUtils.executeMultipleQuery(mongoColl,("moveId" $exists true))

    returnedItem
  }


}
