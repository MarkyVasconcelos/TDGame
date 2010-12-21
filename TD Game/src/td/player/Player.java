package td.player;

import javax.swing.JOptionPane;

public class Player {
	private int gold;
	private int lives;

	public Player() {

	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public void addGold(int x) {
		gold += x;
	}

	public void spendGold(int x) {
		gold -= x;
	}

	public boolean isOver() {
		return lives <= 0;
	}

	public void spendLife() {
		lives--;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int i) {
		System.out.println("L"+i);
		lives = i;
	}
}
