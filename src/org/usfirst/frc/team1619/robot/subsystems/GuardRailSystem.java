package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GuardRailSystem extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private CANTalon guardRailMotor; //overdrive slightly
				
	private GuardRailSystem() {
    	guardRailMotor = new CANTalon(RobotMap.guardRailMotor);
    	guardRailMotor.enableLimitSwitch(false, false);
    	guardRailMotor.enableBrakeMode(false);
	}
	
	private static GuardRailSystem theSystem;
	
	public static GuardRailSystem getInstance() {
		if(theSystem == null)
			theSystem = new GuardRailSystem();
		return theSystem;
	}

	public void setGuardRailSpeed(double speed)
	{
		guardRailMotor.set(speed);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    	//setDefaultCommand(new ManualGuardRailCommand(0.0));
    }
    
}

