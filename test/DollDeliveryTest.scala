import java.io.FileNotFoundException

import org.scalatest._

/**
 * Created by jtumidanski on 9/10/15.
 */
class DollDeliveryTest extends FlatSpec with Matchers {

  val edgeList1 = List(
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
  val graph1 = new Graph("A", "F", edgeList1)
  val graph2 = new Graph("B", "F", edgeList1)
  val graph3 = new Graph("C", "F", edgeList1)

  info("Starting Test Suite")

  info("Testing GetMinimum Function")
  "getMinimum" should "return the only element in the map" in {
    val onlyVertex = new Vertex("A", Int.MaxValue)
    val minVertex = DollDelivery.getMinimum(Map(onlyVertex.name -> onlyVertex))
    minVertex.name should equal(onlyVertex.name)
  }

  "getMinimum" should "throw IllegalArgumentException if an empty map is provided." in {
    val error = intercept[IllegalArgumentException] {
      DollDelivery.getMinimum(Map[String, Vertex]())
    }
    error.getMessage shouldEqual "No vertices present."
  }

  "getMinimum" should "return the lowest distance vertex in the map at the end of the map" in {
    val vertex1 = new Vertex("A", Int.MaxValue)
    val vertex2 = new Vertex("B", 0)
    val map1 = Map(vertex1.name -> vertex1, vertex2.name -> vertex2)
    val minVertex = DollDelivery.getMinimum(map1)
    minVertex.name should equal(vertex2.name)
  }

  "getMinimum" should "return the lowest distance vertex in the map at the beginning of the map" in {
    val vertex1 = new Vertex("A", 0)
    val vertex2 = new Vertex("B", Int.MaxValue)
    val map1 = Map(vertex1.name -> vertex1, vertex2.name -> vertex2)
    val minVertex = DollDelivery.getMinimum(map1)
    minVertex.name should equal(vertex1.name)
  }

  "getMinimum" should "return the lowest distance vertex in the map in the middle of the map" in {
    val vertex1 = new Vertex("A", Int.MaxValue)
    val vertex2 = new Vertex("B", 0)
    val vertex3 = new Vertex("C", Int.MaxValue)
    val map1 = Map(vertex1.name -> vertex1, vertex2.name -> vertex2, vertex3.name -> vertex3)
    val minVertex = DollDelivery.getMinimum(map1)
    minVertex.name should equal(vertex2.name)
  }

  "getMinimum" should "return the last minimum vertex if more than one exist with the same distance" in {
    val vertex1 = new Vertex("A", Int.MaxValue)
    val vertex2 = new Vertex("B", Int.MaxValue)
    val vertex3 = new Vertex("C", Int.MaxValue)
    val map1 = Map(vertex1.name -> vertex1, vertex2.name -> vertex2, vertex3.name -> vertex3)
    val minVertex = DollDelivery.getMinimum(map1)
    minVertex.name should equal(vertex3.name)
  }

  info("Testing GetNeighbors Function")
  "getNeighbors" should "return no edges if the vertex does not have any" in {
    val vertex = new Vertex("Z", Int.MaxValue)
    val edges = DollDelivery.getNeighbors(vertex, graph1.edges)
    edges should equal(List[Edge]())
  }

  "getNeighbors" should "should return 3 edges for vertex A" in {
    val vertex = new Vertex("A", Int.MaxValue)
    val edges = DollDelivery.getNeighbors(vertex, graph1.edges)
    edges.size should equal(3)
  }

  "getNeighbors" should "should return 2 edges for vertex C" in {
    val vertex = new Vertex("C", Int.MaxValue)
    val edges = DollDelivery.getNeighbors(vertex, graph1.edges)
    edges.size should equal(2)
  }

  "getNeighbors" should "should return 1 edges for vertex F" in {
    val vertex = new Vertex("F", Int.MaxValue)
    val edges = DollDelivery.getNeighbors(vertex, graph1.edges)
    edges.size should equal(1)
  }

  info("Testing ConstructPathList Function")
  "constructPathList" should "should return a string 'A' if it is has no predecessors" in {
    val vertex1 = new Vertex("A", Int.MaxValue)
    val pathList = DollDelivery.constructPathList(vertex1)
    pathList should equal("A")
  }

  "constructPathList" should "should return a string 'A => B' if A is a predecessor to B" in {
    val vertex1 = new Vertex("A", Int.MaxValue)
    val vertex2 = new Vertex("B", Int.MaxValue, vertex1)
    val pathList = DollDelivery.constructPathList(vertex2)
    pathList should equal("A => B")
  }

  "constructPathList" should "should return a string 'A => B => C' if A is a predecessor to B and B is a predecessor " +
    "to C" in {
    val vertex1 = new Vertex("A", Int.MaxValue)
    val vertex2 = new Vertex("B", Int.MaxValue, vertex1)
    val vertex3 = new Vertex("C", Int.MaxValue, vertex2)
    val pathList = DollDelivery.constructPathList(vertex3)
    pathList should equal("A => B => C")
  }

  "constructPathList" should "should return a string 'A' if A is a predecessor to itself. " in {
    val vertex1 = new Vertex("A", Int.MaxValue)
    val pathList = DollDelivery.constructPathList(new Vertex("A", Int.MaxValue, vertex1))
    pathList should equal("A")
  }


  info("Testing GenerateTree Function")
  "generateTree" should "find a path from A to all other nodes" in {
    val vertexA = new Vertex("A", 0)
    val vertexB = new Vertex("B", 4, vertexA)
    val vertexC = new Vertex("C", 3, vertexA)
    val vertexD = new Vertex("D", 9, vertexB)
    val vertexE = new Vertex("E", 7, vertexA)
    val vertexF = new Vertex("F", 11, vertexD)
    val vertexG = new Vertex("G", 12, vertexE)
    
    val expectedOutput = Map(vertexA.name -> vertexA, vertexB.name -> vertexB, vertexC.name -> vertexC, vertexD.name
      -> vertexD, vertexE.name -> vertexE, vertexF.name -> vertexF, vertexG.name -> vertexG)

    val settled = Map[String, Vertex]()
    val unSettled = Map(graph1.start -> graph1.vertices(graph1.start))
    val actualOutput = DollDelivery.generateTree(graph1.vertices, graph1.edges, settled, unSettled)

    actualOutput.size should equal(expectedOutput.size)
    actualOutput("A").distance should equal(expectedOutput("A").distance)
    actualOutput("B").distance should equal(expectedOutput("B").distance)
    actualOutput("C").distance should equal(expectedOutput("C").distance)
    actualOutput("D").distance should equal(expectedOutput("D").distance)
    actualOutput("E").distance should equal(expectedOutput("E").distance)
    actualOutput("F").distance should equal(expectedOutput("F").distance)
    actualOutput("G").distance should equal(expectedOutput("G").distance)
  }

  "generateTree" should "find a path from B to all other nodes (not A)" in {
    val vertexB = new Vertex("B", 0)
    val vertexC = new Vertex("C", 6, vertexB)
    val vertexD = new Vertex("D", 5, vertexB)
    val vertexE = new Vertex("E", 7, vertexD)
    val vertexF = new Vertex("F", 7, vertexD)
    val vertexG = new Vertex("G", 10, vertexF)
    
    val expectedOutput = Map(vertexB.name -> vertexB, vertexC.name -> vertexC, vertexD.name
      -> vertexD, vertexE.name -> vertexE, vertexF.name -> vertexF, vertexG.name -> vertexG)

    val settled = Map[String, Vertex]()
    val unSettled = Map(graph2.start -> graph2.vertices(graph2.start))
    val actualOutput = DollDelivery.generateTree(graph2.vertices, graph2.edges, settled, unSettled)

    actualOutput.size should equal(expectedOutput.size)
    actualOutput("B").distance should equal(expectedOutput("B").distance)
    actualOutput("C").distance should equal(expectedOutput("C").distance)
    actualOutput("D").distance should equal(expectedOutput("D").distance)
    actualOutput("E").distance should equal(expectedOutput("E").distance)
    actualOutput("F").distance should equal(expectedOutput("F").distance)
    actualOutput("G").distance should equal(expectedOutput("G").distance)
  }

  "generateTree" should "find a path from C to all other nodes (not A, B)" in {
    val vertexC = new Vertex("C", 0)
    val vertexD = new Vertex("D", 11, vertexC)
    val vertexE = new Vertex("E", 8, vertexC)
    val vertexF = new Vertex("F", 13, vertexD)
    val vertexG = new Vertex("G", 13, vertexE)

    val expectedOutput = Map(vertexC.name -> vertexC, vertexD.name
      -> vertexD, vertexE.name -> vertexE, vertexF.name -> vertexF, vertexG.name -> vertexG)

    val settled = Map[String, Vertex]()
    val unSettled = Map(graph3.start -> graph3.vertices(graph3.start))
    val actualOutput = DollDelivery.generateTree(graph3.vertices, graph3.edges, settled, unSettled)

    actualOutput.size should equal(expectedOutput.size)
    actualOutput("C").distance should equal(expectedOutput("C").distance)
    actualOutput("D").distance should equal(expectedOutput("D").distance)
    actualOutput("E").distance should equal(expectedOutput("E").distance)
    actualOutput("F").distance should equal(expectedOutput("F").distance)
    actualOutput("G").distance should equal(expectedOutput("G").distance)
  }

  info("Testing Main Function")

  "main" should "throw an illegal argument exception if not enough arguments are provided." in {
    val error = intercept[IllegalArgumentException] {
      DollDelivery.main(Array[String]())
    }
    error.getMessage shouldEqual "Not enough arguments present. Expecting the following 3: Start, End, Edge file."
  }

  "main" should "throw a file not found exception if the edge file does not exist." in {
    val error = intercept[FileNotFoundException] {
      DollDelivery.main(Array[String]("A", "F", "rsc/edge-set-3"))
    }
    error.getMessage shouldEqual "File cannot be found. Please check path rsc/edge-set-3"
  }

  "main" should "produce output with a distance of 31" in {
    val stream = new java.io.ByteArrayOutputStream()
    Console.withOut(stream) {
      DollDelivery.main(Array[String]("Kruthika's abode", "Craig's haunt", "rsc/edge-set-0"))
    }
    stream.toString shouldEqual "Distance: 31\nPath: Kruthika's abode => Brian's apartment => Wesley's condo =>" +
      " Bryce's den => Craig's haunt\n"
  }

  "main" should "produce output with a distance of 11" in {
    val stream = new java.io.ByteArrayOutputStream()
    Console.withOut(stream) {
      DollDelivery.main(Array[String]("A", "F", "rsc/edge-set-1"))
    }
    stream.toString shouldEqual "Distance: 11\nPath: A => B => D => F\n"
  }

  "main" should "produce output with a distance of 6" in {
    val stream = new java.io.ByteArrayOutputStream()
    Console.withOut(stream) {
      DollDelivery.main(Array[String]("U", "Z", "rsc/edge-set-2"))
    }
    stream.toString shouldEqual "Distance: 6\nPath: U => V => Y => Z\n"
  }
}
