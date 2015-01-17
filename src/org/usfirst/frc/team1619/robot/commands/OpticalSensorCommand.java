package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.Robot;
import org.usfirst.frc.team1619.robot.subsystems.OpticalSensor;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class OpticalSensorCommand extends Command {

	private OpticalSensor opticalSensor;
	
    public OpticalSensorCommand() {
        // Use requires() here to declare subsystem dependencies
    	opticalSensor = Robot.getRobot().opticalSensor;
        requires(opticalSensor);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	opticalSensor.getState();
    	//SmartDashboard.putBoolean("OpticalSensor", opticalSensor.getState());
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
