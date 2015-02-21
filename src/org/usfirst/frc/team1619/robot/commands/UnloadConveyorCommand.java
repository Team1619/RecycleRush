package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.subsystems.BinLiftSystem;
import org.usfirst.frc.team1619.robot.subsystems.Conveyor;
import org.usfirst.frc.team1619.robot.subsystems.GuardRailSystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class UnloadConveyorCommand extends Command {
	private static final double kForwardConveyorSpeed = 0.75;
	private static final double kReverseConveyorSpeed = -1.0;
	private static final double kCloseGuardRailSpeed = -0.25;
	private static final double kOpenGuardRailSpeed = 0.15;
	private static final double kBinLiftUpSpeed = 1.0;
	private static final double kBinLiftDownSpeed = -1.0;

	private GuardRailSystem guardRailSystem;
	private Conveyor conveyor;
	private State currentState;	
	private BinLiftSystem binLiftSystem;
	
    public UnloadConveyorCommand() {
    	guardRailSystem = GuardRailSystem.getInstance();
    	requires(guardRailSystem);
    	conveyor = Conveyor.getInstance();
    	requires(conveyor);
    	binLiftSystem = BinLiftSystem.getInstance();
    	requires(binLiftSystem);
    }
   
    Timer stateTimeoutTimer = new Timer();
    Timer retractTimer = new Timer();
    Timer finishTimer = new Timer();
    
    enum State {
    	RunTilBroken(3.0) {
			@Override
			State run(UnloadConveyorCommand cmd) {
				cmd.conveyor.moveConveyor(kForwardConveyorSpeed);
				cmd.guardRailSystem.setGuardRailSpeed(kCloseGuardRailSpeed);
				
				if(cmd.conveyor.getFrontSensor())
					return SensorBroken;
				else
					return this;
			}
			
			public String toString() {
				return "RunTilBroken";
			}
		}, 
		SensorBroken(3.0) {
			@Override
			State run(UnloadConveyorCommand cmd) {
				cmd.conveyor.moveConveyor(kForwardConveyorSpeed);
				cmd.guardRailSystem.setGuardRailSpeed(kCloseGuardRailSpeed);
				cmd.binLiftSystem.setBinElevatorSpeed(kBinLiftUpSpeed);
				
				if(cmd.conveyor.getFrontSensor())
					return SensorBroken;
				else
					return Retract;
					
			}
			
			public String toString() {
				return "SensorBroken";
			}
		}, 
		Retract(3.0) {
			@Override
			State run(UnloadConveyorCommand cmd) {
				cmd.conveyor.moveConveyor(kReverseConveyorSpeed);
				cmd.guardRailSystem.setGuardRailSpeed(kOpenGuardRailSpeed);
				
				if (cmd.retractTimer.get() > 0.5) {
					return Finish;
				}
				else {
					return Retract;
				}
			}
						
			@Override
			void init(UnloadConveyorCommand cmd) {
				cmd.retractTimer.stop();
				cmd.retractTimer.reset();
				cmd.retractTimer.start();
			}

			public String toString() {
				return "Retract";
			}
		},
		Finish(-1) {
			@Override
			State run(UnloadConveyorCommand cmd) {
				cmd.conveyor.moveConveyor(0);
				cmd.guardRailSystem.setGuardRailSpeed(kOpenGuardRailSpeed);
				cmd.binLiftSystem.setBinElevatorSpeed(kBinLiftDownSpeed);
				return this;
			}
			@Override
			void init(UnloadConveyorCommand cmd) {
				cmd.finishTimer.stop();
				cmd.finishTimer.reset();
				cmd.finishTimer.start();
			}
			@Override
			boolean isFinished(UnloadConveyorCommand cmd){
	    		if (super.isFinished(cmd)){
	    			return true;
	    		}
	    		return cmd.finishTimer.get() > 0.5;
	    	}
			public String toString() {
				return "Finish";
			}

		};
    	
		double timeoutValue;
		State(double timeoutValue){
			this.timeoutValue = timeoutValue;
		}
		
		State runState(UnloadConveyorCommand cmd) {
			if(timeoutValue < 0 || cmd.stateTimeoutTimer.get() < timeoutValue) {
				return run(cmd);
			}	
			else {
				return Finish;
			}
		}
		

		abstract State run(UnloadConveyorCommand cmd);
    	
    	void init(UnloadConveyorCommand cmd) {
    		cmd.stateTimeoutTimer.stop();
    		cmd.stateTimeoutTimer.reset();
    		cmd.stateTimeoutTimer.start();
    		
    		System.out.println("state: " + this);
    	}
    	
    	boolean isFinished(UnloadConveyorCommand cmd){
    		return false;
    	}
    	
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	currentState = State.RunTilBroken;
    	currentState.init(this);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	State nextState = currentState.runState(this);
    	if (nextState != currentState){
    		System.out.println("Going from state '" + currentState + "' to '" + nextState + "'");
    		currentState = nextState;
    		currentState.init(this);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return currentState.isFinished(this);
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
