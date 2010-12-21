package td.pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import jgf.imaging.ImageWorker;
import jgf.math.Vector2D;
import jgf.pathfinding.MatrixGraph;
import td.cfg.GameConfig;
import td.effect.Effect;
import td.effect.EffectModifier;
import td.gui.TDGame;
import td.util.image.ComposedImage;

public class Monster extends MovablePiece implements Cloneable {

	private double maxSpeed;
	private int maxHealth, health;
	private int gold;
	private List<Point2D> tempPath;
	private EffectModifier modifier;

	private List<Effect> effects;

	public Monster(Vector2D pos, Vector2D target, double speed, int maxHp,
			MatrixGraph graph, ComposedImage image, int gold) {
		super(pos, target, speed, graph, null);
		setImage(image);
		maxSpeed = speed;
		effects = new ArrayList<Effect>();
		setVisionAngle(50);
		setVisionRadius(50);
		setDirection(pos.clone());
		setTarget(target);
		maxHealth = maxHp;
		health = maxHp;
		makePath(target);
		this.gold = gold;
		tempPath = new ArrayList<Point2D>();
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void addEffect(Effect effect) {
		for (Effect ef : effects)
			if (ef.getId() == effect.getId())
				return;
		effects.add(effect);
	}

	private void drawLifeBar(Graphics2D g) {
		Graphics2D g2d = (Graphics2D) g.create();
		int percentGood = (health * 100) / maxHealth;
		int percentBad = -(percentGood - 100);
		g2d.setColor(Color.GREEN);
		g2d.fillRect((int) getX() - 5, (int) getY() - 5, percentGood / 5, 3);
		g2d.setColor(Color.RED);
		g2d.fillRect((int) (getX() - 5) + (percentGood / 5), (int) getY() - 5,
				percentBad / 5, 3);
		if (maxSpeed > getSpeed()) {
			g2d.setColor(Color.blue);
			g2d.drawRect((int) getX() - 5, (int) getY() - 5, 20, 3);
		}
		g2d.dispose();
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		BufferedImage img = getImage();
		img = ImageWorker.getInstance().rotateImage(img,
				getDirection().getAngle());
		float halfImgW = img.getWidth() / 2;
		float halfImgH = img.getHeight() / 2;
		int newX = (int) (getX() + ((getImage().getWidth() / 2) - halfImgW));
		int newY = (int) (getY() + ((getImage().getHeight() / 2) - halfImgH));
		g2d.drawImage(img, newX, newY, null);
		drawLifeBar(g2d);
		g2d.dispose();
	}

	@Override
	public void processAI() {
		if (!move()) {
			goldAdded = true;
			setDead(true);
			GameConfig.player.spendLife();
		}
		for (Effect effect : effects)
			if (modifier != null)
				modifier.inflict(effect, this);
			else
				effect.inflict(this);

		Iterator<Effect> it = effects.iterator();
		while (it.hasNext())
			if (it.next().hasEnd())
				it.remove();

	}

	@Override
	public void setDead(boolean dead) {
		super.setDead(dead);

		if (dead && !goldAdded) {
			goldAdded = true;
			GameConfig.player.addGold(gold);
		}
	}

	private boolean goldAdded = false;

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
		if (health <= 0)
			setDead(true);
	}

	public void remakePath() {
		makePath(getXTarget(), getYTarget());
	}

	private boolean pathDone = false;

	public void makeTempPath(MatrixGraph graph) {
		pathDone = false;
		makePath(getXTarget(), getYTarget(), tempPath, graph);
		pathDone = true;
	}

	public void changePath() {
		if (pathDone) {
			getPath().clear();
			getPath().addAll(tempPath);
		}
		pathDone = false;
	}

	@Override
	public Object clone() {
		try {
			Monster clone = (Monster) super.clone();
			clone.maxHealth = maxHealth;
			clone.health = health;
			return clone;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public EffectModifier getModifier() {
		return modifier;
	}

	public void setModifier(EffectModifier modifier) {
		this.modifier = modifier;
	}
}
