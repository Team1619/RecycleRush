package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.robot.subsystems.UBinElevatorSystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class UClawsClose extends Command {

	private static final double kSpeed = UBinElevatorSystem.kBinGripCloseSpeed;
	private static final double kTime = 2;
	
	private Timer closeTimer = new Timer();
	private double speed, time;
	
    public UClawsClose(double speed, double time) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires (UBinElevatorSystem.getInstance());
    	this.speed = speed;
    	this.time = time;
    }
    
    public UClawsClose() {
    	requires (UBinElevatorSystem.getInstance());
    	this.speed = kSpeed;
    	this.time = kTime;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	closeTimer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	UBinElevatorSystem.getInstance().setBinGrip(-speed);  
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return closeTimer.get() >= time;
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
