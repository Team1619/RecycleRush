package org.usfirst.frc.team1619.robot;

import java.util.ArrayList;

import org.usfirst.frc.team1619.robot.subsystems.BinElevatorSystem;
import org.usfirst.frc.team1619.robot.subsystems.ConveyorSystem;
import org.usfirst.frc.team1619.robot.subsystems.StateMachineSystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class StateMachine {
	private State currentState = State.Idle;

	public static int numberTotes;
	private boolean incrementNumberTotes;
	
	private boolean toStopHumanFeed;
	public boolean getToStopHumanFeed() {
		return toStopHumanFeed;
	}
	
	private StateMachine() {
		numberTotes = 0;
		incrementNumberTotes = false;
		toStopHumanFeed = false;
	}
	
	public void init() {
		currentState = State.Abort;
		currentState.init(this);
		for(StateMachineSystem system : systems)
			system.init(currentState);
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
	
	public class Signal {
		private boolean hasRisen;
		
		public Signal() {
			hasRisen = false;
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

	private ArrayList<Signal> autoClearSignals = new ArrayList<Signal>(); 
	public class AutoClearSignal extends Signal {
		public AutoClearSignal() {
			autoClearSignals.add(this);
		}
	}

	public final Signal abortSignal = new AutoClearSignal();
	public final Signal resetSignal = new AutoClearSignal();
	public final Signal humanFeed_Stop = new AutoClearSignal();
	public final Signal humanFeed_Start = new AutoClearSignal();
	public final Signal humanFeed_RaiseTote = new AutoClearSignal();
	public final Signal humanFeed_WaitForTote = new AutoClearSignal();
	public final Signal humanFeed_ToteOnConveyor = new AutoClearSignal();
	public final Signal humanFeed_ThrottleConveyorDescend = new Signal();
	public final Signal humanFeed_EndCurrentStateAndDescend = new AutoClearSignal();
	public final Signal dropoffSignal = new AutoClearSignal();
	public final Signal groundFeedSignal = new AutoClearSignal(); 
	
//	public final Signal dropoffSignal = new AutoClearSignal();
//	public final Signal groundFeedSignal = new AutoClearSignal(); 
//	public final Signal groundFeedSignal_Descend = new AutoClearSignal();
	private final Timer stateTimer = new Timer();
	
	public enum State {
		Init {
			@Override
			protected void init(StateMachine sm) {
				numberTotes = 0;
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
				sm.toStopHumanFeed = false;
				numberTotes = 0;
			}
			
			@Override
			public State run(StateMachine sm) {
				if(sm.abortSignal.check()) {
					return Abort;
				}
				if(sm.humanFeed_Start.check()) {
					StateMachine.numberTotes = 0;
					
					if(BinElevatorSystem.getInstance().getTilterBackLimitSwitch()) {
						sm.humanFeed_ToteOnConveyor.clear();
						sm.humanFeed_ThrottleConveyorDescend.clear();
						return HumanFeed_RaiseTote;
					}
					else {
						return Idle;
					}
				}
				if(sm.humanFeed_EndCurrentStateAndDescend.check())
				{
					return TotePickup;
				}
				return this;
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
				if(sm.humanFeed_WaitForTote.check()) {
					return HumanFeed_WaitForTote;
				}
				if(sm.humanFeed_Stop.check()) {
					sm.toStopHumanFeed = true;
				}
				if(sm.humanFeed_EndCurrentStateAndDescend.check())
				{
					return HumanFeed_ThrottleConveyorAndDescend;
				}
				return this;
			}

			@Override
			public String toString() {
				return "HumanFeed_RaiseTote";
			}

			@Override
			protected void init(StateMachine sm) {
				sm.humanFeed_ThrottleConveyorDescend.clear();
			}
		},
		HumanFeed_WaitForTote {
			@Override
			protected void init(StateMachine sm) {
			}
			
			@Override
			public State run(StateMachine sm) {
				if(sm.toStopHumanFeed) {
					return Idle;
				}
				if(sm.abortSignal.check()) {
					return Abort;
				}
				if(ConveyorSystem.getInstance().getRearSensor() ||
						ConveyorSystem.getInstance().getFrontSensor() ||
						sm.humanFeed_ThrottleConveyorDescend.check()) {
					return HumanFeed_ToteOnConveyor;
				}
				if(sm.humanFeed_Stop.check()) {
					sm.toStopHumanFeed = true;
				}
				if(sm.humanFeed_EndCurrentStateAndDescend.check())
				{
					return HumanFeed_ThrottleConveyorAndDescend;
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
				if(sm.humanFeed_ThrottleConveyorDescend.check()) {
					return HumanFeed_ThrottleConveyorAndDescend;
				}
				if(sm.humanFeed_Stop.check()) {
					sm.toStopHumanFeed = true;
				}
				if(sm.humanFeed_EndCurrentStateAndDescend.check())
				{
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
				if(sm.humanFeed_RaiseTote.check()) {				
					if(sm.toStopHumanFeed) {
						return Idle;
					}
					return HumanFeed_RaiseTote;
				}
				if(sm.humanFeed_Stop.check()) {
					sm.toStopHumanFeed = true;
				}
				if(sm.humanFeed_EndCurrentStateAndDescend.check())
				{
					return HumanFeed_ThrottleConveyorAndDescend;
				}
				return this;
			}

			@Override
			public String toString() {
				return "HumanFeed_ThrottleConveyorDescend";
			}

			@Override
			protected void init(StateMachine sm) {
				StateMachine.numberTotes++;
				if(StateMachine.numberTotes == 6) {
					sm.toStopHumanFeed = true;
				}
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
		TotePickup {
			@Override
			protected void init(StateMachine sm) {
			}
			
			@Override
			public State run(StateMachine sm) {
				if(sm.abortSignal.check()) {
					return Abort;
				}
				if(sm.humanFeed_Start.check()) {
					StateMachine.numberTotes = 0;
					
					if(BinElevatorSystem.getInstance().getTilterFowardLimitSwitch()) {
						sm.humanFeed_ToteOnConveyor.clear();
						sm.humanFeed_ThrottleConveyorDescend.clear();
						return HumanFeed_RaiseTote;
					}
					else {
						return Idle;
					}
				}
				return Idle;
			}
			
			@Override
			public String toString() {
				return "Tote Pickup";
			}
		},
//		GroundFeed_Raise {
//			@Override
//			protected void init(StateMachine sm) {
//			}
//			
//			@Override
//			public State run(StateMachine sm) {
//				if(sm.abortSignal.check()) {
//					return Abort;
//				}
//				if(sm.groundFeedSignal_Descend.check()) {
//					return GroundFeed_Descend;
//				}
//				return this;
//			}
//
//			@Override
//			public String toString() {
//				return "GroundFeed_Raise";
//			}
//		},
//		GroundFeed_Descend {
//			@Override
//			public State run(StateMachine sm) {
//				if(sm.abortSignal.check()) {
//					return Abort;
//				}
//				return this;
//			}
//
//			@Override
//			public String toString() {
//				return "GroundFeed_Descend";
//			}
//
//			@Override
//			protected void init(StateMachine sm) {
//				
//			}
//		},
//		Dropoff {
//			@Override
//			protected void init(StateMachine sm) {
//			}
//			
//			@Override
//			public State run(StateMachine sm) {
//				if(sm.abortSignal.check()) {
//					return Abort;
//				}
//				return Idle;
//			}
//
//			@Override
//			public String toString() {
//				return "Dropoff";
//			}
//		},
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
				if(sm.humanFeed_Start.check()) {
					return HumanFeed_RaiseTote;
				}
				if(sm.humanFeed_Stop.check()) {
					return Idle;
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
			
//			System.out.println("Changed State to " + currentState.toString());
			
			currentState.init(this);
			stateTimer.reset();
			for(StateMachineSystem s : systems)
				s.init(currentState);
		}
		
		if (OI.getInstance().incrementNumberTotesButton.get()) {
			if (!incrementNumberTotes) {
				numberTotes = numberTotes--;
				numberTotes = (numberTotes < 0) ? 0 : numberTotes;
				incrementNumberTotes = true;
			}
		}
		else {
			incrementNumberTotes = false;
		}
		
		for(Signal sig : autoClearSignals) {
			sig.clear();
		}
	}
	
	public void display() {
		SmartDashboard.putNumber("Number of Totes", numberTotes);
		SmartDashboard.putString("CurrentState", currentState.toString());
	}
	
	public State getState() {
		return currentState;
	}
}
