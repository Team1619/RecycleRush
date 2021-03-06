package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.robot.subsystems.URakerSystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class URakerMove extends Command {

	private Timer timer;
	private final double kRakeTime = 1.5;
	private double time;
	private double speed;

	public URakerMove(double speed) {
		time = kRakeTime;
		this.speed = speed;
		setInterruptible(true);
		timer = new Timer();
	}

	public URakerMove(double speed, double time) {
		this.time = time;
		this.speed = speed;
		setInterruptible(true);
		timer = new Timer();
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		timer.reset();
		timer.start();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		URakerSystem.getInstance().moveRaker(speed);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		// return BinElevatorSystem.getInstance().getOpenedLimitSwitch();
		return timer.get() > time;
	}

	// Called once after isFinished returns true
	protected void end() {
		URakerSystem.getInstance().moveRaker(0.0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		URakerSystem.getInstance().moveRaker(0.0);
	}
}
