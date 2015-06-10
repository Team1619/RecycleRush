package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.robot.commands.ULinearDriveCommand;
import org.usfirst.frc.team1619.robot.subsystems.UBinElevatorSystem;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class UBinGrabWithLitterAuto extends CommandGroup {

	public UBinGrabWithLitterAuto() {
		addParallel(new UMoveBinElevator(false,
				UBinElevatorSystem.kBinElevatorUpSpeed));
		addParallel(new UClawsOpen(UBinElevatorSystem.kBinGripOpenTime));
		addSequential(new ULinearDriveCommand(0.8, 0.5));
		addSequential(new UClawsClose(0.5, 2));
		// addParallel(new LinearDriveCommand(0.5, 0.5));
		addSequential(new UArmUp(1, 1.35));
		// addParallel(new TurnCommand(180));
	}
}
