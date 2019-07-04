package edu.wm.cs.cs301.danielquiroga.generation;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 *@author Daniel Quiroga
 *
 */

public class MazeBuilderEller extends MazeBuilder implements Runnable 
{
	// constructor for the program
	public MazeBuilderEller() 
	{
		super();
		System.out.println("MazeBuilderEller uses Eller's algorithm to generate maze.");
	}
	
	public MazeBuilderEller(boolean det) 
	{
		super(det);
		System.out.println("MazeBuilderEller uses Eller's algorithm to generate maze.");
	}
	
	
	
	// variables that are used throughout the program and need to be initialized once the file is called
	
	protected int[][] set;
	public Random rand = new Random();// creating a variable that will be used to implement the functions of the Random class
	public ArrayList<Integer> setList = new ArrayList<Integer>(); // will keep track of the sets visited
	public ArrayList<Integer> colList = new ArrayList<Integer>(); // will keep track of the col in which new walls need to be chosen
	
	
	
	
	
	/**
	 *  this is going to create an Eller style of maze
	 *  The method will override the default maze given by the MazeBuilder.java
	 */
	@Override
	protected void generatePathways() 
	{
		// create a 2-D array to act as the tracker of the sets for the algorithm
		set = new int[height][width];
		populateGen(width, height);
		
		
		// begin Eller by creating the first row
		createInitRow(width);
		
		for (int row = 0; row < height - 1; row++) 
		{
			combineSetsRandomly(row); // this is the second step to Eller, which chooses random sets to combine
			createDownPathRand(row); // create the connections to the row below
			fillOutRow(row + 1);// create the population of cells that are still 0 with new sets
		}
		
		
		clearFinalRow(height - 1);// make the last row empty without any walls

	}
	
	
	/**
	 * initially I  populate the entire array with 0 in each cell
	 * this will allow me to be able to tell which cells have not been visited yet by the algorithm
	 * eventually I will use these 0's in the algorithm for the vertical movement down the rows. 
	 * @param width(x)
	 * @param height(y)
	 */
	protected void populateGen(int width, int height) 
	{
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				set[row][col] = 0; // setting every cell visited equal to 0 on the array
			}
		}
	}
	
	
	/**
	 * go through first row and populate with numbers 1 through width-1
	 * @param gives how long the row we are working with is
	 */
	protected void createInitRow(int width) 
	{
		int count = 1;
		int index = 0;
		while (index < width) 
		//for (int index = 0; index < width; index++)
		{
			set[0][index] = count;// the row is numbered off 1 to width
			count++;
			index++;
		}
	}
	
	
	/** 
	 * makes random connections inside the row and then combine to a single set
	 * @param is the row we are working with currently
	 */
	protected void combineSetsRandomly(int row) 
	{

		for(int col = 0; col < width - 1; col++) // go through each cell in the row
		{ 
			Wall wall = new Wall(col, row, CardinalDirection.East);// creating a reference to the wall that might need to be put up in the eller algorithm
			
			if(cells.canGo(wall) && // this checks that the cell to the right is not in the same set and then randomly deletes a wall to the right
			   set[row][col] != set[row][col+1] && 
			   rand.nextBoolean() == true) 
			{ 
					cells.deleteWall(wall); // wall has been deleted
					set[row][col+1] = set[row][col]; // the cell to the right has been added to the set
					// might need to write separate function for last order
			}
		}
	}
	
	

	
	
	
	/**
	 * this function will go through and choose downward passages to be built, at least one downward passage per set
	 * @param row gives the position where the current row is located
	 */
	protected void createDownPathRand(int row) 
	{
		// use array list to keep track of how many cells are in each set -- reset the array list after every set
		// addToHashMap(row);
		int temp = set[row][0];
		for (int col = 0; col < width; col++) {
			temp = set[row][col];
			colList = addToArray(col, row, temp); // this adds the next number to the list increasing the size and returns true if the next cell is in the same set
			
			if (colList.size() == 1) {
				Wall wall = new Wall(colList.get(0), row, CardinalDirection.South); // creates the wall that will be deleted downward (South)
				cells.deleteWall(wall);
				set[row+1][col] = set[row][col]; // resets the cell to be a new set
				// resets the lists so that the next iteration
				colList.clear();
			}
			else if(colList.size() >= 2) {
				randomDownSet(row);
				colList.clear();
			}
			colList.clear();
		}setList.clear();
	}
	
	
	/**
	 * chooses random connections downward and makes sure there is at least one connections before moving on
	 * @param row
	 */
	protected void randomDownSet(int row) {
		boolean oneDown = false;
		while (oneDown == false) {
			for (int i = 0; i < colList.size(); i++) {
				Wall wall = new Wall(colList.get(i), row, CardinalDirection.South);
				if (rand.nextBoolean() == true /*&& cells.canGo(wall)*/) {
					cells.deleteWall(wall);
					set[row+1][colList.get(i)] = set[row][colList.get(i)];
				}
			}
			//System.out.println("hi");
			oneDown = atLeastDownOne(row, colList);
		}
	}
	
	/**
	 *  this is the method that checks to be sure there is at least one connection in each set
	 * @param row
	 * @param newlist
	 * @return boolean informing if there is at least one connection
	 */
	protected boolean atLeastDownOne(int row, ArrayList <Integer> newlist)
	
	{
		int count = 0;
		for(int index = 0; index < newlist.size(); index++) {
			if (set[row+1][newlist.get(index)] != 0) {
				count = count + 1;
			}
		}
		if (count > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * this function is used to check if the current cell's set has already been visited
	 * it also sets up the list that will tell how many members there are in a current set on that row
	 * @param col
	 * @param row
	 * @param val (the value of the current set)
	 * @return false once the all the values in each set have been found and added to the list
	 */
	protected ArrayList <Integer> addToArray(int col, int row, int val){
		if (setList.contains(val) == false) { // this checks to see if the set has already been visited
			setList.add(val); // this adds the value to know that the current set has been visited
			for (int index = col; index < width; index++) { // this iterates through the row, adding members of the same set in order to keep track of size
				if (val == set[row][index]) { // condition for two cells to be apart of the same set
					colList.add(index);
				}
			}return colList;
		}return colList;
	}
	
	
	/**
	 * this fills out the row after the connections have been made from the original row to the next row
	 * @param row
	 */
	protected void fillOutRow(int row)
	{
		int nextNum = findNextOpenNum(row);
		for (int col = 0; col < width; col++) {
			if (set[row][col] == 0) {
				set[row][col] = nextNum; // this is where the unassigned cell is given its own set according to the algorithm
				nextNum++;
			}
		}
	}
	
	
	/**
	 * looks through the next row and finds the highest number
	 * @param row
	 * @return returns the highest number plus 1 in order to be sure the set is its own
	 */
	protected int findNextOpenNum(int row) 
	{
		int tempNum = set[row][0];
		for (int col = 0; col < width; col++) {
			if (set[row][col] > tempNum) {
				tempNum = set[row][col]; // sets he temporary number variable to the next highest number in the loop until we have the highest number
			}
		}return (tempNum + 1); // this returns one higher than the highest number in order to create a set that is not apart 
	}
	
	
	/**
	 * this method is going to clear out the final row in order to connect the entire maze and does nothing to cells that are apart of the same set
	 * @param row
	 */
	protected void clearFinalRow(int row)
	{
		for (int col = 0; col < width - 1; col++) {
			/*Wall wall = new Wall(col, row, CardinalDirection.East);
			cells.deleteWall(wall);*/
			if (set[row][col+1] != set[row][col]) {
				Wall wall = new Wall(col, row, CardinalDirection.East);// this references the right wall that is going to be deleted since it is safe too
				cells.deleteWall(wall);
				set[row][col+1] = set[row][col]; // this sets all the changes by equal to one another
			}
		}
	}	
}