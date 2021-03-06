package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.UStateMachine.Signal;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class URaiseSignalCommand extends Command {

	private Signal signal;

	public URaiseSignalCommand(Signal signal) {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		this.signal = signal;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		signal.raise();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
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
