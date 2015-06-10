package org.usfirst.frc.team1619;

public class UTrapezoidLine implements ULine {
	public static double map(double x0, double y0, double x1, double y1,
			double x) {
		return ((y1 - y0) / (x1 - x0)) * (x - x0) + y0;
	}

	public double getValue(double time) {
		if (time >= aX && time < bX) {
			return map(aX, aY, bX, bY, time);
		}
		else if (bX < time && time < cX) {
			return map(bX, bY, cX, cY, time);
		}

		else if (cX < time && time <= dX) {
			return map(cX, cY, dX, dY, time);
		}
		return 0;
	}

	private double aX, aY;
	private double bX, bY;
	private double cX, cY;
	private double dX, dY;

	public UTrapezoidLine(double aX, double aY, double bX, double bY,
			double cX, double cY, double dX, double dY) {
		this.aX = aX;
		this.aY = aY;
		this.bX = bX;
		this.bY = bY;
		this.cX = cX;
		this.cY = cY;
		this.dX = dX;
		this.dY = dY;
	}

	public UTrapezoidLine() {
		aX = aY = 0.0;
		bX = bY = 0.0;
		cX = cY = 0.0;
		dX = dY = 0.0;
	}
}
