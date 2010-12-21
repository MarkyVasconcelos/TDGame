/**
 * Project: Java Game Framework<br>
 * Filename: NodeSet.java<br>
 * Created on: 09/08/2007<br>
 * <p>
 * @author Mendonça, Vinícius Godoy de
 * @author Moribe, Marcos
 */
package jgf.pathfinding;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A simple Node Holder implementation.
 */
public class DefaultNodeHolder implements NodeHolder
{
    Set<Node> nodes = new HashSet<Node>();
    
    public void add(Node node)
    {
        nodes.add(node);
    }

    public void clear()
    {
        nodes.clear();
    }

    public boolean contains(Node node)
    {
        return nodes.contains(node);
    }

    public Node find(Node node)
    {
        for (Node n : nodes)
            if (n.equals(node))
                return n;
        return null;
    }

    public boolean isEmpty()
    {
        return nodes.isEmpty();
    }

    public void remove(Node node)
    {
        nodes.remove(node);
    }

    public Node removeMinF()
    {
        Node min = null;
        for (Node n : nodes)
            if (min == null || n.getF() < min.getF())
                min = n;
        
        remove(min);
        return min;
            
    }

    public Iterator<Node> iterator()
    {
        return nodes.iterator();
    }

}
