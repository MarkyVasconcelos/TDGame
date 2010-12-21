package td.gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jgf.core.LoopSteps;
import jgf.core.MainLoop;
import jgf.pathfinding.MatrixGraph;
import td.cfg.GameConfig;
import td.cfg.PiecesManager;
import td.level.WaveManager;
import td.path.PathManager;
import td.pieces.MovablePiece;
import td.pieces.Tower;
import td.pieces.Wall;
import td.pieces.template.TowerTemplate;
import td.player.Player;
import td.util.MappedMap;

public class TDGame implements LoopSteps {
	private static final long serialVersionUID = -8012159790479062135L;

	private MappedMap<Integer, Integer, Tower> towers;
	private JFrame gameFrame;
	private MainLoop mainLoop;
	private WaveManager waveManager;
	private PiecesManager piecesManager;
	private TowerInfo towerInfo;
	private List<ActionDispatcher> logicActions;
	private MapView mapView;
	private BuildGUI buildGUI;
	private TowerGroup towerGroup;

	private static TDGame instance;

	public static TDGame getInstance() {
		if (instance == null)
			instance = new TDGame();

		return instance;
	}

	private TDGame() {
		towers = new MappedMap<Integer, Integer, Tower>();
		logicActions = new ArrayList<ActionDispatcher>();
		mapView = new MapView();
		buildGUI = new BuildGUI();
		towerInfo = new TowerInfo();
		towerGroup = new TowerGroup();

		mapView.addKeyListener(buildGUI.createUnselectListener());
		createFrame();

		gameFrame.getContentPane().setLayout(new BorderLayout());
		gameFrame.getContentPane().add(mapView, BorderLayout.CENTER);

		JPanel sidePanels = new JPanel();
		sidePanels.setLayout(new BoxLayout(sidePanels, BoxLayout.PAGE_AXIS));
		sidePanels.add(buildGUI);
		sidePanels.add(towerInfo);
		sidePanels.add(new JPanel());
		gameFrame.getContentPane().add(sidePanels, BorderLayout.EAST);

		gameFrame.pack();
		gameFrame.setLocationRelativeTo(null);

		piecesManager = new PiecesManager();
		waveManager = new WaveManager();
	}

	private void createFrame() {
		gameFrame = new JFrame("Tower Defense Game");
		gameFrame.setSize(GameConfig.MAP_WIDTH, GameConfig.MAP_HEIGHT);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setVisible(true);
		gameFrame.createBufferStrategy(2);
		gameFrame.setIgnoreRepaint(false);

		MouseHandler mouseHandler = new MouseHandler();
		mapView.addMouseListener(mouseHandler);
		mapView.addMouseMotionListener(mouseHandler);

	}

	public void start() {
		mainLoop = new MainLoop(this, 20);
		new Thread(mainLoop).start();
	}

	public MapView getMapView() {
		return mapView;
	}

	public PiecesManager getPiecesManager() {
		return piecesManager;
	}

	public void paintScreen() {
		mapView.paintScreen();
	}

	public void processLogics() {
		for (int i = logicActions.size() - 1; i >= 0; i--) {
			logicActions.get(i).doAction();
			logicActions.remove(i);
		}
		mapView.processLogics();
	}

	public void renderGraphics() {
		mapView.renderGraphics();
	}

	public void setup() {
		MatrixGraph graph = mapView.getMatrixGraph();
		int half = GameConfig.TILE_WIDTH / 2;
		for (int i = 0; i < GameConfig.TILE_HEIGHT; i++) {
			graph.set(0, i, MatrixGraph.WALL);
			if (i >= half - 5 && i < half)
				continue;
			graph.set(GameConfig.TILE_WIDTH - 1, i, MatrixGraph.WALL);

			piecesManager.add(new Wall(0, i * GameConfig.TILE_SIZE));
			piecesManager.add(new Wall(
					((GameConfig.TILE_WIDTH - 1) * GameConfig.TILE_SIZE), i
							* GameConfig.TILE_SIZE));
		}
		for (int i = 0; i < GameConfig.TILE_WIDTH; i++) {
			graph.set(i, 0, MatrixGraph.WALL);
			graph.set(i, GameConfig.TILE_HEIGHT - 1, MatrixGraph.WALL);
			piecesManager.add(new Wall(i * GameConfig.TILE_SIZE, 0));
			piecesManager.add(new Wall(i * GameConfig.TILE_SIZE,
					((GameConfig.TILE_HEIGHT - 1) * GameConfig.TILE_SIZE)));
		}

		mapView.setup();
	}

	public void tearDown() {
	}

	public static void main(String[] args) {
		TDGame game = TDGame.getInstance();
		game.mapView.setupLevel();
		game.start();
	}

	private boolean tryAddTower(final int x, final int y) {
		if (mapView.getMatrixGraph().get(GameConfig.pixelToTile(x),
				GameConfig.pixelToTile(y)) == MatrixGraph.WALL)
			return false;
		if (buildGUI.getSelected() == null)
			return false;

		final int tileX = GameConfig.pixelToTile(x);
		final int tileY = GameConfig.pixelToTile(y);
		MatrixGraph clone = (MatrixGraph) mapView.getMatrixGraph().clone();
		clone.set(tileX, tileY, MatrixGraph.WALL);
		try {
			int half = GameConfig.TILE_WIDTH / 2;
			int halfSize = half * GameConfig.TILE_SIZE;
			int halfUtil = (halfSize - (5 * GameConfig.TILE_SIZE));

			for (int i = 0; i < 5; i++) {
				int path = halfUtil + (i * GameConfig.TILE_SIZE);
				if (!MovablePiece.canMakePath(0, path,
						GameConfig.MAP_WIDTH - 1, path, clone))
					return false;
			}
		} catch (Exception e) {
			return false;
		}

		final TowerTemplate tower = buildGUI.getSelected();
		final Player currentPlayer = GameConfig.player;

		if (currentPlayer.getGold() < tower.getValue())
			return false;

		getPiecesManager().makeTempPaths(clone);

		logicActions.add(new ActionDispatcher() {
			public void doAction() {
				mapView.getMatrixGraph().set(tileX, tileY, MatrixGraph.WALL);
				try {
					getPiecesManager().changePaths();
					PathManager.getPathManager().clear();
				} catch (Exception e) {
					e.printStackTrace();
				}

				Tower t = tower.createTower(x, y, mapView.getMatrixGraph());
				towerGroup.add(t);
				currentPlayer.spendGold(tower.getValue());
				towers.put(tileX, tileY, t);
				getPiecesManager().add(t);
			}
		});
		return true;
	}

	private class MouseHandler extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			int tileX = GameConfig.pixelToTile(e.getX());
			int tileY = GameConfig.pixelToTile(e.getY());
			if (!tryAddTower(GameConfig.tileToPixel(tileX), GameConfig
					.tileToPixel(tileY)))
				if (towers.hasValue(tileX, tileY)) {
					Tower t = towers.get(tileX, tileY);
					towerInfo.showInfo(t);
					towerGroup.setSelected(t);
				}
		}
	}

	public WaveManager getWaveManager() {
		return waveManager;
	}

	private interface ActionDispatcher {
		public void doAction();
	}

	public void unselectTowers() {
		towerGroup.setSelected(null);
	}

	private class TowerGroup {
		private List<Tower> towers;

		public TowerGroup() {
			towers = new ArrayList<Tower>();
		}

		public void add(Tower t) {
			towers.add(t);
		}

		public void setSelected(Tower t) {
			for (Tower l : towers)
				l.setSelected(false);
			if (t != null) {
				t.setSelected(true);
				buildGUI.unselect();
			}
		}
	}

	public void sellTower(Tower t) {
		GameConfig.player.addGold(t.getTemplate().getValue());
		int xTile = GameConfig.pixelToTile(t.getX());
		int yTile = GameConfig.pixelToTile(t.getY());
		mapView.getMatrixGraph().set(xTile, yTile, 0);
		towers.put(xTile, yTile, null);
		t.setDead(true);
	}
	
	public MainLoop getMainLoop() {
		return mainLoop;
	}
}
