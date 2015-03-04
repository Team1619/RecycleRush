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
	
	private final JoystickButton resetGyroButton;
	private final JoystickButton resetDriveEncodersButton;
	private final JoystickButton liftAbortButton;
	private final JoystickButton liftResetButton;
	
	public final JoystickButton closeClawButton;
	public final JoystickButton openClawButton;
	
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
		
		binElevatorUp = new InternalButton();
		binElevatorDown = new InternalButton();
		
		resetGyroButton = new JoystickButton(rightStick, RobotMap.resetGyroButtonID);
		resetDriveEncodersButton = new JoystickButton(rightStick, RobotMap.resetDriveEncodersButtonID);
		
		liftAbortButton = new JoystickButton(rightStick, RobotMap.liftAbortButtonID);
		liftResetButton = new JoystickButton(rightStick, RobotMap.liftResetButtonID);
		
		startHumanFeedButton = new JoystickButton(rightStick, RobotMap.startHumanFeedButtonID);
		stopHumanFeedButton = new JoystickButton(rightStick, RobotMap.stopHumanFeedButtonID);
		
		//unloadConveyorButton = new JoystickButton(rightStick, RobotMap.unloadConveyorButtonID);
		
		//Left stick
		closeClawButton = new JoystickButton(leftStick, RobotMap.closeClawButtonID);
		openClawButton = new JoystickButton(leftStick, RobotMap.openClawButtonID);
		
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
		
		// unstickToteButton.whileHeld(new UnstickToteCommand());
		//unloadConveyorButton.whenPressed(new UnloadConveyorCommand());
		
		liftAbortButton.whenPressed(new RaiseSignalCommand(StateMachine.getInstance().abortSignal));
		liftResetButton.whenPressed(new RaiseSignalCommand(StateMachine.getInstance().resetSignal));
	}
	
	public void updateKachig() {
		int povLeft = rightStick.getPOV();
		kachigRight.setPressed(povLeft == 90);
		kachigForward.setPressed(povLeft == 0);
		kachigLeft.setPressed(povLeft == 270);
		kachigBackward.setPressed(povLeft == 180);
		
		int povRight = leftStick.getPOV();
		binElevatorUp.setPressed(povRight == 0);
		binElevatorDown.setPressed(povRight == 180);
	}
	
	private static OI oi = new OI();
	
	public static OI getInstance() {
		return oi;
	}
}

