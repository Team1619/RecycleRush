package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.Robot;
import org.usfirst.frc.team1619.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class LinearDriveCommand extends Command {
	public static final double kMoveForwardDistance = 3.0; //in meters	
	
	public static final double kDriveSpeed = 0.5;

	
	private Drivetrain drivetrain;
	private double distance; 
	private double leftStartVal;
	private double rightStartVal;
	private double leftChangeVal;
	private double rightChangeVal;

	private double turnVal;
	
    public LinearDriveCommand(double moveDistance) {
        // Use requires() here to declare subsystem dependencies
    	drivetrain = Robot.getRobot().drivetrain;
        requires(drivetrain);
        distance = moveDistance;
        turnVal = 0.0;
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
    	
    	if(leftChangeVal != rightChangeVal) {
    		turnVal = (rightChangeVal - leftChangeVal);
    	}
    	
    	else {
    		turnVal = 0;
    	}
    	
    	drivetrain.drive(kDriveSpeed * (distance/Math.abs(distance)), turnVal);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (leftChangeVal + rightChangeVal)/2 >= distance;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
