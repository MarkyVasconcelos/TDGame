package td.pieces;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import jgf.imaging.ImageWorker;
import jgf.math.Vector2D;
import jgf.pathfinding.MatrixGraph;
import td.effect.Effect;

public class Shoot extends MovablePiece implements Cloneable {
	private Monster target;
	private int str;
	private Effect effect;

	public Shoot(int x, int y, Monster target, int strength, double speed,
			MatrixGraph graph, Effect effect) {
		super(x, y, 0, 0, speed, graph, "shoot");
		this.effect = effect;
		this.target = target;
		str = strength;
		Vector2D distance = getPosition().subtract(target.getPosition());

		Vector2D futurePos = target.getDirection().multiply(target.getSpeed())
				.multiplyMe(distance.getMagnitude() / speed * 2).addMe(
						target.getPosition());

		setDirection(futurePos.subtract(getPosition()));
	}

	protected void hitTarget() {
		target.setHealth(target.getHealth() - str);
		target.addEffect((Effect) effect.clone());
	}

	@Override
	public boolean move() {
		Vector2D dist = getPosition().subtract(target.getPosition());

		Vector2D futurePos = target.getDirection().multiply(target.getSpeed())
				.multiplyMe(dist.getMagnitude() / getSpeed() * 2).addMe(
						target.getPosition());

		setDirection(futurePos.subtract(getPosition()));

		getPosition().addMe(getDirection().multiply(getSpeed()));

		return dist.getMagnitude() > 10;
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		BufferedImage img = getImage();
		img = ImageWorker.getInstance().rotateImage(img,
				getDirection().getAngle());
		int newX = (int) (getX() + (getImage().getWidth() / 2) - img.getWidth() / 2);
		int newY = (int) (getY() + (getImage().getHeight() / 2) - img
				.getHeight() / 2);
		g2d.drawImage(img, newX, newY, null);
		g2d.dispose();
	}

	@Override
	public void processAI() {
		if (!move() || target.isDead()) {
			hitTarget();
			setDead(true);
		}
	}

	@Override
	public Object clone() {
		try {
			Shoot clone = (Shoot) super.clone();
			clone.str = str;
			clone.target = target;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected int getStr() {
		return str;
	}

	protected Effect getEffect() {
		return effect;
	}

}
