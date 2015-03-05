package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.Preferences;
import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.StateMachine.State;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.InternalButton;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

/**
 *
 */
public class BinElevatorSystem extends StateMachineSystem {
	public static final double kEncoderTicksPerInch = 2043 / 32.0;
	public static final double kOutOfTheWayPosition = -2.0;
	public static final double kTransitPosition = 0.0;
	public static final double kFeederPosition = 0.0;
	public static final double kPickUpPosition = 0.0;
	public static final double kPositionTolerance = 1.0;
	public static final double kInitSpeed = -0.2;
	public static final double kBinElevatorUpSpeed = -0.4;
	public static final double kBinElevatorDownSpeed = 0.4;
	
	public static final double kTotalHeight = 62.0; //fish
	public static final double kToteElevatorHeight = 25.0; //fish
	public static final double kBinElevatorHeight = 37.0; //fish
	public static final double kToteElevatorHeightModifier = 10.0; //fish, accounts for the plastic fins on elevator being above the "toteElevatorPosition" 
	public static final double kBinElevatorHeightModifier = -6.0; //fish, accounts for bottom of bin gripper being below the "binElevatorPosition"
	public static final double kDistanceBetweenLifts = 45.0; //catfinches
	public static final double kSafetyTolerance = 1.0;
	
	public static final double kToteElevatorSafetyForTilt = 5.5; //fish
	
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	public final CANTalon binElevatorMotor;
	public final CANTalon tilterMotor;
	public final CANTalon binGripMotor;
	public final CANTalon rakerMotor;
	
	//private final Joystick rightStick;
	private Joystick leftStick;
	
	private InternalButton binElevatorUp;
	private InternalButton binElevatorDown;
	
	private JoystickButton binTiltManualButton;
	
	private JoystickButton rakerOpenManualButton;
	private JoystickButton rakerCloseManualButton;
	
	private JoystickButton openClawButton;
	private JoystickButton closeClawButton;
	
	
	private double binElevatorSpeed; // will be %vbus 
	private boolean usePosition;
	private double moveTo;
	
	private double binGripSpeed = 0.0;
	private double rakerSpeed = 0.0;
	private double tilterMotorSpeed = 0.0;
	
	private boolean bInitFinished = false;
	
	private boolean ableToTilt = true;
	
	private BinElevatorSystem() {
		binElevatorMotor = new CANTalon(RobotMap.binElevatorMotor);
		binElevatorMotor.enableLimitSwitch(true, true);
		binElevatorMotor.enableBrakeMode(true);
		binElevatorMotor.reverseSensor(true);
		binElevatorMotor.reverseOutput(true);
		binElevatorMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	
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
		
		//internal buttons
		binElevatorUp = OI.getInstance().binElevatorUp;
		binElevatorDown = OI.getInstance().binElevatorDown;
		
		//left stick
		binTiltManualButton = new JoystickButton(leftStick, RobotMap.binTiltManualButtonID);
		
		rakerOpenManualButton = new JoystickButton(leftStick, RobotMap.rakerOpenManualButtonID);
		rakerCloseManualButton = new JoystickButton(leftStick, RobotMap.rakerCloseManualButtonID);
		
		openClawButton = OI.getInstance().openClawButton;
		closeClawButton = OI.getInstance().closeClawButton;
		
		binElevatorMotor.setPID(2.5, 0.00001, 0, 0.0001, 500, 24/0.250, 0);
    	
		Preferences.addTableListener(new ITableListener() {
			@Override
			public void valueChanged(ITable source, String key, Object value,
					boolean isNew) {
				System.out.println(String.format("Key '%s' changed to '%s' (new = '%s')",
						key, value.toString(), Boolean.toString(isNew)));
				
				switch(key) {
				case "binP":
					binElevatorMotor.setP(Double.parseDouble((String)value));
					break;
				case "binI":
					binElevatorMotor.setI(Double.parseDouble((String)value));
					break;
				case "binD":
					binElevatorMotor.setD(Double.parseDouble((String)value));
					break;
				case "binF":
					binElevatorMotor.setF(Double.parseDouble((String)value));
					break;
				}
			}
			
		}, true);
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
    
    private boolean wasManual = false;
    private double toToteElevatorPosition(double binElevatorPosition) {
    	return binElevatorPosition + kTotalHeight;
    }
    private double toBinElevatorPosition(double binElevatorPosition) {
    	return binElevatorPosition - kTotalHeight;
    }
    private void binElevatorUpdate() {  
    	double bottonOfBinElevator = toToteElevatorPosition(moveTo) + kBinElevatorHeightModifier;
    	double topOfToteElevator = ToteElevatorSystem.getInstance().getToteElevatorPosition() + kToteElevatorHeightModifier + kSafetyTolerance;
    	double finalMoveTo;
    	if(bottonOfBinElevator <= topOfToteElevator) {
    		bottonOfBinElevator = topOfToteElevator;
    		finalMoveTo = toBinElevatorPosition(bottonOfBinElevator - kBinElevatorHeightModifier);
    	}
    	else {
    		finalMoveTo = moveTo;
    	}
    	
    	System.out.println(bottonOfBinElevator);
    	System.out.println(topOfToteElevator);
    	
		if(binElevatorUp.get()) {
			binElevatorMotor.changeControlMode(ControlMode.PercentVbus);
			binElevatorMotor.set(kBinElevatorUpSpeed);
			usePosition = false;
			moveTo = Double.NaN;
			binElevatorSpeed = 0.0;
			wasManual = true;
		}
		else if(binElevatorDown.get()) {
			binElevatorMotor.changeControlMode(ControlMode.PercentVbus);
			if(bottonOfBinElevator <= topOfToteElevator) {
				binElevatorMotor.set(0.0);	
			}
			else {
				binElevatorMotor.set(kBinElevatorDownSpeed);
			}
			usePosition = false;
			moveTo = Double.NaN;
			binElevatorSpeed = 0.0;
			wasManual = true;
		}
		else {
			if(wasManual) {
				setBinElevatorPosition(getBinElevatorPosition());
				wasManual = false;
				useStatePostion = false;
			}
			if(usePosition) {
				if(Double.isNaN(moveTo)) {
					binElevatorMotor.changeControlMode(ControlMode.PercentVbus);
					binElevatorMotor.set(0);
				}
				else  {
//					if(Math.abs(moveTo - getBinElevatorPosition()) < kPositionTolerance) {
//						binElevatorMotor.changeControlMode(ControlMode.PercentVbus);
//						binElevatorMotor.set(0);
//					}
//					else {
					binElevatorMotor.changeControlMode(ControlMode.Position);
					binElevatorMotor.set(finalMoveTo *kEncoderTicksPerInch);
//					}
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
		SmartDashboard.putNumber("binElevatorMotor.getOutputCurrent()", binElevatorMotor.getOutputCurrent());
    }
    
    public void binTilt(double moveValue) {
    	tilterMotorSpeed = moveValue;
    }
    
    private void binTiltUpdate() {
    	if(ableToTilt) {
    		double toteElevatorPosition = ToteElevatorSystem.getInstance().getToteElevatorPosition();
        	if(binTiltManualButton.get()) {
        		double manualSpeed = leftStick.getY();
        		if(manualSpeed < 0.0 && toteElevatorPosition >= kToteElevatorSafetyForTilt) {
        			tilterMotor.set(0.0);
        		} 
        		else {
        			tilterMotor.set(manualSpeed);
        		}
        	}
        	else {
        		if(tilterMotorSpeed < 0.0 && toteElevatorPosition >= kToteElevatorSafetyForTilt) {
        			tilterMotor.set(0.0);
        		}
        		else {
        			tilterMotor.set(tilterMotorSpeed);
        		}
        	}	
    	}
    	else {
    		tilterMotor.set(0.0);
    	}
    }
    
    public boolean getTilterMotorFwdLimitSwitch() {
    	return tilterMotor.isFwdLimitSwitchClosed();
    }
    
    public void moveBinGrip(double moveValue) {
    	binGripSpeed = moveValue;
    }
    
    private void binGripUpdate() {
    	if(openClawButton.get()) {
    		binGripMotor.set(0.5);
    	}
    	else if(closeClawButton.get()) {
    		binGripMotor.set(-0.5);
    	}
    	else {
    		binGripMotor.set(binGripSpeed);
    	}
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

    private boolean useStatePostion = true;
    
    public void init(State state) {
		usePosition = false;
		binElevatorSpeed = 0;
		moveTo = Double.NaN;
		
		useStatePostion = true;
		ableToTilt = true;
		
		switch(state) {
		case Init:
			bInitFinished = false;
			break;
		case Idle:
			setBinElevatorPosition(getBinElevatorPosition());
			break;
		case HumanFeed_RaiseTote:
			ableToTilt = false;
			break;
		case HumanFeed_ThrottleConveyorAndDescend:
			ableToTilt = false;
			break;
		case HumanFeed_ToteOnConveyor:
			ableToTilt = false;
			break;
		case HumanFeed_WaitForTote:
			ableToTilt = false;
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
			//setBinElevatorPosition(0.0); //just move it to top for now
			break;
		case HumanFeed_RaiseTote:
			if(useStatePostion) {
				setBinElevatorPosition(kOutOfTheWayPosition);	
			}
			break;
		case HumanFeed_WaitForTote:
			if(useStatePostion) {
				setBinElevatorPosition(kOutOfTheWayPosition);	
			}
			break;
		case HumanFeed_ToteOnConveyor:
			if(useStatePostion) {
				setBinElevatorPosition(kOutOfTheWayPosition);	
			}
			break;
		case HumanFeed_ThrottleConveyorAndDescend:
			if(useStatePostion) {
				setBinElevatorPosition(kOutOfTheWayPosition);	
			}
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

