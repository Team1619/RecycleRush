package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.StateMachine;
import org.usfirst.frc.team1619.robot.StateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ToteElevatorSystem extends StateMachineSystem {
	public static final double kEncoderTicksPerInch = 173.63;
	public static final double kTransitPosition = 3.0;
	public static final double kFeederPosition = 29.2;
	public static final double kPickUpPosition = -2.0;
	public static final double kPositionTolerance = 2.0;
	public static final double kInitSpeed = -0.2;

	public final CANTalon toteElevatorMotor;
	public final CANTalon toteElevatorMotorSmall;

	private final Joystick leftStick;
	
	private final JoystickButton toteElevatorManualButton;

	private double toteElevatorSpeed; // will be %vbus 
	private boolean usePosition;
	private double moveTo;

	private boolean bInitFinished = false;
	
	private ToteElevatorSystem() {
		leftStick = OI.getInstance().leftStick;
		
		toteElevatorManualButton = new JoystickButton(leftStick, RobotMap.toteElevatorManualButtonID);

		toteElevatorMotor = new CANTalon(RobotMap.toteElevatorMotor);
		toteElevatorMotor.enableLimitSwitch(true, true);
		toteElevatorMotor.enableBrakeMode(true);
		toteElevatorMotor.reverseSensor(false);
		toteElevatorMotor.reverseOutput(false);
		toteElevatorMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		toteElevatorMotor.setPID(0.55, 0.0015, 0, 0.0001, 500, 24/0.250, 0);
		
		toteElevatorMotorSmall = new CANTalon(RobotMap.toteElevatorMotorSmall);
		toteElevatorMotorSmall.enableLimitSwitch(false, false);
		toteElevatorMotorSmall.enableBrakeMode(true);
		toteElevatorMotorSmall.changeControlMode(ControlMode.Follower);
		toteElevatorMotorSmall.set(RobotMap.toteElevatorMotor);
		toteElevatorMotorSmall.reverseOutput(true);
	}

	private final static ToteElevatorSystem theSystem = new ToteElevatorSystem();

	public static ToteElevatorSystem getInstance() {
		return theSystem;
	}

	public void initDefaultCommand() {
	}

	public void setToteElevatorSpeed(double speed) {
		toteElevatorSpeed = speed;
		usePosition = false;
	}

	public void setToteElevatorPosition(double position) {  //in inches
		moveTo = position;
		usePosition = true;
	}

	//set position in inches, not move motor. Only use for calibration
	private void setToteElevatorPositionValue(double position) { 
		toteElevatorMotor.setPosition(position*kEncoderTicksPerInch);
		moveTo = Double.NaN;
	}
	
	//get current position in inches
	public double getToteElevatorPosition() { 
		return toteElevatorMotor.getPosition()/kEncoderTicksPerInch;
	}

	private void toteElevatorUpdate() {

		double joystickY = leftStick.getY();
		if(toteElevatorManualButton.get()) {
			toteElevatorMotor.changeControlMode(ControlMode.PercentVbus);
			toteElevatorMotor.set(joystickY);
			usePosition = false;
			moveTo = Double.NaN;
			toteElevatorSpeed = 0.0;
		}
		else {
			if(usePosition) {
				if(Double.isNaN(moveTo)) {
					toteElevatorMotor.changeControlMode(ControlMode.PercentVbus);
					toteElevatorMotor.set(0);
				}
				else {
					if(Math.abs(moveTo - getToteElevatorPosition()) < kPositionTolerance) {
						toteElevatorMotor.changeControlMode(ControlMode.PercentVbus);
						toteElevatorMotor.set(0);
					}
					else {
						toteElevatorMotor.changeControlMode(ControlMode.Position);
						toteElevatorMotor.set(moveTo*kEncoderTicksPerInch);
					}
				}
			}
			else {
				toteElevatorMotor.changeControlMode(ControlMode.PercentVbus);
				toteElevatorMotor.set(toteElevatorSpeed);
			}
		}

		SmartDashboard.putNumber("toteElevatorMotor.getEncPosition()",toteElevatorMotor.getEncPosition());
		SmartDashboard.putNumber("toteElevatorMotor.getOutputVoltage()",toteElevatorMotor.getOutputVoltage());
		SmartDashboard.putNumber("toteElevatorMotor.get()",toteElevatorMotor.get());
	}

	public void init(State state) {
		usePosition = false;
		toteElevatorSpeed = 0;
		moveTo = Double.NaN;
		
		switch(state) {
		case Init:
			bInitFinished = false;
			break;
		default:
			break;
		}
	}

	@Override
	public void run(State state, double elapsed) {
		
		

		switch(state) {
		case Init:
			//should be bottom limit switch
			if(toteElevatorMotor.isRevLimitSwitchClosed()) {
				setToteElevatorPositionValue(0.0);
				setToteElevatorSpeed(0.0);
				bInitFinished = true;
			}
			else {
				setToteElevatorSpeed(kInitSpeed);
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
			break;
		default:
			break;
		}

		toteElevatorUpdate();
	}

	public boolean initFinished() {
		return bInitFinished;
	}
}

