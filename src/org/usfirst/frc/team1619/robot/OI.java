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
	
	
	/**
	 * Operation Pairing via Buttons
	 * pairs functions together that use the same button
	 */
	private final InternalButton kachigLeft, kachigRight, kachigForward, kachigBackward;
	public final InternalButton binElevatorUp, binElevatorDown;
//	public final InternalButton binTiltUpButton, binTiltDownButton;
	
	//rightStick
	private final JoystickButton resetGyroButton;
	private final JoystickButton resetDriveEncodersButton;
	private final JoystickButton liftAbortButton;
	private final JoystickButton liftResetButton;
	
	private final JoystickButton startHumanFeedButton;
	private final JoystickButton stopHumanFeedButton;
	
	//leftStick
	public final JoystickButton closeClawButton;
	public final JoystickButton openClawButton;
//	public final JoystickButton binTiltManualButton;
	public final JoystickButton rakerOpenManualButton;
	public final JoystickButton rakerCloseManualButton;
//	public final JoystickButton moveClawForBinPickupButton;
//	public final JoystickButton moveClawForNoodleInsertionButton;
	
	public final JoystickButton toteElevatorManualButton;
		
	public final JoystickButton conveyorForwardButton;
	public final JoystickButton conveyorBackButton;
	
	public final JoystickButton guardRailOpenButton;
	public final JoystickButton guardRailCloseButton;
	
	public final JoystickButton pickUpToteButton;
	
	public final JoystickButton incrementNumberTotesButton;
	OI() {
		rightStick = new Joystick(RobotMap.rightStickID);
		leftStick = new Joystick(RobotMap.leftStickID);
		
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
		pickUpToteButton = new JoystickButton(rightStick, RobotMap.pickUpToteButtonID);
		
		//Left stick
		binElevatorUp = new InternalButton();
		binElevatorDown = new InternalButton();
//		binTiltUpButton = new InternalButton();
//		binTiltDownButton = new InternalButton();
		
		closeClawButton = new JoystickButton(leftStick, RobotMap.closeClawButtonID);
		openClawButton = new JoystickButton(leftStick, RobotMap.openClawButtonID);
		rakerOpenManualButton = new JoystickButton(leftStick, RobotMap.rakerOpenManualButtonID);
		rakerCloseManualButton = new JoystickButton(leftStick, RobotMap.rakerCloseManualButtonID);
//		binTiltManualButton = new JoystickButton(leftStick, RobotMap.binTiltManualButtonID);
//		moveClawForBinPickupButton = new JoystickButton(leftStick, RobotMap.moveClawForBinPickupButtonID);
//		moveClawForNoodleInsertionButton = new JoystickButton(leftStick, RobotMap.moveClawForNoodleInsertionButtonID);
		
		toteElevatorManualButton = new JoystickButton(leftStick, RobotMap.toteElevatorManualButtonID);
		
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
		pickUpToteButton.whenPressed(new RaiseSignalCommand(StateMachine.getInstance().humanFeed_EndCurrentStateAndDescend));
		
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

