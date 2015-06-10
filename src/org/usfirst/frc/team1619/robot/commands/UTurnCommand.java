package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.subsystems.UDrivetrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class UTurnCommand extends Command {
	public static final double kTurnRadius = (26 * 0.0254) / 2.0; // Meters
	public static final double kDriveSpeed = 0.5;

	private UDrivetrain drivetrain;
	private double distance;
	private double leftStartVal;
	private double rightStartVal;

	public UTurnCommand(double turnAngle) {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		drivetrain = UDrivetrain.getInstance();
		requires(drivetrain);
		distance = turnAngle * kTurnRadius * Math.PI / 180;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		leftStartVal = drivetrain.getLeftEncoderPosition();
		rightStartVal = drivetrain.getRightEncoderPosition();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (distance > 0)
			drivetrain.drive(0.0, kDriveSpeed);
		else
			drivetrain.drive(0.0, -kDriveSpeed);
	}

	protected double getLeftDistance() {
		return drivetrain.getLeftEncoderPosition() - leftStartVal;
	}

	protected double getRightDistance() {
		return drivetrain.getRightEncoderPosition() - rightStartVal;
	}

	protected double getAngle() {
		return getTurnDistance() / kTurnRadius;
	}

	protected double getTurnDistance() {
		return (getLeftDistance() - getRightDistance()) / 2;
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		// Robot.getRobot().smashboard.write("Left turn distance",
		// getLeftDistance());
		// Robot.getRobot().smashboard.write("Right turn distance",
		// getRightDistance());
		// SmartDashboard.putNumber("Turn angle", getAngle());
		// SmartDashboard.putNumber("Turn distance", getTurnDistance());

		return getTurnDistance() > distance;
	}

	// Called once after isFinished returns true
	protected void end() {
		drivetrain.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
