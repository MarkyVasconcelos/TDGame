package td.gui.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

public class InfoArea extends JPanel {
	private String text;
	private String fmtText = "";
	private Color bg1, bg2, txt;

	public InfoArea(int w, int h) {
		this(new Dimension(w, h));
	}

	public InfoArea(Dimension d) {
		setPreferredSize(d);
		setMaximumSize(d);
		setColors(Color.gray.darker(),Color.black,Color.green);
		setBackground(Color.gray);
	}

	public String getText() {
		return text;
	}
	
	public void setColors(Color bg1, Color bg2, Color txt){
		this.bg1 = bg1;
		this.bg2 = bg2;
		this.txt = txt;
	}

	public void setText(String text) {
		this.text = text;

		int totalW = getSize().width - 10;
		String[] texts = text.split(" ");
		FontMetrics metrics = getMetrics();
		StringBuilder fmtT = new StringBuilder();
		int w = 5;
		for (String s : texts) {
			int tW = metrics.stringWidth(" " + s);
			if (w + tW > totalW) {
				fmtT.append("//x//");
				w = 5 + tW;
				fmtT.append(s);
			} else {
				w += tW;
				fmtT.append(" " + s);
			}
		}
		fmtT.delete(0, 1);
		fmtText = fmtT.toString();
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g.create();
		RoundRectangle2D rec = new RoundRectangle2D.Double(0, 0, getBounds()
				.getWidth(), getBounds().getHeight(), 10, 10);
		RoundRectangle2D rec2 = new RoundRectangle2D.Double(5, 5, getBounds()
				.getWidth() - 10, getBounds().getHeight() - 10, 10, 10);
		GradientPaint paint = new GradientPaint(0, 5, bg1, 60, 50, bg2, true);
		g2d.setPaint(paint);
		g2d.fill(rec);
		g2d.setColor(txt);
		g2d.draw(rec2);

		String[] texts = fmtText.split("//x//");
		int h = getMetrics().getHeight();

		for (int i = 0; i < texts.length; i++)
			g2d.drawString(texts[i], 10, 20 + h * i);
	}

	private FontMetrics getMetrics() {
		return getFontMetrics(getFont());
	}
}
