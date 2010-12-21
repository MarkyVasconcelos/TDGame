package td.cfg;

import jgf.imaging.ImagePallete;
import td.player.Player;

public class GameConfig {
	public final static int TILE_WIDTH = 40;
	public final static int TILE_HEIGHT = 30;
	public final static int TILE_SIZE = 20;
	public final static int MAP_WIDTH = TILE_WIDTH * TILE_SIZE;
	public final static int MAP_HEIGHT = TILE_HEIGHT * TILE_SIZE;

	public static final Player player = new Player();
	public static ImagePallete images;

	static {
		images = new ImagePallete("/resource/images.xml");
		player.setGold(200);//XXX:200
		player.setLives(20);
	}

	public static int pixelToTile(double pixel) {
		return (int) (pixel / TILE_SIZE);
	}

	public static int tileToPixel(int tile) {
		return tile * TILE_SIZE;
	}
	
}
