
package org.usfirst.frc.team1619.robot;

import org.usfirst.frc.team1619.Lumberjack;
import org.usfirst.frc.team1619.robot.auto.BinGrabReverseAuto;
import org.usfirst.frc.team1619.robot.auto.BinGrabWithLitterAuto;
import org.usfirst.frc.team1619.robot.auto.BinRakerAuto;
import org.usfirst.frc.team1619.robot.auto.GetOutTheWayAuto;
import org.usfirst.frc.team1619.robot.subsystems.Accelerometer;
import org.usfirst.frc.team1619.robot.subsystems.BinElevatorSystem;
import org.usfirst.frc.team1619.robot.subsystems.ConveyorSystem;
import org.usfirst.frc.team1619.robot.subsystems.Drivetrain;
import org.usfirst.frc.team1619.robot.subsystems.GuardRailSystem;
import org.usfirst.frc.team1619.robot.subsystems.GyroSystem;
import org.usfirst.frc.team1619.robot.subsystems.RakerSystem;
import org.usfirst.frc.team1619.robot.subsystems.ToteElevatorSystem;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	//create a singleton robot
	private static Robot robot;
	public Robot() {
		robot = this;
	}
	static public Robot getRobot() {
		return robot;
	}

	public PowerDistributionPanel pdpCAN;
	private Lumberjack pdpLumberjack;
	private Lumberjack elevatorLumberjack;
	private Lumberjack motorLumberjack;
	private Timer pdpLogTimer;
	private Timer elevatorLogTimer;
	private Timer motorLogTimer;
	private boolean logsChanged = false;
	private SendableChooser autoChooser;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		RobotMap.MotorDefinition.initMotors();
		OI.getInstance().init();
		GyroSystem.getInstance().calibrate();
		//Camera.getInstance();
		BinElevatorSystem.getInstance();
		RakerSystem.getInstance();
		ToteElevatorSystem.getInstance();
		GuardRailSystem.getInstance();
		pdpCAN = new PowerDistributionPanel();
		pdpLogTimer = new Timer();
		pdpLogTimer.start();
		elevatorLogTimer = new Timer();
		elevatorLogTimer.start();
		motorLogTimer = new Timer();
		motorLogTimer.start();

		//switchSubsystem = new LimitSwitch();
		pdpLumberjack = new Lumberjack("PDP.csv",
				"PDP Total Current", 
				"Current 0", 
				"Current 1", 
				"Current 2", 
				"Current 3", 
				"Current 4", 
				"Current 5",
				"Current 6",
				"Current 7",
				"Current 8",
				"Current 9",
				"Current 10",
				"Current 11",
				"Current 12",
				"Current 13",
				"Current 14",
				"Current 15",
				"PDP Voltage", 
				"PDP Temperature", 
				"PDP Total Power", 
				"PDP Total Energy"
				);
		
		
		elevatorLumberjack = new Lumberjack(
				"Elevators.csv", 
				"Tote Voltage 1", 
				"Tote Voltage 2",
				"Bin Voltage", 
				"Tote Current 1", 
				"Tote Current 2", 
				"Bin Current"
				);
		
		
		String[] motorLumberjackHeaders = new String[RobotMap.MotorDefinition.values().length * 2];
		for(int i = 0, j = 0, len = motorLumberjackHeaders.length; i < len; i+=2, j++) {
			motorLumberjackHeaders[i] = "Current " + j;
			motorLumberjackHeaders[i + 1] = "Voltage " + j;
		}
		motorLumberjack = new Lumberjack("Motors.csv", motorLumberjackHeaders);
				
		
		autoChooser = new SendableChooser();
		autoChooser.addDefault("Get out of The Way", new GetOutTheWayAuto());
		autoChooser.addObject("Present Bin For Litter", new BinGrabWithLitterAuto());
		autoChooser.addObject("Pickup Bin and Get Out Way", new BinGrabReverseAuto());
		autoChooser.addObject("Rake Bins", new BinRakerAuto());
		SmartDashboard.putData("Auto Mode", autoChooser);
		
		//ManualKeyboardControl.getInstance().startRainbowSTORMServer();
	}

	/**
	 * Smart Dashboard
	 */
	public void sharedPeriodic() {
		SmartDashboard.putBoolean("chute door", true);
//		SmartDashboard.putNumber("Gyro Direction", GyroSystem.getInstance().getHeading());
//		SmartDashboard.putNumber("Gyro Temperature", GyroSystem.getInstance().getTemperature());
//		SmartDashboard.putNumber("Gyro Turn Rate", GyroSystem.getInstance().getTurnRate());
//		SmartDashboard.putString("Accelerometer X", Double.toString(accelerometer.getX()));
//		SmartDashboard.putString("Accelerometer Y", Double.toString(accelerometer.getY()));
//		SmartDashboard.putString("Accelerometer Z", Double.toString(accelerometer.getZ()));
		SmartDashboard.putNumber("Left Encoder Position", Drivetrain.getInstance().getLeftEncoderPosition());
		SmartDashboard.putNumber("Right Encoder Position", Drivetrain.getInstance().getRightEncoderPosition());
		SmartDashboard.putBoolean("Front Conveyor Optical Sensor", ConveyorSystem.getInstance().getFrontSensor());
		SmartDashboard.putBoolean("Rear Conveyor Optical Sensor", ConveyorSystem.getInstance().getRearSensor());
		
		SmartDashboard.putNumber("ToteLiftPositionValue", ToteElevatorSystem.getInstance().getToteElevatorPosition());
		SmartDashboard.putNumber("toteElevatorMotor.getEncPosition()", ToteElevatorSystem.getInstance().toteElevatorMotor.getEncPosition());
		SmartDashboard.putBoolean("toteElevatorMotor Fwd Limit", ToteElevatorSystem.getInstance().toteElevatorMotor.isFwdLimitSwitchClosed());
		SmartDashboard.putBoolean("toteElevatorMotor Rev Limit", ToteElevatorSystem.getInstance().toteElevatorMotor.isRevLimitSwitchClosed());
		SmartDashboard.putBoolean("binElevator Fwd Limit", BinElevatorSystem.getInstance().binElevatorMotor.isFwdLimitSwitchClosed());
    	SmartDashboard.putBoolean("binElevator Rev Limit", BinElevatorSystem.getInstance().binElevatorMotor.isRevLimitSwitchClosed());
    	SmartDashboard.putBoolean("binTilt  Fwd Limit", BinElevatorSystem.getInstance().tilterMotor.isFwdLimitSwitchClosed());
    	SmartDashboard.putBoolean("binTilt  Rev Limit", BinElevatorSystem.getInstance().tilterMotor.isRevLimitSwitchClosed());
    	SmartDashboard.putNumber("toteElevatorMotor.getOutputVoltage()", ToteElevatorSystem.getInstance().toteElevatorMotor.getOutputVoltage());
		SmartDashboard.putNumber("toteElevatorMotor.getOutputCurrent()", ToteElevatorSystem.getInstance().toteElevatorMotor.getOutputCurrent());
		SmartDashboard.putNumber("binElevatorMotor.getOutputVoltage()", BinElevatorSystem.getInstance().binElevatorMotor.getOutputVoltage());
		SmartDashboard.putNumber("binElevatorMotor.getOutputCurrent()", BinElevatorSystem.getInstance().binElevatorMotor.getOutputCurrent());
		SmartDashboard.putNumber("conveyorMotor.getOutputCurrent()", ConveyorSystem.getInstance().conveyorMotor.getOutputCurrent());
    	SmartDashboard.putNumber("conveyorMotor.getOutputVoltage()", ConveyorSystem.getInstance().conveyorMotor.getOutputVoltage());
    	SmartDashboard.putNumber("guardRail.getOutputCurrent()", GuardRailSystem.getInstance().guardRailMotor.getOutputCurrent());
    	SmartDashboard.putNumber("guardRail.getOutputVoltage()", GuardRailSystem.getInstance().guardRailMotor.getOutputVoltage());
    	SmartDashboard.putNumber("rakerMotor.getOutputCurrent()", RakerSystem.getInstance().rakerMotor.getOutputCurrent());
    	SmartDashboard.putNumber("rakerMotor.getOutputVoltage()", RakerSystem.getInstance().rakerMotor.getOutputVoltage());
		
		StateMachine.getInstance().display();
		
		Accelerometer.getInstance().display();
		OI.getInstance().updatePOV();

		if(RobotState.isDisabled() == false) {
			if (pdpLogTimer.get() >= 1) {
				pdpLumberjack.log(Double.toString(pdpCAN.getTotalCurrent()), 
						Double.toString(pdpCAN.getCurrent(0)),
						Double.toString(pdpCAN.getCurrent(1)),
						Double.toString(pdpCAN.getCurrent(2)),
						Double.toString(pdpCAN.getCurrent(3)),
						Double.toString(pdpCAN.getCurrent(4)),
						Double.toString(pdpCAN.getCurrent(5)),
						Double.toString(pdpCAN.getCurrent(6)),
						Double.toString(pdpCAN.getCurrent(7)),
						Double.toString(pdpCAN.getCurrent(8)),
						Double.toString(pdpCAN.getCurrent(9)),
						Double.toString(pdpCAN.getCurrent(10)),
						Double.toString(pdpCAN.getCurrent(11)),
						Double.toString(pdpCAN.getCurrent(12)),
						Double.toString(pdpCAN.getCurrent(13)),
						Double.toString(pdpCAN.getCurrent(14)),
						Double.toString(pdpCAN.getCurrent(15)),
						Double.toString(pdpCAN.getVoltage()), 
						Double.toString(pdpCAN.getTemperature()), 
						Double.toString(pdpCAN.getTotalPower()), 
						Double.toString(pdpCAN.getTotalEnergy())
						);
				pdpLogTimer.reset();
			} //25.0 fish
			
			
			if (elevatorLogTimer.get() >= 0.1) {
				elevatorLumberjack.log(
						Double.toString(ToteElevatorSystem.getInstance().toteElevatorMotor.getOutputVoltage()),
						Double.toString(ToteElevatorSystem.getInstance().toteElevatorMotorSmall.getOutputVoltage()),
						Double.toString(BinElevatorSystem.getInstance().binElevatorMotor.getOutputVoltage()),
						Double.toString(ToteElevatorSystem.getInstance().toteElevatorMotor.getOutputCurrent()),
						Double.toString(ToteElevatorSystem.getInstance().toteElevatorMotorSmall.getOutputCurrent()),
						Double.toString(BinElevatorSystem.getInstance().binElevatorMotor.getOutputCurrent())
						);
				elevatorLogTimer.reset();
			}
			
			if(motorLogTimer.get() >= 0.1) {
				RobotMap.MotorDefinition[] motorDefinitions = RobotMap.MotorDefinition.values();
				String[] motorLumberjackValues= new String[motorDefinitions.length * 2];
				for(int i = 0, j = 0, len = motorLumberjackValues.length; i < len; i+=2, j++) {
					motorLumberjackValues[i] = Double.toString(motorDefinitions[j].getMotor().getOutputCurrent());
					motorLumberjackValues[i + 1] = Double.toString(motorDefinitions[j].getMotor().getOutputVoltage());
				}
				motorLumberjack.log(motorLumberjackValues);
				motorLogTimer.reset();
			}
		}
	}


	public void autonomousInit() {
		if(!logsChanged) {
			logsChanged = true;
			Lumberjack.changeLogs();
		}
		//add in Auto stuff thing
		Drivetrain.getInstance().autoMode();
		//new BinGrabReverseAuto().start();
		//new BinGrabAutoWithLitter().start();
		Command autoCommand = (Command) autoChooser.getSelected();
		autoCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		sharedPeriodic();
		BinElevatorSystem.getInstance().binElevatorUpdate();
		BinElevatorSystem.getInstance().binTiltUpdate();
		BinElevatorSystem.getInstance().binGripUpdate();
		RakerSystem.getInstance().rakerUpdate();
		ToteElevatorSystem.getInstance().toteElevatorUpdate();
	}



	/**
	 * This function is called when the disabled button is hit.
	 * You can use it to reset subsystems before shutting down.
	 */

	private Timer gyroInitTimer = new Timer();
	private boolean gyroInitTimerRunning;
	private boolean gyroInitTimerFinished;
	
	public void disabledInit(){
		logsChanged = false;
		Lumberjack.changeLogs();
		gyroInitTimer.start();
		gyroInitTimerRunning = false;
		gyroInitTimerFinished = true;
	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		sharedPeriodic();

		/**
		 * Gyro Timer to recalibrate
		 */
		if(!gyroInitTimerFinished) {
			if(Math.abs(GyroSystem.getInstance().getTurnRate()) < 0.5) {
				if(!gyroInitTimerRunning) {
					gyroInitTimerRunning = true;
					gyroInitTimer.start();
					SmartDashboard.putString("Gyro status", "stabilizing");
				}

				if(gyroInitTimer.hasPeriodPassed(3.0)) {
					gyroInitTimer.stop();
					SmartDashboard.putString("Gyro status", "stabilized and calibrating");
					GyroSystem.getInstance().calibrate();
					SmartDashboard.putString("Gyro status", "calibrated");
					gyroInitTimerFinished = true;
					gyroInitTimerRunning = false;
				}
			}
			else {
				gyroInitTimer.stop();
				gyroInitTimer.reset();
				if(gyroInitTimerRunning)
					SmartDashboard.putString("Gyro status", "unstable");
				gyroInitTimerRunning = false;
			}
		}
	}

	/**
	 * This function is called periodically during operator control
	 */


	public void teleopInit() {

		if(!logsChanged) {
			logsChanged = true;
			Lumberjack.changeLogs();
		}
		//StateMachine.getInstance().abortSignal.raise();
		StateMachine.getInstance().init();
		Drivetrain.getInstance().teleopMode();
	}

	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		sharedPeriodic();
		StateMachine.getInstance().run();
	}


	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		//LiveWindow.run();
		//ManualKeyboardControl.getInstance().runRainbowSTORM();
		sharedPeriodic();
	}
}
