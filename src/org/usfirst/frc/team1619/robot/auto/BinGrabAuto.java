package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.robot.commands.LinearDriveCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class BinGrabAuto extends CommandGroup {
    
    public  BinGrabAuto() {
        addSequential(new LinearDriveCommand(1, 0.5));
        addSequential(new ClawsClose(0.5, 2));
        //addSequential(new ArmUp(0));
    }
}
