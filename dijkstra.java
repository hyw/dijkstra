//
// SHORTESTPATHS.JAVA
// Compute shortest paths in a graph.
//
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("unchecked")

class ShortestPaths {
    
	public final static double INF = java.lang.Double.POSITIVE_INFINITY;
	
	public Handle handles[];
	public Map<Vertex, Edge> parents;
	public Multigraph g;
	public int i;

    //
    // constructor
    //
    public ShortestPaths(Multigraph G, int startId) {
    	PriorityQueue<Vertex> q = new PriorityQueue<Vertex>();
    	handles = new Handle[G.nVertices()]; //necessary because not able to index into q
    	parents = new HashMap<Vertex, Edge>();
    	g = G;
    	i = startId;
    	for (int i = 0; i < G.nVertices(); i++) { // insert all vertices with INF key
    		handles[i] = q.insert((int) INF, G.get(i)); // initally make all keys INF
    		parents.put(G.get(i), null); // initially set all vertex's parents to null
    	}
    	q.decreaseKey(handles[startId], 0); // after all inserted, index into ith vertex and set key to 0
    	handles[startId].key = 0; // update handle too
    	
    	while (!q.isEmpty()) { // while the queue isn't empty
    		Vertex curr = q.extractMin(); // the vertex element in q with the min key
    		Handle h = handles[curr.id()]; // the handle of curr
    		if (q.handleGetKey(h) == (int) INF) {
    			break; // can't reach anymore vertices
    		}
    		Vertex.EdgeIterator ei = curr.adj();
    		while (ei.hasNext()) { // while there is an adjacent vertex
    			Edge e = ei.next(); // the edge connecting the curr vertex to an adj vertex
    			Vertex next = e.to(); // an adj vertex
    			if (q.decreaseKey(handles[next.id()], q.handleGetKey(h) + e.weight())) {
    				handles[next.id()].key = q.handleGetKey(h) + e.weight(); // don't forget the handle
    				parents.put(next, e);
    			}
    		}
    	}
    }
    
    //
    // returnPath()
    // Return an array containing a list of edge ID's forming
    // a shortest path from the start vertex to the specified
    // end vertex.
    //
    public int [] returnPath(int endId) 
    { 
    	int[] path = null;
    	if (i == endId) {
    		path = new int[0];
    	} else {
    		ArrayList<Integer> list = new ArrayList<Integer>();
    		Vertex i = g.get(endId);
    		while (parents.get(i) != null) {
    			Edge e = parents.get(i);
    			list.add(e.id());
    			i = e.from();
    		}
    		Collections.reverse(list);
    		path = convertIntegers(list);
    	}
    	return path;
    }
    
    // Convert Integer[] to primitive int[]
    public static int[] convertIntegers(ArrayList<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }
}
