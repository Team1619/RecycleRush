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
	
	public static final int bearClawMotorID = 1;
	public static final int conveyorMotorID = 2;
	
	public final static int leftDriveMotor1 = 1;
	public final static int leftDriveMotor2 = 4;
	public final static int rightDriveMotor1 = 3;
	public final static int rightDriveMotor2 = 5;
	
	public static final int rightStickID = 0;
	public static final int leftStickID = 1;
	
	//Right stick
	public static final int kachigLeftButtonID = 4;
	public static final int kachigRightButtonID = 3;
	public static final int resetGyroButtonID = 7;
	public static final int toteElevatorUpManualButtonID = 5;
	public static final int toteElevatorDownManualButtonID = 6;
	
	//Left Stick
	public static final int conveyorForwardManualButtonID = 4;
	public static final int conveyorBackwardManualButtonID = 3;
	public static final int guardrailCloseManualButtonID = 13;
	public static final int guardrailOpenManualButtonID = 14;
	public static final int binElevatorUpManualButtonID = 8;
	public static final int binElevatorDownManualButtonID = 7;
	public static final int binTiltUpManualButtonID = 9;
	public static final int binTiltDownManualButtonID = 6;
	public static final int binGripOpenManualButtonID = 10;
	public static final int binGripCloseManualButtonID = 5;
	
	
	public static final int OpticalSensorID = 1;
	
	public static final int motorSystemID = 2;
	
	public static final int sonarAnalogInputID = 0;
	
	public static final int switchSubsystemID = 9;
	
	public static final int gyroTempAnalogID = 2;//temperature - analog 1
	public static final int gyroRateAnalogID = 1;//rate - analog 2
	
	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
}
