package TableMaker;

import java.text.DecimalFormat;

public class ArrayPrinter {



	//Function which returns 2D string array of the table
	public static String[][] printToArray(PivotTree pt, LabelTree row, LabelTree col, int sum, int ave){

		if(row != null && col != null){    //if there are both row and column selections

			String[][] cols = col.getLabels();
			String[][] rows = row.getLabels();
			String[][] output = new String[rows[0].length + cols.length][cols[0].length + rows.length];

			appendRowCol(output, rows, cols);   //append labels to row/columns

			appendData(output, rows, cols, pt, sum, ave);	//append data to the body of the table

			return output;                                 //return table
		}
		else if(row != null && col == null){  	//if there are only column selections

			String[][] rows = row.getLabels();
			String[][] output = new String[rows[0].length][rows.length + 1];

			appendRowsOnlyNdata(output, rows, pt, sum, ave);   //append row labels and data
			return output;										 //return table
		}
		else if(row == null && col != null){	//if there are only row selections

			String[][] cols = col.getLabels();
			String[][] output = new String[cols.length+1][cols[0].length];

			appendColsOnlyNData(output, cols, pt, sum, ave);	//append column labels and data
			return output;										 //return table
		}

		return null;   //should never get to this statement

	}



	//function which appends row/column labels to the output array
	public static void appendRowCol(String[][] arr, String[][] rows, String[][] cols){

		int rpos , cpos = 0;

		//append rows
		for(String[] x : rows){

			rpos = cols.length;

			for(String s: x){

				arr[rpos][cpos] = s;

				rpos++;
			}

			cpos++;
		}



		rpos = 0;

		//append columns
		for(String[] x : cols){
			cpos = rows.length;

			for(String s: x){

				arr[rpos][cpos] = s;

				cpos++;
			}

			rpos++;
		}


	}


	//functions which appends the data to the body of the table (array)
	public static void appendData(String[][] arr, String[][] rows, String[][] cols, PivotTree pt, int sum, int ave){


		DecimalFormat formatter = new DecimalFormat("#0.00");    //formatter to prevent 0.9999999
		String[] search = new String[cols.length + rows.length];	//array containing the element to search for in the tree (for one cell)
		int outRow = cols.length;	//row starting position
		int outCol = rows.length;	//column starting position
		int inColLim = cols[0].length;   //table limits for input columns
		int inRowLim = rows[0].length;	//table limit for input rows
		int inCol = 0;					//index for input from column labels
		int inRow = 0;					//index for input from column labels
		int outColLim = arr[0].length;	//output column limit
		String aveS, sumS;				//Strings used for formatting average and sum


		while(true){


			//load up query with rows
			for(int x=0; x<rows.length; x++){
				search[x] = rows[x][inRow];
			}

			//load up query with columns
			int z =  rows.length;

			for(int x=0; z<search.length; x++){
				search[z] = cols[x][inCol];
				z++;
			}

			inCol++;	//move to next column

			if(inCol == inColLim){		//if out of limit move down to a new row
				inCol = 0;
				inRow++;
			}

			/*if(inRow==inRowLim){
				break;
			}*/

			//search in tree using the query generated above
			Record[] result = pt.searchTree(search);


			if(result == null){   //if not found print "-" (or nothing...)
				arr[outRow][outCol] = "";
				result = new Record[0];   //preventing null pointer

			}else{    // if found

				if(sum != -1){   //if sum value was selected

					double outsum = 0.0;

					for(Record r : result){
						outsum += Double.parseDouble(r.getData()[sum]);  //compute sum
					}

					sumS = formatter.format(outsum);
					arr[outRow][outCol] =  sumS;  //put sum into table

				}else if(ave != -1){	//if average value was selected

					double outave = 0.0;

					for(Record r : result){
						outave += Double.parseDouble(r.getData()[ave]);
					}

					outave = outave/result.length;    //compute average

					if(result.length == 0){outave = 0.0;}

					aveS = formatter.format(outave);  //format
					arr[outRow][outCol] =   aveS;    //put average into table

				}else{

					arr[outRow][outCol] = ""+result.length;     //if sum AND average were not selected ---> save count into table
				}

			}

			outCol++;    //move to the next column

			if(outCol==outColLim){
				outCol = rows.length;	//if out of bounds--> move to the next row
				outRow++;
			}

			if(inRow==inRowLim){     //once you reach the limit --> input is complete
				break;
			}

		}
	}



	//Similar to the methods above (just only rows)
	public static void appendRowsOnlyNdata(String[][] arr, String[][] rows, PivotTree pt, int sum, int ave){

		DecimalFormat formatter = new DecimalFormat("#0.00");
		int inRow = 0;
		int inRowLim = rows[0].length;
		String[] search = new String[rows.length];
		String aveS, sumS;

		while(true){


			for(int x = 0; x < rows.length; x++){

				arr[inRow][x] = rows[x][inRow];
				search[x] = rows[x][inRow]; //preventing null pointer

			}


			Record[] result = pt.searchTree(search);


			if(result == null){
				arr[inRow][rows.length] = "";
				result = new Record[0];}

			if(sum != -1){

				double outsum = 0.0;

				for(Record r : result){
					outsum += Double.parseDouble(r.getData()[sum]);
				}

				sumS = formatter.format(outsum);
				arr[inRow][rows.length] =  "" + sumS;


			}else if(ave != -1 ){

				double outave = 0.0;

				for(Record r : result){
					outave += Double.parseDouble(r.getData()[ave]);
				}

				outave = outave/result.length;

				if(result.length == 0){outave = 0.0;}

				aveS = formatter.format(outave);
				arr[inRow][rows.length] =  "" + aveS;



			}
			else{
				arr[inRow][rows.length] = "" + result.length;
			}

			inRow++;

			if(inRow == inRowLim){
				break;
			}


		}



	}


	//Similar to the methods above (just only columns)
	public static void appendColsOnlyNData(String[][] arr, String[][] cols, PivotTree pt, int sum, int ave){

		DecimalFormat formatter = new DecimalFormat("#0.00");
		int inCol = 0;
		int inColLim = cols[0].length;
		String[] search = new String[cols.length];
		String sumS, aveS;

		while(true){


			for(int x = 0; x < cols.length; x++){

				arr[x][inCol] = cols[x][inCol];
				search[x] = cols[x][inCol];

			}


			Record[] result = pt.searchTree(search);


			if(result == null){
				arr[cols.length][inCol] = "";
				result = new Record[0];}   //preventing null pointer

			if(sum != -1){

				double outsum = 0.0;

				for(Record r : result){
					outsum += Double.parseDouble(r.getData()[sum]);
				}

				sumS = formatter.format(outsum);
				arr[cols.length][inCol] =  "" + sumS;


			}else if(ave != -1 ){

				double outave = 0.0;

				for(Record r : result){
					outave += Double.parseDouble(r.getData()[ave]);
				}

				outave = outave/result.length;

				if(result.length == 0){outave = 0.0;}

				aveS = formatter.format(outave);

				arr[cols.length][inCol] =  "" + aveS;



			}
			else{
				arr[cols.length][inCol] = "" + result.length;
			}

			inCol++;

			if(inCol == inColLim){
				break;
			}


		}

	}


}
