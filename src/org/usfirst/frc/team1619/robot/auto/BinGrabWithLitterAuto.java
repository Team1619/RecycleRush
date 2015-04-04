package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.robot.commands.LinearDriveCommand;
import org.usfirst.frc.team1619.robot.subsystems.BinElevatorSystem;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class BinGrabWithLitterAuto extends CommandGroup {
    
    public  BinGrabWithLitterAuto() {
        addParallel(new RaiseBinElevatorCommand());
        addParallel(new ClawsOpen(BinElevatorSystem.kBinGripOpenTime));
    	addSequential(new LinearDriveCommand(0.8, 0.5));
        addSequential(new ClawsClose(0.5, 2));
        //addParallel(new LinearDriveCommand(0.5, 0.5));
        addSequential(new ArmUp(1, 1.35));
        //addParallel(new TurnCommand(180));
    }
}
