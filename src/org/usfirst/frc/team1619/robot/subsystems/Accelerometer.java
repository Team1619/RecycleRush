package org.usfirst.frc.team1619.robot.subsystems;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	
	private static final Accelerometer theSystem = new Accelerometer();
	
	public static Accelerometer getInstance() {
		return theSystem;
	}
	
	public void display() {
		//display information about the accelerator to the dashboard so drivers know the speed.
		SmartDashboard.putString("Accelerometer X", Double.toString(accelerometer.getX()));
		SmartDashboard.putString("Accelerometer Y", Double.toString(accelerometer.getY()));
		SmartDashboard.putString("Accelerometer Z", Double.toString(accelerometer.getZ()));
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
