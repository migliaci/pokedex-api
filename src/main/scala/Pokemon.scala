package com.ign.pokedex

import com.mongodb.casbah.commons.MongoDBObject
/**
 * Created with IntelliJ IDEA.
 * User: mmigliacio
 * Date: 8/5/12
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */

class Pokemon {

  var hpValue = 0
  var attackValue = 0
  var defenseValue = 0
  var specialAttackValue = 0
  var specialDefenseValue = 0
  var typeValue = Array

  def populateValues(databaseObject: MongoDBObject) {

    if (databaseObject != null) {

      hpValue = (databaseObject.get(PokedexUtils.HP_NAME)).toString.toInt
      attackValue = (databaseObject.get(PokedexUtils.ATK_NAME)).toString.toInt
      defenseValue = (databaseObject.get(PokedexUtils.DEF_NAME)).toString.toInt
      specialAttackValue = (databaseObject.get(PokedexUtils.SPATK_NAME)).toString.toInt
      specialDefenseValue = (databaseObject.get(PokedexUtils.SPDEF_NAME)).toString.toInt

      //get type array from database object

    }
  }

  //need to do type mail

}
