package com.ign.pokedex

import com.mongodb.casbah.Imports._
import java.util
import com.mongodb.util.JSON

import scala.math.abs
import scala.math.max
import scala.util.control.Breaks._



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

  def Query_ObjectByParameters(objectString: String, startIndex : Int, count: Int, fieldList : List[String],   sortBy: Option[String], sortOrder: Int, mongoConn : MongoConnection) : String =  {
    val objectColl = mongoConn("pokedex")(objectString)
    var returnedItem = Query_ListByParameters(startIndex, count, fieldList, sortBy, sortOrder, objectColl)
    returnedItem

  }

  def Query_ComplexObjectByParameters(objectString : String, queryParm : String, queryParmValue: Int, startIndex : Int, count: Int, fieldList : List[String],   sortBy: Option[String], sortOrder: Int, mongoConn : MongoConnection) : String =  {
    val pokemonColl = mongoConn("pokedex")(objectString)
    var returnedItem = Query_ComplexListByParameters(queryParm, queryParmValue, startIndex, count, fieldList, sortBy, sortOrder, pokemonColl)
    returnedItem
  }

  def Query_ListByParameters(startIndex : Int, count: Int, fieldList : List[String], sortBy: Option[String], sortOrder: Int, mongoColl : MongoCollection) : String =  {
    var returnedItem = ""

    if (fieldList != Nil) {
      if (sortBy == None) {
        returnedItem = runUnsortedQueryWithFields("", 0, startIndex, count, mongoColl, buildDBObjectFromFields(fieldList))
      } else {
        returnedItem = runSortedQueryWithFields("", 0, startIndex, count, mongoColl, buildDBObjectFromFields(fieldList), sortBy, sortOrder)
      }
    } else {
      if (sortBy == None) {
        returnedItem = runUnsortedQueryWithoutFields("", 0, startIndex, count, mongoColl)
      } else {
        returnedItem = runSortedQueryWithoutFields("", 0, startIndex, count, mongoColl, sortBy, sortOrder)
      }
    }

    returnedItem

  }

  def Query_ComplexListByParameters(queryParm : String, queryParmValue: Int, startIndex : Int, count: Int, fieldList : List[String], sortBy: Option[String], sortOrder: Int, mongoColl : MongoCollection) : String =  {
    var returnedItem = ""

    if (fieldList != Nil) {
      if (sortBy == None) {
        returnedItem = runUnsortedQueryWithFields(queryParm, queryParmValue, startIndex, count, mongoColl, buildDBObjectFromFields(fieldList))
      } else {
        returnedItem = runSortedQueryWithFields(queryParm, queryParmValue, startIndex, count, mongoColl, buildDBObjectFromFields(fieldList), sortBy, sortOrder)
      }
    } else {
      if (sortBy == None) {
        returnedItem = runUnsortedQueryWithoutFields(queryParm, queryParmValue, startIndex, count, mongoColl)
      } else {
        returnedItem = runSortedQueryWithoutFields(queryParm, queryParmValue, startIndex, count, mongoColl, sortBy, sortOrder)
      }
    }

    returnedItem

  }

  //pass those params into existing method.  If they exist, create a new MongoDBObject

  def buildDBObjectFromFields(fieldList: List[String]) : MongoDBObject = {
    val builder = MongoDBObject.newBuilder

    //compute the fieldList for the MongoDBObject
    if (fieldList != Nil) {
      fieldList.foreach(x =>
        builder += x.toString -> 1
      )
    }
     builder.result
  }

  def runSortedQueryWithFields(queryParm : String, queryParmValue: Int, startIndex: Int, count: Int, mongoColl: MongoCollection, fieldObject : MongoDBObject, sortBy: Option[String], sortOrder: Int) : String = {

    var returnedItem = ""

    var mongoQuery = MongoDBObject()

    if(!"".equals(queryParm)) {
      mongoQuery = MongoDBObject(queryParm -> queryParmValue)
    }

    if (startIndex > 0) {
      val queryObject = mongoColl.find(mongoQuery, fieldObject).skip(startIndex).limit(count).sort(MongoDBObject(sortBy.get -> sortOrder))
      returnedItem = PokedexUtils.computeJSON(queryObject)
    } else {
      val queryObject = mongoColl.find(mongoQuery,fieldObject).limit(count).sort(MongoDBObject(sortBy.get -> sortOrder))
      returnedItem = PokedexUtils.computeJSON(queryObject)
    }
    returnedItem
  }

  def runSortedQueryWithoutFields(queryParm : String, queryParmValue: Int, startIndex: Int, count: Int, mongoColl: MongoCollection, sortBy: Option[String], sortOrder: Int) : String = {

    var returnedItem = ""

    var mongoQuery = MongoDBObject()

    if(!"".equals(queryParm)) {
      mongoQuery = MongoDBObject(queryParm -> queryParmValue)
    }

    if (startIndex > 0) {
      val queryObject = mongoColl.find(mongoQuery).skip(startIndex).limit(count).sort(MongoDBObject(sortBy.get -> sortOrder))
      returnedItem = PokedexUtils.computeJSON(queryObject)
    } else {
      val queryObject = mongoColl.find(mongoQuery).limit(count).sort(MongoDBObject(sortBy.get -> sortOrder))
      returnedItem = PokedexUtils.computeJSON(queryObject)
    }
    returnedItem
  }

  def runUnsortedQueryWithFields(queryParm : String, queryParmValue: Int, startIndex: Int, count: Int, mongoColl: MongoCollection, fieldObject : MongoDBObject) : String = {

    var returnedItem = ""

    var mongoQuery = MongoDBObject()

    if(!"".equals(queryParm)) {
      mongoQuery = MongoDBObject(queryParm -> queryParmValue)
    }


    if (startIndex > 0) {
      val queryObject = mongoColl.find(mongoQuery, fieldObject).skip(startIndex).limit(count)
      returnedItem = PokedexUtils.computeJSON(queryObject)
    } else {
      val queryObject = mongoColl.find(mongoQuery,fieldObject).limit(count)
      returnedItem = PokedexUtils.computeJSON(queryObject)
    }
    returnedItem
  }

  def runUnsortedQueryWithoutFields(queryParm : String, queryParmValue: Int, startIndex: Int, count: Int, mongoColl: MongoCollection) : String = {

    var returnedItem = ""

    var mongoQuery = MongoDBObject()

    if(!"".equals(queryParm)) {
      mongoQuery = MongoDBObject(queryParm -> queryParmValue)
    }

    if (startIndex > 0) {
      val queryObject = mongoColl.find(mongoQuery).skip(startIndex).limit(count)
      returnedItem = PokedexUtils.computeJSON(queryObject)
    } else {
      val queryObject = mongoColl.find(mongoQuery).limit(count)
      returnedItem = PokedexUtils.computeJSON(queryObject)
    }
    returnedItem
  }

  def Query_PokemonByName( name : String, mongoConn : MongoConnection) : String = {
    val pokemonColl = mongoConn("pokedex")("pokemon")
    val queryObject = pokemonColl.findOne(MongoDBObject("slug" -> name, "metadata.generation" -> 5))
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
    val queryObject = pokemonColl.findOne(MongoDBObject("metadata.nationalId" -> nationalId, "metadata.generation" -> 5))
    if (queryObject.size == 0) {
      return V3Utils.generateErrorJSON("Pokemon name is invalid.")  //HERE'S THE BUG
    }

    val returnedItem = JSON.serialize(queryObject)
    returnedItem

  }


  def Query_ObjectByObjectId(objectString: String, objectId: ObjectId, mongoConn: MongoConnection) : String =  {
    val objectColl = mongoConn("pokedex")(objectString)
    val queryObject = objectColl.findOne(MongoDBObject("_id" -> objectId ))
    var returnedItem = ""

    if (queryObject.size == 0) {
      returnedItem = V3Utils.generateErrorJSON("ObjectID does not exist.")
      return returnedItem
    }

    returnedItem = JSON.serialize(queryObject)
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
    val subQuery = $or(("moves.levelMoves."+move_id) -> MongoDBObject("$exists"-> true), ("moves.tutorMoves."+move_id) -> MongoDBObject("$exists" -> true), ("moves.hmMoves." + move_id) -> MongoDBObject("$exists" -> true), ("moves.tmMoves." + move_id) -> MongoDBObject("$exists" -> true))
    val allPokemonQueryObject = MongoDBObject("metadata.generation" -> generation) ++ subQuery
    returnedItem = PokedexUtils.executeMultipleQuery(mongoPokemonColl, allPokemonQueryObject)


    returnedItem

  }

  def Query_PokemonByMoveTypeLearned(move_type: String, move_id : String, generation: Int, mongoConn: MongoConnection) : String = {
    val mongoPokemonColl = mongoConn("pokedex")("pokemon")
    var moveClass = ""

    move_type match {
      case "tm" => moveClass = "tmMoves"
      case "hm" => moveClass = "hmMoves"
      case "level" => moveClass = "levelMoves"
      case "tutor" => moveClass = "tutorMoves"
      case _ =>  V3Utils.generateErrorJSON("Non-existant move type specified.")
    }


    var returnedItem =""
    //get all pokemon of generation 5, then search levelMoves
    val subQuery = ("moves." + moveClass + "." +move_id) -> MongoDBObject("$exists"-> true)
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
    //naming
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
    var winner = 0

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
      //get should return a float here
      pdamage = p2EfficacyObject.get(pkmType1.toString.toLowerCase).toString.toFloat
      pdamage2 = p2EfficacyObject.get(pkmType2.toString.toLowerCase).toString.toFloat
      pmax = max(pdamage, pdamage2)
    }
    // Pokemon2 type1 against pokemon1 efficacy
    //match
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
       //case statement
    if (statDiff <= 100) {
      // Type wins
      if (maxDamage<=100) battleScore = 70
      if (maxDamage>100) battleScore = 80
      if (maxDamage>200) battleScore = 90
      if (pmax > p2max){
        // pokemon 1 wins
        battleOutcomeMessage = queryObject1.get("slug")+" has type advantage"
        winner = Metadata.get("nationalId").toString.toInt
      }
      else if (p2max > pmax) {
        // pokemon 2 wins
        battleOutcomeMessage = queryObject2.get("slug")+" has type advantage"
        winner = Metadata2.get("nationalId").toString.toInt
      }
      else if (p2max == pmax) {
        // tie
        if (pkm1TotalStats > pkm2TotalStats) {
          battleScore = 60
          battleOutcomeMessage = queryObject1.get("slug")+" has a slight stat advantage"
          winner = Metadata.get("nationalId").toString.toInt
        }
        else if (pkm2TotalStats > pkm1TotalStats) {
          battleScore = 60
          battleOutcomeMessage = queryObject2.get("slug")+" has a slight stat advantage"
          winner = Metadata2.get("nationalId").toString.toInt
        }
        else if (pkm2TotalStats == pkm1TotalStats){
          battleScore = 50
          battleOutcomeMessage = "tie"
          winner = -1
        }
      }
    }
    else {

      // Stats wins
      if (statDiff<=100) battleScore = 70
      if (statDiff>100) battleScore = 80
      if (statDiff>200) battleScore = 90
      if (pkm1TotalStats > pkm2TotalStats){
        // pokemon 1 wins
        battleOutcomeMessage = queryObject1.get("slug").toString+" has greater stats advantage"
        winner = Metadata.get("nationalId").toString.toInt
      } else if (pkm2TotalStats > pkm1TotalStats){
        battleOutcomeMessage = queryObject2.get("slug").toString+" has greater stats advantage"
        winner = Metadata2.get("nationalId").toString.toInt
      } else if (pkm2TotalStats == pkm1TotalStats){
        battleScore = 50
        battleOutcomeMessage = "too close to call!"
        winner = -1
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
      "\"outcomeMessage\":" + "\""+battleOutcomeMessage+"\"," +
      "\"winner\":"+ winner.toString +"}"
    comparatorJSON
  }

  def Query_ComparatorById2(id1 : Int, id2 : Int, mongoConn : MongoConnection) : String = {

    val pokemonColl = mongoConn("pokedex")("pokemon")
    val queryObject1 = pokemonColl.findOne(MongoDBObject("metadata.nationalId" -> id1, "metadata.generation" -> 5))
    val queryObject2 = pokemonColl.findOne(MongoDBObject("metadata.nationalId" -> id2, "metadata.generation" -> 5))

    if (queryObject1.isEmpty || queryObject2.isEmpty)
      return V3Utils.generateErrorJSON("bad news")
    //naming
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
    var winner = 0

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
      //get should return a float here
      pdamage = p2EfficacyObject.get(pkmType1.toString.toLowerCase).toString.toFloat
      pdamage2 = p2EfficacyObject.get(pkmType2.toString.toLowerCase).toString.toFloat
      pmax = max(pdamage, pdamage2)
    }
    // Pokemon2 type1 against pokemon1 efficacy
    //match
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

    var pkm1Health = Metadata.get("hp").toString.toFloat
    var pkm2Health = Metadata2.get("hp").toString.toFloat

    var pkm1AllAttack =  Metadata.get("attack").toString.toFloat + Metadata.get("specialAttack").toString.toFloat
    var pkm1AllDefense =  Metadata.get("defense").toString.toFloat + Metadata.get("specialDefense").toString.toFloat
    var pkm2AllAttack = Metadata2.get("attack").toString.toFloat + Metadata2.get("specialAttack").toString.toFloat
    var pkm2AllDefense = Metadata2.get("defense").toString.toFloat + Metadata2.get("specialDefense").toString.toFloat
    var pkm1AttackDamage = pkm1AllAttack - pkm2AllDefense
    var pkm2AttackDamage = pkm2AllAttack - pkm1AllDefense

    if (pkm1AttackDamage < 0) pkm1AttackDamage = 1
    if (pkm2AttackDamage < 0) pkm2AttackDamage = 1


    println("pkm1AllAttack: " + pkm1AllAttack)
    println("pkm1AllDefense:" + pkm1AllDefense)

    println("pkm2AllAttack: " + pkm2AllAttack)
    println("pkm2AllDefense:" + pkm2AllDefense)

    println("pkm1AttackDamage: " + pkm1AttackDamage)
    println("pkm2AttackDamage:" + pkm2AttackDamage)



    var p1GoesFirst = true

    if (Metadata.get("speed").toString.toFloat < Metadata2.get("speed").toString.toFloat) {
      p1GoesFirst = false
      println("pkm2 with speed advantage")
    } else {
      println("pkm1 with speed advantage")
    }

    if ((pkm1AllAttack == pkm2AllDefense) && (pkm2AllAttack == pkm1AllDefense)) {
      //too close to call (this will loop forever)
       pkm1Health = 0
       pkm2Health = 0

    } else {

    while(pkm1Health > 0 && pkm2Health > 0) {

      println("In the Loop! pkm1Health: " + pkm1Health)
      println("In the Loop! pkm2Health:" + pkm2Health)

      if (p1GoesFirst) {
        pkm2Health = pkm2Health - pkm1AttackDamage
        pkm1Health = pkm1Health - pkm2AttackDamage
        println("Battling! pkm1Health: " + pkm1Health)
        println("Battling! pkm2Health:" + pkm2Health)
      } else {
        pkm1Health = pkm1Health - pkm2AttackDamage
        pkm2Health = pkm2Health - pkm1AttackDamage
        println("Battling! pkm1Health: " + pkm1Health)
        println("Battling! pkm2Health:" + pkm2Health)
      }

      println("Modification complete! pkm1Health: " + pkm1Health)
      println("Modification complete! pkm2Health:" + pkm2Health)
    }

    }

    var statDiff = if (pkm1Health > 0 ) pkm1Health else pkm2Health

    println("Results:")
    println("pkm1: " + pkm1Health)
    println("pkm2: " + pkm2Health)

    val maxDamage = abs(pmax-p2max)
    var battleScore = 0.0
    var battleOutcomeMessage = ""
    //case statement
    if (statDiff <= 20) {

      println("inside of type advantage")
      println("p1max: " + pmax)
      println("p2max: " + p2max)
      // Type wins
      if (maxDamage<=100) battleScore = 70
      if (maxDamage>100) battleScore = 80
      if (maxDamage>200) battleScore = 90
      if (pmax > p2max){
        // pokemon 1 wins
        battleOutcomeMessage = queryObject1.get("slug")+" has type advantage"
        winner = Metadata.get("nationalId").toString.toInt
      }
      else if (p2max > pmax) {
        // pokemon 2 wins
        battleOutcomeMessage = queryObject2.get("slug")+" has type advantage"
        winner = Metadata2.get("nationalId").toString.toInt
      }
      else if (p2max == pmax) {
        // tie
        if (pkm1Health > pkm2Health) {
          battleScore = 60
          battleOutcomeMessage = queryObject1.get("slug")+" has a slight stat advantage"
          winner = Metadata.get("nationalId").toString.toInt
        }
        else if (pkm2Health > pkm1Health) {
          battleScore = 60
          battleOutcomeMessage = queryObject2.get("slug")+" has a slight stat advantage"
          winner = Metadata2.get("nationalId").toString.toInt
        }
        else if (pkm2Health == pkm1Health){
          battleScore = 50
          battleOutcomeMessage = "tie"
          winner = -1
        }
      }
    }
    else {

      // Stats wins
      if (statDiff<=100) battleScore = 70
      if (statDiff>100) battleScore = 80
      if (statDiff>200) battleScore = 90
      if (pkm1Health > pkm2Health){
        // pokemon 1 wins
        battleOutcomeMessage = queryObject1.get("slug").toString+" has greater stats advantage"
        winner = Metadata.get("nationalId").toString.toInt
      } else if (pkm2Health > pkm1Health){
        battleOutcomeMessage = queryObject2.get("slug").toString+" has greater stats advantage"
        winner = Metadata2.get("nationalId").toString.toInt
      } else if (pkm2Health == pkm1Health){
        battleScore = 50
        battleOutcomeMessage = "too close to call!"
        winner = -1
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
      "\"outcomeMessage\":" + "\""+battleOutcomeMessage+"\"," +
      "\"winner\":"+ winner.toString +"}"
    comparatorJSON
  }

}
