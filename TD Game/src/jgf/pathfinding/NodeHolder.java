/**
 * Project: Java Game Framework<br>
 * Filename: NodeHolder.java<br>
 * Created on: 09/08/2007<br>
 * <p>
 * @author Mendonça, Vinícius Godoy de
 * @author Moribe, Marcos
 */
package jgf.pathfinding;

/**
 * Holds a list of nodes.
 */
public interface NodeHolder extends Iterable<Node>
{

    /**
     * Clears all nodes from this node holder.
     */
    void clear();

    /**
     * Adds a node to the node Holder
     * 
     * @param startNode Node
     */
    void add(Node node);

    /**
     * Indicate if the node holder is completely empty.
     * 
     * @return True if no nodes are inside this holder, false if not.
     */
    boolean isEmpty();

    /**
     * Removes the node with the minimum f value from this node.
     * 
     * @return The removed node.
     * @see #remove(Node)
     */
    Node removeMinF();

    /**
     * Indicate if the given node is inside this holder.
     * 
     * @param node The node to test.
     * @return True if list contains the node, false if not.
     */
    boolean contains(Node node);

    /**
     * Find a node that is identical to this onde inside the holder. Returns
     * null if no node was found.
     * 
     * @param node The node to find.
     * @return The found node, or null, if no node was found.
     */
    Node find(Node node);

    /**
     * Removes the given node from the holder.
     * 
     * @param node A node to be removed.
     * @see #removeMinF()
     */
    void remove(Node node);

}
