package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.Preferences;
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
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

/**
 *
 */
public class ToteElevatorSystem extends StateMachineSystem {
	public static final double kEncoderTicksPerInch = 5468/22.5; //fish
	public static final double kTransitPosition = 2.1;
	public static final double kFeederPosition = 20.8;
	public static final double kPickUpPosition = -2.0;
	public static final double kPositionTolerance = 3.0;
	public static final double kDeadZone = 1.0;
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
		
		toteElevatorManualButton = OI.getInstance().toteElevatorManualButton;

		toteElevatorMotor = new CANTalon(RobotMap.toteElevatorMotor);
		toteElevatorMotor.enableLimitSwitch(true, true);
		toteElevatorMotor.enableBrakeMode(true);
		toteElevatorMotor.reverseSensor(false);
		toteElevatorMotor.reverseOutput(false);
		toteElevatorMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		toteElevatorMotor.setPID(0.55, 0.40, 0, 0.0001, 300, 24/0.250, 0);

		Preferences.putNumber("toteP", toteElevatorMotor.getP());
		Preferences.putNumber("toteI", toteElevatorMotor.getI());
		Preferences.putNumber("toteD", toteElevatorMotor.getD());
		Preferences.putNumber("toteF", toteElevatorMotor.getF());
		Preferences.putNumber("toteIZone", toteElevatorMotor.getIZone());
		Preferences.addTableListener(new ITableListener() {
			
			@Override
			public void valueChanged(ITable source, String key, Object value, boolean isNew) {
				System.out.println(String.format("Key '%s' changed to '%s' (new = '%s')",
						key, value.toString(), Boolean.toString(isNew)));
				
				switch(key) {
				case "toteP":
					toteElevatorMotor.setP(Double.parseDouble((String)value));
					break;
				case "toteI":
					toteElevatorMotor.setI(Double.parseDouble((String)value));
					break;
				case "toteD":
					toteElevatorMotor.setD(Double.parseDouble((String)value));
					break;
				case "toteF":
					toteElevatorMotor.setF(Double.parseDouble((String)value));
					break;
				case "toteIZone":
					toteElevatorMotor.setIZone(Integer.parseInt((String)value));
					break;
				}
			}
			
		});
		
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

	private boolean wasManual = false;
	
	private void toteElevatorUpdate() {
		boolean noGoUp = !BinElevatorSystem.getInstance().getTilterMotorFwdLimitSwitch();
		double joystickY = leftStick.getY();
		
		if(toteElevatorManualButton.get()) {
			toteElevatorMotor.changeControlMode(ControlMode.PercentVbus);
			if(joystickY > 0.0 && noGoUp) {
				toteElevatorMotor.set(0.0);
			}
			else {
				toteElevatorMotor.set(joystickY);
			}
			usePosition = false;
			moveTo = Double.NaN;
			toteElevatorSpeed = 0.0;
			wasManual = true;
		}
		else {
			if(wasManual) {
				setToteElevatorPosition(getToteElevatorPosition());
				wasManual = false;
				useStatePosition = false;
			}
			if(usePosition) {
				if(Double.isNaN(moveTo)) {
					toteElevatorMotor.changeControlMode(ControlMode.PercentVbus);
					toteElevatorMotor.set(0);
				}
				else {
					if(Math.abs(moveTo - getToteElevatorPosition()) < kDeadZone) {
						toteElevatorMotor.changeControlMode(ControlMode.PercentVbus);
						toteElevatorMotor.set(0.0);
					}
					else {
						toteElevatorMotor.changeControlMode(ControlMode.Position);
						if(moveTo > getToteElevatorPosition() && noGoUp) {
							toteElevatorMotor.changeControlMode(ControlMode.PercentVbus);
							toteElevatorMotor.set(0.0);
						}
						else {
							toteElevatorMotor.changeControlMode(ControlMode.Position);
							toteElevatorMotor.set(moveTo*kEncoderTicksPerInch);	
						}
					}
				}
			}
			else {
				toteElevatorMotor.changeControlMode(ControlMode.PercentVbus);
				if(toteElevatorSpeed > 0.0 && noGoUp) {
					toteElevatorMotor.set(0.0);
				}
				else {
					toteElevatorMotor.set(toteElevatorSpeed);
				}
			}
		}

		SmartDashboard.putNumber("toteElevatorMotor.getIZone()", toteElevatorMotor.getIZone());
		SmartDashboard.putNumber("toteElevatorMotor.getEncPosition()",toteElevatorMotor.getEncPosition());
		SmartDashboard.putNumber("toteElevatorMotor.getOutputVoltage()",toteElevatorMotor.getOutputVoltage());
		SmartDashboard.putNumber("toteElevatorMotor.getOutputCurrent()", toteElevatorMotor.getOutputCurrent());
		SmartDashboard.putNumber("toteElevatorMotor.get()",toteElevatorMotor.get());
	}

	boolean useStatePosition = true;
	
	public void init(State state) {
		usePosition = false;
		toteElevatorSpeed = 0;
		moveTo = Double.NaN;
		
		useStatePosition = true;
		
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
			if(useStatePosition) {
				setToteElevatorPosition(kTransitPosition);		
			}
			break;
		case HumanFeed_RaiseTote:
			if(useStatePosition) {
				setToteElevatorPosition(kFeederPosition);
			}
			if(Math.abs(getToteElevatorPosition() - kFeederPosition) <= kPositionTolerance) {
				StateMachine.getInstance().humanPlayerFeed_WaitForTote.raise();
			}
			break;
		case HumanFeed_WaitForTote:
			if(useStatePosition) {
				setToteElevatorPosition(kFeederPosition);
			}
			break;
		case HumanFeed_ToteOnConveyor:
			if(useStatePosition) {
				setToteElevatorPosition(kFeederPosition);
			}
			break;
		case HumanFeed_ThrottleConveyorAndDescend:
			if(useStatePosition) {
				setToteElevatorPosition(kPickUpPosition);
			}
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