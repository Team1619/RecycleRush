package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.StateMachine;
import org.usfirst.frc.team1619.robot.StateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class GuardRailSystem extends StateMachineSystem {
	public static final double kCloseGuardRailSpeed = -0.75;
	public static final double kOpenGuardRailSpeed = 0.40;
	public static final double kSlowOpenGuardRailSpeed = 0.00;
	public static final double kSlowCloseGuardRailSpeed = -0.10;
	
	private Timer humanFeedCloseTimer = new Timer();
	private boolean closeInHumanFeed = false;
	
	private final JoystickButton guardRailOpenButton;
	private final JoystickButton guardRailCloseButton;
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private CANTalon guardRailMotor; //overdrive slightly
	
	private GuardRailSystem() {
    	guardRailMotor = new CANTalon(RobotMap.guardRailMotor);
    	guardRailMotor.changeControlMode(ControlMode.PercentVbus);
    	guardRailMotor.enableLimitSwitch(false, false);
    	guardRailMotor.enableBrakeMode(false);
    	
    	humanFeedCloseTimer.start();
    	
    	guardRailOpenButton = OI.getInstance().guardRailOpenButton;
		guardRailCloseButton = OI.getInstance().guardRailCloseButton;
	}
	
	private static GuardRailSystem theSystem;
	
	public static GuardRailSystem getInstance() {
		if(theSystem == null)
			theSystem = new GuardRailSystem();
		return theSystem;
	}

	private double guardRailSpeed = 0.0;
	
	private void updateGuardRail() {
		if(guardRailOpenButton.get()) {
			guardRailMotor.set(kOpenGuardRailSpeed);
		}
		else if(guardRailCloseButton.get()) {
			guardRailMotor.set(kCloseGuardRailSpeed);
		}
		else {
			guardRailMotor.set(guardRailSpeed);
		}
		
		SmartDashboard.putNumber("guardRail.getOutputCurrent()", guardRailMotor.getOutputCurrent());
    	SmartDashboard.putNumber("guardRail.getOutputVoltage()", guardRailMotor.getOutputVoltage());
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    	//setDefaultCommand(new ManualGuardRailCommand(0.0));
    }

    public void init(State state) {
    	guardRailSpeed = 0.0;
    	switch(state) {
    	case Init:
    		break;
    	case HumanFeed_RaiseTote:
    		closeInHumanFeed = true;
    		break;
    	default:
    		break;
    	}
    }
    
	@Override
	public void run(State state, double elapsed) {
		switch(state) {
		case Init:
			break;
		case Idle:
			guardRailSpeed = 0.0;
			break;
		case HumanFeed_RaiseTote:
			if(!Conveyor.getInstance().getRearSensor()) {
				if(elapsed <= 0.25) { //just at beginning
					guardRailSpeed = kOpenGuardRailSpeed;
				}
				else {
					guardRailSpeed = kSlowOpenGuardRailSpeed;
				}
			}
			else {
				if(closeInHumanFeed){
					humanFeedCloseTimer.reset();
					closeInHumanFeed = false;
				}
				if(humanFeedCloseTimer.get() <= 1.0) {
					guardRailSpeed = kCloseGuardRailSpeed;
				}
				else {
					guardRailSpeed = kSlowCloseGuardRailSpeed;
				}
			} 
			break;
		case HumanFeed_WaitForTote:
			guardRailSpeed = kSlowOpenGuardRailSpeed;
			break;
		case HumanFeed_ToteOnConveyor:
			if(elapsed <= 1.0) {
				guardRailSpeed = kCloseGuardRailSpeed;
			}
			else {
				guardRailSpeed = kSlowCloseGuardRailSpeed;
			}
			break;
		case HumanFeed_ThrottleConveyorAndDescend:
			if(StateMachine.getInstance().getToStopHumanFeed()) {
				guardRailSpeed = 0.0;
			}
			else {
				guardRailSpeed = kOpenGuardRailSpeed;	
			}
			break;
//		case GroundFeed:
//			break;
//		case Dropoff:
//			break;
		case Abort:	
			guardRailSpeed = 0.0;
			break;
		default:
			break;
		}
		
		updateGuardRail();
	}
}

