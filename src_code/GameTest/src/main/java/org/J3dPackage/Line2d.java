package org.J3dPackage;

public class Line2d implements Cloneable{
	Point2d a;
	Point2d b;
	private static final double TOL = 1e-10f;
	/**
	 * Line2d constructor a and b are both equal to (0,0)
	 */
	public Line2d(){
		a=new Point2d();
		b=new Point2d();
	}
	/**
	 * Line2d constructor using 2 points to create the line - a and b
	 */
	public Line2d(Point2d a,Point2d b){
		this.a=a;
		this.b=b;
	}
	/**
	 * @return 
	 * returns the m and z for the line where <code>y=mx+z</code>
	 * @warning1
	 * The function returns only one double if the points are above one another and it is the x value where the vertical line is
	 * @warning2
	 * if a.x is less than b.x the points will flip
	 * @warning3
	 * if the two points are at the same place the function will give 3 values
	 */
	public double[] lineEquation(){
		if(a.equals(b)){
			return new double[]{a.x,a.y,0};
		}
		if(a.x+TOL>=b.x && a.x<=b.x+TOL){
			return new double[]{a.x};
		}
		double m=(a.y-b.y)/(a.x-b.x);
		double z=a.y-m*a.x;
		
		return new double[]{m,z};
	}
	public Line2d clone(){
		return new Line2d(a,b);
	}
}
