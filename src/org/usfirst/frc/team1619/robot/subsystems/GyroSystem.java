package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.command.Subsystem;

public class GyroSystem extends Subsystem {

	private Gyro gyro;
	private AnalogInput gyroTemp;
	private double gyroDirection = 0.0;
	
	
	
	private GyroSystem() {
		gyro = new Gyro(RobotMap.gyroRateAnalogID);
		gyroTemp = new AnalogInput(RobotMap.gyroTempAnalogID);
		gyro.setSensitivity(0.007);
	}
	
	private static GyroSystem theSystem;
	
	public static GyroSystem getInstance() {
		if(theSystem == null)
			theSystem = new GyroSystem();
		return theSystem;
	}

    //gyro stuff
    public void calibrate() {
    	gyroDirection = getHeading();
		gyro.initGyro();
	}
	
	public void resetGyro() {
		gyroDirection = 0.0;
		gyro.reset();
	}
	
	public double getHeading() {
		return (gyro.getAngle() + gyroDirection)%360;
	}
	
	public double getTurnRate() {
		return gyro.getRate();
	}
	
	public double getTemperature() {
		//prevent gyro from overheating
		return 25 + (gyroTemp.getVoltage() - 2.5) / 0.009;
	}
	
	@Override
	protected void initDefaultCommand() {
	}

}
