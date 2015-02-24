package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.StateMachine;
import org.usfirst.frc.team1619.robot.StateMachine.Signal;
import org.usfirst.frc.team1619.robot.StateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.FixedInterruptHandler;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 */
public class Conveyor extends StateMachineSystem {
	private static final double kForwardConveyorSpeed = 0.75;
	private static final double kSlowForwardConveyorSpeed = 0.2;
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private CANTalon conveyorMotor; //multiple speeds based on optical sensor configuration
	private DigitalInput frontConveyorOpticalSensor;
	private DigitalInput rearConveyorOpticalSensor;

	private boolean frontSensor = false;
	private boolean rearSensor = false;

	private Timer frontSensorDebounceTimer = new Timer();
	private Timer rearSensorDebounceTimer = new Timer();
	private final double kDebounceTime = 0.1;
		
	private Conveyor() {
		conveyorMotor = new CANTalon(RobotMap.conveyorMotor);
		conveyorMotor.enableLimitSwitch(false, false);
    	conveyorMotor.enableBrakeMode(false);
    	
    	frontConveyorOpticalSensor = new DigitalInput(RobotMap.frontConveyorOpticalSensorID);
		rearConveyorOpticalSensor = new DigitalInput(RobotMap.rearConveyorOpticalSensorID);
		
		frontSensorDebounceTimer.start();
		rearSensorDebounceTimer.start();
	}
	
	private static Conveyor theSystem;
	
	public static Conveyor getInstance() {
		if(theSystem == null)
			theSystem = new Conveyor();
		return theSystem;
	}
	
	public void init() {
		/*
		rearConveyorOpticalSensor.requestInterrupts(new FixedInterruptHandler<Object>() {
			private Signal signal = StateMachine.getInstance().humanPlayerFeed_ToteOnConveyor;
			
			public Object overridableParamater() {
				return null;
			}
			
			@Override
			protected void interruptFired2(int interruptAssertedMask,
					Object object) {
				signal.raise();
			}
			
		});
		rearConveyorOpticalSensor.setUpSourceEdge(false, true);
		rearConveyorOpticalSensor.enableInterrupts();
		
		frontConveyorOpticalSensor.requestInterrupts(new FixedInterruptHandler<Object>() {
			private Signal signal = StateMachine.getInstance().humanPlayerFeed_ThrottleConveyorDescend;
			
			@SuppressWarnings("unused")
			public Object overridablePramater() {
				return null;
			}
			
			@Override
			protected void interruptFired2(int interruptAssertedMask, Object object) {
				signal.raise();
			}
		});
		frontConveyorOpticalSensor.setUpSourceEdge(true, false);
		frontConveyorOpticalSensor.enableInterrupts();
		*/
	}
	
	public void updateConveyorSignals() {
		if(!getFrontSensorRaw()) {
			if(frontSensor) {
				frontSensor = !(frontSensorDebounceTimer.get() > kDebounceTime);
				if(!frontSensor) {
					StateMachine.getInstance().humanPlayerFeed_ThrottleConveyorDescend.raise();
				}
			}
		}
		else {
			frontSensor = false;
			frontSensorDebounceTimer.reset();
		}
		if(getRearSensorRaw()) {
			if(!rearSensor) {
				rearSensor = rearSensorDebounceTimer.get() > kDebounceTime;
				if(rearSensor) {
					StateMachine.getInstance().humanPlayerFeed_ToteOnConveyor.raise();
				}
			}
		}
		else {
			rearSensor = false;
			rearSensorDebounceTimer.reset();
		}
	}
	
	public void moveConveyor(double moveValue) {
		conveyorMotor.set(moveValue);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    	//setDefaultCommand(new ManualConveyorCommand());
    }
    
    public boolean getFrontSensorRaw() {
    	return !frontConveyorOpticalSensor.get();
    }
    public boolean getRearSensorRaw() {
    	return !rearConveyorOpticalSensor.get();
    }

    public boolean getFrontSensor() {
    	return frontSensor;
    }
    public boolean getRearSensor() {
    	return rearSensor;
    }
    

	@Override
	public void run(State state, double elapsed) {
    	updateConveyorSignals();
		switch(state) {
		case Init:
			break;
		case Idle:
			moveConveyor(0.0);
			break;
		case HumanFeed_RaiseTote:
			moveConveyor(kForwardConveyorSpeed);
			break;
		case HumanFeed_WaitForTote:
			moveConveyor(kForwardConveyorSpeed);
			break;
		case HumanFeed_ToteOnConveyor:
			moveConveyor(kForwardConveyorSpeed);
			break;
		case HumanFeed_ThrottleConveyorDescend:
			moveConveyor(kSlowForwardConveyorSpeed);
			break;
		case GroundFeed:
			break;
		case Dropoff:
			break;
		case BinPickup:
			break;
		case Abort:	
			moveConveyor(0.0);
			break;
		default:
			break;
		}
	}
}

