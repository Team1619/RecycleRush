package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.URobotMap;
import org.usfirst.frc.team1619.robot.commands.UManualDriveCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class UDrivetrain extends Subsystem {
	public static final double kWheelDiameter = 6 * (2.54 / 100); // in meters
	public static final int kPulsesPerRev = 512;
	public static final double kDistancePerPulse = kWheelDiameter * Math.PI
			/ kPulsesPerRev;
	public static final double kCorrection = 1.175 / 1.00;
	public static final double kTwistScalar = 1.0;

	private RobotDrive drive;
	private CANTalon leftMotor1;
	private CANTalon leftMotor2;
	private CANTalon rightMotor1;
	private CANTalon rightMotor2;

	private static UDrivetrain theSystem = new UDrivetrain();

	public static UDrivetrain getInstance() {
		return theSystem;
	}

	private UDrivetrain() {
		leftMotor1 = URobotMap.MotorDefinition.leftDriveMotor1.getMotor();
		leftMotor2 = URobotMap.MotorDefinition.leftDriveMotor2.getMotor();
		rightMotor1 = URobotMap.MotorDefinition.rightDriveMotor1.getMotor();
		rightMotor2 = URobotMap.MotorDefinition.rightDriveMotor2.getMotor();

		drive = new RobotDrive(leftMotor1, leftMotor2, rightMotor1, rightMotor2);
		drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);

		// drive motor parameters, no limit switches and coasting
		leftMotor1.enableLimitSwitch(false, false);
		leftMotor2.enableLimitSwitch(false, false);
		rightMotor1.enableLimitSwitch(false, false);
		rightMotor2.enableLimitSwitch(false, false);
		leftMotor1.enableBrakeMode(true);
		leftMotor2.enableBrakeMode(true);
		rightMotor1.enableBrakeMode(true);
		rightMotor2.enableBrakeMode(true);

		leftMotor1.setPosition(0.0);
		rightMotor1.setPosition(0.0);
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new UManualDriveCommand());
	}

	public void drive(GenericHID inputDevice) {
		drive.arcadeDrive(inputDevice.getY(), inputDevice.getTwist()
				* kTwistScalar);
	}

	/**
	 * Arcade drive with linearVal being like the linear input on a joystick,
	 * and rotateVal being the twist value. Both -1.0 to 1.0.
	 * 
	 * @param linearVal
	 * @param rotateVal
	 */
	public void drive(double linearVal, double rotateVal) {
		drive.arcadeDrive(-linearVal, rotateVal);
	}

	public void stop() {
		drive.stopMotor();
	}

	/**
	 * sends signal from wheels to encoder to get position, number of clicks
	 * 
	 * @return
	 */
	public double getLeftEncoderPosition() {
		return -kDistancePerPulse * leftMotor1.getEncPosition() * kCorrection;
	}

	public double getRightEncoderPosition() {
		return kDistancePerPulse * rightMotor1.getEncPosition() * kCorrection;
	}

	public int getRawRightEncoderPosition() {
		return rightMotor1.getEncPosition();
	}

	public int getRawLeftEncoderPosition() {
		return leftMotor1.getEncPosition();
	}

	public void resetEncoders() {
		leftMotor1.setPosition(0.0);
		rightMotor1.setPosition(0.0);
	}

	public void setBrakeMode(boolean enabled) {
		leftMotor1.enableBrakeMode(enabled);
		rightMotor1.enableBrakeMode(enabled);
		leftMotor2.enableBrakeMode(enabled);
		rightMotor2.enableBrakeMode(enabled);
	}

	public void autoMode() {
		setBrakeMode(true);
	}

	public void teleopMode() {
		setBrakeMode(true);

	}
}
