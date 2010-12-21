package td.pieces;

import java.util.HashSet;
import java.util.Set;

import jgf.pathfinding.MatrixGraph;
import td.effect.Effect;

public class SplashShoot extends Shoot {
	public SplashShoot(int x, int y, Monster target, int strength,
			double speed, MatrixGraph graph, Effect effect, double splashRadius) {
		super(x, y, target, strength, speed, graph, effect);
		setVisionRadius(splashRadius);
		setVisionAngle(360);
	}

	protected void hitTarget() {
		Set<Monster> vision = getMonsters(getVision());
		for (Monster target : vision) {
			target.setHealth(target.getHealth() - getStr());
			target.addEffect((Effect) getEffect().clone());
		}
	}

	private Set<Monster> getMonsters(Set<Piece> pieces) {
		Set<Monster> monsters = new HashSet<Monster>();
		for (Piece piece : pieces)
			if (piece instanceof Monster)
				monsters.add((Monster) piece);
		return monsters;
	}
}
