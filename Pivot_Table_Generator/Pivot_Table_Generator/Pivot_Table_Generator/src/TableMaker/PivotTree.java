package TableMaker;

import java.util.Arrays;

public class PivotTree {




	Node root = null;							//root of PivotTree object
	int[] fields = null;						//values which specify how tree will be split up
	Record[] dataSet = null;					//raw data which will begin at the root of the tree





	public PivotTree(int[] f, Record[] r){			//constructor
		fields = f;			//specification for how to build tree
		dataSet = r;		//raw data for tree
		this.addNodes();    //automatically add Nodes
	}




	private void addNodes(){

		if(root == null){		//if tree is empty --> create root
			root = new Node(null, this.dataSet);
		}

		addChilds(root);		//grow tree from root by adding child nodes
	}





	//method which (using recursion) adds child nodes to tree
	private void addChilds(Node n){

		if(n.depth == fields.length){return;}		//stop condition for recursion

		String[] elmnts = Record.uniqueElements(fields[n.depth], n.data);		//finding unique elements at the given node

		int x = 0;

		n.elements = elmnts;					//keeping track of unique elements for indexing

		n.childs = new Node[elmnts.length];		//creating child Node array of appropriate size

		for(String s : elmnts){					//loading Node array with child nodes

			Record[] r = Record.extractRecords(n.data, fields[n.depth], s);
			n.childs[x] = new Node(n, r);
			x++;

		}

		n.data = null;   //remove data from processed (parent) Node


		for(Node w : n.childs){				//continue calling to add children to all of the new children
			addChilds(w);
		}

	}




	//method which helps in traversing the tree based on index of unique elements
	public int binaryElmSearch(String[] S, String x){

		int guess = Arrays.binarySearch(S, x);  //calling the sort method from Arrays

		if(guess < 0){return -1;}
		else{return guess;}

	}




	/*method which searches tree for a Node which contains both String
	as elements and return Record[] if match is found*/
	public Record[] searchTree(String[] search){


		Node ghost =  this.root;

		while(true){

			String look = search[ghost.depth];

			String[] index = ghost.elements;


			int find = binaryElmSearch(index, look);


			if(find == -1){
				return null;}
			else if(ghost.depth == search.length-1){
				ghost = ghost.childs[find];
				return ghost.data;
			}
			else{
				ghost = ghost.childs[find];
			}

		}
	}


	public Record[][] pageData(){

		Record[][] output = new Record[this.root.childs.length][];

		int index = 0;

		for(Node n : this.root.childs){
			output[index] = n.data;
			index++;
		}

		return output;

	}





	//Node class used to build tree
	public class Node{


		int depth = -1;				//integer which signifies the depth of a given node
		Node parent = null;			//pointer to parent
		Record[] data = null;		//Record array of a node "Record Data"
		String[] elements = null;	//String[] to help in traversing the tree with search methods "indexing tool"
		Node[] childs = null;		//Array of pointer for children



		public Node(Node p, Record[] d){		//constructor for node

			parent = p;

			if(parent != null){	//if not root
				depth = parent.depth + 1;
			}
			else{depth = 0;}	//if root

			data = d;
		}
	}


}
