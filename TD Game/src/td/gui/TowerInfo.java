package td.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import td.gui.view.InfoArea;
import td.pieces.Tower;
import td.pieces.template.TowerTemplate;

public class TowerInfo extends JPanel {
	private TowerInfos infos;
	private InfoArea info;
	private Tower tower;

	private JButton upgrade, sell;
	
	private static final String arrow = "->";
	public TowerInfo() {
		setBackground(Color.gray);
		setBorder(new TitledBorder("Upgrade & Sell"));
		
		setFocusable(false);
		
		setLayout(new GridLayout(3,1));

		infos = new TowerInfos();
		infos.setPreferredSize(new Dimension(200, 120));

		upgrade = new JButton("Upgrade");
		sell = new JButton("Sell");

//		sell.setBorder(null);
//		upgrade.setBorder(null);

		JPanel buttons = new JPanel(new FlowLayout());
		buttons.setBackground(Color.gray);
		buttons.add(sell);
		buttons.add(upgrade);
		
		sell.setPreferredSize(upgrade.getPreferredSize());

		info = new InfoArea(200,100);

//		JPanel infoArea = new JPanel();
//		infoArea.setBorder(new TitledBorder(""));
//		infoArea.add(info);

		add(infos);
		add(buttons);
		add(info);

		upgrade.addActionListener(upgradeListener);
		sell.addActionListener(sellListener);
	}

	public void showInfo(Tower t) {
		tower = t;
		infos.repaint();
		TowerTemplate next = t.getTemplate().getNextTower();
		if (next != null)
			info.setText(next.getInfo());
	}

	private ActionListener upgradeListener = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			if (tower != null) {
				tower.upgrade();
				showInfo(tower);
			}
		}
	};

	private ActionListener sellListener = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			if (tower != null) {
				TDGame.getInstance().sellTower(tower);
				tower = null;
				infos.repaint();
			}
		}
	};

	private class TowerInfos extends JPanel {
		public TowerInfos() {
			setBackground(Color.gray);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			FontMetrics metrics = getFontMetrics(getFont());
			int h = metrics.getHeight() + 5;
			Graphics2D g2d = (Graphics2D) g.create();
			RoundRectangle2D rec = new RoundRectangle2D.Double(0, 0, 200, 130,
					50, 50);
			RoundRectangle2D rec2 = new RoundRectangle2D.Double(10, 10, 180,
					110, 50, 50);
			GradientPaint paint = new GradientPaint(0, 5, Color.gray.darker(), 60, 50, Color.black, true);
			g2d.setPaint(paint);
			g2d.fill(rec);
			g2d.setColor(Color.green);
			g2d.draw(rec2);
			g2d.drawString("Fire rate:", 25, h + 15);
			g2d.drawString("Damage:", 25, h * 2 + 15);
			g2d.drawString("Radius:", 25, h * 3 + 15);
			g2d.drawString("Value:", 25, h * 4 + 15);

			if (tower != null) {
				g2d.drawString(String.valueOf(tower.getFireRate()), 80, h + 15);
				g2d.drawString(String.valueOf(tower.getShootStrenght()), 80,
						h * 2 + 15);
				g2d.drawString(String.valueOf(tower.getVisionRadius()), 80,
						h * 3 + 15);
				g2d.drawString(String.valueOf(tower.getTemplate().getValue()),
						80, h * 4 + 15);

				TowerTemplate template = tower.getTemplate().getNextTower();
				if (template != null) {
					g2d.drawString(arrow, 110, h + 15);
					g2d.drawString(arrow, 110, h * 2 + 15);
					g2d.drawString(arrow, 110, h * 3 + 15);
					g2d.drawString(arrow, 110, h * 4 + 15);

					g2d.drawString(String.valueOf(template.getFireRate()), 120,
							h + 15);
					g2d.drawString(String.valueOf(template.getShootStrenght()),
							120, h * 2 + 15);
					g2d.drawString(String.valueOf(template.getVisionRadius()),
							120, h * 3 + 15);
					g2d.drawString(String.valueOf(template.getValue()), 120,
							h * 4 + 15);
				}
			}
			
			g2d.dispose();
		}
	}

	@SuppressWarnings("unused")
	private class Button extends JButton {
		private String text;
		private Color textColor;

		public Button(String text) {
			this.text = text;
			setPreferredSize(new Dimension(80, 20));
			textColor = Color.green;
			addMouseListener(new Listener());
		}

		public void paintComponent(Graphics g) {
			FontMetrics metrics = getFontMetrics(getFont());
			int w = metrics.stringWidth(text);
			int start = (60 - w) / 2;
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setColor(Color.gray);
			g2d.fillRect(0, 0, 80, 20);
			RoundRectangle2D rec = new RoundRectangle2D.Double(0, 0, 60, 20,
					10, 10);
			GradientPaint paint = new GradientPaint(0, 5, Color.orange.darker(), 60, 50, Color.black, true);
			g2d.setPaint(paint);
			g2d.fill(rec);
			g2d.setColor(textColor);
			g2d.drawString(text, start, 15);
			g2d.dispose();
		}

		private class Listener extends MouseAdapter {
			public void mousePressed(MouseEvent evt) {
				textColor = Color.red;
				repaint();
			}

			public void mouseReleased(MouseEvent evt) {
				textColor = Color.green;
				repaint();
			}
		}
	}
}
