package org.usfirst.frc.team1619.robot.subsystems;

import java.util.ArrayList;

import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.commands.LiftSystemStateMachineCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class LiftSystem extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private CANTalon toteElevatorMotor;
	private CANTalon binElevatorMotor;
	private CANTalon tilterMotor;
	private CANTalon binGripMotor;
	private CANTalon rakerMotor;

	private ArrayList<Signal> signals = new ArrayList<Signal>(); 
	public class LiftSystemSignal extends Signal {
		public LiftSystemSignal() {
			signals.add(this);
		}
	}

	public final Signal abortSignal = new LiftSystemSignal();
	public final Signal resetSignal = new LiftSystemSignal();
	
	private State eCurrentState = State.Init;
	
	private LiftSystem() {
		toteElevatorMotor = new CANTalon(RobotMap.toteElevatorMotor);
    	toteElevatorMotor.enableLimitSwitch(false, false);
    	toteElevatorMotor.enableBrakeMode(false);
    	
    	binElevatorMotor = new CANTalon(RobotMap.binElevatorMotor);
    	binElevatorMotor.enableLimitSwitch(false, false);
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
    
    public void moveToteElevator(double moveValue) {
    	toteElevatorMotor.set(moveValue);
    }
    
    public void moveBinElevator(double moveValue) {
    	binElevatorMotor.set(moveValue*0.2);
    }
    
    public void binTilt(double moveValue) {
    	tilterMotor.set(moveValue);
    }
    
    public void moveBinGrip(double moveValue) {
    	binGripMotor.set(moveValue);
    }
    
    public void moveRaker(double moveValue) {
    	rakerMotor.set(moveValue*0.2);
    }
    
    
    enum State {
    	Init {
    		State run(LiftSystem liftSystem) {
    			return Idle;
    		}
    		
    		public String toString() {
    			return "Init";
    		}
    	},
    	Idle {
    		State run(LiftSystem liftSystem) {
    			if (liftSystem.resetSignal.check()) {
    				return Init;
    			}
    			return Idle;
    		}
    		
    		public String toString() {
    			return "Idle";
    		}
    	},
    	
    	BeginStack {
    		State run(LiftSystem liftSystem) {
    			return Idle;
    		}
    		
    		public String toString() {
    			return "Begin Stack";
    		}
    		
    	},
    	BeginFeed {
    		State run(LiftSystem liftSystem) {
    			return Idle;
    		}
    		
    		public String toString() {
    			return "Begin Feed";
    		}
    	},
    	StackForFeed {
    		State run(LiftSystem liftSystem) {
    			return Idle;
    		}
    		
    		public String toString() {
    			return "Stack For Feed";
    		}
    	},
    	StackForPickup {
    		State run(LiftSystem liftSystem) {
    			return Idle;
    		}
    		
    		public String toString() {
    			return "Stack For Pickup";
    		}
    	},
    	Pickup {
    		State run(LiftSystem liftSystem) {
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
    	
    	//static LiftSystem liftSystem = Robot.getRobot().liftSubsystem;
    	abstract State run(LiftSystem liftSystem);
    }
    
    
    public void runStateMachine() {
    	State eNextState = eCurrentState;

    	eCurrentState = State.Idle;
    	//System.out.println("Current State: " + eCurrentState);
    	
    	
    	
    	eNextState = eCurrentState.run(this);
    	
    	for(Signal signal: signals) {
    		signal.clear();
    	}
    	
    	if(eNextState != eCurrentState) {
        	//System.out.println("Next State: " + stateToString(nextState));
        	eCurrentState = eNextState;
    	}
    }
    
    public String getLimits() { //"fwd    rev"
    	return "" + tilterMotor.isFwdLimitSwitchClosed() + "   " + tilterMotor.isRevLimitSwitchClosed();
    }
}

