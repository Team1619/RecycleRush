package org.usfirst.frc.team1619.robot.subsystems;

import java.util.ArrayList;

import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.commands.LiftSystemStateMachineCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class LiftSystem extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	public final CANTalon toteElevatorMotor;
	public final CANTalon binElevatorMotor;
	public final CANTalon tilterMotor;
	public final CANTalon binGripMotor;
	public final CANTalon rakerMotor;
	
	private final Joystick rightStick;
	private final Joystick leftStick;
	
	private final JoystickButton toteElevatorDownManualButton, toteElevatorUpManualButton;
	private final JoystickButton binElevatorUpManualButton, binElevatorDownManualButton;
	private final JoystickButton binTiltUpManualButton, binTiltDownManualButton;
	private final JoystickButton binGripOpenManualButton, binGripCloseManualButton;

	private ArrayList<Signal> signals = new ArrayList<Signal>(); 
	public class LiftSystemSignal extends Signal {
		public LiftSystemSignal() {
			signals.add(this);
		}
	}

	public final Signal abortSignal = new LiftSystemSignal();
	public final Signal resetSignal = new LiftSystemSignal();
	public final Signal beginLiftSignal = new LiftSystemSignal();
	public final Signal tryLiftSignal = new LiftSystemSignal();
	public final Signal liftSignal = new LiftSystemSignal();
	public final Signal finishSignal = new LiftSystemSignal();
	public final Signal beginFeedSignal = new LiftSystemSignal();
	public final Signal tryFeedPickupSignal = new LiftSystemSignal();
	public final Signal fedTotePickupSignal = new LiftSystemSignal();
	public final Signal continueFeedingSignal = new LiftSystemSignal();
	public final Signal dropToteSignal = new LiftSystemSignal();
	
	private State eCurrentState = State.Init;
	
	
	private LiftSystem() {
		rightStick = OI.getInstance().rightStick;
		leftStick = OI.getInstance().leftStick;
		
		//right stick
		toteElevatorUpManualButton = new JoystickButton(rightStick, RobotMap.toteElevatorUpManualButtonID);
		toteElevatorDownManualButton = new JoystickButton(rightStick, RobotMap.toteElevatorDownManualButtonID);
		
		//left stick
		binElevatorUpManualButton = new JoystickButton(leftStick, RobotMap.binElevatorUpManualButtonID);
		binElevatorDownManualButton = new JoystickButton(leftStick, RobotMap.binElevatorDownManualButtonID);
		binTiltUpManualButton = new JoystickButton(leftStick, RobotMap.binTiltUpManualButtonID);
		binTiltDownManualButton = new JoystickButton(leftStick, RobotMap.binTiltDownManualButtonID);
		binGripOpenManualButton = new JoystickButton(leftStick, RobotMap.binGripOpenManualButtonID);
		binGripCloseManualButton = new JoystickButton(leftStick, RobotMap.binGripCloseManualButtonID);
		
		toteElevatorMotor = new CANTalon(RobotMap.toteElevatorMotor);
    	toteElevatorMotor.enableLimitSwitch(false, false);
    	toteElevatorMotor.enableBrakeMode(false);
    	
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
	
	private static final LiftSystem theSystem = new LiftSystem();
	
	public static LiftSystem getInstance() {
		return theSystem;
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new LiftSystemStateMachineCommand());
    }
    
    //Manual commands
    double toteElevatorSpeed;
    double binElevatorSpeed;
    double tilterMotorSpeed;
    double binGripSpeed;
    double rakerSpeed;
    
    public void moveToteElevator(double moveValue) {
    	toteElevatorSpeed = moveValue;
    }
    private void toteElevatorUpdate() {
    	if(toteElevatorUpManualButton.get())
        	toteElevatorMotor.set(0.1);
    	else if(toteElevatorDownManualButton.get())
    		toteElevatorMotor.set(-0.1);
    	else
    		toteElevatorMotor.set(toteElevatorSpeed);
    }
    
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
        	tilterMotor.set(0.50);
    	else if(binTiltDownManualButton.get())
        	tilterMotor.set(-0.50);
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
    
    /*
    public void moveRaker(double moveValue) {
    	rakerSpeed = moveValue;
    }
    private void rakerUpdate() {
    	rakerMotor.set(rakerSpeed);
    }*/
    
    
    enum State {
    	Init {
    		State run(LiftSystem liftSystem) {
    			if(!liftSystem.toteElevatorMotor.isRevLimitSwitchClosed()) {
    				//liftSystem.moveToteElevator(-0.3);    				
    			}
    			if(!liftSystem.binElevatorMotor.isRevLimitSwitchClosed()) {
    				//liftSystem.moveBinElevator(-0.2);    				
    			}
    			
    			if(liftSystem.binElevatorMotor.isRevLimitSwitchClosed() && liftSystem.toteElevatorMotor.isRevLimitSwitchClosed()) {
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
    		State run(LiftSystem liftSystem) {
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
    		State run(LiftSystem liftSystem) {
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
    		State run(LiftSystem liftSystem) {
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
    		State run(LiftSystem liftSystem) {
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
    		State run(LiftSystem liftSystem) {
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
    		State run(LiftSystem liftSystem) {
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
    		State run(LiftSystem liftSystem) {
    			return Idle;
    		}
    		
    		public String toString() {
    			return "Dropoff";
    		}
    	};
    	
    	
    	abstract State run(LiftSystem liftSystem);
    	
    	void init(LiftSystem liftSystem) {}
    }
    
    
    public void runStateMachine() {
    	State eNextState = eCurrentState;

    	eCurrentState = State.Idle;
    	//System.out.println("Current State: " + eCurrentState);
    	
    	eNextState = eCurrentState.run(this);
    	
    	binGripUpdate();
    	binTiltUpdate();
    	binElevatorUpdate();
    	toteElevatorUpdate();
    	//rakerUpdate();
    	
    	for(Signal signal: signals) {
    		signal.clear();
    	}
    	
    	if(eNextState != eCurrentState) {
        	//System.out.println("Next State: " + stateToString(nextState));
        	eCurrentState = eNextState;
        	eCurrentState.init(this);
    	}
    }
    
    public String getLimits() { //"fwd    rev"
    	return "" + tilterMotor.isFwdLimitSwitchClosed() + "    " + tilterMotor.isRevLimitSwitchClosed();
    }
}

