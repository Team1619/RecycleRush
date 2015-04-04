package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.robot.subsystems.ConveyorSystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class MoveConveyor extends Command {

	private double runSpeed; 
	private double runTime;
	private Timer runTimer;
	
    public MoveConveyor(double speed, double time) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	runSpeed = speed;
    	runTime = time;
    	runTimer = new Timer();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	runTimer.stop();
    	runTimer.reset();
    	runTimer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	ConveyorSystem.getInstance().conveyorMotor.set(runSpeed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return runTimer.get() > runTime;
    }

    // Called once after isFinished returns true
    protected void end() {
    	ConveyorSystem.getInstance().conveyorMotor.set(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
