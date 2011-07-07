package eltimn
package snippet

import org.specs.Specification
import org.specs.specification.Examples

import net.liftweb._
import common._
import http._
import util._
import Helpers._
import lib._

object HelloWorldSpec extends Specification {
  val session = new LiftSession("", randomString(20), Empty)
  val stableTime = now

  override def executeExpectations(ex: Examples, t: => Any): Any = {
    S.initIfUninitted(session) {
      DependencyFactory.time.doWith(stableTime) {
        super.executeExpectations(ex, t)
      }
    }
  }

  "HelloWorld Snippet" should {
    "Put the time in the node" in {
      val hello = new HelloWorld
      Thread.sleep(1000) // make sure the time changes

      val str = hello.render(<span>Welcome to your Lift app at <span id="time">Time goes here</span></span>).toString

      str.indexOf(stableTime.toString) must be >= 0
      str.indexOf("Welcome to your Lift app at") must be >= 0
    }
  }
}

