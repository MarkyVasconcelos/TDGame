package td.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import td.gui.view.InfoArea;
import td.pieces.template.TowerTemplate;

public class BuildGUI extends JPanel {
	private static final long serialVersionUID = 9209495638546923391L;

	private TowerTemplate selected = null;

	private InfoArea infoArea;
	private TowerLabelGroup group;

	public BuildGUI() {
		super();
		setBackground(Color.gray);
		setBorder(new TitledBorder("Build"));
		setMinimumSize(new Dimension(200, 120));
		setPreferredSize(getMinimumSize());
		placeComps();

		setFocusable(false);
	}

	public void placeComps() {
		JPanel towers = new JPanel();
		towers.setLayout(new BoxLayout(towers, BoxLayout.LINE_AXIS));

		group = new TowerLabelGroup();

		TowerLabel basic = new TowerLabel(TowerTemplate.Basic);
		TowerLabel ice = new TowerLabel(TowerTemplate.Ice);
		TowerLabel fire = new TowerLabel(TowerTemplate.Fire);
		TowerLabel dmgUp = new TowerLabel(TowerTemplate.DamageBoost);
		TowerLabel fireRate = new TowerLabel(TowerTemplate.FireRateBoost);
		TowerLabel range = new TowerLabel(TowerTemplate.RangeBoost);
		
		group.add(basic);
		group.add(ice);
		group.add(fire);
		group.add(dmgUp);
		group.add(fireRate);
		group.add(range);

		towers.add(basic);
		towers.add(ice);
		towers.add(fire);
		towers.add(dmgUp);
		towers.add(fireRate);
		towers.add(range);

		infoArea = new InfoArea(200,80);
		infoArea.setColors(Color.gray.brighter(), Color.white, Color.blue.darker());

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(towers);
		add(infoArea);

	}

	private class TowerLabelGroup {
		private List<TowerLabel> labels;

		public TowerLabelGroup() {
			labels = new ArrayList<TowerLabel>();
		}

		public void add(TowerLabel t) {
			labels.add(t);
		}

		public void setSelected(TowerLabel t) {
			selected = null;
			if (t != null) {
				t.highlight = true;
				selected = t.tower;
				TDGame.getInstance().unselectTowers();
			}
			for (TowerLabel l : labels) {
				if (l != t)
					l.highlight = false;
				l.repaint();
			}
		}
	}

	private class TowerLabel extends JLabel {
		private TowerTemplate tower;
		private boolean highlight;
		
		public TowerLabel(TowerTemplate template) {
			super(new ImageIcon(template.getImageItem().getImage()));
			tower = template;
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					BuildGUI.this.selected = tower;
					BuildGUI.this.infoArea.setText(tower.getInfo());
					group.setSelected(TowerLabel.this);
				}
			});
		}

		public void paintComponent(Graphics g) {
//			g.setColor(Color.gray);
//			g.fillRect(0, 0, getWidth(), getHeight());
			
			super.paintComponent(g);

			
			if (highlight) {
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setComposite(AlphaComposite.SrcOver.derive(0.7f));
				g2d.setColor(Color.white);
				g2d.fillRect(0, 0, getWidth(), getHeight());
				g2d.dispose();
			}
		}
	}

	public TowerTemplate getSelected() {
		return selected;
	}

	public void unselect() {
		group.setSelected(null);
	}

	public KeyListener createUnselectListener() {
		return new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE)
					unselect();
			}
		};
	}

	public boolean hasSelected() {
		return selected != null;
	}
}
