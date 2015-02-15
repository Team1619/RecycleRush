package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.commands.ManualConveyorCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Conveyor extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private CANTalon conveyorMotor; //multiple speeds based on optical sensor configuration
	
	private Conveyor() {
		conveyorMotor = new CANTalon(RobotMap.conveyorMotor);
		conveyorMotor.enableLimitSwitch(false, false);
    	conveyorMotor.enableBrakeMode(false);
	}
	
	private static Conveyor theSystem;
	
	public static Conveyor getInstance() {
		if(theSystem == null)
			theSystem = new Conveyor();
		return theSystem;
	}

	public void moveConveyor(double moveValue) {
		conveyorMotor.set(moveValue);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    	setDefaultCommand(new ManualConveyorCommand(0.675));
    }
    
    public boolean getFrontSensor() {
    	return false;
    }
    
    public boolean getBackSensor() {
    	return false;
    }
}

