package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.Robot;
import org.usfirst.frc.team1619.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class LinearDriveCommand extends Command {
	public static final double kMoveForwardDistance = 3.0; //in meters
	
	private Drivetrain drivetrain;
	private double distance; 
	private double leftStartVal;
	private double rightStartVal;
	
    public LinearDriveCommand(double moveDistance) {
        // Use requires() here to declare subsystem dependencies
    	drivetrain = Robot.getRobot().drivetrain;
        requires(drivetrain);
        distance = moveDistance;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	leftStartVal = drivetrain.getLeftEncoderPosition();
    	rightStartVal = drivetrain.getRightEncoderPosition();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	drivetrain.drive(0.5, 0.0);
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
