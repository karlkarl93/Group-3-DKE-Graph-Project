public class Edge {
	int u, v;
	
	/** Constructor for Edge objects, with two int parameters specifying the two points this edge connects
		@param u, the first vertex this edge connects
		@param v, the second vertex this edge connects
	*/
	public Edge(int u, int v) {
		this.u = u;
		this.v = v;
	}
	
	/** Just for demonstration purposes, can be deleted later on
	*/
	protected static Edge[] DEFAULT_EDGE_ARRAY() {
		Edge[] edges = {
			new Edge(1,6),
			new Edge(1,5),
			new Edge(2,5),
			new Edge(2,4),
			new Edge(3,4),
			new Edge(5,3),
			new Edge(6,8),
			new Edge(7,5),
			new Edge(8,1),
			new Edge(9,2),
			new Edge(10,8),
			new Edge(11,7),
			new Edge(12,3),
			new Edge(13,5),
			new Edge(14,6),
			new Edge(15,8),
			new Edge(16,2),
			new Edge(17,1),
			new Edge(18,4),
		};
		
		return edges;
	}
	
	/** For demonstration purposes
	*/
	public String toString() {
		return u + " - " + v;
	}
}