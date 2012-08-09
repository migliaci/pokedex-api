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

  val pokemonCollection = MongoConnection()("pokedex")("pokemon")
  val moveCollection = MongoConnection()("pokedex")("moves")
  val evolutionCollection = MongoConnection()("pokedex")("evolutions")


  def getTestPokemon1(): MongoDBObject = {

    val objectToReturn = MongoDBObject(
      "pokemonId" -> "001BulbasaurV",
      "evolutionChain" -> 1,
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
        "species" -> "Bulbasaur",
        "primaryType" -> "grass",
        "secondaryType" -> "poison",
        "attack" -> 44,
        "specialAttack" -> 45,
        "defense" -> 30,
        "specialDefence" -> 45,
        "speed" -> 45,
        "hp" -> 100,
        "generation" -> 5,
        "nationalId" -> 1)
    )
    objectToReturn
  }

  def getTestPokemon2(): MongoDBObject = {

    val objectToReturn = MongoDBObject(
      "pokemonId" -> "025PikachuV",
      "evolutionChain" -> 10,
      "metadata" -> MongoDBObject(
        "name" -> "Pikachu",
        "jpName" -> "ピカチュ",
        "imageURL" -> "someUrl",
        "generation" -> 1,
        "height" -> 5,
        "weight" -> 5,
        "form" -> "normal",
        "eggCycles" -> 21,
        "maleGenderPercent" -> 50,
        "species" -> "Pikachu",
        "primaryType" -> "electric",
        "secondaryType" -> "",
        "attack" -> 34,
        "specialAttack" -> 25,
        "defense" -> 10,
        "specialDefence" -> 45,
        "speed" -> 60,
        "hp" -> 40,
        "generation" -> 5,
        "nationalId" -> 25)
    )
    objectToReturn
  }

  def getTestPokemon3(): MongoDBObject = {

    val objectToReturn = MongoDBObject(
      "pokemonId" -> "172PichuV",
      "evolutionChain" -> 10,
      "metadata" -> MongoDBObject(
        "name" -> "Pichu",
        "jpName" -> "ピチュ",
        "imageURL" -> "someUrl",
        "generation" -> 1,
        "height" -> 1,
        "weight" -> 2,
        "form" -> "normal",
        "eggCycles" -> 21,
        "maleGenderPercent" -> 60,
        "species" -> "Pichu",
        "primaryType" -> "electric",
        "secondaryType" -> "",
        "attack" -> 1,
        "specialAttack" -> 1,
        "defense" -> 30,
        "specialDefence" -> 1,
        "speed" -> 30,
        "hp" -> 10,
        "generation" -> 5,
        "nationalId" -> 172)
    )
    objectToReturn
  }

  def getTestPokemon4(): MongoDBObject = {

    val objectToReturn = MongoDBObject(
      "pokemonId" -> "026RaichuV",
      "evolutionChain" -> 10,
      "metadata" -> MongoDBObject(
        "name" -> "Raichu",
        "jpName" -> "ライチュ",
        "imageURL" -> "someUrl",
        "generation" -> 1,
        "height" -> 10,
        "weight" -> 20,
        "form" -> "normal",
        "eggCycles" -> 21,
        "maleGenderPercent" -> 89,
        "species" -> "Raichu",
        "primaryType" -> "electric",
        "secondaryType" -> "",
        "attack" -> 44,
        "specialAttack" -> 45,
        "defense" -> 30,
        "specialDefence" -> 45,
        "speed" -> 45,
        "hp" -> 100,
        "generation" -> 5,
        "nationalId" -> 26),
       "moves"-> MongoDBObject(
          "levelMoves" -> MongoDBObject(
            "1" -> "001"
          ),
          "hmMoves" -> MongoDBObject(
            "2" -> "002"
          ),
          "tmMoves" -> MongoDBObject(
            "1" -> "001"
          ),
          "tutorMoves" -> MongoDBObject (
            "3" -> "003"
          )

        )
    )
    objectToReturn
  }

  def getTestMove1(): MongoDBObject = {

    val objectToReturn = MongoDBObject(
      "moveId" -> "1",
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
      "moveId" -> "2",
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

  def getTestEvolution1() : MongoDBObject = {

    val objectToReturn = MongoDBObject(
    "evolutionChain" -> 10,
    "from" -> 172,
    "to" -> 25,
    "how" -> "Level up Max Happiness"

    )

    objectToReturn
  }

  def getTestEvolution2() : MongoDBObject = {

    val objectToReturn = MongoDBObject(
      "evolutionChain" -> 10,
      "from" -> 25,
      "to" -> 26,
      "how" -> "Use Thunderstone"
    )

    objectToReturn

  }

  def getTestEvolution3() : MongoDBObject = {

    val objectToReturn = MongoDBObject(
      "evolutionChain" -> 1,
      "from" -> 1,
      "to" -> 2,
      "how" -> "Level up"
    )

    objectToReturn

  }

  def setupTestDatabase() {


    //save to the DB
    pokemonCollection += this.getTestPokemon1
    pokemonCollection += this.getTestPokemon2
    pokemonCollection += this.getTestPokemon3
    pokemonCollection += this.getTestPokemon4
    moveCollection += this.getTestMove1
    moveCollection += this.getTestMove2
    evolutionCollection += this.getTestEvolution1
    evolutionCollection += this.getTestEvolution2
    evolutionCollection += this.getTestEvolution3
    pokemonCollection.find
    moveCollection.find
    evolutionCollection.find

  }

  def deleteTestDatabase() {
    moveCollection.drop
    pokemonCollection.drop
    evolutionCollection.drop
  }

}
