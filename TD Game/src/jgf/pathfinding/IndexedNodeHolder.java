/** Siemens Ltda.
 *<p> 
 *    Project: Java Game Framework<br>
 *   Filename: IndexedNodeSet.java<br>
 * Created on: 09/08/2007<br>
 */
package jgf.pathfinding;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A Node Holder implementation based on a map.
 * The map indexes nodes so time to find an specific node is close to O(1).
 */
public class IndexedNodeHolder implements NodeHolder
{
    Map<Long, Node> index = new HashMap<Long, Node>();
    
    private long keyFor(Node node)
    {
        return ((long)node.getX() << 32) + node.getY();
    }
    
    public void add(Node node)
    {
        index.put(keyFor(node), node);
    }

    public void clear()
    {
        index.clear();
    }

    public boolean contains(Node node)
    {
        return index.containsKey(keyFor(node));        
    }

    public Node find(Node node)
    {
        return index.get(keyFor(node));
    }

    public boolean isEmpty()
    {
        return index.isEmpty();
    }

    public void remove(Node node)
    {
        index.remove(keyFor(node));
    }

    public Node removeMinF()
    {
        Node min = null;
        for (Node n : index.values())
            if (min == null || n.getF() < min.getF())
                min = n;
        
        remove(min);
        return min;
            
    }

    public Iterator<Node> iterator()
    {
        return index.values().iterator();
    }
}
