package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.subsystems.LiftSystem;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ManualBinTiltCommand extends Command {
	private LiftSystem liftSystem;
	private double speed;
	private Joystick joystick;

    public ManualBinTiltCommand(double speed) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	liftSystem = LiftSystem.getInstance();
    	this.speed = speed;
    	requires(liftSystem);
    	this.joystick = OI.getInstance().leftStick;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	liftSystem.binTilt(joystick);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    	liftSystem.binTilt(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
