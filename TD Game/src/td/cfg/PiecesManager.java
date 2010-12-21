package td.cfg;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import jgf.pathfinding.MatrixGraph;
import td.pieces.Monster;
import td.pieces.Piece;

public class PiecesManager {
	private List<Piece> pieces;

	public PiecesManager() {
		pieces = new ArrayList<Piece>();
	}

	public List<Piece> getPieces() {
		return new ArrayList<Piece>(Collections.unmodifiableList(pieces));
	}

	public void add(Piece piece) {
		pieces.add(piece);
	}

	public void remove(Piece piece) {
		pieces.remove(piece);
	}

	public void makeTempPaths(MatrixGraph graph) {
		for (Piece piece : new ArrayList<Piece>(pieces))
			if (piece instanceof Monster)
				((Monster) piece).makeTempPath(graph);
	}

	public void changePaths() {
		for (Piece piece : new ArrayList<Piece>(pieces))
			if (piece instanceof Monster)
				((Monster) piece).changePath();
	}

	public void remakePaths(MatrixGraph graph) {
		for (Piece piece : new ArrayList<Piece>(pieces))
			if (piece instanceof Monster)
				((Monster) piece).remakePath();
	}

	public void processPiecesAI() {
		for (Piece piece : new ArrayList<Piece>(pieces)){
			piece.processAI();
		}

		Iterator<Piece> it = pieces.iterator();
		while (it.hasNext()) {
			Piece p = it.next();
			if (p.isDead())
				it.remove();
		}
	}

	public void draw(Graphics2D g2d) {
		for (Piece piece : new ArrayList<Piece>(pieces))
			piece.draw(g2d);
	}
}
