package td.hud;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import td.level.LevelInfo;
import td.level.WaveManager;

public class LevelScrollBar implements HighUpDisplayRender {
	private WaveManager manager;

	private List<LevelInfo> levels;

	public LevelScrollBar(WaveManager manager) {
		this.manager = manager;
	}

	public void processLogics() {
		levels = manager.getLevelsInfos();

	}

	public void renderGraphics(Graphics2D g2d) {
		for (LevelInfo info : levels) {
			g2d.setColor(info.getColor());
			g2d.fillRect(info.getStart(), 0, 60, 20);
			g2d.setColor(info.isBossLevel() ? Color.red : Color.black);
			g2d.drawString(String.valueOf(info.getLevel()),
					info.getStart() + 25, 15);
		}
		g2d.dispose();
	}
}
