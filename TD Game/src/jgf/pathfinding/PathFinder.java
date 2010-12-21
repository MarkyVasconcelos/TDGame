package jgf.pathfinding;

import java.util.List;

public interface PathFinder
{
    public List<Node> search(int startx, int starty, int targetx, int targety);
}
