package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.subsystems.UDrivetrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class UKachigCommand extends Command {

	protected UDrivetrain drivetrain;
	private Timer timer;
	private double rotationalspeed;
	private double linearspeed;
	private double time;

	public UKachigCommand(double linearspeed, double rotationalspeed,
			double time) {
		// Use requires() here to declare subsystem dependencies
		drivetrain = UDrivetrain.getInstance();
		requires(drivetrain);
		this.rotationalspeed = rotationalspeed;
		this.linearspeed = linearspeed;
		this.time = time;

		timer = new Timer();
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		timer.stop();
		timer.reset();
		timer.start();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		drivetrain.drive(linearspeed, rotationalspeed);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return timer.get() > time;
	}

	// Called once after isFinished returns true
	protected void end() {
		drivetrain.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
