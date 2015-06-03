package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.UOI;
import org.usfirst.frc.team1619.robot.URobotMap;
import org.usfirst.frc.team1619.robot.UStateMachine;
import org.usfirst.frc.team1619.robot.UStateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;

/**
 *
 */
public class UBinElevatorSystem extends UStateMachineSystem {

	public static final double kInitSpeed = -0.4;
	public static final double kBinElevatorUpSpeed = -0.4;
	public static final double kBinElevatorDownSpeed = 0.4;
	
	public static final double kBinIdleSpeed = -0.2;
	public static final double kBinTiltHumanFeedSpeed = -0.2;

	
	public static final double kBinGripOpenSpeed = 0.8;
	public static final double kBinGripCloseSpeed = -0.8;
	public static final double kBinGripOpenSpeedSlow = 0.4;
	public static final double kToteElevatorSafetyForTilt = 5.5; //fish
	
	public static final double kBinGripOpenTime = 0.5;
	
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	public final CANTalon binElevatorMotor;
	public final CANTalon tilterMotor;
	public final CANTalon binGripMotor;
	
	private double binElevatorSpeed; // will be %vbus 
	
	private double binGripSpeed = 0.0;
	private double tilterMotorSpeed = 0.0;
	
		
	private UBinElevatorSystem() {
		binElevatorMotor = URobotMap.MotorDefinition.binElevatorMotor.getMotor();
		binElevatorMotor.enableLimitSwitch(true, true);
		binElevatorMotor.enableBrakeMode(true);
//		binElevatorMotor.reverseSensor(true);
		binElevatorMotor.reverseOutput(true);
//		binElevatorMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	
    	tilterMotor = URobotMap.MotorDefinition.tilterMotor.getMotor();
    	tilterMotor.enableLimitSwitch(true, true);
    	tilterMotor.enableBrakeMode(true);
    	tilterMotor.ConfigFwdLimitSwitchNormallyOpen(true);
    	tilterMotor.ConfigRevLimitSwitchNormallyOpen(true);
    	
    	binGripMotor = URobotMap.MotorDefinition.binGripMotor.getMotor();
    	binGripMotor.enableLimitSwitch(false, false);
    	binGripMotor.enableBrakeMode(true);
	}
	
	private final static UBinElevatorSystem theSystem = new UBinElevatorSystem();
	
	public static UBinElevatorSystem getInstance() {
		return theSystem;
	}

    public void initDefaultCommand() {
    }
    
    public void setBinElevatorSpeed(double speed) {
    	binElevatorSpeed = speed;
    }
    
    public void binElevatorUpdate() {  
		if(UOI.getInstance().binElevatorUp.get()) {
			binElevatorMotor.set(kBinElevatorUpSpeed);
		}
		else if(UOI.getInstance().binElevatorDown.get()) {
			binElevatorMotor.set(kBinElevatorDownSpeed);
		}
    	else {
			binElevatorMotor.set(binElevatorSpeed);
		}
    }
    
    public void setBinTilt(double moveValue) {
    	tilterMotorSpeed = moveValue;
    }
    
    public void binTiltUpdate() {
    	double finalTiltSpeed;
    	
    	//get the tilt speed we want
    	double joystickY = UOI.getInstance().tiltStick.getY();
    	boolean manual = Math.abs(joystickY) > 0.1;
    	if(manual) {
    		finalTiltSpeed = joystickY;
    	}
    	else {
    		finalTiltSpeed = tilterMotorSpeed;
    	}
    	
    	//assign the speed if it's safe
    	if(isSafeToTilt() || (finalTiltSpeed > 0)) {
    		tilterMotor.changeControlMode(ControlMode.PercentVbus);
    		tilterMotor.set(finalTiltSpeed);
    	} 
    	else {
    		tilterMotor.changeControlMode(ControlMode.PercentVbus);
    		tilterMotor.set(0.0);
    	}
    }

    public boolean isSafeToTilt() {
    	if(UToteElevatorSystem.getInstance().getToteElevatorPosition() <= kToteElevatorSafetyForTilt) {
    		switch (UStateMachine.getInstance().getState()) {
    		case Init:
    		case Idle:
    			return true;
    		default:
    			return false;
    		}
    	}
    	else {
    		return false;
    	}
    }


    public boolean getTilterBackLimitSwitch() {
    	return tilterMotor.isFwdLimitSwitchClosed();
    }
    
    public boolean getTilterFowardLimitSwitch() {
    	return tilterMotor.isRevLimitSwitchClosed();
    }
    
    public void setBinGrip(double moveValue) {
    	binGripSpeed = moveValue;
    }
    
    public void binGripUpdate() {
    	if(UOI.getInstance().openClawButton.get()) {
    		binGripMotor.set(kBinGripOpenSpeed);
    	}
    	else if(UOI.getInstance().closeClawButton.get()) {
    		binGripMotor.set(kBinGripCloseSpeed);
    	}
    	else if(UOI.getInstance().driverCloseButton.get()) {
    		binGripMotor.set(kBinGripCloseSpeed);
    	}
    	else if(UOI.getInstance().lowerToteElevatorAndOpenClawButton.get() && 
    			(UStateMachine.getInstance().getState() == UStateMachine.State.Idle || UStateMachine.getInstance().getState() == UStateMachine.State.Abort)) {
    		binGripMotor.set(kBinGripOpenSpeed);
    	}
    	else {
    		binGripMotor.set(binGripSpeed);
    	}
    }

    public void init(State state) {
		setBinElevatorSpeed(0.0);
		setBinTilt(0.0);
		setBinGrip(0.0);
		
		switch(state) {
		case Init:
			break;
		case Idle:
			break;
		case HumanFeed_RaiseTote:
			break;
		case HumanFeed_ThrottleConveyorAndDescend:
			break;
		case HumanFeed_ToteOnConveyor:
			break;
		case HumanFeed_WaitForTote:
			break;
		default:
			break;
		}
	}
    
	@Override
	public void run(State state, double elapsed) {	
		switch(state) {
		case Init:
			setBinElevatorSpeed(kInitSpeed);
			break;
		case Idle:
			setBinElevatorSpeed(0);
			break;
		case HumanFeed_RaiseTote:
			setBinElevatorSpeed(kBinIdleSpeed);
			setBinTilt(kBinTiltHumanFeedSpeed);
			break;
		case HumanFeed_WaitForTote:
			setBinElevatorSpeed(kBinIdleSpeed);
			setBinTilt(kBinTiltHumanFeedSpeed);
			break;
		case HumanFeed_ToteOnConveyor:
			setBinElevatorSpeed(kBinIdleSpeed);
			setBinTilt(kBinTiltHumanFeedSpeed);
			break;
		case HumanFeed_ThrottleConveyorBack:
			setBinElevatorSpeed(kBinIdleSpeed);
			setBinTilt(kBinTiltHumanFeedSpeed);
			break;
		case HumanFeed_ThrottleConveyorAndDescend:
			setBinElevatorSpeed(kBinIdleSpeed);
			setBinTilt(kBinTiltHumanFeedSpeed);
			break;
		case TiltUp:
			setBinTilt(0.7);
			break;
		case Abort:
			setBinElevatorSpeed(0);
			break;
		default:
			break;
		}
		
		binElevatorUpdate();
		binGripUpdate();
		binTiltUpdate();
	}
	
	public boolean initFinished() {
		return true;
	}
}

