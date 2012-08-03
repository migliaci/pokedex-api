package com.ign.pokedex

/**
 * Created with IntelliJ IDEA.
 * User: mmigliacio
 * Date: 7/31/12
 * Time: 6:49 PM
 * To change this template use File | Settings | File Templates.
 */

import org.scalatra._
import java.net.URL
import scalate.ScalateSupport
import java.io.PrintWriter
import scala.util.parsing.json.JSONObject
import com.mongodb.casbah.Imports._
import com.mongodb.util.JSON
import javax.servlet.http.HttpServletResponse

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
    val json = connectToDB("pokemonId", "001BulbasaurV", "pokemon", response);
    println( "Bai" )
    response.getWriter.write(json)


  }

  get("/moves") {
    response.setContentType("application/json");
    println( "Move Time!" )
    println( "Here we go! ")
    //val json = connectToDB("moveId", "001", "move", response)
    connectToDB_Multiple("moveId", "001", "move", response)
    println( "Bai" )

  }

  get("/pokemans") {

    println( "Hello Unova!" )
    println( "Here we go! ")
    val json = connectToDB("pokemonId", "001BulbasaurV", "pokemon", response)
    println( "Bai" )
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


  def getTestPokemon(): MongoDBObject = {

    val objectToReturn = MongoDBObject(
      "pokemonId" -> "001BulbasaurV",
      "metadata" -> MongoDBObject(
        "name" -> "Bulbasaur",
        "jpName" -> "フシギダネ",
        "imageURL"->"someUrl",
        "generation"->1,
        "height"->10,
        "weight"->20,
        "form"->"normal",
        "eggCycles"->21,
        "maleGenderPercent"->89,
        "species"->"someSpecies",
        "primaryType"->"grass",
        "secondaryType"->"poison",
        "attack"->44,
        "specialAttack"->45,
        "defense"->30,
        "specialDefence"->45,
        "speed"->45,
        "hp"->100,
        "nationalId"->"001")
    )
    objectToReturn
  }

  def getTestMove(): MongoDBObject = {

    val objectToReturn = MongoDBObject(
      "moveId" -> "001",
      "metadata" -> MongoDBObject(
        "name" -> "Hyper Beam",
        "category"->"Special",
        "power"->100,
        "accuracy"->75,
        "pp"->10,
        "maxpp"->20,
        "introducedInGeneration"->1,
        "description"->"I PWN YOU.")
    )
    return objectToReturn
  }

  def getTestMove2(): MongoDBObject = {

    val objectToReturn = MongoDBObject(
      "moveId" -> "002",
      "metadata" -> MongoDBObject(
        "name" -> "Fire Blast",
        "category"->"Special",
        "power"->200,
        "accuracy"->55,
        "pp"->5,
        "maxpp"->10,
        "introducedInGeneration"->1,
        "description"->"YOGA FIRE!")
    )
    return objectToReturn
  }

  def cleanupDB(m: MongoCollection) = {
    m.drop()
  }

  def connectToDB(obj_id: String, obj_val: String, obj_type: String, response: HttpServletResponse) :String =  {
    val mongoColl = MongoConnection()("pokedex")("test_data")
    var returnedItem =""

    //save to the DB

    if (obj_type == "pokemon"){
      mongoColl += this.getTestPokemon()
    } else if (obj_type == "move") {
      mongoColl += this.getTestMove()
    }

    mongoColl.find()


    val q = MongoDBObject("pokemonId" -> "001BulbasaurV")
    for (x <- mongoColl.find(q)) returnedItem = JSON.serialize(x)

    cleanupDB(mongoColl)
    returnedItem

  }

  def connectToDB_Multiple(obj_id: String, obj_val: String, obj_type: String, response: HttpServletResponse)  {
    val mongoColl = MongoConnection()("pokedex")("test_data")
    var returnedItem =""

    //save to the DB
    mongoColl += this.getTestMove()
    mongoColl += this.getTestMove2()
    mongoColl.find()

    var count = 0

    //val q = "moveId" $exists true
    //val q = MongoDBObject("moveId" -> "001")

    //can use MongoDB query logic inside of MongoDBObject constructor
    val q = MongoDBObject("metadata.category"->"Special")

    for (x <- mongoColl.find(q))
    {
      println("count " + count)
      if (count == 0){
         returnedItem += ("[" + JSON.serialize(x) + ",")
         println("Current returned item: " + returnedItem)
      }else {
        returnedItem += JSON.serialize(x) + ","
        println("Current returned item: " + returnedItem)
      }

      count+=1;
    }

    if(returnedItem != "")  {
    returnedItem = returnedItem.substring(0, returnedItem.length-1)
    returnedItem += "]";
    }


    cleanupDB(mongoColl)

    println(returnedItem)
    response.getWriter.write(returnedItem)


  }

}
