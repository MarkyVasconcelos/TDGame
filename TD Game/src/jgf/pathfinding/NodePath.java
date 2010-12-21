package jgf.pathfinding;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import jgf.math.Vector2D;

public class NodePath {
	private List<Node> path;
	private int x, y;
	private int targetX, targetY;

	public NodePath() {
		this(0, 0, 0, 0);
	}

	public NodePath(int x, int y, int tX, int tY) {
		this.x = x;
		this.y = y;
		this.targetX = tX;
		this.targetY = tY;
		path = new ArrayList<Node>();
	}

	public NodePath(List<Node> path) {
		this.path = path;
	}

	public List<Node> asList() {
		return new ArrayList<Node>(path);
	}

	public NodePath getPath() {
		return new NodePath(new ArrayList<Node>(path));
	}

	public NodePath getPath(Point2D start) {
		int current = 0;
		int distance = Integer.MAX_VALUE;
		int elegible = 0;
		Vector2D pos = new Vector2D(start);
		for (Node p : path) {
			Vector2D vec = new Vector2D(p.getX(), p.getY());
			double dist = vec.subtract(pos).getMagnitudeSqr();
			if (dist < distance) {
				distance = (int) dist;
				elegible = current;
			}
			current++;
		}
		return new NodePath(path.subList(elegible, path.size()));
	}

	public void remakePath(MatrixGraph graph) {
		AStar aStar = new AStar(graph);
		List<Node> nodePath = aStar.search(x, y, targetX, targetY);
		nodePath.remove(0);
		nodePath.add(new Node(targetX, targetY));
		path.clear();
		path.addAll(nodePath);
	}
}
