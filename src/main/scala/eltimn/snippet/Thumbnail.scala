package eltimn
package snippet

import model.Book

class Thumbnail {
  def render =
    <ul>
    {
      Book.findAll.flatMap { b =>
        <li><span>{"Book: %s".format(b.name.is)}</span><img src={"/api/book/thumb/%s".format(b.id.toString)}></img></li>
      }
    }
    </ul>
}
