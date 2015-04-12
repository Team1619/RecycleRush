package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.Preferences;
import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.StateMachine;
import org.usfirst.frc.team1619.robot.StateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 */
public class ConveyorSystem extends StateMachineSystem {
	//Competition Bot
	public static final double kForwardConveyorSpeed = -1.0; 
	public static final double kSlowForwardConveyorSpeed = -0.4;
	public static final double kManualForwardConveyorSpeed = -1.0; 
	public static final double kManualBackConveyorSpeed = 1.0;
	private static final double kConveyorDelayTime = 0.25;
	
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
	
		
	private ConveyorSystem() {
		conveyorMotor = RobotMap.MotorDefinition.conveyorMotor.getMotor();
		conveyorMotor.changeControlMode(ControlMode.PercentVbus);
		conveyorMotor.enableLimitSwitch(false, false);
    	conveyorMotor.enableBrakeMode(false);
    	
    	frontConveyorOpticalSensor = new DigitalInput(RobotMap.frontConveyorOpticalSensorID);
		rearConveyorOpticalSensor = new DigitalInput(RobotMap.rearConveyorOpticalSensorID);

		frontSensorDebounceTimer.start();
		rearSensorDebounceTimer.start();
	}
	
	private static ConveyorSystem theSystem;
	
	public static ConveyorSystem getInstance() {
		if(theSystem == null)
			theSystem = new ConveyorSystem();
		return theSystem;
	}
	
	public void updateConveyorSignals() {
    	//double debounceTime = Preferences.getNumber("DebounceTime", kDebounceTime);
		
		if(!getFrontSensorRaw()) {
			if(frontSensor) {
				//frontSensor = !(frontSensorDebounceTimer.get() > kDebounceTime);
				frontSensor = !frontSensor;
				if(!frontSensor) {
					StateMachine.getInstance().humanFeed_ThrottleConveyorDescend.raise();
				}
			}
		}
		else {
			frontSensor = true;
			frontSensorDebounceTimer.reset();
			StateMachine.getInstance().humanFeed_ThrottleConveyorBack.raise();
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
    	
    	double forwardConveyorSpeed = Preferences.getNumber("ForwardConveyorSpeed", kForwardConveyorSpeed);
    	double slowForwardConveyorSpeed = Preferences.getNumber("SlowForwardConveyorSpeed", kSlowForwardConveyorSpeed);
    	double conveyorDelayTime = Preferences.getNumber("ConveyorDelayTime", kConveyorDelayTime);
    	
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
    		conveyorSpeed = 0.0;
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

