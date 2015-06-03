package org.usfirst.frc.team1619.robot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import org.usfirst.frc.team1619.robot.subsystems.UBinElevatorSystem;
import org.usfirst.frc.team1619.robot.subsystems.UConveyorSystem;
import org.usfirst.frc.team1619.robot.subsystems.UStateMachineSystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class UStateMachine {
	private State currentState = State.Idle;

	public int numberTotes;
	private boolean incrementNumberTotes;
	
	private boolean toStopHumanFeed;
	public boolean getToStopHumanFeed() {
		return toStopHumanFeed;
	}
	
	private UStateMachine() {
		numberTotes = 0;
		incrementNumberTotes = false;
		toStopHumanFeed = false;
	}
	
	public void init() {
		stateTimer.reset();
		currentState = State.Init;
		currentState.init(this);
		numberTotes = 0;
		for(UStateMachineSystem system : systems)
			system.init(currentState);
	}
	
	private static UStateMachine stateMachine;
	public static UStateMachine getInstance() {
		if(stateMachine == null) {
			stateMachine = new UStateMachine();
			stateMachine.stateTimer.start();
		}
		return stateMachine;
	}
	
	private ArrayList<UStateMachineSystem> systems = new ArrayList<UStateMachineSystem>();
	public void addSystem(UStateMachineSystem sms) {
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

	private ArrayList<Signal> autoClearSignals = new ArrayList<>(); 
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
	public final Signal humanFeed_ThrottleConveyorBack = new AutoClearSignal();
	public final Signal humanFeed_ThrottleConveyorDescend = new Signal();
	public final Signal humanFeed_EndCurrentStateAndDescend = new AutoClearSignal();
	
//	public final Signal dropoffSignal = new AutoClearSignal();
//	public final Signal groundFeedSignal = new AutoClearSignal(); 
//	public final Signal groundFeedSignal_Descend = new AutoClearSignal();
	private final Timer stateTimer = new Timer();
	
	public enum State {
		Init {
			@Override
			protected void init(UStateMachine sm) {
				sm.numberTotes = 0;
			}
			
			@Override
			public State run(UStateMachine sm) {
				boolean finished = true;
				for(UStateMachineSystem s : sm.systems) {
					if(s.initFinished())
						continue;
					else {
						finished = false;
						break;
					}
				}
				if(finished)
					return Idle;
				else if (sm.abortSignal.check() || (sm.stateTimer.get() > 4))
					return Abort;
				else
					return Init;
			}
		},
		Idle {
			@Override
			protected void init(UStateMachine sm) {
				sm.toStopHumanFeed = false;
				sm.numberTotes = 0;
			}
			
			@Override
			public State run(UStateMachine sm) {
				if(sm.abortSignal.check()) {
					return Abort;
				}
				if(sm.humanFeed_Start.check()) {
					UStateMachine.getInstance().numberTotes = 0;
					
					if(UBinElevatorSystem.getInstance().getTilterBackLimitSwitch()) {
						sm.humanFeed_ToteOnConveyor.clear();
						sm.humanFeed_ThrottleConveyorDescend.clear();
						return HumanFeed_RaiseTote;
					}
					else {
						return TiltUp;
					}
				}
				return this;
			}
		},
		HumanFeed_RaiseTote {
			@Override
			public State run(UStateMachine sm) {
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
			protected void init(UStateMachine sm) {
				sm.humanFeed_ThrottleConveyorDescend.clear();
			}
		},
		HumanFeed_WaitForTote {
			@Override
			protected void init(UStateMachine sm) {
			}
			
			@Override
			public State run(UStateMachine sm) {
				if(sm.toStopHumanFeed) {
					return Idle;
				}
				if(sm.abortSignal.check()) {
					return Abort;
				}
				if(UConveyorSystem.getInstance().getRearSensor() ||
						UConveyorSystem.getInstance().getFrontSensor() ||
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
		},
		HumanFeed_ToteOnConveyor {
			@Override
			public State run(UStateMachine sm) {
				if(sm.abortSignal.check()) {
					return Abort;
				}
				if(sm.humanFeed_ThrottleConveyorDescend.check()) {
					return HumanFeed_ThrottleConveyorAndDescend;
				}
				if(sm.humanFeed_Stop.check()) {
					sm.toStopHumanFeed = true;
				}
				if(sm.humanFeed_EndCurrentStateAndDescend.check()) {
					return HumanFeed_ThrottleConveyorAndDescend;
				}
				if(sm.humanFeed_ThrottleConveyorBack.check()) {
					return HumanFeed_ThrottleConveyorBack;
				}
				return this;
			}
			@Override
			protected void init(UStateMachine sm) {
			}
		},
		HumanFeed_ThrottleConveyorBack {
			@Override
			public State run(UStateMachine sm) {
				if(sm.abortSignal.check()) {
					return Abort;
				}
				if(sm.humanFeed_ThrottleConveyorDescend.check()) {
					return HumanFeed_ThrottleConveyorAndDescend;
				}
				if(sm.humanFeed_Stop.check()) {
					sm.toStopHumanFeed = true;
				}
				if(sm.humanFeed_EndCurrentStateAndDescend.check()) {
					return HumanFeed_ThrottleConveyorAndDescend;
				}
				return this;
			}
			@Override
			protected void init(UStateMachine sm) {
			}
		},
		HumanFeed_ThrottleConveyorAndDescend {
			@Override
			public State run(UStateMachine sm) {
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
			protected void init(UStateMachine sm) {
				UStateMachine.getInstance().numberTotes++;
				if(UStateMachine.getInstance().numberTotes == 6) {
					sm.toStopHumanFeed = true;
				}
			}
		},
		TiltUp {
			@Override
			public State run(UStateMachine sm) {
				if(sm.abortSignal.check()) {
					return Abort;
				}
				if(UBinElevatorSystem.getInstance().getTilterBackLimitSwitch()) {
					return HumanFeed_RaiseTote;
				}
				return this;
			}
			@Override
			protected void init(UStateMachine sm) {
			}
		},
		Abort {
			@Override
			protected void init(UStateMachine sm) {
			}
			
			@Override
			public State run(UStateMachine sm) {
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
		};
		
		public abstract State run(UStateMachine sm);
		
		@Override
		public String toString() {
			return this.name();
		}
		
		protected abstract void init(UStateMachine sm);
	}
	
	public void run() {
		double elapsed = stateTimer.get();
		for(UStateMachineSystem sms: systems) {
			sms.superSecretSpecialSatanRun(currentState, elapsed);
		}

		State nextState = currentState.run(this);
		if(currentState != nextState) {
			currentState = nextState;
			
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS'Z'");
			date.setTimeZone(TimeZone.getTimeZone("UTC"));
//			System.out.println(date.format(new Date()) + " -- Changed State to " + currentState.name());
			
			
			currentState.init(this);
			stateTimer.reset();
			for(UStateMachineSystem s : systems)
				s.init(currentState);
		}
		
		if (UOI.getInstance().incrementNumberTotesButton.get()) {
			if (!incrementNumberTotes) {
				numberTotes = numberTotes--;
				numberTotes = numberTotes < 0 ? 0 : numberTotes;
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
