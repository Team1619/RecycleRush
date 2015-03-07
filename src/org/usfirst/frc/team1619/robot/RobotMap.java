package org.usfirst.frc.team1619.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	/**
	 * Motor Identification
	 */
	public final static int leftDriveMotor1 = 1;
	public final static int leftDriveMotor2 = 2;
	public final static int rightDriveMotor1 = 3;
	public final static int rightDriveMotor2 = 4;

	public final static int frontConveyorOpticalSensorID = 0;
	public final static int rearConveyorOpticalSensorID = 1;

	public final static int toteElevatorMotor = 11;
	public final static int tilterMotor = 6;
	public final static int conveyorMotor = 7;
	public final static int guardRailMotor = 8;
	public final static int binElevatorMotor = 9;
	public final static int binGripMotor = 10;
	public final static int rakerMotor = 5;
	public final static int toteElevatorMotorSmall = 12;

	public static final int rightStickID = 0;
	public static final int leftStickID = 1;

	/**
	 * Button Identification
	 */
	//Right stick
	public static final int resetGyroButtonID = 7;
	public static final int resetDriveEncodersButtonID = 8;

	public static final int liftAbortButtonID = 11;
	public static final int liftResetButtonID = 12;	

	public static final int startHumanFeedButtonID = 16;
	public static final int stopHumanFeedButtonID = 15; //last tote on conveyor or ground (before tote elevator has active conrtol of tote)
	
	public static final int incrementNumberTotesButtonID = 2;
	public static final int pickUpToteButtonID = 14;

	//Left Stick
	public static final int guardrailCloseManualButtonID = 13;
	public static final int guardrailOpenManualButtonID = 14;
	
	public static final int coneyorFowardButtonID = 12;
	public static final int coneyorBackButtonID = 15;

	public static final int toteElevatorManualButtonID = 8;
	
	public static final int binTiltManualButtonID = 6;
	public static final int rakerOpenManualButtonID = 11;
	public static final int rakerCloseManualButtonID = 16;
	public static final int closeClawButtonID = 1;
	public static final int openClawButtonID = 3;
	public static final int moveClawForBinPickupButtonID = 4;
	public static final int moveClawForNoodleInsertionButtonID = 2;
	

	//other stuff
	public static final int gyroTempAnalogID = 1;//temperature - analog 1
	public static final int gyroRateAnalogID = 0;//rate - analog 2
}
