package TableMaker;

import java.util.Stack;

public class Record{


	private String[] data = null;		//array of Strings --> all the data in the line


	public Record(String[] input){
		setData(input);
	}


	public String[] getData() {
		return data;
	}


	public void setData(String[] data) {
		this.data = data;
	}


	//toString method to help display data
	public String toString(){
		String output = "";
		for(String s: this.data){
			output = output + s + "\t";
		}

		return output;
	}



	/**
	 * This method finds the first position of a given element in a sorted Record array
	 * @param r Record array
	 * @param field
	 * @param s String which is searched for
	 * @return -1 if not found, index if found
	 */
	public static int findStartIndex(Record[] r, int field, String s){


		int guess = binaryRecSearch(r, field, s);    	//find location of element using binary search


		if(guess == -1){return -1;}                 	//if element was not found --> return -1

		while(true){

			if(s.equals(r[guess].getData()[field])){
				if(guess == 0){return 0;}             	//case where record is found at position 0
				guess--;}
			else{break;}
		}

		return guess+1;      							//return first position
	}





	/**
	 *
	 * @param r Record array input
	 * @param field, field specification
	 * @param s, String element which specifies extraction
	 * @return output, record array of extracted records
	 */
	public static Record[] extractRecords(Record[] r, int field, String s){

		int index = findStartIndex(r, field, s);	//find first index of matching Record
		if(index == -1){return null;}            	//if it was not found

		int count = 0;								//initialize record count to zero
		int index2 = index;							//copy index for later use

		while(true){

			if (index >= r.length){break;}  					//Search is out of bounds  --> break
			if(!(r[index].getData()[field].equals(s))){break;} 	//mismatch is found --> break
			else{index ++; count ++;}							//increment index and count
		}


		if(count == 0){return null;}							//no matches found to extract

		Record[] output = new Record[count]; 					//initialize output array

		for(int i = 0; i < count; i++){

			output[i] = r[index2];								//load output array
			index2++;
		}

		return output;  										//return array of matching records
	}





	/**
	 *
	 * @param r Record array
	 * @param field specification for search
	 * @param s String being searched
	 * @return -1 if not found, if found --> 1st find of string
	 */
	public static int binaryRecSearch(Record[] r, int field, String s){




		Record.heapSort(r, field);           //sorting based on specified field

		int guess = r.length/2;              //middle of array
		int min = 0;						 //start of array
		int max = r.length-1;                //end of array

		while(true){


			if (min > max){return -1;}	//condition where match is not found  --> return -1

			if(s.equals(r[guess].getData()[field])){return guess;}  								//--> return first match index
			else if(s.compareTo(r[guess].getData()[field])>0){min = guess+1; guess = (min+max)/2;}  //binary search
			else if(s.compareTo(r[guess].getData()[field])<0){max = guess-1; guess = guess/2;}

		}
	}




	/**
	 * This method returns all the unique elements (of a given field) in an array of records
	 *
	 * @param field, specifies which field the elements will come from
	 * @param r, input array
	 * @return output, a String array containing all the unique elements
	 */
	public static String[] uniqueElements(int field, Record[] r){



		Stack<String> unique = new Stack<String>();  		//stack which will hold the elements


		Record[] copy = r;  								//input array is copied

		Record.heapSort(r, field); 							//array is then sorted according to field argument


		for(int i = 0; i < copy.length; i++){


			if(i == 0){unique.push(copy[i].getData()[field]);} 						//first element is always unique --> push into stack
			else if(copy[i].getData()[field].equals(copy[i-1].getData()[field])); 	//ignore if duplicate element is found
			else{unique.push(copy[i].getData()[field]);           					//if not duplicate --> push into stack

			}
		}


		String[] output = new String[unique.size()];    	//initialization of output string array

		for(int i = output.length-1; i>= 0; i--){   		//stack is loaded into array in reverse order

			output[i] = unique.pop();
		}


		return output;                              		//unique elements are returned

	}


	//This classes uses HeapSort to sort Record arrays based on field specification


	private static void workDown(Record[] theArray, int initial, int end, int field){

		int root = initial;

		while(((root * 2) + 1) <= end) //keep going condition
		{
			int child = (root * 2) + 1; //location
			//conditional check

			if((child + 1) <= end && (theArray[child].getData()[field].compareTo(theArray[child + 1].getData()[field]) < 0)) //smaller than
				child = child + 1; // point right

			if(theArray[root].getData()[field].compareTo(theArray[child].getData()[field]) < 0){     //smaller than
				Record ghostTwo = theArray[root];
				theArray[root] = theArray[child];
				theArray[child] = ghostTwo;
				root = child; //continue with child
			}else

				return;
		}
	}



	private static void heapify(Record[] theArray, int l, int field){

		int start = (l - 2) / 2; //INDEX STARTING POINT

		while(start >= 0){

			workDown(theArray, start, l - 1, field); // working order
			start = start-1;
		}


	}



	protected static void heapSort(Record[] theArray, int field){

		int length = theArray.length; //loaded size of the array

		heapify(theArray, length, field); // max heap it.

		int end = length-1;

		while(end > 0){

			Record ghost = theArray[end]; // swap

			theArray[end]= theArray[0];

			theArray[0]=ghost;

			workDown(theArray, 0, end - 1, field); // max heap

			end = end-1; // decrement heap
		}
	}



}
