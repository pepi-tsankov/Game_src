package org.J3dPackage;

public class Vector3d implements Cloneable{
	public double x;
	public double y;
	public double z;
	/**
	 * 3D vector constructor using (x,y,z)
	 */
	public Vector3d(double x,double y,double z){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	/**
	 * 3D vector constructor using (x=1,y=0,z=0)
	 */
	public Vector3d(){
		this.x=1;
		this.y=0;
		this.z=0;
	}
	public void cross(Vector3d a,Vector3d b){
		x=a.y*b.z-a.z*b.y;
		y=a.z*b.x-a.x*b.z;
		z=a.x*b.y-a.y*b.x;
	}
	public void normalize(){
		double l= Math.sqrt(x*x+y*y+z*z);
		x=x/l;
		y=y/l;
		z=z/l;
	}
        public void multiply(double a){
            x*=a;
            y*=a;
            z*=a;
        }
	public double angle(Vector3d xz) {
		Vector3d a=xz.clone();
		Vector3d b=this.clone();
		a.normalize();
		b.normalize();
		double c=Math.sqrt(sqr(a.x-b.x)+sqr(a.y-b.y)+sqr(a.z-b.z));
		double s_x=1-(sqr(c)/2);
		double s_y=Math.sqrt(1-sqr(s_x));
		return Math.atan2(s_y, s_x);
	}
        private static double sqr(double a){
            return a*a;
        }
	public double length(){
		return Math.sqrt(x*x+y*y+z*z);
	}
	@Override
	public Vector3d clone(){
		return new Vector3d(x,y,z);
	}
	public double dot(Vector3d a) {
		return x*a.x+y*a.y+z*a.z;
	}
	@Override
	public String toString(){
		return "("+x+";"+y+";"+z+")";
	}
}
