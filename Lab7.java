import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

class Graph {
	
	private HashMap<String,Node> graph;
	
	public Graph() {
		graph = new HashMap<String,Node>();
	}
	
	public Graph(String... args) {
		graph = new HashMap<String,Node>();
		init(args);
	}
	
	public Graph(ArrayList<Node> nodes) {
		graph = new HashMap<String,Node>();
		for(Node i : nodes) {
			graph.put(i.getName(),i);
		}
	}
	
	public void addConnections(String [] args) {
		if(!(args.length == 0 || args.length == 1)) {
			for(int i = 1;i < args.length; i++) {
				graph.get(args[0]).pushConnectedNodes(args[i]);
			}
		}
	}
	
	public void init(String... args) {
		for(String i : args) {
			graph.put(i,new Node(i));
		}
	}
	
	public HashMap<String,Node> getGraph(){
		return graph;
	}
	
	public Node getNode(String name) {
		return graph.get(name);
	}
	
	//Bubble Sort based on the degree
	public ArrayList<Node> sortBasedOnDegree(Graph theGraph) {
		ArrayList<Node> graph = new ArrayList<Node>(theGraph.getGraph().values());
		for(int i = 0;i < graph.size()-1;i++) {
			for(int j = 0;j < graph.size()-i-1;j++) {
				if(graph.get(j).getConnectedNodes().size() < graph.get(j+1).getConnectedNodes().size() ) {
					Collections.swap(graph, j, j+1);
				}
			}
		}
		return graph;
	}

	public void sortArr(ArrayList<Node> arr) {
		for(int i = 0;i < arr.size()-1;i++) {
			for(int j = 0;j < arr.size()-i-1;j++) {
				if(arr.get(j).getName().compareTo(arr.get(j+1).getName()) < 0) {
					Collections.swap(arr, j, j+1);
				}
			}
		}
	}
	
	public void brelaz() {
		ArrayList<Node> sortedNodes = sortBasedOnDegree(this);
		Node selectedNode = sortedNodes.get(0);
		sortedNodes.get(0).setColor(1);
		while(!areAllNodesColored(sortedNodes)) {
			sortedNodes = sortBasedOnDegree(this);
			selectedNode = graph.get(getNodeWithHighestDegree(getNextNodes(sortedNodes)));
			graph.get(selectedNode.getName()).setColor(findProperColor(selectedNode));
		}
		for(Node i : graph.values()) {
			System.out.println("Name : "+i.getName() + " | Color : "+i.getColor());
		}
	}

	public int findProperColor(Node aNode) {
		ArrayList<Integer> usedColors = new ArrayList<Integer>();
		for(String i : aNode.getConnectedNodes()) {
			if(!usedColors.contains(graph.get(i).getColor())) {
				usedColors.add(graph.get(i).getColor());
			}
		}
		for(int i = 1;i < graph.values().size();i++) {
			if(!usedColors.contains(i)) {
				return i;
			}
		}
		System.out.println("WARNING : ?? findProperColor");
		return 1;
	}
	
	//Creates a HashMap with the nodes and their color degrees.It returns the nodes with the maximum color-degree
	//The while loop will check whether the nextNodes is empty or not and it will try to decrease
	//the max value in order to get nodes with less than max color-degree.(since the rest -with max- are colored)
	public ArrayList<Node> getNextNodes(ArrayList<Node> nodes) {
		int max = 0;
		HashMap<Node,Integer> map = new HashMap<Node,Integer>();
		ArrayList<Integer> colors = new ArrayList<Integer>();
		ArrayList<Node> nextNodes = new ArrayList<Node>();
		for(Node i : nodes) {
			for(String j : i.getConnectedNodes()) {
				if(!colors.contains(graph.get(j).getColor()) &&
						!(graph.get(j).getColor() == 0)) {
					colors.add(graph.get(j).getColor());
				}
			}
			map.put(i,colors.size());
			colors.clear();
		}
		max = map.get(Collections.max(
				map.entrySet(),(entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey());
		while(nextNodes.isEmpty()) {
			for(Node i : map.keySet()) {
				if(map.get(i) == max && i.getColor() == 0) {
					nextNodes.add(i);
				}
			}
			max -= 1;
		}
		sortArr(nextNodes);
		return nextNodes;
	}
	
	public String getNodeWithHighestDegree(ArrayList<Node> nextNodes) {
		String maxDegreeNode = "";
		int max = 0;
		if(nextNodes.size() == 1) {
			return nextNodes.get(0).getName();
		}
		for(Node i : nextNodes) {
			if(graph.get(i.getName()).getConnectedNodes().size() > max) {
				maxDegreeNode = i.getName();
				max = graph.get(i.getName()).getConnectedNodes().size();
			}
		}
		return maxDegreeNode;
	}
	
	public boolean areAllNodesColored(ArrayList<Node> nodes) {
		for(Node i : nodes) {
			if(i.getColor() == 0) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isTwoChromatic() {
		boolean check = false;
		for(Node i : graph.values()) {
			if(i.getColor() > 2) {
				return false;
			}
			if(i.getColor() == 2) {
				check = true;
			}
		}
		return check;
	}
	
	public String toString() {
		String returnString = "";
		for(Node i : graph.values()) {
			returnString = returnString + i.getName() + " " + i.getConnectedNodes().toString() + "\n";
		}
		return returnString;
	}
	
}

class Node {
	
	private ArrayList<String> connectedNodes;
	private String name;
	private int color;
	
	public Node() {
		connectedNodes = new ArrayList<String>();
		color = 0;
	}
	
	public Node(String name) {
		this.name = name;
		connectedNodes = new ArrayList<String>();
		color = 0;
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
	
	public int getColor() {
		return color;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public String toString() {
		return name;
	}
}

public class Lab7 {
	private static Scanner sc;
	
	public static void firstExample(Graph firstExample) {
		firstExample.getNode("A").pushConnectedNodes("B");
		firstExample.getNode("B").pushConnectedNodes("A","C");
		firstExample.getNode("C").pushConnectedNodes("B");
		firstExample.getNode("D").pushConnectedNodes("E");
		firstExample.getNode("E").pushConnectedNodes("D");
	}
	
	public static void secondExample(Graph secondExample) {
		secondExample.getNode("A").pushConnectedNodes("B","C","D","G");
		secondExample.getNode("B").pushConnectedNodes("A","E","F");
		secondExample.getNode("C").pushConnectedNodes("A","G");
		secondExample.getNode("D").pushConnectedNodes("A","G");
		secondExample.getNode("E").pushConnectedNodes("B","F","H");
		secondExample.getNode("F").pushConnectedNodes("B","E","H");
		secondExample.getNode("G").pushConnectedNodes("A","C","D","H");
		secondExample.getNode("H").pushConnectedNodes("E","F","G");
	}
	
	public static String[] initInputGraph(Graph inputGraph,ArrayList<String> nameOfNodes) {
		String [] arr = new String[	nameOfNodes.size()];
		for(int i = 0;i < nameOfNodes.size();i++) {
			arr[i] = nameOfNodes.get(i);
		}
		return arr;
	}
	
	public static void initGraphNeighborNodes(Graph inputGraph,ArrayList<String> inputFile) {
		int offset = 0;
		for(String i : inputGraph.getGraph().keySet()) {
			for(String j : inputFile.get(offset).split("->")[1].trim().split(",")) {
				inputGraph.getNode(i).pushConnectedNodes(j.trim());
			}
			offset++;
		}
	}
	
	public static void main(String args[]) {
		String filename = "inputLab7.txt";
		String [] namesOfNodesFirstEx = {"A","B","C","D","E"};
		String [] nameOfNodesSecondEx = {"A","B","C","D","E","F","G","H"};
		Graph firstGraph = new Graph(namesOfNodesFirstEx);
		Graph secondGraph = new Graph(nameOfNodesSecondEx);
		firstExample(firstGraph);
		secondExample(secondGraph);
		System.out.println("=====================================");
		firstGraph.brelaz();
		System.out.println("Is 2-chromatic? : "+ firstGraph.isTwoChromatic());
		System.out.println("=====================================");
		System.out.println(secondGraph);
		secondGraph.brelaz();
		System.out.println("Is 2-chromatic? : "+ secondGraph.isTwoChromatic());
		System.out.println("=====================================");
		try {
			Graph inputGraph = null;
			ArrayList<String> inputFile = new ArrayList<String>();
			ArrayList<String> nameOfNodes = new ArrayList<String>();
			sc = new Scanner(new File(filename));
			while (sc.hasNextLine()) {
				inputFile.add(sc.nextLine());
				nameOfNodes.add(inputFile.get(inputFile.size()-1).split("->")[0].trim());
			}
			inputGraph = new Graph(initInputGraph(inputGraph,nameOfNodes));
			initGraphNeighborNodes(inputGraph,inputFile);
			System.out.println(inputGraph);
			System.out.println("=====================================");
			inputGraph.brelaz();
			System.out.println("Is 2-chromatic? : "+ inputGraph.isTwoChromatic());
			System.out.println("=====================================");
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			System.exit(0);
		}
	}
}