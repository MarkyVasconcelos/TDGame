package jgf.pathfinding;

/**
 * Represents a graph that can be used in A* algorithm. 
 */
public interface Graph
{
    /**
     * Builds a list of sucessors node for this graph.
     * 
     * @param node A node to build the sucessors to.
     * @return The sucessors of this node.
     */
    NodeHolder buildSucessors(Node node);
    
    /**
     * Builds a list of sucessors node for this graph.
     * 
     * @param node A node to build the sucessors to.
     * @return The sucessors of this node.
     */
    NodeHolder buildSucessorsIgnoreEdges(Node node);

    /**
     * Test if the node is in the graph. Return true if it is, false if not.
     * 
     * @param node Not to test the limits.
     */
    boolean testLimits(Node node);

    /**
     * Creates a new Node Holder.
     * 
     * @return A new Node Holder.
     */
    NodeHolder createHolder();
}
