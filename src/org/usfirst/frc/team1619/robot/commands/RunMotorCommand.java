package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.Robot;
import org.usfirst.frc.team1619.robot.subsystems.MotorSystem;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RunMotorCommand extends Command {

	private MotorSystem motorSystem;
	private Joystick rightStick;
	
    public RunMotorCommand() {
    	
        // Use requires() here to declare subsystem dependencies
    	motorSystem = Robot.getRobot().motorSystem;
        requires(motorSystem);
        rightStick = Robot.getRobot().oi.getRightStick();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	motorSystem.run(rightStick.getX());
    	//SmartDashboard.putNumber("MotorSystem Encoder Position", motorSystem.getPosition());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	motorSystem.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
