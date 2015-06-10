package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.UOI;
import org.usfirst.frc.team1619.robot.URobotMap;
import org.usfirst.frc.team1619.robot.UStateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;

/**
 *
 */
public class URakerSystem extends UStateMachineSystem {

	public static final double kRakerDownSpeed = -1.0;
	public static final double kRakerUpSpeed = 0.4;

	public final CANTalon rakerMotor;

	private double rakerSpeed = 0.0;

	private URakerSystem() {
		rakerMotor = URobotMap.MotorDefinition.rakerMotor.getMotor();
		rakerMotor.enableBrakeMode(true);
	}

	private final static URakerSystem theSystem = new URakerSystem();

	public static URakerSystem getInstance() {
		return theSystem;
	}

	public void initDefaultCommand() {
	}

	public void moveRaker(double moveValue) {
		rakerSpeed = moveValue;
	}

	public void rakerUpdate() {
		if (UOI.getInstance().rakerDownManualButton.get())
			rakerMotor.set(kRakerDownSpeed);
		else if (UOI.getInstance().rakerUpManualButton.get())
			rakerMotor.set(kRakerUpSpeed);
		else
			rakerMotor.set(rakerSpeed);
	}

	@Override
	public void run(State state, double elapsed) {
		rakerSpeed = 0.0;
		rakerUpdate();
	}
}
