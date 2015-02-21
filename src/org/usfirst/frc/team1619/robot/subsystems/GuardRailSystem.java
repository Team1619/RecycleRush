package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.StateMachine;
import org.usfirst.frc.team1619.robot.StateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;

/**
 *
 */
public class GuardRailSystem extends StateMachineSystem {
	public static final double kCloseGuardRailSpeed = -0.25;
	public static final double kOpenGuardRailSpeed = 0.15;
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private CANTalon guardRailMotor; //overdrive slightly
	
	private GuardRailSystem() {
    	guardRailMotor = new CANTalon(RobotMap.guardRailMotor);
    	guardRailMotor.enableLimitSwitch(false, false);
    	guardRailMotor.enableBrakeMode(false);
	}
	
	private static GuardRailSystem theSystem;
	
	public static GuardRailSystem getInstance() {
		if(theSystem == null)
			theSystem = new GuardRailSystem();
		return theSystem;
	}

	public void moveGuardRail(double speed)
	{
		guardRailMotor.set(speed);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    	//setDefaultCommand(new ManualGuardRailCommand(0.0));
    }

	@Override
	public void run(State state) {
		switch(state) {
		case Init:
			break;
		case Idle:
			break;
		case HumanFeed:
			if(StateMachine.getInstance().humanFeedTimer.get() <= 0.25) { //just at beginning
				moveGuardRail(kOpenGuardRailSpeed);
			}
			break;
		case GroundFeed:
			break;
		case Dropoff:
			break;
		case BinPickup:
			break;
		case Abort:	
			moveGuardRail(0.0);
			break;
		default:
			break;
		}
	}
}

