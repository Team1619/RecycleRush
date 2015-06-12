package org.usfirst.frc.team1619;

import java.util.ArrayList;

public class UGenericLine implements ULine {
	class Point {
		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public double x, y;
	}

	ArrayList<Point> fPoints = new ArrayList<Point>();

	public UGenericLine() {
		fPoints.add(new Point(0, 0));
	}

	public UGenericLine(double x, double y) {
		fPoints.add(new Point(x, y));
	}

	public static double map(double x, double x0, double y0, double x1,
			double y1) {
		return ((y1 - y0) / (x1 - x0)) * (x - x0) + y0;
	}

	public void addPoint(double x, double y) {
		// Assuming all points are inserted in order
		if (x < fPoints.get(0).x) {
			fPoints.add(0, new Point(x, y));
		}
		else
			fPoints.add(new Point(x, y));
	}

	@Override
	public double getValue(double time) {
		if (time < fPoints.get(0).x)
			return fPoints.get(0).y;
		for (int i = 0; i < fPoints.size() - 1; i++) {
			Point start = fPoints.get(i);
			Point end = fPoints.get(i + 1);

			if (time >= start.x && time <= end.x) {
				return map(time, start.x, start.y, end.x, end.y);
			}
		}
		return fPoints.get(fPoints.size() - 1).y;
	}

	@Override
	public String toString() {
		String s = "{";
		boolean firstPoint = true;
		for (Point p : fPoints) {
			if (!firstPoint) {
				s += ", ";
			}
			else {
				firstPoint = false;
			}
			s += String.format("<%f, %f>", p.x, p.y);
		}
		return s + "}";
	}
}
