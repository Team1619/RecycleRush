package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.StateMachine;
import org.usfirst.frc.team1619.robot.StateMachine.State;
import org.usfirst.frc.team1619.Preferences;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 */
public class ToteElevatorSystem extends StateMachineSystem {
	public static final double kEncoderTicksPerInch = 5468/22.5; //fish
	public static final double kTransitPosition = 2.0;
	public static final double kFeederPosition = 18.8;
	public static final double kPickUpPosition = -0.5;
	public static final double kPositionTolerance = 1.0;
	public static final double kSlowdownDistance = 14.0;
	public static final double kDeadZone = 0.25;
	public static final double kInitSpeed = -0.5;
	public static final double kRateOffset = 0.4;
	public static final double kRateOffsetConstant = 2.5;
	
	public static final double kToteElevatorUpSpeed = 0.7;
	public static final double kToteElevatorDownSpeed = -0.5;
	
	public static final double kDebounceTime = 1.0;
	
	//With Two miniCIM
	public static double k0TotePUp = 0.720, k0TotePDown = 0.72, k0ToteI = 0.005, k0ToteD = 0;
	public static double k1TotePUp = 0.810, k1TotePDown = 0.78, k1ToteI = 0.006, k1ToteD = 0;
	public static double k2TotePUp = 0.855, k2TotePDown = 0.82, k2ToteI = 0.007, k2ToteD = 0;
	public static double k3TotePUp = 0.945, k3TotePDown = 0.75, k3ToteI = 0.008, k3ToteD = 0;
	public static double k4TotePUp = 1.035, k4TotePDown = 0.60, k4ToteI = 0.009, k4ToteD = 0;
	public static double k5TotePUp = 1.260, k5TotePDown = 0.55, k5ToteI = 0.011, k5ToteD = 0;
	
	//With CIM and miniCIM
//	public static final double k0ToteP = 0.60, k0ToteI = 0.003, k0ToteD = 0;
//	public static final double k1ToteP = 0.60, k1ToteI = 0.003, k1ToteD = 0;
//	public static final double k2ToteP = 0.70, k2ToteI = 0.003, k2ToteD = 0;
//	public static final double k3ToteP = 0.75, k3ToteI = 0.003, k3ToteD = 0;
//	public static final double k4ToteP = 0.75, k4ToteI = 0.003, k4ToteD = 0;
//	public static final double k5ToteP = 0.85, k5ToteI = 0.003, k5ToteD = 0;
	
	//With two 775s
//	public static final double k0ToteP = 0.70, k0ToteI = 0.002, k0ToteD = 0;
//	public static final double k1ToteP = 0.70, k1ToteI = 0.002, k1ToteD = 0;
//	public static final double k2ToteP = 0.85, k2ToteI = 0.002, k2ToteD = 0;
//	public static final double k3ToteP = 0.90, k3ToteI = 0.002, k3ToteD = 0;
//	public static final double k4ToteP = 1.0, k4ToteI = 0.003, k4ToteD = 0;
//	public static final double k5ToteP = 1.1, k5ToteI = 0.004, k5ToteD = 0;
	
//	public static final double k0TotePosition = 1.0, k1TotePosition = 2.0, k2TotePosition = 3.0, k3TotePosition = 4.0, k4TotePosition = 5.0, k5TotePosition = 6.0;
	
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
	private boolean isSafeToRaiseTote = true;
	private Timer safeToRaiseToteTimer = new Timer();

	
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
		toteElevatorMotor.setPID(k0TotePUp, k0ToteI, k0ToteD, 0.0001, 800, 24/0.250, 0);
		
		toteElevatorMotorSmall = RobotMap.MotorDefinition.toteElevatorMotorSmall.getMotor();
		toteElevatorMotorSmall.enableLimitSwitch(false, false);
		toteElevatorMotorSmall.enableBrakeMode(true);
		toteElevatorMotorSmall.changeControlMode(ControlMode.Follower);
		toteElevatorMotorSmall.set(RobotMap.MotorDefinition.toteElevatorMotor.id);
		toteElevatorMotorSmall.reverseOutput(true);
		
//		Preferences.putNumber("Current_RateOffsetConstant_Value", kRateOffsetConstant);
//		Preferences.putNumber("Current_RateOffset_Value", kRateOffset);
		
		safeToRaiseToteTimer.start();
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
		
		if(!checkIfSafeToRaiseTote()) {
			if(isSafeToRaiseTote) {
				isSafeToRaiseTote = !(safeToRaiseToteTimer.get() > kDebounceTime);
			}
		}
		else {
			if(!isSafeToRaiseTote) {
				isSafeToRaiseTote = true;
				toteElevatorMotor.ClearIaccum();
			}
			safeToRaiseToteTimer.reset();
		}	
		
		if(!isFinishedMoving()) {
			toteElevatorMotor.ClearIaccum();

		}	
		
		if(OI.getInstance().lowerToteElevatorAndOpenClawButton.get() && 
    			(StateMachine.getInstance().getState() == StateMachine.State.Idle || StateMachine.getInstance().getState() == StateMachine.State.Abort)) {
			setToteElevatorPosition(0);
			useStatePosition = false;
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
			if(isSafeToRaiseTote || (finalSetValue < 0)) {
				toteElevatorMotor.changeControlMode(ControlMode.Position);
				toteElevatorMotor.set(finalSetValue);
			}
			else {
				toteElevatorMotor.changeControlMode(ControlMode.PercentVbus);
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
	
	public boolean checkIfSafeToRaiseTote() {
		return BinElevatorSystem.getInstance().getTilterBackLimitSwitch();
	}
	
	public void init(State state) {
		stopAutoToteElevator();
		
		useStatePosition = true;
		
		switch(state) {
		case Init:
			
			k0TotePUp = Preferences.getNumber("k0TotePUp", k0TotePUp);
			k0ToteI = Preferences.getNumber("k0ToteI", k0ToteI);
			k1TotePUp = Preferences.getNumber("k1TotePUp", k1TotePUp);
			k1ToteI = Preferences.getNumber("k1ToteI", k1ToteI);
			k2TotePUp = Preferences.getNumber("k2TotePUp", k2TotePUp);
			k2ToteI = Preferences.getNumber("k2ToteI", k2ToteI);
			k3TotePUp = Preferences.getNumber("k3TotePUp", k3TotePUp);
			k3ToteI = Preferences.getNumber("k3ToteI", k3ToteI);
			k4TotePUp = Preferences.getNumber("k4TotePUp", k4TotePUp);
			k4ToteI = Preferences.getNumber("k4ToteI", k4ToteI);
			k5TotePUp = Preferences.getNumber("k5TotePUp", k5TotePUp);
			k5ToteI = Preferences.getNumber("k5ToteI", k5ToteI);
			
			k0TotePDown = Preferences.getNumber("k0TotePDown", k0TotePDown);
			k1TotePDown = Preferences.getNumber("k1TotePDown", k1TotePDown);
			k2TotePDown = Preferences.getNumber("k2TotePDown", k2TotePDown);
			k3TotePDown = Preferences.getNumber("k3TotePDown", k3TotePDown);
			k4TotePDown = Preferences.getNumber("k4TotePDown", k4TotePDown);
			k5TotePDown = Preferences.getNumber("k5TotePDown", k5TotePDown);
			
			bInitFinished = false;
			toteElevatorMotor.setPID(k0TotePUp, k0ToteI, k0ToteD, 0.0001, 800, 24/0.250, 0);
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
//				double slowdownDis1tance = Preferences.getNumber("slowdownDistance", kSlowdownDistance);
				if(getToteElevatorPosition() > kSlowdownDistance + kPositionTolerance) {
					setToteElevatorPosition(kSlowdownDistance);
				}
				else {
					setToteElevatorPosition(kPickUpPosition);
				}
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
		
		// set PID values based on number of totes currently in robot, and direction of toteElevator movement
		switch(state) {
		case HumanFeed_ThrottleConveyorAndDescend:
			switch (StateMachine.getInstance().numberTotes) {
			case 0:
				toteElevatorMotor.setPID(k0TotePDown, k0ToteI, k0ToteD, 0.0001, 800, 24/0.250, 0);
				break;
			case 1:
				toteElevatorMotor.setPID(k1TotePDown, k1ToteI, k1ToteD, 0.0001, 800, 24/0.250, 0);
				break;
			case 2: 
				toteElevatorMotor.setPID(k2TotePDown, k2ToteI, k2ToteD, 0.0001, 800, 24/0.250, 0);
				break;
			case 3:
				toteElevatorMotor.setPID(k3TotePDown, k3ToteI, k3ToteD, 0.0001, 800, 24/0.250, 0);
				break;
			case 4:
				toteElevatorMotor.setPID(k4TotePDown, k4ToteI, k4ToteD, 0.0001, 800, 24/0.250, 0);
				break;
			case 5:
				toteElevatorMotor.setPID(k5TotePDown, k5ToteI, k5ToteD, 0.0001, 800, 24/0.250, 0);
				break;
			case 6:
				toteElevatorMotor.setPID(k5TotePDown, k5ToteI, k5ToteD, 0.0001, 800, 24/0.250, 0);
				break;
			default:
				break;
			}
			break;
		default:
			switch (StateMachine.getInstance().numberTotes) {
			case 0:
				toteElevatorMotor.setPID(k0TotePUp, k0ToteI, k0ToteD, 0.0001, 800, 24/0.250, 0);
				break;
			case 1:
				toteElevatorMotor.setPID(k1TotePUp, k1ToteI, k1ToteD, 0.0001, 800, 24/0.250, 0);
				break;
			case 2: 
				toteElevatorMotor.setPID(k2TotePUp, k2ToteI, k2ToteD, 0.0001, 800, 24/0.250, 0);
				break;
			case 3:
				toteElevatorMotor.setPID(k3TotePUp, k3ToteI, k3ToteD, 0.0001, 800, 24/0.250, 0);
				break;
			case 4:
				toteElevatorMotor.setPID(k4TotePUp, k4ToteI, k4ToteD, 0.0001, 800, 24/0.250, 0);
				break;
			case 5:
				toteElevatorMotor.setPID(k5TotePUp, k5ToteI, k5ToteD, 0.0001, 800, 24/0.250, 0);
				break;
			case 6:
				toteElevatorMotor.setPID(k5TotePUp, k5ToteI, k5ToteD, 0.0001, 800, 24/0.250, 0);
				break;
			default:
				break;
			}
			break;
		}

		toteElevatorUpdate();
	}

	public boolean initFinished() {
		return bInitFinished;
	}
}
