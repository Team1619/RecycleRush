package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.GenericLine;
import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.StateMachine;
import org.usfirst.frc.team1619.robot.StateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ToteElevatorSystem extends StateMachineSystem {
	public static final double kEncoderTicksPerInch = -173.63;
	public static final double kSpeed = Math.abs(20.0*kEncoderTicksPerInch);
	public static final double kTransitPosition = 3.0;
	public static final double kFeederPosition = 29.2;
	public static final double kPickUpPosition = 0.0;
	public static final double kPositionTolerance = 2.0;
	public static final double kConstantUpSpeed = -0.25;
	public static final double kSpeedDeadband = 5;


	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	public final CANTalon toteElevatorMotor;
	public final CANTalon toteElevatorMotorSmall;

	private final Joystick leftStick;

	private double toteElevatorSpeed; //will be %vbus 
	private boolean usePosition;
	private GenericLine speedCurve;
	private double moveTo = Double.NaN;

	private boolean useFullCurve;
	private double fullCurveBreakpoint;
	
	private PIDController toteElevatorController;

	private ToteElevatorSystem() {
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

		toteElevatorMotor.reverseSensor(true);
		//toteElevatorMotor.changeControlMode(ControlMode.Speed);
		toteElevatorMotor.changeControlMode(ControlMode.Speed);
		toteElevatorMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		//toteElevatorMotor.setPID(0.10, 0.00005, 0, 0.1, 0, 36, 0);
		
		toteElevatorController = new PIDController(0.010, 0.000005, 0,
		new PIDSource() {
			@Override
			public double pidGet() {
				return toteElevatorMotor.getSpeed();
			}

		}, new PIDOutput() {
			@Override
			public void pidWrite(double output) {
				if(Math.abs(output) >= kSpeedDeadband)
					toteElevatorMotor.set(output);
				else
					toteElevatorMotor.set(0);
			}
		});
		
		toteElevatorSpeed = 0.0;
		usePosition = false;
		speedCurve = new GenericLine();
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

	
	private void moveToteElevator(double speed) {
		if(speed < 0 && toteElevatorMotor.isRevLimitSwitchClosed()) {
			 // Reset accumulator and reenable the controller
			toteElevatorController.reset();
			toteElevatorController.enable();
			//toteElevatorMotor.ClearIaccum();
		}
		else if(speed >= 0 && toteElevatorMotor.isFwdLimitSwitchClosed()) {
			// Reset accumulator and reenable the controller
			toteElevatorController.reset();
			toteElevatorController.enable();
			//toteElevatorMotor.ClearIaccum();
		}
		//toteElevatorMotor.set(speed*kSpeed);
		//toteElevatorMotor.set(speed);
		toteElevatorController.setSetpoint(speed*kSpeed);
	}

	public void setToteElevatorSpeed(double speed) {
		toteElevatorSpeed = speed;
		usePosition = false;
	}


	private final double kRampSpeed = -1.0;
	private final double kStartRampSpeed = -0.2;
	private final double kStartRampDistance = 3.0;
	private final double kStopRampDistance = 6.0;

	public void setToteElevatorPosition(double position) {
		if(position != moveTo) {
			double currentPosition = getToteElevatorPosition();

			double moveDisplacement = position - currentPosition;
			double sign = Math.signum(moveDisplacement);
			double rampSpeed = kRampSpeed*sign;
			double startRampSpeed = kStartRampSpeed*sign;
			if(Math.abs(moveDisplacement) < (kStartRampDistance + kStopRampDistance)) {
				double lerp = Math.abs(moveDisplacement)/(kStartRampDistance + kStopRampDistance);
				double peakRampDisplacement = kStopRampDistance*sign*lerp;
				double peakRampSpeed = rampSpeed*lerp;
				double stopRampDisplacement = kStopRampDistance*sign;

				speedCurve = new GenericLine(currentPosition, startRampSpeed);
				speedCurve.addPoint(currentPosition + peakRampDisplacement, peakRampSpeed);
				speedCurve.addPoint(position + stopRampDisplacement, -rampSpeed);

				fullCurveBreakpoint = currentPosition + peakRampDisplacement;
			}
			else {
				double startRampDisplacement = kStartRampDistance*sign;
				double stopRampDisplacement = kStopRampDistance*sign;

				speedCurve = new GenericLine(currentPosition, startRampSpeed);
				speedCurve.addPoint(currentPosition + startRampDisplacement, rampSpeed);
				speedCurve.addPoint(position - stopRampDisplacement, rampSpeed);
				speedCurve.addPoint(position + stopRampDisplacement, -rampSpeed);

				fullCurveBreakpoint = position - stopRampDisplacement;
			}

			useFullCurve = false;
			usePosition = true;
			moveTo = position;
			
			toteElevatorMotor.ClearIaccum();
			//System.out.println(String.format("Going to %f", moveTo));
			//System.out.println("GenericLine = " + speedCurve);
		}
	}

	public void updateToteElevatorSpeedCurve() {
		if(usePosition && !useFullCurve) {
			if((moveTo > fullCurveBreakpoint && getToteElevatorPosition() > fullCurveBreakpoint) ||
					(moveTo < fullCurveBreakpoint && getToteElevatorPosition() < fullCurveBreakpoint)) {
				speedCurve = new GenericLine(moveTo - kStopRampDistance, kRampSpeed);
				speedCurve.addPoint(moveTo + kStopRampDistance, -kRampSpeed);
				useFullCurve = true;
				//System.out.println(String.format("Going to %f", moveTo));
				//System.out.println("GenericLine = " + speedCurve);
			}
		}
	}

	private void setToteElevatorPositionValue(double position) { //set position in inches, not move motor. Only use for calibration
		toteElevatorMotor.setPosition(position*kEncoderTicksPerInch);
		moveTo = Double.NaN;
		usePosition = false;
	}
	public double getToteElevatorPosition() { //get current position in inches
		return toteElevatorMotor.getPosition()/kEncoderTicksPerInch;
	}

	private void toteElevatorUpdate() {
		SmartDashboard.putNumber("Set Position Speeed", speedCurve.getValue(getToteElevatorPosition()));
		SmartDashboard.putNumber("toteElevatorMotor.getIZone()",toteElevatorMotor.getIZone());
		SmartDashboard.putNumber("toteElevatorMotor()",toteElevatorMotor.get());
		
		if(Math.abs(leftStick.getY()) > 0.1) {
			moveToteElevator(leftStick.getY());
			usePosition = false;
			moveTo = Double.NaN;
			toteElevatorSpeed = 0.0;
		}
		else {
			if(usePosition) {
				updateToteElevatorSpeedCurve();
				moveToteElevator(speedCurve.getValue(getToteElevatorPosition()));
			}
			else {
				moveToteElevator(toteElevatorSpeed);
			}
		}

	}

	public void init(State state) {
		switch(state) {
		case Idle:
			toteElevatorSpeed = 0.0;
			break;
		default:
			break;
		}
	}

	@Override
	public void run(State state, double elapsed) {

		/*
		if(OI.getInstance().leftStick.getRawButton(3))
			setToteElevatorPosition(6);
		if(OI.getInstance().leftStick.getRawButton(4))
			setToteElevatorPositionValue(0);
		 */
		
		switch(state) {
		case Init:
			//should be bottom limit switch
			if(!toteElevatorMotor.isFwdLimitSwitchClosed()) { 
				setToteElevatorSpeed(0.4);
			}
			else {
				setToteElevatorPositionValue(0.0);
				setToteElevatorSpeed(0.0);
			}
			break;
		case Idle:
			setToteElevatorPosition(kTransitPosition);
			break;
		case HumanFeed_RaiseTote:
			setToteElevatorPosition(kFeederPosition);
			if(Math.abs(getToteElevatorPosition() - kFeederPosition) <= kPositionTolerance) {
				StateMachine.getInstance().humanPlayerFeed_WaitForTote.raise();
			}
			break;
		case HumanFeed_WaitForTote:
			setToteElevatorPosition(kFeederPosition);
			break;
		case HumanFeed_ToteOnConveyor:
			setToteElevatorPosition(kFeederPosition);
			break;
		case HumanFeed_ThrottleConveyorAndDescend:
			setToteElevatorPosition(kPickUpPosition);
			if(Math.abs(getToteElevatorPosition() - kPickUpPosition) <= kPositionTolerance) {
				StateMachine.getInstance().humanPlayerFeed_RaiseTote.raise();	
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
			speedCurve = new GenericLine();
			break;
		default:
			break;
		}

		toteElevatorUpdate();
	}

	public boolean initFinished() {
		return getToteElevatorPosition() == 0.0;
	}
}

