package edu.frostburg.cosc310;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * BunnyGraph for searching for bunnies with DFS
 */
public class MyBunnyGraph implements BunnyGraph {

	private final boolean DEBUG = false;

	/**
	 * first vertex added to graph
	 */
	private VertexNode entryVertex;

	/**
	 * Map of the vertices
	 */
	private LinkedHashMap<String, VertexNode> vertexMap;

	/**
	 * Set of vertices with bunnies
	 */
	private LinkedHashSet<String> verticesWithBunnies;

	/**
	 * MyBunnyGraph constructor
	 */
	public MyBunnyGraph() {
		this.vertexMap = new LinkedHashMap<>();
		this.verticesWithBunnies = new LinkedHashSet<>();
	}

	/**
	 * Perform the search
	 */
	@Override
	public void go() {

		if (this.DEBUG)
			System.out.println(this.toString());

		// run the dfs search
		PathResult result = this.findPath(this.entryVertex);

		// if we found a path
		// print it out
		if (result != null)
			System.out.println(result);
	}

	/**
	 * current total cost
	 */
	private int _cost = 0;

	/**
	 * Start a dfs for the path
	 *
	 * @param a starting vertex
	 * @return PathResult with the visited nodes, found bunnies, and total cost.
	 * Also returns null if path could not be found
	 */
	private PathResult findPath(VertexNode a) {
		this._cost = 0;
		return findPath(
				a,
				new LinkedHashSet<>(),
				new LinkedHashSet<>()
		);
	}

	/**
	 * Internal version of findPath for recursion
	 *
	 * @param a       start vertex
	 * @param visited current set of visited vert names
	 * @param found   current set of found bunnies
	 * @return PathResult or null if path not found
	 */
	private PathResult findPath(VertexNode a, LinkedHashSet<String> visited, LinkedHashSet<String> found) {

		if (this.DEBUG) {
			for (String node : visited) {
				if (verticesWithBunnies.contains(node))
					System.out.print("*");
				System.out.print(node);
				System.out.print(" ");
			}
			System.out.print("(" + a.name + ") " + this._cost);
		}


		// node was already visited
		if (visited.contains(a.name)) {
			if (this.DEBUG)
				System.out.print(" visited\n");
			return null;
		}

		if (this.DEBUG)
			System.out.print("\n");

		// add current node to visited
		visited.add(a.name);

		// if a contains a bunny
		if (a.bunny != null) {

			// add bunny to found set
			found.add(a.bunny);

			// we found 4 bunnies
			if (found.size() > 3)
				return new PathResult(visited, found, this._cost);
		}

		// try going to the adjacent nodes
		for (String adjacent : a.edges.keySet()) {

			// get edge cost
			int adjCost = a.edges.get(adjacent);

			// add the total cost if the next node hasn't been visited
			if (!visited.contains(adjacent))
				this._cost += adjCost;

			// go to adjacent node
			PathResult result = findPath(
					this.vertexMap.get(adjacent),
					visited,
					found
			);

			// return result if not null
			if (result != null)
				return result;
		}

		// 404 path not found
		return null;
	}


	/**
	 * Struct for path info returned by findPath
	 */
	static class PathResult {

		/**
		 * set of the names of each node in the path in order
		 */
		final LinkedHashSet<String> path;

		/**
		 * set of the found bunnies
		 */
		final LinkedHashSet<String> found;

		/**
		 * total cost of searching the path
		 */
		final int cost;

		/**
		 * PathResult Constructor
		 *
		 * @param path  set of the names of each node in the path in order
		 * @param found set of the found bunnies
		 * @param cost  total cost of searching the path
		 */
		PathResult(
				LinkedHashSet<String> path,
				LinkedHashSet<String> found,
				int cost
		) {
			this.path = path;
			this.found = found;
			this.cost = cost;
		}

		/**
		 * @return PathResult as a string
		 */
		@Override
		public String toString() {
			String s = "Search path ";

			for (String vertexName : this.path)
				s += vertexName;

			s += ", (Cost " + this.cost + ")\nFound: ";

			Iterator<String> iter = this.found.iterator();
			while (true) {
				String name = iter.next();

				if (!iter.hasNext())
					return s + "and " + name;
				else
					s += name + ", ";
			}
		}
	}


	/**
	 * Adds a vertex to the graph from an input string.
	 * <p>
	 * format: V [B] W1 C1 W2 C2 W3 C3...
	 *
	 * @param input input string from file
	 */
	public void addVertexFromString(String input) {

		if (this.DEBUG)
			System.out.println(input);

		// split the string by spaces
		String[] inputSplit = input.split(" ");

		// the node we are going to create
		VertexNode vertexNode;

		// start of the for loop below
		int i = 1;

		// vertex contains a bunny
		if (inputSplit[0].startsWith("*")) {

			// undercover agent
			// so we can ignore the bunny
			if (inputSplit[1].startsWith("Z")) {
				// create a node
				vertexNode = new VertexNode(inputSplit[0].substring(1));
			} else {
				// create a node with a bunny
				vertexNode = new VertexNode(inputSplit[0].substring(1));
				vertexNode.bunny = inputSplit[1];
			}

			// shift start of our for loop by 1
			i++;
		} else
			// create a node
			vertexNode = new VertexNode(inputSplit[0]);

		// read the adjacent edges
		for (; i < inputSplit.length; i += 2) {

			// name of the adjacent node
			String otherName = inputSplit[i];

			// skip edges that point to the same vertex
			if (otherName.equals(vertexNode.name))
				continue;

			// parse the cost
			int cost = Integer.parseInt(inputSplit[i + 1]);

			// skip edges with cost > 10
			if (cost > 10)
				continue;

			// add edge to our new node
			vertexNode.addEdge(inputSplit[i], cost);
		}

		// if this is the first vertex
		if (this.entryVertex == null)
			this.entryVertex = vertexNode;

		// add new vertex to the vertex map
		this.vertexMap.put(vertexNode.name, vertexNode);

		// if vertex has bunny
		if (vertexNode.bunny != null)
			// add to the vertex with bunnies list
			this.verticesWithBunnies.add(vertexNode.name);
	}

	/**
	 * Node Class representing a Vertex in the graph
	 */
	private static class VertexNode {

		/**
		 * Vertex name
		 */
		String name;

		/**
		 * Bunny name.
		 * null if vertex does not contain a bunny
		 */
		String bunny;

		/**
		 * map of adjacent edges.
		 * Key is the Node name.
		 * Value is the cost.
		 */
		LinkedHashMap<String, Integer> edges;

		/**
		 * VertexNode constructor
		 *
		 * @param name name for the vertex
		 */
		VertexNode(String name) {
			this.name = name;
			this.edges = new LinkedHashMap<>();
		}

		/**
		 * Add a new edge to the vertex node
		 *
		 * @param vertexName name of the other vertex
		 * @param cost       cost of the edge
		 */
		void addEdge(String vertexName, int cost) {

			// update existing node
			if (this.edges.containsKey(vertexName)) {
				this.edges.put(
						vertexName,
						Math.min(this.edges.get(vertexName), cost)
				);
				return;
			}

			// add new edge node
			this.edges.put(vertexName, cost);
		}

		/**
		 * string representation of the VertexNode
		 *
		 * @return
		 */
		@Override
		public String toString() {
			String s = "Vertex ";
			if (this.bunny != null)
				s += "*";

			s += this.name + ": ";

			for (String key : this.edges.keySet())
				s += "(" + key + "," + this.edges.get(key) + ") ";

			return s;
		}

		/**
		 * Compares the names of two VertexNodes to determine if they are equal.
		 *
		 * @param obj other object to compare this one to
		 * @return true if they are equal
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof VertexNode)
				return this.name.equals(((VertexNode) obj).name);
			else
				return super.equals(obj);
		}
	}

	/**
	 * @return String representation of this graph
	 */
	@Override
	public String toString() {
		String s = "";

		for (VertexNode vertex : this.vertexMap.values())
			s += vertex.toString() + "\n";

		return s;
	}

	/**
	 * @return my name
	 */
	@Override
	public String getMyName() {
		return "Jacob Coughenour";
	}

	/**
	 * Prints greeting message
	 */
	@Override
	public void hello() {
		System.out.println("Welcome to the Bobcat DFS Search!");
	}

	/**
	 * Prints exit message
	 */
	@Override
	public void exit() {
		System.out.println("");
		System.out.println("Exiting...");
	}

}

