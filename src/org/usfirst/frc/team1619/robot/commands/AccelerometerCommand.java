package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.Robot;
import org.usfirst.frc.team1619.robot.subsystems.Accelerometer;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AccelerometerCommand extends Command {
	private Accelerometer accelerometer;

    public AccelerometerCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	accelerometer = Robot.getRobot().accelerometer;
    	requires(accelerometer);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	accelerometer.operatorControl();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}