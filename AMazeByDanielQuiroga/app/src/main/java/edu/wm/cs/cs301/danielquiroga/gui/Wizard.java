package edu.wm.cs.cs301.danielquiroga.gui;


import edu.wm.cs.cs301.danielquiroga.gui.Robot;
import edu.wm.cs.cs301.danielquiroga.gui.Robot.Direction;
import edu.wm.cs.cs301.danielquiroga.gui.Robot.Turn;



import edu.wm.cs.cs301.danielquiroga.generation.Distance;
import edu.wm.cs.cs301.danielquiroga.generation.MazeConfiguration;
import edu.wm.cs.cs301.danielquiroga.generation.CardinalDirection;



/**
 * 
 * 
 * @author Daniel Quiroga
 *
 */
public class Wizard implements RobotDriver{
	
	/**
	 * width and height are used to contain the information regarding 
	 * the dimensions of the maze for the robot.This is needed with the 
	 * explorer in order to keep create the array that will keep track 
	 * of the positions already visited inside the class
	 */
	protected int width;
	protected int height; 
	
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
	 * the algorithm. This is used when the robot is moving through the maze
	 * and needs to go a certain direction
	 */
	protected CardinalDirection direction;
	
	
	/**
	 * this is the constructor that gives variables the basic values such as 0 and null
	 */
	public Wizard() {
		width = 0;
		height= 0;
		dist = null;
		odometer = 0;
	}
	
	
	
	/**
	 * this connects the driver to the robot which will eventually 
	 * just connect to the controller in order to play the game
	 */
	@Override
	public void setRobot(Robot r) {

		robot = (BasicRobot) r; 
		
	}
	
	
	/**
	 * @return the BasicRobot
	 */
	public BasicRobot getRobot() {
		return this.robot;
	}

	/**
	 * this gives the robot the information of the maze 
	 * by giving the dimensions of said maze
	 */
	@Override
	public void setDimensions(int width, int height) {
	
		this.width = width;
		this.height = height;
		
	}

	/**
	 * this gives the driver the information of the 
	 * distance matrix
	 */
	@Override
	public void setDistance(Distance distance) {

		this.dist = distance;
		
	}

	/**
	 * this method automatically drives the robot 
	 * to the exit which then returns true 
	 * if the robot stops cause of the lost 
	 * of power or any error, then the method returns false 
	 * 
	 * Wizard algorithm -- cheats by using the controller
	 * to find out which cell gets to the exit the quickest 
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		
		
		CardinalDirection nDirr = null;
		CardinalDirection robotDirr = null;

		while(robot.isAtExit() == false && robot.getBatteryLevel() > 0) {
			
			
			// this will get the  current direction of the robot currently
			robotDirr = robot.getCurrentDirection();
			
			// this will get the direction of the adjacent cell that has the closest path to the exit
			nDirr = closerNeighbor();
			
			// this moves the robot toward the exit one step closer
			wizardAlgo(nDirr, robotDirr);
			
					
		}
		
		if(robot.isAtExit() == true) {
			//autoRobotExit();
			return true;

		}
		//robot.getMaze().batErrorScreen();
		//robot.batteryErrorScreen();
		return false;
	}

	@Override
	public boolean drive2ExitAnimation() throws Exception{
		CardinalDirection nDirr = null;
		CardinalDirection robotDirr = null;
		// this will get the  current direction of the robot currently
		robotDirr = robot.getCurrentDirection();

		// this will get the direction of the adjacent cell that has the closest path to the exit
		nDirr = closerNeighbor();

		// this moves the robot toward the exit one step closer
		wizardAlgo(nDirr, robotDirr);

		if(robot.isAtExit() == true) {
			autoRobotExit();
			return true;

		}
		//robot.getMaze().batErrorScreen();
		//robot.batteryErrorScreen();
		return false;

	}



	/**
	 * the switch statement is used to see where the automated robot will go 
	 * and toward what direction it will rotate the robot to the desired 
	 * direction and then move it as well and within the for loop it will 
	 * continue to go until it reached the exit or the robot
	 * loss of energy crashes the game
	 * @param nDirr the direction where we want the robot to go
	 * @param robotDirr the current direction of the robot
	 */
	private void wizardAlgo(CardinalDirection nDirr, CardinalDirection robotDirr) {
		
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
			default:
				break;
			}
			break;
		
		}
	}

	/**
	 * once at the exit position, this method stub just moves 
	 * the robot through the exit by checking in which direction 
	 * the exit is
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
	 * this method stub uses the controller to retrieve the 
	 * coordinates of the adjacent cell that has a shorter 
	 * path to the exit it then converts the coordinates into 
	 * a direction and returns that direction
	 * @return the direction of the adjacent cell that has the shortest path
	 * @throws Exception
	 */
	private CardinalDirection closerNeighbor() throws Exception {

		MazeConfiguration mazeConfig = robot.getMaze().getMazeConfiguration();
		int[] neighborCordinates = mazeConfig.getNeighborCloserToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]);
		
		
		int dx = neighborCordinates[0] - robot.getCurrentPosition()[0];
		
		// to make sure that dx would be a valid parameter for getDirection method
		//assertTrue(dx == 0 || dx == 1 || dx == -1);
		int dy = neighborCordinates[1] - robot.getCurrentPosition()[1];
		
		// to make sure that dy would be a valid parameter for getDirection method
		//assertTrue(dy == 0 || dy == 1 || dy == -1);
		direction = CardinalDirection.getDirection(dx, dy);
		//direction = CardinalDirection.East;
		return direction;
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
	 * sets the distance of the file and if it null 
	 * than uses the control
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