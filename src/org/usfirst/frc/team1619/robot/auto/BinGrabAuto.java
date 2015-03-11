package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.robot.commands.LinearDriveCommand;
import org.usfirst.frc.team1619.robot.commands.RaiseBinElevatorCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class BinGrabAuto extends CommandGroup {
    
    public  BinGrabAuto() {
        addParallel(new RaiseBinElevatorCommand());
    	addSequential(new LinearDriveCommand(1, 0.5));
        addSequential(new ClawsClose(0.5, 2));
        addParallel(new LinearDriveCommand(0.56, 0.5));
        addSequential(new ArmUp(1, 1.35));
        //addParallel(new TurnCommand(180));
    }
}
