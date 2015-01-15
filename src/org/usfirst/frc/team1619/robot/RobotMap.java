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

	//public static final int bearClawMotorID = 1;
	//public static final int conveyorMotorID = 2;

	public final static int leftMotor = 0;
	public final static int rightMotor = 1;
    
    public static final int rightStickID = 1;
    public static final int leftStickID = 0;
    
    public static final int kachigLeftButtonID = 4;
    public static final int kachigRightButtonID = 3;
    public static final int resetGyroButtonID = 6;

    public static final int gyroID = 0;
    
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static int rangefinderPort = 1;
    // public static int rangefinderModule = 1;
}
