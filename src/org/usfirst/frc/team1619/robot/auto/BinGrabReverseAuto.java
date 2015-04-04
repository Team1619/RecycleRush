package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.robot.commands.LinearDriveCommand;
import org.usfirst.frc.team1619.robot.subsystems.BinElevatorSystem;
import org.usfirst.frc.team1619.robot.subsystems.ConveyorSystem;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class BinGrabReverseAuto extends CommandGroup {
    
    public  BinGrabReverseAuto() {
//    	addParallel(new RaiseBinElevatorCommand());
//    	addSequential(new LinearDriveCommand(0.5, 0.5));
//    	addParallel(new ClawsOpen(BinElevatorSystem.kBinGripOpenTime));
//    	addParallel(new LinearDriveCommand(0.4, 0.6));
//    	addSequential(new ArmDown());
//    	addSequential(new ClawsClose(0.5, 2));
//    	addParallel(new LinearDriveCommand(-3, 1.0));
//    	addSequential(new ArmUp(1));
    	
    	//drive 1 previous 
    	//drive 1 previous speed = 1.0
    	addParallel(new ClawsOpen(BinElevatorSystem.kBinGripOpenTime));
    	addSequential(new MoveBinElevator(true, -0.6));
    	addSequential(new LinearDriveCommand(0.1, 1.0));
    	addSequential(new ArmDown());
    	addSequential(new ClawsClose(0.5, 1.0));
    	addParallel(new ArmUp(1));
    	addSequential(new LinearDriveCommand(-1.0, 0.5));
    	addSequential(new LinearDriveCommand(-2.8, 0.7));
    	addSequential(new MoveConveyor(ConveyorSystem.kManualBackConveyorSpeed, 0.5));
    }
}