package td.level;

import java.util.Random;

import jgf.math.Vector2D;
import jgf.pathfinding.MatrixGraph;
import td.cfg.GameConfig;
import td.gui.TDGame;
import td.pieces.Monster;
import td.pieces.template.MonsterTemplate;

public class WaveLevel {
	private int monsters;
	private int level;
	private MonsterTemplate monster;
	private int spawRate;
	private int monstersPerSpaw;
	private boolean bossLvl;

	private Random rdm = new Random();
	private long lastSpaw = 0;

	protected WaveLevel(int lvl) {
		this.level = lvl;
	}

	public WaveLevel(int monsters, MonsterTemplate monster, int level,
			int spawRate, int monstersPerSpaw, boolean bossLevel) {
		this.monsters = monsters;
		this.monster = monster;
		this.level = level;
		this.spawRate = spawRate;
		this.monstersPerSpaw = monstersPerSpaw;
		this.bossLvl = bossLevel;
	}

	public WaveLevel(int monsters, MonsterTemplate monster, int level,
			int spawRate, int monstersPerSpaw) {
		this(monsters, monster, level, spawRate, monstersPerSpaw, false);
	}

	public void proccess(MatrixGraph graph) {
		if (monsters <= 0)
			return;
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastSpaw > spawRate) {
			int half = GameConfig.TILE_WIDTH / 2;
			int halfSize = half * GameConfig.TILE_SIZE;
			int halfUtil = (halfSize - (5 * GameConfig.TILE_SIZE));
			int tilesUtil = 5 * GameConfig.TILE_SIZE;
			for (int i = 0; i < monstersPerSpaw; i++) {
				Vector2D pos = new Vector2D(0, halfUtil
						+ rdm.nextInt(tilesUtil));
				Vector2D target = new Vector2D(GameConfig.MAP_WIDTH - 20,
						halfUtil + rdm.nextInt(tilesUtil));
				Monster toAdd = monster.createMonster(pos, target, graph,
						level, bossLvl);
				toAdd.setDirection(toAdd.getPosition());
				toAdd.remakePath();

				TDGame.getInstance().getPiecesManager().add(toAdd);
				monsters--;
			}
			lastSpaw = currentTime;
		}
	}

	public boolean hasEnd() {
		return (monsters <= 0);
	}

	public int getLevel() {
		return level;
	}

	public MonsterTemplate getMonster() {
		return monster;
	}

	public boolean isBossLvl() {
		return bossLvl;
	}
}
