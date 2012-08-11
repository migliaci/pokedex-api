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

  get("/pokemon/unfiltered/:size"){
    val size:Int = params.getOrElse("size", "20").toInt
    response.setContentType("application/json")
    response.getWriter.write(QueryManager.Query_PokemonBySizeUnfiltered(size, mongo))
  }

  get("/pokemon/all/:size"){
    val size:Int = params.getOrElse("size", "20").toInt
    response.setContentType("application/json")
    response.getWriter.write(QueryManager.Query_PokemonBySizeAndGenerationFiltered(size, 5, mongo))
  }

  get("/pokemon/all/:size/:generation"){
    val size:Int = params.getOrElse("size", "20").toInt
    val generation:Int = params.getOrElse("generation", "5").toInt
    response.setContentType("application/json")
    response.getWriter.write(QueryManager.Query_PokemonBySizeAndGenerationFiltered(size, generation, mongo))
  }

  get("/pokemon/all/:low/to/:high/:generation"){
    val low:Int = params.getOrElse("low", "1").toInt
    val high:Int = params.getOrElse("high", "20").toInt
    val generation:Int = params.getOrElse("generation", "5").toInt
    response.setContentType("application/json")
    response.getWriter.write(QueryManager.Query_PokemonByRange(low, high, generation, mongo))
  }

  get("/pokemon/:name"){
    val name:String = params.getOrElse("name", halt(400))
    response.setContentType("application/json")
    response.getWriter.write(QueryManager.Query_PokemonByName(name, mongo))
  }

  get("/pokemon/national_id/:id"){
    val id:Int = params.getOrElse("id", "1").toInt
    response.setContentType("application/json")
    response.getWriter.write(QueryManager.Query_PokemonByNationalId(id, mongo))
  }

  get("/pokemon/national_id/:id/generation/:generation"){
    val id:Int = params.getOrElse("id", "1").toInt
    val generation:Int = params.getOrElse("generation", "5").toInt
    response.setContentType("application/json")
    response.getWriter.write(QueryManager.Query_PokemonByNationalIdAndGeneration(id, generation, mongo))
  }

  //pokemon/name/:name/generation/:generation?
  get("/pokemon/:name/generation/:generation"){
    val name:String = params.getOrElse("name", halt(400))
    val generation:Int = params.getOrElse("generation", "5").toInt
    response.setContentType("application/json")
    response.getWriter.write(QueryManager.Query_PokemonByNameAndGeneration(name, generation, mongo))
  }

  get("/moves") {
    response.setContentType("application/json")
    println( "in all moves query" )
    response.getWriter.write(QueryManager.Query_AllMoves(mongo))
    println( "Bai" )

  }


  get("/moves/:name") {
    response.setContentType("application/json")
    println("in moves name query")
    val name:String = params.getOrElse("name", halt(400))
    response.getWriter.write(QueryManager.Query_MovesBySingleParameter("metadata.name", name, mongo))

  }

  get("/types/efficacy/type1/:type1/type2/:type2") {
    response.setContentType("application/json")
    println("in types test query")
    var type1:String = params.getOrElse("type1", halt(400))
    var type2:String = params.getOrElse("type2", halt(400))
    response.getWriter.write(QueryManager.Query_EfficacyByMultipleType(type1, type2, mongo))
  }

  get("/types/efficacy/type1/:type1") {
    response.setContentType("application/json")
    println("in types test query")
    var type1:String = params.getOrElse("type1", halt(400))
    response.getWriter.write(QueryManager.Query_EfficacyBySingleType(type1, mongo))
  }

  get("/moves/pokemon/:id"){
    response.setContentType("application/json")
    val moveId:String = params.getOrElse("id", "1").toString
    println("inside move query for pokemon")
    response.getWriter.write(QueryManager.Query_PokemonByMoveLearned(moveId, 5, mongo))

  }

  //moves/level/pokemon/id?  add generation?
  get("/moves/pokemon/level/:id"){
    response.setContentType("application/json")
    val moveId:String = params.getOrElse("id", "1").toString
    println("inside move query for pokemon")
    response.getWriter.write(QueryManager.Query_PokemonByLevelMoveLearned(moveId, 5, mongo))

  }
  //moves/tm/pokemon/id? add generation?
  get("/moves/pokemon/tm/:id"){
    response.setContentType("application/json")
    val moveId:String = params.getOrElse("id", "1").toString
    println("inside move query for pokemon")
    response.getWriter.write(QueryManager.Query_PokemonByTMLearned(moveId, 5, mongo))

  }
  //moves/hm/pokemon/id? add generation?
  get("/moves/pokemon/hm/:id"){
    response.setContentType("application/json")
    val moveId:String = params.getOrElse("id", "1").toString
    println("inside move query for pokemon")
    response.getWriter.write(QueryManager.Query_PokemonByHMLearned(moveId, 5, mongo))

  }


  get("/moves/category/:category") {
    response.setContentType("application/json")
    println("in moves category query")
    val category:String = params.getOrElse("category", halt(400))
    response.getWriter.write(QueryManager.Query_MovesBySingleParameter("metadata.category", category, mongo))

  }

  get("/moves/type/:type") {
    response.setContentType("application/json")
    println("in moves type query")
    val moveType:String = params.getOrElse("type", halt(400))
    response.getWriter.write(QueryManager.Query_MovesBySingleParameter("metadata.type", moveType, mongo))
    println( "Bai" )

  }

  get("/comparator/pokemon1/:id1/pokemon2/:id2") {
    response.setContentType("application/json")
    val id1:Int = params.getOrElse("id1", halt(400)).toInt
    val id2:Int = params.getOrElse("id2", halt(400)).toInt
    response.getWriter.write(QueryManager.Query_ComparatorById(id1, id2, mongo))
  }

  //evolutions/pokemon/national_id/id?
  //add one for evolutions/pokemon/name/name?
  get("/evolutions/pokemon/:national_id") {
    response.setContentType("application/json")
    println("in evolution query by id")
    val nationalId:Int = params.getOrElse("national_id", halt(400)).toInt
    response.getWriter.write(QueryManager.Query_EvolutionsByNationalId(nationalId, mongo))
    println( "Bai" )

  }

  get("/evolutions/chain/:chain_id") {
    response.setContentType("application/json")
    println("in evolution query by chain")
    val chainId:Int = params.getOrElse("chain_id", halt(400)).toInt
    response.getWriter.write(QueryManager.Query_EvolutionsByChainId(chainId, mongo))
    println( "Bai" )

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
