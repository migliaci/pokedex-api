package com.ign.pokedex

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatra.test.scalatest.ScalatraFunSuite

/**
 * Created with IntelliJ IDEA.
 * User: mmigliacio
 * Date: 8/11/12
 * Time: 10:30 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(classOf[JUnitRunner]) // makes test run with Maven Surefire
class MyScalatraServletSuite extends ScalatraFunSuite with ShouldMatchers {
  addServlet(classOf[MyScalatraServlet], "/*")

  test("GET / returns status 200") {
    get("/") {
      status should equal (200)
    }
  }
/*
  test("Pokemon object can be queried") {
    get("/pokemon/Bulbasaur") {
      status should equal(200)
      response.body should include ("Bulbasaur")
    }
  }

  test("Moves object can be queried") {
    get("/moves/hyper-beam") {
      status should equal(200)
      response.body should include ("hyper-beam")
    }
  }

  test("Evolutions object can be queried") {
    get("/evolutions/pokemon/25") {
      status should equal(200)
      response.body should include ("pikachu")
    }
  }

   test("Types object can be queried") {
    get("/types/efficacy?type1=grass") {
      status should equal(200)
      response.body should include ("grass")
    }
  }
  */
  /*
  test("Comparator object can be queried") {
    get("/comparator/pokemon1/1/pokemon2/25") {
      status should equal(200)
      response.body should include ("Bulbasaur")
      response.body should include ("Pikachu")
    }
  }
  */

  /* V3 TESTS */
  /*
  test("Pokemon object can be queried") {
    get("/pokemon?name=Bulbasaur") {
      status should equal(200)
      response.body should include ("Bulbasaur")
    }
  }

  test("Moves object can be queried") {
    get("/moves?moveName=Hyper-beam") {
      status should equal(200)
      response.body should include ("hyper-beam")
    }
  }

  test("Evolutions object can be queried") {
    get("/evolutions/pokemon?nationalId=25") {
      status should equal(200)
      response.body should include ("pikachu")
    }
  }

  test("Types object can be queried") {
    get("/types/efficacy?type1=grass") {
      status should equal(200)
      response.body should include ("grass")
    }
  }


  test("Comparator object can be queried") {
    get("/comparator?firstPokemonId=1&secondPokemonId=25") {
      status should equal(200)
      response.body should include ("Bulbasaur")
      response.body should include ("Pikachu")
    }
  }


  //error cases
  test("Bad count should return 404") {
    get("/pokemon?count=shit,man") {
      status should equal(404)
    }
  }

  test("Null count should return 404") {
    get("/pokemon?count=") {
      status should equal(404)
    }
  }


  test("Bad startIndex with count should return 404") {
    get("/pokemon?startIndex=fuckVIM&count=5") {
      status should equal(404)
    }
  }

  test("Null startIndex with count should return 404") {
    get("/pokemon?startIndex=&count=5") {
      status should equal(404)
    }
  }

  test("Bad count with startIndex should return 404") {
    get("/pokemon?startIndex=5&count=fuckVIM") {
      status should equal(404)
    }
  }

  test("Bad count with startIndex (reversed order) should return 404") {
    get("/pokemon?count=fuckVIM&startIndex=5") {
      status should equal(404)
    }
  }


  test("Bad generation with count should return 404") {
    get("/pokemon?count=5&generation=bullshit") {
      status should equal(404)
    }
  }

  test("Null generation with count should return 404") {
    get("/pokemon?count=5&generation=") {
      status should equal(404)
    }
  }

  test("Bad nationalId with generation should return 404") {
    get("/pokemon?nationalId=shit,man&generation=5") {
      status should equal(404)
    }
  }

  test("Null nationalId with generation should return 404") {
    get("/pokemon?nationalId=&generation=5") {
      status should equal(404)
    }
  }

  */

  /* VFINAL */

  test("Default pokemon list can be queried") {
    get("/pokemon") {
      status should equal(200)
      response.body should include ("Bulbasaur")
      response.body should include ("Ivysaur")
      response.body should include ("Venusaur")
      response.body should include ("Charmander")
      response.body should include ("Charmeleon")
      response.body should include ("Charizard")
      response.body should include ("Squirtle")
      response.body should include ("Wartortle")
      response.body should include ("Blastoise")
      response.body should include ("Caterpie")
      response.body should include ("Metapod")
      response.body should include ("Butterfree")
      response.body should include ("Weedle")
      response.body should include ("Kakuna")
      response.body should include ("Beedrill")
      response.body should include ("Pidgey")
      response.body should include ("Pidgeotto")
      response.body should include ("Pidgeot")
      response.body should include ("Rattata")
      response.body should include ("Raticate")


    }
  }

    test("Individual pokemon can be queried by name") {
      get("/pokemon/slug/bulbasaur") {
        status should equal(200)
        response.body should include ("Bulbasaur")
      }
    }

    test("Individual pokemon can be queried by national id") {
        get("/pokemon/nationalId/1") {
          status should equal(200)
          response.body should include ("Bulbasaur")
      }

    }

    test("Individual pokemon can be queried by national id and generation") {
        get("/pokemon/nationalId/1/generation/4") {
          status should equal(200)
          response.body should include ("Bulbasaur")
          response.body should include ("4")
        }

      }

    test("Individual pokemon can be queried by name and generation") {
      get("/pokemon/slug/bulbasaur/generation/4") {
        status should equal(200)
        response.body should include ("Bulbasaur")
        response.body should include ("4")
      }

    }

    //test type object can be queried by object_id

    test("Types object can be queried") {
      get("/types/efficacy/type1/grass") {
        status should equal(200)
        response.body should include ("grass")
      }
    }

    test("Multiple types object can be queried") {
      get("/types/efficacy/type1/fire/type2/dark") {
        status should equal(200)
        response.body should include ("dark")
        response.body should include ("fire")
      }
    }

    test("Comparator object can be queried") {
      get("/comparator?pokemonIds=1,25") {
        status should equal(200)
        response.body should include ("Bulbasaur")
        response.body should include ("Pikachu")
      }
    }

    //test evolution object can be queried by object_id

    test("Evolution object can be queried (by nationalId)") {
      get("/evolutions/pokemon/nationalId/1") {
        status should equal(200)
        response.body should include ("venusaur")
        response.body should include ("ivysaur")
        response.body should include ("bulbasaur")
      }
    }

    test("Evolution object can be queried (by chainId)") {
      get("/evolutions/chain/chainId/10") {
        status should equal(200)
        response.body should include ("pikachu")
        response.body should include ("raichu")
        response.body should include ("pichu")
      }
    }

     //test moves can be queried

    test("Moves list can be queried") {
      get("/moves") {
        status should equal(200)
        response.body should include ("tackle")
      }
    }

    //test("Moves list can be queried by id") {
    //  get("/moves/id/id") {
    //    status should equal(200)
    //    response.body should include ("Hyper Beam")
    //  }
    // }

    test("Moves list can be queried by name") {
      get("/moves/slug/hyper-beam") {
        status should equal(200)
        response.body should include ("hyper-beam")
      }
    }

    test("Moves list can be queried by category") {
      get("/moves/category/special") {
        status should equal(200)
        response.body should include ("searing-shot")
      }
    }

    test("Moves list can be queried by type") {
      get("/moves/type/fire") {
        status should equal(200)
        response.body should include ("fire-blast")
      }
    }

    test("Pokemon list can be queried by type") {
      get("/moves/moveId/1/pokemon") {
        status should equal(200)
        response.body should include ("Clefairy")
      }
    }

    test("Pokemon list can be queried by moveGroup") {
      get("/moves/moveId/70/pokemon/moveGroup/hm") {
        status should equal(200)
        response.body should include ("Geodude")
      }
    }

    test("Pokemon list can be queried by results") {
      get("/moves/moveId/70/pokemon/moveGroup/hm/generation/5") {
        status should equal(200)
        response.body should include ("Geodude")
      }
    }
}
