package org.usfirst.frc.team1619.robot.subsystems;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Accelerometer extends Subsystem {
    
    /**
     *  Put methods for controlling this subsystem here. Call these from Commands.
     */
	private BuiltInAccelerometer accelerometer;
	
	private Accelerometer() {
		accelerometer = new BuiltInAccelerometer();
	}
	
	private static Accelerometer theSystem;
	
	public static Accelerometer getInstance() {
		if(theSystem == null)
			theSystem = new Accelerometer();
		return theSystem;
	}
	
	public void display() {
		//display information about the accelerator to the dashboard so drivers know the speed.
	}
	
	public double getX() {
		return accelerometer.getX();
	}
	
	public double getY() {
		return accelerometer.getY();
	}
	
	public double getZ() {
		return accelerometer.getZ();
	}

	@Override
	protected void initDefaultCommand() {
		
	}
}
