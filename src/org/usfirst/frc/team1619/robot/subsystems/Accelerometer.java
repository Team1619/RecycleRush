package org.usfirst.frc.team1619.robot.subsystems;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Accelerometer extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private BuiltInAccelerometer accelerometer;
	
	public Accelerometer() {
		accelerometer = new BuiltInAccelerometer();
	}
	
	public void display() {
		SmartDashboard.putString("Accelerometer X", Double.toString(accelerometer.getX()));
		SmartDashboard.putString("Accelerometer X", Double.toString(accelerometer.getY()));
		SmartDashboard.putString("Accelerometer X", Double.toString(accelerometer.getZ()));
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