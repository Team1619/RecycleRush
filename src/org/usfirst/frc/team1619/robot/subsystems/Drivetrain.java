package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.commands.DriveCommand;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Drivetrain extends Subsystem {
  
	private RobotDrive drive;
	
	//Motor controllers for drivetrain
	private TalonSRX leftDriveMotor1;
	private TalonSRX leftDriveMotor2;
	private TalonSRX rightDriveMotor1;
	private TalonSRX rightDriveMotor2;
	
	public Drivetrain()
	{
		leftDriveMotor1 = new TalonSRX(RobotMap.leftDriveMotor1ID);
		leftDriveMotor2 = new TalonSRX(RobotMap.leftDriveMotor2ID);
		rightDriveMotor2 = new TalonSRX(RobotMap.rightDriveMotor1ID);
		rightDriveMotor1 = new TalonSRX(RobotMap.rightDriveMotor2ID);

		drive = new RobotDrive(leftDriveMotor1, leftDriveMotor2, rightDriveMotor1, rightDriveMotor2);
	}

    public void initDefaultCommand() {
        setDefaultCommand(new DriveCommand());
    }
    
    public void drive(GenericHID joystick) {
    	drive.arcadeDrive(joystick.getY(), joystick.getTwist() * 0.75);
    }
    
    public void drive(double moveVal, double rotateVal) {
    	drive.arcadeDrive(moveVal, rotateVal);
    }
    
    public void stop() {
    	drive.arcadeDrive(0.0, 0.0);
    }
}

