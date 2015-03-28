package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.robot.commands.LinearDriveCommand;
import org.usfirst.frc.team1619.robot.subsystems.RakerSystem;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Master Class of Autonomous, Stage 0
 */
public class BinRakerAuto extends CommandGroup {

    public  BinRakerAuto() {
    	addParallel(new RaiseBinElevatorCommand());
//    	addSequential(new LinearDriveCommand(-0.15, 0.5));
    	addSequential(new RakerMove(RakerSystem.kRakerDownSpeed, 1.0));
    	addParallel(new ToteElevatorMove(0.5, 0.5));
    	
    	addSequential(new LinearDriveCommand(1.0, 1));
    	addSequential(new LinearDriveCommand(2.0, 0.5));
    	addParallel(new RakerMove(RakerSystem.kRakerUpSpeed, 6.0));
    	for(int i = 0; i < 1; i++) {
    		addSequential(new LinearDriveCommand(-0.3, 0.7));
    		addSequential(new LinearDriveCommand(0.3, 0.7));
    	}
    	
//    	addParallel(new LinearDriveCommand(2.5, 0.5));
//    	addParallel(new RakerMove(RakerSystem.kRakerUpSpeed, 6.0));
//    	addSequential(new LinearDriveCommand(-0.1, 0.7));
//    	addSequential(new LinearDriveCommand(0.5, 0.7));
    }
}
