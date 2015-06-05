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
	
	private State fCurrentState = State.Idle;
	private boolean fIncrementNumberTotes;
	private boolean fToStopHumanFeed;
	private ArrayList<UStateMachineSystem> fSystems = new ArrayList<>();
	
	//Creates singleton stateMachine
	private static UStateMachine sStateMachine = null;
	
	public int fNumberTotes;
	
	public static UStateMachine getInstance() {
		if(sStateMachine == null) {
			sStateMachine = new UStateMachine();
			sStateMachine.stateTimer.start();
		}
		return sStateMachine;
	}
	
	public boolean getToStopHumanFeed() {
		return fToStopHumanFeed;
	}
	
	private UStateMachine() {
		fNumberTotes = 0;
		fIncrementNumberTotes = false;
		fToStopHumanFeed = false;
	}
	
	public void init() {
		stateTimer.reset();
		fCurrentState = State.Init;
		fCurrentState.init(this);
		fNumberTotes = 0;
		for(UStateMachineSystem system : fSystems)
			system.init(fCurrentState);
	}
	
	public void addSystem(UStateMachineSystem sms) {
		fSystems.add(sms);
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
	
	private final Timer stateTimer = new Timer();
	
	public enum State {
		Init {
			@Override
			protected void init(UStateMachine sm) {
				sm.fNumberTotes = 0;
			}
			
			@Override
			public State run(UStateMachine sm) {
				boolean finished = true;
				for(UStateMachineSystem s : sm.fSystems) {
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
				sm.fToStopHumanFeed = false;
				sm.fNumberTotes = 0;
			}
			
			@Override
			public State run(UStateMachine sm) {
				if(sm.abortSignal.check()) {
					return Abort;
				}
				if(sm.humanFeed_Start.check()) {
					UStateMachine.getInstance().fNumberTotes = 0;
					
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
					sm.fToStopHumanFeed = true;
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
				if(sm.fToStopHumanFeed) {
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
					sm.fToStopHumanFeed = true;
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
					sm.fToStopHumanFeed = true;
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
					sm.fToStopHumanFeed = true;
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
					if(sm.fToStopHumanFeed) {
						return Idle;
					}
					return HumanFeed_RaiseTote;
				}
				if(sm.humanFeed_Stop.check()) {
					sm.fToStopHumanFeed = true;
				}
				if(sm.humanFeed_EndCurrentStateAndDescend.check())
				{
					return HumanFeed_ThrottleConveyorAndDescend;
				}
				return this;
			}
			@Override
			protected void init(UStateMachine sm) {
				UStateMachine.getInstance().fNumberTotes++;
				if(UStateMachine.getInstance().fNumberTotes == 6) {
					sm.fToStopHumanFeed = true;
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
		for(UStateMachineSystem sms: fSystems) {
			sms.superSecretSpecialSatanRun(fCurrentState, elapsed);
		}

		State nextState = fCurrentState.run(this);
		if(fCurrentState != nextState) {
			fCurrentState = nextState;
			
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS'Z'");
			date.setTimeZone(TimeZone.getTimeZone("UTC"));
//			System.out.println(date.format(new Date()) + " -- Changed State to " + currentState.name());
			
			
			fCurrentState.init(this);
			stateTimer.reset();
			for(UStateMachineSystem s : fSystems)
				s.init(fCurrentState);
		}
		
		if (UOI.getInstance().incrementNumberTotesButton.get()) {
			if (!fIncrementNumberTotes) {
				fNumberTotes = fNumberTotes--;
				fNumberTotes = fNumberTotes < 0 ? 0 : fNumberTotes;
				fIncrementNumberTotes = true;
			}
		}
		else {
			fIncrementNumberTotes = false;
		}
		
		for(Signal sig : autoClearSignals) {
			sig.clear();
		}
	}
	
	public void display() {
		SmartDashboard.putNumber("Number of Totes", fNumberTotes);
		SmartDashboard.putString("CurrentState", fCurrentState.toString());
	}
	
	public State getState() {
		return fCurrentState;
	}
}
