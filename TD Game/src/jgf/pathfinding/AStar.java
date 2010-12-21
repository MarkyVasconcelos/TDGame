/**
 * <p>
 * Project: Java Game Framework<br>
 * Filename: AStar.java<br>
 * Created on: 09/08/2007<br>
 * <p>
 * @author Mendonça, Vinícius Godoy de
 * @author Moribe, Marcos
 */
package jgf.pathfinding;

import java.util.ArrayList;
import java.util.List;

public class AStar implements PathFinder {
	private NodeHolder open;
	private NodeHolder closed;

	private Graph graph;

	public AStar(Graph graph) {
		this.graph = graph;

		// lista de nos em aberto
		open = graph.createHolder();

		// lista de nos processados
		closed = graph.createHolder();
	}

	private List<Node> search(Node startNode, Node targetNode,
			boolean ignoreEdges) {
		// testa os limites da busca em relacao ao tamanho do grafo
		if (!graph.testLimits(startNode))
			throw new IndexOutOfBoundsException(
					"Start node out of the graph bounds!");

		if (targetNode != null && !graph.testLimits(targetNode))
			throw new IndexOutOfBoundsException(
					"Target node out of the graph bounds!");

		open.clear();
		closed.clear();

		startNode.estimate(targetNode);
		open.add(startNode);
		while (!open.isEmpty()) {
			// encontra e remove o MELHOR_NO de ABERTOS
			Node node = open.removeMinF();

			// coloca MELHOR_NO de ABERTOS em FECHADOS
			closed.add(node);

			// testa se MELHOR_NO ja nao eh o destino
			if (node.equals(targetNode))
				return buildPath(node);

			// Construct the list of nodes
			NodeHolder sucessors = null;
			if (!ignoreEdges)
				sucessors = graph.buildSucessors(node);
			else
				sucessors = graph.buildSucessorsIgnoreEdges(node);
			for (Node tmp : sucessors) {
				boolean inOpen = false;
				boolean inClosed = false;
				Node nodeVisited = null;

				if (open.contains(tmp)) {
					inOpen = true;
					nodeVisited = open.find(tmp);
				}

				if (closed.contains(tmp)) {
					inClosed = true;
					nodeVisited = closed.find(tmp);
				}

				// se o no nao esta em [ABERTO, FECHADO] ou custoG >
				// novoVertice, o novo caminho
				// encontrado eh melhor que o antigo e deve substitui-lo
				if (nodeVisited == null || nodeVisited.getG() > tmp.getG()) {
					if (inOpen)
						open.remove(nodeVisited);

					if (inClosed)
						closed.remove(nodeVisited);

					// Calculate estimated cost to goal
					tmp.estimate(targetNode);

					open.add(tmp);
				}
			}
			sucessors.clear();
		}

		return new ArrayList<Node>();
	}

	public List<Node> search(int startx, int starty) {
		Node startNode = new Node(startx, starty);
		Node targetNode = null;
		return search(startNode, targetNode, false);
	}

	public List<Node> search(int startx, int starty, int targetx, int targety,
			boolean ignoreEdges) {
		Node startNode = new Node(startx, starty);
		Node targetNode = new Node(targetx, targety);
		return search(startNode, targetNode, ignoreEdges);
	}

	private List<Node> buildPath(Node node) {
		List<Node> path = new ArrayList<Node>();

		while (node != null) {
			path.add(0, node);
			node = node.getParent();
		}

		return path;
	}

	public List<Node> search(int startx, int starty, int targetx, int targety) {
		return search(startx, starty, targetx, targety, false);
	}

}
