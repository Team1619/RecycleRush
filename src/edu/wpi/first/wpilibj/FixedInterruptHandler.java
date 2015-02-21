package edu.wpi.first.wpilibj;

abstract public class FixedInterruptHandler<T> extends InterruptHandlerFunction<T>{
	static final int FALLING_EDGE_MASK = 256;
	static final int RISING_EDGE_MASK = 1;
	
	static boolean isRisingEdge(int mask) {
		return (mask & RISING_EDGE_MASK) != 0;
	}
	
	static boolean isFallingEdge(int mask) {
		return (mask & FALLING_EDGE_MASK) != 0;
	}
	
	//Made because the interruptFired class in InterruptHandlerFunction is not defined as 
	//protected, so it cannot be seen to override. 
	@Override
	void interruptFired(int interruptAssertedMask, T param) {
		interruptFired2(interruptAssertedMask, param);
	}
	//Override using a lambda expression to use interruptHandlerFunction
	protected abstract void interruptFired2(int interruptAssertedMask, T param);
}





