package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.UOI;
import org.usfirst.frc.team1619.robot.URobotMap;
import org.usfirst.frc.team1619.robot.UStateMachine;
import org.usfirst.frc.team1619.robot.UStateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;


public class UBinElevatorSystem extends UStateMachineSystem {

	public static final double INIT_SPEED = -0.4;
	public static final double BIN_ELEVATOR_UP_SPEED = -0.4;
	public static final double BIN_ELEVATOR_DOWN_SPEED = 0.4;

	public static final double BIN_IDLE_SPEED = -0.2;
	public static final double BIN_TILT_HUMAN_FEED_SPEED = -0.2;

	public static final double BIN_GRIP_OPEN_SPEED = 0.8;
	public static final double BIN_GRIP_CLOSE_SPEED = -0.8;
	public static final double BIN_GRIP_OPEN_SPEED_SLOW = 0.4;
	public static final double TOTE_ELEVATOR_SAFETY_FOR_TILT = 5.5; // fish

	public static final double BIN_GRIP_OPEN_TIME = 0.5;

	public final CANTalon fBinElevatorMotor;
	public final CANTalon fTilterMotor;
	public final CANTalon fBinGripMotor;

	private double fBinElevatorSpeed; // will be %vbus

	private double fBinGripSpeed = 0.0;
	private double fTilterMotorSpeed = 0.0;

	private UBinElevatorSystem() {
		fBinElevatorMotor = URobotMap.MotorDefinition.binElevatorMotor
				.getMotor();
		fBinElevatorMotor.enableLimitSwitch(true, true);
		fBinElevatorMotor.enableBrakeMode(true);
		fBinElevatorMotor.reverseOutput(true);

		fTilterMotor = URobotMap.MotorDefinition.tilterMotor.getMotor();
		fTilterMotor.enableLimitSwitch(true, true);
		fTilterMotor.enableBrakeMode(true);
		fTilterMotor.ConfigFwdLimitSwitchNormallyOpen(true);
		fTilterMotor.ConfigRevLimitSwitchNormallyOpen(true);

		fBinGripMotor = URobotMap.MotorDefinition.binGripMotor.getMotor();
		fBinGripMotor.enableLimitSwitch(false, false);
		fBinGripMotor.enableBrakeMode(true);
	}

	private final static UBinElevatorSystem theSystem = new UBinElevatorSystem();

	public static UBinElevatorSystem getInstance() {
		return theSystem;
	}

	public void initDefaultCommand() {
	}

	public void setBinElevatorSpeed(double speed) {
		fBinElevatorSpeed = speed;
	}

	public void binElevatorUpdate() {
		if (UOI.getInstance().binElevatorUp.get()) {
			fBinElevatorMotor.set(BIN_ELEVATOR_UP_SPEED);
		}
		else if (UOI.getInstance().binElevatorDown.get()) {
			fBinElevatorMotor.set(BIN_ELEVATOR_DOWN_SPEED);
		}
		else {
			fBinElevatorMotor.set(fBinElevatorSpeed);
		}
	}

	public void setBinTilt(double moveValue) {
		fTilterMotorSpeed = moveValue;
	}

	public void binTiltUpdate() {
		double finalTiltSpeed;

		// get the tilt speed we want
		double joystickY = UOI.getInstance().tiltStick.getY();
		boolean manual = Math.abs(joystickY) > 0.1;
		if (manual) {
			finalTiltSpeed = joystickY;
		}
		else {
			finalTiltSpeed = fTilterMotorSpeed;
		}

		// assign the speed if it's safe
		if (isSafeToTilt() || (finalTiltSpeed > 0)) {
			fTilterMotor.changeControlMode(ControlMode.PercentVbus);
			fTilterMotor.set(finalTiltSpeed);
		}
		else {
			fTilterMotor.changeControlMode(ControlMode.PercentVbus);
			fTilterMotor.set(0.0);
		}
	}

	public boolean isSafeToTilt() {
		if (UToteElevatorSystem.getInstance().getToteElevatorPosition() <= TOTE_ELEVATOR_SAFETY_FOR_TILT) {
			switch (UStateMachine.getInstance().getState()) {
			case Init:
			case Idle:
				return true;
			default:
				return false;
			}
		}
		else {
			return false;
		}
	}

	public boolean getTilterBackLimitSwitch() {
		return fTilterMotor.isFwdLimitSwitchClosed();
	}

	public boolean getTilterFowardLimitSwitch() {
		return fTilterMotor.isRevLimitSwitchClosed();
	}

	public void setBinGrip(double moveValue) {
		fBinGripSpeed = moveValue;
	}

	public void binGripUpdate() {
		if (UOI.getInstance().openClawButton.get()) {
			fBinGripMotor.set(BIN_GRIP_OPEN_SPEED);
		}
		else if (UOI.getInstance().closeClawButton.get()) {
			fBinGripMotor.set(BIN_GRIP_CLOSE_SPEED);
		}
		else if (UOI.getInstance().driverCloseButton.get()) {
			fBinGripMotor.set(BIN_GRIP_CLOSE_SPEED);
		}
		else if (UOI.getInstance().lowerToteElevatorAndOpenClawButton.get()
				&& (UStateMachine.getInstance().getState() == UStateMachine.State.Idle || UStateMachine
						.getInstance().getState() == UStateMachine.State.Abort)) {
			fBinGripMotor.set(BIN_GRIP_OPEN_SPEED);
		}
		else {
			fBinGripMotor.set(fBinGripSpeed);
		}
	}

	public void init(State state) {
		setBinElevatorSpeed(0.0);
		setBinTilt(0.0);
		setBinGrip(0.0);

		switch (state) {
		case Init:
			break;
		case Idle:
			break;
		case HumanFeed_RaiseTote:
			break;
		case HumanFeed_ThrottleConveyorAndDescend:
			break;
		case HumanFeed_ToteOnConveyor:
			break;
		case HumanFeed_WaitForTote:
			break;
		default:
			break;
		}
	}

	@Override
	public void run(State state, double elapsed) {
		switch (state) {
		case Init:
			setBinElevatorSpeed(INIT_SPEED);
			break;
		case Idle:
			setBinElevatorSpeed(0);
			break;
		case HumanFeed_RaiseTote:
			setBinElevatorSpeed(BIN_IDLE_SPEED);
			setBinTilt(BIN_TILT_HUMAN_FEED_SPEED);
			break;
		case HumanFeed_WaitForTote:
			setBinElevatorSpeed(BIN_IDLE_SPEED);
			setBinTilt(BIN_TILT_HUMAN_FEED_SPEED);
			break;
		case HumanFeed_ToteOnConveyor:
			setBinElevatorSpeed(BIN_IDLE_SPEED);
			setBinTilt(BIN_TILT_HUMAN_FEED_SPEED);
			break;
		case HumanFeed_ThrottleConveyorBack:
			setBinElevatorSpeed(BIN_IDLE_SPEED);
			setBinTilt(BIN_TILT_HUMAN_FEED_SPEED);
			break;
		case HumanFeed_ThrottleConveyorAndDescend:
			setBinElevatorSpeed(BIN_IDLE_SPEED);
			setBinTilt(BIN_TILT_HUMAN_FEED_SPEED);
			break;
		case TiltUp:
			setBinTilt(0.7);
			break;
		case Abort:
			setBinElevatorSpeed(0);
			break;
		default:
			break;
		}

		binElevatorUpdate();
		binGripUpdate();
		binTiltUpdate();
	}

	public boolean initFinished() {
		return true;
	}
}
