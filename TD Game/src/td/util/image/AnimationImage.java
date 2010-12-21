package td.util.image;

import java.awt.image.BufferedImage;

import jgf.imaging.Animation;
import jgf.imaging.ImageItem;
import jgf.imaging.ImageItemList;
import td.cfg.GameConfig;

public class AnimationImage implements ComposedImage {
	private Animation animation;

	public AnimationImage(String listName) {
		animation = new Animation(60, GameConfig.images.getList(listName),
				Double.MAX_VALUE);
		animation.setRunning(true);
	}

	public AnimationImage(ImageItemList<ImageItem> list) {
		animation = new Animation(20, list, 1.5);
		animation.setRunning(true);
	}

	public BufferedImage getImage() {
		animation.update();
		return animation.getImage();
	}

}