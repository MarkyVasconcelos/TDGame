package jgf.pathfinding;

import java.util.List;

public class MatrixGraph implements Graph {
	public static final int WALL = Short.MAX_VALUE;
	private int matrix[][]; // matriz que representa os custos do grafo.

	public MatrixGraph(int larg, int alt) {
		matrix = new int[larg][alt];
	}

	public MatrixGraph(int[][] matrix) {
		this.matrix = new int[matrix.length][matrix[0].length];

		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++)
				this.matrix[i][j] = matrix[i][j];
	}

	/**
	 * calcula o custo para se locomover do vertice (x1,x2) ao vertice (x2,y2) o
	 * custo inclui: <br>
	 * <ul>
	 * <li>a diferenca de altura entre os vertices
	 * <li>a distância euclidiana na diagonal
	 * </ul>
	 */
	public float getCost(int x1, int y1, int x2, int y2) {
		float cost = Math.abs(matrix[x1][y1] - matrix[x2][y2]);

		if (x1 == x2 || y1 == y2)
			cost += 1.0f; // distancia em linha reta
		else
			cost += 1.4142f; // distancia euclidiana na diagonal.
		// Tambem pode ser manhattan.
		return cost;
	}

	// verifica se existe um muro no cenario.
	public boolean isWall(int x, int y) {
		if (x < 0 || y < 0 || x > getWidth() || y > getWidth())
			return true;

		return (matrix[x][y] == WALL);
	}

	public int getWidth() {
		return matrix.length;
	}

	public int getHeight() {
		return matrix[0].length;
	}

	// imprime o grafo em formato matricial, com o path selecionado
	public void print(List<Node> list) {
		System.out.printf("\n\n ** Grafo com path **\n");
		for (int c = 0; c <= getWidth(); c++)
			System.out.printf("--");
		System.out.printf("\n");
		for (int l = 0; l < getHeight(); l++) {
			for (int c = 0; c < getWidth(); c++) {
				if (c == 0)
					System.out.printf("|");
				if (match(list, c, l))
					System.out.printf(". ");
				else if (matrix[c][l] == WALL)
					System.out.printf("# ");
				else if (matrix[c][l] > 0)
					System.out.printf("%d ", matrix[c][l]);
				else
					System.out.printf("  ");

				if (c == getWidth() - 1)
					System.out.printf("|");
			}
			System.out.printf(" %d\n", l);
		}
		for (int c = 0; c <= getWidth(); c++)
			System.out.printf("--");
		System.out.printf("\n");
		for (int c = 0; c < getWidth(); c++)
			System.out.printf(" %d", c);
		System.out.printf("\n");
	}

	private boolean match(List<Node> list, int x, int y) {
		for (Node n : list)
			if (n.getX() == x && n.getY() == y)
				return true;
		return false;
	}

	public int get(int x, int y) {
		return matrix[x][y];
	}

	public void set(int x, int y, int value) {
		matrix[x][y] = value;
	}

	public NodeHolder buildSucessors(Node node) {
		return buildSucessors(node, 8);
	}

	public NodeHolder buildSucessorsIgnoreEdges(Node node) {
		return buildSucessorsIgnoreEdges(node, 4);
	}

	private NodeHolder buildSucessorsIgnoreEdges(Node node, int neighbors) {
		NodeHolder sucessors = createHolder();

		Node parent = node.getParent();

		int px;
		int py;
		int ini = 0;
		int fim = 4;
		int i;
		int pos[][] = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

		if (parent != null && neighbors < 4) {
			// acha a posicao do no pai relativao ao no node.
			for (i = 0; i < 4; i++)
				if (pos[i][0] == (parent.getX() - node.getX())
						&& pos[i][1] == (parent.getY() - node.getY()))
					break;
			if (neighbors == 5)
				ini = i + 2;
			else if (neighbors == 3)
				ini = i + 3;
			fim = ini + neighbors;
		}

		for (i = ini; i < fim; i++) {
			px = node.getX() + pos[i][0];
			py = node.getY() + pos[i][1];

			assert (i < 14);

			if (px >= 0 && py >= 0 && px < getWidth() && py < getHeight()) {
				// verifica se nao tem uma parede na frente do caminho
				if (isWall(px, py))
					continue;

				float g = node.getG()
						+ getCost(node.getX(), node.getY(), px, py);

				// todos os sucessores apontam para o pai, para poder
				// reconstruir o caminho
				sucessors.add(new Node(px, py, node, g));
			}
		}

		return sucessors;
	}

	/**
	 * Retorna em List a lista com os N nós vizinhos de node, onde N =
	 * neighbors. <br>
	 * Se N for 8, todos os nos vizinhos sao consultados. Senao, somente os mais
	 * a frente do caminho de busca
	 */
	private NodeHolder buildSucessors(Node node, int neighbors) {
		NodeHolder sucessors = createHolder();

		Node parent = node.getParent();

		int px;
		int py;
		int ini = 0;
		int fim = 8;
		int i;
		int pos[][] = { { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, -1 },
				{ -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, 1 }, { 1, 1 }, { 1, 0 },
				{ 1, -1 }, { 0, -1 }, { -1, -1 } };

		if (parent != null && neighbors < 8) {
			// acha a posicao do no pai relativao ao no node.
			for (i = 0; i < 8; i++)
				if (pos[i][0] == (parent.getX() - node.getX())
						&& pos[i][1] == (parent.getY() - node.getY()))
					break;
			if (neighbors == 5)
				ini = i + 2;
			else if (neighbors == 3)
				ini = i + 3;
			fim = ini + neighbors;
		}

		for (i = ini; i < fim; i++) {
			px = node.getX() + pos[i][0];
			py = node.getY() + pos[i][1];

			assert (i < 14);

			if (px >= 0 && py >= 0 && px < getWidth() && py < getHeight()) {
				// verifica se nao tem uma parede na frente do caminho
				if (isWall(px, py))
					continue;

				float g = node.getG()
						+ getCost(node.getX(), node.getY(), px, py);

				// todos os sucessores apontam para o pai, para poder
				// reconstruir o caminho
				sucessors.add(new Node(px, py, node, g));
			}
		}

		return sucessors;
	}

	public boolean testLimits(Node node) {
		if (node == null)
			throw new IllegalArgumentException("Null node is invalid!");

		return (node.getX() >= 0 && node.getX() < getWidth()
				&& node.getY() >= 0 && node.getY() < getHeight());
	}

	public NodeHolder createHolder() {
		return new IndexedNodeHolder();
	}

	public Object clone() {
		return new MatrixGraph(matrix);
	}
}
