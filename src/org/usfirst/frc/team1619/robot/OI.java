package org.usfirst.frc.team1619.robot;

//github.com/Team1619/RecycleRush.git
//github.com/Team1619/RecycleRush.git
import org.usfirst.frc.team1619.robot.commands.KachigCommand;
import org.usfirst.frc.team1619.robot.commands.LinearDriveCommand;
import org.usfirst.frc.team1619.robot.commands.ManualBinElevatorCommand;
import org.usfirst.frc.team1619.robot.commands.ManualBinGripCommand;
import org.usfirst.frc.team1619.robot.commands.ManualBinTiltCommand;
import org.usfirst.frc.team1619.robot.commands.ManualConveyorCommand;
import org.usfirst.frc.team1619.robot.commands.ManualGuardRailCommand;
import org.usfirst.frc.team1619.robot.commands.ManualToteElevatorCommand;
import org.usfirst.frc.team1619.robot.commands.RaiseSignalCommand;
import org.usfirst.frc.team1619.robot.commands.ResetEncoderCommand;
import org.usfirst.frc.team1619.robot.commands.ResetGyroCommand;
import org.usfirst.frc.team1619.robot.commands.TurnCommand;
import org.usfirst.frc.team1619.robot.subsystems.LiftSystem;

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
	private final JoystickButton toteElevatorDownManualButton, toteElevatorUpManualButton;
	private final JoystickButton binElevatorUpManualButton, binElevatorDownManualButton;
	private final JoystickButton binTiltUpManualButton, binTiltDownManualButton;
	private final JoystickButton binGripOpenManualButton, binGripCloseManualButton;
	private final JoystickButton resetEncoderButton;
	private final JoystickButton driveForwardButton;
	private final JoystickButton turnRightButton;
	private final JoystickButton liftAbortButton;
	private final JoystickButton liftResetButton;
	
	private OI() {
		rightStick = new Joystick(RobotMap.rightStickID);
		leftStick = new Joystick(RobotMap.leftStickID);
		
		//Right stick
		kachigLeft = new JoystickButton(rightStick, RobotMap.kachigLeftButtonID);
		kachigRight = new JoystickButton(rightStick, RobotMap.kachigRightButtonID);
		resetGyroButton = new JoystickButton(rightStick, RobotMap.resetGyroButtonID);
		toteElevatorUpManualButton = new JoystickButton(rightStick, RobotMap.toteElevatorUpManualButtonID);
		toteElevatorDownManualButton = new JoystickButton(rightStick, RobotMap.toteElevatorDownManualButtonID);
		resetEncoderButton = new JoystickButton(rightStick, RobotMap.resetEncoderButtonID);
		driveForwardButton = new JoystickButton(rightStick, RobotMap.driveForwardButtonID);
		turnRightButton = new JoystickButton(rightStick, RobotMap.turnRightButtonID);
		
		
		//Left stick
		conveyorForwardManualButton = new JoystickButton(leftStick, RobotMap.conveyorForwardManualButtonID);
		conveyorBackwardManualButton = new JoystickButton(leftStick, RobotMap.conveyorBackwardManualButtonID);
		guardrailCloseManualButton = new JoystickButton(leftStick, RobotMap.guardrailCloseManualButtonID);
		guardrailOpenManualButton = new JoystickButton(leftStick, RobotMap.guardrailOpenManualButtonID);
		binElevatorUpManualButton = new JoystickButton(leftStick, RobotMap.binElevatorUpManualButtonID);
		binElevatorDownManualButton = new JoystickButton(leftStick, RobotMap.binElevatorDownManualButtonID);
		binTiltUpManualButton = new JoystickButton(leftStick, RobotMap.binTiltUpManualButtonID);
		binTiltDownManualButton = new JoystickButton(leftStick, RobotMap.binTiltDownManualButtonID);
		binGripOpenManualButton = new JoystickButton(leftStick, RobotMap.binGripOpenManualButtonID);
		binGripCloseManualButton = new JoystickButton(leftStick, RobotMap.binGripCloseManualButtonID);
		liftAbortButton = new JoystickButton(leftStick, RobotMap.liftAbortButtonID);
		liftResetButton = new JoystickButton(leftStick, RobotMap.liftResetButtonID);
		
		kachigLeft.whenPressed(new KachigCommand.KachigLeftCommand());
		kachigRight.whenPressed(new KachigCommand.KachigRightCommand());
		resetGyroButton.whenPressed(new ResetGyroCommand());
		conveyorForwardManualButton.whileHeld(new ManualConveyorCommand(1.0));
		conveyorBackwardManualButton.whileHeld(new ManualConveyorCommand(-1.0));
		guardrailCloseManualButton.whileHeld(new ManualGuardRailCommand(-1.0));
		guardrailOpenManualButton.whileHeld(new ManualGuardRailCommand(1.0));
		toteElevatorDownManualButton.whileHeld(new ManualToteElevatorCommand(-1.0));
		toteElevatorUpManualButton.whileHeld(new ManualToteElevatorCommand(1.0));
		binElevatorUpManualButton.whileHeld(new ManualBinElevatorCommand(1.0));
		binElevatorDownManualButton.whileHeld(new ManualBinElevatorCommand(-1.0));
		binTiltUpManualButton.whileHeld(new ManualBinTiltCommand(1.0));
		binTiltDownManualButton.whileHeld(new ManualBinTiltCommand(-1.0));
		binGripOpenManualButton.whileHeld(new ManualBinGripCommand(1.0));
		binGripCloseManualButton.whileHeld(new ManualBinGripCommand(-1.0));
		resetEncoderButton.whenPressed(new ResetEncoderCommand());
		driveForwardButton.whenPressed(new LinearDriveCommand(LinearDriveCommand.kMoveForwardDistance));
		turnRightButton.whenPressed(new TurnCommand(TurnCommand.kTurnAngle));
		turnRightButton.whenPressed(new TurnCommand(TurnCommand.kTurnRightAngle));
		
		liftAbortButton.whenPressed(new RaiseSignalCommand(LiftSystem.getInstance().abortSignal));
		liftResetButton.whenPressed(new RaiseSignalCommand(LiftSystem.getInstance().resetSignal));
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

