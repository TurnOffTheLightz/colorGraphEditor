import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JColorChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
/*
 * plik: GraphPanel.java
 * autor: Miron Oskroba
 * data: listopad 2019
 */
enum PressState{
	EDGE("edge"),
	NODE("node"),
	NONE("none");
	
	String state = "";
	PressState(String state) {
		this.state=state;
	}
	
	public PressState find(String state) {
		for(PressState st : values())
			if(state.toLowerCase().equals(state))
				return st;
		return null;
	}
}

public class GraphPanel extends JPanel implements KeyListener,MouseMotionListener,MouseListener{
	private static final long serialVersionUID = 1L;
	private Graph graph;
	
	private int mouseX=0;
	private int mouseY=0;
	private boolean buttonMouseLeft = false;
	private boolean buttonMouseRight = false;
	private boolean mouseDragged = false;
	private int cursor = Cursor.DEFAULT_CURSOR;
	
	private Node currentNode = null;
	private Edge currentEdge = null;
	
	private Node nodeFrom = null;
	
	private PressState pressState = PressState.NONE;
	
	public GraphPanel() {
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setFocusable(true);
		requestFocus();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(graph == null) return;
		graph.draw(g);
	}
	
	public void setGraph(Graph graph) {this.graph = graph;}
	public Graph getGraph() {return graph;}

	private void moveNode(int dx, int dy, Node node) {	graph.moveNode(dx,dy,node);	}//mouse dragged on node
	private void moveAllNodes(int dx, int dy) {	graph.moveAllNodes(dx,dy);	}//mouse dragged not on node
	private void moveEdge(int dx, int dy, Edge edge) { graph.moveEdge(dx, dy, edge);}
	
	private Node findNode(int mx, int my, MouseEvent event) {	return graph.findNode(mx,my);	}
	private Edge findEdge(int mx, int my, MouseEvent event) { 
		return graph.findEdge(mx,my);	}
	
	private void setMouseCursor(MouseEvent e) {
		if(mouseDragged)
			cursor = Cursor.HAND_CURSOR;
		else
			cursor = Cursor.DEFAULT_CURSOR;
		if(findEdge(mouseX, mouseY, e) != null)
			cursor = Cursor.CROSSHAIR_CURSOR;
		if(findNode(mouseX, mouseY, e) != null) {
			cursor = Cursor.HAND_CURSOR;
		}
		setCursor(Cursor.getPredefinedCursor(cursor));
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	private void currentNodeOnPress(MouseEvent e) {
		if(currentNode!=null) {
			currentNode.setPressed(false);
			currentNode = null;
			pressState = PressState.NONE;
		}
		currentNode = findNode(mouseX, mouseY, e);
		if(currentNode!=null	&&	!currentNode.isPressed()) {
			currentNode.setPressed(true);
			pressState = PressState.NODE;
		}
	}
	private void currentEdgeOnPress(MouseEvent e) {
		if(currentEdge != null) {
		currentEdge = null;
		pressState = PressState.NONE;		
	}
		currentEdge = findEdge(mouseX, mouseY, e);
		if(currentEdge != null) {
			pressState = PressState.EDGE;
		}
	}
	
	private void connectNodes(MouseEvent e) {
		if(nodeFrom!=null) {
			Node nodeTo = findNode(mouseX, mouseY, e);
			if(nodeTo!=null) 
				graph.addEdge(new Edge(nodeFrom, nodeTo));
			nodeFrom = null;
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		currentEdgeOnPress(e);
		currentNodeOnPress(e);
		if(e.getButton()==1) {//left button press
			connectNodes(e);
			buttonMouseLeft = true;
		}else if(e.getButton()==3) {//right button rpeess
			buttonMouseRight = true;
			if(pressState == PressState.NODE)
				createPopupMenu(e,currentNode);
			else if(pressState == PressState.EDGE)
				createPopupMenu(e,currentEdge);
			else if(pressState == PressState.NONE)
				createPopupMenu(e);
		}
		setMouseCursor(e);
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton()==1) {
			buttonMouseLeft = false;
			
		}else if(e.getButton()==3) {	
			buttonMouseRight = false;
			
		}
		if(mouseDragged) {
			mouseDragged = false;
		}
		repaint();
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(buttonMouseLeft) {
			if(!mouseDragged)
				mouseDragged = true;
			
			if(pressState == PressState.NODE) {
				moveNode(e.getX() - mouseX, e.getY()-mouseY , currentNode);		
			}else if(pressState == PressState.EDGE){
				moveEdge(e.getX() - mouseX, e.getY() - mouseY, currentEdge);
			}else if(pressState == PressState.NONE){
				moveAllNodes(e.getX() - mouseX,e.getY() - mouseY);
			}
			setMouseCursor(e);
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		setMouseCursor(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int distance;
		if(e.isShiftDown())
			distance = 20;
		else
			distance = 5;
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			moveAllNodes(-distance, 0);
			break;
		case KeyEvent.VK_RIGHT:
			moveAllNodes(distance, 0);
			break;
		case KeyEvent.VK_UP:
			moveAllNodes(0, -distance);
			break;
		case KeyEvent.VK_DOWN:
			moveAllNodes(0, distance);
			break;
		case KeyEvent.VK_MINUS:
			if(currentNode != null)
				currentNode.decreaseR();
			break;
		case KeyEvent.VK_EQUALS:
			if(pressState == PressState.NODE)
				currentNode.increaseR();
			break;
		case KeyEvent.VK_DELETE:
			if(pressState == PressState.NODE) {
				graph.removeNode(currentNode);
				currentNode = null;
			}
			break;
	//colors:
		case KeyEvent.VK_1:
			if(pressState == PressState.NODE)
				currentNode.setColor(Color.MAGENTA);
			break;
		case KeyEvent.VK_2:
			if(pressState == PressState.NODE)
				currentNode.setColor(Color.CYAN);
			break;
		case KeyEvent.VK_3:
			if(pressState == PressState.NODE)
				currentNode.setColor(Color.green);
			break;
		case KeyEvent.VK_4:
			if(pressState == PressState.NODE)
				currentNode.setColor(Color.lightGray);
			break;
		case KeyEvent.VK_5:
			if(pressState == PressState.NODE)
				currentNode.setColor(Color.white);
			break;
		case KeyEvent.VK_6:
			if(pressState == PressState.NODE)
				currentNode.setColor(Color.pink);
			break;
		case KeyEvent.VK_7:
			if(pressState == PressState.NODE)
				currentNode.setColor(Color.orange);
			break;
		case KeyEvent.VK_8:
			if(pressState == PressState.NODE)
				currentNode.setColor(Color.blue);
			break;
		case KeyEvent.VK_9:
			if(pressState == PressState.NODE)
				currentNode.setColor(Color.red);
			break;
			
		default:
			break;
		}
		
		//ctrl+s save to file
		if(e.isControlDown())
			if(e.getKeyCode() == KeyEvent.VK_S)
				Graph.saveGraphToFile(this, graph, "lastProject.GRAPH");
		
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	
	private void createPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem itemCreateNode = new JMenuItem("Create new node.");
		
		//lambda expression actionListener to get new node color from JColorChooser
		itemCreateNode.addActionListener((action) -> {
			graph.addNode(new Node(e.getX(), e.getY()));
			repaint();
		});
		popupMenu.add(itemCreateNode);
		
		popupMenu.show(this, e.getX() , e.getY());	
	}
	
	private void createPopupMenu(MouseEvent e, Node node) {
		JPopupMenu popupMenu = new JPopupMenu();
	//change color	
		JMenuItem item = new JMenuItem("Change color.") {
			private static final long serialVersionUID = 1L;
			
		};
		item.addActionListener((action) -> {//lambda expression actionListener to get new node color from JColorChooser
			Color newColor = JColorChooser.showDialog(this, "Change color window.", node.getColor());
			if(newColor!=null)
				node.setColor(newColor);
			repaint();
		});
		popupMenu.add(item);
		
	//set text	
		item = new JMenuItem("Set text.");
		item.addActionListener((action) -> {
			String text = (String) JOptionPane.showInputDialog(this, "Wprowadz tekst:");
			node.setText(text);
			repaint();
		});
		popupMenu.add(item);
	//create edge	
		item = new JMenuItem("Create edge starting from this node.");
		item.addActionListener((action) -> { 
			JOptionPane.showMessageDialog(this, "Select second node.");
			this.nodeFrom = node;			
			repaint();
		});
		popupMenu.add(item);
	//remove node	
		item = new JMenuItem("Remove node.");
		popupMenu.add(item);
		item.addActionListener((action) -> {
			graph.removeNode(node);
			repaint();
		});
		popupMenu.show(this, e.getX() , e.getY());	
	}
	
	private void createPopupMenu(MouseEvent e, Edge edge) {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem item = new JMenuItem("Remove edge.");
		
		//lambda expression actionListener to get new node color from JColorChooser
		item.addActionListener((action) -> {
			graph.removeEdge(edge);
			repaint();
		});
		popupMenu.add(item);
		//set edge color
		item = new JMenuItem("Change color.");
		item.addActionListener((action) -> {
			Color newColor = JColorChooser.showDialog(this, "Change color window.", edge.getColor());
			if(newColor!=null)
				edge.setColor(newColor);
			repaint();
		});
		popupMenu.add(item);
		//set edge stroke
		item = new JMenuItem("Change width.");
		item.addActionListener((action) -> {
			try {
			float width =  Float.parseFloat((String) JOptionPane.showInputDialog(this,
			        "Text",
			        "Enter width '1-15", JOptionPane.INFORMATION_MESSAGE,
			        null,
			        null,
			        "Enter width '1-15"));
			if(width>=1)
				edge.setStroke(width);
			}catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "Width must be float.", "Error.", JOptionPane.ERROR_MESSAGE);
			}
			repaint();
		});
		popupMenu.add(item);
		
		popupMenu.show(this, e.getX() , e.getY());	
	}
	
	public void saveGraphToFile(String fileName) {
		Graph.saveGraphToFile(this, graph, fileName);
	}
	
	public void loadGraphFromFile(String fileName) {
		graph = Graph.loadGraphFromFile(this, fileName);
	}
}
