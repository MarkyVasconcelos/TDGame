package jgf.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractButton;

public class ImageButton extends AbstractButton {
	private BufferedImage upImg, downImg;
	private BufferedImage enteredImg;
	private BufferedImage atual;

	public ImageButton(BufferedImage upImg, BufferedImage downImg) {
		this.upImg = upImg;
		this.downImg = downImg;
		atual = upImg;
		enteredImg = upImg;
		addMouseListener(new Listener());
	}

	public void setOnEnteredImage(BufferedImage img) {
		if (img == null)
			throw new IllegalArgumentException("Image cannot be null!");
		this.enteredImg = img;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.drawImage(atual, getX(), getY(), atual.getWidth(),
				atual.getHeight(), this);
		g2d.dispose();
	}

	private class Listener extends MouseAdapter {
		public void mouseEntered(MouseEvent evt) {
			atual = enteredImg;
			repaint();
		}

		public void mouseClicked(MouseEvent evt) {
			atual = downImg;
			repaint();
		}

		public void mouseReleased(MouseEvent evt) {
			atual = upImg;
			repaint();
		}
	}
}
