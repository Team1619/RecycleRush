package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.commands.GyroCommand;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GyroSubsystem extends Subsystem {
	private Gyro gyro;
	private AnalogInput gyroTemp;
	
	public GyroSubsystem() {
		gyro = new Gyro(RobotMap.gyroRateAnalogID);
		gyroTemp = new AnalogInput(RobotMap.gyroTempAnalogID);
		
		calibrate();
	}
	
	public void calibrate() {
		gyro.initGyro();
	}
	
	public void reset() {
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
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new GyroCommand());
    }
}
