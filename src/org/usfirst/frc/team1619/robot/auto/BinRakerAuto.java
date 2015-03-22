package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.robot.commands.LinearDriveCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Master Class of Autonomous, Stage 0
 */
public class BinRakerAuto extends CommandGroup {
    public  BinRakerAuto() {
    	addParallel(new RaiseBinElevatorCommand());
    	addSequential(new RakerMove(1, 1));
    	addParallel(new ToteElevatorMove(0.5, 0.5));
    	addSequential(new LinearDriveCommand(1.5, 1));
    	addParallel(new RakerMove(-1, 3));
    	addSequential(new LinearDriveCommand(3.25, 0.5));
    }
}
