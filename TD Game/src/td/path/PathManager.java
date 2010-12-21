package td.path;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import td.util.MappedMap;

public class PathManager {
	private static PathManager singleton;

	public static PathManager getPathManager() {
		if (singleton == null)
			singleton = new PathManager();
		return singleton;
	}

	private MappedMap<Integer, Integer, List<Point2D>> cache;

	public PathManager() {
		cache = new MappedMap<Integer, Integer, List<Point2D>>();
	}

	public List<Point2D> getPath(int x, int y) {
		List<Point2D> returnList = new ArrayList<Point2D>(cache.get(x, y));
		return returnList;
	}

	public boolean hasPath(int x, int y) {
		return cache.contains(x, y);
	}

	public void addPath(int x, int y, List<Point2D> path) {
		cache.put(x, y, new ArrayList<Point2D>(path));
	}

	public void clear() {
		cache.clear();
	}
}
