package TableMaker;

public class LabelTree extends PivotTree {

	private String[][] labels = null;   //array which will hold labels
	private int row = -1;				//row array index
	private int column = -1;			//column array index


	//labelTree constructor
	public LabelTree(int[] f, Record[] r) {
		super(f, r);
		initializeArray();
		generateLabels(this.root);

	}



	//getter for labels array
	public String[][] getLabels(){
		return labels;
	}


	//function which calculates the required size of the label array
	private int treeWidth(Node n){

		int pass = 0;

		if(n.childs[0].childs == null){

			return n.childs.length;

		}
		else{

			for (Node x : n.childs){
				pass += treeWidth(x);
			}

		}

		return pass;
	}



	//function which initializes label array
	private void initializeArray(){

		labels = new String[this.fields.length][this.treeWidth(this.root)];

	}




	//function which adds labels to a given column in the array
	public void addToLabel(Node n){

		column++;

		for(int x : this.fields){
			row++;

			this.labels[row][column] = n.data[0].getData()[x];

		}

		row = -1;

	}



	//Recursive function which fills the entire label array
	public void generateLabels(Node n){


		if(n.childs == null){

			addToLabel(n);

		}
		else{

			for (Node x : n.childs){
				generateLabels(x);
			}

		}

	}




}
