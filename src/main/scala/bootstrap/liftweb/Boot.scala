package bootstrap.liftweb

import net.liftweb._
import common._
import http._
import sitemap._
import sitemap.Loc._
import util._
import util.Helpers._

import eltimn.api._
import eltimn.model._

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot extends Loggable {
  def boot {
    logger.info("Run Mode: "+Props.mode.toString)

    // init mongodb
    MongoConfig.init()

    // where to search snippet
    LiftRules.addToPackages("eltimn")

    // set the default htmlProperties
    LiftRules.htmlProperties.default.set((r: Req) => new Html5Properties(r.userAgent))

    // Build SiteMap
    val entries = List(
      Menu.i("Home") / "index", // the simple way to declare a menu

      // more complex because this menu allows anything in the
      // /static path to be visible
      Menu(Loc("Static", Link(List("static"), true, "/static/index"), "Static Content")))
    // the User management menu items

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMap(SiteMap(entries:_*))

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // The function to test if a user is logged in. Used by built-in snippet TestCond.
    //LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    // stateless -- no session created
    LiftRules.statelessDispatchTable.append(BookApiStateless)

    // add some test data
    if (Book.findAll.length == 0) {
      val in = getClass.getResourceAsStream("/ajax-loader.gif")

      if (in != null) {
        val buffer = Stream.continually(in.read()).takeWhile(_ != -1).map(_.toByte).toArray
        val result = Book.createRecord.thumbnail(buffer).name("arthur").save
      }
    }
  }
}
