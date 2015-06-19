package org.usfirst.frc.team1619.robot.auto;

import org.usfirst.frc.team1619.robot.commands.ULinearDriveCommand;
import org.usfirst.frc.team1619.robot.subsystems.UBinElevatorSystem;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class UGetOutTheWayAuto extends CommandGroup {

	public UGetOutTheWayAuto() {
		addParallel(new UMoveBinElevator(false,
				UBinElevatorSystem.BIN_ELEVATOR_UP_SPEED));
		addSequential(new ULinearDriveCommand(2.0, 0.5));
		// Add Commands here:
		// e.g. addSequential(new Command1());
		// addSequential(new Command2());
		// these will run in order.

		// To run multiple commands at the same time,
		// use addParallel()
		// e.g. addParallel(new Command1());
		// addSequential(new Command2());
		// Command1 and Command2 will run in parallel.

		// A command group will require all of the subsystems that each member
		// would require.
		// e.g. if Command1 requires chassis, and Command2 requires arm,
		// a CommandGroup containing them would require both the chassis and the
		// arm.
	}
}
