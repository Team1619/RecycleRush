package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class KachigCommand extends Command {
	
	protected Drivetrain drivetrain;
	private Timer timer;
	private double rotationalspeed;
	private double linearspeed;
	
    public KachigCommand(double linearspeed, double rotationalspeed) {
        // Use requires() here to declare subsystem dependencies
    	drivetrain = Drivetrain.getInstance();
        requires(drivetrain);
        this.rotationalspeed = rotationalspeed;
        this.linearspeed = linearspeed;
        
        timer = new Timer();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timer.start();
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	drivetrain.drive(linearspeed, rotationalspeed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return timer.get() > 0.025;
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
