package td.pieces.template;

import java.awt.Color;

import jgf.math.Vector2D;
import jgf.pathfinding.MatrixGraph;
import td.cfg.GameConfig;
import td.effect.ImuneEffect;
import td.pieces.Monster;
import td.util.image.AnimationImage;
import td.util.image.ComposedImage;
import td.util.image.SingleImage;

public enum MonsterTemplate {
	Basic(1000, 1.1d, Color.gray, "monster-basic"), Fast(800, 3.d, Color.cyan,
			"monster-fast"), Imune(800, 1.2d, Color.pink, "monster-imune") {
		public Monster createMonster(Vector2D pos, Vector2D target,
				MatrixGraph graph, int lvl, boolean boss) {
			Monster m = super.createMonster(pos, target, graph, lvl, boss);
			m.setModifier(new ImuneEffect());
			return m;
		};
	};

	private MonsterTemplate(int maxHp, double speed, String image, Color color) {
		this.maxHp = maxHp;
		this.speed = speed;
		this.image = new SingleImage(GameConfig.images.getList("towers")
				.getByName(image));
		this.color = color;
	}

	private MonsterTemplate(int maxHp, double speed, Color color, String list) {
		this.maxHp = maxHp;
		this.speed = speed;
		this.image = new AnimationImage(GameConfig.images.getList(list));
		this.color = color;
	}

	public Monster createMonster(Vector2D pos, Vector2D target,
			MatrixGraph graph, int lvl, boolean boss) {
		int maxhp = maxHp + (maxHp * (boss ? lvl + 20 : lvl) / 5);
		int gp = (int) (3 + (lvl * (boss ? 1.4 : 0.6)));
		return new Monster(pos, target, speed, maxhp, graph, image, gp);
	}

	public Color getColor() {
		return color;
	}

	private int maxHp;
	private double speed;
	private ComposedImage image;
	private Color color;

}
