package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.StateMachine;
import org.usfirst.frc.team1619.robot.StateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;

/**
 *
 */
public class BinElevatorSystem extends StateMachineSystem {
	//Not used right now
//	public static final double kEncoderTicksPerInch = 2043 / 32.0; //fish
//	public static final double kOutOfTheWayPosition = 0.0;
//	public static final double kTransitPosition = 0.0;
//	public static final double kFeederPosition = 0.0;
//	public static final double kPickUpPosition = 0.0;
//	public static final double kPositionTolerance = 1.0;
	public static final double kInitSpeed = -0.4;
	public static final double kBinElevatorUpSpeed = -0.4;
	public static final double kBinElevatorDownSpeed = 0.4;
//	public static final double kBinTiltSpeed = 0.5;
	public static final double kBinIdleSpeed = -0.2;
	
	//Not used right now
//	public static final double kTotalHeight = 62.0; //fish
//	public static final double kToteElevatorHeight = 25.0; //fish
//	public static final double kBinElevatorHeight = 37.0; //fish
//	public static final double kToteElevatorHeightModifier = 10.0; //fish, accounts for the plastic fins on elevator being above the "toteElevatorPosition" 
//	public static final double kBinElevatorHeightModifier = -6.0; //fish, accounts for bottom of bin gripper being below the "binElevatorPosition"
//	public static final double kDistanceBetweenLifts = 45.0; //catfinches
//	public static final double kSafetyTolerance = 12.0;
//	public static final double kBinPickupPosition = -8.427; //catfinches
//	public static final double kBinNoodleInsertionPosition = -26.69; //catfinches
	public static final double kBinGripOpenSpeed = 0.8;
	public static final double kBinGripCloseSpeed = -0.8;
	public static final double kBinGripOpenSpeedSlow = 0.4;
	public static final double kToteElevatorSafetyForTilt = 5.5; //fish
	
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	public final CANTalon binElevatorMotor;
	public final CANTalon tilterMotor;
	public final CANTalon binGripMotor;
	
	private double binElevatorSpeed; // will be %vbus 
	
	private double binGripSpeed = 0.0;
	private double tilterMotorSpeed = 0.0;
	
		
	private BinElevatorSystem() {
		binElevatorMotor = RobotMap.MotorDefinition.binElevatorMotor.getMotor();
		binElevatorMotor.enableLimitSwitch(true, true);
		binElevatorMotor.enableBrakeMode(true);
//		binElevatorMotor.reverseSensor(true);
		binElevatorMotor.reverseOutput(true);
//		binElevatorMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	
    	tilterMotor = RobotMap.MotorDefinition.tilterMotor.getMotor();
    	tilterMotor.enableLimitSwitch(true, true);
    	tilterMotor.enableBrakeMode(true);
    	tilterMotor.ConfigFwdLimitSwitchNormallyOpen(true);
    	tilterMotor.ConfigRevLimitSwitchNormallyOpen(true);
    	
    	binGripMotor = RobotMap.MotorDefinition.binGripMotor.getMotor();
    	binGripMotor.enableLimitSwitch(false, false);
    	binGripMotor.enableBrakeMode(true);
	}
	
	private final static BinElevatorSystem theSystem = new BinElevatorSystem();
	
	public static BinElevatorSystem getInstance() {
		return theSystem;
	}

    public void initDefaultCommand() {
    }
    
    public void setBinElevatorSpeed(double speed) {
    	binElevatorSpeed = speed;
    }
    
    public void binElevatorUpdate() {  
		if(OI.getInstance().binElevatorUp.get()) {
			binElevatorMotor.set(kBinElevatorUpSpeed);
		}
		else if(OI.getInstance().binElevatorDown.get()) {
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
    	double joystickY = OI.getInstance().tiltStick.getY();
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
    	if(ToteElevatorSystem.getInstance().getToteElevatorPosition() <= kToteElevatorSafetyForTilt) {
    		switch (StateMachine.getInstance().getState()) {
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
    	if(OI.getInstance().openClawButton.get()) {
    		binGripMotor.set(kBinGripOpenSpeed);
    	}
    	else if(OI.getInstance().closeClawButton.get()) {
    		binGripMotor.set(kBinGripCloseSpeed);
    	}
    	else if(OI.getInstance().driverCloseButton.get()) {
    		binGripMotor.set(kBinGripCloseSpeed);
    	}
    	else if(OI.getInstance().lowerToteElevatorAndOpenClawButton.get() && 
    			(StateMachine.getInstance().getState() == StateMachine.State.Idle || StateMachine.getInstance().getState() == StateMachine.State.Abort)) {
    		binGripMotor.set(kBinGripOpenSpeedSlow);
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
			break;
		case HumanFeed_WaitForTote:
			setBinElevatorSpeed(kBinIdleSpeed);
			break;
		case HumanFeed_ToteOnConveyor:
			setBinElevatorSpeed(kBinIdleSpeed);
			break;
		case HumanFeed_ThrottleConveyorAndDescend:
			setBinElevatorSpeed(kBinIdleSpeed);
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

