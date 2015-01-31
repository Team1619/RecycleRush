package org.usfirst.frc.team1619.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Encoder extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	
    	
    }
    
    // Calculates distance traveled via encoder pulses counted.
    public static double Distance(double DiameterCM, int PulsesPerRev, int Pulses){
    	double circumference = Math.PI * DiameterCM;
    	
    	double distancePerPulse = circumference / PulsesPerRev;
    	
    	return distancePerPulse * Pulses;
    }
}

