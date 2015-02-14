package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.commands.ManualConveyorCommand;
import org.usfirst.frc.team1619.robot.commands.ManualGuardRailCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.CommandGroup;
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
		//conveyor and guardrails don't have limit switches nor brake modes
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
	

	public void moveConveyor(double moveValue) {
		//conveyorMotor.set(moveValue);
	}
	
	public void moveGuardRail(double moveValue) {
		//guardRailMotor.set(moveValue);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    	CommandGroup cg = new CommandGroup();
    	//creates function that does the same thing but is manual
    	cg.addParallel(new ManualConveyorCommand(0));
    	cg.addParallel(new ManualGuardRailCommand(0));
        setDefaultCommand(cg);
    }
}

