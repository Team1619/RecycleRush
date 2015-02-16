package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.TrapezoidLine;
import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.StateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 *
 */
public class ToteLiftSystem extends StateMachineSystem {
	public static final double kEncoderTicksPerInch = 0.0;
	public static final double kTransitPosition = 0.0;
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	public final CANTalon toteElevatorMotor;
	public final CANTalon toteElevatorMotorSmall;
	
	private final Joystick rightStick;
	private final Joystick leftStick;
	
	private double toteElevatorSpeed; //will be %vbus 
	private boolean usePosition;
	private TrapezoidLine speedCurve;
	
	private final JoystickButton toteElevatorDownManualButton, toteElevatorUpManualButton;
	
	private ToteLiftSystem() {
		rightStick = OI.getInstance().rightStick;
		leftStick = OI.getInstance().leftStick;
		
		//right stick
		toteElevatorUpManualButton = new JoystickButton(rightStick, RobotMap.toteElevatorUpManualButtonID);
		toteElevatorDownManualButton = new JoystickButton(rightStick, RobotMap.toteElevatorDownManualButtonID);
		
		toteElevatorMotor = new CANTalon(RobotMap.toteElevatorMotor);
    	toteElevatorMotor.enableLimitSwitch(true, true);
    	toteElevatorMotor.enableBrakeMode(true);
    	
    	toteElevatorMotorSmall = new CANTalon(RobotMap.toteElevatorMotorSmall);
    	toteElevatorMotorSmall.enableLimitSwitch(false, false);
    	toteElevatorMotorSmall.enableBrakeMode(true);
    	
    	toteElevatorSpeed = 0.0;
    	usePosition = false;
    	speedCurve = new TrapezoidLine();
	}
	
	private static ToteLiftSystem theSystem;
	
	public static ToteLiftSystem getInstance() {
		if(theSystem == null)
			theSystem = new ToteLiftSystem();
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
    	usePosition = true;
    	
    	speedCurve = new TrapezoidLine(
    			getToteElevatorPosition(), 0.0,
    			getToteElevatorPosition() + (position - getToteElevatorPosition())/3, 0.5,
    			getToteElevatorPosition() + 2*(position - getToteElevatorPosition())/3, 0.5,
    			position, 0.0
    			);
    }
   
    private void setToteElevatorPositionValue(double position) { //set position in inches, not move motor. Only use for calibration
    	toteElevatorMotor.setPosition(position*kEncoderTicksPerInch);
    }
    public double getToteElevatorPosition() { //get current position in inches
    	return toteElevatorMotor.getPosition()/kEncoderTicksPerInch;
    }
    
    private void toteElevatorUpdate() {
    	//Following code is temporary, although controlling the tote elevator
    	//with a joystick is likely a good idea.
    	//moveToteElevator(leftStick.getY());
    	
    	//Code to be used
    	if(leftStick.getY() != 0.0) {
    		moveToteElevator(leftStick.getY());	
    	}
    	else {
    		if(usePosition) {
    			moveToteElevator(speedCurve.getValue(getToteElevatorPosition()));
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
			if(!toteElevatorMotor.isRevLimitSwitchClosed()) { 
				setToteElevatorSpeed(-0.3);    				
			}
			else {
				setToteElevatorPositionValue(0.0); //should set the "position" value in inches, not move motor
			}
			break;
		case Idle:
			setToteElevatorPosition(kTransitPosition);
			break;
		case HumanFeed:
			break;
		case GroundFeed:
			break;
		case Dropoff:
			break;
		case BinPickup:
			break;
		case Abort:	
			setToteElevatorSpeed(0.0);
			break;
		default:
			break;
		}
		
		toteElevatorUpdate();
	}
}

