package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.commands.SonarCommand;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Sonar extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private AnalogInput rangeFinder;
	private double distanceToVoltageRatio;
	
	public Sonar() {
		rangeFinder = new AnalogInput(RobotMap.sonarAnalogInputID);
		distanceToVoltageRatio = 1.0;
	}
	
	public void initDefaultCommand() {
		setDefaultCommand(new SonarCommand());
	}

	public double getRange() {
		return rangeFinder.getValue()*distanceToVoltageRatio;
	}

}

