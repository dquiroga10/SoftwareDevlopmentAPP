package edu.wm.cs.cs301.danielquiroga.gui;

import edu.wm.cs.cs301.danielquiroga.generation.Cells;
import edu.wm.cs.cs301.danielquiroga.gui.Constants.UserInput;



import java.lang.AssertionError;
import edu.wm.cs.cs301.danielquiroga.generation.CardinalDirection;


/**
 * 
 * @author Daniel Quiroga
 *
 */

public class BasicRobot implements Robot{
	 // TODO finish writing comments
	
	/**
	 *  this booalean return true since they are showing 
	 *  that the robot has the sensors it needs in order to preform
	 */
	private boolean backwardSensor;
	private boolean striaghtSensor;
	private boolean rightSensor;
	private boolean leftSensor;
	private boolean roomSensor;
	
	/**
	 * this  variable will be set to the corresponding 
	 * control object that is connecting to the maze
	 */
	private StatePlaying control;
	
	/**
	 * this variable will be set in order to have a reference
	 * to the Cells class which will be referred to for the 
	 * width/height of the maze. and to check is the robot is 
	 * at an exit position or not. or inside a room or not. uses
	 * information from the controller to do so.
	 */
	private Cells cell;
	
	/**
	 * this odometer keeps track of how many cells the robot has 
	 * visited and will be essential to the last screen functionality
	 */
	private static int odometer = 0;
	
	/**
	 * this batLev will keep track of how much energy has been used inside
	 * the class. this will be used in the last screen functionality
	 * since the driver will subtract it to find out the energy consumption
	 */
	private static float batLev;
	
	/**
	 * this will tell if the robot is moving or has stopped for some reason
	 */
	private boolean cantMove;

	
	
	
	/**
	 * this will set the variables to the basic values of true or 0 and the 
	 * battery level back to the original level
	 */
	public BasicRobot() {
		cantMove = false;
		batLev = 3000;
		backwardSensor = true;
		striaghtSensor = true;
		leftSensor = true;
		rightSensor = true;
		roomSensor = true;
		odometer = 0;
		
	}
	
	/**
	 * moved the error screen from controller to basicrobot in 
	 * order to lessen the multiple passing of code
	 * this will just call on the error screen 
	 */
	public void batteryErrorScreen() {
		this.setBatteryLevel(-1);
		// TODO control.switchFromPlayingToWinning(this.getOdometerReading(), this.getBatteryLevel());
	}
	
	
	/**
	 * this will rotate the robot to the specified position
	 * @param turn you want to preform
	 */
	@Override
	public void rotate(Turn turn) {
		float tempBat = this.getBatteryLevel() - 3;
		int odo = this.getOdometerReading();
		//assertNotNull(turn); // making sure that a null parameter did not make it through
		switch(turn) {
		case AROUND:
			
			turnAround(tempBat, odo);
			break;
			
		case RIGHT:
			
			turnRight(tempBat, odo);
			break;
			
		case LEFT:
			
			turnLeft(tempBat, odo);
			break;
			
		default:
			
			throw new AssertionError(turn); // if it gets this far, than there is a mistake somewhere in the code
		}
		/*System.out.println("right: " + this.distanceToObstacle(Direction.RIGHT));
		System.out.println("left: " + this.distanceToObstacle(Direction.LEFT));
		System.out.println("straight: " + this.distanceToObstacle(Direction.FORWARD));
		System.out.println("back: " + this.distanceToObstacle(Direction.BACKWARD));
		System.out.println("seeExit: " + this.canSeeExit(Direction.FORWARD));
		System.out.println("atExit: " + this.isAtExit());
		this.setBatteryLevel(this.getBatteryLevel() + 5); // have to add 5 to the battery since battery is lost in print statements
		
		assertTrue(tempBat == this.getBatteryLevel());
		assertTrue(odo == this.getOdometerReading());*/
	}


	/**
	 * this will rotate the robot Left
	 * @param tempBat the battery of the robot after the movement
	 * @param odo the current pathLenght of the robot
	 */
	private void turnLeft(float tempBat, int odo) {
		if(batLev >= 3) {
			this.setBatteryLevel(batLev - 3);
			control.keyDown(UserInput.Left,0);
			//assertTrue(tempBat == this.getBatteryLevel());
			//assertTrue(odo == this.getOdometerReading());
			//System.out.println("leftclick: "+ this.getCurrentDirection());

		}
		else if(batLev < 3) {
			cantMove = true;
			//control.batErrorScreen();
			//batteryErrorScreen();
		}
	}


	/**
	 * this will rotate the robot Right
	 * @param tempBat the battery of the robot after the movement
	 * @param odo the current pathLenght of the robot
	 */
	private void turnRight(float tempBat, int odo) {
		if(batLev >= 3) {
			this.setBatteryLevel(batLev - 3);
			control.keyDown(UserInput.Right,0);
			//assertTrue(tempBat == this.getBatteryLevel());
			//assertTrue(odo == this.getOdometerReading());
			//System.out.println("rightclick: " + this.getCurrentDirection());
			/*System.out.println(this.distanceToObstacle(Direction.RIGHT));
			System.out.println(this.distanceToObstacle(Direction.LEFT));
			System.out.println(this.distanceToObstacle(Direction.FORWARD));
			System.out.println(this.distanceToObstacle(Direction.BACKWARD));
			System.out.println(this.canSeeExit(Direction.FORWARD));
			System.out.println(this.isAtExit());*/ 
			// used these to test out the assert statements and fix bugs in program, 
			// also to check to see the methods were printing out expected outcomes

		}
		else if(batLev < 3) {
			cantMove = true;
			//control.batErrorScreen();
			//batteryErrorScreen();
		}
	}


	/**
	 * this will rotate the robot Right
	 * @param tempBat the battery of the robot after the movement
	 * @param odo the current pathLenght of the robot
	 */
	private void turnAround(float tempBat, int odo) {
		if(batLev >= 6) {
			
			this.setBatteryLevel(batLev - 6);
			control.keyDown(UserInput.Right,0);
			control.keyDown(UserInput.Right,0);
			//assertTrue(tempBat - 3 == this.getBatteryLevel());
			//assertTrue(odo == this.getOdometerReading());

		}
		else if(batLev < 6) {
			cantMove = true;
			//control.batErrorScreen();
			//batteryErrorScreen();
		}
	}
	
	/**
	 * this will move the robot by using the controller reference 
	 * in order for it to move the robot in the maze which is held
	 * in the control
	 * @param distance, the amount the robot needs to move
	 * @param manual, if the robot is being moved by the driver or not
	 */
	@Override
	public void move(int distance, boolean manual) {
		
		
		if (manual == true && batLev >= 5) {
			distance = 1;
			//System.out.println("one if");
		}
		float bat = batLev;// keeps track of the battery level before the if statement
		if (batLev >= 5) {
			//System.out.println("second if");
			moveRobot(distance, bat);
			cantMove = false;
			return;
		}
		else if (batLev < 5) {
			cantMove = true;
			//assertTrue(this.getBatteryLevel() == bat);// this is too make sure that no battery was lost due to a movement that was not possible
			//assertTrue(hasStopped());
			//control.batErrorScreen();
			//batteryErrorScreen();
			return;
		}
		else {
			//assertTrue(this.getBatteryLevel() == bat);// this is too make sure that no battery was lost due to a movement that was not possible
			cantMove = true;
			//assertTrue(hasStopped());
			return;
		}
	}


	/**
	 * this will move the robot the desired distance and makes sure the correct amount of battery is used
	 * @param distance, how much the robot will move
	 * @param bat, the battery the robot should be after the movement
	 * @return
	 */
	private void moveRobot(int distance, float bat) {
		while(distance > 0) {
			int odo = this.getOdometerReading();
			//System.out.println("enter while");
			this.setBatteryLevel(bat + 1);
			//System.out.println(this.distanceToObstacle(Direction.FORWARD));
			if (this.distanceToObstacle(Direction.FORWARD) > 0 && batLev >= 5) {// checks to see if a move is possible
				//System.out.println("gets in");
				// using the temp variable to set the battery level correctly
				this.setBatteryLevel(bat - 5);
				odometer++; // increase the odometer by 1
				cantMove = false;
				//assertFalse(this.getBatteryLevel() == bat); // this is too check that the battery is not the same after the movement
				//assertTrue(this.getBatteryLevel() == bat - 5); // this is to check that the battery is the exact level we want it to be
				//assertFalse(hasStopped());// making sure cantMove is false since the robot has not stopped
				//assertTrue(odo + 1 == this.getOdometerReading()); // making sure that the odometer is updated properly
				control.keyDown(UserInput.Up, 0); // calls on the control to make the move
				distance--;
				//System.out.println(this.isInsideRoom());
				//System.out.println(this.isAtExit());
				//System.out.println(this.distanceToObstacle(Direction.FORWARD));

			}
			else if (batLev < 5) {
				cantMove = true;
				//assertTrue(this.getBatteryLevel() == bat);// this is too make sure that no battery was lost due to a movement that was not possible
				//assertTrue(hasStopped());
				//assertTrue(odo == this.getOdometerReading()); // make sure the odometer was not incremented in any way
				//control.batErrorScreen();
				//batteryErrorScreen();
			}
			else {
				//this.setBatteryLevel(bat);// so the robot does not lose any battery when no move is made 
				cantMove = true;
				//assertTrue(hasStopped());
				//assertTrue(odo == this.getOdometerReading());// make sure the odometer was not incremented in any way
				//assertTrue(this.getBatteryLevel() == bat); // this is too make sure that no battery was lost due to a movement that was not possible
				distance--;
			}
		}
	}
	
	
	/**
	 * this returns the current in the form of an array
	 */
	@Override
	public int[] getCurrentPosition() throws Exception {

		return control.getCurrentPosition();
	}

	/**
	 * sets the robot to be controlled by the controller that is being
	 * passed by the mazeapplication
	 */
	@Override
	public void setMaze(StatePlaying controller) {
		//assertNotNull(controller);// makes sure that an actual controller is being passsed in, or else this will cause many unwanted problems in the program when trying to play

		control = controller;
		//resetMazeVariables();
		// setting the controller variable in the file with the one being set
	}
	
	/**
	 * this will return the control of the maze
	 * @return the control of the maze
	 */
	public StatePlaying getMaze() {
		return control;
	}

	
	/**
	 * this will @return true is the robot is at an exit position
	 * @return false otherwise
	 */

	@Override
	public boolean isAtExit() {
		//if (cell == null) {
		float bat = this.getBatteryLevel();
		cell = control.getMazeConfiguration().getMazecells();
		//assertNotNull(cell); // avoids the problem of finding the null pointer in the program
		//assertTrue(batLev == bat); // making sure this method does not use up any energy
		//}
		return cell.isExitPosition(control.getCurrentPosition()[0], control.getCurrentPosition()[1]); // using method stub in cells that returns a boolean to check if the current position of the robot is an exit position
	}

	/**
	 * This method will check to see if the exit is seen at the exit specified
	 * @return true if the robot can see it, false otherwise
	 */
	@Override
	public boolean canSeeExit(Direction direction) throws UnsupportedOperationException {
		// this if statements checks to make sure the correct number of power is available and that the distanceToObstacle returns the correct value that the exit, if visible, should be at
		float tempBat = batLev;
		if (batLev > 0) {
			tempBat = batLev - 1;
		}
		this.setBatteryLevel(batLev + 1);// adding one for the cost that distanceToObstacle uses on battery
		return directionExit(direction, tempBat);
		
	}


	/**
	 * helper method for canSeeExit to see if you can see the exit from the
	 * specified direction
	 * @param direction of where the robot is checking the exit
	 * @param tempBat the battery of the robot after the operation
	 * @return
	 */
	private boolean directionExit(Direction direction, float tempBat) {
		if (batLev >= 1 && distanceToObstacle(direction) == Integer.MAX_VALUE && hasDistanceSensor(direction) == true) {
			// this sets the battery level to one lower than before
			this.setBatteryLevel(tempBat);
			if (batLev < 1) {
				cantMove = true;
				//assertTrue(this.getBatteryLevel() == tempBat);// making sure that only one battery was used up with this operation
				//assertTrue(cantMove);
				//control.batErrorScreen();
				//batteryErrorScreen();
			}
			//assertTrue(this.getBatteryLevel() == tempBat);// making sure that only one battery was used up with this operation

			return true;
		}
		else if(batLev < 1) {
			cantMove = true;
			//assertTrue(hasStopped()); // this would have to be true since the robot would not have battery anymore
			//control.batErrorScreen();
			//batteryErrorScreen();
			return false;
		}
		else {
			this.setBatteryLevel(tempBat);
			//assertTrue(tempBat == this.getBatteryLevel()); // checking to see that the right amount of energy has been taken for using the method
			//cantMove = true;
			return false;
		}
	}
	
	/**
	 * @return true if robot is inside a room
	 * @return false otherwise
	 */
	@Override
	public boolean isInsideRoom() throws UnsupportedOperationException {
		//if (cell == null) {
		float bat = this.getBatteryLevel();
		cell = control.getMazeConfiguration().getMazecells();
		//}
		//assertNotNull(cell); // avoids the problem of finding the null pointer in the program
		if(hasRoomSensor() == true) {
			//assertTrue(bat == batLev);// checking to see that the battery has not been impacted by this method call
			return cell.isInRoom(control.getCurrentPosition()[0], control.getCurrentPosition()[1]); // using method stub in cells that returns a boolean to check if the current position of the robot is in a room
		}
		//assertTrue(bat == batLev);// checking to see that the battery has not been impacted by this method call
		return false;
	}
	
	/**
	 * check if there a roomsensor on the robot
	 */
	@Override
	public boolean hasRoomSensor() {
		float bat = this.getBatteryLevel();
		//assertTrue(bat == batLev); // checking to see that the battery has not been impacted by this method call
		
		return roomSensor; // roomSensor was set to true as a boolean in the constructor, therefore this method stub should return true
	}
	
	
	/**
	 * the current direction of the robot 
	 */
	@Override
	public CardinalDirection getCurrentDirection() {
		float bat = this.getBatteryLevel();
		//assertTrue(bat == batLev); // checking to see that the battery has not been impacted by this method call
		//assertNotNull(control.getCurrentDirection()); // checking to make sure the control gives a direction, if it is null, this will tell where the probelm in the program is

		return control.getCurrentDirection(); // this variable is updated as rotations occur in the rotate method stub. therefore the direction of the robot is given
	}
	
	
	/**
	 * @return the battery level
	 */
	@Override
	public float getBatteryLevel() {

		return batLev; // returns the int val of the battery after various movements 
	}
	
	/**
	 * sets the battery level
	 */
	@Override
	public void setBatteryLevel(float level) {
		
		batLev = level; // setting the battery to a current level given 
	}

	/**
	 * @return the pathLength
	 */
	@Override
	public int getOdometerReading() {
		float bat = this.getBatteryLevel();
		//assertTrue(bat == batLev); // checking to see that the battery has not been impacted by this method call
		return odometer; // this returns how far the robot has traveled thus far
	}

	/**
	 * set the odometer back to 0
	 */
	@Override
	public void resetOdometer() {
		float bat = this.getBatteryLevel();
		//assertTrue(bat == batLev); // checking to see that the battery has not been impacted by this method call
		odometer = 0; // this sets the path length of the robot back to 0
	}

	@Override
	public float getEnergyForFullRotation() {
		
		int energy = 12;

		return energy; // in order for the robot to do a full rotation is must go through all four directions, hence 4 movements that has an energy cost of 3 per movment
	}

	@Override
	public float getEnergyForStepForward() {
		
		int energy = 5;

		return energy; // in order to move the robot needs at least 5 energy in its battery
	}

	@Override
	public boolean hasStopped() {

		//assertTrue(bat == batLev); // checking to see that the battery has not been impacted by this method call
		return cantMove; // this is set to false, until the robots battery has been emptied or there is an obstacle in its current position
	}

	/**
	 * this method will give an integer of how far away a 
	 * wall is in a current direction
	 * @return integer value of the distance to wall
	 */
	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		// this variable of Direction is only used in this method to keep track of the desired direction wanted to be checked and a variable that keeps track of the battery needed
		//if (cell == null) {
		cell = control.getMazeConfiguration().getMazecells();
		//}
		//assertNotNull(cell); // avoids the problem of finding the null pointer in the program
		CardinalDirection tempDir = this.getCurrentDirection();
		//float tempBat = this.getBatteryLevel();
		if (this.hasDistanceSensor(direction) && batLev >= 1) {
			
			this.setBatteryLevel(batLev - 1);
			

			// this switch statement sets the tempDir variable to the correct desired direction
			tempDir = mapDirection(direction, tempDir);

			int tempX = control.getCurrentPosition()[0];
			int tempY = control.getCurrentPosition()[1];
			int tempNum = 0;
			// this will continue to push through the maze until we reach a wall or the actual exit which is what the if statement checks
			while (!false) {
				
				if (tempX >= cell.width || tempY >= cell.height || tempY < 0 || tempX < 0) {
				//if (cell.isExitPosition(tempX, tempY)){ // this was changed because this does not follow through with method stub canSeeExit since it gives a 
					// position is an exit but not that we can see the exit if it to a side in a different direction
					//assertTrue(tempBat - 1 == this.getBatteryLevel());// checks that the battery has only gone down by one during this operation
					return Integer.MAX_VALUE;
				}
				// uses the tempDir variable to find out how far away the next obstacle is 
				// tempNum keeps track of the integer value of the the distance
				// tempX and tempY are updated based on the direction the sensor is checking
				else {
					switch(tempDir) {
					case East:
						if (cell.hasWall(tempX, tempY, CardinalDirection.East)) {
							//assertTrue(tempBat - 1 == this.getBatteryLevel());// checks that the battery has only gone down by one during this operation
							return tempNum;
						}
						tempNum++;
						tempX++;
						break;
						
					case North:
						if (cell.hasWall(tempX, tempY, CardinalDirection.North)) {
							//assertTrue(tempBat - 1 == this.getBatteryLevel());// checks that the battery has only gone down by one during this operation
							return tempNum;
						}
						tempNum++;
						tempY--;
						break;
						
					case South:
						if (cell.hasWall(tempX, tempY, CardinalDirection.South)) {
							//assertTrue(tempBat - 1 == this.getBatteryLevel());// checks that the battery has only gone down by one during this operation
							return tempNum;
						}
						tempNum++;
						tempY++;
						break;
						
						
					case West:
						if (cell.hasWall(tempX, tempY, CardinalDirection.West)) {
							//assertTrue(tempBat - 1 == this.getBatteryLevel());// checks that the battery has only gone down by one during this operation
							return tempNum;
						}
						tempNum++;
						tempX--;
						break;
						
					default:
						throw new AssertionError(tempDir); // I expect that tempDir to follow one of the cardinal directions, this is just making sure that if it doesn't an error be thrown
					// saying what went wrong and where it occured
						
					}
				}
			}
		}
		else {
			if (batLev < 1) {
				//assertTrue(tempBat == this.getBatteryLevel());// no battery was used during this method
				//control.batErrorScreen();
				//batteryErrorScreen();
				cantMove = true;
			}
			//assertTrue(tempBat == this.getBatteryLevel());// no battery was used during this method 
			throw new UnsupportedOperationException();
		}
	}
	/**
	 * this sets variable tempDir to the direction to which the sensor is pointing toward
	 * @param direction desire direction we want
	 * @param tempDir the current direction of the robot
	 * @return tempDir pointing toward direction
	 */
	private CardinalDirection mapDirection(Direction direction, CardinalDirection tempDir) {
		switch(direction) {
		
		case BACKWARD:
			//tempDir = this.getCurrentDirection();
			tempDir = tempDir.oppositeDirection();
			break;
		case FORWARD:
			//tempDir = this.getCurrentDirection();
			break;
		case RIGHT: // MIGHT NEED TO SWITCH LEFT AND RIGHT MAYBE
			//tempDir = this.getCurrentDirection();
			tempDir = tempDir.oppositeDirection();
			tempDir = tempDir.rotateClockwise();
			break;
		case LEFT:
			//tempDir = this.getCurrentDirection();
			tempDir = tempDir.rotateClockwise();
			break;
		default:
			throw new AssertionError(direction); // this does similar work as the asset statement abbove 
			
		}
		return tempDir;
	}
 
	/**
	 * this will return true if the distance sensors have been initialized correctly
	 */
	@Override
	public boolean hasDistanceSensor(Direction direction) {
		
		float temp = this.getBatteryLevel();
		
		// each if statement will return true since in the constructor all the booleans were given a value of true
		if (direction == Direction.BACKWARD) {
			//assertTrue(temp == this.getBatteryLevel()); // the battery should be the same after the operation
			return this.backwardSensor;
		}
		
		else if (direction == Direction.FORWARD) {
			//assertTrue(temp == this.getBatteryLevel()); // the battery should be the same after the operation
			return this.striaghtSensor;
		}
		else if (direction == Direction.LEFT) {
			//assertTrue(temp == this.getBatteryLevel()); // the battery should be the same after the operation
			return this.leftSensor;
		}
		else if (direction == Direction.RIGHT) {
			//assertTrue(temp == this.getBatteryLevel()); // the battery should be the same after the operation
			return this.rightSensor;
		}
		else {
			//assertTrue(temp == this.getBatteryLevel()); // the battery should be the same after the operation
			throw new AssertionError(direction); // if some other parameter makes into the method and it is not one of the ones that are used in the program this will throw the assertion 
		// error to notify the programmer
		}
	}
	
}