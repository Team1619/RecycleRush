package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.robot.commands.LinearDriveCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class BinGrabReverseAuto extends CommandGroup {
    
    public  BinGrabReverseAuto() {
        addParallel(new RaiseBinElevatorCommand());
    	addSequential(new LinearDriveCommand(0.8, 0.5));
        addSequential(new ClawsClose(0.5, 2));
        addParallel(new LinearDriveCommand(-3, 0.5));
        addSequential(new ArmUp(1));
    }
}