package org.maths;

import org.J3dPackage.Vector3d;
import org.J3dPackage.Point3d;
import org.J3dPackage.Line2d;
import org.J3dPackage.Point2d;
import org.J3dPackage.Line3d;
import java.io.File;
import java.util.Vector;

import org.J3DBool.Solid;
import org.joml.Matrix4d;
public final class Math3d {
	private static final double TOL = 1e-10f;
        public static Vector3d getNormal(Point3d a,Point3d b,Point3d c){
            Vector3d p,v;
            p=new Vector3d(b.x-a.x,b.y-a.y,b.z-a.z);
            p.normalize();
            v=new Vector3d(c.x-a.x,c.y-a.y,c.z-a.z);
            v.normalize();
            Vector3d returns=new Vector3d();
            returns.cross(p, v);
            returns.normalize();
            return returns;
        }
        public static Point3d lineSolidIntersection(Solid s,Line3d l){
            Point3d closest=null;
            double distance=Double.MAX_VALUE;
            Point3d[] points=s.getVertices();
            int[] indices=s.getIndices();
            for(int i=0;i<(indices.length/3);i++){
                Point3d p1=points[indices[i*3]];
                Point3d p2=points[indices[i*3+1]];
                Point3d p3=points[indices[i*3+2]];
                if(equals(volumeOfTrianglePyramid(l.a, p1,p2,p3),Math.abs(volumeOfTrianglePyramid(l.a, l.b,p1,p2)+volumeOfTrianglePyramid(l.a, l.b,p2,p3)+volumeOfTrianglePyramid(l.a, l.b,p1,p3)-volumeOfTrianglePyramid(l.b, p1,p2,p3)))){
                    Point3d p=linePlaneIntersectionPoint(p1, p2, p3, l);
                    if(p!=null &&distance(p, l.a)<distance){
                        distance=distance(p,l.a);
                        closest=p;
                    }
                }
            }
            return closest;
        }
        private static boolean equals(double a,double b){
            return a<(b+TOL)&&a>(b-TOL);
        }
        public static Point3d linePlaneIntersectionPoint(Point3d v0, Point3d v1, Point3d v2,Line3d l){
            Vector3d d=new Vector3d(l.b.x-l.a.x,l.b.y-l.a.y,l.b.z-l.a.z);
            Vector3d p=l.a.toVector();
            d.normalize();
            Vector3d e1,e2,h,s,q;
            float a,f,u,v;
            e1=new Vector3d(v1.x-v0.x,v1.y-v0.y,v1.z-v0.z);
            e2=new Vector3d(v2.x-v0.x,v2.y-v0.y,v2.z-v0.z);
            h=new Vector3d();
            h.cross(d, e2);
            a=(float)e1.dot(h);
            if (a > -0.00001 && a < 0.00001)
		return null;
            f=1/a;
            s=new Vector3d(p.x-v0.x,p.y-v0.y,p.z-v0.z);
            u=(float)(f*s.dot(h));
            if (u < 0.0 || u > 1.0)
		return null;
            q=new Vector3d();
            q.cross(s, e1);
            v=(float)(f*new Vector3d(d.x,d.y,d.z).dot(q));
            if (v < 0.0 || u + v > 1.0)
		return null;
            float t=(float)(f*e2.dot(q))/*(float)l.a.distance(l.b)/2f*/;
            d.multiply(t);
            return new Point3d(p.x+d.x,p.y+d.y,p.z+d.z);
        }
        public static double volumeOfTrianglePyramid(Point3d a,Point3d b,Point3d c,Point3d d){
            Matrix4d matrix=new Matrix4d(a.x,b.x,c.x,d.x,a.y,b.y,c.y,d.y,a.z,b.z,c.z,d.z,1,1,1,1);
            return Math.abs(matrix.determinant())/6;
        }
	/**
	 * 
	 * @param ps first point
	 * @param ps2 second point
	 * @param ps3 third point
	 * @return the volume of the triangle pyramid (ps,ps2,ps3,(0,0,0)) 
	 * @warning the volume returned is negative if the face is facing away from (0,0,0)
	 */
	public static double SignedVolumeOfTriangle(Point3d ps, Point3d ps2, Point3d ps3) {
	    double v321 = ps3.x*ps2.y*ps.z;
	    double v231 = ps2.x*ps3.y*ps.z;
	    double v312 = ps3.x*ps.y*ps2.z;
	    double v132 = ps.x*ps3.y*ps2.z;
	    double v213 = ps2.x*ps.y*ps3.z;
	    double v123 = ps.x*ps2.y*ps3.z;
	    return (1.0f/6.0f)*(-v321 + v231 + v312 - v132 - v213 + v123);
	}
	/**
	 * @param mesh - the mesh
	 * @return
	 * returns the volume of the given mesh in double
	 */
	public static double VolumeOfMesh(Solid mesh) {
		Point3d[] ps=mesh.getVertices();
		int[] inds=mesh.getIndices();
		double volume=0;
		for(int i=0;i<inds.length;i+=3){
			volume+=SignedVolumeOfTriangle(ps[inds[i]],ps[inds[i+1]],ps[inds[i+2]]);
		}
	    return Math.abs(volume);
	}
	/**
	 * @param vec - the vector
	 * @param angle - the rotation angle
	 * @return the rotated vector around the z axis
	 */
	public static Vector3d rotateZ(Vector3d vec,double angle){
		angle=Math.toRadians(angle);
		Vector3d result=new Vector3d(vec.x*Math.cos(angle)-vec.y*Math.sin(angle),vec.x*Math.sin(angle)+vec.y*Math.cos(angle),vec.z);
		result.normalize();
		return result;
	}
	/**
	 * @param vec - the vector
	 * @param angle - the rotation angle
	 * @return the rotated vector around the x axis
	 */
	public static Vector3d rotateX(Vector3d vec,double angle){
		angle=Math.toRadians(angle);
		Vector3d result=new Vector3d(vec.x,vec.y*Math.cos(angle)-vec.z*Math.sin(angle),vec.y*Math.sin(angle)+vec.z*Math.cos(angle));
		result.normalize();
		return result;
	}
	/**
	 * @param vec - the vector
	 * @param angle - the rotation angle
	 * @return the rotated vector around the y axis
	 */
	public static Vector3d rotateY(Vector3d vec,double angle){
		angle=Math.toRadians(angle);
		Vector3d result=new Vector3d(vec.x*Math.cos(angle)+vec.z*Math.sin(angle),vec.y,vec.z*Math.cos(angle)-vec.x*Math.sin(angle));
		result.normalize();
		return result;
	}
	/**
	 * 
	 * @param a first point
	 * @param b second point
	 * @return the distance between two points
	 */
	public static double distance(Point3d a,Point3d b){
		return(Math.sqrt((a.x-b.x)*(a.x-b.x)+(a.y-b.y)*(a.y-b.y)+(a.z-b.z)*(a.z-b.z)));
	}
	/**
	 * 
	 * @param p center
	 * @param size diameter
	 * @return a sphere at the location of the given point with the given diameter
	 */
	public static Solid getSphereAt(Point3d p,double size){
		size/=2;
		Solid s=new Solid(new File("./src/main/resources/Objects/Sphere.solid"));
		Point3d[] verticies=s.getVertices();
		if(p==null){return new Solid();}
		for(int i=0;i<s.getVertices().length;i++){
			verticies[i].x*=size;
			verticies[i].y*=size;
			verticies[i].z*=size;
			verticies[i].x+=p.x;
			verticies[i].y+=p.y;
			verticies[i].z+=p.z;
		}
		s.setData(verticies, s.getIndices());
		return s;
	}
	/**
	 * 
	 * @param p the center of the cube
	 * @param size - the length of the side of the cube
	 * @return a cube at the position of the point with the given size
	 */
	public static Solid getCubeAt(Point3d p,double size){
		size/=2;
		Solid s=new Solid(new File("./src/main/resources/Objects/Sphere.solid"));
		Point3d[] verticies=s.getVertices();
		if(p==null){return new Solid();}
		for(int i=0;i<s.getVertices().length;i++){
			verticies[i].x*=size;
			verticies[i].y*=size;
			verticies[i].z*=size;
			verticies[i].x+=p.x;
			verticies[i].y+=p.y;
			verticies[i].z+=p.z;
		}
		s.setData(verticies, s.getIndices());
		return s;
	}
	/**
	 * 
	 * @param p position of the head of the player
	 * @return the player's colision at the given position
	 */
	public static Solid getPlayerColisonAt(Point3d p){
		Solid s=new Solid(new File("./src/main/resources/Objects/player.solid"));
		Point3d[] verticies=s.getVertices();
		if(p==null){return new Solid();}
		for(int i=0;i<s.getVertices().length;i++){
			verticies[i].x+=p.x;
			verticies[i].y+=p.y;
			verticies[i].z+=p.z;
		}
		s.setData(verticies, s.getIndices());
		return s;
	}
	/**
	 * 
	 * @param s 3d object (Solid)
	 * @param p point to which to search
	 * @return the closest point to the given point from the given object
	 */
	public static Point3d getClosest(Solid s,Point3d p){
		Point3d closest=new Point3d(Float.MAX_VALUE,Float.MAX_VALUE,Float.MAX_VALUE);
		double cd=Double.MAX_VALUE;
		for(int i=0;i<s.getVertices().length;i++){
			if(distance(s.getVertices()[i],p)<cd){
				cd=distance(s.getVertices()[i],p);
				closest=s.getVertices()[i];
			}
		}
		return closest;
	}
	/**
	 * 
	 * @param p point to which to compate
	 * @param a a vector of points
	 * @return the closest point to the given point from the vector
	 */
	public static Point3d getClosest(Point3d p,Vector<Point3d> a){
		Point3d closest=new Point3d(Float.MAX_VALUE,Float.MAX_VALUE,Float.MAX_VALUE);
		double cd=Double.MAX_VALUE;
		for(int i=0;i<a.size();i++){
			if(distance(a.get(i),p)<cd){
				cd=distance(a.get(i),p);
				closest=a.get(i);
			}
		}
		return closest;
	}
	/**
	 * converts a Float array to a float array
	 * @param in_  the input array
	 * @return  the output array
	 */
	public static float[] ConvertArrayFloat(Object[] in_){
		Float[] in=(Float[])in_;
		float[] out= new float[in.length];
		for(int i=0;i<in.length;i++){
			out[i]=in[i];
		}
		return out;
	}
	/**
	 * 
	 * @param a starting point of the line
	 * @param c direction of the line
	 * @param length the length of the line
	 * @return
	 * the cubes that the line goes trouhg
	 * @warning
	 * the functions normalizes the vector c
	 */
	public static Vector<Point3d> LineIntersections(Point3d a,Vector3d c, double length){
		
		c.normalize();
		
		Point3d b=new Point3d();
		
		b.x=a.x+c.x*length;
		b.y=a.y+c.y*length;
		b.z=a.z+c.z*length;
		
		
		Point3d min=new Point3d();
		Point3d max=new Point3d();
		
		
		min.x=Math.min(a.x, b.x);
		max.x=Math.max(a.x, b.x);
		min.y=Math.min(a.y, b.y);
		max.y=Math.max(a.y, b.y);
		min.z=Math.min(a.z, b.z);
		max.z=Math.max(a.z, b.z);
		
		
		min.x=Math.floor(min.x);
		max.x=Math.ceil(max.x);
		min.y=Math.floor(min.y);
		max.y=Math.ceil(max.y);
		min.z=Math.floor(min.z);
		max.z=Math.ceil(max.z);
		
		
		Vector<Point3d> returns=new Vector<Point3d>();
		boolean xy[][]= LineIntersections2d(new Point2d(min.x,min.y), new Point2d(max.x,max.y), new Line2d(new Point2d(a.x,a.y),new Point2d(b.x,b.y)));
		boolean xz[][]= LineIntersections2d(new Point2d(min.x,min.z), new Point2d(max.x,max.z), new Line2d(new Point2d(a.x,a.z),new Point2d(b.x,b.z)));
		boolean yz[][]= LineIntersections2d(new Point2d(min.y,min.z), new Point2d(max.y,max.z), new Line2d(new Point2d(a.y,a.z),new Point2d(b.y,b.z)));
		
		for(int i=(int) min.x;i<=max.x;i++){
			for(int j=(int) min.y;j<=max.y;j++){
				for(int k=(int) min.z;k<=max.z;k++){
					if(xy[i-(int) min.x][j-(int) min.y]&&xz[i-(int) min.x][k-(int) min.z]&&yz[j-(int)min.y][k-(int)min.z]){
						returns.addElement(new Point3d(i,j,k));
					}
				}
			}
		}
		return returns;
	}
	////// PRIVATE //////
	/**
	 * 
	 * @param min down left coordinates
	 * @param max up right coordinates
	 * @param line the line for checking
	 * @return what squares does the line go trough in the given coordinates
	 */
	private static boolean[][] LineIntersections2d(Point2d min,Point2d max, Line2d line){
		boolean[][] returns=new boolean[(int)max.x-(int)min.x+1][(int)max.y-(int)min.y+1];
		for(int i=(int) min.x;i<=max.x;i++){
			for(int j=(int)min.y;j<=max.y;j++){
				returns[i-(int)min.x][j-(int)min.y]=LineIntersects(line, new Point2d(i,j));
			}
		}
		
		return returns;
	}
	/**
	 * 
	 * @param l line
	 * @param p center of a square with side 1
	 * @return does the line intersect the square
	 */
	private static boolean LineIntersects(Line2d l, Point2d p) {
		double[] function=l.lineEquation();
		if(function.length==1){
			if(function[0]>=(p.x-0.5)-TOL &&function[0]<=(p.x+0.5)+TOL){
				return true;
			}
			return false;
		}else if(function.length==2){
			if((f(p.x-0.5,function[0],function[1])>p.y+0.5+TOL && f(p.x+0.5,function[0],function[1])>p.y+0.5+TOL)
				||(f(p.x-0.5,function[0],function[1])<p.y-0.5-TOL && f(p.x+0.5,function[0],function[1])<p.y-0.5-TOL) ){
				return false;
			}
			return true;
		}else if(function.length==3){
			if(function[0]<=p.x+0.5+TOL&&function[0]>=p.x-0.5-TOL&&function[1]<=p.y+0.5+TOL&&function[1]>=p.y-0.5-TOL){
				return true;
			}
			return false;
		}
		return false;
	}
	/**
	 * 
	 * @param x
	 * @param a
	 * @param b
	 * @return x*a+b
	 */
	private static double f(double x, double a, double b){
		double returns=(x*a+b);
		return returns;
	}
}