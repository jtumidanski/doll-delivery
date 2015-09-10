/**
 * Created by jtumidanski on 9/10/15.
 */
// The Vertex class. Stores a vertex name and distance from starting location.
class Vertex(val name: String, val distance: Int, val predecessor: Vertex = null) {
  // Override toString for a useful println.
  override def toString = {
    s"Name=$name Distance=$distance Predecessor=${if (predecessor == null) "n/a" else predecessor.name}"
  }
}
