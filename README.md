# Design and implementation of data structures and algorithms on directed and weighted graphs:
In this assignment we We were asked to design and implementation of data structures and algorithms on directed and weighted graphs and to represent the graph in efficient way.

In order to do so, we run on the graph a few algorithms like:
**isConnected, tsp, shortest path**(returns list of the route),
**shortest path distance**(returns the length in double)
and **centner** who based on algorithms like Dijkstra and DFS.

**_At this assignment we decided to use HashMap as data structures._**
This data structures returns everything in permanent running time of **O(1)**. Therefore is the best way to hold the graph's in the most efficient way for the functions.
We represent the graph via HashMap of nodes and the edges {private HashMap<Integer, HashMap<Integer, EdgeData>> graph; }
and inside this HashMap we have another HashMap {private HashMap<Integer, NodeData> nodes; } who holds the nodes.

### Packages and classes:

**Package API-** contain interfaces for the classes.

**Package classes**- contain all the implementaions and the algorithmics of the graph by the interface.
Class DirectedWeightedGraph- represent the graph.

Class DirectedWeightedGraphAlgorithms- represent the algorithms we can use on the graph.

Class EdgeData- represent the edges on the graph.

Class GeoLocation - represent the coordinates of the nodes.

Class NodeData- represent the nodes.

**Package gui** - this package will present the results in a graphic - visual way.
Inside we have the next classes.

GraphDraw - draw the graph.

Menu - the right side frame, where you have the buttons of connected and TSP algorithm.

MyFrame -doing the whole visual frame.

MyMenuBar - doing the top frame of the menu bar, where the "edit" and "file" buttons.

**Package test** - inside there is 2 classes where we check our code.

**Package Util** - inside we have 4 classes who convert the graph from coordinates presentation to a graph presentation and same opposite.

## Important functions:


**public boolean isConnected()** -> Graph is called connected if and only if there is path between u and v which u and v are vertex in the graph. Did it with the help of dfs algorithm which go over all the graph and see it there is a path between a source code to all other nodes in the graph, Also transposed the graph to save the amount of time by using the dfs algorithm to each node.

**public void connect(int src, int dest, double w)** ->  Connect between source and dest and edding as EdgeData object to the graph. Override if the src and dest is already exist at graph. Throw exceptions when one of the varibels is forbiden.

**public Iterator<NodeData> nodeIter()** ->  Iterating over all varibles of the data structure that contains all the nodes of the graph, Throw exception when trying to change the graph while iterating.

**public Iterator<EdgeData> edgeIter()** -> Iterate over all the edges of the graph using nodeIter() function and edgeIter(int node_id) to save using to another data structure. will throw exception because the use of the related iterators.

**public NodeData removeNode(int key)** -> Remove of node by given key remove all the edges that out of that node, remove all the nodes thats get in to this node by using EdgeIn data structure. Returning null if the key is not exist.

**public EdgeData removeEdge(int src, int dest)** -> Remove edge by given source and destination. Return null if the edge is not exist.

**private DirectedWeightedGraph graphTranspose()** - > Helper to is connected graph, return new transposed graph, by opposited all the edges direction.

**private void DFS(DirectedWeightedGraph gr, int entryKey, HashMap<Integer, Boolean> visited)** -> Helper to isConnected function. getting a soruce node id and and iterate over all his neighbors nood add it to stack and after that it iterating over the neighbors of the neighbor and reach to the all nodes that related to the source Tying element.

**public void DIJKSTRA(int src)** -> Known algorithm which given source node and it returning the shortest path from the particular source node to all other nodes in the graph Time Complexity O(V + E*log*V).

**public void clean() ** -> Clean method like cleaning buffer of the graph before we do a new algorithm.

**public double shortestPathDist(int src, int dest)** -> Return the distance between the source node to the dest node, by computing the weights thats exists on each edge thats have been contained in the path.

**public NodeData center()** -> Center of a graph is the set of all vertices of minimum eccentricity, hat is, the set of all vertices u where the greatest distance d(u,v) to other vertices v is minimal. used the wikipedia - https://en.wikipedia.org/wiki/Graph_center

**public boolean save(String file)** -> Save the DirectedWeightedGraph as json.

**public boolean load(String file)** -> Load a json file to DirectedWeightedGraph object.

**public List<NodeData> tsp(List<NodeData> cities)** -> * This particular tsp is kind of greedy algorithm, which chose the better node to start from. In the algorithm I issolate the relative nodes and edges that related to cities. I going over leftest city (x Location) and then looking for the promsing closest nod by using shortestPathDist function. The algoritm works for both sides to see if it better start from rightest side.

**private DirectedWeightedGraph buildGraphOnlyForCities(List<NodeData> cities)** -> Building sub graph that contains all the edges and all the nodes that relative to specific cities list.

## GUI:
gui is a class who Implements a graphical-visual interface where we can visually see the program results.
![you can see visually your graph](https://github.com/JosefMamo12/Ex2_OOP/blob/master/image/graph.jpeg)
![you can edit your graph](https://github.com/JosefMamo12/Ex2_OOP/blob/master/image/edit.jpeg)
![you can laod and save files](https://github.com/JosefMamo12/Ex2_OOP/blob/master/image/file%20%20-%20status%20bar.jpeg)
![you can see the menu - where ypu can choose to operate algorithms on the graph](https://github.com/JosefMamo12/Ex2_OOP/blob/master/image/menu.jpeg)
  
## Algoritham analysis for multiple big graphs:
Center() 1k nodes 20k Edges 2 seconds.

Center() 10k nodes 200k edges 5min 20 sec.

Center() 100k nodes 2m edges java heap exception.

isConnected() 10k nodes 200k edges 357ms.

isConnected() 100k nodes 2m edges 3 sec 504 ms.

isConnected() 1m nodes 20m edges 26 sec 486ms.

tsp () 1k nodes 20k edges (3 cities) 293 ms.

tsp () 10k nodes 200k edges (3 cities) 1 sec 672ms.

tsp () 100k nodes 2m edges (3 cities) 35 sec 242 ms.

shortestPath() 10k nodes 200k edges 51ms.

shortestPath() 100k nodes 2m edges 2sec 798ms.

shortestPath() 1m nodes 20m edges 51s sec 643 ms.
