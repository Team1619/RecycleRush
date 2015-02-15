package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.subsystems.Conveyor;
import org.usfirst.frc.team1619.robot.subsystems.GuardRailSystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class UnloadConveyorCommand extends Command {

	private GuardRailSystem guardRailSystem;
	private Conveyor conveyor;
	private State currentState;	
	
    public UnloadConveyorCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	guardRailSystem = GuardRailSystem.getInstance();
    	requires(guardRailSystem);
    	conveyor = Conveyor.getInstance();
    	requires(conveyor);
    	currentState = State.RunTilBroken;
    }
   
    Timer stateTimeoutTimer = new Timer();
    
    enum State {
    	RunTilBroken(3.0) {
			@Override
			State run(UnloadConveyorCommand cmd) {
				// TODO Auto-generated method stub
				return null;
			}
		}, 
		SensorBroken(3.0) {
			@Override
			State run(UnloadConveyorCommand cmd) {
				// TODO Auto-generated method stub
				return null;
			}
		}, 
		Retract(3.0) {
			@Override
			State run(UnloadConveyorCommand cmd) {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			boolean isFinished(UnloadConveyorCommand cmd){
	    		if (super.isFinished(cmd)){
	    			return true;
	    		}
	    		return false;
	    	}
		};
    	
		double timeoutValue;
		State(double timeoutValue){
			this.timeoutValue = timeoutValue;
		}
		
		abstract State run(UnloadConveyorCommand cmd);
    	
    	void init(UnloadConveyorCommand cmd){
    		cmd.stateTimeoutTimer.stop();
    		cmd.stateTimeoutTimer.reset();
    		cmd.stateTimeoutTimer.start();
    	}
    	
    	boolean isFinished(UnloadConveyorCommand cmd){
    		return cmd.stateTimeoutTimer.get() >= timeoutValue;
    	}
    	
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	State nextState = currentState.run(this);
    	if (nextState != currentState){
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
