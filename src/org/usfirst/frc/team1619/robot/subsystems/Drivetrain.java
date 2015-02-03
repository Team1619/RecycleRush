package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.commands.DriveCommand;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Drivetrain extends Subsystem {
	private RobotDrive drive;	
    private CANTalon leftMotor1;
    private CANTalon leftMotor2;
    private CANTalon rightMotor1;
    private CANTalon rightMotor2;
    
    //gyro stuff
    private Gyro gyro;
	private AnalogInput gyroTemp;

    public Drivetrain() {
    	leftMotor1 = new CANTalon(RobotMap.leftDriveMotor1);
    	leftMotor2 = new CANTalon(RobotMap.leftDriveMotor2);
    	rightMotor1 = new CANTalon(RobotMap.rightDriveMotor1);
    	rightMotor2 = new CANTalon(RobotMap.rightDriveMotor2);
    	
    	drive = new RobotDrive(leftMotor1, leftMotor2, rightMotor1, rightMotor2);
    	drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
    	drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
    	drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
    	drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
    	
    	//drive motor parameters, no limit switches and coasting
    	leftMotor1.enableLimitSwitch(false, false);
    	leftMotor2.enableLimitSwitch(false, false);
    	rightMotor1.enableLimitSwitch(false, false);
    	rightMotor2.enableLimitSwitch(false, false);
    	leftMotor1.enableBrakeMode(true);
    	leftMotor2.enableBrakeMode(true);
    	rightMotor1.enableBrakeMode(true);
    	rightMotor2.enableBrakeMode(true);
    	
    	gyro = new Gyro(RobotMap.gyroRateAnalogID);
		gyroTemp = new AnalogInput(RobotMap.gyroTempAnalogID);
		
		calibrate();
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new DriveCommand());
    }
    
    public void drive(GenericHID inputDevice) {
    	drive.arcadeDrive(inputDevice.getY(), inputDevice.getTwist());
    }
    
    public void drive(double moveX, double moveY) {
    	drive.arcadeDrive(moveX, moveY);
    }
    
    public void stop() {
    	drive.stopMotor();
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
}

