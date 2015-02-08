package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.subsystems.GyroSystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ResetGyroCommand extends Command {
	
	private GyroSystem gyro;
	
    public ResetGyroCommand() {
        // Use requires() here to declare subsystem dependencies
    	//gyroSubsystem = Robot.getRobot().gyroSubsystem;
    	gyro = GyroSystem.getInstance();
    	requires(gyro);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	gyro.resetGyro();
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
