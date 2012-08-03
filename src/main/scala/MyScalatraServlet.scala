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

  get("/pokemon/all/:size"){
    val size:Int = params.getOrElse("size", "20").toInt
    response.setContentType("application/json")
    val pokeColl = MongoConnection()("pokedex")("pokedex_data")
    val q  = MongoDBObject.empty
    val fields = MongoDBObject("metadata.name" -> 1)
    response.getWriter.write('[')
    for (x <- pokeColl.find(q, fields).limit(size)) {
      response.getWriter.write(JSON.serialize(x))

    }
    response.getWriter.write(']')
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

  get("/somejson") {

    response.setContentType("application/json")
    println( "Hello Unova!" )
    println( "Here we go! ")
    val json = connectToDB("pokemonId", "001BulbasaurV", "pokemon", response)
    println( "Bai" )
    response.getWriter.write(json)


  }

  get("/moves") {
    response.setContentType("application/json")
    println( "in all moves query" )
    response.getWriter.write(this.connectToDB_MoveQuery)
    println( "Bai" )

  }

  get("/moves/:name") {
    response.setContentType("application/json")
    println("in moves name query")
    val name:String = params.getOrElse("name", halt(400))
    response.getWriter.write(this.connectToDB_MoveSingleParameterQuery("metadata.name", name))

  }

  get("/moves/category/:category") {
    response.setContentType("application/json")
    println("in moves category query")
    val category:String = params.getOrElse("category", halt(400))
    response.getWriter.write(this.connectToDB_MoveSingleParameterQuery("metadata.category", category))

  }

  get("/moves/type/:type") {
    response.setContentType("application/json")
    println("in moves type query")
    val moveType:String = params.getOrElse("type", halt(400))
    response.getWriter.write(this.connectToDB_MoveSingleParameterQuery("metadata.type", moveType))
    println( "Bai" )

  }


  get("/pokemans") {

    val json = connectToDB("pokemonId", "001BulbasaurV", "pokemon", response)
    response.getWriter.write(json)

    <html>
      <body>
        <h1>LET ME SHOW YOU MY POKEMANZ!</h1>
        <h2>LET ME SHOW YOU THEM!</h2>
        <ul>
          <li>MUDKIPZ</li>
          <li>MAGICARP</li>
          <li>GOLDEEN</li>
        </ul>
      </body>
    </html>
  }

  notFound {

    <html>
      <body>
        <h1>NOT found</h1>
      </body>
    </html>

  }

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

  def connectToDB_MoveQuery() : String = {

    val mongoColl = MongoConnection()("pokedex")("test_data")
    var returnedItem =""

    //save to the DB
    mongoColl += PokedexTestGenerator.getTestMove()
    mongoColl += PokedexTestGenerator.getTestMove2()
    mongoColl.find()

    returnedItem = PokedexUtils.executeMultipleQuery(mongoColl,("moveId" $exists true))

    PokedexUtils.cleanupDB(mongoColl)

    returnedItem
  }


}
