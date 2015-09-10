/**
 * Created by jtumidanski on 9/10/15.
 */
// The Edge class. Maps a source to destination with an associated distance.
class Edge(val source: String, val destination: String, val distance: Int) {
  // Override toString for a useful println.
  override def toString = {
    s"$source->$destination D=$distance"
  }
}
