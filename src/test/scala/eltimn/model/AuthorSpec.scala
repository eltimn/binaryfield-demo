package eltimn
package model

import java.io.{File, FileInputStream}

import org.specs.Specification

import com.mongodb._

object AuthorSpec extends Specification with MongoTestKit {
  "Author" should {
    "Save thumbnail as binary" in {
      val in = getClass.getResourceAsStream("/ajax-loader.gif")
      
      if (in != null) {
        val buffer = Stream.continually(in.read()).takeWhile(_ != -1).map(_.toByte).toArray 
        val result = Book.createRecord.thumbnail(buffer).name("arthur").save
      }
      else
        println("in is null")
      
      true must_== true
    }
  }
}
