package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.subsystems.UDrivetrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ULinearDriveCommand extends Command {
	public static final double kDriveSpeed = 0.5;
	public static final double kPValue = 4.0;

	private UDrivetrain drivetrain;
	private double distance;
	private double speed;
	private double leftStartVal;
	private double rightStartVal;
	private double leftChangeVal;
	private double rightChangeVal;

	private double turnVal;

	void init() {
		drivetrain = UDrivetrain.getInstance();
		requires(drivetrain);
		turnVal = 0.0;
		setInterruptible(true);

	}

	public ULinearDriveCommand(double moveDistance) {
		speed = kDriveSpeed;
		distance = moveDistance;
		init();
	}

	/**
	 * speed should always be positive, moveDistance determines direction of
	 * driving.
	 * 
	 * @param moveDistance
	 * @param speed
	 */
	public ULinearDriveCommand(double moveDistance, double speed) {
		this.speed = speed;
		distance = moveDistance;
		init();
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		leftStartVal = drivetrain.getLeftEncoderPosition();
		rightStartVal = drivetrain.getRightEncoderPosition();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		leftChangeVal = drivetrain.getLeftEncoderPosition() - leftStartVal;
		rightChangeVal = drivetrain.getRightEncoderPosition() - rightStartVal;

		if (leftChangeVal != rightChangeVal) {
			turnVal = (rightChangeVal - leftChangeVal) * kPValue;
		}
		else {
			turnVal = 0;
		}
		drivetrain.drive(speed * Math.signum(distance), turnVal);
		// System.out.println(turnVal);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return Math.abs((leftChangeVal + rightChangeVal) / 2) >= Math
				.abs(distance);
	}

	// Called once after isFinished returns true
	protected void end() {
		drivetrain.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		drivetrain.stop();
	}
}
