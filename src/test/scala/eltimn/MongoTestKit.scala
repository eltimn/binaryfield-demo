package eltimn 

import org.specs.Specification

import net.liftweb.mongodb._

import com.mongodb._

trait MongoTestKit {
  this: Specification =>

  def dbName = "binary_demo_"+getClass.getName
    .replace("$", "")
    .replace(".", "_")
    .toLowerCase

  val defaultHost = MongoHost("127.0.0.1", 27017)

  // If you need more than one db, override this
  def dbs: List[(MongoIdentifier, MongoHost, String)] = 
    List((DefaultMongoIdentifier, defaultHost, dbName))

  def debug = false

  doBeforeSpec {
    // define the dbs
    dbs foreach { dbtuple =>
      MongoDB.defineDb(dbtuple._1, MongoAddress(dbtuple._2, dbtuple._3))
    }
  }

  def isMongoRunning: Boolean =
    try {
      if (dbs.length < 1)
        false
      else {
        dbs foreach { dbtuple =>
          MongoDB.use(dbtuple._1) ( db => { db.getLastError } )
        }
        true
      }
    } catch {
      case _ => false
    }

  def checkMongoIsRunning =
    isMongoRunning must beEqualTo(true).orSkipExample

  doAfterSpec {
    if (!debug && isMongoRunning) {
      // drop the databases
      dbs foreach { dbtuple =>
        MongoDB.use(dbtuple._1) { db => db.dropDatabase }
      }
    }

    // clear the mongo instances
    MongoDB.close
  }
}

