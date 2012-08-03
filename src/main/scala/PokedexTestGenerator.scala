package com.ign.pokedex

import com.mongodb.casbah.Imports._

/**
 * Created with IntelliJ IDEA.
 * User: mmigliacio
 * Date: 8/3/12
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */

object PokedexTestGenerator {
  def getTestPokemon(): MongoDBObject = {

    val objectToReturn = MongoDBObject(
      "pokemonId" -> "001BulbasaurV",
      "metadata" -> MongoDBObject(
        "name" -> "Bulbasaur",
        "jpName" -> "フシギダネ",
        "imageURL" -> "someUrl",
        "generation" -> 1,
        "height" -> 10,
        "weight" -> 20,
        "form" -> "normal",
        "eggCycles" -> 21,
        "maleGenderPercent" -> 89,
        "species" -> "someSpecies",
        "primaryType" -> "grass",
        "secondaryType" -> "poison",
        "attack" -> 44,
        "specialAttack" -> 45,
        "defense" -> 30,
        "specialDefence" -> 45,
        "speed" -> 45,
        "hp" -> 100,
        "nationalId" -> "001")
    )
    objectToReturn
  }

  def getTestMove(): MongoDBObject = {

    val objectToReturn = MongoDBObject(
      "moveId" -> "001",
      "metadata" -> MongoDBObject(
        "name" -> "Hyper_Beam",
        "type" -> "fighting",
        "category" -> "special",
        "power" -> 100,
        "accuracy" -> 75,
        "pp" -> 10,
        "maxpp" -> 20,
        "introducedInGeneration" -> 1,
        "description" -> "I PWN YOU.")
    )
    objectToReturn
  }

  def getTestMove2(): MongoDBObject = {

    val objectToReturn = MongoDBObject(
      "moveId" -> "002",
      "metadata" -> MongoDBObject(
        "name" -> "Fire_Blast",
        "type" -> "fire",
        "category" -> "special",
        "power" -> 200,
        "accuracy" -> 55,
        "pp" -> 5,
        "maxpp" -> 10,
        "introducedInGeneration" -> 1,
        "description" -> "YOGA FIRE!")
    )
    objectToReturn
  }

}
