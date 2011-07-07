package eltimn
package model

import net.liftweb._
import common._
import mongodb._
import util.Props

import com.mongodb.Mongo

object AdminDB extends MongoIdentifier {
	val jndiName = "admin"
}

object MongoConfig extends Loggable {
  def init() {
    val mainMongoHost = new Mongo(Props.get("mongo.host", "localhost"), Props.getInt("mongo.port", 27017))
		MongoDB.defineDb(
		  DefaultMongoIdentifier,
		  mainMongoHost,
		  Props.get("mongo.main_name", "binaryfield_demo")
		)
		MongoDB.defineDb(
		  AdminDB,
		  mainMongoHost,
		  Props.get("mongo.admin_name", "admin")
		)
		logger.info("MongoDB inited")
  }

  override def finalize() {
    MongoDB.close
  }
}
