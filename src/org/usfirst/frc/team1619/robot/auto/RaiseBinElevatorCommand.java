package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.robot.subsystems.BinElevatorSystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RaiseBinElevatorCommand extends Command {

    public RaiseBinElevatorCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	BinElevatorSystem.getInstance().setBinElevatorSpeed(-0.3);
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
