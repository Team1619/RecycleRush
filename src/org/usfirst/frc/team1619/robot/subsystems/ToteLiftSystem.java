package org.usfirst.frc.team1619.robot.subsystems;

import java.util.ArrayList;

import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.commands.ToteLiftSystemStateMachineCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class ToteLiftSystem extends Subsystem {
	public static final double kEncoderTicksPerInch = 0.0;
	public static final double kTransitPosition = 0.0;
    
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
	public final Signal humanPlayerFeedSignal = new ToteLiftSystemSignal();
	public final Signal dropoffSignal = new ToteLiftSystemSignal();
	public final Signal groundFeedSignal = new ToteLiftSystemSignal(); 
	
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
    	
    	toteElevatorMotorSmall = new CANTalon(RobotMap.toteElevatorMotorSmall);
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
    private double toteElevatorValue; //will be either %vbus or position
    
    public void moveToteElevator(double moveValue) {
    	toteElevatorValue = moveValue;
    	
    	toteElevatorMotor.changeControlMode(ControlMode.PercentVbus);
    	toteElevatorMotorSmall.changeControlMode(ControlMode.PercentVbus);
    }
    public void setToteElevatorPosition(double position) { //position in inches from '0' position
    	toteElevatorValue = position*kEncoderTicksPerInch;
    	
    	toteElevatorMotor.changeControlMode(ControlMode.Position);
    	toteElevatorMotorSmall.changeControlMode(ControlMode.Position);
    }
    private void toteElevatorUpdate() {
    	/*if(toteElevatorUpManualButton.get())
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
    	
    	/*Code to be used
    	if(leftStick.getY() != 0.0) {
    		toteElevatorMotor.changeControlMode(ControlMode.PercentVbus);
    		toteElevatorMotorSmall.changeControlMode(ControlMode.PercentVbus);
    		toteElevatorMotor.set(leftStick.getY() * 1);
        	toteElevatorMotorSmall.set(leftStick.getY() * -1);	
    	}
    	else {
    		toteElevatorMotor.set(toteElevatorValue);
    		toteElevatorMotorSmall.set(-toteElevatorValue);
    	}
    	*/
    }
    
    enum State {
    	Init {
    		State run(ToteLiftSystem liftSystem) {
    			//should be bottom limit switch
    			if(!liftSystem.toteElevatorMotor.isRevLimitSwitchClosed()) { 
    				liftSystem.moveToteElevator(-0.3);    				
    			}
    			else {
    				liftSystem.toteElevatorMotor.setPosition(0.0); //should set the "position" value, not move motor
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
    			if(liftSystem.abortSignal.check()) {
    				return Abort;
    			}
    			liftSystem.setToteElevatorPosition(kTransitPosition);
    			
    			if(liftSystem.resetSignal.check()) {
    				//check to make sure assumptions are true?
    				return Init;
    			}
    			if(liftSystem.humanPlayerFeedSignal.check()) {
    				return HumanPlayerFeed;
    			}
    			if(liftSystem.dropoffSignal.check()) {
    				return Dropoff;
    			}
    			if(liftSystem.groundFeedSignal.check()) {
    				return GroundFeed;
    			}
    			
    			return Idle;
    		}
    		
    		public String toString() {
    			return "Idle";
    		}
    	},
    	HumanPlayerFeed {
    		State run(ToteLiftSystem liftSystem) {
    			if(liftSystem.abortSignal.check()) {
    				return Abort;
    			}
    			
    			boolean done = false;
    			
    			if(done) {
    				return Idle;
    			}
    			return HumanPlayerFeed;
    		}
    		
    		public String toString() {
    			return "Human Player Feed";
    		}
    	},
    	Dropoff {
    		State run(ToteLiftSystem liftSystem) {
    			if(liftSystem.abortSignal.check()) {
    				return Abort;
    			}
    			
    			boolean done = false;

    			if(done) {
    				return Idle;
    			}
    			return Dropoff;
    		}
    		
    		public String toString() {
    			return "Dropoff";
    		}
    	},
    	GroundFeed {
    		State run(ToteLiftSystem liftSystem) {
    			if(liftSystem.abortSignal.check()) {
    				return Abort;
    			}
    			
    			boolean done = false;
    			
    			if(done) {
    				return Idle;	
    			}
    			return GroundFeed;
    		}
    		
    		public String toString() {
    			return "Ground Feed";
    		}
    	},
    	Abort {
    		State run(ToteLiftSystem liftSystem) {
    			//set speed to 0
    			if(liftSystem.abortSignal.check()) {
    				return Idle;
    			}
    			if(liftSystem.resetSignal.check()) {
    				return Init;
    			}
    			return Abort;
    		}
    		
    		public String toString() {
    			return "Abort";
    		}
    	};
      	
    	
    	abstract State run(ToteLiftSystem liftSystem);
    	public abstract String toString();
    	
    	void init(ToteLiftSystem liftSystem) {}
    }
    
    public void runStateMachine() {
    	State eNextState = eCurrentState;

    	eCurrentState = State.Idle; //temporary for safety
    	
    	eNextState = eCurrentState.run(this);
    	
    	toteElevatorUpdate();
    	
    	for(Signal signal: signals) {
    		signal.clear();
    	}
    	
    	if(eNextState != eCurrentState) {
        	eCurrentState = eNextState;
        	eCurrentState.init(this);
    	}
    }
    
}

