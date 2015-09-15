# Doll Delivery Dijkstra's Algorithm

You are a nice old lady who delivers porcelain dolls on foot using your walker to residents in a neighborhood. Every day a nice young man fills your handbag with porcelain dolls. He gives you a map of the neighborhood and an address of the neighbor to deliver the handbag of dolls to. The handbag is extremely heavy and you have a bad hip so you always try to minimize the walking distance from your beginning position to your destination. Your job is to determine the shortest path to take to deliever your handbag of dolls.

## Objectives
**Write a function in the [Scala](http://www.scala-lang.org/) programming language** when given:

* a starting location
* a target location
* a list of edges where each edge is represented as a map and connects two locations
  
Produces the shortest path with distance which:

* starts at the given starting location
* ends at the given target location

Include a set of executable high-level tests for your solution.

## Setup
Ensure environment variables are configured properly. SCALA_HOME references the local scala directory. SCALA_TEST_JAR
 references the scalatest.jar located in the git repository. PATH must reference the bin directory of SCALA_HOME. 
 Here is an example .profile

    SCALA_HOME="/usr/local/scala"
    SCALA_TEST_JAR="/Users/jtumidanski/Documents/Projects/Scala/doll-delivery/lib/scalatest_2.11-2.2.4.jar"
    PATH="/Library/Frameworks/Python.framework/Versions/2.7/bin:$SCALA_HOME/bin:${PATH}"
    export PATH

## Usage

### Compiling
Navigate to the doll-delivery root. Execute the following commands:

    $ scalac -d classes -classpath classes src/Vertex.scala
    $ scalac -d classes -classpath classes src/Edge.scala
    $ scalac -d classes -classpath classes src/Graph.scala
    $ scalac -d classes -classpath classes src/DollDelivery.scala
    $ scalac -d classes -classpath $SCALA_TEST_JAR:classes test/DollDeliveryTest.scala
    
### Executing
Navigate to the classes folder. Execute the following command:

    $ scala -classpath classes DollDelivery [start] [end] [edge file]
    Distance: [distance]
    Path: [path from start to end]

#### Example Executions

    $ scala -classpath classes DollDelivery "Kruthika's abode" "Craig's haunt" "rsc/edge-set-0"
    Distance: 31
    Path: Kruthika's abode => Brian's apartment => Wesley's condo => Bryce's den => Craig's haunt
    
    $ scala -classpath classes DollDelivery "A" "F" "rsc/edge-set-1"
    Distance: 11
    Path: A => B => D => F
    
    $ scala -classpath classes DollDelivery "U" "Z" "rsc/edge-set-2"
    Distance: 6
    Path: U => V => Y => Z

### Testing
Navigate to the classes folder. Execute the following commands:

    $ scala -classpath $SCALA_TEST_JAR:classes org.scalatest.run DollDeliveryTest
    Run starting. Expected test count is: 22
    DollDeliveryTest:
    + Starting Test Suite 
    + Testing GetMinimum Function 
    getMinimum
    - should return the only element in the map
    getMinimum
    - should throw IllegalArgumentException if an empty map is provided.
    getMinimum
    - should return the lowest distance vertex in the map at the end of the map
    getMinimum
    - should return the lowest distance vertex in the map at the beginning of the map
    getMinimum
    - should return the lowest distance vertex in the map in the middle of the map
    getMinimum
    - should return the last minimum vertex if more than one exist with the same distance
    + Testing GetNeighbors Function 
    getNeighbors
    - should return no edges if the vertex does not have any
    getNeighbors
    - should should return 3 edges for vertex A
    getNeighbors
    - should should return 2 edges for vertex C
    getNeighbors
    - should should return 1 edges for vertex F
    + Testing ConstructPathList Function 
    constructPathList
    - should should return a string 'A' if it is has no predecessors
    constructPathList
    - should should return a string 'A => B' if A is a predecessor to B
    constructPathList
    - should should return a string 'A => B => C' if A is a predecessor to B and B is a predecessor to C
    constructPathList
    - should should return a string 'A' if A is a predecessor to itself.
    + Testing GenerateTree Function 
    generateTree
    - should find a path from A to all other nodes
    generateTree
    - should find a path from B to all other nodes (not A)
    generateTree
    - should find a path from C to all other nodes (not A, B)
    + Testing Main Function 
    main
    - should throw an illegal argument exception if not enough arguments are provided.
    main
    - should throw a file not found exception if the edge file does not exist.
    main
    - should produce output with a distance of 31
    main
    - should produce output with a distance of 11
    main
    - should produce output with a distance of 6
    Run completed in 177 milliseconds.
    Total number of tests run: 22
    Suites: completed 1, aborted 0
    Tests: succeeded 22, failed 0, canceled 0, ignored 0, pending 0
    All tests passed.

Hints:

* read about [Dijkstra's Algorithm](http://en.wikipedia.org/wiki/Dijkstra%27s_algorithm)
* find more interesting example data for test cases on the internet
