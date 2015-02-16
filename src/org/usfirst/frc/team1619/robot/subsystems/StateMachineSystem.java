package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.StateMachine;
import org.usfirst.frc.team1619.robot.StateMachine.State;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public abstract class StateMachineSystem extends Subsystem {
    
	public StateMachineSystem() {
		StateMachine.getInstance().addSystem(this);
	}
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	public void superSecretSpecialSatanRun(State state) {
		if(this.getCurrentCommand() == null) {
			run(state);
		}
	}
	
	public abstract void run(State state);
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

