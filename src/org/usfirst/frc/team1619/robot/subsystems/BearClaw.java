package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;

import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;

public class BearClaw extends Subsystem{
	
	// Put methods for controlling this subsystem
    // here. Call these from Commands.
	private TalonSRX bearClawMotor;
	
	public BearClaw()
	{
		bearClawMotor = new TalonSRX(RobotMap.bearClawMotorID);
	}
	
	protected void initDefaultCommand() {
		
	}
	
	public void move(double speed)
	{
		bearClawMotor.set(speed);
	}

	public void stop()
	{
		bearClawMotor.stopMotor();
	}
}
