package org.usfirst.frc.team1619.robot.subsystems;

public class Signal {
	private boolean hasRisen = false;
	
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
