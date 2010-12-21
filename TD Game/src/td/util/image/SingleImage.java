package td.util.image;

import java.awt.image.BufferedImage;

import jgf.imaging.ImageItem;
import td.cfg.GameConfig;

public class SingleImage implements ComposedImage {
	private ImageItem image;

	public SingleImage(String singleName) {
		image = GameConfig.images.getSingleImage(singleName);
	}

	public SingleImage(ImageItem singleImage) {
		this.image = singleImage;
	}

	public BufferedImage getImage() {
		return image.getImage();
	}
}

