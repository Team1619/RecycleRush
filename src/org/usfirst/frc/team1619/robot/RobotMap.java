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
    public static final int leftDriveMotor1ID = 0;
	public static final int leftDriveMotor2ID = 0;
	public static final int rightDriveMotor1ID = 0;
    public static final int rightDriveMotor2ID = 0;
    
    public static final int rightStickID = 0;
    public static final int leftStickID = 0;
    
    public static final int kachigLeftButtonID = 4;
    public static final int kachigRightButtonID = 3;
    
	
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static int rangefinderPort = 1;
    // public static int rangefinderModule = 1;
}
