package org.usfirst.frc.team1619.robot;

import org.usfirst.frc.team1619.robot.commands.UKachigCommand;
import org.usfirst.frc.team1619.robot.commands.URaiseSignalCommand;
import org.usfirst.frc.team1619.robot.commands.UResetDriveEncodersCommand;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.InternalButton;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class UOI {

	public final Joystick rightStick;
	public final Joystick leftStick;
	public final Joystick tiltStick;

	private final InternalButton kachigLeft, kachigRight, kachigForward, kachigBackward;
	public final InternalButton binElevatorUp, binElevatorDown;

	// rightStick
	private final JoystickButton resetDriveEncodersButton;
	public final JoystickButton liftAbortButton;
	public final JoystickButton liftResetButton;

	public final JoystickButton startHumanFeedButton;
	public final JoystickButton stopHumanFeedButton;

	public final JoystickButton toteElevatorUpManualButton;
	public final JoystickButton toteElevatorDownManualButton;

	public final JoystickButton lowerToteElevatorAndOpenClawButton;
	public final JoystickButton driverCloseButton;

	public final JoystickButton incrementNumberTotesButton;

	// leftStick
	public final JoystickButton closeClawButton;
	public final JoystickButton openClawButton;
	public final JoystickButton rakerDownManualButton;
	public final JoystickButton rakerUpManualButton;

	public final JoystickButton conveyorForwardButton;
	public final JoystickButton conveyorBackButton;

	public final JoystickButton guardRailOpenButton;
	public final JoystickButton guardRailCloseButton;

	// Creates singleton OI
	private static UOI sOI = new UOI();

	public static UOI getInstance() {
		return sOI;
	}

	UOI() {
		rightStick = new Joystick(URobotMap.RIGHT_STICK_ID);
		leftStick = new Joystick(URobotMap.LEFT_STICK_ID);
		tiltStick = leftStick;

		// Right stick
		kachigLeft = new InternalButton();
		kachigRight = new InternalButton();
		kachigForward = new InternalButton();
		kachigBackward = new InternalButton();

		resetDriveEncodersButton = new JoystickButton(rightStick, URobotMap.RESET_DRIVE_ENCODERS_BUTTON_ID);

		liftAbortButton = new JoystickButton(rightStick, URobotMap.LIFT_ABORT_BUTTON_ID);
		liftResetButton = new JoystickButton(rightStick, URobotMap.LIFT_RESET_BUTTON_ID);

		startHumanFeedButton = new JoystickButton(rightStick, URobotMap.START_HUMAN_FEED_BUTTON_ID);
		stopHumanFeedButton = new JoystickButton(rightStick, URobotMap.STOP_HUMAN_FEED_BUTTON_ID);

		incrementNumberTotesButton = new JoystickButton(rightStick, URobotMap.INCREMENT_NUMBER_TOTES_BUTTON_ID);

		toteElevatorUpManualButton = new JoystickButton(rightStick, URobotMap.TOTE_ELEVATOR_UP_MANUAL_BUTTON_ID);
		toteElevatorDownManualButton = new JoystickButton(rightStick, URobotMap.TOTE_ELEVATOR_DOWN_MANUAL_BUTTON_ID);

		lowerToteElevatorAndOpenClawButton = new JoystickButton(rightStick, URobotMap.LOWER_TOTE_ELEVATOR_AND_OPEN_CLAW_BUTTON_ID);
		driverCloseButton = new JoystickButton(rightStick, URobotMap.DRIVER_CLOSE_CLAW_BUTTON_ID);

		// Left stick
		binElevatorUp = new InternalButton();
		binElevatorDown = new InternalButton();

		closeClawButton = new JoystickButton(leftStick, URobotMap.CLOSE_CLAW_BUTTON_ID);
		openClawButton = new JoystickButton(leftStick, URobotMap.OPEN_CLAW_BUTTON_ID);

		rakerDownManualButton = new JoystickButton(leftStick, URobotMap.RAKER_DOWN_MANUAL_BUTTON_ID);
		rakerUpManualButton = new JoystickButton(leftStick, URobotMap.RAKER_UP_MANUAL_BUTTON_ID);

		conveyorForwardButton = new JoystickButton(leftStick, URobotMap.CONEYOR_FOWARD_BUTTON_ID);
		conveyorBackButton = new JoystickButton(leftStick, URobotMap.CONVEYOR_BACK_BUTTON_ID);

		guardRailOpenButton = new JoystickButton(leftStick, URobotMap.GUARDRAIL_OPEN_MANUAL_BUTTON_ID);
		guardRailCloseButton = new JoystickButton(leftStick, URobotMap.GUARDRAIL_CLOSE_MANUAL_BUTTON_ID);
	}

	public void init() {
		kachigLeft.whenPressed(new UKachigCommand(0, -1, 0.075));
		kachigRight.whenPressed(new UKachigCommand(0, 1, 0.075));
		kachigForward.whenPressed(new UKachigCommand(1, 0, 0.05));
		kachigBackward.whenPressed(new UKachigCommand(-1, 0, 0.05));

		resetDriveEncodersButton.whenPressed(new UResetDriveEncodersCommand());

		startHumanFeedButton.whenPressed(new URaiseSignalCommand(UStateMachine.getInstance().humanFeed_Start));
		stopHumanFeedButton.whenPressed(new URaiseSignalCommand(UStateMachine.getInstance().humanFeed_Stop));

		liftAbortButton.whenPressed(new URaiseSignalCommand(UStateMachine.getInstance().abortSignal));
		liftResetButton.whenPressed(new URaiseSignalCommand(UStateMachine.getInstance().resetSignal));
	}

	public void updatePOV() {
		int povRight = rightStick.getPOV();
		kachigRight.setPressed(povRight == 90);
		kachigForward.setPressed(povRight == 0);
		kachigLeft.setPressed(povRight == 270);
		kachigBackward.setPressed(povRight == 180);

		int povLeft = leftStick.getPOV();
		binElevatorUp.setPressed(povLeft == 0);
		binElevatorDown.setPressed(povLeft == 180);
	}
}
