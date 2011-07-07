package eltimn
package api

import model._

import org.bson.types.ObjectId

import net.liftweb._
import common._
import http._
import http.rest._

object BookApiStateless extends RestHelper with Loggable {
  serve("api" / "book" prefix {
    case "thumb" :: AsObjectId(bookId) :: _ Get _ =>
      val data: Array[Byte] = Book.find(bookId).map(_.thumbnail.is).openOr(Array.empty)
      InMemoryResponse(data, Nil, Nil, 200)
  })
}

object AsObjectId {
  def unapply(in: String): Option[ObjectId] = asObjectId(in)
  
  private def asObjectId(in: String): Option[ObjectId] =
    if (ObjectId.isValid(in)) Some(new ObjectId(in))
    else None
}
