package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.UOI;
import org.usfirst.frc.team1619.robot.subsystems.UDrivetrain;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class UManualDriveCommand extends Command {

	private UDrivetrain drivetrain;
	private Joystick joystick;
	
    public UManualDriveCommand() {
        // Use requires() here to declare subsystem dependencies
    	drivetrain = UDrivetrain.getInstance();
        requires(drivetrain);
        
        this.joystick = UOI.getInstance().rightStick;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	drivetrain.drive(joystick);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	// Manual joystick drive is the default state!
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	drivetrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
