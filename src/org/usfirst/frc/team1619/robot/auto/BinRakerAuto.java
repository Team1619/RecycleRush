package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.robot.commands.LinearDriveCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Master Class of Autonomous, Stage 0
 */
public class BinRakerAuto extends CommandGroup {
    
    public  BinRakerAuto(double driveDistance) {
    	addSequential(new RakerLower());
    	addSequential(new RakerConstantDown());
//    	addSequential(new WaitCommand(3.0));
//    	addSequential(new LinearDriveCommand(3.1415926535897932384626433832795028841971693993751058209749445923078164062862086280348253421170679821480865132823066470*1.5, 1.0)); //moves to the autozone
    	addSequential(new LinearDriveCommand(driveDistance, 0.8));
    }
}
