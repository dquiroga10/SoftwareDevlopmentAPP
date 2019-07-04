package edu.wm.cs.cs301.danielquiroga.gui;

import edu.wm.cs.cs301.danielquiroga.gui.Constants.UserInput;
import edu.wm.cs.cs301.danielquiroga.gui.Robot;
import edu.wm.cs.cs301.danielquiroga.gui.Robot.Direction;
import edu.wm.cs.cs301.danielquiroga.gui.Robot.Turn;




import edu.wm.cs.cs301.danielquiroga.generation.Distance;


/**
 * @author Daniel Quiroga
 */

public class ManualDriver implements RobotDriver{
	
	Robot robot;
	private int width;
	private int height;
	private Distance dist;
	private int odometer; 
	
	
	public ManualDriver() {
		
		width = 0;
		height= 0;
		//robot = null;
		dist = null;
		odometer = 0;
	}

	@Override
	public void setRobot(Robot r) {
		// this is set in the MazeApplication file
		//assertNotNull(r);// makes sure that there is a robot that will read what is being passed by this file
		robot = r;
		
	}

	@Override
	public void setDimensions(int width, int height) {
		// this is set the SimpleKeyListener whenever a key is pressed
		//assertNotNull(width);// makes sure that actual integers are passed in both assert() statements
		//assertNotNull(height);
		this.width = width;
		this.height = height;
		
	}

	@Override
	public void setDistance(Distance distance) {
		// this is set in the SimpleKeyListener whenever a key is pressed
		//assertNotNull(distance);// makes sure that actual integers are passed in both assert() statements
		dist = distance;
		//dist = ((BasicRobot)robot).control.getMazeConfiguration().getMazedists();
	}

	@Override
	public boolean drive2Exit() throws Exception {
		// since it is manual the robot has to be at the exit and facing it
		// TODO
		/*if(robot.canSeeExit(Direction.FORWARD) && robot.isAtExit()) {
			return true;
		}*/
		return false;
	}

	@Override
	public boolean drive2ExitAnimation() throws Exception{
		return false;
	}

	@Override
	public float getEnergyConsumption() {
		
		return 3000 - robot.getBatteryLevel();
	}

	@Override
	public int getPathLength() {
		// receive the information for how far the robot has traveled from the robot
		odometer = robot.getOdometerReading();
		
		return odometer;
	}
	
	
	/**
	 * takes a key from simplekeylistener and tells the robot what to do with it 
	 * @param key
	 * @return
	 */
	public boolean keyDown(UserInput key) {
		//assertNotNull(key);// make sure a null key input is not passed, this would cause many errors in the program
		switch(key) {
		case Down: // battery should lose 11 since its a half rotation and a movement
			robot.rotate(Turn.AROUND);
			robot.move(1, true);
			//System.out.println(robot.getBatteryLevel());
			//System.out.println(getPathLength());
			break;

		case Left:
			robot.rotate(Turn.LEFT);
			//System.out.println(robot.getBatteryLevel());
			//System.out.println(getPathLength());
			break;

		case Right:
			robot.rotate(Turn.RIGHT);
			//System.out.println(robot.getBatteryLevel());
			//System.out.println(getPathLength());
			break;

		case Up:
			robot.move(1, true);
			//System.out.println(robot.getBatteryLevel());
			//System.out.println(getPathLength());
			break;
		default:
			break;

		}
		return true;
	}
	
	
	/**
	 * @return the value of the width variable in the ManualDriver 
	 */
	public int getWidth() {
		return width;
	}
	
	
	/**
	 * @return the value of the height variable in the ManualDriver
	 */
	public int getHeigth() {
		return height;
	}
	
	
	/**
	 * @return the value of dist variable in the ManualDriver
	 */
	public Distance getDist() {
		return dist;
	}
}