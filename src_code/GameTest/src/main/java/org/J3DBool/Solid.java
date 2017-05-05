package org.J3DBool;

import org.J3dPackage.Point3d;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import org.J3dPackage.Point3f;

//import javax.media.j3d.Shape3D;
//import com.sun.j3d.utils.geometry.GeometryInfo;
//import com.sun.j3d.utils.geometry.NormalGenerator;

/**
 * Class representing a 3D solid.
 *  
 * @author Danilo Balby Silva Castanheira (danbalby@yahoo.com)
 */
public class Solid
{
	/** array of indices for the vertices from the 'vertices' attribute */
	protected int[] indices;
	/** array of points defining the solid's vertices */
	protected Point3d[] vertices;
	
	//--------------------------------CONSTRUCTORS----------------------------------//
	
	/** Constructs an empty solid. */			
	public Solid()
	{
		super();
		setInitialFeatures();
	}
	
	/**
	 * Construct a solid based on data arrays. An exception may occur in the case of 
	 * abnormal arrays (indices making references to inexistent vertices, there are less
	 * colors than vertices...)
	 * 
	 * @param vertices array of points defining the solid vertices
	 * @param indices array of indices for a array of vertices
	 */
	public Solid(Point3d[] vertices, int[] indices)
	{
		this();
		setData(vertices, indices);		
	}
	
	/**
	 * Constructs a solid based on a coordinates file. It contains vertices and indices, 
	 * and its format is like this:
	 * 
	 * <br><br>4
	 * <br>0 -5.00000000000000E-0001 -5.00000000000000E-0001 -5.00000000000000E-0001
	 * <br>1  5.00000000000000E-0001 -5.00000000000000E-0001 -5.00000000000000E-0001
	 * <br>2 -5.00000000000000E-0001  5.00000000000000E-0001 -5.00000000000000E-0001
	 * <br>3  5.00000000000000E-0001  5.00000000000000E-0001 -5.00000000000000E-0001
	 * 
	 * <br><br>2
	 * <br>0 0 2 3
	 * <br>1 3 1 0 
	 * 
	 * @param solidFile file containing the solid coordinates
	 */
	public Solid(File solidFile)
	{
		this();
		loadCoordinateFile(solidFile);
	}
	
	/** Sets the initial features common to all constructors */
	protected void setInitialFeatures()
	{
		vertices = new Point3d[0];
		indices = new int[0];
		
		/*setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		setCapability(Shape3D.ALLOW_APPEARANCE_READ);*/
	}
		
	//---------------------------------------GETS-----------------------------------//
	/**
	 * Gets Vertecies for every indice
	 * 
	 * @return indiced solid vertecies
	 */
	public Vector<Float> getIndicedVertecies(){
                Vector<Point3f> vert=new Vector<>();
		Vector<Float> newVertecies=new Vector<>();
                for(Point3d v:vertices){
                    vert.add(v.toPoint3f());
                }
		for(int i=0;i<indices.length;i++){
			newVertecies.add(vert.get(indices[i]).x);
			newVertecies.add(vert.get(indices[i]).y);
			newVertecies.add(vert.get(indices[i]).z);
		}
		return newVertecies;
	}
	/**
	 * Gets the solid vertices
	 * 
	 * @return solid vertices
	 */	
	public Point3d[] getVertices()
	{
		Point3d[] newVertices = new Point3d[vertices.length];
		for(int i=0;i<newVertices.length;i++)
		{
			newVertices[i] = (Point3d)vertices[i].clone();
		}
		return newVertices;
	}
	
	/** Gets the solid indices for its vertices
	 * 
	 * @return solid indices for its vertices
	 */
	public int[] getIndices()
	{
		int[] newIndices = new int[indices.length];
		System.arraycopy(indices,0,newIndices,0,indices.length);
		return newIndices;
	}
	
	/**
	 * Gets if the solid is empty (without any vertex)
	 * 
	 * @return true if the solid is empty, false otherwise
	 */
	public boolean isEmpty()
	{
		if(indices.length==0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}	

	//---------------------------------------SETS-----------------------------------//
	
	/**
	 * Sets the solid data. Each vertex may have a different color. An exception may 
	 * occur in the case of abnormal arrays (e.g., indices making references to  
	 * inexistent vertices, there are less colors than vertices...)
	 * 
	 * @param vertices array of points defining the solid vertices
	 * @param indices array of indices for a array of vertices
	 * @param colors array of colors defining the vertices colors 
	 */
	public void setData(Point3d[] vertices, int[] indices)
	{
		this.vertices = new Point3d[vertices.length];
		this.indices = new int[indices.length];
		if(indices.length!=0)
		{
			for(int i=0;i<vertices.length;i++)
			{
				this.vertices[i] = (Point3d)vertices[i].clone();
			}
			System.arraycopy(indices, 0, this.indices, 0, indices.length);
		
		}
	}
	
	//-------------------------GEOMETRICAL_TRANSFORMATIONS-------------------------//
	
	/**
	 * Applies a translation into a solid
	 * 
	 * @param dx translation on the x axis
	 * @param dy translation on the y axis
	 */
	public void translate(float dx, float dy)
	{
		if(dx!=0||dy!=0)
		{
			for(int i=0;i<vertices.length;i++)
			{
				vertices[i].x += dx;
				vertices[i].y += dy;
			}
			
		}
	}
	
	/**
	 * Applies a rotation into a solid
	 * 
	 * @param dx rotation on the x axis
	 * @param dy rotation on the y axis
	 */
	public void rotate(float dx, float dy)
	{
		float cosX = (float) Math.cos(dx);
		float cosY = (float) Math.cos(dy);
		float sinX = (float) Math.sin(dx);
		float sinY = (float) Math.sin(dy);
					
		if(dx!=0||dy!=0)
		{
			//get mean
			Point3d mean = getMean();
			
			double newX,newY,newZ;
			for(int i=0;i<vertices.length;i++)
			{
				vertices[i].x -= mean.x; 
				vertices[i].y -= mean.y; 
				vertices[i].z -= mean.z; 
				
				//x rotation
				if(dx!=0)
				{
					newY = vertices[i].y*cosX - vertices[i].z*sinX;
					newZ = vertices[i].y*sinX + vertices[i].z*cosX;
					vertices[i].y = newY;
					vertices[i].z = newZ;
				}
				
				//y rotation
				if(dy!=0)
				{
					newX = vertices[i].x*cosY + vertices[i].z*sinY;
					newZ = -vertices[i].x*sinY + vertices[i].z*cosY;
					vertices[i].x = newX;
					vertices[i].z = newZ;
				}
										
				vertices[i].x += mean.x; 
				vertices[i].y += mean.y; 
				vertices[i].z += mean.z;
			}
		}
		
	}
	
	/**
	 * Applies a zoom into a solid
	 * 
	 * @param dz translation on the z axis
	 */
	public void zoom(float dz)
	{
		if(dz!=0)
		{
			for(int i=0;i<vertices.length;i++)
			{
				vertices[i].z += dz;
			}
	
		}
	}
	
	/**
	 * Applies a scale changing into the solid
	 * 
	 * @param dx scale changing for the x axis 
	 * @param dy scale changing for the y axis
	 * @param dz scale changing for the z axis
	 */
	public void scale(float dx, float dy, float dz)
	{
		for(int i=0;i<vertices.length;i++)
		{
			vertices[i].x*=dx;
			vertices[i].y*=dy;
			vertices[i].z*=dz;
		}
		
	}
	
	//-----------------------------------PRIVATES--------------------------------//
	
	
	/**
	 * Loads a coordinates file, setting vertices and indices 
	 * 
	 * @param solidFile file used to create the solid
	 */
	@SuppressWarnings({ "resource", "unused" })
	protected void loadCoordinateFile(File solidFile)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(solidFile));
			
			String line = reader.readLine();
			int numVertices = Integer.parseInt(line);
			vertices = new Point3d[numVertices];
									
			StringTokenizer tokens;
			String token;
						
			for(int i=0;i<numVertices;i++)
			{
				line = reader.readLine();
				tokens = new StringTokenizer(line);
				tokens.nextToken();
				vertices[i]= new Point3d(Float.parseFloat(tokens.nextToken()), Float.parseFloat(tokens.nextToken()), Float.parseFloat(tokens.nextToken()));
			}
			
			reader.readLine();
			
			line = reader.readLine();
			int numTriangles = Integer.parseInt(line);
			indices = new int[numTriangles*3];
						
			for(int i=0,j=0;i<numTriangles*3;i=i+3,j++)
			{
				line = reader.readLine();
				tokens = new StringTokenizer(line);
				tokens.nextToken();
				indices[i] = Integer.parseInt(tokens.nextToken());
				indices[i+1] = Integer.parseInt(tokens.nextToken());
				indices[i+2] = Integer.parseInt(tokens.nextToken());
			}
			
		}
		
		catch(IOException e)
		{
			System.out.println("invalid file!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the solid mean
	 * 
	 * @return point representing the mean
	 */
	protected Point3d getMean()
	{
		Point3d mean = new Point3d();
		for(int i=0;i<vertices.length;i++)
		{
			mean.x += vertices[i].x;
			mean.y += vertices[i].y;
			mean.z += vertices[i].z;
		}
		mean.x /= vertices.length;
		mean.y /= vertices.length;
		mean.z /= vertices.length;
		
		return mean;
	}
}