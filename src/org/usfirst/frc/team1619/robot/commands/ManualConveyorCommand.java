package org.usfirst.frc.team1619.robot.commands;


import org.usfirst.frc.team1619.robot.subsystems.Conveyor;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ManualConveyorCommand extends Command {
	private Conveyor conveyor;
	private double speed;

    public ManualConveyorCommand(double speed) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	conveyor = Conveyor.getInstance();
    	this.speed = speed;
    	requires(conveyor);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
		conveyor.moveConveyor(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
