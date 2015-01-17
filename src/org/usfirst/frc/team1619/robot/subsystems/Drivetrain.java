package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.commands.DriveCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.GenericHID;
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

    public Drivetrain() {
    	leftMotor1 = new CANTalon(RobotMap.leftDriveMotor1);
    	leftMotor2 = new CANTalon(RobotMap.leftDriveMotor2);
    	rightMotor1 = new CANTalon(RobotMap.rightDriveMotor1);
    	rightMotor2 = new CANTalon(RobotMap.rightDriveMotor2);
    	
    	drive = new RobotDrive(leftMotor1, leftMotor2, rightMotor1, rightMotor2);
    	drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, false);
    	drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, false);
    	drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, false);
    	drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, false);
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new DriveCommand());
    }
    
    public void drive(GenericHID inputDevice) {
    	drive.arcadeDrive(inputDevice.getY(), inputDevice.getTwist() * 0.75);
    }
    
    public void drive(double moveX, double moveY) {
    	drive.arcadeDrive(moveX, moveY);
    }
    
    public void stop() {
    	drive.stopMotor();
    }
}

