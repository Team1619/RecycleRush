package org.usfirst.frc.team1619.robot;

import org.usfirst.frc.team1619.robot.commands.KachigCommand;
import org.usfirst.frc.team1619.robot.commands.RaiseSignalCommand;
import org.usfirst.frc.team1619.robot.commands.ResetDriveEncodersCommand;
import org.usfirst.frc.team1619.robot.commands.ResetGyroCommand;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.InternalButton;
import edu.wpi.first.wpilibj.buttons.JoystickButton;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	
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
	private final JoystickButton resetGyroButton;
	private final JoystickButton resetDriveEncodersButton;
	public final JoystickButton liftAbortButton;
	public final JoystickButton liftResetButton;
	
	public final JoystickButton startHumanFeedButton;
	public final JoystickButton stopHumanFeedButton;
	
	public final JoystickButton toteElevatorUpManualButton;
	public final JoystickButton toteElevatorDownManualButton;
	
	public final JoystickButton lowerAndOpenClawButton;
	
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
	OI() {
		rightStick = new Joystick(RobotMap.rightStickID);
		leftStick = new Joystick(RobotMap.leftStickID);
		tiltStick = leftStick;
		
		//Right stick
		kachigLeft = new InternalButton();
		kachigRight = new InternalButton();
		kachigForward = new InternalButton();
		kachigBackward = new InternalButton();
		
		resetGyroButton = new JoystickButton(rightStick, RobotMap.resetGyroButtonID);
		resetDriveEncodersButton = new JoystickButton(rightStick, RobotMap.resetDriveEncodersButtonID);
		
		liftAbortButton = new JoystickButton(rightStick, RobotMap.liftAbortButtonID);
		liftResetButton = new JoystickButton(rightStick, RobotMap.liftResetButtonID);
		
		startHumanFeedButton = new JoystickButton(rightStick, RobotMap.startHumanFeedButtonID);
		stopHumanFeedButton = new JoystickButton(rightStick, RobotMap.stopHumanFeedButtonID);
		
		incrementNumberTotesButton = new JoystickButton(rightStick, RobotMap.incrementNumberTotesButtonID);
		
		toteElevatorUpManualButton = new JoystickButton(rightStick, RobotMap.toteElevatorUpManualButtonID);
		toteElevatorDownManualButton = new JoystickButton(rightStick, RobotMap.toteElevatorDownManualButtonID);

		lowerAndOpenClawButton = new JoystickButton(rightStick, RobotMap.lowerToteElevatorAndOpenClawButtonID);
		
		//Left stick
		binElevatorUp = new InternalButton();
		binElevatorDown = new InternalButton();
//		binTiltUpButton = new InternalButton();
//		binTiltDownButton = new InternalButton();
		
		closeClawButton = new JoystickButton(leftStick, RobotMap.closeClawButtonID);
		openClawButton = new JoystickButton(leftStick, RobotMap.openClawButtonID);

		rakerDownManualButton = new JoystickButton(leftStick, RobotMap.rakerDownManualButtonID);
		rakerUpManualButton = new JoystickButton(leftStick, RobotMap.rakerUpManualButtonID);
//		binTiltManualButton = new JoystickButton(leftStick, RobotMap.binTiltManualButtonID);
//		moveClawForBinPickupButton = new JoystickButton(leftStick, RobotMap.moveClawForBinPickupButtonID);
//		moveClawForNoodleInsertionButton = new JoystickButton(leftStick, RobotMap.moveClawForNoodleInsertionButtonID);
		
		
		conveyorForwardButton = new JoystickButton(leftStick, RobotMap.coneyorFowardButtonID);
		conveyorBackButton = new JoystickButton(leftStick, RobotMap.coneyorBackButtonID);
		
		guardRailOpenButton = new JoystickButton(leftStick, RobotMap.guardrailOpenManualButtonID);
		guardRailCloseButton = new JoystickButton(leftStick, RobotMap.guardrailCloseManualButtonID);
	}
	
	public void init() {
		/**
		 * whenPressed = function that when a button is pressed, it starts and doesn't stop when released
		 * whileHeld = function that only operates when a button is pressed and held, and stops when released
		 */
		kachigLeft.whenPressed(new KachigCommand(0, -1, 0.075));
		kachigRight.whenPressed(new KachigCommand(0, 1, 0.075));
		kachigForward.whenPressed(new KachigCommand(1, 0, 0.05));
		kachigBackward.whenPressed(new KachigCommand(-1, 0, 0.05));
		
		resetGyroButton.whenPressed(new ResetGyroCommand());
		resetDriveEncodersButton.whenPressed(new ResetDriveEncodersCommand());
		
		startHumanFeedButton.whenPressed(new RaiseSignalCommand(StateMachine.getInstance().humanFeed_Start));
		stopHumanFeedButton.whenPressed(new RaiseSignalCommand(StateMachine.getInstance().humanFeed_Stop));
		
		// unstickToteButton.whileHeld(new UnstickToteCommand());
		//unloadConveyorButton.whenPressed(new UnloadConveyorCommand());
		
		liftAbortButton.whenPressed(new RaiseSignalCommand(StateMachine.getInstance().abortSignal));
		liftResetButton.whenPressed(new RaiseSignalCommand(StateMachine.getInstance().resetSignal));
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
	
	private static OI oi = new OI();
	
	public static OI getInstance() {
		return oi;
	}
}

