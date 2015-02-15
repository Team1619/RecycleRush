package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.subsystems.GuardRailSystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ManualGuardRailCommand extends Command {
	private GuardRailSystem guardRailSystem;
	private Timer timer;
	private double speed;
	
    public ManualGuardRailCommand(double speed) {
        // Use requires() here to declare subsystem dependencies
    	guardRailSystem = GuardRailSystem.getInstance();
    	leftStick = OI.getInstance().leftStick;
    	timer = new Timer();
    	this.speed = speed;
        requires(guardRailSystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timer.stop();
    	timer.reset();
    	timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	guardRailSystem.setGuardRailSpeed(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return timer.get() >= 0.25;
    }

    // Called once after isFinished returns true
    protected void end() {
    	guardRailSystem.setGuardRailSpeed(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }

}
