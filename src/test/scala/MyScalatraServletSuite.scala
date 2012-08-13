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

//  test("Comparator object can be queried") {
//    get("/comparator/pokemon1/1/pokemon2/25") {
//      status should equal(200)
//      response.body should include ("Bulbasaur")
//      response.body should include ("Pikachu")
//    }
//  }

  /*
  test("Pokemon object can be queried for id") {
    get("pokemon/national_id/1") {
      response.body should include ("Bulbasaur")
    }
  }
  */
}
