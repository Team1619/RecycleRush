package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.robot.subsystems.UBinElevatorSystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class UClawsOpen extends Command {

	private Timer timer;
	private double driveTime;

	public UClawsOpen(double time) {
		// Use requires() here to declare subsystem dependencies
		// requires(BinElevatorSystem.getInstance());
		driveTime = time;
		timer = new Timer();
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		timer.start();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		UBinElevatorSystem.getInstance().setBinGrip(
				UBinElevatorSystem.kBinGripOpenSpeed);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return timer.get() > driveTime;
	}

	// Called once after isFinished returns true
	protected void end() {
		UBinElevatorSystem.getInstance().setBinGrip(0.0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
