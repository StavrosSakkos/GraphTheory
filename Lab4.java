import java.util.ArrayList;

class Node {
	
	private int label = -1;
	private ArrayList<Node> children;
	private Node parent = null;

	public Node(int label) {
		this.label =  label;
		children = new ArrayList<Node>();
	}
	
	public Node addChild(Node child) {
		if(!(child == null)) {
			child.setParent(this);
			children.add(child);
		}
		return child;
	}
	
	public void deleteNode() {
		if(parent != null) {
			int offset = parent.getChildren().indexOf(this);
			parent.getChildren().remove(this);
			for(Node i : getChildren()) {
				i.setParent(parent);
			}
			parent.getChildren().addAll(offset,getChildren());
		} else {
			deleteRootNode();
		}
		getChildren().clear();
	}
	
	public Node deleteRootNode() {
		Node newParent = null;
		if(parent != null) {
			return null;
		}
		if(!getChildren().isEmpty()) {
			newParent = getChildren().get(0);
			newParent.setParent(null);
			getChildren().remove(0);
			for(Node i : getChildren()) {
				i.setParent(newParent);
			}
			newParent.getChildren().addAll(getChildren());
		}
		getChildren().clear();
		return newParent;
	}
	
	public void remove(int label) {
		for(int i =0; i < children.size(); i++ ) {
			if(children.get(i).getLabel() == label) {
				children.remove(i);
				break;
			}
		}
	}
	
	public ArrayList<Node> removedChildrenNodes(int label){
		for(int i =0; i < children.size(); i++ ) {
			if(children.get(i).getLabel() == label) {
				if(!children.get(i).getChildren().isEmpty()) {
					return children.remove(i).getChildren();
				}
				children.remove(i);
			}
		}
		return new ArrayList<Node>();
	}
	
	public Node findNode(int label) {
		Node n = null;
		if(getRoot().label == label) {
			return getRoot();
		}
		if(!(parent == null)) {
			if(parent.getLabel() == label) {
				for(Node i : parent.getChildren()) {
					if(i.getLabel() == label) {
						return i;
					}
				}
			}
		}
		for(Node i : children) {
			if(i.getLabel() == label) {
				return i;
			}
			n = i.findNode(label);
			if(!(n == null)) {
				return n;
			}
		}
		return n;
	}
	
	public ArrayList<Node> getLeaves() {
		ArrayList<Node> leaves = new ArrayList<Node>();
		if(parent == null && children.size() == 1) {
			leaves.add(this);
		}
		if(children.isEmpty()) {
			leaves.add(this);
			return leaves;
		}
		for(Node i : children) {
			leaves.addAll(i.getLeaves());
		}
		return leaves;
	}
	
	public String toString() {
		return Integer.toString(label);
	}
	public int getSize() {
		int size = 1;
		if(children.isEmpty()) {
			return size;
		}
		for(Node i : children) {
			size = size + i.getSize();
		}
		return size;
	}

	public int getSmallestLabel(ArrayList<Node> arr) {
		Node n = null;
		if(!arr.isEmpty()) {
			n = arr.get(0);
		}
		for(int i = 0;i < arr.size(); i++) {
			if(arr.get(i).getLabel() < n.getLabel()) {
				n = arr.get(i);
			}
		}
		return arr.indexOf(n);
	}
	
	public Node getSmallestLabelNode(ArrayList<Node> arr) {
		return getLeaves().get(getSmallestLabel(arr));
	}
	
	public Node getRoot() {
		if(parent == null) {
			return this;
		}
		return parent.getRoot();
	}
	
	public ArrayList<Node> getChildren() {
		return children;
	}
	
	public int getLabel() {
		return label;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}

	public static void printTree(Node n, String str) {
		System.out.println(str + "(" + n.getLabel() + ")");
		n.getChildren().forEach(i ->  printTree(i,str + str));
	}
}

class Lab4 {
	static Node root = init();
	
	public static ArrayList<Integer> encoding() {
		int size = root.getSize()-2;
		ArrayList<Integer> returnArr = new ArrayList<Integer>();
		for(int i = 0; i < size;i++) {
			ArrayList<Node> leaves = root.getLeaves();
			if(root.getRoot().getLabel() == root.getSmallestLabelNode(leaves).getLabel()) {
				returnArr.add(root.getChildren().get(0).getLabel());
				root = root.deleteRootNode();
			} else {
				returnArr.add(root.getSmallestLabelNode(leaves).getParent().getLabel());
				root.getSmallestLabelNode(leaves).deleteNode();
			}
		}
		return returnArr;
	}

	public static Node decoding(ArrayList<Integer> s,ArrayList<Integer> arrOfN,int size) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		while(!s.isEmpty()) {
			for(Integer j: arrOfN) {
				int i = s.get(0);
				if(!s.contains(j)) {
					boolean check = true;
					Node newNode = new Node(j);
					newNode.addChild(new Node(i));
					check = connectedNodesLoop(nodes,newNode,check);
					if(check) { nodes.add(newNode); }
					arrOfN.remove(j);
					break;
				}
			}
			if(arrOfN.size() == 2) {
				Node aNode = new Node(arrOfN.get(0));
				aNode.addChild(new Node(arrOfN.get(1)));
				nodes.add(aNode);
				arrOfN.clear();
			}
			s.remove(0);
		}
		while(nodes.size() != 1) {
			boolean check = true;
			Node newNode = nodes.remove(0);
			for(Node k : nodes) {
				if(!(k.findNode(newNode.getLabel()) == null)) {
					check = false;
					for(int l = 0; l < newNode.getChildren().size(); l++) {
						k.findNode(newNode.getLabel()).addChild(newNode.getChildren().get(l));
					}
					break;
				}else if(!(k.findNode(newNode.getChildren().get(0).getLabel()) == null)) {
					for(int l = 0; l < newNode.getChildren().size();l++) {
						int findLabel = newNode.getChildren().get(l).getLabel();
						ArrayList<Node> removedChildren = newNode.removedChildrenNodes(findLabel);
						check = false;
						for(Node m : removedChildren) {
							k.findNode(findLabel).addChild(m);
						}
						newNode.remove(findLabel);
						k.findNode(findLabel).addChild(newNode);
					}
					break;
				}
			}
			if(check) {
				nodes.add(newNode);
			}
		}
		return nodes.get(0);
	}

	public static boolean connectedNodesLoop(ArrayList<Node> nodes,Node newNode,boolean check) {
		for(Node k : nodes) {
			if(!(k.findNode(newNode.getLabel()) == null)) {
				k.findNode(newNode.getLabel()).addChild(newNode.getChildren().get(0));
				return false;
			}else if(!(k.findNode(newNode.getChildren().get(0).getLabel()) == null)) {
				int findLabel = newNode.getChildren().get(0).getLabel();
				int connectionLabel = k.findNode(findLabel).getLabel();
				newNode.remove(connectionLabel);
				k.findNode(findLabel).addChild(newNode);
				return false;
			}
		}
		return true;
	}
	
	private static Node init() {
		Node node0 = new Node(2);
		Node node4 = node0.addChild(new Node(4));
		Node node1 = node4.addChild(new Node(1));
		Node node5 = node4.addChild(new Node(5));
		node1.addChild(new Node(3));
		node1.addChild(new Node(7));
		node5.addChild(new Node(6));
		node5.addChild(new Node(8));
		return node0;
	}
	
	private static void printTree(Node n,String str) {
		System.out.println(str + "(" + n.getLabel() + ")");
		n.getChildren().forEach(i ->  printTree(i, str + str));
	}
	
	public static void main(String [] args) {
		int size = root.getSize();
		ArrayList<Integer> arrOfN = new ArrayList<Integer>();
		for(int i = 1; i <= size;i++) { arrOfN.add(i); }
		System.out.println("After init...");
		printTree(root, "-");
		System.out.println("Encoding...");
		ArrayList<Integer> s = encoding();
		System.out.println("S: " + s);
		System.out.println("Decoding ...");
		printTree(decoding(s, arrOfN,size), "-");
	}
}
