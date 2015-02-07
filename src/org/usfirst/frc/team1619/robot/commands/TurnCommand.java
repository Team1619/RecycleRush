package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.Robot;
import org.usfirst.frc.team1619.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TurnCommand extends Command {
	public static final double kTurnRightAngle = Math.PI / 2; //Radians
	public static final double kTurnRadius = 0.3302; //Meters
	
	private Drivetrain drivetrain;
	private double distance;
	private double leftStartVal;
	private double rightStartVal;

    public TurnCommand(double turnAngle) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	drivetrain = Robot.getRobot().drivetrain;
    	requires(drivetrain);
    	distance = turnAngle * kTurnRadius;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	leftStartVal = drivetrain.getLeftEncoderPosition();
    	rightStartVal = drivetrain.getRightEncoderPosition();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	drivetrain.drive(0.0, 0.3);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return ((drivetrain.getLeftEncoderPosition()-leftStartVal) + (drivetrain.getRightEncoderPosition()-rightStartVal))/2 >= distance;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
