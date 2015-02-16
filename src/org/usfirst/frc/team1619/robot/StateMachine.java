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
	
	enum State {
		Init {

			@Override
			public State run(StateMachine sm) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String toString() {
				// TODO Auto-generated method stub
				return null;
			}
			
		},
		Idle {

			@Override
			public State run(StateMachine sm) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String toString() {
				// TODO Auto-generated method stub
				return null;
			}
			
		},
		HumanFeed {

			@Override
			public State run(StateMachine sm) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String toString() {
				// TODO Auto-generated method stub
				return null;
			}
			
		},
		GroundFeed {

			@Override
			public State run(StateMachine sm) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String toString() {
				// TODO Auto-generated method stub
				return null;
			}
			
		},
		Dropoff {

			@Override
			public State run(StateMachine sm) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String toString() {
				// TODO Auto-generated method stub
				return null;
			}
			
		},
		BinPickup {

			@Override
			public State run(StateMachine sm) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String toString() {
				// TODO Auto-generated method stub
				return null;
			}
			
		},
		Abort {

			@Override
			public State run(StateMachine sm) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String toString() {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
		
		public abstract State run(StateMachine sm);
		public abstract String toString();
	}
	
	public void run() {
		State nextState = currentState;
		
		
	}
}
