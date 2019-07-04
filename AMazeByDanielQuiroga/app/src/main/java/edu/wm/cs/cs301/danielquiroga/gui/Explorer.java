package edu.wm.cs.cs301.danielquiroga.gui;


import edu.wm.cs.cs301.danielquiroga.gui.Robot;
import edu.wm.cs.cs301.danielquiroga.gui.Robot.Direction;
import edu.wm.cs.cs301.danielquiroga.gui.Robot.Turn;



import edu.wm.cs.cs301.danielquiroga.generation.Distance;

import edu.wm.cs.cs301.danielquiroga.generation.CardinalDirection;
import edu.wm.cs.cs301.danielquiroga.generation.Cells;

import java.util.ArrayList;
import java.util.Random;



/**
 * 
 * @author Daniel Quiroga
 *
 */
public class Explorer implements RobotDriver{
	
	/**
	 * width and height are used to contain the information regarding 
	 * the dimensions of the maze for the robot.This is needed with the 
	 * explorer in order to keep create the array that will keep track 
	 * of the positions already visited inside the class
	 */
	protected int width;
	protected int height;
	
	/**
	 * this 2-D array is a representation of the maze inside the class. 
	 * When initialized it keeps track of what cells have been visited 
	 * and increments everytime the robot moves to a new position. 
	 */
	protected int[][] numVisits;
	
	/**
	 * this robot is initialized here as null until it is set to the 
	 * robot of the application inside MazeApplication. It will be 
	 * the listener of the algorithm and will follow through until 
	 * it reaches the exit or runs out of battery. This information 
	 * is all kept inside basicrobot. This robot will also contain 
	 * the information and reference to the controller which is what 
	 * allows this class to preform and work as expected. 
	 */
	protected BasicRobot robot;
	
	/**
	 * odometer will call on the reference of the basicrobot to get 
	 * a value for what the pathLength is currently. The the get statement 
	 * and reset statement is used when the game ends and the final screen 
	 * prints out the path length and following resets the value of it. 
	 */
	protected int odometer;
	
	/**
	 * dist will hold the information of the distance matrix which gives 
	 * the class information of the maze and how to find the end of the maze. 
	 */
	protected Distance dist;
	
	/**
	 * direction is used to tell the robot where to turn in order to continue 
	 * the algorithm. This is used when the robot is moving through the
	 * maze when not inside a room
	 */
	protected Direction direction;
	
	/**
	 * currDir is initialized and used to tell the class which direction 
	 * the robot is currently facing and how the algorithm needs to use this
	 * information to preform the movement of the algorithm
	 */
	protected CardinalDirection currDirr;
	
	/**
	 * rand is a reference to the Random class which I will use to break 
	 * ties when two positions are possible 
	 */
	protected Random rand = new Random();
	
	/**
	 * this array list will keep track of the the possible directions that 
	 * the robot can go that does not have a wall preventing it form moving
	 */
	protected ArrayList<Direction> possDirr = new ArrayList<Direction>();
	
	/**
	 * cell is used to reference the Cell class which we will attain the 
	 * reference to via the controller. This will be used when the robot needs 
	 * information to find out if the current position is in a room or not. 
	 * if the robot is in a room, this will help the class find the dimensions 
	 * of the room by default helping find all possible doors in the maze
	 */
	protected Cells cell;
	/**
	 *  index and count are used when choosing which door to take in the method. 
	 *  They are reset prior to use in the while loop. 
	 */
	protected int index;
	protected int count;

	
	
	/**
	 * this is the constructor that gives variables 
	 * the basic values such as 0 and null
	 */
	public Explorer() {
		width = 0;
		height= 0;
		dist = null;
		odometer = 0;
		
	}
	
	/**
	 * @return the BasicRobot
	 */
	public BasicRobot getRobot() {
		return this.robot;
	}
	
	/**
	 * this connects the driver to the robot which will 
	 * eventually just connect to the controller in order 
	 * to play the game
	 */
	@Override
	public void setRobot(Robot r) {

		robot = (BasicRobot) r; 
	}
	
	
	/**
	 * this gives the robot the information of the 
	 * maze by giving the dimensions of said maze
	 */
	@Override
	public void setDimensions(int width, int height) {
	
		this.width = width;
		this.height = height;
		
	}

	/**
	 * this gives the driver the information of 
	 * the distance matrix
	 */
	@Override
	public void setDistance(Distance distance) {

		this.dist = distance;
		
	}
	
	
	/**
	 * this method automatically drives the robot to 
	 * the exit which then returns true if the robot 
	 * stops cause of the lost of power or any error, 
	 * then the method returns false 
	 * 
	 * uses the explorer algorithm -- which keeps track 
	 * of where the robot has visited and takes the path 
	 * least traveled 
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		
		initExplorer();
		numVisits[robot.getCurrentPosition()[0]][robot.getCurrentPosition()[1]] = 1;

		while(robot.isAtExit() == false && robot.getBatteryLevel() > 0) {
			//System.out.println(rand.nextInt(3));
			if(robot.isInsideRoom() == false) {
				
				explorerAlgoOutsideRoom();
			}
			//if inside a room
			if(robot.isInsideRoom() == true) {
				
				explorerAlgoInsideRoom();	
			}
		
		}
		//if at exit, leaves the maze and returns true 
		if(robot.isAtExit() == true) {
			autoRobotExit();
			return true;
		}
		// if the robot is not at the exit, push to the error screen and return false
		//robot.getMaze().batErrorScreen();
		robot.batteryErrorScreen();
		return false;
		
	}
	@Override
	public boolean drive2ExitAnimation() throws Exception{

        initExplorer();
        numVisits[robot.getCurrentPosition()[0]][robot.getCurrentPosition()[1]] = 1;
		//System.out.println(rand.nextInt(3));
		if(robot.isInsideRoom() == false) {

			explorerAlgoOutsideRoom();
		}
		//if inside a room
		if(robot.isInsideRoom() == true) {

			explorerAlgoInsideRoom();
		}
		//if at exit, leaves the maze and returns true
		if(robot.isAtExit() == true) {
			autoRobotExit();
			return true;
		}
		// if the robot is not at the exit, push to the error screen and return false
		//robot.getMaze().batErrorScreen();
		//robot.batteryErrorScreen();
		return false;
	}

	/**
	 * when inside a room explorer will considered possible doors 
	 * and will choose the door least traveled
	 * @throws Exception
	 */
	private void explorerAlgoInsideRoom() throws Exception {
		CardinalDirection card = robot.getCurrentDirection();
		int tempX = robot.getCurrentPosition()[0];
		int tempY = robot.getCurrentPosition()[1];
		int[][] cord = new int[30][2];
		this.index = 0;
		// checks where doors are 
		
		possibleDoors(card, tempX, tempY, cord);
		// sees which door has been used the least
		
		// this move array keeps track of what door we want to move to the robot too
		int[] move = new int[2];
		move[0] = cord[0][0];
		move[1] = cord[0][1];
		// num keeps track of the value of the least visited door
		int num = numVisits[move[0]][move[1]];//0;
		//num = numVisits[move[0]][move[1]];
		this.count = 0;
		choosePosition(cord, move, num);
		
		// keeps track of the changes of x and y values
		int dx = move[0]- tempX;
		int dy = move[1] - tempY;
		// increment that chosen door by one
		numVisits[move[0]][move[1]] += 1;
		moveRobotAcrossRoom(card, dx, dy);
		int newx = robot.getCurrentPosition()[0];
		int newy = robot.getCurrentPosition()[1];
		// increment the position outside the room by one
		numVisits[newx][newy] += 1;
	}

	/**
	 * when outside a room explorer will considered possible paths and
	 * choose the position that has been traveled least and resolves 
	 * ties by randomization
	 * @throws Exception
	 */
	private void explorerAlgoOutsideRoom() throws Exception {
		// first check to see which directions are available to move
		// possDir keeps track of all the possible directions that the robot can move too
		addPossibleDirections();
		
		// checks where the robot can move, and adds all the possible movements to an array
		int[][] cord = checkRobotMoves();
		
		// assigns the first possible coordinate's value 
		int tempNum = numVisits[cord[0][0]][cord[0][1]];
		
		// x and y values of the chosen movement
		int[] tempArr = new int[2];
		int count = 0;
		// the fist possible coordinate's x and y value 
		tempArr[0] = cord[0][0];
		tempArr[1] = cord[0][1];
		
		// passes the number, and the array that is 
		// keeping track of the x and y values of the
		// chosen position
		choseMove(cord, tempNum, tempArr, count);
		
		
		// move the robot to the chosen position and then 
		// increment the position in the array that represents 
		// the maze
		int dx = tempArr[0] - robot.getCurrentPosition()[0];
		int dy = tempArr[1] - robot.getCurrentPosition()[1];
		CardinalDirection robotDirr = robot.getCurrentDirection();
		CardinalDirection nDirr = CardinalDirection.getDirection(dx, dy);
		moveRobot(robotDirr, nDirr);
		numVisits[tempArr[0]][tempArr[1]] += 1;
		
		// reset the list after the operation
		possDirr.clear();
	}
	
	/**
	 * this method checks all possible places the robot 
	 * can move and then puts the coordinates of the possible 
	 * movement into a 2-D array
	 * @return the position where the robot will move 
	 * @throws Exception
	 */
	private int[][] checkRobotMoves() throws Exception {
		// then check which cell has been visited the least amount of times 
		int tempX = robot.getCurrentPosition()[0];
		int tempY = robot.getCurrentPosition()[1];
		// cord keeps track of the coordinates [i][0] refers to x and [i][1] refers to y that are possible for movements
		int[][] cord = new int[possDirr.size()][2];
		// getting current direction the robot is facing
		currDirr = robot.getCurrentDirection();
		for(int index = 0; index < possDirr.size(); index++) {
			// get in which direction the robot can go
			direction = possDirr.get(index);
			// switch statement for each card direction the robot may be facing
			addPossibleCoordinates(tempX, tempY, cord, index);
		}
		return cord;
	}
	
	
	/**
	 * this method moves the robot across the maze through a door 
	 * it has not visited it
	 * @param card tells which direction that he robot is currently facing 
	 * @param dx the difference of x-values
	 * @param dy the difference of y-values 
	 * @throws Exception
	 */
	private void moveRobotAcrossRoom(CardinalDirection card, int dx, int dy) throws Exception {
		switch(card) {
		case East:
			if(dx > 0 && dy > 0) {
				robot.move(dx, false);
				robot.rotate(Turn.LEFT);
				robot.move(dy, false);
				exitRoom();
			}
			else if(dx > 0 && dy < 0) {
				robot.move(dx, false);
				robot.rotate(Turn.RIGHT);
				robot.move(-dy, false);
				exitRoom();
			}
			else if(dx <= 0 && dy > 0) {
				robot.rotate(Turn.LEFT);
				robot.move(dy, false);
				exitRoom();
			}
			else if(dx <= 0 && dy < 0) {
				robot.rotate(Turn.RIGHT);
				robot.move(-dy, false);
				exitRoom();
			}
			else if(dx > 0 && dy == 0) {
				robot.move(dx, false);
				exitRoom();
			}
			else {
				exitRoom();
			}
			
			
			break;
		case North:
			if(dx > 0 && dy >= 0) {
				robot.rotate(Turn.LEFT);
				robot.move(dx, false);
				exitRoom();
			}
			else if(dx > 0 && dy < 0) {
				robot.move(-dy, false);
				robot.rotate(Turn.LEFT);
				robot.move(dx, false);
				exitRoom();
			}
			else if(dx < 0 && dy >= 0) {
				robot.rotate(Turn.RIGHT);
				robot.move(-dx, false);
				exitRoom();
			}
			else if(dx < 0 && dy < 0) {
				robot.move(-dy, false);
				robot.rotate(Turn.RIGHT);
				robot.move(-dx, false);
				exitRoom();
			}
			else if(dx == 0 && dy < 0) {
				robot.move(-dy, false);
				exitRoom();
			}
			else {
				exitRoom();
			}
			
			break;
		case South:
			
			if(dx < 0 && dy <= 0) {
				robot.rotate(Turn.LEFT);
				robot.move(-dx, false);
				exitRoom();
			}
			else if(dx < 0 && dy > 0) {
				robot.move(dy, false);
				robot.rotate(Turn.LEFT);
				robot.move(-dx, false);
				exitRoom();
			}
			else if(dx > 0 && dy <= 0) {
				robot.rotate(Turn.RIGHT);
				robot.move(dx, false);
				exitRoom();
			}
			else if(dx > 0 && dy > 0) {
				robot.move(dy, false);
				robot.rotate(Turn.RIGHT);
				robot.move(dx, false);
				exitRoom();
			}
			else if(dx == 0 && dy > 0) {
				robot.move(dy, false);
				exitRoom();
			}
			else {
				exitRoom();
			}
			
			break;
		case West:
			if(dx < 0 && dy < 0) {
				robot.move(-dx, false);
				robot.rotate(Turn.LEFT);
				robot.move(-dy, false);
				exitRoom();
			}
			else if(dx < 0 && dy > 0) {
				robot.move(-dx, false);
				robot.rotate(Turn.RIGHT);
				robot.move(dy, false);
				exitRoom();
			}
			else if(dx >= 0 && dy < 0) {
				robot.rotate(Turn.LEFT);
				robot.move(-dy, false);
				exitRoom();
			}
			else if(dx >= 0 && dy > 0) {
				robot.rotate(Turn.RIGHT);
				robot.move(dy, false);
				exitRoom();
			}
			else if(dx < 0 && dy == 0) {
				robot.move(-dx, false);
				exitRoom();
			}
			else {
				exitRoom();
			}
			
			
			break;
		default:
			break;
		
		}
	}
	/**
	 * this method allows the robot to exit 
	 * the room once it reached the door cell
	 * @throws Exception
	 */
	private void exitRoom() throws Exception {
		int x = robot.getCurrentPosition()[0];
		int y = robot.getCurrentPosition()[1];
		numVisits[x][y] += 1; 
		if(cell.isInRoom(x + 1, y) == false && cell.hasNoWall(x, y, CardinalDirection.East)) {
			int rx = (x+1) - x;
			int ry = y - y;
			CardinalDirection robotDir = robot.getCurrentDirection();
			CardinalDirection nDir = CardinalDirection.getDirection(rx, ry);
			moveRobot(robotDir, nDir);
		}
		if(cell.isInRoom(x - 1, y) == false && cell.hasNoWall(x, y, CardinalDirection.West)) {
			int rx = (x-1) - x;
			int ry = y - y;
			CardinalDirection robotDir = robot.getCurrentDirection();
			CardinalDirection nDir = CardinalDirection.getDirection(rx, ry);
			moveRobot(robotDir, nDir);
		}
		if(cell.isInRoom(x, y + 1) == false && cell.hasNoWall(x, y, CardinalDirection.South)) {
			int rx = x - x;
			int ry = (y+1) - y;
			CardinalDirection robotDir = robot.getCurrentDirection();
			CardinalDirection nDir = CardinalDirection.getDirection(rx, ry);
			moveRobot(robotDir, nDir);
		}
		if(cell.isInRoom(x, y - 1) == false && cell.hasNoWall(x, y, CardinalDirection.North)) {
			int rx = x - x;
			int ry = (y-1) - y;
			CardinalDirection robotDir = robot.getCurrentDirection();
			CardinalDirection nDir = CardinalDirection.getDirection(rx, ry);
			moveRobot(robotDir, nDir);
		}
	}
	
	
	/**
	 * chooses the door that the robot 
	 * should use randomly 
	 * @param cord keeps track of all the coordinates of the doors
	 * @param move the chosen move currently
	 * @param num the lowest number currently
	 */
	private void choosePosition(int[][] cord, int[] move, int num) {
		if(this.index > 1) {
			for(int i = 1; i < index; i++) {
				if(numVisits[cord[i][0]][cord[i][1]] < num) {
					num = numVisits[cord[i][0]][cord[i][1]];
					move[0] = cord[i][0];
					move[1] = cord[i][1];
					this.count = 0;
				}
				else if(numVisits[cord[i][0]][cord[i][1]] == num) {
					this.count++;
				}
			}
		}

		if(this.count > 0) {
			
			int[] tieArr = new int[count + 1];
			int tempIndex = 0;
			for(int i = 0; i < index; i++) {
				if(numVisits[cord[i][0]][cord[i][1]] == num) {
					//assertTrue(tempIndex <= count);
					tieArr[tempIndex] = i; 
					tempIndex++;
				}
			}
			int randomNum = rand.nextInt(count+1);
			move[0] = cord[tieArr[randomNum]][0];
			move[1] = cord[tieArr[randomNum]][1];
			
		}
	}
	
	
	/**
	 * this method checks where the doors are in the room 
	 * and returns an array with all the doors
	 * @param card, which direction the robot is facing 
	 * @param tempX the current x position of the robot
	 * @param tempY the current y position of the robot
	 * @param cord the array that will keep track of the positions of the possible doors
	 */
	private void possibleDoors(CardinalDirection card, int tempX, int tempY, int[][] cord) {
		
		if(card == CardinalDirection.East || card == CardinalDirection.West) {
			// gives the dimensions of the room  rh is the height of the room rw is the width of the room
			int rw = robot.distanceToObstacle(Direction.FORWARD);
			int distL = robot.distanceToObstacle(Direction.LEFT);
			int distR = robot.distanceToObstacle(Direction.RIGHT);
			

			
			switch(card) {
			// this checks when the robot is east wherever there is a door in the room
			
			case East:
				
			while(cell.isInRoom(tempX, tempY - distR) == false) {
				distR--;
			}
			while(cell.isInRoom(tempX, tempY + distL) == false) {
				distL--;
			}
			while(cell.isInRoom(tempX + rw, tempY) == false) {
				rw--;
			}
			
			
			for(int col = 0; col <= distR; col++) { 
				if(cell.hasNoWall(tempX, tempY - col, CardinalDirection.West)) {
					//assertTrue(index < 30);
					cord[index][0] = tempX;
					cord[index][1] = tempY - col;
					index++;
				}
				if(cell.hasNoWall(tempX + rw, tempY - col, CardinalDirection.East)) {
					//assertTrue(index < 30);
					cord[index][0] = tempX + rw;
					cord[index][1] = tempY - col;
					index++;
				}
			}
			for(int col = 1; col <= distL; col++) {
				if(cell.hasNoWall(tempX, tempY + col, CardinalDirection.West)) {
					//assertTrue(index < 30);
					cord[index][0] = tempX;
					cord[index][1] = tempY + col;
					index++;
				}
				if(cell.hasNoWall(tempX + rw, tempY + col, CardinalDirection.East)) {
					//assertTrue(index < 30);
					cord[index][0] = tempX + rw;
					cord[index][1] = tempY + col;
					index++;
				}
			}
			for(int row = 0; row <= rw; row++){
				if(cell.hasNoWall(tempX + row, tempY + distL, CardinalDirection.South)) {
					//assertTrue(index < 30);
					cord[index][0] = tempX + row;
					cord[index][1] = tempY + distL;
					index++;
				}
				if(cell.hasNoWall(tempX + row, tempY - distR, CardinalDirection.North)) {
					//assertTrue(index < 30);
					cord[index][0] = tempX + row;
					cord[index][1] = tempY - distR;
					index++;
				
				}
			}
			
				break;
			
			case West:
				
				while(cell.isInRoom(tempX, tempY + distR) == false) {
					distR--;
				}
				while(cell.isInRoom(tempX, tempY - distL) == false) {
					distL--;
				}
				while(cell.isInRoom(tempX - rw, tempY) == false) {
					rw--;
				}
				
				for(int col = 0; col <= distL; col++) {
					if(cell.hasNoWall(tempX, tempY - col, CardinalDirection.East)) {
						//assertTrue(index < 30);
						cord[index][0] = tempX;
						cord[index][1] = tempY - col;
						index++;
					}
					if(cell.hasNoWall(tempX - rw, tempY - col, CardinalDirection.West)) {
						//assertTrue(index < 30);
						cord[index][0] = tempX - rw;
						cord[index][1] = tempY - col;
						index++;
					}
				}
				for(int col = 1; col <= distR; col++) {
					if(cell.hasNoWall(tempX, tempY + col, CardinalDirection.East)) {
						//assertTrue(index < 30);
						cord[index][0] = tempX;
						cord[index][1] = tempY + col;
						index++;
					}
					if(cell.hasNoWall(tempX - rw, tempY + col, CardinalDirection.West)) {
						//assertTrue(index < 30);
						cord[index][0] = tempX - rw;
						cord[index][1] = tempY + col;
						index++;
					}
				}
				for(int row = 0; row <= rw; row++){
					if(cell.hasNoWall(tempX - row, tempY + distR, CardinalDirection.South)) {
						//assertTrue(index < 30);
						cord[index][0] = tempX - row;
						cord[index][1] = tempY + distR;
						index++;
					}
					if(cell.hasNoWall(tempX - row, tempY - distL, CardinalDirection.North)) {
						//assertTrue(index < 30);
						cord[index][0] = tempX - row;
						cord[index][1] = tempY - distL;
						index++;
					
					}
				}

				break;
			default:
				break;
				
			}	
		}
		
		
		else if(card == CardinalDirection.North || card == CardinalDirection.South) {
			// gives the dimensions of the room rh is the height of the room rw is the width of the room
			int rh = robot.distanceToObstacle(Direction.FORWARD);
			int distL = robot.distanceToObstacle(Direction.LEFT);
			int distR = robot.distanceToObstacle(Direction.RIGHT);
			
			switch(card) {
			case North:
				
				while(cell.isInRoom(tempX - distR, tempY) == false) {
					distR--;
				}
				while(cell.isInRoom(tempX + distL, tempY) == false) {
					distL--;
				}
				while(cell.isInRoom(tempX, tempY - rh) == false) {
					rh--;
				}
				
				for(int col = 0; col <= rh; col++) {
					
					if(cell.hasNoWall(tempX - distR, tempY - col, CardinalDirection.West)) {
						//assertTrue(index < 30);
						cord[index][0] = tempX - distR;
						cord[index][1] = tempY - col;
						index++;
					}
					if(cell.hasNoWall(tempX + distL, tempY - col, CardinalDirection.East)) {
						//assertTrue(index < 30);
						cord[index][0] = tempX + distL;
						cord[index][1] = tempY - col;
						index++;
					}
				}
				
				for(int row = 0; row <= distL; row++) {
					if(cell.hasNoWall(tempX + row, tempY, CardinalDirection.South)) {
						//assertTrue(index < 30);
						cord[index][0] = tempX + row;
						cord[index][1] = tempY;
						index++;
					}
					if(cell.hasNoWall(tempX + row, tempY - rh, CardinalDirection.North)) {
						//assertTrue(index < 30);
						cord[index][0] = tempX + row;
						cord[index][1] = tempY - rh;
						index++;
					}
				}
				
				for(int row = 1; row <= distR; row++) {
					if(cell.hasNoWall(tempX - row, tempY, CardinalDirection.South)) {
						//assertTrue(index < 30);
						cord[index][0] = tempX - row;
						cord[index][1] = tempY;
						index++;
					}
					if(cell.hasNoWall(tempX - row, tempY - rh, CardinalDirection.North)) {
						//assertTrue(index < 30);
						cord[index][0] = tempX - row;
						cord[index][1] = tempY - rh;
						index++;
					}
				}
				
				
				break;
			case South:
				

				while(cell.isInRoom(tempX + distR, tempY) == false) {
					distR--;
				}
				while(cell.isInRoom(tempX - distL, tempY) == false) {
					distL--;
				}
				while(cell.isInRoom(tempX, tempY + rh) == false) {
					rh--;
				}
				
				for(int col = 0; col <= rh; col++) {
					
					if(cell.hasNoWall(tempX + distR, tempY + col, CardinalDirection.East)) {
						//assertTrue(index < 30);
						cord[index][0] = tempX + distR;
						cord[index][1] = tempY + col;
						index++;
					}
					if(cell.hasNoWall(tempX - distL, tempY + col, CardinalDirection.West)) {
						//assertTrue(index < 30);
						cord[index][0] = tempX - distL;
						cord[index][1] = tempY + col;
						index++;
					}
				}
				
				
				for(int row = 0; row <= distL; row++) {
					if(cell.hasNoWall(tempX - row, tempY, CardinalDirection.North)) {
						//assertTrue(index < 30);
						cord[index][0] = tempX - row;
						cord[index][1] = tempY;
						index++;
					}
					if(cell.hasNoWall(tempX - row, tempY + rh, CardinalDirection.South)) {
					//	assertTrue(index < 30);
						cord[index][0] = tempX - row;
						cord[index][1] = tempY + rh;
						index++;
					}
				}
				
				for(int row = 1; row <= distR; row++) {
					if(cell.hasNoWall(tempX + row, tempY, CardinalDirection.North)) {
						//assertTrue(index < 30);
						cord[index][0] = tempX + row;
						cord[index][1] = tempY;
						index++;
					}
					if(cell.hasNoWall(tempX + row, tempY + rh, CardinalDirection.South)) {
						//assertTrue(index < 30);
						cord[index][0] = tempX + row;
						cord[index][1] = tempY + rh;
						index++;
					}
				}
				break;
			default:
				break;
			}
			
			

		}
	}
	
	
	/**
	 * This function actually moves the robot 1 toward the 
	 * cell that has been visited the least amount of times 
	 * @param robotDirr, the direction the robot is facing currently
	 * @param nDirr, the direction we want the robot to move toward once
	 */
	private void moveRobot(CardinalDirection robotDirr, CardinalDirection nDirr) {
		switch(robotDirr) {
		
		case East:
			switch(nDirr) {
			case East:
				robot.move(1, false);
				break;
			case North:
				robot.rotate(Turn.RIGHT);
				robot.move(1, false);
				break;
			case South:
				robot.rotate(Turn.LEFT);
				robot.move(1, false);
				break;
			case West:
				robot.rotate(Turn.AROUND);
				robot.move(1, false);
				break;
			default:
				break;
			}
			break;
				
		case North:
			switch(nDirr) {
			case East:
				robot.rotate(Turn.LEFT);
				robot.move(1, false);
				break;
			case North:
				robot.move(1, false);
				break;
			case South:
				robot.rotate(Turn.AROUND);
				robot.move(1, false);
				break;
			case West:
				robot.rotate(Turn.RIGHT);
				robot.move(1, false);
				break;
			default:
				break;
			
			}
			break;
			
		case South:
			switch(nDirr) {
			case West:
				robot.rotate(Turn.LEFT);
				robot.move(1, false);
				break;
			case South:
				robot.move(1, false);
				break;
			case North:
				robot.rotate(Turn.AROUND);
				robot.move(1, false);
				break;
			case East:
				robot.rotate(Turn.RIGHT);
				robot.move(1, false);
				break;
			default:
				break;
			
			}
			break;

		case West:
			switch(nDirr) {
			case West:
				robot.move(1, false);
				break;
			case North:
				robot.rotate(Turn.LEFT);
				robot.move(1, false);
				break;
			case South:
				robot.rotate(Turn.RIGHT);
				robot.move(1, false);
				break;
			case East:
				robot.rotate(Turn.AROUND);
				robot.move(1, false);
				break;
			}
			break;
		
		}
	}
	
	
	/**
	 * this method decides which direction to move the robot 
	 * and updates tempArr to give the coordinates of the movement 
	 * @param cord the array that is keeping track of the possible places the robot can go
	 * @param tempNum the lowest number , allows the robot to see where to go
	 * @param tempArr keeps track of the coordinates of the preferred movement
	 * @param count keeps track of how many tempNums there are
	 */
	private void choseMove(int[][] cord, int tempNum, int[] tempArr, int count) {
		if (possDirr.size() > 1) {
			for(int i = 1; i < possDirr.size(); i++) {
				if(numVisits[cord[i][0]][cord[i][1]] < tempNum) {
					tempNum = numVisits[cord[i][0]][cord[i][1]];
					tempArr[0] = cord[i][0];
					tempArr[1] = cord[i][1];
					count = 0;
				}
				else if(numVisits[cord[i][0]][cord[i][1]] == tempNum) {
					count++;
				}
			}
		}
			
		// if tie just randomly choose one 
		if(count > 0) {
			int[] tieArr = new int[count + 1];
			int tempIndex = 0;
			for(int i = 0; i < possDirr.size(); i++) {
				if(numVisits[cord[i][0]][cord[i][1]] == tempNum) {
					tieArr[tempIndex] = i; 
					tempIndex++;
					//assertTrue(tempIndex <= count + 1);
				}
			}
			int randomNum = rand.nextInt(count+1);
			tempArr[0] = cord[tieArr[randomNum]][0];
			tempArr[1] = cord[tieArr[randomNum]][1];
		}
		
	}
	
	
	/**
	 * this adds all the possible directions to a list
	 */
	private void addPossibleDirections() {
		if(robot.distanceToObstacle(Direction.FORWARD) > 0) {
			possDirr.add(Direction.FORWARD);
		}
		if(robot.distanceToObstacle(Direction.BACKWARD) > 0) {
			possDirr.add(Direction.BACKWARD);
		}
		if(robot.distanceToObstacle(Direction.RIGHT) > 0) {
			possDirr.add(Direction.RIGHT);
		}
		if(robot.distanceToObstacle(Direction.LEFT) > 0) {
			possDirr.add(Direction.LEFT);
		}
	}
	
	
	/**
	 * this uses the list thats keeping track of directions 
	 * and converts the direction into coordinates
	 * @param tempX the current x position of the robot
	 * @param tempY the current y position of the robot
	 * @param cord adds the coordinates to an array
	 * @param index adds the coordinate to its unique position
	 */
	private void addPossibleCoordinates(int tempX, int tempY, int[][] cord, int index) {
		switch(currDirr) {
		// in each case place a switch statement that will update the tempX and tempY variables for the possible cord
		case East:
			switch(direction) {
			case BACKWARD:
				cord[index][0] = tempX - 1;
				cord[index][1] = tempY;
				break;
			case FORWARD:
				cord[index][0] = tempX + 1;
				cord[index][1] = tempY;
				break;
			case LEFT:
				cord[index][0] = tempX;
				cord[index][1] = tempY + 1;
				break;
			case RIGHT:
				cord[index][0] = tempX;
				cord[index][1] = tempY - 1;
				break;
			}
			break;
			
			
		case North:
			switch(direction) {
			case BACKWARD:
				cord[index][0] = tempX;
				cord[index][1] = tempY + 1;
				break;
			case FORWARD:
				cord[index][0] = tempX;
				cord[index][1] = tempY - 1;
				break;
			case LEFT:
				cord[index][0] = tempX + 1;
				cord[index][1] = tempY;
				break;
			case RIGHT:
				cord[index][0] = tempX - 1;
				cord[index][1] = tempY;
				break;
			}
			break;
			
			
		case South:
			switch(direction) {
			case BACKWARD:
				cord[index][0] = tempX;
				cord[index][1] = tempY - 1;
				break;
			case FORWARD:
				cord[index][0] = tempX;
				cord[index][1] = tempY + 1;
				break;
			case LEFT:
				cord[index][0] = tempX - 1;
				cord[index][1] = tempY;
				break;
			case RIGHT:
				cord[index][0] = tempX + 1;
				cord[index][1] = tempY;
				break;
			}
			break;
			
			
		case West:
			switch(direction) {
			case BACKWARD:
				cord[index][0] = tempX + 1;
				cord[index][1] = tempY;
				break;
			case FORWARD:
				cord[index][0] = tempX - 1;
				cord[index][1] = tempY;
				break;
			case LEFT:
				cord[index][0] = tempX;
				cord[index][1] = tempY - 1;
				break;
			case RIGHT:
				cord[index][0] = tempX;
				cord[index][1] = tempY + 1;
				break;
			}
			break;

		}
	}
	
	
	/**
	 * initializes Explorer to have the proper dimensions 
	 * for the array that keeps track of where the robot 
	 * has gone
	 */
	private void initExplorer() {
		cell = robot.getMaze().getMazeConfiguration().getMazecells();
		//int row = cell.width;
		//int col = cell.height;
		numVisits = new int[this.width][this.height];
		for(int i = 0; i < this.width; i++) {
			for(int j = 0; j < this.height; j++) {
				numVisits[i][j] = 0;
			}
		}
		possDirr.clear();
	}

	
	/**
	 * once at the exit position, this method stub just 
	 * moves the robot through the exit by checking in 
	 * which direction the exit is
	 */
	private void autoRobotExit() {
		if(robot.canSeeExit(Direction.RIGHT) == false && robot.canSeeExit(Direction.LEFT) == false) {
			robot.move(1, false);
		}
		else if(robot.canSeeExit(Direction.RIGHT) == true) {
			robot.rotate(Turn.RIGHT);
			robot.move(1, false);
		}
		else {
			robot.rotate(Turn.LEFT);
			robot.move(1, false);
		}
	}
	
	
	/**
	 * @return the amount that the driver has used of the energy
	 */
	@Override
	public float getEnergyConsumption() {

		return 3000 - robot.getBatteryLevel();
	}
	
	/**
	 * @return the pathLength that the driver has gone through
	 */
	@Override
	public int getPathLength() {

		odometer = robot.getOdometerReading();
		
		return odometer;
	}
	
	
	/**
	 * sets the distance of the file and if it 
	 * null than uses the control
	 * @return the variable in reference to distance
	 */

	public Distance getDist() {
		if(this.dist == null) {
			Distance distance = robot.getMaze().getMazeConfiguration().getMazedists();
			this.setDistance(distance);
		}
		return this.dist;
	}
	
	
	/**
	 * sets the dimensions of the driver for the robot
	 * @return the integer of the width of the maze 
	 */
	public int getWidth() {
		if(this.width == 0) {
			int width = robot.getMaze().getMazeConfiguration().getWidth();
			int height = robot.getMaze().getMazeConfiguration().getHeight();
			this.setDimensions(width, height);
		}
		return this.width;
	}
	
	/**
	 * sets the dimensions of the driver for the robot
	 * @return the integer of the height of the maze 
	 */
	public int getHeight() {
		if(this.width == 0) {
			int width = robot.getMaze().getMazeConfiguration().getWidth();
			int height = robot.getMaze().getMazeConfiguration().getHeight();
			this.setDimensions(width, height);
		}
		return this.height;
	}
}