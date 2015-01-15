package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.Robot;
import org.usfirst.frc.team1619.robot.subsystems.GyroSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ResetGyroCommand extends Command {
	
	private GyroSubsystem gyroSubsystem;
	
    public ResetGyroCommand() {
        // Use requires() here to declare subsystem dependencies
    	gyroSubsystem = Robot.getRobot().gyroSubsystem;
    	requires(gyroSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	gyroSubsystem.reset();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
