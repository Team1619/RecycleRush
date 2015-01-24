
package org.usfirst.frc.team1619.robot;


import org.usfirst.frc.team1619.Lumberjack;
import org.usfirst.frc.team1619.robot.subsystems.Accelerometer;
import org.usfirst.frc.team1619.robot.subsystems.BearClaw;
import org.usfirst.frc.team1619.robot.subsystems.Camera;
import org.usfirst.frc.team1619.robot.subsystems.Drivetrain;
import org.usfirst.frc.team1619.robot.subsystems.GyroSubsystem;
import org.usfirst.frc.team1619.robot.subsystems.LiftSubsystem;
import org.usfirst.frc.team1619.robot.subsystems.MotorSystem;
import org.usfirst.frc.team1619.robot.subsystems.OpticalSensor;
import org.usfirst.frc.team1619.robot.subsystems.Smashboard;
import org.usfirst.frc.team1619.robot.subsystems.SonarSystem;

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
	public BearClaw bearClaw;
	public OpticalSensor opticalSensor;
	public MotorSystem motorSystem;
	public SonarSystem sonarSystem;
	public Accelerometer accelerometer;
	public PowerDistributionPanel pdpCAN;
	public GyroSubsystem gyro;
	public Camera camera;
	public Smashboard smashboard;
	public LiftSubsystem liftSubsystem;
	private Lumberjack lumberjack;
	

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	
    	drivetrain = new Drivetrain();
		//bearClaw = new BearClaw();
		opticalSensor = new OpticalSensor();
		motorSystem = new MotorSystem();
		sonarSystem = new SonarSystem();
		accelerometer = new Accelerometer();
		pdpCAN = new PowerDistributionPanel();
		gyro = new GyroSubsystem();
		camera = new Camera();
		smashboard = new Smashboard();
		oi = new OI();
		liftSubsystem = new LiftSubsystem();
		lumberjack = new Lumberjack("Robot Log", "PDP Total Current");
        // instantiate the command used for the autonomous period
		// new OI needs to be called last
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
        lumberjack.log(Double.toString(pdpCAN.getTotalCurrent()));
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
    Timer timer = new Timer();
    
   public void teleopInit() {
	   timer.start();
	   
    }
    
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        SmartDashboard.putNumber("PDP", pdpCAN.getTotalCurrent());
    }
    
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
