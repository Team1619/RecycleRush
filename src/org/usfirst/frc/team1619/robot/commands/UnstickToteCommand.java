package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.subsystems.Conveyor;
import org.usfirst.frc.team1619.robot.subsystems.GuardRailSystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class UnstickToteCommand extends Command {
	private Conveyor conveyor;
	private GuardRailSystem guardRailSystem;
	private ManualGuardRailCommand manualGuardRailCommand;

    public UnstickToteCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	conveyor = Conveyor.getInstance();
    	guardRailSystem = GuardRailSystem.getInstance();
    	requires(conveyor);
    	requires(guardRailSystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	manualGuardRailCommand = new ManualGuardRailCommand(0.15);
    	manualGuardRailCommand.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	conveyor.moveConveyor(-1.0);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
