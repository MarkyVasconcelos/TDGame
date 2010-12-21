package td.pieces;

import td.cfg.GameConfig;
import jgf.math.Vector2D;

public class Wall extends Piece {
	public Wall(int x, int y) {
		super(new Vector2D(x, y), GameConfig.images.getSingleImage("wall"));
	}

	@Override
	public void processAI() {
		//Don't do nothing.. Walls don't think
	}

}
