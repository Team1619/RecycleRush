package org.usfirst.frc.team1619.robot.subsystems;

import java.util.ArrayList;

import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.commands.BinLiftSystemStateMachineCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class BinLiftSystem extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	public final CANTalon binElevatorMotor;
	public final CANTalon tilterMotor;
	public final CANTalon binGripMotor;
	public final CANTalon rakerMotor;
	
	
	private final Joystick rightStick;
	private final Joystick leftStick;
	
	private final JoystickButton binElevatorUpManualButton, binElevatorDownManualButton;
	private final JoystickButton binTiltUpManualButton, binTiltDownManualButton;
	private final JoystickButton binGripOpenManualButton, binGripCloseManualButton;
	private final JoystickButton rakerOpenManualButton, rakerCloseManualButton;
	
	private ArrayList<Signal> signals = new ArrayList<Signal>(); 
	public class BinLiftSystemSignal extends Signal {
		public BinLiftSystemSignal() {
			signals.add(this);
		}
	}
	
	public final Signal abortSignal = new BinLiftSystemSignal();
	public final Signal resetSignal = new BinLiftSystemSignal();
	public final Signal beginLiftSignal = new BinLiftSystemSignal();
	public final Signal tryLiftSignal = new BinLiftSystemSignal();
	public final Signal liftSignal = new BinLiftSystemSignal();
	public final Signal finishSignal = new BinLiftSystemSignal();
	public final Signal beginFeedSignal = new BinLiftSystemSignal();
	public final Signal tryFeedPickupSignal = new BinLiftSystemSignal();
	public final Signal fedTotePickupSignal = new BinLiftSystemSignal();
	public final Signal continueFeedingSignal = new BinLiftSystemSignal();
	public final Signal dropToteSignal = new BinLiftSystemSignal();
	
	private State eCurrentState = State.Init;
	
	private BinLiftSystem() {
		rightStick = OI.getInstance().rightStick;
		leftStick = OI.getInstance().leftStick;
		
		//left stick
		binElevatorUpManualButton = new JoystickButton(leftStick, RobotMap.binElevatorUpManualButtonID);
		binElevatorDownManualButton = new JoystickButton(leftStick, RobotMap.binElevatorDownManualButtonID);
		binTiltUpManualButton = new JoystickButton(leftStick, RobotMap.binTiltUpManualButtonID);
		binTiltDownManualButton = new JoystickButton(leftStick, RobotMap.binTiltDownManualButtonID);
		binGripOpenManualButton = new JoystickButton(leftStick, RobotMap.binGripOpenManualButtonID);
		binGripCloseManualButton = new JoystickButton(leftStick, RobotMap.binGripCloseManualButtonID);
		rakerOpenManualButton = new JoystickButton(leftStick, RobotMap.rakerOpenManualButtonID);
		rakerCloseManualButton = new JoystickButton(leftStick, RobotMap.rakerCloseManualButtonID);
		
		binElevatorMotor = new CANTalon(RobotMap.binElevatorMotor);
    	binElevatorMotor.enableLimitSwitch(true, true);
    	binElevatorMotor.enableBrakeMode(true);
    	
    	tilterMotor = new CANTalon(RobotMap.tilterMotor);
    	tilterMotor.enableLimitSwitch(true, true);
    	tilterMotor.enableBrakeMode(false);
    	tilterMotor.ConfigFwdLimitSwitchNormallyOpen(false);
    	tilterMotor.ConfigRevLimitSwitchNormallyOpen(false);
    	
    	binGripMotor = new CANTalon(RobotMap.binGripMotor);
    	binGripMotor.enableLimitSwitch(false, false);
    	binGripMotor.enableBrakeMode(false);
    	
    	rakerMotor = new CANTalon(RobotMap.rakerMotor);
    	rakerMotor.enableLimitSwitch(false, false);
    	rakerMotor.enableBrakeMode(true);
	}
	
	private static BinLiftSystem theSystem;
	
	public static BinLiftSystem getInstance() {
		if(theSystem == null)
			theSystem = new BinLiftSystem();
		return theSystem;
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    	setDefaultCommand(new BinLiftSystemStateMachineCommand());
    }
    
    //manual commands
    double binElevatorSpeed;
    double binGripSpeed;
    double rakerSpeed;
    double tilterMotorSpeed;
    
    public void moveBinElevator(double moveValue) {
    	binElevatorSpeed = moveValue;
    }
    private void binElevatorUpdate() {
    	if(binElevatorUpManualButton.get())
    		binElevatorMotor.set(0.1);
    	else if(binElevatorDownManualButton.get())
    		binElevatorMotor.set(-0.1);
    	else
    		binElevatorMotor.set(binElevatorSpeed);
    }
    
    public void binTilt(double moveValue) {
    	tilterMotorSpeed = moveValue;
    }
    private void binTiltUpdate() {
    	if(binTiltUpManualButton.get())
        	tilterMotor.set(1);
    	else if(binTiltDownManualButton.get())
        	tilterMotor.set(-1);
    	else
    		tilterMotor.set(tilterMotorSpeed);
    }
    
    public void moveBinGrip(double moveValue) {
    	binGripSpeed = moveValue;
    }
    private void binGripUpdate() {
    	if(binGripOpenManualButton.get())
        	binGripMotor.set(0.1);
    	else if(binGripCloseManualButton.get())
    		binGripMotor.set(-0.1);
    	else
    		binGripMotor.set(binGripSpeed);
    }
    
    public void moveRaker(double moveValue) {
    	rakerSpeed = moveValue;
    }
    private void rakerUpdate() {
    	if(rakerOpenManualButton.get())
    		rakerMotor.set(0.25);
    	else if(rakerCloseManualButton.get())
    		rakerMotor.set(-0.25);
    	else
    		rakerMotor.set(rakerSpeed);
    }
    
    enum State {
    	Init {
    		State run(BinLiftSystem liftSystem) {
    			if(!liftSystem.binElevatorMotor.isRevLimitSwitchClosed()) {
    				//liftSystem.moveToteElevator(-0.3);    				
    			}
    			if(!liftSystem.binElevatorMotor.isRevLimitSwitchClosed()) {
    				//liftSystem.moveBinElevator(-0.2);    				
    			}
    			
    			if(liftSystem.binElevatorMotor.isRevLimitSwitchClosed() && liftSystem.binElevatorMotor.isRevLimitSwitchClosed()) {
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
    		State run(BinLiftSystem liftSystem) {
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
    		State run(BinLiftSystem liftSystem) {
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
    		State run(BinLiftSystem liftSystem) {
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
    		State run(BinLiftSystem liftSystem) {
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
    		State run(BinLiftSystem liftSystem) {
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
    		State run(BinLiftSystem liftSystem) {
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
    		State run(BinLiftSystem liftSystem) {
    			return Idle;
    		}
    		
    		public String toString() {
    			return "Dropoff";
    		}
    	};
    	
    	
    	abstract State run(BinLiftSystem liftSystem);
    	
    	void init(BinLiftSystem liftSystem) {}
    }
    
    public void runStateMachine() {
    	State eNextState = eCurrentState;

    	eCurrentState = State.Idle;
    	//System.out.println("Current State: " + eCurrentState);
    	
    	eNextState = eCurrentState.run(this);
    	
    	binGripUpdate();
    	binTiltUpdate();
    	binElevatorUpdate();
    	rakerUpdate();
    	
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
