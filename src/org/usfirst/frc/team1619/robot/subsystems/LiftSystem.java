package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.commands.LiftSystemStateMachineCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class LiftSystem extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private CANTalon toteElevatorMotor;
	private CANTalon binElevatorMotor;
	private CANTalon tilterMotor;
	private CANTalon binGripMotor;
	
	public static final int kStateIdle = 0;
	public static final int kStateBeginStack = 1;
	public static final int kStateBeginFeed = 2;
	public static final int kStateStackForFeed = 3;
	public static final int kStateStackForPickup = 4;
	public static final int kStatePickup = 5;
	public static final int kStateDropoff = 6;
	
	
	private int currentState = kStateIdle;
	
	public LiftSystem() {
		toteElevatorMotor = new CANTalon(RobotMap.toteElevatorMotor);
    	toteElevatorMotor.enableLimitSwitch(false, false);
    	toteElevatorMotor.enableBrakeMode(false);
    	
    	binElevatorMotor = new CANTalon(RobotMap.binElevatorMotor);
    	binElevatorMotor.enableLimitSwitch(false, false);
    	binElevatorMotor.enableBrakeMode(false);
    	
    	tilterMotor = new CANTalon(RobotMap.tilterMotor);
    	tilterMotor.enableLimitSwitch(true, true);
    	tilterMotor.enableBrakeMode(false);
    	tilterMotor.ConfigFwdLimitSwitchNormallyOpen(false);
    	tilterMotor.ConfigRevLimitSwitchNormallyOpen(false);
    	
    	binGripMotor = new CANTalon(RobotMap.binGripMotor);
    	binGripMotor.enableLimitSwitch(false, false);
    	binGripMotor.enableBrakeMode(false);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new LiftSystemStateMachineCommand());
    }
    
    public void moveToteElevator(double moveValue) {
    	toteElevatorMotor.set(moveValue);
    }
    
    public void moveBinElevator(double moveValue) {
    	binElevatorMotor.set(moveValue);
    }
    
    public void binTilt(double moveValue) {
    	tilterMotor.set(moveValue);
    }
    
    public void moveBinGrip(double moveValue) {
    	binGripMotor.set(moveValue);
    }
    
    private static String stateToString(int state) {
    	switch(state) {
    	case kStateIdle:
    		return "Idle";
    	case kStateBeginStack:
    		return "Begin Stack";
    	case kStateBeginFeed:
    		return "Begin Feed";
    	case kStateStackForFeed:
    		return "Stack For Feed";
    	case kStateStackForPickup:
    		return "Stack for Pickup";
    	case kStatePickup:
    		return "Pickup";
    	case kStateDropoff:
    		return "Dropoff";
    	default: 
    		return "Invalid State";
    	}
    }
    
    private int stateIdle() {
    	return kStateIdle;
    }
    
    public void runStateMachine() {
    	int nextState = currentState;
    	//System.out.println("Current State: " + stateToString(currentState));
    	
    	switch(currentState) {
    	case kStateIdle:
    		nextState = stateIdle();
    		break;
    	default:
    		break;
    	}
    	
    	if(nextState != currentState) {
        	//System.out.println("Next State: " + stateToString(nextState));
        	currentState = nextState;
    	}
    }
    
    public String getLimits() { //"fwd    rev"
    	return "" + tilterMotor.isFwdLimitSwitchClosed() + "   " + tilterMotor.isRevLimitSwitchClosed();
    }
}

