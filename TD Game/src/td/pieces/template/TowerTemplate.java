package td.pieces.template;

import java.awt.Graphics2D;

import jgf.imaging.ImageItem;
import jgf.pathfinding.MatrixGraph;
import td.cfg.GameConfig;
import td.effect.Effect;
import td.effect.TowerModifier;
import td.pieces.BuffTower;
import td.pieces.Tower;

public enum TowerTemplate {
	Basic(80.d, 1000, 70, "tower", "Basic tower.\n10 Gold.", 10, 0) {
		public Effect getEffect() {
			return new Effect(Effect.NONE, 0, 0);
		}

		public TowerTemplate getNextTower() {
			return Basic2;
		}
	},
	Basic2(160.d, 900, 90, "tower", "Regular fire rate, radius and damage.",
			60, 0) {
		public Effect getEffect() {
			return new Effect(Effect.NONE, 0, 0);
		}

		public TowerTemplate getNextTower() {
			return Basic3;
		}
	},
	Basic3(220.d, 800, 110, "tower",
			"Large radius, regular fire rate and high damage", 150, 0) {
		public Effect getEffect() {
			return new Effect(Effect.NONE, 0, 0);
		}

		public TowerTemplate getNextTower() {
			return null;
		}
	},
	Fire(100.d, 1000, 80, "tower_fire", "Fire tower, splash damage.\n30 Gold.",
			30, 20) {
		public Effect getEffect() {
			return new Effect(Effect.NONE, 0, 0);
		}

		public TowerTemplate getNextTower() {
			return Fire2;
		}
	},
	Fire2(
			110.d,
			900,
			120,
			"tower_fire",
			"Fire tower, splash damage.\n120 Gold.",
			120, 20) {
		public Effect getEffect() {
			return new Effect(Effect.NONE, 0, 0);
		}

		public TowerTemplate getNextTower() {
			return Fire3;
		}
	},
	Fire3(
			120.d,
			860,
			140,
			"tower_fire",
			"Fire tower, splash damage.\n210 Gold.",
			210, 20) {
		public Effect getEffect() {
			return new Effect(Effect.NONE, 0, 0);
		}

		public TowerTemplate getNextTower() {
			return null;
		}
	},
	Ice(
			60.d,
			1000,
			0,
			"tower_ice",
			"Ice tower, affect creeps in a small radius for 20% slow for 5 seconds.\n30 Gold.",
			30, 25) {
		public Effect getEffect() {
			return new Effect(Effect.SLOW, 5, 20);
		}

		public TowerTemplate getNextTower() {
			return Ice2;
		}
	},
	Ice2(
			90.d,
			800,
			0,
			"tower_ice",
			"Ice tower, affect creeps in a small radius for 30% slow for 10 seconds.\n90 Gold.",
			90, 30) {
		public Effect getEffect() {
			return new Effect(Effect.SLOW, 10, 30);
		}

		public TowerTemplate getNextTower() {
			return Ice3;
		}
	},
	Ice3(
			100.d,
			700,
			0,
			"tower_ice",
			"Ice tower, affect creeps in a small radius for 35% slow for 15 seconds.\n160 Gold.",
			160, 30) {
		public Effect getEffect() {
			return new Effect(Effect.SLOW, 15, 35);
		}

		public TowerTemplate getNextTower() {
			return null;
		}
	},
	DamageBoost(40, 0, 0, "tower_damage",
			"Damage boost damage of all towers in range in 10%.\n50 Gold.", 50,
			0) {
		@Override
		public Effect getEffect() {
			return null;
		}

		@Override
		public TowerTemplate getNextTower() {
			return null;
		}

		@Override
		public Tower createTower(int x, int y, MatrixGraph graph) {
			TowerModifier modifier = new TowerModifier(Integer.valueOf(String
					.valueOf(x).concat(String.valueOf(y)))) {
				@Override
				public void modify(Tower t) {
					t.setShootStrenght((t.getShootStrenght() * 110 / 100));
				}
			};
			Tower t = new BuffTower(x, y, getVisionRadius(), getFireRate(),
					getShootStrenght(), graph, getImageItem(), modifier);
			t.setTemplate(this);

			return t;
		}

	},
	FireRateBoost(40, 0, 0, "tower_firerate",
			"Fire rate boost in all towers in range in 10%.\n50 Gold.", 50, 0) {
		@Override
		public Effect getEffect() {
			return null;
		}

		@Override
		public TowerTemplate getNextTower() {
			return null;
		}

		@Override
		public Tower createTower(int x, int y, MatrixGraph graph) {
			TowerModifier modifier = new TowerModifier(Integer.valueOf(String
					.valueOf(x).concat(String.valueOf(y)))) {
				@Override
				public void modify(Tower t) {
					t.setFireRate(t.getFireRate() * 90 / 100);
				}
			};
			Tower t = new BuffTower(x, y, getVisionRadius(), getFireRate(),
					getShootStrenght(), graph, getImageItem(), modifier);
			t.setTemplate(this);

			return t;
		}

	},
	RangeBoost(40, 0, 0, "tower_range",
			"Range boost in all towers in range in 10%.\n50 Gold.", 50, 0) {
		@Override
		public Effect getEffect() {
			return null;
		}

		@Override
		public TowerTemplate getNextTower() {
			return null;
		}

		@Override
		public Tower createTower(int x, int y, MatrixGraph graph) {
			TowerModifier modifier = new TowerModifier(Integer.valueOf(String
					.valueOf(x).concat(String.valueOf(y)))) {
				@Override
				public void modify(Tower t) {
					t.setRadius(t.getVisionRadius() * 110 / 100);
				}
			};
			Tower t = new BuffTower(x, y, getVisionRadius(), getFireRate(),
					getShootStrenght(), graph, getImageItem(), modifier);
			t.setTemplate(this);

			return t;
		}

	};

	private TowerTemplate(double visionRadius, int fireRate, int shotStrenght,
			String image, String info, int value, double shootSplash) {
		this.visionRadius = visionRadius;
		this.fireRate = fireRate;
		this.shootStrenght = shotStrenght;
		this.image = GameConfig.images.getList("towers").getByName(image);
		this.info = info;
		this.value = value;
		this.shootSplash = shootSplash;
	}

	public Tower createTower(int x, int y, MatrixGraph graph) {
		Tower t = new Tower(x, y, visionRadius, fireRate, shootStrenght, graph,
				image, getEffect(), shootSplash);
		t.setTemplate(this);
		return t;
	}

	private double visionRadius, shootSplash;
	private int fireRate, shootStrenght;
	private ImageItem image;
	private String info;
	private int value;

	public abstract TowerTemplate getNextTower();

	public abstract Effect getEffect();

	public void draw(Graphics2D surface, int x, int y) {
		image.draw(surface, x, y);
	}

	public ImageItem getImageItem() {
		return image;
	}

	public String getInfo() {
		return info;
	}

	public int getValue() {
		return value;
	}

	public double getVisionRadius() {
		return visionRadius;
	}

	public int getFireRate() {
		return fireRate;
	}

	public int getShootStrenght() {
		return shootStrenght;
	}
}
