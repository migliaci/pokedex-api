package com.ign.pokedex

/**
 * Created with IntelliJ IDEA.
 * User: mmigliacio
 * Date: 8/10/12
 * Time: 9:27 PM
 * To change this template use File | Settings | File Templates.
 */


/**
 * Case class that represents a request from the client
 * <p>
 * Ideally, this should be broken up into a number of classes that extend from
 * a class that only contains the core set of fields, but scala doesn't allow
 * extension of case classes. It is also unclear how the core apply() method
 * could be built upon.
 */
case class APIRequest(var startIndex: Int = -1,
                      var count: Int = -1,
                      var sortBy: Option[String] = None,
                      var sortOrder: Int = -1,
                      var fields: List[String] = Nil,
                      var nationalId: Int = -1,
                      var name: String = "",
                      var generation: Int = -1,
                      var expand: Int = -1,
                      var type1: String = "",
                      var type2: String = "",
                      var firstPokemonId: Int = -1,
                      var secondPokemonId: Int = -1,
                      var evolutionChainId: Int = -1,
                      var moveId: String = "",
                      var moveName: String = "",
                      var moveCategory: String = "",
                      var moveType: String = "",
                      var pokemonName: String = "",
                      var pokemonMoveClass: String = ""
                      /*
                      // common search criteria, most will be None for any given request
                      var query: Option[String] = None, // used for autocomplete support
                      var id: String = "",
                      var slug: String = "",
                      var legacyId: String = "",
                      var typ: String = "", // "type" is a reserved word, so spell it "typ"
                      var q: String = "", // used for /search queries

                      // release-specific fields
                      var states: List[String] = Nil,
                      var regions: List[String] = Nil */)

object APIRequest {
  def apply(params: Map[String,String]) : APIRequest = {
    val req = APIRequest()
    if (params != null) {
      params.keys.foreach(x => x match {
        case "startIndex" =>
          try {
            req.startIndex = params(x).toInt
          } catch {
            case e:NumberFormatException => req.startIndex = -1
          }
        case "count" =>
          try {
            req.count = params(x).toInt
          } catch {
            case e:NumberFormatException => req.count = -1
          }
        case "sortBy" => req.sortBy = Some(params(x))
        case "sortOrder" if params(x).equals("asc") => req.sortOrder = 1
        case "fields" => req.fields = params(x).split(",").toList
        case "name" => req.name = params(x)
        case "nationalId" =>
          try {
            req.nationalId = params(x).toInt
          } catch {
            case e:NumberFormatException => req.nationalId = -1
          }
        case "generation" =>
          try {
            req.generation = params(x).toInt
          } catch {
            case e:NumberFormatException => req.generation = -1
          }
        case "expand" if params(x).equals("true") => req.expand = 1
        case "type1" => req.type1 = params(x)
        case "type2" => req.type2 = params(x)
        case "firstPokemonId" => req.firstPokemonId = params(x).toInt
          try {
            req.firstPokemonId = params(x).toInt
          } catch {
          case e:NumberFormatException => req.firstPokemonId = -1
          }
        case "secondPokemonId" => req.secondPokemonId = params(x).toInt
          try {
            req.firstPokemonId = params(x).toInt
          } catch {
            case e:NumberFormatException => req.firstPokemonId = -1
          }
        case "evolutionChainId" =>
          try {
          req.evolutionChainId = params(x).toInt
        } catch {
          case e:NumberFormatException => req.evolutionChainId = -1
        }
        case "moveName" => req.moveName = params(x)
        case "moveCategory" => req.moveCategory = params(x)
        case "moveType" => req.moveType = params(x)
        case "pokemonName" => req.pokemonName = params(x)
        case "moveId" => req.moveId = params(x)
        case "pokemonMoveClass" => req.pokemonMoveClass = params(x)
        /*
        var moveName: String = "",
                      var moveCategory: String = "",
                      var moveType: String = "",
                      var pokemonName: String = ""
         */
        //name, nationalId, generation
        /*
        case "query" => req.query = Some(params(x)) // used for autocomplete support
        case "id" => req.id = params(x)
        case "slug" => req.slug = params(x)
        case "legacyId" => req.legacyId = params(x)
        case "type" => req.typ = params(x) // note the parameter name is spelled correctly ("type") but the field is spelled "typ" because "type" is a reserved word
        case "q" => req.q = params(x) // used for /search queries
        case "metadata.state" => req.states = if (params(x) != "") params(x).split(",").toList else Nil // if present but value == "", don't return List("")
        case "metadata.region" => req.regions = if (params(x) != "") params(x).split(",").toList else Nil // if present but value == "", don't return List("")
        */
        case _ =>

      })
    }
    req
  }
}