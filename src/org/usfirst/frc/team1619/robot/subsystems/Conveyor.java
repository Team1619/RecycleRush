package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.StateMachine;
import org.usfirst.frc.team1619.robot.StateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Conveyor extends StateMachineSystem {
	
	private static final double kForwardConveyorSpeed = -1.0; 
	private static final double kManualForwardConveyorSpeed = -1.0; 
	private static final double kManualBackConveyorSpeed = 1.0; 
	
	private static final double kConveyorDelayTime = 0.25;
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private CANTalon conveyorMotor; //multiple speeds based on optical sensor configuration
	private DigitalInput frontConveyorOpticalSensor;
	private DigitalInput rearConveyorOpticalSensor;

	private boolean frontSensor = false;
	private boolean rearSensor = false;

	private Timer frontSensorDebounceTimer = new Timer();
	private Timer rearSensorDebounceTimer = new Timer();
	private Timer frontSensorFedDelay = new Timer();
	private final double kDebounceTime = 0.05;
	
		
	private Conveyor() {
		conveyorMotor = RobotMap.MotorDefinition.conveyorMotor.getMotor();
		conveyorMotor.changeControlMode(ControlMode.PercentVbus);
		conveyorMotor.enableLimitSwitch(false, false);
    	conveyorMotor.enableBrakeMode(false);
    	
    	frontConveyorOpticalSensor = new DigitalInput(RobotMap.frontConveyorOpticalSensorID);
		rearConveyorOpticalSensor = new DigitalInput(RobotMap.rearConveyorOpticalSensorID);

		frontSensorDebounceTimer.start();
		rearSensorDebounceTimer.start();
		frontSensorFedDelay.start();
	}
	
	private static Conveyor theSystem;
	
	public static Conveyor getInstance() {
		if(theSystem == null)
			theSystem = new Conveyor();
		return theSystem;
	}
	
	public void updateConveyorSignals() {
		if(!getFrontSensorRaw()) {
			if(frontSensor) {
				frontSensor = !(frontSensorDebounceTimer.get() > kDebounceTime);
				if(!frontSensor) {
					StateMachine.getInstance().humanFeed_ThrottleConveyorDescend.raise();
				}
			}
		}
		else {
			frontSensor = true;
			frontSensorDebounceTimer.reset();
		}
		if(getRearSensorRaw()) {
			rearSensor = rearSensorDebounceTimer.get() > kDebounceTime;
			if(rearSensor) {
				StateMachine.getInstance().humanFeed_ToteOnConveyor.raise();
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
    
    private void updateConveyor() {
    	if(OI.getInstance().conveyorForwardButton.get()) {
			conveyorMotor.set(kManualForwardConveyorSpeed);
		}
		else if(OI.getInstance().conveyorBackButton.get()) {
			conveyorMotor.set(kManualBackConveyorSpeed);
		}
		else {
			conveyorMotor.set(conveyorSpeed);	
		}
    	
    	SmartDashboard.putNumber("conveyorMotor.getOutputCurrent()", conveyorMotor.getOutputCurrent());
    	SmartDashboard.putNumber("conveyorMotor.getOutputVoltage()", conveyorMotor.getOutputVoltage());
    }

    @Override
    public void init(State state) {
    	switch (state) {
    	case HumanFeed_ThrottleConveyorAndDescend:
    		frontSensorFedDelay.reset();
    		break;
    	default:
    		break;
    	}
    };

    @Override
    public void run(State state, double elapsed) {
    	updateConveyorSignals();
    	switch(state) {
    	case Init:
    		conveyorSpeed = 0.0;
    		break;
    	case Idle:
    		conveyorSpeed = 0.0;
    		break;
    	case HumanFeed_RaiseTote:
    		conveyorSpeed = kForwardConveyorSpeed;
    		break;
    	case HumanFeed_WaitForTote:
    		conveyorSpeed = kForwardConveyorSpeed;
    		break;
    	case HumanFeed_ToteOnConveyor:
    		conveyorSpeed = kForwardConveyorSpeed;
    		break;
    	case HumanFeed_ThrottleConveyorAndDescend:
    		if(frontSensorFedDelay.get() >= kConveyorDelayTime && StateMachine.getInstance().getToStopHumanFeed()) {
    			conveyorSpeed = 0.0;
    		}
    		else {
    			conveyorSpeed = kForwardConveyorSpeed;
    		}
    		break;
//		case GroundFeed:
//			break;
//		case Dropoff:
//			break;
		case Abort:	
			conveyorSpeed = 0.0;
			break;
		default:
			break;
		}
		
		updateConveyor();
	}
}

