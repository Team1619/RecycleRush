package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.commands.AccelerometerCommand;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Accelerometer extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private BuiltInAccelerometer accelerometer;
	private double accelerationX;
	private double accelerationY;
	private double accelerationZ;
	private Timer timer;
	private double last;
	
	public Accelerometer()
	{
		accelerometer = new BuiltInAccelerometer();
		timer = new Timer();
		timer.start();
		last = 0;
		
	}
	
	public void operatorControl()
	{
		accelerationX = accelerometer.getX();
		accelerationY = accelerometer.getY();
		accelerationZ = accelerometer.getZ();
		if(timer.get() >= last + 1) {
			SmartDashboard.putString("Accelerometer", "X - " + accelerationX + "\nY - " + accelerationY + "\nZ - " + accelerationZ);
			last = timer.get();
		}
		
	}
		

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new AccelerometerCommand());
    }
}