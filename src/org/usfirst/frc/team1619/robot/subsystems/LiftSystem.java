package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class LiftSystem extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private CANTalon toteElevatorMotor;
	private CANTalon binElevatorMotor;
	private CANTalon tilterMotor;
	private CANTalon binGripMotor;

	public LiftSystem() {
		
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	toteElevatorMotor = new CANTalon(RobotMap.toteElevatorMotor);
    	toteElevatorMotor.enableLimitSwitch(false, false);
    	toteElevatorMotor.enableBrakeMode(false);
    	
    	binElevatorMotor = new CANTalon(RobotMap.binElevatorMotor);
    	binElevatorMotor.enableLimitSwitch(false, false);
    	binElevatorMotor.enableBrakeMode(false);
    	
    	tilterMotor = new CANTalon(RobotMap.tilterMotor);
    	tilterMotor.enableLimitSwitch(false, false);
    	tilterMotor.enableBrakeMode(false);
    	
    	binGripMotor = new CANTalon(RobotMap.binGripMotor);
    	binGripMotor.enableLimitSwitch(false, false);
    	binGripMotor.enableBrakeMode(false);
    }
    
    public void moveToteElevator(double moveValue) {
    	toteElevatorMotor.set(moveValue);
    }
    
    public void moveBinElevator(double moveValue) {
    	binElevatorMotor.set(moveValue);
    }
    
    public void binTilt(double moveValue) {
    	tilterMotor.set(moveValue);
    }
    
    public void moveBinGrip(double moveValue) {
    	binGripMotor.set(moveValue);
    }
}

