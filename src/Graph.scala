/**
 * Created by jtumidanski on 9/10/15.
 */
class Graph() {
  var start: String = ""
  var target: String = ""
  var edges: List[Edge] = List[Edge]()
  var vertices: Map[String, Vertex] = Map[String, Vertex]()

  def this (start:String, target:String, edgeList:List[Map[String, Any]]) {
    this()
    this.start = start
    this.target = target
    edges = edgeList.map(edge => new Edge(edge("startLocation").asInstanceOf[String], edge("endLocation")
      .asInstanceOf[String], edge("distance").asInstanceOf[Int]))

    val vertexNames = (edgeList.map(edge => edge("startLocation").asInstanceOf[String]) ++ edgeList.map(edge => edge
      ("endLocation").asInstanceOf[String])).distinct

    vertices = vertexNames.map(vertexName => (vertexName, new Vertex(vertexName, if (vertexName == start) 0 else Int.MaxValue))).toMap
  }
}
