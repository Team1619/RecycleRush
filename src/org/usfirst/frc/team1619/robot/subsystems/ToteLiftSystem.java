package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.GenericLine;
import org.usfirst.frc.team1619.TrapezoidLine;
import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.StateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ToteLiftSystem extends StateMachineSystem {
	public static final double kEncoderTicksPerInch = 173.63;
	public static final double kTransitPosition = 0.0;
    
	//don't delete this
	//it is important
	;;;;;
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	public final CANTalon toteElevatorMotor;
	public final CANTalon toteElevatorMotorSmall;
	
	private final Joystick rightStick;
	private final Joystick leftStick;
	
	private double toteElevatorSpeed = 0.0; //will be %vbus 
	private boolean usePosition = false;
	private GenericLine speedCurve = new GenericLine();
	
	private ToteLiftSystem() {
		rightStick = OI.getInstance().rightStick;
		leftStick = OI.getInstance().leftStick;
				
		toteElevatorMotor = new CANTalon(RobotMap.toteElevatorMotor);
    	toteElevatorMotor.enableLimitSwitch(true, true);
    	toteElevatorMotor.enableBrakeMode(true);
    	
    	toteElevatorMotorSmall = new CANTalon(RobotMap.toteElevatorMotorSmall);
    	toteElevatorMotorSmall.enableLimitSwitch(false, false);
    	toteElevatorMotorSmall.enableBrakeMode(true);
    	toteElevatorMotorSmall.changeControlMode(ControlMode.Follower);
    	toteElevatorMotorSmall.set(RobotMap.toteElevatorMotor);
    	toteElevatorMotorSmall.reverseOutput(true);
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
    
    private void moveToteElevator(double v) {
    	SmartDashboard.putNumber("Tote elevator speed", v);
    	toteElevatorMotor.set(v);
    }
    
    public void setToteElevatorSpeed(double speed) {
    	toteElevatorSpeed = speed;
    	usePosition = false;
    }

    public void setToteElevatorPosition(double position) {  //in inches
    	final double kRampSpeed = -0.5;
    	final double kStartRampSpeed = -0.2;
    	final double kStartRampDistance = 2;
    	final double kStopRampDistance = 2;
    	
    	double moveDisplacement = position - getToteElevatorPosition();
    	double sign = Math.signum(moveDisplacement);
    	double rampSpeed = kRampSpeed*sign;
    	double startRampSpeed = kStartRampSpeed*sign;
    	if(Math.abs(moveDisplacement) < (kStartRampDistance + kStopRampDistance)) {
    		double peakRampDisplacement = Math.min(kStartRampDistance, kStopRampDistance)*sign;
    		double peakRampSpeed = kRampSpeed*(Math.abs(peakRampDisplacement)/(kStartRampDistance + kStopRampDistance));
        	double stopRampDisplacement = kStopRampDistance*sign;
        			
    		speedCurve = new GenericLine(getToteElevatorPosition(), startRampSpeed);
        	speedCurve.addPoint(getToteElevatorPosition() + peakRampDisplacement, peakRampSpeed);
        	speedCurve.addPoint(position + stopRampDisplacement, -rampSpeed);
    	}
    	else {
        	double startRampDisplacement = kStartRampDistance*sign;
        	double stopRampDisplacement = kStopRampDistance*sign;
        	
    		speedCurve = new GenericLine(getToteElevatorPosition(), startRampSpeed);
        	speedCurve.addPoint(getToteElevatorPosition() + startRampDisplacement, rampSpeed);
        	speedCurve.addPoint(position - stopRampDisplacement, rampSpeed);
        	speedCurve.addPoint(position + stopRampDisplacement, -rampSpeed);
    	}
    	
    	
    	usePosition = true;
    }
  
    //set position in inches, not move motor. Only use for calibration
    private void setToteElevatorPositionValue(double position) { 
    	toteElevatorMotor.setPosition(position*kEncoderTicksPerInch);
    }
    
    //get current position in inches
    public double getToteElevatorPosition() { 
    	return toteElevatorMotor.getPosition()/kEncoderTicksPerInch;
    }
    
    
    private void toteElevatorUpdate() {
    	SmartDashboard.putNumber("Set Position Speeed", speedCurve.getValue(getToteElevatorPosition()));
    	
    	if(Math.abs(leftStick.getY()) > 0.1) {
    		moveToteElevator(leftStick.getY());	
    		usePosition = false;
    	}
    	else if(usePosition) {
			moveToteElevator(speedCurve.getValue(getToteElevatorPosition()));
		}
		else {
    		moveToteElevator(0);
    		//moveToteElevator(toteElevatorSpeed);
    	}
    	
    }
    
	@Override
	public void run(State state) {
		
		if(OI.getInstance().leftStick.getRawButton(3))
			setToteElevatorPosition(6);
		if(OI.getInstance().leftStick.getRawButton(4))
			setToteElevatorPositionValue(0);
		
		
		switch(state) {
		case Init:
			//should be bottom limit switch
			if(!toteElevatorMotor.isFwdLimitSwitchClosed()) { 
				//setToteElevatorSpeed(0.2);
			}
			else {
				//setToteElevatorPositionValue(0.0); //should set the "position" value in inches, not move motor
				//setToteElevatorSpeed(0.0);
			}
			break;
		case Idle:
			//setToteElevatorPosition(kTransitPosition);
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
			usePosition = false;
			speedCurve = new GenericLine();
			break;
		default:
			break;
		}
		
		toteElevatorUpdate();
	}
}

