package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GyroSubsystem extends Subsystem {
	private Gyro gyro;
	
	public GyroSubsystem() {
		gyro = new Gyro(RobotMap.gyroID);
		calibrate();
	}
	
	public void calibrate() {
		gyro.initGyro();
	}
	
	public void reset() {
		gyro.reset();
	}
	
	public double getHeading() {
		return gyro.getAngle();
	}
	
	public double getTurnRate() {
		return gyro.getRate();
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
