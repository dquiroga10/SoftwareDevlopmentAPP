/**
 * 
 */
package edu.wm.cs.cs301.danielquiroga.gui;



import edu.wm.cs.cs301.danielquiroga.generation.CardinalDirection;
import edu.wm.cs.cs301.danielquiroga.generation.Distance;
import edu.wm.cs.cs301.danielquiroga.gui.Robot.Direction;
import edu.wm.cs.cs301.danielquiroga.gui.Robot.Turn;

/**
 * @author danielquiroga
 *
 */
public class Pledge implements RobotDriver {
	
	/**
	 * width and height are used to contain the information regarding 
	 * the dimensions of the maze for the robot.This is needed with the 
	 * explorer in order to keep create the array that will keep track 
	 * of the positions already visited inside the class
	 */
	protected int width;
	protected int height;
	private int counter = 0;
	
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
	 * this is the constructor that gives variables the basic values 
	 * such as 0 and null
	 */
	public Pledge() {
		width = 0;
		height= 0;
		dist = null;
		odometer = 0;
	}

	/**
	 * this connects the driver to the robot which will eventually just 
	 * connect to the controller in order to play the game
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
	 * this gives the robot the information of the maze by giving 
	 * the dimensions of said maze
	 */
	@Override
	public void setDimensions(int width, int height) {
	
		this.width = width;
		this.height = height;
		
	}

	/**
	 * this gives the driver the information of the distance matrix
	 */
	@Override
	public void setDistance(Distance distance) {

		this.dist = distance;
		
	}

	/* 
	 *  this method drives the robot to the exit, if it reaches the exit it 
	 * @returns true
	 * if for some reason it does not reach the exit it 
	 * @returns false 
	 * pledge algo explained in the method
	 */
	@Override
	public boolean drive2Exit() throws Exception {

		while(robot.isAtExit() == false && robot.getBatteryLevel() > 0) {
			if(robot.getCurrentDirection() == CardinalDirection.East && robot.distanceToObstacle(Direction.FORWARD) > 0) {
				robot.move(1, false);
			}
			else {
				pledgeAlgo();
			}
			
		}
		
		if(robot.isAtExit() == true) {
			autoRobotExit();
			return true;
		}
		//robot.getMaze().batErrorScreen();
		robot.batteryErrorScreen();
		return false;
	}

	@Override
	public boolean drive2ExitAnimation() throws Exception{
		if ( counter == 0) {
			if (robot.getCurrentDirection() == CardinalDirection.East && robot.distanceToObstacle(Direction.FORWARD) > 0) {
				robot.move(1, false);
			} else {
				if (robot.distanceToObstacle(Direction.FORWARD) == 0) {
					robot.rotate(Turn.LEFT);
					counter--;
				}
				counter = pledgeAlgoAnimation(counter);
			}
		}
		counter = pledgeAlgoAnimation(counter);
		if(robot.isAtExit() == true) {
			autoRobotExit();
			return true;
		}
		//robot.getMaze().batErrorScreen();
		//robot.batteryErrorScreen();
		return false;
	}

	private int pledgeAlgoAnimation(int count){
		if(robot.getCurrentDirection() != CardinalDirection.East || count != 0){
			if(robot.isAtExit() == false) {
				if (robot.distanceToObstacle(Direction.RIGHT) == 0 && robot.distanceToObstacle(Direction.FORWARD) == 0) {
					robot.rotate(Turn.LEFT);
					count--;
				} else if (robot.distanceToObstacle(Direction.RIGHT) > 0) {
					robot.rotate(Turn.RIGHT);
					robot.move(1, false);
					count++;
				} else if (robot.distanceToObstacle(Direction.RIGHT) == 0 && robot.distanceToObstacle(Direction.FORWARD) > 0) {
					robot.move(1, false);
				}
			}

		}
		return count;
	}

	/**
	 * Pledge -- the  robot moves forward until an obstacle is reached. 
	 * Once the bot hits the obstacle, it rotates left and the counter 
	 * decremeants by one.
	 * For every movement the bot makes, the bot first evaluates if 
	 * there is an obstacle to the right. If there isn't, the bot moves right 
	 * and the counter is incremeanted by one.
	 * It will then move forward.
	 * If there is an obstacle to the right AND no obstacle in front of the 
	 * bot, the bot moves forward. And finally, if there is an obstacle 
	 * to the right AND to the front, the bot 
	 * turns to the left and the counter is decreamented by one.
	 */
	private void pledgeAlgo() {

		int count = 0;
		if(robot.distanceToObstacle(Direction.FORWARD) == 0) {
			robot.rotate(Turn.LEFT);
			count--;
		}
		while(robot.getCurrentDirection() != CardinalDirection.East || count != 0) {
			if(robot.isAtExit() == false) {
				if(robot.distanceToObstacle(Direction.RIGHT) == 0 && robot.distanceToObstacle(Direction.FORWARD) == 0) {
					robot.rotate(Turn.LEFT);
					count--;
				}
				else if(robot.distanceToObstacle(Direction.RIGHT) > 0) {
					robot.rotate(Turn.RIGHT);
					robot.move(1, false);
					count++;
				}
				else if(robot.distanceToObstacle(Direction.RIGHT) == 0 && robot.distanceToObstacle(Direction.FORWARD) > 0) {
					robot.move(1, false);
				}
			}
			else {
				break;
			}
		}
	}
	
	/**
	 * once at the exit position, this method stub just moves the 
	 * robot through the exit by checking in which direction the exit is
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
	 * sets the distance of the file and if it null than uses the control
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
			int width = 1;//TODO robot.getMaze().getMazeConfiguration().getWidth();
			int height = 1;//TODO  robot.getMaze().getMazeConfiguration().getHeight();
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
			int width = 1;//TODO robot.getMaze().getMazeConfiguration().getWidth();
			int height = 1; //TODO robot.getMaze().getMazeConfiguration().getHeight();
			this.setDimensions(width, height);
		}
		return this.height;
	}

}
