package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.Robot;
import org.usfirst.frc.team1619.robot.subsystems.SonarSystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class SonarCommand extends Command {

	private SonarSystem sonarSystem;
    public SonarCommand() {
    	sonarSystem = Robot.getRobot().sonarSystem;
        // Use requires() here to declare subsystem dependencies
        requires(sonarSystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	SmartDashboard.putNumber("Distance Ahead", sonarSystem.getRange());
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