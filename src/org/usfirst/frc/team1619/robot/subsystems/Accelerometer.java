package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.commands.AccelerometerCommand;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Accelerometer extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	BuiltInAccelerometer accelerometer;
	double accelerationX;
	double accelerationY;
	double accelerationZ;
	Timer timer;
	double last;
	
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
			System.out.println(accelerationX + ", " + accelerationY + ", " + accelerationZ); 
			last = timer.get();
		}
		
	}
		

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new AccelerometerCommand());
    }
}