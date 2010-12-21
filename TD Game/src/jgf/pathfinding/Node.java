package jgf.pathfinding;

public class Node
{    
	private int x;
	private int y; // graph coordinates
	private float f;
	private float g;
	private float h; // used by A*

	private Node parent;
	private Node next;
	private Node prev;

	Node(int x, int y, Node parent, float g)
	{
		this.x = x;
		this.y = y;
		this.parent = parent;
		this.g = g;
		f = 0;
		h = 0;
	}

	public Node(int x, int y)
	{
		this(x, y, null, 0);
	}

	/** calcula a distancia do no ao destino */
	void estimate(Node target)
	{
		if (target == null) // Dijkstra
			h = 0;
		else
		// A*
		{
			float dx = target.x - x;
			float dy = target.y - y;
			h = (float) Math.sqrt(dx * dx + dy * dy);
		}

		f = g + h;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;

		if (!(obj instanceof Node))
			return false;

		Node n = (Node) obj;
		return x == n.x && y == n.y;
	}

	@Override
	public int hashCode()
	{
		return x * 1000 + y;
	}

	public float getF()
	{
		return f;
	}

	public float getG()
	{
		return g;
	}

	public float getH()
	{
		return h;
	}

	public Node getNext()
	{
		return next;
	}

	public Node getParent()
	{
		return parent;
	}

	public Node getPrev()
	{
		return prev;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}
	
	@Override
	public String toString()
	{
		return "x: " + x + "  y: " + y + " f: " + f + "  g: " + g + "  h: "+ h;
	}

	public void setNext(Node next)
	{
		this.next = next;
	}

	public void setParent(Node parent)
	{
		this.parent = parent;
	}

	public void setPrev(Node prev)
	{
		this.prev = prev;
	}
}
