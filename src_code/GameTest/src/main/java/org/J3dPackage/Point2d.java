package org.J3dPackage;

public class Point2d implements Cloneable{
	public double x;
	public double y;
	private static final double TOL = 1e-10f;
	/**
	 * Point2d constructor using (x,y)
	 */
	public Point2d(double x,double y){
		this.x=x;
		this.y=y;
	}
	public Point2d clone(){
		return new Point2d(x,y);
	}
	/**
	 * Point2d constructor using (0,0)
	 */
	public Point2d(){
		x=0;
		y=0;
	}
	@Override
	public String toString(){
		return "("+x+";"+y+")";
	}
	@Override
	public int hashCode(){
		return toString().hashCode();
	}
	@Override
	public boolean equals(Object a_){
		Point2d a=(Point2d) a_;
		if((a.x+TOL>=x)&&(a.x-TOL<=x)&&(a.y+TOL>=y)&&(a.y-TOL<=y)){
			return true;
		}
		return false;
	}
}
