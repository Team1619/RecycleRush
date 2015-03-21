package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.StateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;

/**
 *
 */
public class RakerSystem extends StateMachineSystem {

	public static final double kRakerOpenSpeed = 0.4;
	public static final double kRakerCloseSpeed = -0.4;
	
	public final CANTalon rakerMotor;
	
	private double rakerSpeed = 0.0;
	
	private RakerSystem() {
		rakerMotor = RobotMap.MotorDefinition.rakerMotor.getMotor();
    	rakerMotor.enableBrakeMode(true);
	}
	
	private final static RakerSystem theSystem = new RakerSystem();
	
	public static RakerSystem getInstance() {
		return theSystem;
	}
	
    public void initDefaultCommand() {
    }
    public void moveRaker(double moveValue) {
    	rakerSpeed = moveValue;
    }
    public void rakerUpdate() {
    	if(OI.getInstance().rakerOpenManualButton.get())
    		rakerMotor.set(kRakerOpenSpeed);
    	else if(OI.getInstance().rakerCloseManualButton.get())
    		rakerMotor.set(kRakerCloseSpeed);
    	else
    		rakerMotor.set(rakerSpeed);
    }

	@Override
	public void run(State state, double elapsed) {
		rakerSpeed = 0.0;
		rakerUpdate();
	}
}

