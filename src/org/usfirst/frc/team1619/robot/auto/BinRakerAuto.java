package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.robot.commands.LinearDriveCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Master Class of Autonomous, Stage 0
 */
public class BinRakerAuto extends CommandGroup {
    
    public  BinRakerAuto() {
    	addSequential(new PrepareRake());
    	addSequential(new LinearDriveCommand(-1));
    	addSequential(new MoveSecondPosition());
    	addSequential(new PrepareRake());
    	addSequential(new LinearDriveCommand(-1));
    	addSequential(new LinearDriveCommand(10)); //moves to the autozone
    	
    }
}
