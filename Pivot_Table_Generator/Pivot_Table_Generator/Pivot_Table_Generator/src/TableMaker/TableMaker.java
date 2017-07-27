package TableMaker;

public class TableMaker {

	private int colNum, rowNum;
	private String[][][] outArray = null;
	private String[] pageNames = null;
        private int[] numberFields = null;





	//constructor (which also does all the work)
	public TableMaker(Record[] data, int[] rowSel, int[] colSel, int sum, int ave, int page){

		rowNum = rowSel.length;   //number of row fields
		colNum = colSel.length;		//number of column fields

		LabelTree rowTree = null;
		LabelTree colTree = null;      //declaration of tree = null
		PivotTree mainTree = null;
		setNumberFields(data[1].getData());

		if(page < 0){             //if no page was selected  ---> only one page

			int[] treeInput = new int[colSel.length + rowSel.length];    //array to build main pivot tree

			int index = 0;

			for(int z : rowSel){
				treeInput[index] = z;                //load up array row first
				index++;
			}

			for(int z : colSel){
				treeInput[index] = z;			//columns second
				index++;
			}


			mainTree = new PivotTree(treeInput, data);     //make pivot tree

			if(rowNum > 0){								//if rows were selected
				rowTree = new LabelTree(rowSel, data);
			}

			if(colNum > 0){							// if columns were selected
				colTree = new LabelTree(colSel, data);
			}

			outArray = new String[1][][];		//only one page in return

			outArray[0] = ArrayPrinter.printToArray(mainTree, rowTree, colTree, sum, ave);   //load table into the page


		}
		else{     //if a page field was selected

			int[] treeInput = {page};    //split a tree based on the single page value

			mainTree = new PivotTree(treeInput, data);  				//make pivot table
			outArray = new String[mainTree.root.childs.length][][];  	//make output array with appropriate number of "pages"
			Record[][] pageInputs = mainTree.pageData();				//get the data for each page in a an array

			this.pageNames = mainTree.root.elements;               		 //set the name of the pages based on  the root of the tree

			treeInput = new int[colSel.length + rowSel.length];          //split tree based on "actual" parameters (vs. the pages chosen previously)
			int index = 0;

			for(int z : rowSel){
				treeInput[index] = z;
				index++;
			}
                                                  //load up the int array for splitting the tree
			for(int z : colSel){
				treeInput[index] = z;
				index++;
			}

			if(rowNum > 0){
				rowTree = new LabelTree(rowSel, data);
			}
																	//make column/row trees
			if(colNum > 0){
				colTree = new LabelTree(colSel, data);
			}


			int index1 = 0;                           // index for filling the 3D array (index refers to page)


			for(Record[] pageData : pageInputs){                 //iterate through each data "page"

				mainTree = new PivotTree(treeInput, pageData);	//make tree using data and selections

				outArray[index1] = ArrayPrinter.printToArray(mainTree, rowTree, colTree, sum, ave);   //fill array with one page

				index1++;								//increment

			}

		}

	}





	public int getColNum() {		//number of columns
		return colNum;
	}





	public String[] getPageNames() {   //array of page name (only if you choose to make pages)
		return pageNames;
	}





	public int getRowNum() {		//number of rows
		return rowNum;
	}



	public String[][][] getOutArray() {   //return table/pages
		return outArray;
	}

//method which set the numberfields (summable fields) -->  add to TableMaker class
    public void setNumberFields(String[] someRecord){
        int i = 0;
        for(String s : someRecord){
            try{
                Double.parseDouble(s);
                i++;
            }catch(Exception e){
                continue;
            }
        }
        int[] output = new int[i];
        i = -1;
        int x = 0;
        for(String s : someRecord){
            try{
                Double.parseDouble(s);
                i++;
                output[x] = i;
                x++;
            }catch(Exception e){
                i++;
                continue;
            }
        }

        numberFields = output;

    }
    //method which returns int array of summable fields  --> add to TableMakers
    public int[] getNumberFields(){
        return numberFields;
    }







}
