package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.UOI;
import org.usfirst.frc.team1619.robot.URobotMap;
import org.usfirst.frc.team1619.robot.UStateMachine;
import org.usfirst.frc.team1619.robot.UStateMachine.SignalName;
import org.usfirst.frc.team1619.robot.UStateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 */
public class UConveyorSystem extends UStateMachineSystem {
	// Competition Bot
	private static final double FORWARD_CONVEYOR_SPEED = -0.55;
	private static final double SLOW_FORWARD_CONVEYOR_SPEED = -0.55;
	private static final double MANUAL_FORWARD_CONVEYOR_SPEED = -1.0;
	private static final double MANUAL_BACK_CONVEYOR_SPEED = 1.0;
	private static final double CONVEYOR_DELAY_TIME = 0.4;
	private final double DEBOUNCE_TIME = 0.0;

	// Practice Bot
	// private static final double kForwardConveyorSpeed = -0.7;
	// private static final double kManualForwardConveyorSpeed = -0.7;
	// private static final double kManualBackConveyorSpeed = 0.7;

	public final CANTalon conveyorMotor; 

	private DigitalInput frontConveyorOpticalSensor;
	private DigitalInput rearConveyorOpticalSensor;

	private boolean frontSensor = false;
	private boolean rearSensor = false;

	private double conveyorSpeed = 0.0;

	private Timer frontSensorDebounceTimer = new Timer();
	private Timer rearSensorDebounceTimer = new Timer();

	public double getForwardSpeed() {
		return FORWARD_CONVEYOR_SPEED;
	}

	private UConveyorSystem() {
		conveyorMotor = URobotMap.MotorDefinition.conveyorMotor.getMotor();
		conveyorMotor.changeControlMode(ControlMode.PercentVbus);
		conveyorMotor.enableLimitSwitch(false, false);
		conveyorMotor.enableBrakeMode(false);

		frontConveyorOpticalSensor = new DigitalInput(
				URobotMap.FRONT_CONVEYOR_OPTICAL_SENSOR_ID);
		rearConveyorOpticalSensor = new DigitalInput(
				URobotMap.REAR_CONVEYOR_OPTICAL_SENSOR_ID);

		frontSensorDebounceTimer.start();
		rearSensorDebounceTimer.start();
	}

	private static UConveyorSystem theSystem;

	public static UConveyorSystem getInstance() {
		if (theSystem == null)
			theSystem = new UConveyorSystem();
		return theSystem;
	}

	public void updateConveyorSignals() {

		if (!getFrontSensorRaw()) {
			if (frontSensor) {
				frontSensor = false;
				UStateMachine.getSignal(SignalName.HUMAN_FEED_THROTTLE_CONVEYOR_DESCEND).raise();
			}
		}
		else {
			if (!frontSensor) {
				frontSensor = true;
				frontSensorDebounceTimer.reset();
				UStateMachine.getSignal(SignalName.HUMAN_FEED_THROTTLE_CONVEYOR_BACK).raise();
			}
		}
		if (getRearSensorRaw()) {
			rearSensor = rearSensorDebounceTimer.get() > DEBOUNCE_TIME;
			if (rearSensor) {
				UStateMachine.getSignal(SignalName.HUMAN_FEED_TOTE_ON_CONVEYOR).raise();
			}
		}
		else {
			rearSensor = false;
			rearSensorDebounceTimer.reset();
		}
	}


	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
	}

	public boolean getFrontSensorRaw() {
		return !frontConveyorOpticalSensor.get();
	}

	public boolean getRearSensorRaw() {
		return !rearConveyorOpticalSensor.get();
	}

	public boolean getFrontSensor() {
		return frontSensor;
	}

	public boolean getRearSensor() {
		return rearSensor;
	}

	private void updateConveyor() {
		if (UOI.getInstance().conveyorForwardButton.get()) {
			conveyorMotor.set(MANUAL_FORWARD_CONVEYOR_SPEED);
		}
		else if (UOI.getInstance().conveyorBackButton.get()) {
			conveyorMotor.set(MANUAL_BACK_CONVEYOR_SPEED);
		}
		else {
			conveyorMotor.set(conveyorSpeed);
		}
	}

	@Override
	public void init(State state) {
		switch (state) {
		default:
			break;
		}
	};

	@Override
	public void run(State state, double elapsed) {
		updateConveyorSignals();

		double forwardConveyorSpeed = FORWARD_CONVEYOR_SPEED;
		double slowForwardConveyorSpeed = SLOW_FORWARD_CONVEYOR_SPEED;
		double conveyorDelayTime = CONVEYOR_DELAY_TIME;

		switch (state) {
		case Init:
			conveyorSpeed = 0.0;
			break;
		case Idle:
			conveyorSpeed = 0.0;
			break;
		case HumanFeed_RaiseTote:
			conveyorSpeed = forwardConveyorSpeed;
			break;
		case HumanFeed_WaitForTote:
			conveyorSpeed = forwardConveyorSpeed;
			break;
		case HumanFeed_ToteOnConveyor:
			conveyorSpeed = forwardConveyorSpeed;
			break;
		case HumanFeed_ThrottleConveyorBack:
			conveyorSpeed = elapsed > conveyorDelayTime ? slowForwardConveyorSpeed
					: forwardConveyorSpeed;
			break;
		case HumanFeed_ThrottleConveyorAndDescend:
			conveyorSpeed = forwardConveyorSpeed;
			break;
		case Abort:
			conveyorSpeed = 0.0;
			break;
		default:
			break;
		}

		updateConveyor();
	}
}
