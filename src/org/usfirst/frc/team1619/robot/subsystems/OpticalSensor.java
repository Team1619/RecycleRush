//package org.usfirst.frc.team1619.robot.subsystems;
package org.usfirst.frc.team1619.robot.subsystems;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.commands.OpticalSensorCommand;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.FixedInterruptHandler;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class OpticalSensor extends Subsystem {
	
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private DigitalInput opticalSensor;
	
	public OpticalSensor() {
		opticalSensor = new DigitalInput(RobotMap.opticalSensorID);
		
		opticalSensor.requestInterrupts(new FixedInterruptHandler<Integer>() {
			
			@Override
			protected void interruptFired2(int interruptAssertedMask, Integer param) {
				System.out.println("Edge" + " " + interruptAssertedMask);
			}
			
		});
		opticalSensor.enableInterrupts();
		opticalSensor.setUpSourceEdge(true, true);
	}

    public void initDefaultCommand() {
        setDefaultCommand(new OpticalSensorCommand());
    }
    
    public boolean getState() {
    	return opticalSensor.get();
    }
}

