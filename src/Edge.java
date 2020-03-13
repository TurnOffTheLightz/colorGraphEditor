import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;
/*
 * plik: Edge.java
 * autor: Miron Oskroba
 * data: listopad 2019
 */
public class Edge implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Node node1;
	private Node node2;
	private Color color;
	
	private double A;
	private final double B=-1;
	private double C;
	
	private float stroke = 2.0f;
	
	public Edge(Node n1, Node n2){
		this.node1 = n1;
		this.node2 = n2;
		n1.addEdge(this);
		n2.addEdge(this);
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(color);
		//ustawia gruboœæ krawedzi
		g2d.setStroke(new BasicStroke(stroke));
		g2d.drawLine(node1.getX(), node1.getY(), node2.getX(), node2.getY());
		//i cofa do wartoœci podstawowej 1.0f
		//aby unikn¹æ pogrubienia w miejscach w których tego nie chcemy
		g2d.setStroke(new BasicStroke(1.0f));
		//podobnie z kolorem
		g.setColor(Color.black);
	}
	
	public void move(int dx, int dy) {//TODO::
		node1.setX(node1.getX()+dx);
		node1.setY(node1.getY()+dy);
		node2.setX(node2.getX()+dx);
		node2.setY(node2.getY()+dy);
	}
	
	public boolean isMouseOver(int mx, int my) {
		int x1 = node1.getX();
		int y1 = node1.getY();
		int x2 = node2.getX();
		int y2 = node2.getY();
		updateLinearParameters(x1, y1 ,x2, y2);

		if((mx>x1 && my>y1 && mx<x2 && my<y2) || (mx>x2 && my>y2 && mx<x1 && my<y1)
				|| 
		   (mx>x1 && my<y1 && mx<x2 && my>y2) || (mx>x2 && my<y2 && mx<x1 && my>y1)) {
			double range=7;
			return (double)Math.abs(A*mx+B*my+C)/Math.sqrt(A*A+B*B) <= range;
		}
		return false;
	}
	
	public void setColor(Color c) {this.color = c;}
	public Color getColor() {return color;}
	public void setStroke(float stroke) {this.stroke = stroke;}
	
	
	private void updateLinearParameters(int x1, int y1, int x2, int y2) {
		//Ax + By + C = 0
		A = (double)(y1-y2)/(x1-x2);
		C = (double)(y1-A*x1);
	}
	
	@Override
	public String toString() {
		return "(node1,node2) = ("+ node1.toString() +","+node2.toString()+")";
	}
}
