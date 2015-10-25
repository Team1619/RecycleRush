package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.robot.subsystems.BinElevatorSystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ArmUp extends Command {

	double speed;
	private Timer timer = new Timer();
	private double time;
	private boolean fullUp = false;
	
    public ArmUp(double speed, double time) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	this.speed = speed;
    	requires (BinElevatorSystem.getInstance());
        this.time = time;
        fullUp = false;
    }
    
    public ArmUp(double speed) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	this.speed = speed;
    	requires (BinElevatorSystem.getInstance());
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
    	BinElevatorSystem.getInstance().setBinTilt(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(fullUp) {
    		return(BinElevatorSystem.getInstance().getTilterBackLimitSwitch());
    	}
    	else {
    		return timer.get() > time;
    	}
    }

    // Called once after isFinished returns true
    protected void end() {
    	BinElevatorSystem.getInstance().setBinTilt(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}