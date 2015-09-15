/**
 * Created by jtumidanski on 9/9/15.
 */

import java.io.FileNotFoundException
import java.io.File
import scala.io._

object DollDelivery {
  def main(args: Array[String]): Unit = {

    // Ensure proper arguments are present.
    if (args.length != 3) {
      throw new IllegalArgumentException("Not enough arguments present. Expecting the following 3: Start, End, Edge " +
        "file.")
    }
    else if (! new File(args(2)).exists) {
      throw new FileNotFoundException(s"File cannot be found. Please check path ${args(2)}")
    }

    // Create empty edge list of maps.
    var edges = List[Map[String, Any]]()

    // Load file.
    val file = Source.fromFile(args(2))

    // For each line in the file, add a edge to the map.
    file.getLines.foreach((line) => {
      val linePieces = line.split(",")
      edges = edges ++ List(Map(
        "startLocation" -> linePieces(0).trim,
        "endLocation" -> linePieces(1).trim,
        "distance" -> linePieces(2).trim.toInt))
    })

    // Construct the graph.
    val graph = new Graph(args(0), args(1), edges)

    // Find the shortest path.
    findShortestPath(graph)
  }

  // Given a graph, find the shortest path from a starting vertex to an ending vertex.
  def findShortestPath(graph: Graph) {
    // Initial step, no vertices are settled.
    val settled = Map[String, Vertex]()
    // Initial step, the starting location is the only unsettled vertex.
    val unSettled = Map(graph.start -> graph.vertices(graph.start))

    val shortestPathTree = generateTree(graph.vertices, graph.edges, settled, unSettled)
    printPath(graph.target, shortestPathTree)
  }

  // Prints the path to the specified vertex.
  def printPath(vertexName: String, shortestPathTree: Map[String, Vertex]) = {
    val destination = shortestPathTree(vertexName)
    val pathString = constructPathList(destination)
    //Map("distance" -> destination.distance, "path" -> pathString)
    println(s"Distance: ${destination.distance}")
    println(s"Path: ${pathString}")
  }

  // Traverses the predecessor tree for the vertex. Creates a string
  def constructPathList(vertex: Vertex): String = {
    if (vertex.predecessor == null) {
      // If no predecessor exists, we are at the root. Just print itself.
      vertex.name
    }
    else if (vertex.name == vertex.predecessor.name) {
      // How can a vertex be a predecessor of itself? Shouldn't really be possible, but handle it just in case.
      vertex.name
    }
    else {
      // If a predecessor exists, evaluate for the predecessor and append itself.
      constructPathList(vertex.predecessor) + " => " + vertex.name
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
  def getMinimum(vertices: Map[String, Vertex]): Vertex = {
    if (vertices.isEmpty) {
      // Throw an illegal argument exception if no vertices are provided.
      throw new IllegalArgumentException("No vertices present.")
    }
    else {
      // At least one vertex exists, find the one with the smallest distance.
      val max = new Vertex("MAX", Int.MaxValue)
      vertices.foldLeft(max) { (minVertex, mapElement) => if (minVertex.distance < mapElement._2.distance) minVertex
      else
        mapElement._2
      }
    }
  }

  // Given a vertex and a set of edges, return the list of edges for a vertex.
  def getNeighbors(vertex: Vertex, edges: List[Edge]): List[Edge] = {
    edges.filter(edge => if (edge.source == vertex.name) true else false)
  }

  // Given a starting vertex and a list of edges coming from the vertex, determine if the edge should be taken. An edge
  // should be taken if the distance from the source vertex and the edge distance are less than the current known
  // distance to the destination vertex.
  def findMinimalDistances(vertexEdges: List[Edge], vertices: Map[String, Vertex]) = {
    vertexEdges.map(edge =>
      if (vertices(edge.destination).distance > vertices(edge.source).distance + edge.distance)
      // The new distance is less than the old distance. Create a new destination vertex with an updated distance.
      // Mark the new predecessor.
        (edge.destination, new Vertex(edge.destination, vertices(edge.source).distance + edge.distance, vertices(edge
          .source)))
      else
      // The old distance is adequate. Make no changes to the destination vertex.
        (edge.destination, vertices(edge.destination))
    ).toMap
  }
}