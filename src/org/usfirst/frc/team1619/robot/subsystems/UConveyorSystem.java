package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.UOI;
import org.usfirst.frc.team1619.robot.URobotMap;
import org.usfirst.frc.team1619.robot.UStateMachine;
import org.usfirst.frc.team1619.robot.UStateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 */
public class UConveyorSystem extends UStateMachineSystem {
	//Competition Bot
	public static final double kForwardConveyorSpeed = -0.55; 
	public static final double kSlowForwardConveyorSpeed = -0.55;
	public static final double kManualForwardConveyorSpeed = -1.0; 
	public static final double kManualBackConveyorSpeed = 1.0;
	private static final double kConveyorDelayTime = 0.4;
	
	//Practice Bot
//	private static final double kForwardConveyorSpeed = -0.7; 
//	private static final double kManualForwardConveyorSpeed = -0.7; 
//	private static final double kManualBackConveyorSpeed = 0.7; 
	
	
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	public final CANTalon conveyorMotor; //multiple speeds based on optical sensor configuration
	private DigitalInput frontConveyorOpticalSensor;
	private DigitalInput rearConveyorOpticalSensor;

	private boolean frontSensor = false;
	private boolean rearSensor = false;

	private Timer frontSensorDebounceTimer = new Timer();
	private Timer rearSensorDebounceTimer = new Timer();
	private final double kDebounceTime = 0.0;
	
	private UConveyorSystem() {
		conveyorMotor = URobotMap.MotorDefinition.conveyorMotor.getMotor();
		conveyorMotor.changeControlMode(ControlMode.PercentVbus);
		conveyorMotor.enableLimitSwitch(false, false);
    	conveyorMotor.enableBrakeMode(false);
    	
    	frontConveyorOpticalSensor = new DigitalInput(URobotMap.frontConveyorOpticalSensorID);
		rearConveyorOpticalSensor = new DigitalInput(URobotMap.rearConveyorOpticalSensorID);

		frontSensorDebounceTimer.start();
		rearSensorDebounceTimer.start();
	}
	
	private static UConveyorSystem theSystem;
	
	public static UConveyorSystem getInstance() {
		if(theSystem == null)
			theSystem = new UConveyorSystem();
		return theSystem;
	}
	
	public void updateConveyorSignals() {
		
		if(!getFrontSensorRaw()) {
			if(frontSensor) {
				frontSensor = !frontSensor;
				if(!frontSensor) {
					UStateMachine.getInstance().humanFeed_ThrottleConveyorDescend.raise();
				}
			}
		}
		else {
			if(!frontSensor) {
				frontSensor = true;
				frontSensorDebounceTimer.reset();
				UStateMachine.getInstance().humanFeed_ThrottleConveyorBack.raise();
			}
		}
		if(getRearSensorRaw()) {
			rearSensor = rearSensorDebounceTimer.get() > kDebounceTime;
			if(rearSensor) {
				UStateMachine.getInstance().humanFeed_ToteOnConveyor.raise();
			}
		}
		else {
			rearSensor = false;
			rearSensorDebounceTimer.reset();
		}
	}
	private double conveyorSpeed = 0.0;
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
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
    
    private void updateConveyor() {
    	if(UOI.getInstance().conveyorForwardButton.get()) {
			conveyorMotor.set(kManualForwardConveyorSpeed);
		}
		else if(UOI.getInstance().conveyorBackButton.get()) {
			conveyorMotor.set(kManualBackConveyorSpeed);
		}
		else {
			conveyorMotor.set(conveyorSpeed);	
		}
    }

    @Override
    public void init(State state) {
    	switch (state) {
    	default:
    		break;
    	}
    };

    @Override
    public void run(State state, double elapsed) {
    	updateConveyorSignals();
    	
    	double forwardConveyorSpeed = kForwardConveyorSpeed;
    	double slowForwardConveyorSpeed = kSlowForwardConveyorSpeed;
    	double conveyorDelayTime = kConveyorDelayTime;
    	
    	switch(state) {
    	case Init:
    		conveyorSpeed = 0.0;
    		break;
    	case Idle:
    		conveyorSpeed = 0.0;
    		break;
    	case HumanFeed_RaiseTote:
    		conveyorSpeed = forwardConveyorSpeed;
    		break;
    	case HumanFeed_WaitForTote:
    		conveyorSpeed = forwardConveyorSpeed;
    		break;
    	case HumanFeed_ToteOnConveyor:
    		conveyorSpeed = forwardConveyorSpeed;
    		break;
    	case HumanFeed_ThrottleConveyorBack:
    		conveyorSpeed = elapsed > conveyorDelayTime ? slowForwardConveyorSpeed : forwardConveyorSpeed;
    		break;
    	case HumanFeed_ThrottleConveyorAndDescend:
    		conveyorSpeed = forwardConveyorSpeed;
    		break;
		case Abort:	
			conveyorSpeed = 0.0;
			break;
		default:
			break;
		}
		
		updateConveyor();
	}
}

