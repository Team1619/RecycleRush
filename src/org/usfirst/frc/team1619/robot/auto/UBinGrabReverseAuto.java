package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.robot.commands.ULinearDriveCommand;
import org.usfirst.frc.team1619.robot.subsystems.UBinElevatorSystem;
import org.usfirst.frc.team1619.robot.subsystems.UConveyorSystem;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class UBinGrabReverseAuto extends CommandGroup {
    
    public  UBinGrabReverseAuto() {
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
    	addParallel(new UClawsOpen(UBinElevatorSystem.kBinGripOpenTime));
    	addSequential(new UMoveBinElevator(true, -0.6));
    	addSequential(new ULinearDriveCommand(0.1, 1.0));
    	addSequential(new UArmDown());
    	addSequential(new UClawsClose(0.5, 1.0));
    	addParallel(new UArmUp(1));
    	addSequential(new ULinearDriveCommand(-1.0, 0.5));
    	addSequential(new ULinearDriveCommand(-2.8, 0.7));
    	addSequential(new UMoveConveyor(UConveyorSystem.kManualBackConveyorSpeed, 0.5));
    }
}