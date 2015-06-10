package org.usfirst.frc.team1619.robot;

import org.usfirst.frc.team1619.UKeyCodes;

import edu.wpi.first.wpilibj.CANTalon;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class URobotMap {
	/**
	 * Motor Identification. Includes: The CANBus ID associated with the motor,
	 * Keys to be pressed for individual motor testing (forward and reverse),
	 * Speeds of motor for each key being pressed.
	 */
	public enum MotorDefinition {
		leftDriveMotor1(1, UKeyCodes.VK_Q, UKeyCodes.VK_A, 0.5, -0.5),
		leftDriveMotor2(2, UKeyCodes.VK_W, UKeyCodes.VK_S, 0.5, -0.5),
		rightDriveMotor1(3, UKeyCodes.VK_E, UKeyCodes.VK_D, 0.5, -0.5),
		rightDriveMotor2(4, UKeyCodes.VK_R, UKeyCodes.VK_F, 0.5, -0.5),
		rakerMotor(5, UKeyCodes.VK_T, UKeyCodes.VK_G, 0.5, -0.5),
		tilterMotor(6, UKeyCodes.VK_Y, UKeyCodes.VK_H, 0.5, -0.5),
		conveyorMotor(7, UKeyCodes.VK_U, UKeyCodes.VK_J, 0.5, -0.5),
		guardRailMotor(8, UKeyCodes.VK_I, UKeyCodes.VK_K, 0.5, -0.5),
		binElevatorMotor(9, UKeyCodes.VK_O, UKeyCodes.VK_L, 0.5, -0.5),
		binGripMotor(10, UKeyCodes.VK_Z, UKeyCodes.VK_X, 0.5, -0.5),
		toteElevatorMotor(11, UKeyCodes.VK_C, UKeyCodes.VK_V, 0.5, -0.5),
		toteElevatorMotorSmall(12, UKeyCodes.VK_B, UKeyCodes.VK_N, 0.5, -0.5);

		public final int id;
		public final int forwardKey;
		public final int reverseKey;
		public final double forwardKeySpeed;
		public final double reverseKeySpeed;

		public CANTalon motor = null;

		MotorDefinition(int id, int forwardKey, int reverseKey,
				double forwardKeySpeed, double reverseKeySpeed) {
			this.id = id;
			this.forwardKey = forwardKey;
			this.reverseKey = reverseKey;
			this.forwardKeySpeed = forwardKeySpeed;
			this.reverseKeySpeed = reverseKeySpeed;
		}

		/**
		 * Associates an actual motor with the enum based on the CANBus ID
		 * provided for each enum
		 */
		public void initMotor() {
			if (motor != null)
				return;
			motor = new CANTalon(id);
		}

		/**
		 * @return the motor associated with the enum
		 */
		public CANTalon getMotor() {
			return motor;
		}

		/**
		 * Calls the initMotor() function to associate a motor with each enum
		 */
		static void initMotors() {
			for (MotorDefinition md : values())
				md.initMotor();
		}
	}

	// Port for keyboard input to motors
	public static final int RAINBOW_STORM_PORT = 0x666;

	// Optical sensor IDs
	public final static int FRONT_CONVEYOR_OPTICAL_SENSOR_ID = 0;
	public final static int REAR_CONVEYOR_OPTICAL_SENSOR_ID = 1;

	// Joystick IDs
	public static final int RIGHT_STICK_ID = 0;
	public static final int LEFT_STICK_ID = 1;

	// Button IDs
	// Right stick
	public static final int RESET_DRIVE_ENCODERS_BUTTON_ID = 8;

	public static final int LIFT_ABORT_BUTTON_ID = 4;
	public static final int LIFT_RESET_BUTTON_ID = 3;

	public static final int START_HUMAN_FEED_BUTTON_ID = 1;
	public static final int STOP_HUMAN_FEED_BUTTON_ID = 2;

	public static final int INCREMENT_NUMBER_TOTES_BUTTON_ID = 11;

	public static final int TOTE_ELEVATOR_UP_MANUAL_BUTTON_ID = 7;
	public static final int TOTE_ELEVATOR_DOWN_MANUAL_BUTTON_ID = 6;

	public static final int LOWER_TOTE_ELEVATOR_AND_OPEN_CLAW_BUTTON_ID = 5;
	public static final int DRIVER_CLOSE_CLAW_BUTTON_ID = 10;

	// Left Stick
	public static final int GUARDRAIL_CLOSE_MANUAL_BUTTON_ID = 9;
	public static final int GUARDRAIL_OPEN_MANUAL_BUTTON_ID = 8;

	public static final int CONEYOR_FOWARD_BUTTON_ID = 4;
	public static final int CONVEYOR_BACK_BUTTON_ID = 3;

	public static final int RAKER_DOWN_MANUAL_BUTTON_ID = 6;
	public static final int RAKER_UP_MANUAL_BUTTON_ID = 7;

	public static final int CLOSE_CLAW_BUTTON_ID = 1;
	public static final int OPEN_CLAW_BUTTON_ID = 2;
}
