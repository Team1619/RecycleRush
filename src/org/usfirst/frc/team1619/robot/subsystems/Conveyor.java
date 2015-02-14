package org.usfirst.frc.team1619.robot.subsystems;

import java.util.ArrayList;

import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.commands.ConveyorStateMachineCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Conveyor extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private CANTalon conveyorMotor; //multiple speeds based on optical sensor configuration
	private CANTalon guardRailMotor; //overdrive slightly
	
	private double conveyorSpeed;
	private double guardRailSpeed;
	
	private final JoystickButton conveyorForwardManualButton;
	private final JoystickButton conveyorBackwardManualButton;
	private final JoystickButton guardRailCloseManualButton;
	private final JoystickButton guardRailOpenManualButton;
	
	private Joystick leftStick;
	
	private State eCurrentState = State.Init;
	
	private ArrayList<Signal> signals = new ArrayList<Signal>(); 
	public class LiftSystemSignal extends Signal {
		public LiftSystemSignal() {
			signals.add(this);
		}
	}
	
	private Conveyor() {
		leftStick = OI.getInstance().leftStick;
		conveyorMotor = new CANTalon(RobotMap.conveyorMotor);
		conveyorMotor.enableLimitSwitch(false, false);
    	conveyorMotor.enableBrakeMode(false);

    	guardRailMotor = new CANTalon(RobotMap.guardRailMotor);
    	guardRailMotor.enableLimitSwitch(false, false);
    	guardRailMotor.enableBrakeMode(false);
    	
    	conveyorSpeed = 0.0;
    	guardRailSpeed = 0.0;
    	
    	conveyorForwardManualButton = new JoystickButton(leftStick, RobotMap.conveyorForwardManualButtonID);
		conveyorBackwardManualButton = new JoystickButton(leftStick, RobotMap.conveyorBackwardManualButtonID);
		guardRailCloseManualButton = new JoystickButton(leftStick, RobotMap.guardrailCloseManualButtonID);
		guardRailOpenManualButton = new JoystickButton(leftStick, RobotMap.guardrailOpenManualButtonID);
	}
	
	private static final Conveyor theSystem = new Conveyor();
	
	public static Conveyor getInstance() {
		return theSystem;
	}

	public void moveConveyor(double moveValue) {
		conveyorSpeed = moveValue;
	}
	public void updateConveyor()
	{
		if(conveyorForwardManualButton.get())
    		conveyorMotor.set(1.0);
    	else if(conveyorBackwardManualButton.get())
    		conveyorMotor.set(-1.0);
    	else
    		conveyorMotor.set(conveyorSpeed);
	}
	
	public void moveGuardRail(double moveValue) {
		guardRailSpeed = moveValue;
	}
	public void updateGuardRail()
	{
		if(guardRailOpenManualButton.get())
    		guardRailMotor.set(0.15);
    	else if(guardRailCloseManualButton.get())
    		guardRailMotor.set(-0.25);
    	else
    		guardRailMotor.set(guardRailSpeed);

	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    	setDefaultCommand(new ConveyorStateMachineCommand());
    }
    
    enum State {
    	Init {
    		State run(Conveyor conveyor) {
    			return Idle;
    		}
    		
    		public String toString() {
    			return "Init";
    		}
    	},
    	Idle {
    		State run(Conveyor conveyor) {
    			conveyor.moveGuardRail(0.0);
    			return Idle;
    		}
    		
    		public String toString() {
    			return "Idle";
    		}
    	};
    	
    	
    	abstract State run(Conveyor conveyor);
    	
    	void init(Conveyor conveyor) {}
    }
    
    public void runStateMachine()
    {
    	State eNextState = eCurrentState;
    	
    	eCurrentState = State.Idle;
    	
    	eNextState = eCurrentState.run(this);
    	
    	updateConveyor();
    	updateGuardRail();
    	
    	for(Signal signal: signals) {
    		signal.clear();
    	}
    	
    	if(eNextState != eCurrentState) {
        	//System.out.println("Next State: " + stateToString(nextState));
        	eCurrentState = eNextState;
        	eCurrentState.init(this);
    	}
    }
}

