
package org.usfirst.frc.team1619.robot;

import org.usfirst.frc.team1619.UPreferences;
import org.usfirst.frc.team1619.robot.auto.UBinGrabReverseAuto;
import org.usfirst.frc.team1619.robot.auto.UBinGrabWithLitterAuto;
import org.usfirst.frc.team1619.robot.auto.UBinRakerAuto;
import org.usfirst.frc.team1619.robot.auto.UBinRakerAuto.BinRakerMode;
import org.usfirst.frc.team1619.robot.auto.UGetOutTheWayAuto;
import org.usfirst.frc.team1619.robot.subsystems.UBinElevatorSystem;
import org.usfirst.frc.team1619.robot.subsystems.UConveyorSystem;
import org.usfirst.frc.team1619.robot.subsystems.UDrivetrain;
import org.usfirst.frc.team1619.robot.subsystems.UGuardRailSystem;
import org.usfirst.frc.team1619.robot.subsystems.URakerSystem;
import org.usfirst.frc.team1619.robot.subsystems.UToteElevatorSystem;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class URobot extends IterativeRobot {

	//create a singleton robot
	private static URobot sRobot;
	
	private PowerDistributionPanel fPDPCAN;
	private Timer fPDPLogTimer;
	private Timer fElevatorLogTimer;
	private SendableChooser fAutoChooser;
	
	public URobot() {
		sRobot = this;
	}
	
	/**
	 * Gets robot instance.
	 * @return the single robot instance
	 */
	public static URobot getRobot() {
		return sRobot;
	}
	
	public PowerDistributionPanel getPDP() {
		return fPDPCAN;
	}
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		URobotMap.MotorDefinition.initMotors();
		UOI.getInstance().init();
		UBinElevatorSystem.getInstance();
		URakerSystem.getInstance();
		UToteElevatorSystem.getInstance();
		UGuardRailSystem.getInstance();
		fPDPCAN = new PowerDistributionPanel();
		fPDPLogTimer = new Timer();
		fPDPLogTimer.start();
		fElevatorLogTimer = new Timer();
		fElevatorLogTimer.start();				
		
		//Adds autonomous mode options to the smart dashboard
		fAutoChooser = new SendableChooser();
		fAutoChooser.addDefault("None", new WaitCommand(0));
		fAutoChooser.addObject("Present Bin For Litter", new UBinGrabWithLitterAuto());
		fAutoChooser.addObject("Get out of The Way", new UGetOutTheWayAuto());
		fAutoChooser.addObject("Pickup Bin and Get Out Way", new UBinGrabReverseAuto());
		fAutoChooser.addObject("Rake bins (platform)", new UBinRakerAuto(BinRakerMode.PLATFORM_SIDE));
		fAutoChooser.addObject("Rake bins (carpet)", new UBinRakerAuto(BinRakerMode.CARPET_SIDE));
		fAutoChooser.addObject("Rake bins (no drive)", new UBinRakerAuto(BinRakerMode.NO_DRIVE));
		fAutoChooser.addObject("Rake bins (and pick up)", new UBinRakerAuto(BinRakerMode.XTR3M3BinRakerN0Sc0pe3MuchSweg5UByMountainDewMkII));
		
		SmartDashboard.putData("Auto Mode", fAutoChooser);
		
		if ( UPreferences.isTestMode() ) {
			UManualKeyboardControl.getInstance().startRainbowSTORMServer();
		}
		UPreferences.getPreferences().put("SecondSlowDriveDistance", 0.4);
	}

	/**
	 * Smart Dashboard
	 */
	public void sharedPeriodic() {
		SmartDashboard.putBoolean("chute door", true);
		SmartDashboard.putNumber("Left Encoder Position", UDrivetrain.getInstance().getLeftEncoderPosition());
		SmartDashboard.putNumber("Right Encoder Position", UDrivetrain.getInstance().getRightEncoderPosition());
		SmartDashboard.putBoolean("Front Conveyor Optical Sensor", UConveyorSystem.getInstance().getFrontSensor());
		SmartDashboard.putBoolean("Rear Conveyor Optical Sensor", UConveyorSystem.getInstance().getRearSensor());
		
		SmartDashboard.putNumber("ToteElevatorPositionValue", UToteElevatorSystem.getInstance().getToteElevatorPosition());
		SmartDashboard.putNumber("toteElevatorMotor.getEncPosition()", UToteElevatorSystem.getInstance().toteElevatorMotor.getEncPosition());
		SmartDashboard.putBoolean("toteElevatorMotor Fwd Limit", UToteElevatorSystem.getInstance().toteElevatorMotor.isFwdLimitSwitchClosed());
		SmartDashboard.putBoolean("toteElevatorMotor Rev Limit", UToteElevatorSystem.getInstance().toteElevatorMotor.isRevLimitSwitchClosed());
		SmartDashboard.putBoolean("binElevator Fwd Limit", UBinElevatorSystem.getInstance().binElevatorMotor.isFwdLimitSwitchClosed());
    	SmartDashboard.putBoolean("binElevator Rev Limit", UBinElevatorSystem.getInstance().binElevatorMotor.isRevLimitSwitchClosed());
    	SmartDashboard.putBoolean("binTilt Fwd Limit", UBinElevatorSystem.getInstance().tilterMotor.isFwdLimitSwitchClosed());
    	SmartDashboard.putBoolean("binTilt Rev Limit", UBinElevatorSystem.getInstance().tilterMotor.isRevLimitSwitchClosed());
    	SmartDashboard.putNumber("toteElevatorMotor.getOutputVoltage()", UToteElevatorSystem.getInstance().toteElevatorMotor.getOutputVoltage());
		SmartDashboard.putNumber("toteElevatorMotor.getOutputCurrent()", UToteElevatorSystem.getInstance().toteElevatorMotor.getOutputCurrent());
		SmartDashboard.putNumber("binElevatorMotor.getOutputVoltage()", UBinElevatorSystem.getInstance().binElevatorMotor.getOutputVoltage());
		SmartDashboard.putNumber("binElevatorMotor.getOutputCurrent()", UBinElevatorSystem.getInstance().binElevatorMotor.getOutputCurrent());
		SmartDashboard.putNumber("conveyorMotor.getOutputCurrent()", UConveyorSystem.getInstance().conveyorMotor.getOutputCurrent());
    	SmartDashboard.putNumber("conveyorMotor.getOutputVoltage()", UConveyorSystem.getInstance().conveyorMotor.getOutputVoltage());
    	SmartDashboard.putNumber("guardRail.getOutputCurrent()", UGuardRailSystem.getInstance().guardRailMotor.getOutputCurrent());
    	SmartDashboard.putNumber("guardRail.getOutputVoltage()", UGuardRailSystem.getInstance().guardRailMotor.getOutputVoltage());
    	SmartDashboard.putNumber("rakerMotor.getOutputCurrent()", URakerSystem.getInstance().rakerMotor.getOutputCurrent());
    	SmartDashboard.putNumber("rakerMotor.getOutputVoltage()", URakerSystem.getInstance().rakerMotor.getOutputVoltage());
		
		UStateMachine.getInstance().display();
		
		UOI.getInstance().updatePOV();

//		if(RobotState.isDisabled() == false) {
//			if (pdpLogTimer.get() >= 1) {
//				pdpLumberjack.log(Double.toString(pdpCAN.getTotalCurrent()), 
//						Double.toString(pdpCAN.getCurrent(0)),
//						Double.toString(pdpCAN.getCurrent(1)),
//						Double.toString(pdpCAN.getCurrent(2)),
//						Double.toString(pdpCAN.getCurrent(3)),
//						Double.toString(pdpCAN.getCurrent(4)),
//						Double.toString(pdpCAN.getCurrent(5)),
//						Double.toString(pdpCAN.getCurrent(6)),
//						Double.toString(pdpCAN.getCurrent(7)),
//						Double.toString(pdpCAN.getCurrent(8)),
//						Double.toString(pdpCAN.getCurrent(9)),
//						Double.toString(pdpCAN.getCurrent(10)),
//						Double.toString(pdpCAN.getCurrent(11)),
//						Double.toString(pdpCAN.getCurrent(12)),
//						Double.toString(pdpCAN.getCurrent(13)),
//						Double.toString(pdpCAN.getCurrent(14)),
//						Double.toString(pdpCAN.getCurrent(15)),
//						Double.toString(pdpCAN.getVoltage()), 
//						Double.toString(pdpCAN.getTemperature()), 
//						Double.toString(pdpCAN.getTotalPower()), 
//						Double.toString(pdpCAN.getTotalEnergy())
//						);
//				pdpLogTimer.reset();
//			} //25.0 fish
//			
//			if(isDisabled()) {
//				operationState = "Disabled";
//			}
//			else if(isAutonomous()) {
//				operationState = "Autonomous";
//			}
//			else if(isOperatorControl()) {
//				operationState = "TeleOp";
//			}
//			else if(isTest()) {
//				operationState = "Test";
//			}
//			else {
//				operationState = "Limbo";
//			}
			
//			if (elevatorLogTimer.get() >= 0.1) {
//				elevatorLumberjack.log(
//						Double.toString(ToteElevatorSystem.getInstance().toteElevatorMotor.getOutputVoltage()),
//						Double.toString(ToteElevatorSystem.getInstance().toteElevatorMotorSmall.getOutputVoltage()),
//						Double.toString(BinElevatorSystem.getInstance().binElevatorMotor.getOutputVoltage()),
//						Double.toString(ToteElevatorSystem.getInstance().toteElevatorMotor.getOutputCurrent()),
//						Double.toString(ToteElevatorSystem.getInstance().toteElevatorMotorSmall.getOutputCurrent()),
//						Double.toString(BinElevatorSystem.getInstance().binElevatorMotor.getOutputCurrent()),
//						Double.toString(ToteElevatorSystem.getInstance().toteElevatorMotor.getEncVelocity()),
//						Double.toString(ToteElevatorSystem.getInstance().toteElevatorMotor.getPosition()),
//						Double.toString(ToteElevatorSystem.getInstance().getToteElevatorPosition()),
//						StateMachine.getInstance().getState().toString(),
//						Double.toString(StateMachine.getInstance().numberTotes),
//						operationState
//						);
//				elevatorLogTimer.reset();
//			}
			
			
//			if(motorLogTimer.get() >= 0.1) {
//				RobotMap.MotorDefinition[] motorDefinitions = RobotMap.MotorDefinition.values();
//				String[] motorLumberjackValues= new String[motorDefinitions.length * 2];
//				for(int i = 0, j = 0, len = motorLumberjackValues.length; i < len; i+=2, j++) {
//					motorLumberjackValues[i] = Double.toString(motorDefinitions[j].getMotor().getOutputCurrent());
//					motorLumberjackValues[i + 1] = Double.toString(motorDefinitions[j].getMotor().getOutputVoltage());
//				}
//				motorLumberjack.log(motorLumberjackValues);
//				motorLogTimer.reset();
//			}
	}


	public void autonomousInit() {
//		if(!logsChanged) {
//			logsChanged = true;
//			Lumberjack.changeLogs();
//		}
		
		//add in Auto stuff thing
		UDrivetrain.getInstance().autoMode();
//		Command autoCommand = new BinGrabWithLitterAuto();
//		Command autoCommand = new GetOutTheWayAuto();
//		Command autoCommand = new BinGrabReverseAuto();
//		Command autoCommand = new BinRakerAuto(BinRakerMode.CARPET_SIDE);
//		Command autoCommand = new BinRakerAuto(BinRakerMode.NO_DRIVE);
//		Command autoCommand = new BinRakerAuto(BinRakerMode.PLATFORM_SIDE);
//		Command autoCommand = new BinRakerAuto(BinRakerMode.CARPET_SIDE);
		Command autoCommand = (Command) fAutoChooser.getSelected();
//		Command autoCommand = new BinRakerAuto(BinRakerMode.WITH_PICKUP);
		autoCommand.cancel();
		autoCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		sharedPeriodic();
		UBinElevatorSystem.getInstance().binElevatorUpdate();
		UBinElevatorSystem.getInstance().binTiltUpdate();
		UBinElevatorSystem.getInstance().binGripUpdate();
		URakerSystem.getInstance().rakerUpdate();
		UToteElevatorSystem.getInstance().toteElevatorUpdate();
	}

	/**
	 * This function is called when the disabled button is hit.
	 * You can use it to reset subsystems before shutting down.
	 */

	private Timer gyroInitTimer = new Timer();
	private boolean gyroInitTimerRunning;
	private boolean gyroInitTimerFinished;
	
	public void disabledInit(){
//		logsChanged = false;
//		Lumberjack.changeLogs();
		gyroInitTimer.start();
		gyroInitTimerRunning = false;
		gyroInitTimerFinished = true;
	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		sharedPeriodic();
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopInit() {

//		if(!logsChanged) {
//			logsChanged = true;
//			Lumberjack.changeLogs();
//		}
		//StateMachine.getInstance().abortSignal.raise();
		UStateMachine.getInstance().init();
		UDrivetrain.getInstance().teleopMode();
	}

	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		sharedPeriodic();
		UStateMachine.getInstance().run();
	}


	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		//LiveWindow.run();
		if ( UPreferences.isTestMode()) {
			UManualKeyboardControl.getInstance().runRainbowSTORM();
		}
		sharedPeriodic();
	}
}
