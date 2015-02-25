package org.usfirst.frc.team1619;

import java.util.ArrayList;

public class GenericLine implements Line {

	public static double map(double x, double x0, double y0, double x1, 
			double y1) {
		return ((y1-y0)/(x1-x0))*(x-x0)+y0;
	}
	
	class Point {
		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
		public double x, y;
	}
	
	ArrayList<Point> points = new ArrayList<Point>();
	
	public GenericLine() {
		points.add(new Point(0, 0));
	}
	
	public GenericLine(double x, double y) {
		points.add(new Point(x, y));
	}
	
	public void addPoint(double x, double y) {
		// Assuming all points are inserted in order
		if(x < points.get(0).x) {
			points.add(0, new Point(x, y));
		}
		else
			points.add(new Point(x, y));
	}
	
	@Override
	public double getValue(double time) {
		if(time < points.get(0).x)
			return points.get(0).y;
		for(int i = 0; i < points.size() - 1; i++) {
			Point start = points.get(i);
			Point end = points.get(i + 1);
			
			if(time >= start.x && time <= end.x) {
				return map(time, start.x, start.y, end.x, end.y);
			}
		}
		return points.get(points.size() - 1).y;
	}

}