package td.effect;

import td.pieces.Monster;

public class Effect {
	private int effectRate;
	private int duration;

	@SuppressWarnings("unused")
	private long lastTick;
	private long firstTick;

	private int id;

	public Effect(int id, int duration, int effectRate) {
		this.id = id;
		this.duration = duration;
		this.effectRate = effectRate;
	}

	public int getId() {
		return id;
	}

	public int getDuration() {
		return duration;
	}

	public boolean hasEnd() {
		long elapsed = System.currentTimeMillis() - firstTick;
		return (elapsed > duration * 1000);
	}

	public void inflict(Monster monster) {
		if (id == NONE)
			return;

		if (firstTick == 0)
			firstTick = System.currentTimeMillis();

		if (hasEnd()) {
			monster.setSpeed(monster.getMaxSpeed());
			return;
		}

		if (id == SLOW) {
			monster
					.setSpeed((monster.getMaxSpeed() * (100 - effectRate)) / 100);
		}
	}

	public Object clone() {
		return new Effect(id, duration, effectRate);
	}

	public int hashCode() {
		return id;
	}

	public boolean equals(Object other) {
		if (other == null || !(other instanceof Effect))
			return false;
		return ((Effect) other).id == id;
	}

	public static final int NONE = 0;
	public static final int SLOW = 1;
}
