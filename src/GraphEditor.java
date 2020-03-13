import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
/*
 * plik: GraphEditor.java
 * autor: Miron Oskroba
 * data: listopad 2019
 */
public class GraphEditor extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private static final String APP_AUTHOR = "Autor: Miron Oskroba\n  Data: listopad 2019";
	private static final String APP_TITLE = "Prosty edytor grafów";
	
	private static final String APP_INSTRUCTION =
			"                  O P I S   P R O G R A M U \n\n" + 
	        "Aktywna klawisze:\n" +
			"   strza³ki ==> przesuwanie wszystkich kó³\n" +
			"   SHIFT + strza³ki ==> szybkie przesuwanie wszystkich kó³\n" +
			"	CTRL + s - zapisuje graf do pliku"+
			"   DEL   ==> kasowanie ko³a\n" +
			"   =, -   ==> powiêkszanie, pomniejszanie ko³a\n" +
			"   1,2,3...,9 ==> zmiana koloru ko³a\n\n" +
			"--MYSZ--:\n" +
			"   przeci¹ganie LPM ==> przesuwanie wszystkich kó³\n" +
			"   PPM ==> tworzenie nowego ko³a w niejscu kursora\n" +
	        "\n--KURSOR NA KOLE--:\n"
	        + "	Zaznaczone ko³o jest ¿ó³te dopóki zaznaczone, aby zaznaczyæ nale¿y w nie klikn¹æ\n" +
	        "   przeci¹ganie LPM ==> przesuwanie ko³a\n" +
			"   PPM ==>			\n"+
	        "1. zmiana koloru ko³a\n" +
	        "2. usuwanie ko³a\n"+
			"3. zmiana tekstu ko³a\n"+
	        "4. dodawanie krawêdzi\n"
			+ "\n--KURSOR NA KRAWEDZI--:\n"
			+ "PPM ==> 			\n"
			+ "1. usuwanie krawêdzi\n"
			+ "2. zmiana koloru krawêdzi\n"
			+ "3. zmiana gruboœci krawêdzi";
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menuGraph = new JMenu("Graph");
	private JMenu menuHelp = new JMenu("Help");
	private JMenuItem menuNew = new JMenuItem("New");
	private JMenuItem menuSave = new JMenuItem("Save");
	private JMenuItem menuLoad = new JMenuItem("Load");
	private JMenuItem menuShowExample = new JMenuItem("Example");
	private JMenuItem menuExit = new JMenuItem("Exit");
	private JMenuItem menuListOfNodes = new JMenuItem("List of Nodes and Edges");
	
	private JMenuItem menuAuthor = new JMenuItem("Author");
	private JMenuItem menuInstruction = new JMenuItem("Instruction");
	
	private GraphPanel graphPanel = new GraphPanel();
	
	public GraphEditor() {
		addActionListeners();
		setMenu();
		setFrame();
		showExample();
		setContentPane(graphPanel);
		setVisible(true);
	}
	
	private void addActionListeners() {
		menuNew.addActionListener(this);
		menuSave.addActionListener(this);
		menuLoad.addActionListener(this);
		menuShowExample.addActionListener(this);
		menuExit.addActionListener(this);
		menuListOfNodes.addActionListener(this);
		menuAuthor.addActionListener(this);
		menuInstruction.addActionListener(this);
	}
	
	private void setMenu() {
		setJMenuBar(menuBar);
		menuBar.add(menuGraph);
		menuBar.add(menuHelp);
		
		menuGraph.add(menuNew);
		menuGraph.addSeparator();
		menuGraph.add(menuSave);
		menuGraph.add(menuLoad);
		menuGraph.addSeparator();
		menuGraph.add(menuShowExample);
		menuGraph.add(menuListOfNodes);
		menuGraph.addSeparator();
		menuGraph.add(menuExit);
		
		menuHelp.add(menuAuthor);
		menuHelp.add(menuInstruction);		
	}
	
	private void setFrame() {
		setTitle(APP_TITLE);
		setSize(new Dimension(800,600));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
	//menuGraph	
		if(source == menuNew) {
			graphPanel.setGraph(new Graph());
		}
		
		else if(source == menuSave) {
			save();
		}else if(source == menuLoad) {
			load();
		}
		
		else if(source == menuShowExample) {
			showExample();
		}else if(source == menuListOfNodes) {
			JOptionPane.showMessageDialog(this, graphPanel.getGraph().toString());
		}

		else if(source == menuExit) {
			System.exit(0);
		}
	//menuHelp	
		else if(source == menuAuthor) {
			JOptionPane.showMessageDialog(this, APP_AUTHOR);
		}else if(source == menuInstruction) {
			JOptionPane.showMessageDialog(this, APP_INSTRUCTION);
		}
	 	graphPanel.repaint();
	}
	
	private void save() {
		JFileChooser chooser = new JFileChooser(".");
		if(chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
			graphPanel.saveGraphToFile(chooser.getSelectedFile().getName());
	}
	
	private void load(){
		JFileChooser chooser = new JFileChooser(".");
		if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			graphPanel.loadGraphFromFile(chooser.getSelectedFile().getName());
	}
	
	private void showExample() {
		Graph graph = new Graph();
		Node node1 = new Node(100, 50);
		Node node2 = new Node(150, 111);
		Node node3 = new Node(200, 222);
		Node node4 = new Node(250, 111);
		Node node5 = new Node(300, 50);
		
		Edge edge1 = new Edge(node1, node2);
		edge1.setColor(Color.BLACK);
		Edge edge2 = new Edge(node2, node3);
		edge2.setColor(Color.blue);
		
		edge1.setColor(Color.BLACK);
		node1.setColor(Color.black);
		node3.setColor(Color.red);
		node4.setColor(Color.green);
		node2.setR(30);
		node5.setR(20);
		node1.setR(15);
		graph.addNodes(node1,node2,node3,node4,node5);
		graph.addEdges(edge1,edge2);
		graphPanel.setGraph(graph);
		// or:
		graphPanel.setGraph(Graph.loadGraphFromFile(null, "tester.GRAPH"));
	}
}
