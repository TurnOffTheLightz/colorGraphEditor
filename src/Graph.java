import java.awt.Graphics;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
/*
 * plik: Graph.java
 * autor: Miron Oskroba
 * data: listopad 2019
 */
public class Graph implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private List<Node> nodes;
	private List<Edge> edges;
	
	public Graph() {
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
	}
	
	public void addNode(Node node) {
		nodes.add(node);
	}
	
	public void addEdge(Edge e) {
		edges.add(e);
	}
	
	public void addNodes(Node ... ns) {
		for(Node n : ns) {
			nodes.add(n);
		}
	}
	
	public void addEdges(Edge ... es) {
		for(Edge n : es) {
			edges.add(n);
		}
	}
	
	public void removeNode(Node node) {
		nodes.remove(node);
		edges.removeAll(node.getEdges());
	}
	
	public void removeEdge(Edge e) {edges.remove(e);}
	
	public void draw(Graphics g) {
		for(Edge edge : edges)
			edge.draw(g);
		for(Node node : nodes)
			node.draw(g);
	}
	
	public void moveNode(int dx, int dy, Node node) {
		node.setX(node.getX()+dx);
		node.setY(node.getY()+dy);
	}
	
	public void moveEdge(int dx, int dy, Edge edge) {
		edge.move(dx, dy);
	}
	
	public void moveAllNodes(int dx, int dy) {
		for(Node node : nodes) {
			moveNode(dx, dy, node);
		}
	}
	
	public Node findNode(int mx, int my) {
		for(Node node : nodes)
			if(node.isMouseOver(mx, my))
				return node;
		return null;
	}
	
	public Edge findEdge(int mx, int my) {
		for(Edge edge : edges)
			if(edge.isMouseOver(mx, my))
				return edge;
		return null;
	}
	
	public List<Node> getNodes(){return nodes;}
	public List<Edge> getEdges(){return edges;}
	
	public void setNodes(List<Node> ns) {nodes.addAll(ns);}
	public void setEdges(List<Edge> es) {edges.addAll(es);}
	
	@Override
	public String toString() {
		String str = "";
		int counter=0;
		for(Node node : nodes)
			str+= "Node["+counter++ +"]=("+node.toString()+"\n";
		counter=0;
		if(!edges.isEmpty())
			for(Edge edge : edges)
				str+="\nEdge["+counter++ +"]=("+edge.toString()+"\n";
		return str;
	}
	
	public static void saveGraphToFile(GraphPanel parent, Graph graph, String fileName) {
		if(!fileName.contains(".GRAPH")) fileName+=".GRAPH";
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))){
			out.writeObject(graph);
			out.close();
			JOptionPane.showMessageDialog(parent, "Zapisano graf do pliku: " + fileName);
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}
	}

	public static Graph loadGraphFromFile(GraphPanel parent,String fileName) {		
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))){
			try {
				JOptionPane.showMessageDialog(parent, "Graph loaded from file: " + fileName);
				return (Graph)in.readObject();
			}finally {
				if(in!=null)
					in.close();
			}
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		} catch (ClassNotFoundException e) {

		}
		return null;
	}
}

