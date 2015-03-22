package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.robot.subsystems.ToteElevatorSystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ToteElevatorMove extends Command {

	private double speed;
	private double time;
	private Timer timer = new Timer();
    public ToteElevatorMove(double speed, double time) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	this.speed = speed;
    	this.time = time;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timer.start();
    	ToteElevatorSystem.getInstance().setToteElevatorSpeed(speed);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return timer.get() > time;
    }

    // Called once after isFinished returns true
    protected void end() {
    	ToteElevatorSystem.getInstance().setToteElevatorSpeed(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	ToteElevatorSystem.getInstance().setToteElevatorSpeed(0);
    }
}
