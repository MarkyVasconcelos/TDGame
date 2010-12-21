package td.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import td.cfg.GameConfig;

import jgf.imaging.ImageItem;

public class TileBackground {
	private BufferedImage img;

	public TileBackground(int cols, int rows, String imageName, int size) {
		ImageItem image = GameConfig.images.getImage("bg", imageName);
		img = new BufferedImage(cols*size,
				rows*size, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = img.createGraphics();
		for (int i = 0; i < cols; i++)
			for (int j = 0; j < rows; j++)
				image.draw(g2d, i * size, j * size);
		g2d.dispose();
	}

	public void draw(Graphics2D surface) {
		surface.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
	}
}
