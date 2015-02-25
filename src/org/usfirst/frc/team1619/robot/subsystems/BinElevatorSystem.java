package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.GenericLine;
import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.StateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class BinElevatorSystem extends StateMachineSystem {
    private static final double kEncoderTicksPerInch = (2834.0 + 33.0)/-32.875;
    public static final double kBinElevatorMaxHeight = 33.0; //very opproximate. Measured from botton limit switch
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	public final CANTalon binElevatorMotor;
	public final CANTalon tilterMotor;
	public final CANTalon binGripMotor;
	public final CANTalon rakerMotor;
	
	//private final Joystick rightStick;
	private final Joystick leftStick;
	
	private final JoystickButton binElevatorUpManualButton, binElevatorDownManualButton;
	private final JoystickButton binTiltUpManualButton, binTiltDownManualButton;
	private final JoystickButton binGripOpenManualButton, binGripCloseManualButton;
	private final JoystickButton rakerOpenManualButton, rakerCloseManualButton;
	
	private double binElevatorSpeed = 0.0;
	private boolean usePosition = false;
	private GenericLine speedCurve = new GenericLine();
	
	private double binGripSpeed = 0.0;
	private double rakerSpeed = 0.0;
	private double tilterMotorSpeed = 0.0;
	
	private BinElevatorSystem() {
		//rightStick = OI.getInstance().rightStick;
		leftStick = OI.getInstance().leftStick;
		
		//left stick
		binElevatorDownManualButton = new JoystickButton(leftStick, RobotMap.binElevatorDownManualButtonID);
		binElevatorUpManualButton = new JoystickButton(leftStick, RobotMap.binElevatorUpManualButtonID);
		binTiltUpManualButton = new JoystickButton(leftStick, RobotMap.binTiltUpManualButtonID);
		binTiltDownManualButton = new JoystickButton(leftStick, RobotMap.binTiltDownManualButtonID);
		binGripOpenManualButton = new JoystickButton(leftStick, RobotMap.binGripOpenManualButtonID);
		binGripCloseManualButton = new JoystickButton(leftStick, RobotMap.binGripCloseManualButtonID);
		rakerOpenManualButton = new JoystickButton(leftStick, RobotMap.rakerOpenManualButtonID);
		rakerCloseManualButton = new JoystickButton(leftStick, RobotMap.rakerCloseManualButtonID);
		
		binElevatorMotor = new CANTalon(RobotMap.binElevatorMotor);
    	binElevatorMotor.enableLimitSwitch(true, true);
    	binElevatorMotor.enableBrakeMode(true);
    	
    	tilterMotor = new CANTalon(RobotMap.tilterMotor);
    	tilterMotor.enableLimitSwitch(true, true);
    	tilterMotor.enableBrakeMode(false);
    	tilterMotor.ConfigFwdLimitSwitchNormallyOpen(true);
    	tilterMotor.ConfigRevLimitSwitchNormallyOpen(true);
    	
    	binGripMotor = new CANTalon(RobotMap.binGripMotor);
    	binGripMotor.enableLimitSwitch(false, false);
    	binGripMotor.enableBrakeMode(false);
    	
    	rakerMotor = new CANTalon(RobotMap.rakerMotor);
    	rakerMotor.enableLimitSwitch(true, true);
    	rakerMotor.enableBrakeMode(true);
    	rakerMotor.ConfigFwdLimitSwitchNormallyOpen(true);
    	rakerMotor.ConfigRevLimitSwitchNormallyOpen(true);
	}
	
	private static BinElevatorSystem theSystem;
	
	public static BinElevatorSystem getInstance() {
		if(theSystem == null)
			theSystem = new BinElevatorSystem();
		return theSystem;
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    	//setDefaultCommand(new BinLiftSystemStateMachineCommand());
    }
    //manual commands
    
    public void setBinElevatorSpeed(double speed) {
    	binElevatorSpeed = speed;
    	usePosition = false;
    }
    public void setBinElevatorPosition(double position) {  //in inches
    	/*usePosition = true;
    	
    	speedCurve = new GenericLine(
    			getBinElevatorPosition(), 0.1,
    			getBinElevatorPosition() + (position - getBinElevatorPosition())/3, 0.5,
    			getBinElevatorPosition() + 2*(position - getBinElevatorPosition())/3, 0.5,
    			position, 0.0
    			);
    			*/
    }
    
    private void setBinElevatorPositionValue(double position) { //set position in inches, not move motor. Only use for calibration
    	binElevatorMotor.setPosition(position*kEncoderTicksPerInch);
    }
    public double getBinElevatorPosition() { //get current position in inches
    	return binElevatorMotor.getPosition()/kEncoderTicksPerInch;
    }
    
    private void binElevatorUpdate() {
    	if(binElevatorDownManualButton.get()) {
    		binElevatorMotor.set(0.5);
    		usePosition = false;
    	}
    	else if(binElevatorUpManualButton.get()) {
    		binElevatorMotor.set(-0.5);    		
    		usePosition = false;
    	}
    	else if(usePosition) {
    		binElevatorMotor.set(speedCurve.getValue(getBinElevatorPosition()));
    	}
    	else {
    		binElevatorMotor.set(binElevatorSpeed);
    	}
    }
    
    public void binTilt(double moveValue) {
    	tilterMotorSpeed = moveValue;
    }
    private void binTiltUpdate() {
    	if(binTiltUpManualButton.get())
        	tilterMotor.set(0.75);
    	else if(binTiltDownManualButton.get())
        	tilterMotor.set(-0.75);
    	else
    		tilterMotor.set(tilterMotorSpeed);
    }
    
    public void moveBinGrip(double moveValue) {
    	binGripSpeed = moveValue;
    }
    private void binGripUpdate() {
    	if(binGripOpenManualButton.get())
        	binGripMotor.set(0.1);
    	else if(binGripCloseManualButton.get())
    		binGripMotor.set(-0.1);
    	else
    		binGripMotor.set(binGripSpeed);
    }
    
    public void moveRaker(double moveValue) {
    	rakerSpeed = moveValue;
    }
    private void rakerUpdate() {
    	if(rakerOpenManualButton.get())
    		rakerMotor.set(0.1);
    	else if(rakerCloseManualButton.get())
    		rakerMotor.set(-0.1);
    	else
    		rakerMotor.set(rakerSpeed);
    	
    }

	@Override
	public void run(State state, double elapsed) {
		switch(state) {
		case Init:
			//should be top limit switch
			if(!binElevatorMotor.isRevLimitSwitchClosed()) { 
				setBinElevatorSpeed(-0.2);    				
			}
			else {
				setBinElevatorPositionValue(0.0);	
				setBinElevatorSpeed(0.0);
			}
			break;
		case Idle:
			break;
		case HumanFeed_RaiseTote:
			setBinElevatorPosition(0.0); //just move it to top for now
			break;
		case HumanFeed_WaitForTote:
			setBinElevatorPosition(0.0); //just move it to top for now
			break;
		case HumanFeed_ToteOnConveyor:
			setBinElevatorPosition(0.0); //just move it to top for now
			break;
		case HumanFeed_ThrottleConveyorDescend:
			setBinElevatorPosition(0.0); //just move it to top for now
			break;
		case GroundFeed:
			break;
		case Dropoff:
			break;
		case BinPickup:
			break;
		case Abort:	
			setBinElevatorSpeed(0.0);
			usePosition = false;
			speedCurve = new GenericLine();
			
			binGripSpeed = 0.0;
			rakerSpeed = 0.0;
			tilterMotorSpeed = 0.0;
			break;
		default:
			break;
		}
		
		binElevatorUpdate();
		binGripUpdate();
		binTiltUpdate();
		rakerUpdate();
		SmartDashboard.putBoolean("binElevatorFwd", binElevatorMotor.isFwdLimitSwitchClosed());
    	SmartDashboard.putBoolean("binElevatorRev", binElevatorMotor.isRevLimitSwitchClosed());
    	SmartDashboard.putBoolean("binTiltFwd", tilterMotor.isFwdLimitSwitchClosed());
    	SmartDashboard.putBoolean("binTiltRev", tilterMotor.isRevLimitSwitchClosed());
    	SmartDashboard.putBoolean("rakerFwd", rakerMotor.isFwdLimitSwitchClosed());
    	SmartDashboard.putBoolean("rakerRev", rakerMotor.isRevLimitSwitchClosed());
	}
	
	public boolean initFinished() {
		return getBinElevatorPosition() == 0.0;
	}
}
