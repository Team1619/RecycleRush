package org.usfirst.frc.team1619.robot;

import org.usfirst.frc.team1619.robot.commands.KachigCommand;
import org.usfirst.frc.team1619.robot.commands.ManualGuardRailCommand;
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
	private final JoystickButton resetGyroButton;
	private final JoystickButton resetDriveEncodersButton;
	private final JoystickButton liftAbortButton;
	private final JoystickButton liftResetButton;
	private final JoystickButton guardRailOpenButton;
	private final JoystickButton guardRailCloseButton;
	// private final JoystickButton unstickToteButton;
	//private final JoystickButton unloadConveyorButton;
	private final JoystickButton startHumanFeedButton;
	private final JoystickButton stopHumanFeedButton;
		
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
		
		//unloadConveyorButton = new JoystickButton(rightStick, RobotMap.unloadConveyorButtonID);
		
		//Left stick
		guardRailOpenButton = new JoystickButton(leftStick, RobotMap.guardrailOpenManualButtonID);
		guardRailCloseButton = new JoystickButton(leftStick, RobotMap.guardrailCloseManualButtonID);
		
		// unstickToteButton = new JoystickButton(leftStick, RobotMap.unstickToteButtonID);
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
		
		startHumanFeedButton.whenPressed(new RaiseSignalCommand(StateMachine.getInstance().humanPlayerFeed_Start));
		stopHumanFeedButton.whenPressed(new RaiseSignalCommand(StateMachine.getInstance().humanPlayerFeed_Stop));
		
		guardRailOpenButton.whileHeld(new ManualGuardRailCommand(0.15));
		guardRailCloseButton.whileHeld(new ManualGuardRailCommand(-0.25));
		
		// unstickToteButton.whileHeld(new UnstickToteCommand());
		//unloadConveyorButton.whenPressed(new UnloadConveyorCommand());
		
		liftAbortButton.whenPressed(new RaiseSignalCommand(StateMachine.getInstance().abortSignal));
		liftResetButton.whenPressed(new RaiseSignalCommand(StateMachine.getInstance().resetSignal));
	}
	
	public void updateKachig() {
		int pov = rightStick.getPOV();
		kachigRight.setPressed(pov == 90);
		kachigForward.setPressed(pov == 0);
		kachigLeft.setPressed(pov == 270);
		kachigBackward.setPressed(pov == 180);
	}
	
	private static OI oi = new OI();
	
	public static OI getInstance() {
		return oi;
	}
}

