package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.UPreferences;
import org.usfirst.frc.team1619.robot.commands.ULinearDriveCommand;
import org.usfirst.frc.team1619.robot.subsystems.UBinElevatorSystem;
import org.usfirst.frc.team1619.robot.subsystems.UConveyorSystem;
import org.usfirst.frc.team1619.robot.subsystems.URakerSystem;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 * Master Class of Autonomous, Stage 0
 */
public class UBinRakerAuto extends CommandGroup {
	public enum BinRakerMode {
		NO_DRIVE,
		CARPET_SIDE,
		PLATFORM_SIDE,
		XTR3M3BinRakerN0Sc0pe3MuchSweg5UByMountainDewMkII,
	}
	
    public  UBinRakerAuto(BinRakerMode mode) {
    	addParallel(new URakerMove(URakerSystem.kRakerDownSpeed, 1.0));
    	addParallel(new UMoveConveyor(UConveyorSystem.kManualForwardConveyorSpeed, 0.5));
    	addSequential(new WaitCommand(0.5));
    	addParallel(new UMoveConveyor(UConveyorSystem.kManualBackConveyorSpeed, 0.5));

    	if(mode == BinRakerMode.NO_DRIVE) {
    		return;
    	}
    	
    	addSequential(new ULinearDriveCommand(1.0, 1));
    	
//    	addParallel(new ToteElevatorMove(1.0, 0.5));
    	addParallel(new UMoveBinElevator(false, UBinElevatorSystem.kBinElevatorUpSpeed));
    	if(mode == BinRakerMode.XTR3M3BinRakerN0Sc0pe3MuchSweg5UByMountainDewMkII) {
    		addParallel(new UClawsOpen(UBinElevatorSystem.kBinGripOpenTime));
    		addParallel(new UArmDown());
    		addSequential(new ULinearDriveCommand(UPreferences.getPreferences().get("SecondSlowDriveDistance", 0.4), 0.5));
//    		addParallel(new )
    	}
    	else {
    		addSequential(new ULinearDriveCommand(0.7, 0.5));
    	}
    	
    	addParallel(new URakerMove(URakerSystem.kRakerUpSpeed, 6.0));
    	
    	if(mode == BinRakerMode.PLATFORM_SIDE) {
    		addSequential(new ULinearDriveCommand(-1.0, 0.6));
    	}
    	else if(mode == BinRakerMode.CARPET_SIDE) {
    		addSequential(new ULinearDriveCommand(-0.5, 0.6));
    	}
    	
    	
//    	for(int i = 0; i < 1; i++) {
//    		addSequential(new LinearDriveCommand(-0.3, 0.7));
//    		addSequential(new LinearDriveCommand(0.3, 0.7));
    	}
    	
//    	addParallel(new LinearDriveCommand(2.5, 0.5));
//    	addParallel(new RakerMove(RakerSystem.kRakerUpSpeed, 6.0));
//    	addSequential(new LinearDriveCommand(-0.1, 0.7));
//    	addSequential(new LinearDriveCommand(0.5, 0.7));
//    }
}
