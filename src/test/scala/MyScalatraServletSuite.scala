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
    get("/types/efficacy/type1/grass/type2/poison") {
      status should equal(200)
      response.body should include ("grass")
    }
  }

  test("Comparator object can be queried") {
    get("/comparator/pokemon1/1/pokemon2/25") {
      status should equal(200)
      response.body should include ("Bulbasaur")
      response.body should include ("Pikachu")
    }
  }


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


}
