package org.usfirst.frc.team1619.robot.auto;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DelayCommand extends Command {
	
	private Timer delayTimer;
	private double delayTime = 0;
	
    public DelayCommand(double delayTime) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	delayTimer = new Timer();
    	this.delayTime = delayTime;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	delayTimer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return delayTimer.get() > delayTime;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
