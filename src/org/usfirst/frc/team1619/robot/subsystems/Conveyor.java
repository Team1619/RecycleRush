package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Conveyor extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private CANTalon conveyorMotor; //multiple speeds based on optical sensor configuration
	private CANTalon guardRailMotor; //overdrive slightly
	
	private Conveyor() {
		conveyorMotor = new CANTalon(RobotMap.conveyorMotor);
		conveyorMotor.enableLimitSwitch(false, false);
    	conveyorMotor.enableBrakeMode(false);

    	guardRailMotor = new CANTalon(RobotMap.guardRailMotor);
    	guardRailMotor.enableLimitSwitch(false, false);
    	guardRailMotor.enableBrakeMode(false);

	}
	
	private static final Conveyor theSystem = new Conveyor();
	
	public static Conveyor getInstance() {
		return theSystem;
	}
	

	public void moveConveryor(double moveValue) {
		conveyorMotor.set(moveValue);
	}
	
	public void moveGuardRail(double moveValue) {
		guardRailMotor.set(moveValue);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

