package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.robot.subsystems.UBinElevatorSystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class UArmUp extends Command {

	double speed;
	private Timer timer = new Timer();
	private double time;
	private boolean fullUp = false;

	public UArmUp(double speed, double time) {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		this.speed = speed;
		requires(UBinElevatorSystem.getInstance());
		this.time = time;
		fullUp = false;
	}

	public UArmUp(double speed) {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		this.speed = speed;
		requires(UBinElevatorSystem.getInstance());
		fullUp = true;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		timer.stop();
		timer.reset();
		timer.start();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		UBinElevatorSystem.getInstance().setBinTilt(speed);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		if (fullUp) {
			return (UBinElevatorSystem.getInstance().getTilterBackLimitSwitch());
		}
		else {
			return timer.get() > time;
		}
	}

	// Called once after isFinished returns true
	protected void end() {
		UBinElevatorSystem.getInstance().setBinTilt(0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
