package td.pieces;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import jgf.imaging.ImageItem;
import jgf.imaging.ImageItemList;
import jgf.math.Vector2D;
import td.cfg.GameConfig;
import td.gui.TDGame;
import td.util.image.AnimationImage;
import td.util.image.ComposedImage;
import td.util.image.SingleImage;

public abstract class Piece implements Cloneable {
	private Vector2D position;
	private Vector2D direction;

	private double visionAngle;
	private double visionRadius;

	private ComposedImage drawer;

	private boolean dead;

	public Piece(int x, int y, String imageFile) {
		position = new Vector2D(x, y);
		drawer = new SingleImage(GameConfig.images.getSingleImage(imageFile));
	}

	public Piece(int x, int y, ImageItem image) {
		position = new Vector2D(x, y);
		this.drawer = new SingleImage(image);
	}

	public Piece(Vector2D pos, ImageItem image) {
		position = pos;
		this.drawer = new SingleImage(image);
	}

	public Piece(Vector2D pos, ImageItemList<ImageItem> image) {
		position = pos;
		this.drawer = new AnimationImage(image);
	}

	public Piece(int x, int y) {
		position = new Vector2D(x, y);
	}

	public Vector2D getPosition() {
		return position;
	}

	public void setPosition(double x, double y) {
		this.position = new Vector2D(x, y);
	}

	public double getX() {
		return position.getX();
	}

	public double getY() {
		return position.getY();
	}

	public double getWidth() {
		return getBounds().getWidth();
	}

	public double getHeight() {
		return getBounds().getHeight();
	}

	public Rectangle2D getBounds() {
		return new Rectangle2D.Double(getX(), getY(), 25, 25);
	}

	public double getVisionAngle() {
		return visionAngle;
	}

	public void setVisionAngle(double visionAngle) {
		this.visionAngle = visionAngle;
	}

	public double getVisionRadius() {
		return visionRadius;
	}

	public void setVisionRadius(double visionRadius) {
		this.visionRadius = visionRadius;
	}

	public Vector2D getDirection() {
		return direction;
	}

	public void setDirection(Vector2D direction) {
		this.direction = direction.normalize();
	}

	public abstract void processAI();

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.drawImage(drawer.getImage(), (int) getX(), (int) getY(), null);
		g2d.dispose();
	}

	public Set<Piece> getVision() {
		double circleX1 = (getX() - visionRadius);
		double circleY1 = (getY() - visionRadius);

		Ellipse2D circle = new Ellipse2D.Double(circleX1, circleY1,
				visionRadius * 2, visionRadius * 2);

		Set<Piece> piecesSet = new HashSet<Piece>();

		for (Piece piece : TDGame.getInstance().getPiecesManager().getPieces()) {
			if (piece == this)
				continue;
			if (!(circle.contains(piece.getPosition().asPoint())))
				continue;

			Vector2D temp = getPosition().subtract(piece.getPosition())
					.normalize();
			double targetAngle = temp.angleBetween(getDirection());

			if (targetAngle > this.visionAngle)
				continue;

			piecesSet.add(piece);
		}

		return piecesSet;
	}

	@Override
	public int hashCode() {
		return (int) (getX() * GameConfig.MAP_WIDTH + getY());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (obj.getClass() != this.getClass())
			return false;

		return this == obj;
	}

	@Override
	public Object clone() {
		try {
			Piece clone = (Piece) super.clone();
			clone.position = position.clone();
			clone.direction = direction.clone();
			clone.visionAngle = visionAngle;
			clone.visionRadius = visionRadius;
			clone.drawer = drawer;
			clone.dead = dead;
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public BufferedImage getImage() {
		return drawer.getImage();
	}

	public void setImage(ComposedImage img) {
		drawer = img;
	}
}
