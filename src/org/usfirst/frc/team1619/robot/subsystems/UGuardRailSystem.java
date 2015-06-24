package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.UOI;
import org.usfirst.frc.team1619.robot.URobotMap;
import org.usfirst.frc.team1619.robot.UStateMachine;
import org.usfirst.frc.team1619.robot.UStateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 */
public class UGuardRailSystem extends UStateMachineSystem {
	// The competition bot and practice bot are now the same.
	public static final double kCloseGuardRailSpeed = -0.5;

	public static final double kOpenGuardRailSpeed = 0.40;
	public static final double kSlowOpenGuardRailSpeed = 0.00;
	public static final double kSlowCloseGuardRailSpeed = 0.00;

	private Timer humanFeedCloseTimer = new Timer();
	private boolean closeInHumanFeed = false;

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	public final CANTalon guardRailMotor; // overdrive slightly

	private UGuardRailSystem() {
		guardRailMotor = URobotMap.MotorDefinition.guardRailMotor.getMotor();
		guardRailMotor.changeControlMode(ControlMode.PercentVbus);
		guardRailMotor.enableLimitSwitch(false, false);
		guardRailMotor.enableBrakeMode(false);

		humanFeedCloseTimer.start();
	}

	private static UGuardRailSystem theSystem = new UGuardRailSystem();

	public static UGuardRailSystem getInstance() {
		return theSystem;
	}

	private double guardRailSpeed = 0.0;

	private void updateGuardRail() {
		if (UOI.getInstance().guardRailOpenButton.get()) {
			guardRailMotor.set(kOpenGuardRailSpeed);
		}
		else if (UOI.getInstance().guardRailCloseButton.get()) {
			guardRailMotor.set(kCloseGuardRailSpeed);
		}
		else {
			guardRailMotor.set(guardRailSpeed);
		}
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new ManualGuardRailCommand(0.0));
	}

	public void init(State state) {
		guardRailSpeed = 0.0;
		switch (state) {
		case Init:
			break;
		case HumanFeed_RaiseTote:
			closeInHumanFeed = true;
			break;
		default:
			break;
		}
	}

	@Override
	public void run(State state, double elapsed) {
		switch (state) {
		case Init:
			break;
		case Idle:
			guardRailSpeed = 0.0;
			break;
		case HumanFeed_RaiseTote:
			if (!UConveyorSystem.getInstance().getRearSensor()) {
				if (elapsed <= 0.25) { // just at beginning
					guardRailSpeed = kOpenGuardRailSpeed;
				}
				else {
					guardRailSpeed = kSlowOpenGuardRailSpeed;
				}
			}
			else {
				if (closeInHumanFeed) {
					humanFeedCloseTimer.reset();
					closeInHumanFeed = false;
				}
				if (humanFeedCloseTimer.get() <= 1.0) {
					guardRailSpeed = kCloseGuardRailSpeed;
				}
				else {
					guardRailSpeed = kSlowCloseGuardRailSpeed;
				}
			}
			break;
		case HumanFeed_WaitForTote:
			guardRailSpeed = kSlowOpenGuardRailSpeed;
			break;
		case HumanFeed_ToteOnConveyor:
			if (elapsed <= 1.0) {
				guardRailSpeed = kCloseGuardRailSpeed;
			}
			else {
				guardRailSpeed = kSlowCloseGuardRailSpeed;
			}
			break;
		case HumanFeed_ThrottleConveyorAndDescend:
			if (UStateMachine.getToStopHumanFeed()) {
				guardRailSpeed = 0.0;
			}
			else {
				guardRailSpeed = kOpenGuardRailSpeed;
			}
			break;
		case Abort:
			guardRailSpeed = 0.0;
			break;
		default:
			break;
		}

		updateGuardRail();
	}
}
