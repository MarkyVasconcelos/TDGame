package td.pieces;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jgf.imaging.ImageItem;
import jgf.pathfinding.MatrixGraph;
import td.effect.Effect;
import td.effect.TowerModifier;

public class BuffTower extends Tower {
	private TowerModifier modifier;

	public BuffTower(int x, int y, double visionRadius, int fireRate,
			int shootStrength, MatrixGraph graph, ImageItem image,
			TowerModifier effect) {
		super(x, y, visionRadius, fireRate, shootStrength, graph, image,
				new Effect(Effect.NONE, 0, 0), 0);
		this.modifier = effect;
	}

	@Override
	public void processAI() {
		Set<Piece> pieces = getVision();
		List<Tower> towers = getTowers(pieces);
		for (Tower t : towers)
			t.addModifier(modifier);
	}

	private List<Tower> getTowers(Set<Piece> pieces) {
		List<Tower> list = new ArrayList<Tower>();
		for (Piece p : pieces)
			if (p instanceof Tower)
				if (!(p instanceof BuffTower))
					list.add((Tower) p);
		return list;
	}
}
