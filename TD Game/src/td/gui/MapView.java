package td.gui;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import jgf.core.LoopSteps;
import jgf.pathfinding.MatrixGraph;
import td.cfg.GameConfig;
import td.hud.HighUpDisplayRender;
import td.hud.LevelScrollBar;
import td.level.EmptyWave;
import td.level.WaveLevel;
import td.pieces.template.MonsterTemplate;

public class MapView extends Canvas implements LoopSteps {
	private static final long serialVersionUID = 6181614595872580181L;

	private TileBackground background;
	private List<HighUpDisplay> huds;
	private MatrixGraph graph;

	public MapView() {
		huds = new ArrayList<HighUpDisplay>();
		graph = new MatrixGraph(GameConfig.TILE_WIDTH, GameConfig.TILE_HEIGHT);

		setMinimumSize(new Dimension(GameConfig.MAP_WIDTH,
				GameConfig.MAP_HEIGHT));
		setPreferredSize(getMinimumSize());
		setMaximumSize(getMinimumSize());

		background = new TileBackground(GameConfig.TILE_WIDTH,
				GameConfig.TILE_HEIGHT, "background", GameConfig.TILE_SIZE);
	}

	public void setupLevel() {
		TDGame.getInstance().getWaveManager().add(new EmptyWave(0), 30);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(5, MonsterTemplate.Basic, 1, 500, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(5, MonsterTemplate.Fast, 2, 500, 1), 1);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(10, MonsterTemplate.Basic, 3, 500, 5), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(5, MonsterTemplate.Fast, 4, 1000, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(15, MonsterTemplate.Imune, 5, 1000, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(5, MonsterTemplate.Fast, 6, 10, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(20, MonsterTemplate.Basic, 7, 500, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(5, MonsterTemplate.Basic, 8, 1000, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(25, MonsterTemplate.Fast, 9, 500, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(5, MonsterTemplate.Basic, 10, 1000, 1, true), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(30, MonsterTemplate.Fast, 11, 500, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(10, MonsterTemplate.Imune, 12, 500, 1), 1);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(35, MonsterTemplate.Basic, 13, 500, 5), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(10, MonsterTemplate.Fast, 14, 1000, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(40, MonsterTemplate.Imune, 15, 1000, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(10, MonsterTemplate.Fast, 16, 10, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(45, MonsterTemplate.Basic, 17, 500, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(10, MonsterTemplate.Basic, 18, 1000, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(60, MonsterTemplate.Imune, 19, 500, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(65, MonsterTemplate.Fast, 20, 1000, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(65, MonsterTemplate.Fast, 21, 500, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(70, MonsterTemplate.Imune, 22, 500, 1), 1);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(70, MonsterTemplate.Basic, 23, 500, 5), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(75, MonsterTemplate.Fast, 24, 500, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(75, MonsterTemplate.Imune, 25, 500, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(80, MonsterTemplate.Fast, 26, 10, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(80, MonsterTemplate.Basic, 27, 500, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(85, MonsterTemplate.Fast, 28, 500, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(85, MonsterTemplate.Fast, 29, 500, 1), 10);
		TDGame.getInstance().getWaveManager().add(
				new WaveLevel(90, MonsterTemplate.Imune, 30, 1000, 1, true), 10);
		TDGame.getInstance().getWaveManager().sortLevels();

		addHUD(300, 10, 200, 20, new td.hud.Stats());
		addHUD(100, 550, 400, 30, new LevelScrollBar(TDGame.getInstance()
				.getWaveManager()));
	}

	@SuppressWarnings("unused")
	private void drawGraph(Graphics2D g2d) {
		Graphics2D surface = (Graphics2D) g2d.create();

		for (int i = 0; i < graph.getWidth(); i++)
			for (int j = 0; j < graph.getHeight(); j++) {
				surface.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_IN, 0.3F));
				surface.setColor(Color.RED.brighter());
				if (graph.isWall(i, j))
					surface.draw(new Rectangle2D.Double(i
							* GameConfig.TILE_SIZE, j * GameConfig.TILE_SIZE,
							GameConfig.TILE_SIZE, GameConfig.TILE_SIZE));
			}
		g2d.dispose();
	}

	public MatrixGraph getMatrixGraph() {
		return graph;
	}

	public void draw(Graphics2D surface) {
		Graphics2D g2d = (Graphics2D) surface.create();
		background.draw(g2d);
		TDGame.getInstance().getPiecesManager().draw(g2d);
		for (HighUpDisplay hud : huds)
			hud.renderGraphics(g2d);
		g2d.dispose();
	}

	public void paintScreen() {
		getBufferStrategy().show();
	}

	public void processLogics() {
		TDGame.getInstance().getPiecesManager().processPiecesAI();
		TDGame.getInstance().getWaveManager().proccess(graph);
		
		for (HighUpDisplay hud : huds)
			hud.render.processLogics();
		if(GameConfig.player.isOver()){
			JOptionPane.showMessageDialog(null, "Game Over!");
			TDGame.getInstance().getMainLoop().stop();
			return;
		}
	}

	public void renderGraphics() {
		Graphics2D g2d = (Graphics2D) getBufferStrategy().getDrawGraphics();
		g2d.clearRect(0, 0, GameConfig.MAP_WIDTH, GameConfig.MAP_HEIGHT);
		draw(g2d);
		g2d.dispose();

	}

	public void setup() {
		createBufferStrategy(2);
		setIgnoreRepaint(false);
		requestFocus();
	}

	
	public void tearDown() {
	}

	public void addHUD(int x, int y, int w, int h, HighUpDisplayRender render) {
		huds.add(new HighUpDisplay(x, y, w, h, render));
	}

	private class HighUpDisplay {
		int x, y, w, h;
		HighUpDisplayRender render;

		public HighUpDisplay(int x, int y, int w, int h,
				HighUpDisplayRender render) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.render = render;
		}

		public void renderGraphics(Graphics2D g2d) {
			Graphics2D drawArea = (Graphics2D) g2d.create(x, y, w, h);
			drawArea.setClip(0, 0, w, h);
			render.renderGraphics(drawArea);
			drawArea.dispose();
		}
	}
}
