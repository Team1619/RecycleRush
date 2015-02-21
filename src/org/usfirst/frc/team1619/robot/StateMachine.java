package org.usfirst.frc.team1619.robot;

import java.util.ArrayList;

import org.usfirst.frc.team1619.robot.subsystems.BinElevatorSystem;
import org.usfirst.frc.team1619.robot.subsystems.StateMachineSystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class StateMachine {
	private State currentState = State.Init;
	
	private StateMachine() {	
	}
	
	private static StateMachine stateMachine;
	public static StateMachine getInstance() {
		if(stateMachine == null) {
			stateMachine = new StateMachine();
		}
		return stateMachine;
	}
	
	private ArrayList<StateMachineSystem> systems = new ArrayList<StateMachineSystem>();
	public void addSystem(StateMachineSystem sms) {
		systems.add(sms);
	}
	
	private ArrayList<Signal> signals = new ArrayList<Signal>(); 
	public class Signal {
		private boolean hasRisen;
		
		public Signal() {
			hasRisen = false;
			signals.add(this);
		}
		
		public boolean check() {
			return /*Our Lord and Savior Jesus*/hasRisen;	// "Hit home to Jerusalem" - Daniel
		}
		
		public void clear() {
			hasRisen = false;
		}
		
		public void raise() {
			hasRisen = true;
		}
	}

	public final Signal abortSignal = new Signal();
	public final Signal resetSignal = new Signal();
	public final Signal humanPlayerFeedSignal = new Signal();
	public final Signal dropoffSignal = new Signal();
	public final Signal groundFeedSignal = new Signal(); 
	
	public final Timer humanFeedTimer = new Timer();
	
	public enum State {
		Init {
			@Override
			protected void init(StateMachine sm) {
			}
			
			@Override
			public State run(StateMachine sm) {
				if(BinElevatorSystem.getInstance().getBinElevatorPosition() == 0.0) {
					return Idle;
				}
				return Init;
			}

			@Override
			public String toString() {
				return "Init";
			}
		},
		Idle {
			@Override
			protected void init(StateMachine sm) {
			}
			
			@Override
			public State run(StateMachine sm) {
				if(sm.abortSignal.check()) {
					return Abort;
				}
				return Idle;
			}

			@Override
			public String toString() {
				return "Idle";
			}
		},
		HumanFeed {
			@Override
			protected void init(StateMachine sm) {
				sm.humanFeedTimer.stop();
				sm.humanFeedTimer.reset();
				sm.humanFeedTimer.start();
			}
			
			@Override
			public State run(StateMachine sm) {
				if(sm.abortSignal.check()) {
					return Abort;
				}
				return Idle;
			}

			@Override
			public String toString() {
				return null;
			}
		},
		GroundFeed {
			@Override
			protected void init(StateMachine sm) {
			}
			
			@Override
			public State run(StateMachine sm) {
				if(sm.abortSignal.check()) {
					return Abort;
				}
				return Idle;
			}

			@Override
			public String toString() {
				return "Ground Feed";
			}
		},
		Dropoff {
			@Override
			protected void init(StateMachine sm) {
			}
			
			@Override
			public State run(StateMachine sm) {
				if(sm.abortSignal.check()) {
					return Abort;
				}
				return Idle;
			}

			@Override
			public String toString() {
				return "Dropoff";
			}
		},
		BinPickup {
			@Override
			protected void init(StateMachine sm) {				
			}
			
			@Override
			public State run(StateMachine sm) {
				if(sm.abortSignal.check()) {
					return Abort;
				}
				return Idle;
			}

			@Override
			public String toString() {
				return "Bin Pickup";
			}
		},
		Abort {
			@Override
			protected void init(StateMachine sm) {
			}
			
			@Override
			public State run(StateMachine sm) {
				if(sm.abortSignal.check()) {
					return Idle;
				}
				if(sm.resetSignal.check()) {
					return Init;
				}
				return Abort;
			}

			@Override
			public String toString() {
				return "Abort";
			}			
		};
		
		public abstract State run(StateMachine sm);
		public abstract String toString();
		
		protected abstract void init(StateMachine sm);
	}
	
	public void run() {
		SmartDashboard.putString("CurrentState", currentState.toString());
		
		for(StateMachineSystem sms: systems) {
			sms.superSecretSpecialSatanRun(currentState);
		}

		State nextState = currentState.run(this);
		if(currentState != nextState) {
			currentState = nextState;
			currentState.init(this);
		}
		
		for(Signal sig : signals) {
			sig.clear();
		}
	}
}
