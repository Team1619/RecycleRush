package org.usfirst.frc.team1619.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	
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
	public static final int kachigLeftButtonID = 4;
	public static final int kachigRightButtonID = 3;
	public static final int resetGyroButtonID = 7;
	public static final int toteElevatorUpManualButtonID = 5;
	public static final int toteElevatorDownManualButtonID = 6;
	public static final int resetEncoderButtonID = 8;
	public static final int driveForwardButtonID = 9;
	public static final int turnButtonID = 10;
	public static final int liftAbortButtonID = 11;
	public static final int liftResetButtonID = 12;
	public static final int calibrateGyroButton = 2;
	public static final int unloadConveyorButtonID = 16;
	
	//Left Stick
	public static final int conveyorForwardManualButtonID = 4;
	public static final int conveyorBackwardManualButtonID = 3;
	public static final int guardrailCloseManualButtonID = 13;
	public static final int guardrailOpenManualButtonID = 14;
	public static final int binElevatorDownManualButtonID = 8;
	public static final int binElevatorUpManualButtonID = 7;
	public static final int binTiltUpManualButtonID = 9;
	public static final int binTiltDownManualButtonID = 6;
	public static final int binGripOpenManualButtonID = 10;
	public static final int binGripCloseManualButtonID = 5;
	public static final int rakerOpenManualButtonID = 11;
	public static final int rakerCloseManualButtonID = 16;
	public static final int unstickToteButtonID = 2;
	
		
	public static final int gyroTempAnalogID = 1;//temperature - analog 1
	public static final int gyroRateAnalogID = 0;//rate - analog 2
	
	
	
	//public static final int opticalSensorID = 1;
		
	//public static final int sonarAnalogInputID = 0;
	
	//public static final int switchSubsystemID = 0;
	
	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
}
