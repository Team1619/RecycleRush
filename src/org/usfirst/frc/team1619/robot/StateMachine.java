package org.usfirst.frc.team1619.robot;

import java.util.ArrayList;

import org.usfirst.frc.team1619.robot.subsystems.StateMachineSystem;

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
			return /*Jesus*/hasRisen;
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
	
	
	public enum State {
		Init {
			@Override
			public State run(StateMachine sm) {
				return Idle;
			}

			@Override
			public String toString() {
				return "Init";
			}
			
		},
		Idle {

			@Override
			public State run(StateMachine sm) {
				return Idle;
			}

			@Override
			public String toString() {
				return "Idle";
			}
			
		},
		HumanFeed {

			@Override
			public State run(StateMachine sm) {
				return Idle;
			}

			@Override
			public String toString() {
				return "Human Feed";
			}
			
		},
		GroundFeed {

			@Override
			public State run(StateMachine sm) {
				return Idle;
			}

			@Override
			public String toString() {
				return "Ground Feed";
			}
			
		},
		Dropoff {

			@Override
			public State run(StateMachine sm) {
				return Idle;
			}

			@Override
			public String toString() {
				return "Dropoff";
			}
			
		},
		BinPickup {

			@Override
			public State run(StateMachine sm) {
				return Idle;
			}

			@Override
			public String toString() {
				return "Bin Pickup";
			}
			
		},
		Abort {

			@Override
			public State run(StateMachine sm) {
				return Idle;
			}

			@Override
			public String toString() {
				return "Abort";
			}
			
		};
		
		public abstract State run(StateMachine sm);
		public abstract String toString();
	}
	
	public void run() {
		for(StateMachineSystem sms: systems) {
			sms.superSecretSpecialSatanRun(currentState);
		}
		currentState = currentState.run(this);
	}
}
