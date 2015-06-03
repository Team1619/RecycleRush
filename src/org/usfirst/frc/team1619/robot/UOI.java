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
	
	
	/**
	 * Operation Pairing via Buttons
	 * pairs functions together that use the same button
	 */
	private final InternalButton kachigLeft, kachigRight, kachigForward, kachigBackward;
	public final InternalButton binElevatorUp, binElevatorDown;
	
	//rightStick
	private final JoystickButton resetDriveEncodersButton;
	public final JoystickButton liftAbortButton;
	public final JoystickButton liftResetButton;
	
	public final JoystickButton startHumanFeedButton;
	public final JoystickButton stopHumanFeedButton;
	
	public final JoystickButton toteElevatorUpManualButton;
	public final JoystickButton toteElevatorDownManualButton;
	
	public final JoystickButton lowerToteElevatorAndOpenClawButton;
	public final JoystickButton driverCloseButton;
	
	//leftStick
	public final JoystickButton closeClawButton;
	public final JoystickButton openClawButton;
//	public final JoystickButton binTiltManualButton;
	public final JoystickButton rakerDownManualButton;
	public final JoystickButton rakerUpManualButton;
//	public final JoystickButton moveClawForBinPickupButton;
//	public final JoystickButton moveClawForNoodleInsertionButton;
		
	public final JoystickButton conveyorForwardButton;
	public final JoystickButton conveyorBackButton;
	
	public final JoystickButton guardRailOpenButton;
	public final JoystickButton guardRailCloseButton;
	
	public final JoystickButton incrementNumberTotesButton;
	UOI() {
		rightStick = new Joystick(URobotMap.rightStickID);
		leftStick = new Joystick(URobotMap.leftStickID);
		tiltStick = leftStick;
		
		//Right stick
		kachigLeft = new InternalButton();
		kachigRight = new InternalButton();
		kachigForward = new InternalButton();
		kachigBackward = new InternalButton();
		
		resetDriveEncodersButton = new JoystickButton(rightStick, URobotMap.resetDriveEncodersButtonID);
		
		liftAbortButton = new JoystickButton(rightStick, URobotMap.liftAbortButtonID);
		liftResetButton = new JoystickButton(rightStick, URobotMap.liftResetButtonID);
		
		startHumanFeedButton = new JoystickButton(rightStick, URobotMap.startHumanFeedButtonID);
		stopHumanFeedButton = new JoystickButton(rightStick, URobotMap.stopHumanFeedButtonID);
		
		incrementNumberTotesButton = new JoystickButton(rightStick, URobotMap.incrementNumberTotesButtonID);
		
		toteElevatorUpManualButton = new JoystickButton(rightStick, URobotMap.toteElevatorUpManualButtonID);
		toteElevatorDownManualButton = new JoystickButton(rightStick, URobotMap.toteElevatorDownManualButtonID);

		lowerToteElevatorAndOpenClawButton = new JoystickButton(rightStick, URobotMap.lowerToteElevatorAndOpenClawButtonID);
		driverCloseButton = new JoystickButton(rightStick, URobotMap.driverCloseClawButtonID);
		
		//Left stick
		binElevatorUp = new InternalButton();
		binElevatorDown = new InternalButton();
//		binTiltUpButton = new InternalButton();
//		binTiltDownButton = new InternalButton();
		
		closeClawButton = new JoystickButton(leftStick, URobotMap.closeClawButtonID);
		openClawButton = new JoystickButton(leftStick, URobotMap.openClawButtonID);

		rakerDownManualButton = new JoystickButton(leftStick, URobotMap.rakerDownManualButtonID);
		rakerUpManualButton = new JoystickButton(leftStick, URobotMap.rakerUpManualButtonID);
//		binTiltManualButton = new JoystickButton(leftStick, RobotMap.binTiltManualButtonID);
//		moveClawForBinPickupButton = new JoystickButton(leftStick, RobotMap.moveClawForBinPickupButtonID);
//		moveClawForNoodleInsertionButton = new JoystickButton(leftStick, RobotMap.moveClawForNoodleInsertionButtonID);
		
		
		conveyorForwardButton = new JoystickButton(leftStick, URobotMap.coneyorFowardButtonID);
		conveyorBackButton = new JoystickButton(leftStick, URobotMap.coneyorBackButtonID);
		
		guardRailOpenButton = new JoystickButton(leftStick, URobotMap.guardrailOpenManualButtonID);
		guardRailCloseButton = new JoystickButton(leftStick, URobotMap.guardrailCloseManualButtonID);
	}
	
	public void init() {
		/**
		 * whenPressed = function that when a button is pressed, it starts and doesn't stop when released
		 * whileHeld = function that only operates when a button is pressed and held, and stops when released
		 */
		kachigLeft.whenPressed(new UKachigCommand(0, -1, 0.075));
		kachigRight.whenPressed(new UKachigCommand(0, 1, 0.075));
		kachigForward.whenPressed(new UKachigCommand(1, 0, 0.05));
		kachigBackward.whenPressed(new UKachigCommand(-1, 0, 0.05));
		
		resetDriveEncodersButton.whenPressed(new UResetDriveEncodersCommand());
		
		startHumanFeedButton.whenPressed(new URaiseSignalCommand(UStateMachine.getInstance().humanFeed_Start));
		stopHumanFeedButton.whenPressed(new URaiseSignalCommand(UStateMachine.getInstance().humanFeed_Stop));
		
		// unstickToteButton.whileHeld(new UnstickToteCommand());
		//unloadConveyorButton.whenPressed(new UnloadConveyorCommand());
		
		liftAbortButton.whenPressed(new URaiseSignalCommand(UStateMachine.getInstance().abortSignal));
		liftResetButton.whenPressed(new URaiseSignalCommand(UStateMachine.getInstance().resetSignal));
		}
	
	public void updatePOV() {
		int povLeft = rightStick.getPOV();
		kachigRight.setPressed(povLeft == 90);
		kachigForward.setPressed(povLeft == 0);
		kachigLeft.setPressed(povLeft == 270);
		kachigBackward.setPressed(povLeft == 180);
		
		int povRight = leftStick.getPOV();
		binElevatorUp.setPressed(povRight == 0);
		binElevatorDown.setPressed(povRight == 180);
//		binTiltUpButton.setPressed(povRight == 180);
//		binTiltDownButton.setPressed(povRight == 0);
	}
	
	private static UOI oi = new UOI();
	
	public static UOI getInstance() {
		return oi;
	}
}

