package eltimn
package model

import net.liftweb._
import mongodb.record._
import mongodb.record.field._
import record.field._

class Book extends MongoRecord[Book] with ObjectIdPk[Book] {
  def meta = Book

  object name extends StringField(this, 100)
  object authors extends BsonRecordListField(this, Author)
  object thumbnail extends BinaryField(this)
}

object Book extends Book with MongoMetaRecord[Book]

class Author extends BsonRecord[Author] {
  def meta = Author

  object name extends StringField(this, 32)
  object email extends StringField(this, 100)
}

object Author extends Author with BsonMetaRecord[Author]
