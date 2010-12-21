package td.effect;

import java.util.ArrayList;
import java.util.List;

import td.pieces.Tower;

public abstract class TowerModifier {
	private int id;
	private List<Tower> buffeds;

	public TowerModifier(int id) {
		this.id = id;
		buffeds = new ArrayList<Tower>();
	}

	public boolean canBuff(Tower t) {
		return buffeds.contains(t);
	}

	public abstract void modify(Tower t);

	public int getId() {
		return id;
	}
}
