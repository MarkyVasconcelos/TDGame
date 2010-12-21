package td.level;

import java.awt.Color;

public class LevelInfo {
	private int level;
	private Color color;
	private int start;
	private boolean boss;

	public LevelInfo(int lvl, Color color, int start, boolean boss) {
		this.level = lvl;
		this.color = color;
		this.start = start;
		this.boss = boss;
	}

	public int getLevel() {
		return level;
	}

	public Color getColor() {
		return color;
	}

	public int getStart() {
		return start;
	}

	public boolean isBossLevel() {
		return boss;
	}
}
