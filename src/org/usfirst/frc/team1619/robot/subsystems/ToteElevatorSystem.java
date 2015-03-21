package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.Preferences;
import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.StateMachine;
import org.usfirst.frc.team1619.robot.StateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ToteElevatorSystem extends StateMachineSystem {
	public static final double kEncoderTicksPerInch = 5468/22.5; //fish
	public static final double kTransitPosition = 1.0;
	public static final double kFeederPosition = 20.8;
	public static final double kPickUpPosition = -2.0;
	public static final double kPositionTolerance = 3.0;
	public static final double kDeadZone = 0.25;
	public static final double kInitSpeed = -0.5;
	public static final double kRateOffset = 0.5;
	
	public static final double kToteElevatorUpSpeed = -0.7;
	public static final double kToteElevatorDownSpeed = 0.7;
	
	public static final double k0ToteP = 0.60, k0ToteI = 0.003, k0ToteD = 0;
	public static final double k2ToteP = 0.70, k2ToteI = 0.003, k2ToteD = 0;
	public static final double k3ToteP = 0.75, k3ToteI = 0.003, k3ToteD = 0;
	public static final double k4ToteP = 0.75, k4ToteI = 0.003, k4ToteD = 0;
	public static final double k5ToteP = 0.85, k5ToteI = 0.003, k5ToteD = 0;
	
	public final CANTalon toteElevatorMotor;
	public final CANTalon toteElevatorMotorSmall;


	private double toteElevatorSpeed; // will be %vbus 
	private boolean usePosition;
	private double moveTo;
	private boolean bInitFinished = false;
	
	private JoystickButton toteElevatorTopPositionButton;
	private JoystickButton toteElevatorBottomPositionButton;
		
	private ToteElevatorSystem() {
		toteElevatorTopPositionButton = OI.getInstance().toteElevatorTopPositionButton;
		toteElevatorBottomPositionButton = OI.getInstance().toteElevatorBottomPositionButton;
		
		toteElevatorMotor = new CANTalon(RobotMap.toteElevatorMotor);
		toteElevatorMotor.enableLimitSwitch(true, true);
		toteElevatorMotor.ConfigFwdLimitSwitchNormallyOpen(true);
		toteElevatorMotor.ConfigRevLimitSwitchNormallyOpen(true);
		toteElevatorMotor.enableBrakeMode(true);
		toteElevatorMotor.reverseSensor(false);
		toteElevatorMotor.reverseOutput(false);
		toteElevatorMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		//PID values default to this
		toteElevatorMotor.setPID(k0ToteP, k0ToteI, k0ToteD, 0.0001, 800, 24/0.250, 0);
		
		toteElevatorMotorSmall = new CANTalon(RobotMap.toteElevatorMotorSmall);
		toteElevatorMotorSmall.enableLimitSwitch(false, false);
		toteElevatorMotorSmall.enableBrakeMode(true);
		toteElevatorMotorSmall.changeControlMode(ControlMode.Follower);
		toteElevatorMotorSmall.set(RobotMap.toteElevatorMotor);
		toteElevatorMotorSmall.reverseOutput(true);

		Preferences.putNumber("Current_P_Value", k0ToteP);
		Preferences.putNumber("Current_I_Value", k0ToteI);
		Preferences.putNumber("Current_D_Value", k0ToteD);
		Preferences.putNumber("Current_F_Value", 0.0001);
		Preferences.putNumber("Current_IZone_Value", 800);
		Preferences.putNumber("Current_RampRate_Value", 24/0.250);
		Preferences.putNumber("Current_RateOffset_Value", kRateOffset);
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
	public void setToteElevatorPositionValue(double position) { 
		toteElevatorMotor.setPosition(position*kEncoderTicksPerInch);
		moveTo = Double.NaN;
	}
	
	//get current position in inches
	public double getToteElevatorPosition() { 
		return toteElevatorMotor.getPosition()/kEncoderTicksPerInch;
	}
	
	public double getToteElevatorRate() {
		return toteElevatorMotor.getSpeed()/kEncoderTicksPerInch;
	}

	private boolean wasManual = false;
	
	private void stopAutoToteElevator() {
		usePosition = false;
		moveTo = Double.NaN;
		toteElevatorSpeed = 0.0;
	}
	
	private void toteElevatorUpdate() {
		if(!isSafeToRaiseTote()) {
			toteElevatorMotor.ClearIaccum();
		}

		boolean finalSetValuePosition;
		double finalSetValue;
		if(OI.getInstance().toteElevatorUpManualButton.get()) {
			finalSetValue = kToteElevatorUpSpeed;
			finalSetValuePosition = false;
			wasManual = true;
		}
		else if(OI.getInstance().toteElevatorDownManualButton.get()) {
			finalSetValue = kToteElevatorDownSpeed;
			finalSetValuePosition = false;
			wasManual = true;
		}
		else {
			if(wasManual) {
				//double rateOffset = getToteElevatorRate() * kRateOffset;
				double rateOffset = getToteElevatorRate() * Preferences.getNumber("Current_RateOffset_Value", kRateOffset);
				setToteElevatorPosition(getToteElevatorPosition() + rateOffset);
				wasManual = false;
				useStatePosition = false;
			}
			if(usePosition) {
				if(Double.isNaN(moveTo)) {
					finalSetValue = 0;
					finalSetValuePosition = false;
				}
				else {
					if(Math.abs(moveTo - getToteElevatorPosition()) < kDeadZone) {
						finalSetValuePosition = false;
						finalSetValue = 0;
					}
					else {
						finalSetValuePosition = true;
						finalSetValue = moveTo*kEncoderTicksPerInch;
					}
				}
			}
			else {
				finalSetValuePosition = false;
				finalSetValue = toteElevatorSpeed;
			}
		}

		if(finalSetValuePosition) {
			toteElevatorMotor.changeControlMode(ControlMode.Position);
			if(isSafeToRaiseTote() || (finalSetValue < 0)) {
				toteElevatorMotor.set(finalSetValue);
			}
			else {
				toteElevatorMotor.set(0.0);
			}
		}
		else {
			toteElevatorMotor.changeControlMode(ControlMode.PercentVbus);
			if(finalSetValue > BinElevatorSystem.kToteElevatorSafetyForTilt) {
				toteElevatorMotor.set(BinElevatorSystem.kToteElevatorSafetyForTilt);
			}
			else {
				toteElevatorMotor.set(finalSetValue);
			}
		}

		SmartDashboard.putNumber("toteElevatorMotor.getIZone()", toteElevatorMotor.getIZone());
		SmartDashboard.putNumber("toteElevatorMotor.getEncPosition()",toteElevatorMotor.getEncPosition());
		SmartDashboard.putNumber("toteElevatorMotor.getOutputVoltage()",toteElevatorMotor.getOutputVoltage());
		SmartDashboard.putNumber("toteElevatorMotor.getOutputCurrent()", toteElevatorMotor.getOutputCurrent());
		SmartDashboard.putNumber("toteElevatorMotor.get()",toteElevatorMotor.get());
		SmartDashboard.putBoolean("toteElevatorMotor Fwd Limit", toteElevatorMotor.isFwdLimitSwitchClosed());
		SmartDashboard.putBoolean("toteElevatorMotor Rev Limit", toteElevatorMotor.isRevLimitSwitchClosed());

	}

	boolean useStatePosition = true;
	
	public boolean isSafeToRaiseTote() {
		return BinElevatorSystem.getInstance().getTilterBackLimitSwitch();
	}
	
	public void init(State state) {
		stopAutoToteElevator();
		
		useStatePosition = true;
		
		switch(state) {
		case Init:
			bInitFinished = false;
			toteElevatorMotor.setPID(k0ToteP, k0ToteI, k0ToteD, 0.0001, 800, 24/0.250, 0);
			break;
		case Idle:
			toteElevatorMotor.setPID(k0ToteP, k0ToteI, k0ToteD, 0.0001, 800, 24/0.250, 0);
			break;
		default:
			break;
		}
		double p = Preferences.getNumber("Current_P_Value", k0ToteP);
		double i = Preferences.getNumber("Current_I_Value", k0ToteI);
		double d = Preferences.getNumber("Current_D_Value", k0ToteD);
		double f = Preferences.getNumber("Current_F_Value", 0.0001);
		int izone = (int) Preferences.getNumber("Current_IZone_Value", 800);
		double closeLoopRampRate = Preferences.getNumber("Current_RampRate_Value", 24/0.250);
		toteElevatorMotor.setPID(p, i, d, f, izone, closeLoopRampRate, 0);
		
		//System.out.println(toteElevatorMotor.getP() + " " + toteElevatorMotor.getI() + " " + toteElevatorMotor.getD());
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
			if(toteElevatorTopPositionButton.get()) {
				useStatePosition = false;
				setToteElevatorPosition(kFeederPosition);
			}
			else if(toteElevatorBottomPositionButton.get()) {
				useStatePosition = false;
				setToteElevatorPosition(kTransitPosition);
			}
			if(useStatePosition) {
				setToteElevatorPosition(kTransitPosition);		
			}
			break;
		case HumanFeed_RaiseTote:
			switch (StateMachine.getInstance().numberTotes) {
			case 0:
				toteElevatorMotor.setPID(k0ToteP, k0ToteI, k0ToteD, 0.0001, 800, 24/0.250, 0);
				break;
			case 1:
				toteElevatorMotor.setPID(k0ToteP, k0ToteI, k0ToteD, 0.0001, 800, 24/0.250, 0);
				break;
			case 2: 
				toteElevatorMotor.setPID(k2ToteP, k2ToteI, k2ToteD, 0.0001, 800, 24/0.250, 0);
				break;
			case 3:
				toteElevatorMotor.setPID(k2ToteP, k2ToteI, k2ToteD, 0.0001, 800, 24/0.250, 0);
				break;
			case 4:
				toteElevatorMotor.setPID(k4ToteP, k4ToteI, k4ToteD, 0.0001, 800, 24/0.250, 0);
				break;
			case 5:
				toteElevatorMotor.setPID(k4ToteP, k4ToteI, k4ToteD, 0.0001, 800, 24/0.250, 0);
				break;
			default:
				break;
			}
			SmartDashboard.putNumber("Tote Elevator Variable P", toteElevatorMotor.getP());
			SmartDashboard.putNumber("Tote Elevator Variable I", toteElevatorMotor.getI());
			if(useStatePosition) {
				setToteElevatorPosition(kFeederPosition);
			}
			if(Math.abs(getToteElevatorPosition() - kFeederPosition) <= kPositionTolerance) {
				StateMachine.getInstance().humanFeed_WaitForTote.raise();
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
				StateMachine.getInstance().humanFeed_RaiseTote.raise();	
			}
			break;
		case TotePickup:
			if(useStatePosition) {
				setToteElevatorPosition(kPickUpPosition);
			}
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
