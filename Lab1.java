import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;	

class Graph {
	
	private HashMap<String,Node> graph;
	
	public Graph() {
		graph = new HashMap<String,Node>();
	}
	
	public Graph(String... args) {
		graph = new HashMap<String,Node>();
		init(args);
	}
	
	public ArrayList<String> findCenter(){
		ArrayList<Integer> eOfNodes = new ArrayList<Integer>();
		ArrayList<String> subGraph = new ArrayList<String>();
		for(String i: graph.keySet()) {
			eOfNodes.add(graph.get(i).getE());
		}
		for(String i: graph.keySet()) {
			if(graph.get(i).getE() == Collections.min(eOfNodes)) {
				subGraph.add(i);
			}
		}
		return subGraph;
	}
	
	public ArrayList<String> findMedian(){
		ArrayList<Integer> distanceOfNodes = new ArrayList<Integer>();
		ArrayList<String> subGraph = new ArrayList<String>();
		for(String i: graph.keySet()) {
			distanceOfNodes.add(graph.get(i).getDistance());
		}
		for(String i: graph.keySet()) {
			if(graph.get(i).getDistance() == Collections.min(distanceOfNodes)) {
				subGraph.add(i);
			}
		}
		return subGraph;
	}
	
	public void calculateEccentricityAndDistance(String nameOfNode,HashMap<String,Node> graph,ArrayList<String> neighborNodes) {
		if(graph.get(nameOfNode).getConnectedNodes().isEmpty() || areNeighorNodesOfAllNodesEmpty(graph)) {
			return;
		} else {
			ArrayList<String> arrOfPaths;
			ArrayList<String> startingPoint = new ArrayList<String>();
			ArrayList<String> returnArr = new ArrayList<String>();
			for(String i : neighborNodes) {
				startingPoint.add(nameOfNode+"->"+i);
			}
			neighborNodes.remove(nameOfNode);
			String path = createPossiblePath(nameOfNode,graph);
			arrOfPaths = new ArrayList<String>(Arrays.asList(path.split("\\s+")));
			arrOfPaths.removeAll(startingPoint);
			returnArr = createEveryPossiblePath(startingPoint,arrOfPaths);
			//https://stackoverflow.com/questions/32593820/finding-largest-string-in-arraylist/32594496
			graph.get(nameOfNode)
				.setE(Collections.max(returnArr,Comparator.comparing(String::length)).split("->").length-1);
			graph.get(nameOfNode).setDistance(calculateDistance(returnArr));
		}
	}
	
	public String createPossiblePath(String nameOfNode,HashMap<String,Node> graph) {
		if(graph.isEmpty() || graph.get(nameOfNode).getConnectedNodes().isEmpty()) {
			return "";
		} else {
			String paths = "";
			ArrayList<String> neighborNodes = new ArrayList<String>(graph.get(nameOfNode).getConnectedNodes());
			for(Node i : graph.values()) {
				if(i.getConnectedNodes().contains(nameOfNode)) {
					i.getConnectedNodes().remove(nameOfNode);
				}
			}
			for(String i: neighborNodes) {
				paths = paths + nameOfNode + "->" + i +" "+ createPossiblePath(i,graph);
			}
		//	paths = removeSpace(paths);
		//	System.out.println(paths);
			return paths;
		}
	}
	
	public ArrayList<String> createEveryPossiblePath(ArrayList<String> startingPoint,ArrayList<String> arrOfPaths) {
		System.out.println(arrOfPaths);
		ArrayList<String> generatedPaths;
		ArrayList<String> returnArr = new ArrayList<String>(startingPoint);
		ArrayList<String> visited = new ArrayList<String>();
		while(true) {
			generatedPaths = new ArrayList<String>();
			if(arrOfPaths.isEmpty()) {
				break;
			}
			for(String i : returnArr) {
				for(String j: arrOfPaths) {
					if(i.substring(i.length()-1,i.length()).equals(j.substring(0,1))) {
						if(!generatedPaths.contains(i.substring(0,i.length()-1)+j)){
							generatedPaths.add(i.substring(0,i.length()-1)+j);
							visited.add(j);
						}
					}
				}
			}
			for(String i: visited) {
				arrOfPaths.remove(i);
			}
			returnArr.addAll(generatedPaths);
		}
		System.out.println(returnArr);
		return returnArr;
	}
	
	//Douleuei san mia "isQueueEmpty".
	public boolean areNeighorNodesOfAllNodesEmpty(HashMap<String,Node> graph) {
		for(Node i: graph.values()) {
			if(!i.getConnectedNodes().isEmpty()) {
				//System.out.println(i.getConnectedNodes());
				return false;
			}
		}
		return true;
	}
	
	public int calculateDistance(ArrayList<String> arr) {
		int d = 0;
		for(String i: arr) {
			d += i.split("->").length -1;
		}
		return d;
	}
	
	public void addConnections(String [] args) {
		if(!(args.length == 0 || args.length == 1)) {
			for(int i = 1;i < args.length; i++) {
				graph.get(args[0]).pushConnectedNode(args[i]);
			}
		}
	}
	
	public String removeSpace(String str) {
		return str = (str.length() > 1 && str.substring(str.length()-1,str.length()).contentEquals(" "))  
		? str.substring(0, str.length() - 1) : str;
	}
	
	public void init(String... args) {
		for(String i : args) {
			graph.put(i,new Node());
		}
	}
	
	public Node getNode(String name) {
		return graph.get(name);
	}
	
	public HashMap<String,Node> getGraph(){
		return graph;
	}
	
}

class Node {
	private ArrayList<String> connectedNodes;
	private int e;
	private int distance;
	
	public Node(){
		e = 0;
		distance = 0;
		connectedNodes = new ArrayList<String>();
	}
	
	public void pushConnectedNode(String... args) {
		for(String i : args) {
			if(!connectedNodes.contains(i)) {
				connectedNodes.add(i);
			}
		}
	}
	
	public ArrayList<String> getConnectedNodes() {
		return connectedNodes;
	}
	
	public int getE() {
		return e;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public void setE(int e){
		//System.out.println(e);
		this.e = e;
	}
}

class Lab1 {
	
	public static void initConnectedNodes(Graph newGraph) {
		newGraph.getNode("A").pushConnectedNode("D");
		newGraph.getNode("B").pushConnectedNode("H");
		newGraph.getNode("C").pushConnectedNode("D");
		newGraph.getNode("D").pushConnectedNode("A","C","E","F","G");
		newGraph.getNode("E").pushConnectedNode("D");
		newGraph.getNode("F").pushConnectedNode("D");
		newGraph.getNode("G").pushConnectedNode("D","H");
		newGraph.getNode("H").pushConnectedNode("B","G");
	}
	
	public static void main(String args[]) {
		String [] namesOfNodes = {"A","B","C","D","E","F","G","H"};
		Graph newGraph = new Graph(namesOfNodes);
		initConnectedNodes(newGraph);
		for(String i: newGraph.getGraph().keySet()) {
			newGraph.calculateEccentricityAndDistance(i, newGraph.getGraph(),newGraph.getGraph().get(i).getConnectedNodes());
			System.out.println(i+ ":"+"e = "+ newGraph.getGraph().get(i).getE()+ " || d = "+
					newGraph.getGraph().get(i).getDistance());
			initConnectedNodes(newGraph);
		}
		System.out.println("Center: "+newGraph.findCenter());
		System.out.println("Median: "+newGraph.findMedian());
	}
}