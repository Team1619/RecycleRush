package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.UStateMachine;
import org.usfirst.frc.team1619.robot.UStateMachine.State;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public abstract class UStateMachineSystem extends Subsystem {

	public UStateMachineSystem() {
		UStateMachine.getInstance().addSystem(this);
	}

	public void superSecretSpecialSatanRun(State state, double elapsed) {
		if (this.getCurrentCommand() == null) {
			run(state, elapsed);
		}
	}

	public void init(State state) {
	}

	public boolean initFinished() {
		return true;
	}

	public abstract void run(State state, double elapsed);

	public void initDefaultCommand() {
	}
}
