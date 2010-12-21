package td.hud;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JOptionPane;

import sun.awt.RepaintArea;
import td.cfg.GameConfig;
import td.gui.TDGame;
import td.player.Player;

public class Stats implements HighUpDisplayRender {
	private String text;

	public void processLogics() {
		Player p = GameConfig.player;
		text = "Gold: " + p.getGold() + " Lives: " + p.getLives();
		
	}

	public void renderGraphics(Graphics2D surface) {
		surface.setComposite(AlphaComposite.SrcOver.derive(0.7f));
		surface.setColor(Color.black);
		surface.fillRect(0, 0, 140, 20);
		surface.setColor(Color.yellow);
		surface.drawString(text, 10, 17);
	}

}
