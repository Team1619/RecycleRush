package org.usfirst.frc.team1619.robot;

import org.usfirst.frc.team1619.robot.commands.KachigCommand;
import org.usfirst.frc.team1619.robot.commands.LinearDriveCommand;
import org.usfirst.frc.team1619.robot.commands.ManualConveyorCommand;
import org.usfirst.frc.team1619.robot.commands.ManualGuardRailCommand;
import org.usfirst.frc.team1619.robot.commands.RaiseSignalCommand;
import org.usfirst.frc.team1619.robot.commands.ResetEncoderCommand;
import org.usfirst.frc.team1619.robot.commands.ResetGyroCommand;
import org.usfirst.frc.team1619.robot.commands.TurnCommand;
import org.usfirst.frc.team1619.robot.commands.UnloadConveyorCommand;
import org.usfirst.frc.team1619.robot.commands.UnstickToteCommand;
import org.usfirst.frc.team1619.robot.subsystems.ToteLiftSystem;

import edu.wpi.first.wpilibj.Joystick;
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
	private final JoystickButton kachigLeft, kachigRight;
	private final JoystickButton resetGyroButton;
	private final JoystickButton resetEncoderButton;
	private final JoystickButton driveForwardButton;
	private final JoystickButton turnRightButton;
	private final JoystickButton liftAbortButton;
	private final JoystickButton liftResetButton;
	private final JoystickButton conveyorForwardButton;
	private final JoystickButton conveyorBackwardButton;
	private final JoystickButton guardRailOpenButton;
	private final JoystickButton guardRailCloseButton;
	private final JoystickButton unstickToteButton;
	private final JoystickButton unloadConveyorButton;
		
	OI() {
		rightStick = new Joystick(RobotMap.rightStickID);
		leftStick = new Joystick(RobotMap.leftStickID);

		//Right stick
		kachigLeft = new JoystickButton(rightStick, RobotMap.kachigLeftButtonID);
		kachigRight = new JoystickButton(rightStick, RobotMap.kachigRightButtonID);
		resetGyroButton = new JoystickButton(rightStick, RobotMap.resetGyroButtonID);
		resetEncoderButton = new JoystickButton(rightStick, RobotMap.resetEncoderButtonID);
		driveForwardButton = new JoystickButton(rightStick, RobotMap.driveForwardButtonID);
		turnRightButton = new JoystickButton(rightStick, RobotMap.turnButtonID);
		unloadConveyorButton = new JoystickButton(rightStick, RobotMap.unloadConveyorButtonID);
		
		//Left stick
		liftAbortButton = new JoystickButton(leftStick, RobotMap.liftAbortButtonID);
		liftResetButton = new JoystickButton(leftStick, RobotMap.liftResetButtonID);
		conveyorForwardButton = new JoystickButton(leftStick, RobotMap.conveyorForwardManualButtonID);
		conveyorBackwardButton = new JoystickButton(leftStick, RobotMap.conveyorBackwardManualButtonID);
		guardRailOpenButton = new JoystickButton(leftStick, RobotMap.guardrailOpenManualButtonID);
		guardRailCloseButton = new JoystickButton(leftStick, RobotMap.guardrailCloseManualButtonID);
		unstickToteButton = new JoystickButton(leftStick, RobotMap.unstickToteButtonID);
	}
	
	public void init() {
		/**
		 * whenPressed = function that when a button is pressed, it starts and doesn't stop when released
		 * whileHeld = function that only operates when a button is pressed and held, and stops when released
		 */
		kachigLeft.whenPressed(new KachigCommand.KachigLeftCommand());
		kachigRight.whenPressed(new KachigCommand.KachigRightCommand());
		resetGyroButton.whenPressed(new ResetGyroCommand());
		resetEncoderButton.whenPressed(new ResetEncoderCommand());
		driveForwardButton.whenPressed(new LinearDriveCommand(LinearDriveCommand.kMoveForwardDistance));
		turnRightButton.whenPressed(new TurnCommand(TurnCommand.kTurnAngle));
		conveyorForwardButton.whileHeld(new ManualConveyorCommand(1.0));
		conveyorBackwardButton.whileHeld(new ManualConveyorCommand(-1.0));
		guardRailOpenButton.whileHeld(new ManualGuardRailCommand(0.15));
		guardRailCloseButton.whileHeld(new ManualGuardRailCommand(-0.25));
		unstickToteButton.whileHeld(new UnstickToteCommand());
		unloadConveyorButton.whenPressed(new UnloadConveyorCommand());
		
		liftAbortButton.whenPressed(new RaiseSignalCommand(ToteLiftSystem.getInstance().abortSignal));
		liftResetButton.whenPressed(new RaiseSignalCommand(ToteLiftSystem.getInstance().resetSignal));
	}
	
	private static OI oi = new OI();
	
	public static OI getInstance() {
		return oi;
	}
	
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);
    
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
}

