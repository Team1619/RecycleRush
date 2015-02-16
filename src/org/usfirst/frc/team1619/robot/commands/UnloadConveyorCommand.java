package org.usfirst.frc.team1619.robot.commands;

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
	
	private GuardRailSystem guardRailSystem;
	private Conveyor conveyor;
	private State currentState;	
	
    public UnloadConveyorCommand() {
    	guardRailSystem = GuardRailSystem.getInstance();
    	requires(guardRailSystem);
    	conveyor = Conveyor.getInstance();
    	requires(conveyor);
    }
   
    Timer stateTimeoutTimer = new Timer();
    Timer retractTimer = new Timer();
    
    enum State {
    	RunTilBroken(3.0) {
			@Override
			State run(UnloadConveyorCommand cmd) {
				cmd.conveyor.moveConveyor(kForwardConveyorSpeed);
				
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
				return this;
			}
			@Override
			boolean isFinished(UnloadConveyorCommand cmd){
	    		if (super.isFinished(cmd)){
	    			return true;
	    		}
	    		
	    		return cmd.retractTimer.get() > 0.5;
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
		};
    	
		double timeoutValue;
		State(double timeoutValue){
			this.timeoutValue = timeoutValue;
		}
		
		abstract State run(UnloadConveyorCommand cmd);
    	
    	void init(UnloadConveyorCommand cmd) {
    		cmd.stateTimeoutTimer.stop();
    		cmd.stateTimeoutTimer.reset();
    		cmd.stateTimeoutTimer.start();
    		
    		System.out.println("state: " + this);
    	}
    	
    	boolean isFinished(UnloadConveyorCommand cmd){
    		return cmd.stateTimeoutTimer.get() >= timeoutValue;
    	}
    	
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	currentState = State.RunTilBroken;
    	currentState.init(this);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	State nextState = currentState.run(this);
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
