package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.subsystems.ToteLiftSystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 * does same job as tote elevator command within LiftSystem subsystem
 * but is controlled manually via the left stick
 */
public class ManualToteElevatorCommand extends Command {
	private ToteLiftSystem liftSystem;
	private double speed;
	
    public ManualToteElevatorCommand(double speed) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	liftSystem = ToteLiftSystem.getInstance();
    	this.speed = speed;
    	requires(liftSystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	liftSystem.moveToteElevator(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    	liftSystem.moveToteElevator(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
