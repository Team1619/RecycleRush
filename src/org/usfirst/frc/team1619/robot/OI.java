package org.usfirst.frc.team1619.robot;

import org.usfirst.frc.team1619.robot.commands.KachigCommand;
import org.usfirst.frc.team1619.robot.commands.LinearDriveCommand;
import org.usfirst.frc.team1619.robot.commands.ManualConveyorCommand;
import org.usfirst.frc.team1619.robot.commands.ManualGuardRailCommand;
import org.usfirst.frc.team1619.robot.commands.RaiseSignalCommand;
import org.usfirst.frc.team1619.robot.commands.ResetEncoderCommand;
import org.usfirst.frc.team1619.robot.commands.ResetGyroCommand;
import org.usfirst.frc.team1619.robot.commands.TurnCommand;
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
	
	private final JoystickButton kachigLeft, kachigRight;
	private final JoystickButton resetGyroButton;
	private final JoystickButton conveyorForwardManualButton, conveyorBackwardManualButton;
	private final JoystickButton guardrailCloseManualButton, guardrailOpenManualButton;
	private final JoystickButton resetEncoderButton;
	private final JoystickButton driveForwardButton;
	private final JoystickButton turnRightButton;
	private final JoystickButton liftAbortButton;
	private final JoystickButton liftResetButton;
		
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
		
		//Left stick
		conveyorForwardManualButton = new JoystickButton(leftStick, RobotMap.conveyorForwardManualButtonID);
		conveyorBackwardManualButton = new JoystickButton(leftStick, RobotMap.conveyorBackwardManualButtonID);
		guardrailCloseManualButton = new JoystickButton(leftStick, RobotMap.guardrailCloseManualButtonID);
		guardrailOpenManualButton = new JoystickButton(leftStick, RobotMap.guardrailOpenManualButtonID);
		liftAbortButton = new JoystickButton(leftStick, RobotMap.liftAbortButtonID);
		liftResetButton = new JoystickButton(leftStick, RobotMap.liftResetButtonID);
	}
	
	public void init() {
		kachigLeft.whenPressed(new KachigCommand.KachigLeftCommand());
		kachigRight.whenPressed(new KachigCommand.KachigRightCommand());
		resetGyroButton.whenPressed(new ResetGyroCommand());
		conveyorForwardManualButton.whileHeld(new ManualConveyorCommand(1.0));
		conveyorBackwardManualButton.whileHeld(new ManualConveyorCommand(-1.0));
		guardrailCloseManualButton.whileHeld(new ManualGuardRailCommand(-0.1));
		guardrailOpenManualButton.whileHeld(new ManualGuardRailCommand(0.1));
		resetEncoderButton.whenPressed(new ResetEncoderCommand());
		driveForwardButton.whenPressed(new LinearDriveCommand(LinearDriveCommand.kMoveForwardDistance));
		turnRightButton.whenPressed(new TurnCommand(TurnCommand.kTurnAngle));
		
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

