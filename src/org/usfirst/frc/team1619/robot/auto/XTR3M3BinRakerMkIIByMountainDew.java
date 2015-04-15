package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.Preferences;
import org.usfirst.frc.team1619.robot.commands.LinearDriveCommand;
import org.usfirst.frc.team1619.robot.subsystems.BinElevatorSystem;
import org.usfirst.frc.team1619.robot.subsystems.RakerSystem;

import edu.wpi.first.wpilibj.command.CommandGroup;


// TODO: Doritos (you mean Darude ;)
/**
 *
 */
public class XTR3M3BinRakerMkIIByMountainDew extends CommandGroup {
    
    public  XTR3M3BinRakerMkIIByMountainDew() {
    	// Hold claw at top
    	addParallel(new MoveBinElevator(false, BinElevatorSystem.kBinElevatorUpSpeed));
    	
    	// Open claws at beginning
    	addParallel(new ClawsOpen(BinElevatorSystem.kBinGripOpenTime));
    	
    	// Let toteElevator fall flat
    	addSequential(new ToteElevatorMove(0.5, 0.5));
    	
    	// Bring it down again
    	addParallel(new ToteElevatorMove(-0.5, 0.5));
    	
    	// Lower arm for bin pickup
    	addParallel(new ArmDown());
    	
    	// Lower raker for rake
    	addSequential(new RakerMove(RakerSystem.kRakerDownSpeed, 1.25));
    	
    	// Drive towards bin
    	addSequential(new LinearDriveCommand(1.0, 1));
    	
    	// Slow down...
    	addSequential(new LinearDriveCommand(Preferences.getNumber("InitialSlowDriveDistance", 1.0), 0.5));
    	
    	// Grab the bin
    	addParallel(new ClawsClose(0.5, 2.0));
    	
    	// Drive over bin as it's grabbed
    	addSequential(new LinearDriveCommand(Preferences.getNumber("SecondSlowDriveDistance", 1.0), 0.5));
    	
    	// Raise arm
    	addParallel(new ArmUp(1, 1.35));
    	
    	// Raise raker
    	addParallel(new RakerMove(RakerSystem.kRakerUpSpeed, 6.0));
    	
    	// Shake bins loose
    	addSequential(new LinearDriveCommand(Preferences.getNumber("BackDriveShakeDistance", -0.1), 0.7));
    	addSequential(new LinearDriveCommand(Preferences.getNumber("ForwardDriveShakeDistance", 0.2), 0.7));
    	
//    	addParallel(new RakerMove(RakerSystem.kRakerUpSpeed, 6.0));
//    	for(int i = 0; i < 3; i++) {
//    		addSequential(new LinearDriveCommand(-0.2, 0.7));
//    		addSequential(new LinearDriveCommand(0.2, 0.7));
//    	}
//    	addParallel(new LinearDriveCommand(2.5, 0.5));
//    	addParallel(new RakerMove(RakerSystem.kRakerUpSpeed, 6.0));
//    	addSequential(new LinearDriveCommand(-0.1, 0.7));
//    	addSequential(new LinearDriveCommand(0.5, 0.7));
    }
}
