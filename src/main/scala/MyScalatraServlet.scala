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

class MyScalatraServlet extends ScalatraServlet {

  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say
        <a href="hello-scalate">hello to Scalate</a>
        .
        <h2>OH RLY?</h2>
      </body>
    </html>
  }


  get("/hello/:name") {
    // Matches "GET /hello/foo" and "GET /hello/bar"
    // params("name") is "foo" or "bar"
    <p>Hello,
      {params("name")}
    </p>
  }

  /*
    SELECT types.identifier,
	type_efficacy.damage_type_id,
	type_efficacy.target_type_id,
	type_efficacy.damage_factor
FROM type_efficacy INNER JOIN types ON type_efficacy.damage_type_id = types.id
WHERE types.identifier="normal"
   */


  get("/pokemon/all/:size"){
    val size:Int = params.getOrElse("size", "20").toInt
    response.setContentType("application/json")
    val pokeColl = MongoConnection()("pokedex")("pokedex_data")
    val q  = MongoDBObject("metadata.generation" -> 5)
    val fields = MongoDBObject("metadata.name" -> 1, "metadata.generation" -> 1, "metadata.nationalId" -> 1)
    var jsonString = "["
    for (x <- pokeColl.find(q, fields).limit(size).sort(MongoDBObject("metadata.nationalId" -> 1))) {
      jsonString += (JSON.serialize(x)+",")
    }
    response.getWriter.write(jsonString.substring(0, jsonString.length -1)+"]")
  }

  get("/pokemon/all/:size/:generation"){
    val size:Int = params.getOrElse("size", "20").toInt
    val generation:Int = params.getOrElse("generation", "5").toInt
    response.setContentType("application/json")
    val pokeColl = MongoConnection()("pokedex")("pokedex_data")
    val q  = MongoDBObject("metadata.generation" -> generation)
    val fields = MongoDBObject("metadata.name" -> 1, "metadata.generation" -> 1, "metadata.nationalId" -> 1)
    var jsonString = "["
    for (x <- pokeColl.find(q, fields).limit(size).sort(MongoDBObject("metadata.nationalId" -> 1))) {
      jsonString += (JSON.serialize(x)+",")
    }
    response.getWriter.write(jsonString.substring(0, jsonString.length -1)+"]")
  }

  get("/pokemon/:name"){
    response.setContentType("application/json")
    val name:String = params.getOrElse("name", halt(400))
    val pokeColl = MongoConnection()("pokedex")("pokedex_data")
    val p = MongoDBObject("metadata.name" -> name)
    pokeColl.findOne(p).foreach { x =>
      response.getWriter.write(JSON.serialize(x))
    //  println("Found a pokemon! %s".format(x("metadata")))
    }
  }

  get("/pokemon/:name/generation/:generation"){
    response.setContentType("application/json")
    val name:String = params.getOrElse("name", halt(400))
    val generation:Int = params.getOrElse("generation", "5").toInt
    val pokeColl = MongoConnection()("pokedex")("pokedex_data")
    val p = MongoDBObject("metadata.name" -> name, "metadata.generation" -> generation)
    println(p)
    pokeColl.findOne(p).foreach { x =>
      response.getWriter.write(JSON.serialize(x))
      //  println("Found a pokemon! %s".format(x("metadata")))
    }
  }

  get("/moves") {
    response.setContentType("application/json")
    println( "in all moves query" )
    response.getWriter.write(QueryManager.Query_AllMoves())
    println( "Bai" )

  }


  get("/moves/:name") {
    response.setContentType("application/json")
    println("in moves name query")
    val name:String = params.getOrElse("name", halt(400))
    response.getWriter.write(QueryManager.Query_MovesBySingleParameter("metadata.name", name))

  }

  get("/types") {

  }

  get("/types/weak-to/:name") {

  }

  get("/types/strong-against/:name") {

  }

  //negative relation - does less damage or zero damage against passed type.
  get("/types/relation/negative/:name") {

  }

  //positive relation - does 1.5-2x damage against passed type.
  get("/types/relation/positive/:name") {

  }
  // moves/id/pokemon
  // search on level moves and machine moves
  // return list of pokemon that learn it, set default generation 5


  get("/moves/pokemon/:id"){
    response.setContentType("application/json")
    val moveId:String = params.getOrElse("id", "1").toString
    println("inside move query for pokemon")
    response.getWriter.write(QueryManager.Query_PokemonByMoveLearned(moveId))

  }

  get("/moves/pokemon/level/:id"){
    response.setContentType("application/json")
    val moveId:String = params.getOrElse("id", "1").toString
    println("inside move query for pokemon")
    response.getWriter.write(QueryManager.Query_PokemonByLevelMoveLearned(moveId))

  }
  //get("/articles-by/:author/:page") {
  get("/moves/pokemon/machine/:id"){
    response.setContentType("application/json")
    val moveId:String = params.getOrElse("id", "1").toString
    println("inside move query for pokemon")
    response.getWriter.write(QueryManager.Query_PokemonByMachineMoveLearned(moveId))

  }


  get("/moves/category/:category") {
    response.setContentType("application/json")
    println("in moves category query")
    val category:String = params.getOrElse("category", halt(400))
    response.getWriter.write(QueryManager.Query_MovesBySingleParameter("metadata.category", category))

  }

  get("/moves/type/:type") {
    response.setContentType("application/json")
    println("in moves type query")
    val moveType:String = params.getOrElse("type", halt(400))
    response.getWriter.write(QueryManager.Query_MovesBySingleParameter("metadata.type", moveType))
    println( "Bai" )

  }

  get("/evolutions/pokemon/:national_id") {
    response.setContentType("application/json")
    println("in evolution query by id")
    val nationalId:Int = params.getOrElse("national_id", halt(400)).toInt
    response.getWriter.write(QueryManager.Query_EvolutionsByNationalId(nationalId))
    println( "Bai" )

  }

  get("/evolutions/chain/:chain_id") {
    response.setContentType("application/json")
    println("in evolution query by chain")
    val chainId:Int = params.getOrElse("chain_id", halt(400)).toInt
    response.getWriter.write(QueryManager.Query_EvolutionsByChainId(chainId))
    println( "Bai" )

  }

  before {
    PokedexTestGenerator.setupTestDatabase()
  }

  after {
    PokedexTestGenerator.deleteTestDatabase()
  }

  notFound {

    <html>
      <body>
        <h1>NOT found</h1>
      </body>
    </html>

  }

  def connectToDB_MoveQuery() : String = {

    val mongoColl = MongoConnection()("pokedex")("moves")
    var returnedItem =""

    //save to the DB
    mongoColl += PokedexTestGenerator.getTestMove1()
    mongoColl += PokedexTestGenerator.getTestMove2()
    mongoColl.find()

    returnedItem = PokedexUtils.executeMultipleQuery(mongoColl,("moveId" $exists true))

    PokedexUtils.cleanupDB(mongoColl)

    returnedItem
  }


}
