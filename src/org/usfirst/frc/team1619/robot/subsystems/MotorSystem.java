package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.commands.RunMotorCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class MotorSystem extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private CANTalon motor;
	
	public MotorSystem() {
		motor = new CANTalon(RobotMap.motorSystemID);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new RunMotorCommand());
    }
    
    public void run(double inputVal) {
    	motor.set(inputVal);
    }
    
    public void stop() {
    	motor.disable();
    }
}

