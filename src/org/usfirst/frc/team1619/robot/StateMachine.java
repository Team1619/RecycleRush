package org.usfirst.frc.team1619.robot;

import java.util.ArrayList;

import org.usfirst.frc.team1619.robot.subsystems.StateMachineSystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class StateMachine {
	private State currentState = State.Init;
	//private State currentState = State.Idle;
	
	private int numberTotes;
	
	private StateMachine() {
		numberTotes = 0;
	}
	
	private static StateMachine stateMachine;
	public static StateMachine getInstance() {
		if(stateMachine == null) {
			stateMachine = new StateMachine();
			stateMachine.stateTimer.start();
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
	public final Signal humanPlayerFeed_Start = new Signal();
	public final Signal humanPlayerFeed_RaiseTote = new Signal();
	public final Signal humanPlayerFeed_WaitForTote = new Signal();
	public final Signal humanPlayerFeed_ToteOnConveyor = new Signal();
	public final Signal humanPlayerFeed_ThrottleConveyorDescend = new Signal();
	public final Signal dropoffSignal = new Signal();
	public final Signal groundFeedSignal = new Signal(); 
	
	private final Timer stateTimer = new Timer();
	
	public enum State {
		Init {
			@Override
			protected void init(StateMachine sm) {
			}
			
			@Override
			public State run(StateMachine sm) {
				boolean finished = true;
				for(StateMachineSystem s : sm.systems) {
					if(s.initFinished())
						continue;
					else {
						finished = false;
						break;
					}
				}
				if(finished)
					return Idle;
				else
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
				if(sm.humanPlayerFeed_Start.check()) {
					sm.numberTotes = 0;
					return HumanFeed_RaiseTote;
				}
				return Idle;
			}

			@Override
			public String toString() {
				return "Idle";
			}
		},
		HumanFeed_RaiseTote {
			@Override
			public State run(StateMachine sm) {
				if(sm.abortSignal.check()) {
					return Abort;
				}
				if(sm.humanPlayerFeed_WaitForTote.check()) {
					return HumanFeed_WaitForTote;
				}
				if(sm.humanPlayerFeed_ToteOnConveyor.check()) {
					return HumanFeed_ToteOnConveyor; 
				}
				return this;
			}

			@Override
			public String toString() {
				return "HumanFeed_RaiseTote";
			}

			@Override
			protected void init(StateMachine sm) {
			}
			
		},
		HumanFeed_WaitForTote {
			@Override
			protected void init(StateMachine sm) {
				
			}
			
			@Override
			public State run(StateMachine sm) {
				if(sm.abortSignal.check()) {
					return Abort;
				}
				if(sm.humanPlayerFeed_ToteOnConveyor.check()) {
					return HumanFeed_ToteOnConveyor;
				}
				return this;
			}

			@Override
			public String toString() {
				return "HumanFeed_WaitForTote";
			}
		},
		HumanFeed_ToteOnConveyor {
			@Override
			public State run(StateMachine sm) {
				if(sm.abortSignal.check()) {
					return Abort;
				}
				if(sm.humanPlayerFeed_ThrottleConveyorDescend.check()) {
					return HumanFeed_ThrottleConveyorAndDescend;
				}
				return this;
			}

			@Override
			public String toString() { 
				return "HumanFeed_ToteOnConveyor";
			}

			@Override
			protected void init(StateMachine sm) {
			}
		},
		HumanFeed_ThrottleConveyorAndDescend {
			@Override
			public State run(StateMachine sm) {
				if(sm.abortSignal.check()) {
					return Abort;
				}
				if(sm.humanPlayerFeed_RaiseTote.check()) {
					sm.numberTotes++;
					if(sm.numberTotes == 5) {
						return Idle;
					}
					return HumanFeed_RaiseTote;
				}
				return this;
			}

			@Override
			public String toString() {
				return "HumanFeed_ThrottleConveyorDescend";
			}

			@Override
			protected void init(StateMachine sm) {
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
		double elapsed = stateTimer.get();
		for(StateMachineSystem sms: systems) {
			sms.superSecretSpecialSatanRun(currentState, elapsed);
		}

		State nextState = currentState.run(this);
		if(currentState != nextState) {
			currentState = nextState;
			
			System.out.println("Changed State to " + currentState.toString());
			
			currentState.init(this);
			stateTimer.reset();
			for(StateMachineSystem s : systems)
				s.init(currentState);
		}
		
		for(Signal sig : signals) {
			sig.clear();
		}
	}
	
	public void display() {
		SmartDashboard.putNumber("Number of Totes", numberTotes);
		SmartDashboard.putString("CurrentState", currentState.toString());
	}
}
