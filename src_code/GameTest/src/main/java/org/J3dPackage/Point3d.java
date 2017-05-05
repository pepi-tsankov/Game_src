package org.J3dPackage;

public class Point3d implements Cloneable{
	public double x;
	public double y;
	public double z;
	private static final double TOL = 1e-10f;
	/**
	 * Point3d constructor with (x,y,z) 
	 */
	public Point3d(double x,double y,double z){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	/**
	 * Point3d constructor with (0,0,0)
	 */
	public Point3d(){
		x=0;
		y=0;
		z=0;
	}
	/**
	 * clones the point
	 */
	public Point3d clone(){
		return new Point3d(x,y,z);
	}
	/**
	 * @param a the point to which the distance is measured
	 * @return
	 * the distance between the curent point and point a
	 */
	public double distance(Point3d a){
		return Math.sqrt((x-a.x)*(x-a.x)+(y-a.y)*(y-a.y)+(z-a.z)*(z-a.z));
	}
	@Override
	public String toString(){
		return "("+x+";"+y+";"+z+")";
	}
	@Override
	public int hashCode(){
		return toString().hashCode();
	}
	@Override
	public boolean equals(Object a_){
		Point3d a=(Point3d)a_;
		if((a.x+TOL>=x)&&(a.x-TOL<=x)&&(a.y+TOL>=y)&&(a.y-TOL<=y)&&(a.z+TOL>=z)&&(a.z-TOL<=z)){
			return true;
		}
		return false;
	}
        public Vector3d toVector(){
            return new Vector3d(x,y,z);
        }
        public Point3f toPoint3f(){
            return new Point3f(x,y,z);
        }
}
