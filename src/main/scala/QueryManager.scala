package com.ign.pokedex

import com.mongodb.casbah.Imports._
import java.util
import com.mongodb.util.JSON

import scala.math.abs
import scala.math.max



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

   def Query_PokemonBySizeUnfiltered(size : Int, mongoConn : MongoConnection) : String = {
    val pokemonColl = mongoConn("pokedex")("pokemon")
    val queryObject = pokemonColl.find(MongoDBObject("metadata.generation" -> 5)).limit(size).sort(MongoDBObject("metadata.nationalId" -> 1))
    val returnedItem = PokedexUtils.computeJSON(queryObject)
     returnedItem

   }

  def Query_PokemonBySizeAndGenerationFiltered(size : Int, generation: Int, mongoConn : MongoConnection) : String =  {
    val pokemonColl = mongoConn("pokedex")("pokemon")
    val fieldsReturned = MongoDBObject("metadata.name" -> 1, "metadata.generation" -> 1, "metadata.nationalId" -> 1)
    val queryObject = pokemonColl.find(MongoDBObject("metadata.generation" -> generation), fieldsReturned).limit(size).sort(MongoDBObject("metadata.nationalId" -> 1))
    val returnedItem = PokedexUtils.computeJSON(queryObject)
    returnedItem

  }

  def Query_PokemonByRange(low : Int, high: Int, generation: Int, mongoConn : MongoConnection) : String =  {
    val pokemonColl = mongoConn("pokedex")("pokemon")
    val fieldsReturned = MongoDBObject("metadata.name" -> 1, "metadata.generation" -> 1, "metadata.nationalId" -> 1)
    val queryObject = pokemonColl.find(("metadata.nationalId" $lte high $gte low) ++ ("metadata.generation" -> generation), fieldsReturned).sort(MongoDBObject("metadata.nationalId" -> 1))
    val returnedItem = PokedexUtils.computeJSON(queryObject)
    returnedItem

    //val q: DBObject = ("metadata.nationalId" $lte high $gte low) ++ ("metadata.generation" -> generation)
  }

  def Query_PokemonByName( name : String, mongoConn : MongoConnection) : String = {
    val pokemonColl = mongoConn("pokedex")("pokemon")
    val queryObject = pokemonColl.findOne(MongoDBObject("slug" -> name))
    if (queryObject.size == 0) {
       return V3Utils.generateErrorJSON("Pokemon name is invalid.")  //HERE'S THE BUG
    }

    val returnedItem = JSON.serialize(queryObject)
    returnedItem

  }

  def Query_PokemonByNameAndGeneration(name : String, generation: Int, mongoConn : MongoConnection) : String = {
    val pokemonColl = mongoConn("pokedex")("pokemon")
    val queryObject = pokemonColl.find(MongoDBObject("slug" -> name, "metadata.generation" -> generation))
    val returnedItem = PokedexUtils.computeJSON(queryObject)
    returnedItem
  }

  def Query_PokemonByNationalId(nationalId: Int, mongoConn: MongoConnection) : String =  {
    val pokemonColl = mongoConn("pokedex")("pokemon")
    val queryObject = pokemonColl.findOne(MongoDBObject("metadata.nationalId" -> nationalId))
    //if (queryObject.size == 0) {
    //  return V3Utils.generateErrorJSON
    //}

    val returnedItem = JSON.serialize(queryObject)
    returnedItem

  }

  def Query_PokemonByNationalIdAndGeneration(nationalId: Int, generation: Int, mongoConn: MongoConnection) : String = {
    val pokemonColl = mongoConn("pokedex")("pokemon")
    val queryObject = pokemonColl.find(MongoDBObject("metadata.nationalId" -> nationalId, "metadata.generation" -> generation))
    val returnedItem = PokedexUtils.computeJSON(queryObject)
    returnedItem
  }

  def Query_MovesBySingleParameter(param_name: String, param_val: String, mongoConn: MongoConnection) : String = {
    val mongoColl = mongoConn("pokedex")("moves")
    var returnedItem =""


    //val r = $or ("metadata.type" -> "fire", "metadata.type"->"fuckyou")
    val s =  MongoDBObject(param_name -> param_val)
    returnedItem = PokedexUtils.executeMultipleQuery(mongoColl, s)

    returnedItem

  }

  def Query_PokemonByMoveLearned(move_id : String, generation : Int, mongoConn: MongoConnection) : String = {

    //NOTE:  THIS IS TEMPORARY
    val mongoPokemonColl = mongoConn("pokedex")("pokemon")
    val mongoMoveColl = mongoConn("pokedex")("moves")

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

  def Query_PokemonByLevelMoveLearned(move_id : String, generation: Int, mongoConn: MongoConnection) : String = {

    //NOTE:  THIS IS TEMPORARY
    val mongoPokemonColl = mongoConn("pokedex")("pokemon")
    var returnedItem =""
    //get all pokemon of generation 5, then search levelMoves
    val subQuery = ("moves.levelMoves."+move_id) -> MongoDBObject("$exists"-> true)
    val allPokemonQueryObject = MongoDBObject("metadata.generation" -> generation) ++ subQuery
    returnedItem = PokedexUtils.executeMultipleQuery(mongoPokemonColl, allPokemonQueryObject)

    returnedItem

  }

  def Query_PokemonByTMLearned(move_id : String, generation: Int, mongoConn: MongoConnection) : String = {

    //NOTE:  THIS IS TEMPORARY
    val mongoPokemonColl = mongoConn("pokedex")("pokemon")
    var returnedItem =""

    //get all pokemon of generation 5, then search levelMoves
    val subQuery = ("moves.tmMoves."+move_id) -> MongoDBObject("$exists"-> true)
    val allPokemonQueryObject = MongoDBObject("metadata.generation" -> generation) ++ subQuery
    returnedItem = PokedexUtils.executeMultipleQuery(mongoPokemonColl, allPokemonQueryObject)


    returnedItem

  }

  def Query_PokemonByHMLearned(move_id : String, generation: Int, mongoConn: MongoConnection) : String = {

    //NOTE:  THIS IS TEMPORARY
    val mongoPokemonColl = mongoConn("pokedex")("pokemon")
    var returnedItem =""

    //get all pokemon of generation 5, then search levelMoves
    val subQuery = ("moves.hmMoves."+move_id) -> MongoDBObject("$exists"-> true)
    val allPokemonQueryObject = MongoDBObject("metadata.generation" -> generation) ++ subQuery
    returnedItem = PokedexUtils.executeMultipleQuery(mongoPokemonColl, allPokemonQueryObject)


    returnedItem

  }

  def Query_AllMoves(mongoConn: MongoConnection) : String = {

    val mongoColl = mongoConn("pokedex")("moves")
    var returnedItem =""

    returnedItem = PokedexUtils.executeMultipleQuery(mongoColl,("moveId" $exists true))

    returnedItem
  }

  def Query_MovesBySize(size: Int, mongoConn: MongoConnection) : String = {

    val moveColl = mongoConn("pokedex")("moves")
    var returnedItem =""
    val queryObject = moveColl.find("moveId" $exists true).limit(size)


    returnedItem
  }

  def Query_EvolutionsByNationalId(national_id : Int, mongoConn: MongoConnection) : String = {

    //NOTE:  THIS IS TEMPORARY
    val mongoPokemonColl = mongoConn("pokedex")("pokemon")
    val mongoEvolutionColl = mongoConn("pokedex")("evolutions")

    var returnedItem =""

    val nationalIdQueryObject = MongoDBObject("metadata.nationalId" -> national_id)
    val firstQuery = mongoPokemonColl.findOne(nationalIdQueryObject)
    var evolutionChain = firstQuery.get("evolutionChain").toString

    val evolutionChainQueryObject = MongoDBObject("evolutionChain" -> evolutionChain.toInt)
    returnedItem = PokedexUtils.executeMultipleQuery(mongoEvolutionColl, evolutionChainQueryObject)

    returnedItem

  }

  def Query_EvolutionsByChainId(chain_id : Int, mongoConn: MongoConnection) : String = {

    //NOTE:  THIS IS TEMPORARY
    val mongoEvolutionColl = mongoConn("pokedex")("evolutions")

    var returnedItem =""

    val evolutionChainQueryObject = MongoDBObject("evolutionChain" -> chain_id)
    returnedItem = PokedexUtils.executeMultipleQuery(mongoEvolutionColl, evolutionChainQueryObject)

    returnedItem

  }

  def Query_EfficacyBySingleType(type1: String, mongoConn: MongoConnection) : String = {
    val mongoTypeColl = mongoConn("pokedex")("type")
    val typeQueryObject =  mongoTypeColl.find(MongoDBObject("opposed_type" -> type1))
    var key = ""
    var value = 0.0
    var types = new util.ArrayList[String]
    var newEfficacy = new Efficacy
    var returnedItem = ""

    if(typeQueryObject.length == 0) {
      returnedItem = V3Utils.generateErrorJSON("Query returned zero elements.  Invalid parameter specified.")
      println(returnedItem)
      return returnedItem
    }

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

  def Query_EfficacyByMultipleType(type1: String, type2: String, mongoConn: MongoConnection) : String = {
    //if type2 == "", make easy query to find weaknesses.
    val mongoTypeColl = mongoConn("pokedex")("type")
    var returnedItem = ""


       //MwC (Manish will Cry)

       //do lots of shit
      var type1QueryObject = mongoTypeColl.find(MongoDBObject("opposed_type" -> type1))
      var type2QueryObject = mongoTypeColl.find(MongoDBObject("opposed_type" -> type2))

      if ((type1QueryObject.length == 0) || (type2QueryObject.length == 0)) {
        returnedItem = V3Utils.generateErrorJSON("Query returned zero elements.  Invalid parameter specified.")
        return returnedItem
      }

      var type1Efficacy = new Efficacy
      var type2Efficacy = new Efficacy
      var newEfficacy = new Efficacy
      var key = ""
      var value = 0.0
      var types = new util.ArrayList[String]

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

  def Query_ComparatorById(id1 : Int, id2 : Int, mongoConn : MongoConnection) : String = {

    val pokemonColl = mongoConn("pokedex")("pokemon")
    val queryObject1 = pokemonColl.findOne(MongoDBObject("metadata.nationalId" -> id1, "metadata.generation" -> 5))
    val queryObject2 = pokemonColl.findOne(MongoDBObject("metadata.nationalId" -> id2, "metadata.generation" -> 5))

    if (queryObject1.isEmpty || queryObject2.isEmpty)
      return V3Utils.generateErrorJSON("bad news")

    val Metadata = JSON.parse(queryObject1.get("metadata").toString).asInstanceOf[DBObject]
    val Type = JSON.parse(Metadata.get("type").toString).asInstanceOf[DBObject]
    val Metadata2 = JSON.parse(queryObject2.get("metadata").toString).asInstanceOf[DBObject]
    val Type2 = JSON.parse(Metadata2.get("type").toString).asInstanceOf[DBObject]
    val pkmType1 = Type.get("type_1")
    val pkmType2 = Type.get("type_2")
    val pkm2Type1 = Type2.get("type_1")
    val pkm2Type2 = Type2.get("type_2")
    var pTypeEfficacy = ""
    var p2TypeEfficacy = ""

    if (pkmType2 == null)
      pTypeEfficacy = Query_EfficacyBySingleType(pkmType1.toString.toLowerCase, mongoConn)
    else
      pTypeEfficacy = Query_EfficacyByMultipleType(pkmType1.toString.toLowerCase, pkmType2.toString.toLowerCase, mongoConn)

    if (pkm2Type2 == null)
      p2TypeEfficacy = Query_EfficacyBySingleType(pkm2Type1.toString.toLowerCase, mongoConn)
    else
      p2TypeEfficacy = Query_EfficacyByMultipleType(pkm2Type1.toString.toLowerCase, pkm2Type2.toString.toLowerCase, mongoConn)
    val p1EfficacyObject = JSON.parse(pTypeEfficacy).asInstanceOf[DBObject]
    val p2EfficacyObject = JSON.parse(p2TypeEfficacy).asInstanceOf[DBObject]

    var pdamage = 0.0
    var pdamage2 = 0.0
    var pmax = 0.0
    var p2damage = 0.0
    var p2damage2 = 0.0
    var p2max = 0.0

    // Pokemon1 type1 against pokemon2 efficacy
    if (pkmType2 == null){
      pdamage = p2EfficacyObject.get(pkmType1.toString.toLowerCase).toString.toFloat
      pmax = pdamage
    }
    else {
      // pokemon1 type2 against pokemon2 efficacy
      pdamage = p2EfficacyObject.get(pkmType1.toString.toLowerCase).toString.toFloat
      pdamage2 = p2EfficacyObject.get(pkmType2.toString.toLowerCase).toString.toFloat
      pmax = max(pdamage, pdamage2)
    }
    // Pokemon2 type1 against pokemon1 efficacy
    if (pkm2Type2 == null) {
      p2damage = p1EfficacyObject.get(pkm2Type1.toString.toLowerCase).toString.toFloat
      p2max = p2damage
    }
    else {
      p2damage = p1EfficacyObject.get(pkm2Type1.toString.toLowerCase).toString.toFloat
      // Pokemon2 type 2 against pokemon1 efficacy
      p2damage2 = p1EfficacyObject.get(pkm2Type2.toString.toLowerCase).toString.toFloat
      p2max = max(p2damage, p2damage2)
    }

    var pkm1TotalStats = 0.0
    var pkm2TotalStats = 0.0

    pkm1TotalStats += Metadata.get("hp").toString.toFloat
    pkm1TotalStats += Metadata.get("attack").toString.toFloat
    pkm1TotalStats += Metadata.get("defense").toString.toFloat
    pkm1TotalStats += Metadata.get("specialAttack").toString.toFloat
    pkm1TotalStats += Metadata.get("specialDefense").toString.toFloat
    pkm1TotalStats += Metadata.get("speed").toString.toFloat

    pkm2TotalStats += Metadata2.get("hp").toString.toFloat
    pkm2TotalStats += Metadata2.get("attack").toString.toFloat
    pkm2TotalStats += Metadata2.get("defense").toString.toFloat
    pkm2TotalStats += Metadata2.get("specialAttack").toString.toFloat
    pkm2TotalStats += Metadata2.get("specialDefense").toString.toFloat
    pkm2TotalStats += Metadata2.get("speed").toString.toFloat

    val statDiff = abs(pkm1TotalStats-pkm2TotalStats)
    val maxDamage = abs(pmax-p2max)
    var battleScore = 0.0
    var battleOutcomeMessage = ""

    if (statDiff <= 100) {
      // Type wins
      println("\n\nmaxDamage"+statDiff)
      if (maxDamage<=100) battleScore = 70
      if (maxDamage>100) battleScore = 80
      if (maxDamage>200) battleScore = 90
      if (pmax > p2max){
        // pokemon 1 wins
        battleOutcomeMessage = queryObject1.get("slug")+" has type advantage"
      }
      else if (p2max > pmax) {
        // pokemon 2 wins
        battleOutcomeMessage = queryObject2.get("slug")+" has type advantage"
      }
      else if (p2max == pmax) {
        // tie
        if (pkm1TotalStats > pkm2TotalStats) {
          battleScore = 60
          battleOutcomeMessage = queryObject1.get("slug")+" has a slight stat advantage"
        }
        else if (pkm2TotalStats > pkm1TotalStats) {
          battleScore = 60
          battleOutcomeMessage = queryObject2.get("slug")+" has a slight stat advantage"
        }
        else if (pkm2TotalStats == pkm1TotalStats){
          battleScore = 50
          battleOutcomeMessage = "tie"
        }
      }
    }
    else {
      // Stats wins
      if (statDiff<=100) battleScore = 70
      if (statDiff>100) battleScore = 80
      if (statDiff>200) battleScore = 90
      println("\n\nstatdiff"+statDiff)
      if (pkm1TotalStats > pkm2TotalStats){
        // pokemon 1 wins
        battleOutcomeMessage = queryObject1.get("slug").toString+" has greater stats advantage"
      } else if (pkm2TotalStats > pkm1TotalStats){
        battleOutcomeMessage = queryObject2.get("slug").toString+" has greater stats advantage"
      } else if (pkm2TotalStats == pkm1TotalStats){
        battleScore = 50
        battleOutcomeMessage = "too close to call!"
      }
    }

    var jsonA = ""
    var jsonB = ""

    queryObject1 foreach { x =>
      jsonA = JSON.serialize(x)
    }

    queryObject2 foreach { x =>
      jsonB = JSON.serialize(x)
    }

    val comparatorJSON = "{" +
      "\"pokemon1\":" +jsonA + "," +
      "\"pokemon2\":" + jsonB + "," +
      "\"score\":" + battleScore.toString + "," +
      "\"outcomeMessage\":" + "\""+battleOutcomeMessage+"\"" +"}"
    comparatorJSON
  }

}
