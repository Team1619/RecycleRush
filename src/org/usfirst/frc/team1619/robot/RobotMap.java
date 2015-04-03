package org.usfirst.frc.team1619.robot;

import org.usfirst.frc.team1619.KeyCodes;

import edu.wpi.first.wpilibj.CANTalon;

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
	public enum MotorDefinition {
		leftDriveMotor1(1, KeyCodes.VK_Q, KeyCodes.VK_A, 0.5, -0.5),
		leftDriveMotor2(2, KeyCodes.VK_W, KeyCodes.VK_S, 0.5, -0.5),
		rightDriveMotor1(3, KeyCodes.VK_E, KeyCodes.VK_D, 0.5, -0.5),
		rightDriveMotor2(4, KeyCodes.VK_R, KeyCodes.VK_F, 0.5, -0.5),
		rakerMotor(5, KeyCodes.VK_T, KeyCodes.VK_G, 0.5, -0.5),
		tilterMotor(6, KeyCodes.VK_Y, KeyCodes.VK_H, 0.5, -0.5),
		conveyorMotor(7, KeyCodes.VK_U, KeyCodes.VK_J, 0.5, -0.5),
		guardRailMotor(8, KeyCodes.VK_I, KeyCodes.VK_K, 0.5, -0.5),
		binElevatorMotor(9, KeyCodes.VK_O, KeyCodes.VK_L, 0.5, -0.5),
		binGripMotor(10, KeyCodes.VK_Z, KeyCodes.VK_X, 0.5, -0.5),
		toteElevatorMotor(11, KeyCodes.VK_C, KeyCodes.VK_V, 0.5, -0.5),
		toteElevatorMotorSmall(12, KeyCodes.VK_B, KeyCodes.VK_N, 0.5, -0.5),
		;
		
		public final int forwardKey;
		public final int reverseKey;
		public final double forwardKeySpeed;
		public final double reverseKeySpeed;
		public final int id;
		
		public CANTalon motor = null;
		
		public void initMotor() {
			if(motor != null)
				return;
			motor = new CANTalon(id);
		}
		
		public CANTalon getMotor() {
			return motor;
		}
		
		MotorDefinition(int id, int forwardKey, int reverseKey,
				double forwardKeySpeed, double reverseKeySpeed) {
			this.id = id;
			this.forwardKey = forwardKey;
			this.reverseKey = reverseKey;
			this.forwardKeySpeed = forwardKeySpeed;
			this.reverseKeySpeed = reverseKeySpeed;
		}
		
		static void initMotors() {
			for(MotorDefinition md : values())
				md.initMotor();
		}
	}
	
	public final static int frontConveyorOpticalSensorID = 0;
	public final static int rearConveyorOpticalSensorID = 1;

	public static final int rightStickID = 0;
	public static final int leftStickID = 1;
	
	public static final int rainbowSTORMPort = 0x666;

	/**
	 * Button Identification
	 */
	//Right stick
	public static final int resetGyroButtonID = 7;
	public static final int resetDriveEncodersButtonID = 8;

	public static final int liftAbortButtonID = 4;
	public static final int liftResetButtonID = 3;	

	public static final int startHumanFeedButtonID = 1;
	public static final int stopHumanFeedButtonID = 2; //last tote on conveyor or ground (before tote elevator has active conrtol of tote)
	
	public static final int incrementNumberTotesButtonID = 11;
	
	public static final int toteElevatorUpManualButtonID = 7;
	public static final int toteElevatorDownManualButtonID = 6;
	
	public static final int lowerToteElevatorAndOpenClawButtonID = 5;
	public static final int driverCloseClawButtonID = 10;


	//Left Stick
	public static final int guardrailCloseManualButtonID = 9;
	public static final int guardrailOpenManualButtonID = 8;
	
	public static final int coneyorFowardButtonID = 4;
	public static final int coneyorBackButtonID = 3;

	
	public static final int rakerDownManualButtonID = 6;
	public static final int rakerUpManualButtonID = 7;
	public static final int closeClawButtonID = 1;
	public static final int openClawButtonID = 2;
	

	//other stuff
	public static final int gyroTempAnalogID = 1;//temperature - analog 1
	public static final int gyroRateAnalogID = 0;//rate - analog 2
}
