package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.StateMachine;
import org.usfirst.frc.team1619.robot.StateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;

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
	public static final double kRateOffset = 0.4;
	public static final double kRateOffsetConstant = 2.5;
	
	public static final double kToteElevatorUpSpeed = 0.7;
	public static final double kToteElevatorDownSpeed = -0.5;
	
	//With CIM and miniCIM
	public static final double k0ToteP = 0.60, k0ToteI = 0.003, k0ToteD = 0;
	public static final double k1ToteP = 0.60, k1ToteI = 0.003, k1ToteD = 0;
	public static final double k2ToteP = 0.70, k2ToteI = 0.003, k2ToteD = 0;
	public static final double k3ToteP = 0.75, k3ToteI = 0.003, k3ToteD = 0;
	public static final double k4ToteP = 0.75, k4ToteI = 0.003, k4ToteD = 0;
	public static final double k5ToteP = 0.85, k5ToteI = 0.003, k5ToteD = 0;
	
	//With two 775s
//	public static final double k0ToteP = 0.70, k0ToteI = 0.002, k0ToteD = 0;
//	public static final double k1ToteP = 0.70, k1ToteI = 0.002, k1ToteD = 0;
//	public static final double k2ToteP = 0.85, k2ToteI = 0.002, k2ToteD = 0;
//	public static final double k3ToteP = 0.90, k3ToteI = 0.002, k3ToteD = 0;
//	public static final double k4ToteP = 1.0, k4ToteI = 0.003, k4ToteD = 0;
//	public static final double k5ToteP = 1.1, k5ToteI = 0.004, k5ToteD = 0;
	
	/* Utah values
	public static final double k0ToteP = 0.50, k0ToteI = 0.003, k0ToteD = 0;
	public static final double k2ToteP = 0.50, k2ToteI = 0.0075, k2ToteD = 0;
	public static final double k4ToteP = 0.50, k4ToteI = 0.010, k4ToteD = 0;
	*/
	
	public final CANTalon toteElevatorMotor;
	public final CANTalon toteElevatorMotorSmall;


	private double toteElevatorSpeed; // will be %vbus 
	private boolean usePosition;
	private double moveTo;
	private boolean bInitFinished = false;
	private boolean up = true;
	private boolean wasManual = false;
	private boolean useStatePosition = true;
	
	private ToteElevatorSystem() {
		toteElevatorMotor = RobotMap.MotorDefinition.toteElevatorMotor.getMotor();
		toteElevatorMotor.enableLimitSwitch(true, true);
		toteElevatorMotor.ConfigFwdLimitSwitchNormallyOpen(true);
		toteElevatorMotor.ConfigRevLimitSwitchNormallyOpen(true);
		toteElevatorMotor.enableBrakeMode(true);
		toteElevatorMotor.reverseSensor(false);
		toteElevatorMotor.reverseOutput(false);
		toteElevatorMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		//PID values default to this
		toteElevatorMotor.setPID(k0ToteP, k0ToteI, k0ToteD, 0.0001, 800, 24/0.250, 0);
		
		toteElevatorMotorSmall = RobotMap.MotorDefinition.toteElevatorMotorSmall.getMotor();
		toteElevatorMotorSmall.enableLimitSwitch(false, false);
		toteElevatorMotorSmall.enableBrakeMode(true);
		toteElevatorMotorSmall.changeControlMode(ControlMode.Follower);
		toteElevatorMotorSmall.set(RobotMap.MotorDefinition.toteElevatorMotor.id);
		toteElevatorMotorSmall.reverseOutput(true);
		
//		Preferences.putNumber("Current_RateOffsetConstant_Value", kRateOffsetConstant);
//		Preferences.putNumber("Current_RateOffset_Value", kRateOffset);
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

	public void setToteElevatorPosition(double position) {  //in catfinches
		moveTo = position;
		usePosition = true;
	}

	//set position in catfinches, not move motor. Only use for calibration
	public void setToteElevatorPositionValue(double position) { 
		toteElevatorMotor.setPosition(position*kEncoderTicksPerInch);
		moveTo = Double.NaN;
	}
	
	//get current position in inches
	public double getToteElevatorPosition() { 
		return toteElevatorMotor.getPosition()/kEncoderTicksPerInch;
	}
	
	public boolean isFinishedMoving() {
		if((getToteElevatorPosition() >= moveTo - 0.1) && (getToteElevatorPosition() <= moveTo + 0.1)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public double getToteElevatorRate() {
		return toteElevatorMotor.getEncVelocity() / 100;
	}

	private void stopAutoToteElevator() {
		usePosition = false;
		moveTo = Double.NaN;
		toteElevatorSpeed = 0.0;
	}
	
	public void toteElevatorUpdate() {
		if(!isSafeToRaiseTote()) {
			toteElevatorMotor.ClearIaccum();
		}

		boolean finalSetValuePosition;
		double finalSetValue;
		if(OI.getInstance().toteElevatorUpManualButton.get()) {
			finalSetValue = kToteElevatorUpSpeed;
			finalSetValuePosition = false;
			wasManual = true;
			up = true;
		}
		else if(OI.getInstance().toteElevatorDownManualButton.get()) {
			finalSetValue = kToteElevatorDownSpeed;
			finalSetValuePosition = false;
			wasManual = true;
			up = false;
		}
    	else if(OI.getInstance().lowerAndOpenClawButton.get() && 
    			(StateMachine.getInstance().getState() == StateMachine.State.Idle || StateMachine.getInstance().getState() == StateMachine.State.Abort)) {
    		finalSetValue = 0.0;
    		finalSetValuePosition = true;
    		useStatePosition = false;
    	}
		else {
			if(wasManual) {
				double rateOffset = getToteElevatorRate() * kRateOffset;
//				double rateOffset = getToteElevatorRate() * Preferences.getNumber("Current_RateOffset_Value", kRateOffset);
//				double rateOffsetConstant = Preferences.getNumber("Current_RateOffsetConstant_Value", kRateOffsetConstant) * (up ? 1.0 : -1.0);
				double rateOffsetConstant = kRateOffsetConstant * (up ? 1.0 : -1.0);
//				System.out.println(rateOffset + " " + rateOffsetConstant);
				setToteElevatorPosition(getToteElevatorPosition() + rateOffset + rateOffsetConstant);
//				System.out.println(getToteElevatorRate());
//				setToteElevatorPosition(getToteElevatorPosition());
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

//		SmartDashboard.putNumber("toteElevatorMotor.getIZone()", toteElevatorMotor.getIZone());
	}
	
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
		case Abort:
			break;
		default:
			break;
		}

		StateMachine.getInstance();
		switch (StateMachine.numberTotes) {
		case 0:
			toteElevatorMotor.setPID(k0ToteP, k0ToteI, k0ToteD, 0.0001, 800, 24/0.250, 0);
			break;
		case 1:
			toteElevatorMotor.setPID(k1ToteP, k1ToteI, k1ToteD, 0.0001, 800, 24/0.250, 0);
			break;
		case 2: 
			toteElevatorMotor.setPID(k2ToteP, k2ToteI, k2ToteD, 0.0001, 800, 24/0.250, 0);
			break;
		case 3:
			toteElevatorMotor.setPID(k3ToteP, k3ToteI, k3ToteD, 0.0001, 800, 24/0.250, 0);
			break;
		case 4:
			toteElevatorMotor.setPID(k4ToteP, k4ToteI, k4ToteD, 0.0001, 800, 24/0.250, 0);
			break;
		case 5:
			toteElevatorMotor.setPID(k5ToteP, k5ToteI, k5ToteD, 0.0001, 800, 24/0.250, 0);
			break;
		case 6:
			toteElevatorMotor.setPID(k5ToteP, k5ToteI, k5ToteD, 0.0001, 800, 24/0.250, 0);
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
