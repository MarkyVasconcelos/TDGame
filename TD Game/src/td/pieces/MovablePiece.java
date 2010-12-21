package td.pieces;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import jgf.imaging.ImageItem;
import jgf.math.Vector2D;
import jgf.pathfinding.AStar;
import jgf.pathfinding.MatrixGraph;
import jgf.pathfinding.Node;
import td.cfg.GameConfig;
import td.path.PathManager;

public abstract class MovablePiece extends Piece implements Cloneable {

	private List<Point2D> path;
	private MatrixGraph graph;
	private double speed;
	
	private int pathKey;
	private double xTarget, yTarget;

	public MovablePiece(Vector2D pos, Vector2D target, double speed,
			MatrixGraph graph, ImageItem image) {
		super(pos, image);

		path = new ArrayList<Point2D>();
		this.graph = graph;
		this.speed = speed;
		setDead(false);
		setTarget(target);
		pathKey = GameConfig.pixelToTile(pos.getX());
	}

	public MovablePiece(int x, int y, int xTarget, int yTarget, double speed,
			MatrixGraph graph, String imageName) {
		super(x, y, imageName);

		path = new ArrayList<Point2D>();
		this.graph = graph;
		this.speed = speed;
		setDead(false);
		this.xTarget = xTarget;
		this.yTarget = yTarget;
	}

	public void setTarget(double x, double y) {
		xTarget = x;
		yTarget = y;
	}

	public void setTarget(Vector2D target) {
		xTarget = target.getX();
		yTarget = target.getY();
	}

	public boolean move() {
		if (path.isEmpty())
			return false;
		Vector2D tilePos = new Vector2D(path.get(0).getX(), path.get(0).getY());
		Vector2D trail = tilePos.subtract(getPosition());

		getDirection().rotateMe(getDirection().angleBetween(trail));

		Vector2D step = getDirection().multiply(speed);

		double newX = Math.min(GameConfig.MAP_WIDTH - getWidth(), Math.max(0,
				getPosition().getX() + step.getX()));

		double newY = Math.min(GameConfig.MAP_HEIGHT - getHeight(), Math.max(0,
				getPosition().getY() + step.getY()));

		setPosition(newX, newY);

		if (trail.getMagnitude() < 6)
			path.remove(0);

		return !path.isEmpty();
	}

	public void makePath(Vector2D target) {
		makePath(target.getX(), target.getY());
	}

	public void makePath(double x, double y) {
		makePath(x, y, path, graph);
	}

	public void makePath(double x, double y, List<Point2D> path,
			MatrixGraph graph) {
		path.clear();
		int xI = GameConfig.pixelToTile(getPosition().getX());
		int yI = GameConfig.pixelToTile(getPosition().getY());
		if (PathManager.getPathManager().hasPath(xI, yI)) {
			path.addAll(PathManager.getPathManager().getPath(xI, yI));
			return;
		}

		AStar aStar = new AStar(graph);

		List<Node> nodePath = aStar.search(xI, yI, GameConfig.pixelToTile(x),
				GameConfig.pixelToTile(y), true);

		nodePath.remove(0);
		for (Node node : nodePath)
			path.add(new Point2D.Double(GameConfig.tileToPixel(node.getX()),
					GameConfig.tileToPixel(node.getY())));

		path.add(new Point2D.Double(x, y));

		PathManager.getPathManager().addPath(xI, yI, path);
	}

	@Override
	public Object clone() {
		try {
			MovablePiece clone = (MovablePiece) super.clone();
			clone.graph = graph;
			clone.path = new ArrayList<Point2D>();
			clone.speed = speed;
			clone.xTarget = xTarget;
			clone.yTarget = yTarget;
			return clone;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getXTarget() {
		return xTarget;
	}

	public double getYTarget() {
		return yTarget;
	}

	public MatrixGraph getGraph() {
		return graph;
	}

	public static boolean canMakePath(int xS, int yS, int xT, int yT,
			MatrixGraph graph) {
		return canMakePath(new Vector2D(xS, yS), new Vector2D(xT, yT), graph);
	}

	public static boolean canMakePath(Vector2D start, Vector2D target,
			MatrixGraph graph) {
		AStar aStar = new AStar(graph);

		List<Node> nodePath = aStar.search(
				GameConfig.pixelToTile(start.getX()), GameConfig
						.pixelToTile(start.getY()), GameConfig
						.pixelToTile(target.getX()), GameConfig
						.pixelToTile(target.getY()), true);

		return nodePath.size() != 0;
	}

	public List<Point2D> getPath() {
		return path;
	}

	public int getPathKey() {
		return pathKey;
	}
}
