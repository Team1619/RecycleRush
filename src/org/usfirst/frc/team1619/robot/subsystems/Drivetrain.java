package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.commands.ManualDriveCommand;

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
	public static final double kWheelDiameter = 6 * (2.54/100); //in meters
	public static final int kPulsesPerRev = 512;
	public static final double kDistancePerPulse = kWheelDiameter*Math.PI / kPulsesPerRev;
	
	public static final double kTwistScalar = 0.5;
			
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
		
		leftMotor1.setPosition(0.0);
		rightMotor1.setPosition(0.0);
		
		calibrate();
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new ManualDriveCommand());
    }
    
    public void drive(GenericHID inputDevice) {
    	drive.arcadeDrive(inputDevice.getY(), inputDevice.getTwist() * kTwistScalar);
    }
    
    /**
     * Arcade drive with linearVal being like the linear input on a joystick, and rotateVal being the 
     * twist value. Both -1.0 to 1.0.
     * @param linearVal
     * @param rotateVal
     */
    public void drive(double linearVal, double rotateVal) {
    	drive.arcadeDrive(-linearVal, rotateVal);
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
	
	public double getLeftEncoderPosition() {
		return -kDistancePerPulse*leftMotor1.getEncPosition();
	}
	
	public double getRightEncoderPosition() {
		return kDistancePerPulse*rightMotor1.getEncPosition();
	}
	
	public int getRawRightEncoderPosition() {
		return rightMotor1.getEncPosition();
	}
	
	public int getRawLeftEncoderPosition() {
		return leftMotor1.getEncPosition();
	}
	
	public void resetEncoders() {
		leftMotor1.setPosition(0.0);
		rightMotor1.setPosition(0.0);
	}
}

