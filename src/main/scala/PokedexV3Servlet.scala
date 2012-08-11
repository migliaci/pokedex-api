package com.ign.pokedex

import com.mongodb.casbah.Imports._
import org.scalatra.ScalatraServlet

/**
 * Created with IntelliJ IDEA.
 * User: mmigliacio
 * Date: 8/11/12
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */

class PokedexV3Servlet extends ScalatraServlet {

  var mongo = MongoConnection()

  get("/") {
    <html>
      <body>
        <h1>Hello, V3 API Unova!</h1>
        <img src="http://www.vgblogger.com/wp-content/uploads/2006/11/30/ShowYouPokemans.jpg" alt="Pokemon" /><br></br>
        <h2>Say hello to our V3 Pokemans.</h2>
      </body>
    </html>
  }

  get("/pokemon") {
    val req = APIRequest(params.toMap[String,String])
    response.setContentType("application/json")
    response.getWriter.write(V3Utils.processPokemonEndpoint(params.toMap[String, String], req, mongo))
  }

  get("/types/efficacy") {
    val req = APIRequest(params.toMap[String,String])
    response.setContentType("application/json")
    response.getWriter.write(V3Utils.processTypesEndpoint(params.toMap[String, String], req, mongo))
  }

  get("/comparator") {
    val req = APIRequest(params.toMap[String,String])
    response.setContentType("application/json")
    response.getWriter.write(V3Utils.processComparatorEndpoint(params.toMap[String, String], req, mongo))

  }

  get("/evolutions/pokemon") {
    val req = APIRequest(params.toMap[String,String])
    response.setContentType("application/json")
    response.getWriter.write(V3Utils.processEvolutionsPokemonEndpoint(params.toMap[String, String], req, mongo))
  }

  get("/evolutions/chain") {
    val req = APIRequest(params.toMap[String,String])
    response.setContentType("application/json")
    response.getWriter.write(V3Utils.processEvolutionsChainEndpoint(params.toMap[String, String], req, mongo))
  }

  get("/moves") {
    val req = APIRequest(params.toMap[String,String])
    response.setContentType("application/json")
    response.getWriter.write(V3Utils.processMovesEndpoint(params.toMap[String, String], req, mongo))
  }

  get("/moves/pokemon") {
    val req = APIRequest(params.toMap[String,String])
    response.setContentType("application/json")
    response.getWriter.write(V3Utils.processMovesPokemonEndpoint(params.toMap[String, String], req, mongo))
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
