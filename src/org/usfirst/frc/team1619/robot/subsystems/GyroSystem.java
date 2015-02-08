package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.command.Subsystem;

public class GyroSystem extends Subsystem {

	private Gyro gyro;
	private AnalogInput gyroTemp;
	
	private GyroSystem() {
		gyro = new Gyro(RobotMap.gyroRateAnalogID);
		gyroTemp = new AnalogInput(RobotMap.gyroTempAnalogID);
	}
	
	private static final GyroSystem theSystem = new GyroSystem();
	
	public static GyroSystem getInstance() {
		return theSystem;
	}

    //gyro stuff
    public void calibrate() {
		gyro.initGyro();
	}
	
	public void resetGyro() {
		gyro.reset();
	}
	
	public double getHeading() {
		return gyro.getAngle()%360;
	}
	
	public double getTurnRate() {
		return gyro.getRate();
	}
	
	public double getTemperature() {
		return gyroTemp.getValue();
	}
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

}
