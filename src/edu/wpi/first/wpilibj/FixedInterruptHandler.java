package edu.wpi.first.wpilibj;

abstract public class FixedInterruptHandler<T> extends InterruptHandlerFunction<T>{
	//Made because the interruptFired class in InterruptHandlerFunction is not defined as 
	//protected, so it cannot be seen to override. 
	@Override
	void interruptFired(int interruptAssertedMask, T param) {
		interruptFired2(interruptAssertedMask, param);
	}
	
	//Override using a lambda expression to use interruptHandlerFunction
	protected abstract void interruptFired2(int interruptAssertedMask, T param);

}





