package org.usfirst.frc.team1619.robot.commands;


import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.subsystems.Conveyor;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ManualConveyorCommand extends Command {
	private Conveyor conveyor;
	private Joystick leftStick;

    public ManualConveyorCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	conveyor = Conveyor.getInstance();
    	leftStick = OI.getInstance().leftStick;;
    	requires(conveyor);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double conveyorSpeed = (leftStick.getThrottle() - 1) / -2;
		if (Math.abs(conveyorSpeed) >= 0.1){
			conveyor.moveConveyor(conveyorSpeed);
			SmartDashboard.putNumber("Conveyor Speed", conveyorSpeed);
		}
		else {
			conveyor.moveConveyor(0);
			SmartDashboard.putNumber("Conveyor Speed", 0);
		}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	conveyor.moveConveyor(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
