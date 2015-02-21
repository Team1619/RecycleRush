package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.TrapezoidLine;
import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.StateMachine;
import org.usfirst.frc.team1619.robot.StateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;

/**
 *
 */
public class ToteElevatorSystem extends StateMachineSystem {
	public static final double kEncoderTicksPerInch = 0.0;
	public static final double kTransitPosition = 0.0;
	public static final double kFeederPosition = 0.0;
	public static final double kPickUpPosition = 0.0;
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	public final CANTalon toteElevatorMotor;
	public final CANTalon toteElevatorMotorSmall;
	
	private final Joystick leftStick;
	
	private double toteElevatorSpeed; //will be %vbus 
	private boolean usePosition;
	private TrapezoidLine speedCurve;
	private double moveTo;
			
	private ToteElevatorSystem() {
		leftStick = OI.getInstance().leftStick;
		
		toteElevatorMotor = new CANTalon(RobotMap.toteElevatorMotor);
    	toteElevatorMotor.enableLimitSwitch(true, true);
    	toteElevatorMotor.enableBrakeMode(true);
    	
    	toteElevatorMotorSmall = new CANTalon(RobotMap.toteElevatorMotorSmall);
    	toteElevatorMotorSmall.enableLimitSwitch(false, false);
    	toteElevatorMotorSmall.enableBrakeMode(true);
    	
    	toteElevatorSpeed = 0.0;
    	usePosition = false;
    	speedCurve = new TrapezoidLine();
    	moveTo = -1.0;
	}
	
	private static ToteElevatorSystem theSystem;
	
	public static ToteElevatorSystem getInstance() {
		if(theSystem == null)
			theSystem = new ToteElevatorSystem();
		return theSystem;
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new ToteLiftSystemStateMachineCommand());
    }
    
    private void moveToteElevator(double percentVBus) {
    	toteElevatorMotor.set(percentVBus);
    	toteElevatorMotorSmall.set(-percentVBus);
    }
    
    public void setToteElevatorSpeed(double speed) {
    	toteElevatorSpeed = speed;
    	usePosition = false;
    }
    public void setToteElevatorPosition(double position) {  //in inches
    	if(position != moveTo) {
    		usePosition = true;
        	
        	speedCurve = new TrapezoidLine(
        			getToteElevatorPosition(), 0.0,
        			getToteElevatorPosition() + (position - getToteElevatorPosition())/3, 0.5,
        			getToteElevatorPosition() + 2*(position - getToteElevatorPosition())/3, 0.5,
        			position, 0.0
        			);
        	moveTo = position;
    	}
    }
    private void setToteElevatorPositionValue(double position) { //set position in inches, not move motor. Only use for calibration
    	toteElevatorMotor.setPosition(position*kEncoderTicksPerInch);
    }
    public double getToteElevatorPosition() { //get current position in inches
    	return toteElevatorMotor.getPosition()/kEncoderTicksPerInch;
    }
    
    private void toteElevatorUpdate() {
    	if(leftStick.getY() != 0.0) {
    		moveToteElevator(leftStick.getY());	
    		usePosition = false;
    	}
    	else {
    		if(usePosition) {
    			moveToteElevator(-speedCurve.getValue(getToteElevatorPosition()));
    		}
    		else {
	    		moveToteElevator(toteElevatorSpeed);
	    	}
    	}
    	
    }
    
	@Override
	public void run(State state) {
		switch(state) {
		case Init:
			//should be bottom limit switch
			if(!toteElevatorMotor.isFwdLimitSwitchClosed()) { 
				//setToteElevatorSpeed(0.2);
			}
			else {
				setToteElevatorPositionValue(0.0); //should set the "position" value in inches, not move motor
				setToteElevatorSpeed(0.0);
			}
			break;
		case Idle:
			setToteElevatorPosition(kTransitPosition);
			break;
		case HumanFeed:
			if(StateMachine.getInstance().humanFeedTimer.get() < 0.1) { //just at beginning of human feeder
				setToteElevatorPosition(kFeederPosition);
			}
			if(Math.abs(kTransitPosition - getToteElevatorPosition()) < 0.1) { //once it picks up tote that was just placed there
				setToteElevatorPosition(kFeederPosition);
			}
			break;
		case GroundFeed:
			break;
		case Dropoff:
			break;
		case BinPickup:
			break;
		case Abort:	
			setToteElevatorSpeed(0.0);
			usePosition = false;
			speedCurve = new TrapezoidLine();
			break;
		default:
			break;
		}
		
		toteElevatorUpdate();
	}
}

