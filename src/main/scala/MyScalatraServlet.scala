package net.srirangan

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

  get("/somejson") {

    response.setContentType("application/json");



  }

  get("/pokemans") {

    println( "Hello Unova!" )
    println( "Here we go! ")
    connectToDB()
    println( "Bai" )
    //response.getWriter().write(json.toString());

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
    // Try to render a ScalateTemplate if no route matched
    /*
    findTemplate(requestPath) map { path =>
      contentType = "text/html"
      layoutTemplate(path)
    } orElse serveStaticResource() getOrElse resourceNotFound()
    */
  }


  //clear database
  //create database
  //add stuff
  //delete stuff
  //start mapping out teh pokemans

  def getTestObject(): MongoDBObject = {

    /*
    val builder = MongoDBObject.newBuilder
    builder += "pokemonId" -> "001BulbasaurV"
    val newObj = builder.result
    */

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
    return objectToReturn
  }

  def cleanupDB(m: MongoCollection) = {
    m.drop();
  }

  def connectToDB() {
    val mongoColl = MongoConnection()("pokedex")("test_data")

    //save to the DB
    mongoColl += this.getTestObject()
    mongoColl.find()

    val q = MongoDBObject("pokemonId" -> "001BulbasaurV")
    for (x <- mongoColl.find(q)) println(x)//returnedItem = x //println(x);

    cleanupDB(mongoColl)
  }

}
