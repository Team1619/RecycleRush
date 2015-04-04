package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.robot.subsystems.BinElevatorSystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class MoveBinElevator extends Command {

	
	private boolean toEnd;
	private double moveSpeed;
	private boolean up;
		
	//TODO: make binElevator positive voltage go up

	
	/**
	 * 
	 * @param toEnd - true = will stop applying voltage once top limit is reached
	 * @param speed - positive = down, negative = up. (PercentVBus)
	 */
    public MoveBinElevator(boolean toEnd, double speed) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	this.toEnd = toEnd;
    	moveSpeed = speed; 
    	up = true;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	if(moveSpeed > 0.0) {
    		up = false;
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	BinElevatorSystem.getInstance().setBinElevatorSpeed(moveSpeed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(!toEnd) {
    		return false;
    	}
    	else {
    		if(up) {
    			return BinElevatorSystem.getInstance().binElevatorMotor.isRevLimitSwitchClosed();
    		}
    		else {
    			return BinElevatorSystem.getInstance().binElevatorMotor.isFwdLimitSwitchClosed();
    		}
    	}
    }

    // Called once after isFinished returns true
    protected void end() {
    	BinElevatorSystem.getInstance().setBinElevatorSpeed(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
