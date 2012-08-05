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
    mongoColl += PokedexTestGenerator.getTestMove()
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
    mongoColl += PokedexTestGenerator.getTestMove()
    mongoColl += PokedexTestGenerator.getTestMove2()
    mongoColl.find()

    val r = $or ("metadata.type" -> "fire", "metadata.type"->"fuckyou")

    returnedItem = PokedexUtils.executeMultipleQuery(mongoColl, r)

    PokedexUtils.cleanupDB(mongoColl)

    returnedItem
  }


}
