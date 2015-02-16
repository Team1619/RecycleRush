package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.commands.ManualConveyorCommand;
import org.usfirst.frc.team1619.robot.commands.UnloadConveyorCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.FixedInterruptHandler;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Conveyor extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private CANTalon conveyorMotor; //multiple speeds based on optical sensor configuration
	private DigitalInput frontConveyorOpticalSensor;
	private DigitalInput rearConveyorOpticalSensor;
		
	private Conveyor() {
		conveyorMotor = new CANTalon(RobotMap.conveyorMotor);
		conveyorMotor.enableLimitSwitch(false, false);
    	conveyorMotor.enableBrakeMode(false);
    	
    	frontConveyorOpticalSensor = new DigitalInput(RobotMap.frontConveyorOpticalSensorID);
		rearConveyorOpticalSensor = new DigitalInput(RobotMap.rearConveyorOpticalSensorID);
	}
	
	private static Conveyor theSystem;
	
	public static Conveyor getInstance() {
		if(theSystem == null)
			theSystem = new Conveyor();
		return theSystem;
	}
	
	public void init() {
		rearConveyorOpticalSensor.requestInterrupts(new FixedInterruptHandler<Command>() {
			Command cmd = new UnloadConveyorCommand();
			
			public Command overridableParamater() {
				return cmd;
			}
			
			@Override
			protected void interruptFired2(int interruptAssertedMask,
					Command cmd) {
				cmd.start();
			}
			
		});
		rearConveyorOpticalSensor.setUpSourceEdge(false, true);
		rearConveyorOpticalSensor.enableInterrupts();
	}

	public void moveConveyor(double moveValue) {
		conveyorMotor.set(moveValue);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    	setDefaultCommand(new ManualConveyorCommand(1.0));
    }
    
    public boolean getFrontSensor() {
    	return !frontConveyorOpticalSensor.get();
    }
    
    public boolean getRearSensor() {
    	return !rearConveyorOpticalSensor.get();
    }
}

