package selftest

import org.scalatest.{FlatSpec, Matchers}

class Test_Self extends FlatSpec with Matchers {

  "Test-Runner" should "run a test" in {
    val i = 1
    i shouldBe 1
  }

  it should "run another test" in {

    val l = List(1)
    l should have size 1
  }

}
