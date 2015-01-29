
package org.usfirst.frc.team1619.robot;


import org.usfirst.frc.team1619.Lumberjack;
import org.usfirst.frc.team1619.robot.subsystems.Accelerometer;
import org.usfirst.frc.team1619.robot.subsystems.Camera;
import org.usfirst.frc.team1619.robot.subsystems.Conveyor;
import org.usfirst.frc.team1619.robot.subsystems.Drivetrain;
import org.usfirst.frc.team1619.robot.subsystems.LEDStrip;
import org.usfirst.frc.team1619.robot.subsystems.LiftSystem;
import org.usfirst.frc.team1619.robot.subsystems.MotorSystem;
import org.usfirst.frc.team1619.robot.subsystems.OpticalSensor;
import org.usfirst.frc.team1619.robot.subsystems.Smashboard;
import org.usfirst.frc.team1619.robot.subsystems.SonarSystem;
import org.usfirst.frc.team1619.robot.subsystems.SwitchSubsystem;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
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

	public OI oi;
	
	public Drivetrain drivetrain;
	public OpticalSensor opticalSensor;
	public MotorSystem motorSystem;
	public SonarSystem sonarSystem;
	public Accelerometer accelerometer;
	public PowerDistributionPanel pdpCAN;
	public Camera camera;
	public Smashboard smashboard;
	public LiftSystem liftSubsystem;
	public Conveyor conveyor;
	private Lumberjack lumberjack;
	private Timer timer;
	public LEDStrip ledStrip;
	public SwitchSubsystem switchSubsystem;
	

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	
    	drivetrain = new Drivetrain();
		opticalSensor = new OpticalSensor();
		motorSystem = new MotorSystem();
		sonarSystem = new SonarSystem();
		accelerometer = new Accelerometer();
		pdpCAN = new PowerDistributionPanel();
		camera = new Camera();
		smashboard = new Smashboard();
		liftSubsystem = new LiftSystem();
		conveyor = new Conveyor();
		timer = new Timer();
		switchSubsystem = new SwitchSubsystem();
		lumberjack = new Lumberjack("PDP.csv",
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
        

		// new OI needs to be called last
		oi = new OI();
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

    public void autonomousInit() {
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){
    	Lumberjack.changeLogs();
    }

    /**
     * This function is called periodically during operator control
     */
    
   public void teleopInit() {
	   timer.start();
	   
    }
    
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        SmartDashboard.putNumber("PDP", pdpCAN.getTotalCurrent());
        if (timer.get() >= 1) {
        	System.out.println(pdpCAN.getTotalCurrent());
            lumberjack.log(Double.toString(pdpCAN.getTotalCurrent()), 
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
            timer.reset();
        }
    }
    
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
