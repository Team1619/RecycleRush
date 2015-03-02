package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.Preferences;
import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.StateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class BinElevatorSystem extends StateMachineSystem {
	public static final double kEncoderTicksPerInch = 173.63;
	public static final double kTransitPosition = 3.0;
	public static final double kFeederPosition = 29.2;
	public static final double kPickUpPosition = 0.0;
	public static final double kPositionTolerance = 2.0;
	public static final double kInitSpeed = -0.2;
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	public final CANTalon binElevatorMotor;
	public final CANTalon tilterMotor;
	public final CANTalon binGripMotor;
	public final CANTalon rakerMotor;
	
	//private final Joystick rightStick;
	private Joystick leftStick;
	
	private JoystickButton binElevatorManualButton;
	private JoystickButton binTiltManualButton;
	private JoystickButton binGripManualButton;
	
	private JoystickButton rakerOpenManualButton;
	private JoystickButton rakerCloseManualButton;
	
	private double binElevatorSpeed; // will be %vbus 
	private boolean usePosition;
	private double moveTo;
	
	private double binGripSpeed = 0.0;
	private double rakerSpeed = 0.0;
	private double tilterMotorSpeed = 0.0;
	
	private boolean bInitFinished = false;
	
	private BinElevatorSystem() {
		binElevatorMotor = new CANTalon(RobotMap.toteElevatorMotor);
		binElevatorMotor.enableLimitSwitch(true, true);
		binElevatorMotor.enableBrakeMode(true);
		binElevatorMotor.reverseSensor(false);
		binElevatorMotor.reverseOutput(false);
		binElevatorMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		binElevatorMotor.setPID(
				Preferences.getNumber("binP", 0.55),
				Preferences.getNumber("binI", 0.00001),
				Preferences.getNumber("binD", 0),
				0.0001,
				500,
				24/0.250,
				0
				);
    	
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
	
	public void init() {
		//rightStick = OI.getInstance().rightStick;
		leftStick = OI.getInstance().leftStick;
		
		//left stick
		binElevatorManualButton = new JoystickButton(leftStick, RobotMap.binElevatorManualButtonID);
		binTiltManualButton = new JoystickButton(leftStick, RobotMap.binTiltManualButtonID);
		binGripManualButton = new JoystickButton(leftStick, RobotMap.binGripManualButtonID);
		
		rakerOpenManualButton = new JoystickButton(leftStick, RobotMap.rakerOpenManualButtonID);
		rakerCloseManualButton = new JoystickButton(leftStick, RobotMap.rakerCloseManualButtonID);
	}
	
	private final static BinElevatorSystem theSystem = new BinElevatorSystem();
	
	public static BinElevatorSystem getInstance() {
		return theSystem;
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    	//setDefaultCommand(new BinLiftSystemStateMachineCommand());
    }
    
    public void setBinElevatorSpeed(double speed) {
    	binElevatorSpeed = speed;
    	usePosition = false;
    }
    
    public void setBinElevatorPosition(double position) {  //in inches
    	moveTo = position;
    	usePosition = true;
    }
    
    private void setBinElevatorPositionValue(double position) { //set position in inches, not move motor. Only use for calibration
    	binElevatorMotor.setPosition(position*kEncoderTicksPerInch);
    }
    
    public double getBinElevatorPosition() { //get current position in inches
    	return binElevatorMotor.getPosition()/kEncoderTicksPerInch;
    }
    
    private void binElevatorUpdate() {

		double joystickY = leftStick.getY();
		if(binElevatorManualButton.get()) {
			binElevatorMotor.changeControlMode(ControlMode.PercentVbus);
			binElevatorMotor.set(joystickY);
			usePosition = false;
			moveTo = Double.NaN;
			binElevatorSpeed = 0.0;
		}
		else {
			if(usePosition) {
				if(Double.isNaN(moveTo)) {
					binElevatorMotor.changeControlMode(ControlMode.PercentVbus);
					binElevatorMotor.set(0);
				}
				else {
					binElevatorMotor.changeControlMode(ControlMode.Position);
					binElevatorMotor.set(moveTo*kEncoderTicksPerInch);
				}
			}
			else {
				binElevatorMotor.changeControlMode(ControlMode.PercentVbus);
				binElevatorMotor.set(binElevatorSpeed);
			}
		}

		SmartDashboard.putNumber("binElevatorMotor.getEncPosition()",binElevatorMotor.getEncPosition());
		SmartDashboard.putNumber("binElevatorMotor.getOutputVoltage()",binElevatorMotor.getOutputVoltage());
		SmartDashboard.putNumber("binElevatorMotor.get()",binElevatorMotor.get());
    }
    
    public void binTilt(double moveValue) {
    	tilterMotorSpeed = moveValue;
    }
    
    private void binTiltUpdate() {
    	if(binTiltManualButton.get())
        	tilterMotor.set(leftStick.getY());
    	else
    		tilterMotor.set(tilterMotorSpeed);
    }
    
    public void moveBinGrip(double moveValue) {
    	binGripSpeed = moveValue;
    }
    
    private void binGripUpdate() {
    	if(binGripManualButton.get())
        	binGripMotor.set(leftStick.getY());
    	else
    		binGripMotor.set(binGripSpeed);
    }
    
    public void moveRaker(double moveValue) {
    	rakerSpeed = moveValue;
    }
    
    private void rakerUpdate() {
    	if(rakerOpenManualButton.get())
    		rakerMotor.set(0.4);
    	else if(rakerCloseManualButton.get())
    		rakerMotor.set(-0.4);
    	else
    		rakerMotor.set(rakerSpeed);
    }

    public void init(State state) {
		usePosition = false;
		binElevatorSpeed = 0;
		moveTo = Double.NaN;
		
		switch(state) {
		case Init:
			bInitFinished = false;
			break;
		default:
			break;
		}
	}
    
	@Override
	public void run(State state, double elapsed) {

		switch(state) {
		case Init:
			//should be top limit switch
			if(binElevatorMotor.isRevLimitSwitchClosed()) {
				setBinElevatorPositionValue(0.0);
				setBinElevatorSpeed(0.0);
				bInitFinished = true;
			}
			else {
				setBinElevatorSpeed(kInitSpeed);
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
		case HumanFeed_ThrottleConveyorAndDescend:
			setBinElevatorPosition(0.0); //just move it to top for now
			break;
		case GroundFeed:
			break;
		case Dropoff:
			break;
		case BinPickup:
			break;
		case Abort:
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
		return bInitFinished;
	}
}

