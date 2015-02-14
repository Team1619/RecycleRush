package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.subsystems.Conveyor;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ManualGuardRailCommand extends Command {
	private Conveyor conveyor; 
	private double speed;
	private Timer timer;
	
    public ManualGuardRailCommand(double speed) {
        // Use requires() here to declare subsystem dependencies
    	conveyor = Conveyor.getInstance();
    	this.speed = speed;
    	timer = new Timer();
        requires(conveyor);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	timer.start();
    	conveyor.moveGuardRail(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(timer.get() >= 0.25)
    	{
    		return true;
    	}
    	return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	conveyor.moveGuardRail(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }

}
