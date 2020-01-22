import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class Graph {
	private ArrayList<Connection> graph;
	private HashMap<String,Node> nodes;
	
	public Graph() {
		graph = new ArrayList<Connection>();
		nodes = new HashMap<String,Node>();
	}
	
	public void setNodes(ArrayList<String> connections) {
		double weight;
		Node a;
		Node b;
		Connection con;
		for(String i: connections) {
			a = new Node(i.split(",")[0]);
			b = new Node(i.split(",")[1]);
			weight = Double.parseDouble(i.split(",")[2]);
			con = new Connection(a,b,weight);
			if(!nodes.containsKey(a.getName())) {
				nodes.put(a.getName(),a);
			} else {
				nodes.get(a.getName()).pushConnectedNodes(b.getName());
			}
			pushConnection(con);
		}
	}

	public void TSP() {
		for(String i : nodes.keySet()) {
			TSP(i);
		}
	}
	
	public void TSP(String startingPoint) {
		String currentPoint = startingPoint;
		int i = 0;
		ArrayList<String> visited = new ArrayList<String>();
		if(graph.isEmpty() || nodes.isEmpty()) {
			return;
		}
		while(i < nodes.size()) {
			//System.out.println("CurrentPoint : "+currentPoint);
			visited.add(currentPoint);
			String nextNode = getWeightFromNeighborNodes(currentPoint,visited);
			//System.out.println("Next Node: "+nextNode);
			currentPoint = nextNode;
			if(nextNode.contentEquals("null") && visited.size() < nodes.size()) {
				System.out.println("Err: Couldnt find next node to visit");
				System.out.println("Visited: "+visited);
				return;
			}
			i++;
		}
		visited.add(startingPoint);
		printTSP(startingPoint,visited);
	}
	
	public String getWeightFromNeighborNodes(String currentPoint,ArrayList<String> visited) {
		double minWeight = -1;
		String nextNode = "null";
		ArrayList<String> connectedNodes = nodes.get(currentPoint).getConnectedNodes();
		for(String i : connectedNodes) {
			for(Connection j : graph) {
				if(currentPoint.contentEquals(j.getA().getName()) 
						&& i.contentEquals(j.getB().getName()) && !visited.contains(j.getB().getName())) {
					if(minWeight == -1) {
						minWeight = j.getWeight();
						nextNode = j.getB().getName();
						//System.out.println("MinWeight 1 -> "+minWeight);
					} else {
						if(j.getWeight() < minWeight) {
							minWeight = j.getWeight();
							nextNode = j.getB().getName();
							//System.out.println("MinWeight 2 -> "+minWeight);
							break;
						}
					}
				}
			}
		}
		return nextNode;
	}
	
	public int getWeightOfPath(ArrayList<String> arr) {
		int weight = 0;
		ArrayList<String> combinations = new ArrayList<String>();
		for(int i=1; i < arr.size();i++) {
			combinations.add(arr.get(i-1)+","+arr.get(i));
		}
		combinations.add(arr.get(arr.size()-1)+","+arr.get(0));
		for(String i : combinations) {
			for(Connection j : graph) {
				if(j.getA().getName().contentEquals(i.split(",")[0]) &&
						j.getB().getName().contentEquals(i.split(",")[1])) {
					weight += j.getWeight();
					break;
				}
			}
		}
		return weight;
	}
	
	public boolean checkIfNodeExists(String name) {
		for(Node i: nodes.values()) {
			if(i.getName().contentEquals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public void pushConnection(Connection con) {
		/*for(Connection i: graph) {
			if((con.getA().hasSameName(i.getA()) && con.getB().hasSameName(i.getB()))
					|| (con.getA().hasSameName(i.getB()) && con.getB().hasSameName(i.getA()))) {
				return;
			}
		}*/
		graph.add(con);
	}
	
	public void printGraph() {
		for(Connection i: graph) {
			System.out.println("Connection : "+ i.getA().getName() + 
					"-"+i.getB().getName()+ "->"+i.getWeight());
		}
		for(Node i : nodes.values()) {
			System.out.println("Node : "+ i.getName() + " -> "+ i.getConnectedNodes());
		}
	}
	
	public void printTSP(String currentPoint,ArrayList<String> visited) {
		System.out.println("===================================");
		System.out.println("Starting Point: "+currentPoint);
		System.out.println("Found path : "+ visited);
		System.out.println("Weight : "+ getWeightOfPath(visited));
		System.out.println("===================================");
	}
}

class Connection {
	private Node a;
	private Node b;
	private double weight;
	
	public Connection(Node a, Node b,double weight) {
		this.a = a;
		this.b = b;
		this.weight = weight;
		a.pushConnectedNodes(b.getName());
		//b.pushConnectedNodes(a.getName());
	}
	
	public double getWeight() {
		return weight;
	}
	
	public Node getA() {
		return a;
	}
	
	public Node getB() {
		return b;
	}
}

class Node {
	
	private ArrayList<String> connectedNodes;
	private String name;
	
	public Node(String name){
		this.name = name;
		connectedNodes = new ArrayList<String>();
	}
	
	public boolean hasSameName(Node other) {
		return other.name.contentEquals(this.name);
	}
	
	public void pushConnectedNodes(String... args) {
		for(String i : args) {
			if(!connectedNodes.contains(i)) {
				connectedNodes.add(i);
			}
		}
	}
	
	public ArrayList<String> getConnectedNodes() {
		return connectedNodes;
	}

	public String getName() {
		return name;
	}
}

public class Lab3 {

	private static Scanner sc;

	public static void main(String args[]) {
		String filename = "inputLab3.txt";
		ArrayList<String> combinations = new ArrayList<String>();
		Graph newGraph = new Graph();
		try {
			sc = new Scanner(new File(filename));
			while (sc.hasNextLine()) {
				combinations.add(sc.nextLine().trim());
			}
			newGraph.setNodes(combinations);
			//newGraph.printGraph();
			newGraph.TSP();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			System.exit(0);
		}
	}
}