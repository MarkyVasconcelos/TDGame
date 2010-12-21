package td.pieces;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import td.cfg.GameConfig;
import td.effect.Effect;
import td.effect.TowerModifier;
import td.gui.TDGame;
import td.pieces.template.TowerTemplate;

import jgf.imaging.ImageItem;
import jgf.math.Vector2D;
import jgf.pathfinding.MatrixGraph;

public class Tower extends Piece {
	private Ellipse2D circle;
	private Monster target;
	private MatrixGraph graph;
	private int shootStrenght;
	private double shootSplashRadius;
	private int fireRate;
	private long lastFire;
	private Effect effect;
	private TowerTemplate template;
	private boolean selected;
	private List<TowerModifier> buffs;

	public Tower(int x, int y, double visionRadius, int fireRate,
			int shootStrength, MatrixGraph graph, ImageItem image,
			Effect effect, double shootSplashRadius) {
		super(x, y, image);

		selected = false;
		this.effect = effect;
		setVisionRadius(visionRadius);
		setVisionAngle(360.d);
		setDirection(new Vector2D(x, y));
		this.shootStrenght = shootStrength;
		this.shootSplashRadius = shootSplashRadius;
		this.fireRate = fireRate;

		buffs = new ArrayList<TowerModifier>();

		double circleX1 = (getX() - visionRadius);
		double circleY1 = (getY() - visionRadius);

		circle = new Ellipse2D.Double(circleX1, circleY1, visionRadius * 2,
				visionRadius * 2);

		this.graph = graph;
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);

		Graphics2D g2d = (Graphics2D) g.create();
		if (selected) {
			drawVisionArea(g2d);
			g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
			g2d.setColor(Color.white);
			g2d.fillRect((int) getX(), (int) getY(), getImage().getWidth(),
					getImage().getHeight());
		}
		g2d.dispose();
	}

	private void tryFire() {
		long currentTime = System.currentTimeMillis();
		if ((currentTime - lastFire) >= fireRate) {
			Shoot shoot = null;
			if (shootSplashRadius > 0)
				shoot = new SplashShoot((int) getX(), (int) getY(), target,
						shootStrenght, 8.0d, graph, effect, shootSplashRadius);
			else
				shoot = new Shoot((int) getX(), (int) getY(), target,
						shootStrenght, 8.0d, graph, effect);
			TDGame.getInstance().getPiecesManager().add(shoot);
			lastFire = currentTime;
		}
	}

	@Override
	public void processAI() {
		if (target != null && circle.contains(target.getPosition().asPoint())
				&& (!target.isDead())) {
			tryFire();
			return;
		}

		Set<Piece> pieces = getVision();
		target = getFirstMonster(pieces);
	}

	private Monster getFirstMonster(Set<Piece> pieces) {
		for (Piece piece : pieces)
			if (piece instanceof Monster)
				return (Monster) piece;
		return null;
	}

	private void drawVisionArea(Graphics2D g2d) {
		int xCenter = (int) (getX() + (getImage().getWidth() / 2));
		int yCenter = (int) (getY() + (getImage().getHeight() / 2));
		g2d.drawOval((int) (xCenter - getVisionRadius()),
				(int) (yCenter - getVisionRadius()),
				(int) getVisionRadius() * 2, (int) getVisionRadius() * 2);
	}

	public int getShootStrenght() {
		return shootStrenght;
	}

	public int getFireRate() {
		return fireRate;
	}

	public Effect getEffect() {
		return effect;
	}

	public TowerTemplate getTemplate() {
		return template;
	}

	public void setTemplate(TowerTemplate template) {
		this.template = template;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void addModifier(TowerModifier modifier) {
		for (TowerModifier mod : buffs)
			if (mod.getId() == modifier.getId())
				return;

		buffs.add(modifier);
		modifier.modify(this);
	}

	public void setRadius(double visionRadius) {
		setVisionRadius(visionRadius);
		double circleX1 = (getX() - getVisionRadius());
		double circleY1 = (getY() - getVisionRadius());
		circle = new Ellipse2D.Double(circleX1, circleY1,
				getVisionRadius() * 2, getVisionRadius() * 2);
	}

	public void upgrade() {
		TowerTemplate next = getTemplate().getNextTower();
		if (next == null || GameConfig.player.getGold() < next.getValue())
			return;
		effect = next.getEffect();
		fireRate = next.getFireRate();
		shootStrenght = next.getShootStrenght();
		setRadius(next.getVisionRadius());

		setTemplate(next);
		GameConfig.player.spendGold(next.getValue());

		for (TowerModifier t : buffs)
			t.modify(this);
	}

	public void setShootStrenght(int shootStrenght) {
		this.shootStrenght = shootStrenght;
	}

	public void setFireRate(int fireRate) {
		this.fireRate = fireRate;
	}
}
