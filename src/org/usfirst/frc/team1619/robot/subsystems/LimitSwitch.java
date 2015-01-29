package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.commands.OpticalSensorCommand;
import org.usfirst.frc.team1619.robot.commands.LimitSwitchCommand;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.FixedInterruptHandler;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class LimitSwitch extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    // here. Call these from Commands.
	private DigitalInput switchSubsystem;
	
	public LimitSwitch() {
		switchSubsystem = new DigitalInput(RobotMap.switchSubsystemID);
		
		switchSubsystem.requestInterrupts(new FixedInterruptHandler<Integer>() {
			
			@Override
			protected void interruptFired2(int interruptAssertedMask, Integer param) {
				System.out.println("Edge" + " " + interruptAssertedMask);
			}
			
		});
		switchSubsystem.enableInterrupts();
		switchSubsystem.setUpSourceEdge(true, true);
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new LimitSwitchCommand());
		// TODO Auto-generated method stub
		
	}
	public boolean getState() {
    	return switchSubsystem.get();
    }
}

