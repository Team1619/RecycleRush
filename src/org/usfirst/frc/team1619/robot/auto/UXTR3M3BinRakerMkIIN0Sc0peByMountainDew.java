package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.UPreferences;
import org.usfirst.frc.team1619.robot.commands.ULinearDriveCommand;
import org.usfirst.frc.team1619.robot.subsystems.UBinElevatorSystem;
import org.usfirst.frc.team1619.robot.subsystems.URakerSystem;

import edu.wpi.first.wpilibj.command.CommandGroup;

// TODO: Doritos (you mean Darude ;)
/**
 *
 */
public class UXTR3M3BinRakerMkIIN0Sc0peByMountainDew extends CommandGroup {

	public UXTR3M3BinRakerMkIIN0Sc0peByMountainDew() {
		// Hold claw at top
		addParallel(new UMoveBinElevator(false,
				UBinElevatorSystem.kBinElevatorUpSpeed));

		// Open claws at beginning
		addParallel(new UClawsOpen(UBinElevatorSystem.kBinGripOpenTime));

		// Let toteElevator fall flat
		addSequential(new UToteElevatorMove(0.5, 0.5));

		// Bring it down again
		addParallel(new UToteElevatorMove(-0.5, 0.5));

		// Lower arm for bin pickup
		addParallel(new UArmDown());

		// Lower raker for rake
		addSequential(new URakerMove(URakerSystem.kRakerDownSpeed, 1.25));

		// Drive towards bin
		addSequential(new ULinearDriveCommand(1.0, 1));

		// Slow down...
		addSequential(new ULinearDriveCommand(UPreferences.getPreferences()
				.get("InitialSlowDriveDistance", 1.0), 0.5));

		// Grab the bin
		addParallel(new UClawsClose(0.5, 2.0));

		// Drive over bin as it's grabbed
		addSequential(new ULinearDriveCommand(UPreferences.getPreferences()
				.get("SecondSlowDriveDistance", 1.0), 0.5));

		// Raise arm
		addParallel(new UArmUp(1, 1.35));

		// Raise raker
		addParallel(new URakerMove(URakerSystem.kRakerUpSpeed, 6.0));

		// Shake bins loose
		addSequential(new ULinearDriveCommand(UPreferences.getPreferences()
				.get("BackDriveShakeDistance", -0.1), 0.7));
		addSequential(new ULinearDriveCommand(UPreferences.getPreferences()
				.get("ForwardDriveShakeDistance", 0.2), 0.7));

		// addParallel(new RakerMove(RakerSystem.kRakerUpSpeed, 6.0));
		// for(int i = 0; i < 3; i++) {
		// addSequential(new LinearDriveCommand(-0.2, 0.7));
		// addSequential(new LinearDriveCommand(0.2, 0.7));
		// }
		// addParallel(new LinearDriveCommand(2.5, 0.5));
		// addParallel(new RakerMove(RakerSystem.kRakerUpSpeed, 6.0));
		// addSequential(new LinearDriveCommand(-0.1, 0.7));
		// addSequential(new LinearDriveCommand(0.5, 0.7));
	}
}
