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

  test("Default pokemon list can be queried") {
    get("/pokemon") {
      status should equal(200)
    }
  }


    test("Individual pokemon can be queried by name") {
      get("/pokemon/slug/bulbasaur") {
        status should equal(200)
        response.body should include ("Bulbasaur")
      }
    }

    test("Bad slug is handled") {
      get("/pokemon/slug/shit,man") {
        status should equal(404)
      }
    }

    test("Null slug is handled") {
      get("/pokemon/slug") {
        status should equal(404)
      }
    }



    test("Individual pokemon can be queried by national id") {
        get("/pokemon/national-id/1") {
          status should equal(200)
          response.body should include ("Bulbasaur")
      }

    }

    test("Bad national id is handled") {
      get("/pokemon/national-id/shit,man") {
        status should equal(404)
      }
    }

    test("Null national id is handled") {
      get("/pokemon/national-id") {
        status should equal(404)
      }
    }


    test("Individual pokemon can be queried by national id and generation") {
        get("/pokemon/national-id/1/generation/4") {
          status should equal(200)
          response.body should include ("Bulbasaur")
          response.body should include ("4")
        }

      }

    test("Bad nationalId is handled when specifying generation") {
        get("/pokemon/national-id/bullshit/generation/4") {
          status should equal(404)
        }
      }

    test("Bad generation is handled when specifying nationalId") {
        get("/pokemon/national-id/1/generation/bullshit") {
        status should equal(404)
      }
    }

    test("Bad generation is handled when specifying name") {
        get("/pokemon/slug/bulbasaur/generation/bullshit"){
          status should equal(404)
        }
    }

    test("Null generation is handled when specifying name"){
        get("/pokemon/slug/bulbasaur/generation") {
          status should equal(404)
        }

    }

    test("Null nationalId is handled when specifying generation") {
      get("/pokemon/national-id/generation/4") {
        status should equal(404)
      }
    }

    test("Null generation is handled when specifying nationalId") {
      get("/pokemon/national-id/generation/4") {
        status should equal(404)
      }
    }


    test("Individual pokemon can be queried by name and generation") {
      get("/pokemon/slug/bulbasaur/generation/4") {
        status should equal(200)
        response.body should include ("Bulbasaur")
        response.body should include ("4")
      }

    }

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

  test("Bad single type is handled") {
    get("/pokemon/national-id/generation/4") {
      status should equal(404)
    }
  }

  test("Null single type is handled") {
    get("/pokemon/national-id/generation/4") {
      status should equal(404)
    }
  }

  test("Comparator object can be queried") {
      get("/comparator?pokemonIds=1,25") {
        status should equal(200)
        response.body should include ("Bulbasaur")
        response.body should include ("Pikachu")
    }
  }

  test("Bad nationalId is handled in evolution") {
    get("/evolutions/pokemon/national-id/bullshit") {
      status should equal(404)
    }
  }

  test("Null nationalId is handled in evolution") {
    get("/evolutions/pokemon/national-id") {
      status should equal(404)
    }
  }

    test("Evolution object can be queried (by nationalId)") {
      get("/evolutions/pokemon/national-id/1") {
        status should equal(200)
        response.body should include ("venusaur")
        response.body should include ("ivysaur")
        response.body should include ("bulbasaur")
      }
    }

    test("Evolution object can be queried (by chainId)") {
      get("/evolutions/chain/chain-id/10") {
        status should equal(200)
        response.body should include ("pikachu")
        response.body should include ("raichu")
        response.body should include ("pichu")
      }
    }

  test("Bad chainId is handled in evolution") {
    get("/evolutions/chain/chain-id/bullshit") {
      status should equal(404)
    }
  }

  test("Null chainId is handled in evolution") {
    get("/evolutions/chain/chain-id") {
      status should equal(404)
    }
  }

     //test moves can be queried

  test("Moves list can be queried") {
      get("/moves") {
        status should equal(200)

      }
  }

  test("Moves list can be queried by name") {
    get("/moves/slug/hyper-beam") {
      status should equal(200)
      response.body should include ("hyper-beam")
    }
  }

  test("Bad move name is handled in moves") {
    get("/moves/slug/bullshit") {
      status should equal(404)
    }
  }

  test("Null move name is handled in moves") {
    get("/moves/slug") {
      status should equal(404)
    }
  }


    //test("Moves list can be queried by id") {
    //  get("/moves/id/id") {
    //    status should equal(200)
    //    response.body should include ("Hyper Beam")
    //  }
    // }

  test("Bad move category is handled in moves") {
    get("/moves/category/bullshit") {
      status should equal(404)
    }
  }

    test("Moves list can be queried by category") {
      get("/moves/category/special") {
        status should equal(200)
        response.body should include ("searing-shot")
      }
    }

  test("Bad move type is handled in moves") {
    get("/moves/type/bullshit") {
      status should equal(404)
    }
  }

    test("Moves list can be queried by type") {
      get("/moves/type/fire") {
        status should equal(200)
        response.body should include ("fire-blast")
      }
    }



    test("Pokemon list can be queried by type") {
      get("/moves/move-id/1/pokemon") {
        status should equal(200)
        response.body should include ("Clefairy")
      }
    }

    test("Pokemon list can be queried by moveGroup") {
      get("/moves/move-id/70/pokemon/move-group/hm") {
        status should equal(200)
        response.body should include ("Geodude")
      }
    }

    test("Pokemon list can be queried by results") {
      get("/moves/move-id/70/pokemon/move-group/hm/generation/5") {
        status should equal(200)
        response.body should include ("Geodude")
      }
    }


}
