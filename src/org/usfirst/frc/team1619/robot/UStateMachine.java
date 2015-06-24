package org.usfirst.frc.team1619.robot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team1619.robot.subsystems.UBinElevatorSystem;
import org.usfirst.frc.team1619.robot.subsystems.UConveyorSystem;
import org.usfirst.frc.team1619.robot.subsystems.UStateMachineSystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class UStateMachine {

	/**
	 * A boolean flag.
	 */
	public static class Signal {
		private boolean hasRisen;

		public Signal() {
			hasRisen = false;
		}

		/**
		 * Returns the state of the signal.
		 * 
		 * @return The boolean state of the signal.
		 */
		public boolean check() {
			return /* Our Lord and Savior Jesus */hasRisen; // "Hit home to Jerusalem"
			// - Daniel
		}

		/**
		 * Sets the state of the signal to false.
		 */
		public void clear() {
			hasRisen = false;
		}

		/**
		 * Sets the state of the signal to true.
		 */
		public void raise() {
			hasRisen = true;
		}
	}

	/**
	 * A class for all auto-clearing signals. Whenever the constructor is called
	 * the class instance is added to a list of AutoClearSignals.
	 */
	public static class AutoClearSignal extends Signal {
		public AutoClearSignal() {
			sStateMachine.fAutoClearSignals.add(this);
		}
	}

	public enum SignalName {
		ABORT, RESET, HUMAN_FEED_STOP, HUMAN_FEED_START, HUMAN_FEED_RAISE_TOTE, HUMAN_FEED_WAIT_FOR_TOTE, HUMAN_FEED_TOTE_ON_CONVEYOR, HUMAN_FEED_THROTTLE_CONVEYOR_BACK, HUMAN_FEED_THROTTLE_CONVEYOR_DESCEND, HUMAN_FEED_END_CURRENT_STATE_AND_DESCEND
	}
	
	// Singleton stateMachine
	private static UStateMachine sStateMachine = new UStateMachine();

	private State fCurrentState = State.Idle;
	private boolean fIncrementNumberTotes;
	private boolean fToStopHumanFeed;
	private int fNumberTotes;
	private ArrayList<UStateMachineSystem> fSystems = new ArrayList<>();
	private ArrayList<Signal> fAutoClearSignals = new ArrayList<>();
	private Map<SignalName, Signal> fSignalMap = new HashMap<>();

	private final Timer fStateTimer = new Timer();

	private UStateMachine() {
		fNumberTotes = 0;
		fIncrementNumberTotes = false;
		fToStopHumanFeed = false;
		for (SignalName name : SignalName.values()) {
			fSignalMap.put(name, new AutoClearSignal());
		}
		fSignalMap.put(SignalName.HUMAN_FEED_THROTTLE_CONVEYOR_DESCEND, new Signal());
	}

	/**
	 * Gets the fToStopHumanFeed variable.
	 * 
	 * @return If human feed should be stopped.
	 */
	public static boolean getToStopHumanFeed() {
		return sStateMachine.fToStopHumanFeed;
	}
	
	public static Signal getSignal(SignalName name) {
		return sStateMachine.fSignalMap.get(name);
	}

	/**
	 * Gets the current number of totes in the stacker.
	 * 
	 * @return The current number of totes
	 */
	public static int getNumberTotes() {
		return sStateMachine.fNumberTotes;
	}

	/**
	 * Gets the current state of the state machine.
	 * 
	 * @return The current state of the state machine
	 */
	public static State getState() {
		return sStateMachine.fCurrentState;
	}

	/**
	 * Initializes the state machine. Resets the state timer, sets the current
	 * state to INIT, sets tote count to zero, and calls the init functions of
	 * all of the subsystems.
	 */
	public static void init() {
		sStateMachine.fStateTimer.reset();
		sStateMachine.fCurrentState = State.Init;
		sStateMachine.fCurrentState.init();
		sStateMachine.fNumberTotes = 0;
		for (UStateMachineSystem system : sStateMachine.fSystems)
			system.init(sStateMachine.fCurrentState);
	}

	/**
	 * Adds a subsystem to an array of subsystems.
	 * 
	 * @param sms The state machine system
	 */
	public static void addSystem(UStateMachineSystem sms) {
		sStateMachine.fSystems.add(sms);
	}

	public static enum State {
		/**
		 * The state in which all initialization occurs. Should be called first.
		 */
		Init {
			@Override
			protected void init() {
				sStateMachine.fNumberTotes = 0;
			}

			@Override
			public State run() {
				boolean finished = true;
				for (UStateMachineSystem s : sStateMachine.fSystems) {
					if (s.initFinished())
						continue;
					else {
						finished = false;
						break;
					}
				}
				if (finished)
					return Idle;
				else if (getSignal(SignalName.ABORT).check() || (sStateMachine.fStateTimer.get() > 4))
					return Abort;
				else
					return Init;
			}
		},

		/**
		 * The robot is not actively performing any actions. The default state.
		 */
		Idle {
			@Override
			protected void init() {
				sStateMachine.fToStopHumanFeed = false;
				sStateMachine.fNumberTotes = 0;
			}

			@Override
			public State run() {
				if (getSignal(SignalName.ABORT).check()) {
					return Abort;
				}
				if (getSignal(SignalName.HUMAN_FEED_START).check()) {
					sStateMachine.fNumberTotes = 0;

					if (UBinElevatorSystem.getInstance().getTilterBackLimitSwitch()) {
						getSignal(SignalName.HUMAN_FEED_TOTE_ON_CONVEYOR).clear();
						getSignal(SignalName.HUMAN_FEED_THROTTLE_CONVEYOR_DESCEND).clear();
						return HumanFeed_RaiseTote;
					}
					else {
						return TiltUp;
					}
				}
				return this;
			}
		},

		/**
		 * Raise the tote elevator.
		 */
		HumanFeed_RaiseTote {
			@Override
			public State run() {
				if (getSignal(SignalName.ABORT).check()) {
					return Abort;
				}
				if (getSignal(SignalName.HUMAN_FEED_WAIT_FOR_TOTE).check()) {
					return HumanFeed_WaitForTote;
				}
				if (getSignal(SignalName.HUMAN_FEED_STOP).check()) {
					sStateMachine.fToStopHumanFeed = true;
				}
				if (getSignal(SignalName.HUMAN_FEED_END_CURRENT_STATE_AND_DESCEND).check()) {
					return HumanFeed_ThrottleConveyorAndDescend;
				}
				return this;
			}

			@Override
			protected void init() {
				getSignal(SignalName.HUMAN_FEED_THROTTLE_CONVEYOR_DESCEND).clear();
			}
		},

		/**
		 * Tote feeding state. Opens the guardrails, throttles the conveyor
		 * forward, and raises the tote elevator.
		 */
		HumanFeed_WaitForTote {
			@Override
			protected void init() {
			}

			@Override
			public State run() {
				if (sStateMachine.fToStopHumanFeed) {
					return Idle;
				}
				if (getSignal(SignalName.ABORT).check()) {
					return Abort;
				}
				if (UConveyorSystem.getInstance().getRearSensor() || UConveyorSystem.getInstance().getFrontSensor() || getSignal(SignalName.HUMAN_FEED_THROTTLE_CONVEYOR_DESCEND).check()) {
					return HumanFeed_ToteOnConveyor;
				}
				if (getSignal(SignalName.HUMAN_FEED_STOP).check()) {
					sStateMachine.fToStopHumanFeed = true;
				}
				if (getSignal(SignalName.HUMAN_FEED_END_CURRENT_STATE_AND_DESCEND).check()) {
					return HumanFeed_ThrottleConveyorAndDescend;
				}
				return this;
			}
		},

		/**
		 * A tote is on the conveyor belt. Closes the guardrail and throttles
		 * the conveyor forward.
		 */
		HumanFeed_ToteOnConveyor {
			@Override
			public State run() {
				if (getSignal(SignalName.ABORT).check()) {
					return Abort;
				}
				if (getSignal(SignalName.HUMAN_FEED_THROTTLE_CONVEYOR_DESCEND).check()) {
					return HumanFeed_ThrottleConveyorAndDescend;
				}
				if (getSignal(SignalName.HUMAN_FEED_STOP).check()) {
					sStateMachine.fToStopHumanFeed = true;
				}
				if (getSignal(SignalName.HUMAN_FEED_END_CURRENT_STATE_AND_DESCEND).check()) {
					return HumanFeed_ThrottleConveyorAndDescend;
				}
				if (getSignal(SignalName.HUMAN_FEED_THROTTLE_CONVEYOR_BACK).check()) {
					return HumanFeed_ThrottleConveyorBack;
				}
				return this;
			}

			@Override
			protected void init() {
			}
		},

		/**
		 * Throttles the conveyor back. Occurs immediately after the falling
		 * edge of the front sensor.
		 */
		HumanFeed_ThrottleConveyorBack {
			@Override
			public State run() {
				if (getSignal(SignalName.ABORT).check()) {
					return Abort;
				}
				if (getSignal(SignalName.HUMAN_FEED_THROTTLE_CONVEYOR_DESCEND).check()) {
					return HumanFeed_ThrottleConveyorAndDescend;
				}
				if (getSignal(SignalName.HUMAN_FEED_STOP).check()) {
					sStateMachine.fToStopHumanFeed = true;
				}
				if (getSignal(SignalName.HUMAN_FEED_END_CURRENT_STATE_AND_DESCEND).check()) {
					return HumanFeed_ThrottleConveyorAndDescend;
				}
				return this;
			}

			@Override
			protected void init() {
			}
		},

		/**
		 * Throttles the conveyor forward and lowers the tote elevator.
		 */
		HumanFeed_ThrottleConveyorAndDescend {
			@Override
			public State run() {
				if (getSignal(SignalName.ABORT).check()) {
					return Abort;
				}
				if (getSignal(SignalName.HUMAN_FEED_RAISE_TOTE).check()) {
					if (sStateMachine.fToStopHumanFeed) {
						return Idle;
					}
					return HumanFeed_RaiseTote;
				}
				if (getSignal(SignalName.HUMAN_FEED_STOP).check()) {
					sStateMachine.fToStopHumanFeed = true;
				}
				if (getSignal(SignalName.HUMAN_FEED_END_CURRENT_STATE_AND_DESCEND).check()) {
					return HumanFeed_ThrottleConveyorAndDescend;
				}
				return this;
			}

			@Override
			protected void init() {
				sStateMachine.fNumberTotes++;
				if (sStateMachine.fNumberTotes == 6) {
					sStateMachine.fToStopHumanFeed = true;
				}
			}
		},

		/**
		 * Tilts the bin elevator up if transitioning from idle to human feed.
		 */
		TiltUp {
			@Override
			public State run() {
				if (getSignal(SignalName.ABORT).check()) {
					return Abort;
				}
				if (UBinElevatorSystem.getInstance().getTilterBackLimitSwitch()) {
					return HumanFeed_RaiseTote;
				}
				return this;
			}

			@Override
			protected void init() {
			}
		},

		/**
		 * Stops all state machine movement. Manual movement is still allowed.
		 */
		Abort {
			@Override
			protected void init() {
			}

			@Override
			public State run() {
				if (getSignal(SignalName.ABORT).check()) {
					return Idle;
				}
				if (getSignal(SignalName.RESET).check()) {
					return Init;
				}
				if (getSignal(SignalName.HUMAN_FEED_START).check()) {
					return HumanFeed_RaiseTote;
				}
				if (getSignal(SignalName.HUMAN_FEED_STOP).check()) {
					return Idle;
				}
				return Abort;
			}
		};

		public abstract State run();

		/**
		 * Returns a string representation of the state.
		 */
		@Override
		public String toString() {
			return this.name();
		}

		protected abstract void init();
	}

	/**
	 * Runs the state machine.
	 */
	public static void run() {
		double elapsed = sStateMachine.fStateTimer.get();
		for (UStateMachineSystem sms : sStateMachine.fSystems) {
			sms.superSecretSpecialSatanRun(sStateMachine.fCurrentState, elapsed);
		}

		State nextState = sStateMachine.fCurrentState.run();
		if (sStateMachine.fCurrentState != nextState) {
			sStateMachine.fCurrentState = nextState;
			sStateMachine.fCurrentState.init();
			sStateMachine.fStateTimer.reset();
			for (UStateMachineSystem s : sStateMachine.fSystems)
				s.init(sStateMachine.fCurrentState);
		}

		if (UOI.getInstance().incrementNumberTotesButton.get()) {
			if (!sStateMachine.fIncrementNumberTotes) {
				sStateMachine.fNumberTotes = sStateMachine.fNumberTotes--;
				sStateMachine.fNumberTotes = sStateMachine.fNumberTotes < 0 ? 0 : sStateMachine.fNumberTotes;
				sStateMachine.fIncrementNumberTotes = true;
			}
		}
		else {
			sStateMachine.fIncrementNumberTotes = false;
		}

		for (Signal sig : sStateMachine.fAutoClearSignals) {
			sig.clear();
		}
	}

	/**
	 * Displays the number of totes and the current state on the smartdashboard.
	 */
	public static void display() {
		SmartDashboard.putNumber("Number of Totes", sStateMachine.fNumberTotes);
		SmartDashboard.putString("CurrentState", sStateMachine.fCurrentState.toString());
	}
}
