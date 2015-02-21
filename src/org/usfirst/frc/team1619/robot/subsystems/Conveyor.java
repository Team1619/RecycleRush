package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.StateMachine;
import org.usfirst.frc.team1619.robot.StateMachine.State;
import org.usfirst.frc.team1619.robot.commands.ManualConveyorCommand;
import org.usfirst.frc.team1619.robot.commands.ManualGuardRailCommand;
import org.usfirst.frc.team1619.robot.commands.UnloadConveyorCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.FixedInterruptHandler;
import edu.wpi.first.wpilibj.command.Command;

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
		
	private boolean runSlowConveyor = false;
	private double slowConveyorStartTime;
		
	private Conveyor() {
		conveyorMotor = new CANTalon(RobotMap.conveyorMotor);
		conveyorMotor.enableLimitSwitch(false, false);
    	conveyorMotor.enableBrakeMode(false);
    	
    	frontConveyorOpticalSensor = new DigitalInput(RobotMap.frontConveyorOpticalSensorID);
		rearConveyorOpticalSensor = new DigitalInput(RobotMap.rearConveyorOpticalSensorID);
	}
	
	private static Conveyor theSystem;
	
	public static Conveyor getInstance() {
		if(theSystem == null)
			theSystem = new Conveyor();
		return theSystem;
	}
	
	public void init() {
		rearConveyorOpticalSensor.requestInterrupts(new FixedInterruptHandler<Command>() {
			Command cmd = new UnloadConveyorCommand();
			//Command cmd = new ManualGuardRailCommand(GuardRailSystem.kCloseGuardRailSpeed); to be used when the bin stateMachine is implemented 
			
			public Command overridableParamater() {
				return cmd;
			}
			
			@Override
			protected void interruptFired2(int interruptAssertedMask,
					Command cmd) {
				cmd.start();
			}
			
		});
		rearConveyorOpticalSensor.setUpSourceEdge(false, true);
		rearConveyorOpticalSensor.enableInterrupts();
		
		frontConveyorOpticalSensor.requestInterrupts(new FixedInterruptHandler<Conveyor>() {
			Conveyor conveyor = Conveyor.getInstance();
			
			@SuppressWarnings("unused")
			public Conveyor overridablePramater() {
				return conveyor;
			}
			
			@Override
			protected void interruptFired2(int interruptAssertedMask, Conveyor conveyor) {
				conveyor.setRunSlowConveyor();
				new ManualGuardRailCommand(GuardRailSystem.kOpenGuardRailSpeed).start();
				ToteElevatorSystem.getInstance().setToteElevatorPosition(ToteElevatorSystem.kPickUpPosition);
			}
		});
		frontConveyorOpticalSensor.setUpSourceEdge(true, false);
		frontConveyorOpticalSensor.enableInterrupts();
	}

	public void setRunSlowConveyor() {
		runSlowConveyor = true;
		slowConveyorStartTime = StateMachine.getInstance().humanFeedTimer.get();
	}
	
	public void moveConveyor(double moveValue) {
		conveyorMotor.set(moveValue);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    	setDefaultCommand(new ManualConveyorCommand(1.0));
    }
    
    public boolean getFrontSensor() {
    	return !frontConveyorOpticalSensor.get();
    }
    public boolean getRearSensor() {
    	return !rearConveyorOpticalSensor.get();
    }

	@Override
	public void run(State state) {
		switch(state) {
		case Init:
			runSlowConveyor = false;
			break;
		case Idle:
			moveConveyor(0.0);
			break;
		case HumanFeed:
			if(runSlowConveyor)
			{
				moveConveyor(kSlowForwardConveyorSpeed);
				if(StateMachine.getInstance().humanFeedTimer.get() - slowConveyorStartTime >= 0.5) {
					runSlowConveyor = false;
				}
			}
			else {
				moveConveyor(kForwardConveyorSpeed);
			}
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

