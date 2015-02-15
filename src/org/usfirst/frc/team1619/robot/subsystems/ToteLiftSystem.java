package org.usfirst.frc.team1619.robot.subsystems;

import java.util.ArrayList;

import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.commands.ToteLiftSystemStateMachineCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class ToteLiftSystem extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	public final CANTalon toteElevatorMotor;
	public final CANTalon toteElevatorMotorSmall;
	
	private final Joystick rightStick;
	private final Joystick leftStick;
	
	private final JoystickButton toteElevatorDownManualButton, toteElevatorUpManualButton;
	
	private ArrayList<Signal> signals = new ArrayList<Signal>(); 
	public class ToteLiftSystemSignal extends Signal {
		public ToteLiftSystemSignal() {
			signals.add(this);
		}
	}

	public final Signal abortSignal = new ToteLiftSystemSignal();
	public final Signal resetSignal = new ToteLiftSystemSignal();
	public final Signal beginLiftSignal = new ToteLiftSystemSignal();
	public final Signal tryLiftSignal = new ToteLiftSystemSignal();
	public final Signal liftSignal = new ToteLiftSystemSignal();
	public final Signal finishSignal = new ToteLiftSystemSignal();
	public final Signal beginFeedSignal = new ToteLiftSystemSignal();
	public final Signal tryFeedPickupSignal = new ToteLiftSystemSignal();
	public final Signal fedTotePickupSignal = new ToteLiftSystemSignal();
	public final Signal continueFeedingSignal = new ToteLiftSystemSignal();
	public final Signal dropToteSignal = new ToteLiftSystemSignal();
	
	private State eCurrentState = State.Init;
	
	
	private ToteLiftSystem() {
		rightStick = OI.getInstance().rightStick;
		leftStick = OI.getInstance().leftStick;
		
		//right stick
		toteElevatorUpManualButton = new JoystickButton(rightStick, RobotMap.toteElevatorUpManualButtonID);
		toteElevatorDownManualButton = new JoystickButton(rightStick, RobotMap.toteElevatorDownManualButtonID);
		
		toteElevatorMotor = new CANTalon(RobotMap.toteElevatorMotor);
    	toteElevatorMotor.enableLimitSwitch(false, false);
    	toteElevatorMotor.enableBrakeMode(true);
    	
    	toteElevatorMotorSmall = new CANTalon(RobotMap.spare);
    	toteElevatorMotorSmall.enableLimitSwitch(false, false);
    	toteElevatorMotorSmall.enableBrakeMode(true);
	}
	
	private static ToteLiftSystem theSystem;
	
	public static ToteLiftSystem getInstance() {
		if(theSystem == null)
			theSystem = new ToteLiftSystem();
		return theSystem;
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new ToteLiftSystemStateMachineCommand());
    }
    
    //Manual commands
    double toteElevatorSpeed;
    
    public void moveToteElevator(double moveValue) {
    	toteElevatorSpeed = moveValue;
    }
    private void toteElevatorUpdate() {
    	/*
    	if(toteElevatorUpManualButton.get())
        	toteElevatorMotor.set(0.1);
    	else if(toteElevatorDownManualButton.get())
    		toteElevatorMotor.set(-0.1);
    	else
    		toteElevatorMotor.set(toteElevatorSpeed);
    		*/
    	//Following code is temporary, although controlling the tote elevator
    	//with a joystick is likely a good idea.
    	toteElevatorMotor.set(leftStick.getY() * 1);
    	toteElevatorMotorSmall.set(leftStick.getY() * -1);
    }
    
    /*
    public void moveRaker(double moveValue) {
    	rakerSpeed = moveValue;
    }
    private void rakerUpdate() {
    	rakerMotor.set(rakerSpeed);
    }*/
    
    
    enum State {
    	Init {
    		State run(ToteLiftSystem liftSystem) {
    			if(!liftSystem.toteElevatorMotor.isRevLimitSwitchClosed()) {
    				//liftSystem.moveToteElevator(-0.3);    				
    			}
    			if(!liftSystem.toteElevatorMotor.isRevLimitSwitchClosed()) {
    				//liftSystem.moveBinElevator(-0.2);    				
    			}
    			
    			if(liftSystem.toteElevatorMotor.isRevLimitSwitchClosed() && liftSystem.toteElevatorMotor.isRevLimitSwitchClosed()) {
    				//liftSystem.toteElevatorMotor.setPosition(0.0);
    				//liftSystem.binElevatorMotor.setPosition(0.0);
    				return Idle;	
    			}
    			return Init;
    			
    		}
    		
    		public String toString() {
    			return "Init";
    		}
    	},
    	Idle {
    		State run(ToteLiftSystem liftSystem) {
    			if(liftSystem.resetSignal.check()) {
    				return Init;
    			}
    			if(liftSystem.beginLiftSignal.check()) {
    				return BeginStack;
    			}
    			if(liftSystem.beginFeedSignal.check()) {
    				return BeginFeed;
    			}
    			if(liftSystem.dropToteSignal.check()) {
    				return Dropoff;
    			}
    			return Idle;
    		}
    		
    		public String toString() {
    			return "Idle";
    		}
    	},
    	
    	BeginStack {
    		State run(ToteLiftSystem liftSystem) {
    			if(liftSystem.abortSignal.check()) {
    				return Idle;
    			}
    			if(liftSystem.tryLiftSignal.check()) {
    				return StackForFeed;
    			}
    			return Idle;
    		}
    		
    		public String toString() {
    			return "Begin Stack";
    		}
    		
    	},
    	BeginFeed {
    		State run(ToteLiftSystem liftSystem) {
    			if(liftSystem.abortSignal.check()) {
    				return Idle;
    			}
    			if(liftSystem.tryFeedPickupSignal.check()) {
    				return StackForFeed;
    			}
    			return Idle;
    		}
    		
    		public String toString() {
    			return "Begin Feed";
    		}
    	},
    	StackForFeed {
    		State run(ToteLiftSystem liftSystem) {
    			if(liftSystem.abortSignal.check()) {
    				return BeginFeed;
    			}
    			if(liftSystem.resetSignal.check()) {
    				return Init;
    			}
    			if(liftSystem.liftSignal.check()) {
    				return Pickup;
    			}
    			return Idle;
    		}
    		
    		public String toString() {
    			return "Stack For Feed";
    		}
    	},
    	StackForPickup {
    		State run(ToteLiftSystem liftSystem) {
    			if(liftSystem.abortSignal.check()) {
    				return BeginStack;
    			}
    			if(liftSystem.resetSignal.check()) {
    				return Idle;
    			}
    			if(liftSystem.fedTotePickupSignal.check()) {
    				return Pickup;
    			}
    			return Idle;
    		}
    		
    		public String toString() {
    			return "Stack For Pickup";
    		}
    	},
    	Pickup {
    		State run(ToteLiftSystem liftSystem) {
    			if(liftSystem.abortSignal.check()) {
    				return Idle; //There will need to be more logic here
    				//We need to handle finding the previous state
    			}
    			if(liftSystem.resetSignal.check()) {
    				return Idle;
    			}
    			if(liftSystem.finishSignal.check()) {
    				return Idle;
    			}
    			if(liftSystem.continueFeedingSignal.check()) {
    				return BeginFeed;
    			}
    			return Idle;
    		}
    		
    		public String toString() {
    			return "Pickup";
    		}
    	},
    	Dropoff {
    		State run(ToteLiftSystem liftSystem) {
    			return Idle;
    		}
    		
    		public String toString() {
    			return "Dropoff";
    		}
    	};
    	
    	
    	abstract State run(ToteLiftSystem liftSystem);
    	
    	void init(ToteLiftSystem liftSystem) {}
    }
    
    
    public void runStateMachine() {
    	State eNextState = eCurrentState;

    	eCurrentState = State.Idle;
    	//System.out.println("Current State: " + eCurrentState);
    	
    	eNextState = eCurrentState.run(this);
    	
    	toteElevatorUpdate();
    	
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

