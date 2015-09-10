/**
 * Created by jtumidanski on 9/9/15.
 */

object DollDelivery {
//  val startingLocation = "Kruthika's abode"
//  val targetLocation = "Craig's haunt"
//  val edges = List(
//    Map("startLocation" -> "Kruthika's abode", "endLocation" -> "Mark's crib", "distance" -> 9),
//    Map("startLocation" -> "Kruthika's abode", "endLocation" -> "Greg's casa", "distance" -> 4),
//    Map("startLocation" -> "Kruthika's abode", "endLocation" -> "Matt's pad", "distance" -> 18),
//    Map("startLocation" -> "Kruthika's abode", "endLocation" -> "Brian's apartment", "distance" -> 8),
//    Map("startLocation" -> "Brian's apartment", "endLocation" -> "Wesley's condo", "distance" -> 7),
//    Map("startLocation" -> "Brian's apartment", "endLocation" -> "Cam's dwelling", "distance" -> 17),
//    Map("startLocation" -> "Greg's casa", "endLocation" -> "Cam's dwelling", "distance" -> 13),
//    Map("startLocation" -> "Greg's casa", "endLocation" -> "Mike's digs", "distance" -> 19),
//    Map("startLocation" -> "Greg's casa", "endLocation" -> "Matt's pad", "distance" -> 14),
//    Map("startLocation" -> "Wesley's condo", "endLocation" -> "Kirk's farm", "distance" -> 10),
//    Map("startLocation" -> "Wesley's condo", "endLocation" -> "Nathan's flat", "distance" -> 11),
//    Map("startLocation" -> "Wesley's condo", "endLocation" -> "Bryce's den", "distance" -> 6),
//    Map("startLocation" -> "Matt's pad", "endLocation" -> "Mark's crib", "distance" -> 19),
//    Map("startLocation" -> "Matt's pad", "endLocation" -> "Nathan's flat", "distance" -> 15),
//    Map("startLocation" -> "Matt's pad", "endLocation" -> "Craig's haunt", "distance" -> 14),
//    Map("startLocation" -> "Mark's crib", "endLocation" -> "Kirk's farm", "distance" -> 9),
//    Map("startLocation" -> "Mark's crib", "endLocation" -> "Nathan's flat", "distance" -> 12),
//    Map("startLocation" -> "Bryce's den", "endLocation" -> "Craig's haunt", "distance" -> 10),
//    Map("startLocation" -> "Bryce's den", "endLocation" -> "Mike's digs", "distance" -> 9),
//    Map("startLocation" -> "Mike's digs", "endLocation" -> "Cam's dwelling", "distance" -> 20),
//    Map("startLocation" -> "Mike's digs", "endLocation" -> "Nathan's flat", "distance" -> 12),
//    Map("startLocation" -> "Cam's dwelling", "endLocation" -> "Craig's haunt", "distance" -> 18),
//    Map("startLocation" -> "Nathan's flat", "endLocation" -> "Kirk's farm", "distance" -> 3)
//  )

  val startingLocation = "A"
  val targetLocation = "F"
  val edgeList = List(
    Map("startLocation" -> "A", "endLocation" -> "B", "distance" -> 4),
    Map("startLocation" -> "A", "endLocation" -> "C", "distance" -> 3),
    Map("startLocation" -> "A", "endLocation" -> "E", "distance" -> 7),
    Map("startLocation" -> "B", "endLocation" -> "C", "distance" -> 6),
    Map("startLocation" -> "B", "endLocation" -> "D", "distance" -> 5),
    Map("startLocation" -> "C", "endLocation" -> "D", "distance" -> 11),
    Map("startLocation" -> "C", "endLocation" -> "E", "distance" -> 8),
    Map("startLocation" -> "D", "endLocation" -> "F", "distance" -> 2),
    Map("startLocation" -> "D", "endLocation" -> "G", "distance" -> 10),
    Map("startLocation" -> "D", "endLocation" -> "E", "distance" -> 2),
    Map("startLocation" -> "E", "endLocation" -> "G", "distance" -> 5),
    Map("startLocation" -> "F", "endLocation" -> "G", "distance" -> 3)
  )

  //def getVertex(name: String, vertices: List[Vertex]): Vertex = {

 // }

  def main(args: Array[String]): Unit = {
    // Construct a list of vertex names.
    val vertexNames = (edgeList.map(edge => edge("startLocation").asInstanceOf[String]) ++ edgeList.map(edge => edge
      ("endLocation").asInstanceOf[String])).distinct
    // Construct a list of vertices.
    val vertices = vertexNames.map(vertexName => (vertexName, new Vertex(vertexName, if (vertexName == startingLocation)
      0 else Int.MaxValue))).toMap

    // Construct a list of edges.
    val edges = edgeList.map(edge => new Edge(edge("startLocation").asInstanceOf[String], edge("endLocation").asInstanceOf[String], edge
      ("distance").asInstanceOf[Int]))

    // Initial step, no vertices are settled.
    val settled = Map[String, Vertex]()
    // Initial step, the starting location is the only unsettled vertex.
    val unSettled = Map(startingLocation -> vertices(startingLocation))

    val shortestPathTree = generateTree(vertices, edges, settled, unSettled)
    println(printPath(targetLocation, shortestPathTree))
  }

  // Prints the path to the specified vertex.
  def printPath(vertexName: String, shortestPathTree: Map[String, Vertex]) = {
    val destination = shortestPathTree(vertexName)
    val pathString = constructPathList(destination)
    Map("distance" -> destination.distance, "path" -> pathString)
  }

  // Traverses the predecessor tree for the vertex. Creates a string
  def constructPathList(vertex: Vertex): String = {
    if (vertex.predecessor == null) {
      // If no predecessor exists, we are at the root. Just print itself.
      vertex.toString
    }
    else {
      // If a predecessor exists, evaluate for the predecessor and append itself.
      constructPathList(vertex.predecessor)+ " => " + vertex.toString
    }
  }

  // Generates a shortest path tree using Dijkstra's Algorithm.
  def generateTree(vertices: Map[String, Vertex], edges: List[Edge], settled: Map[String, Vertex], unSettled: Map[String, Vertex]):
  Map[String, Vertex] = {
    // Only bother doing anything if unsettled vertices exist.
    if (unSettled.nonEmpty) {
      // Pick the vertex with the minimum distance that is unsettled.
      val minVertex = getMinimum(unSettled)

      // Mark the vertex as settled and remove the vertex from the unsettled list.
      val newSettled = settled ++ Map(minVertex.name -> minVertex)
      val unSettledMinusMin = unSettled - minVertex.name

      // Get the edges for the vertex. Remove any edges that are already settled as that is wasted computation.
      val vertexEdges = getNeighbors(minVertex, edges).filter(edge => if (!newSettled.contains(edge.destination)) true else false)

      // Using the edges coming from this vertex, see if any new unsettled vertices exist.
      val newUnSettled = unSettledMinusMin ++ findMinimalDistances(vertexEdges, vertices)

      // Update the vertices map with the new unsettled values.
      val newVertices = (vertices -- newUnSettled.keys) ++ newUnSettled

      // Add the settled vertex to the result list.
      Map(minVertex.name -> minVertex) ++ generateTree(newVertices, edges, newSettled, newUnSettled)
    }
    else {
      // If there are no more vertices to settle, return an empty list.
      Map[String, Vertex]()
    }
  }

  // Gets the vertex with the minimum distance from the starting location.
  def getMinimum(vertices: Map[String, Vertex]) = {
    val max = new Vertex("MAX", Int.MaxValue)
    vertices.foldLeft(max) { (minVertex, mapElement) => if (minVertex.distance < mapElement._2.distance) minVertex else
      mapElement._2}
  }

  // Given a vertex and a set of edges, return the list of edges for a vertex.
  def getNeighbors(vertex: Vertex, edges: List[Edge]) = {
    edges.filter(edge => if (edge.source == vertex.name) true else false)
  }

  // Given a starting vertex and a list of edges coming from the vertex, determine if the edge should be taken. An edge
  // should be taken if the distance from the source vertex and the edge distance are less than the current known
  // distance to the destination vertex.
  def findMinimalDistances(vertexEdges: List[Edge], vertices: Map[String, Vertex]) = {
    vertexEdges.map(edge =>
      if (vertices(edge.destination).distance > vertices(edge.source).distance + edge.distance)
        (edge.destination, new Vertex(edge.destination, vertices(edge.source).distance + edge.distance, vertices(edge
          .source)))
      else
        (edge.destination, vertices(edge.destination))
    ).toMap
  }
}

// The Vertex class. Stores a vertex name and distance from starting location.
class Vertex(val name: String, val distance: Int, val predecessor: Vertex = null) {
  // Override toString for a useful println.
  override def toString = { s"$name" }
}

// The Edge class. Maps a source to destination with an associated distance.
class Edge(val source: String, val destination: String, val distance: Int) {
  // Override toString for a useful println.
  override def toString = { s"$source->$destination D=$distance" }
}