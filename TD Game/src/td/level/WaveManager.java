package td.level;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jgf.pathfinding.MatrixGraph;

public class WaveManager {
	private List<WaveLevel> waves;
	private int currentLevel;
	private long lastWave;

	public WaveManager() {
		waves = new ArrayList<WaveLevel>();
		currentLevel = 0;
	}

	public void add(WaveLevel level, int delay) {
		waves.add(level);
	}

	public void sortLevels() {
		Collections.sort(waves, new Comparator<WaveLevel>() {
			public int compare(WaveLevel arg0, WaveLevel arg1) {
				return arg0.getLevel() - arg1.getLevel();
			}
		});
	}

	public void proccess(MatrixGraph graph) {
		if (lastWave == 0)
			lastWave = System.currentTimeMillis();

		waves.get(currentLevel).proccess(graph);
		System.out.println(currentLevel);
		if (waves.get(currentLevel).hasEnd()) {
			if ((System.currentTimeMillis() - lastWave) / 1000 >= 20) {
				lastWave = System.currentTimeMillis();
				if (currentLevel != waves.size() - 1) {
					currentLevel++;
				}
			}
		}
	}

	public int getTotalLevels() {
		return waves.size();
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public List<LevelInfo> getLevelsInfos() {
		List<LevelInfo> list = new ArrayList<LevelInfo>();
		if (currentLevel == getTotalLevels() - 1
				&& waves.get(currentLevel).hasEnd())
			return list;
		int gap = (int) ((System.currentTimeMillis() - lastWave) / 1000);
		gap = 20 - gap;
		gap *= 3;
		int x = 0;
		list.add(new LevelInfo(currentLevel, Color.black, 0, false));
		for (int i = currentLevel + 1; i < getTotalLevels(); i++) {
			int start = (x++ * 60) + gap;
			WaveLevel lvl = waves.get(i);
			list.add(new LevelInfo(i, lvl.getMonster().getColor(), start, lvl
					.isBossLvl()));
		}
		return list;
	}
}
