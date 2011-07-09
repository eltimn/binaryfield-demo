package eltimn
package snippet

import scala.collection.mutable.{HashMap, SynchronizedMap}
import scala.xml._

import net.liftweb._
import common._
import http._
import util.Props
import util.Helpers._

/*
 * Lift snippet for serving versioned javascript files processed by
 * sbt-closure.
 *
 * Example:
 * <script class="lift:JavaScript?src=/js/core.js"></script>
 */
object JavaScript extends Loggable {
  private val srcMap = new HashMap[String, String] with SynchronizedMap[String, String]
  /*
   * A prefix can be set in the props file (closure.prefix)
   */
  private val propPrefix = Props.get("closure.prefix", "")

  def render(in: NodeSeq): NodeSeq = <script src={jsSrcPath(S.attr("src") openOr "/script.js")}></script>

  private def jsSrcPath(srcUri: String): String =
    if (srcMap.contains(srcUri))
      srcMap(srcUri)
    else {
      val srcPath = calcJsSrcPath(srcUri)
      srcMap += (srcUri -> srcPath)
      srcPath
    }

  private def calcJsSrcPath(srcUri: String): String = {
    val name0 = srcUri.replace(".js", "")
    /**
     * Get the js module key to look for in the props file. The
     * key in the props file will be prefix.path.module_name
     *
     * Eg. {propPrefix}.js.main=3 corresponds to /js/main_3.js which
     * has a manifest file of /js/main.jsm[anifest]
     */
    val modKey = (propPrefix :: name0.split('/').toList)
      .filter(_.length > 0)
      .mkString(".")

    val ver = Props.get(modKey, "")

    if (ver.length > 0) "%s_%s.js".format(name0, ver)
    else "%s.js".format(name0)
  }
}
