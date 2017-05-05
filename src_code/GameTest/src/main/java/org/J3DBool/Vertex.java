package org.J3DBool;
import org.J3dPackage.Point3d;
import java.util.ArrayList;

/** 
 * Represents of a 3d face vertex.
 * 
 * <br><br>See: 
 * D. H. Laidlaw, W. B. Trumbore, and J. F. Hughes.  
 * "Constructive Solid Geometry for Polyhedral Objects" 
 * SIGGRAPH Proceedings, 1986, p.161. 
 * 
 * @author Danilo Balby Silva Castanheira (danbalby@yahoo.com)
 */
public class Vertex implements Cloneable
{
	/** vertex coordinate in X */
	public double x;
	/** vertex coordinate in Y */
	public double y;
	/** vertex coordinate in Z */
	public double z;
	/** references to vertices conected to it by an edge  */
	@SuppressWarnings("rawtypes")
	private ArrayList adjacentVertices;
	/** vertex status relative to other object */
	private int status;
	
	/** tolerance value to test equalities */
	private static final double TOL = 1e-5f;
	
	/** vertex status if it is still unknown */
	public static final int UNKNOWN = 1;
	/** vertex status if it is inside a solid */
	public static final int INSIDE = 2;
	/** vertex status if it is outside a solid */
	public static final int OUTSIDE = 3;
	/** vertex status if it on the boundary of a solid */
	public static final int BOUNDARY = 4;
		
	//----------------------------------CONSTRUCTORS--------------------------------//
	
	/**
	 * Constructs a vertex with unknown status
	 * 
	 * @param position vertex position
	 */
	@SuppressWarnings("rawtypes")
	public Vertex(Point3d position)
	{
		
		x = position.x;
		y = position.y;
		z = position.z;
		
		adjacentVertices = new ArrayList();
		status = UNKNOWN;
	}
	
	/**
	 * Constructs a vertex with unknown status
	 * 
	 * @param x coordinate on the x axis
	 * @param y coordinate on the y axis
	 * @param z coordinate on the z axis
	 */
	@SuppressWarnings("rawtypes")
	public Vertex(double x, double y, double z)
	{
				
		this.x = x;
		this.y = y;
		this.z = z;
		
		adjacentVertices = new ArrayList();
		status = UNKNOWN;
	}
	
	/**
	 * Constructs a vertex with definite status
	 * 
	 * @param position vertex position
	 * @param status vertex status - UNKNOWN, BOUNDARY, INSIDE or OUTSIDE
	 */
	@SuppressWarnings("rawtypes")
	public Vertex(Point3d position, int status)
	{
		
		x = position.x;
		y = position.y;
		z = position.z;
		
		adjacentVertices = new ArrayList();
		this.status = status;
	}
	
	/**
	 * Constructs a vertex with a definite status
	 * 
	 * @param x coordinate on the x axis
	 * @param y coordinate on the y axis
	 * @param z coordinate on the z axis
	 * @param status vertex status - UNKNOWN, BOUNDARY, INSIDE or OUTSIDE
	 */
	@SuppressWarnings("rawtypes")
	public Vertex(double x, double y, double z, int status)
	{
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		adjacentVertices = new ArrayList();
		this.status = status;
	}
	
	//-----------------------------------OVERRIDES----------------------------------//
	
	/**
	 * Clones the vertex object
	 * 
	 * @return cloned vertex object
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object clone()
	{
		try
		{
			Vertex clone = (Vertex)super.clone();
			clone.x = x;
			clone.y = y;
			clone.z = z;
			clone.status = status;
			clone.adjacentVertices = new ArrayList();
			for(int i=0;i<adjacentVertices.size();i++)
			{
				clone.adjacentVertices.add(((Vertex)adjacentVertices.get(i)).clone());								 
			}
			
			return clone;
		}
		catch(CloneNotSupportedException e)
		{	
			return null;
		}
	}
	
	/**
	 * Makes a string definition for the Vertex object
	 * 
	 * @return the string definition
	 */
	public String toString()
	{
		return "("+x+", "+y+", "+z+")";
	}
	
	/**
	 * Checks if an vertex is equal to another. To be equal, they have to have the same
	 * coordinates(with some tolerance)
	 * 
	 * @param anObject the other vertex to be tested
	 * @return true if they are equal, false otherwise. 
	 */
	public boolean equals(Object anObject)
	{
		if(!(anObject instanceof Vertex))
		{
			return false;
		}
		else
		{
			Vertex vertex = (Vertex)anObject;
			return 	Math.abs(x-vertex.x)<TOL && Math.abs(y-vertex.y)<TOL 
					&& Math.abs(z-vertex.z)<TOL; 			
		}
	}
	
	//--------------------------------------SETS------------------------------------//
		
	/**
	 * Sets the vertex status
	 * 
	 * @param status vertex status - UNKNOWN, BOUNDARY, INSIDE or OUTSIDE
	 */
	public void setStatus(int status)
	{
		if(status>=UNKNOWN && status<=BOUNDARY)
		{
			this.status = status;	
		}
	}
	
	//--------------------------------------GETS------------------------------------//
	
	/**
	 * Gets the vertex position
	 * 
	 * @return vertex position
	 */
	public Point3d getPosition()
	{
		return new Point3d(x, y, z);
	} 
	
	/**
	 * Gets an array with the adjacent vertices
	 * 
	 * @return array of the adjacent vertices 
	 */
	public Vertex[] getAdjacentVertices()
	{
		Vertex[] vertices = new Vertex[adjacentVertices.size()];
		for(int i=0;i<adjacentVertices.size();i++)
		{
			vertices[i] = (Vertex)adjacentVertices.get(i); 
		}
		return vertices;
	}
	
	/**
	 * Gets the vertex status
	 * 
	 * @return vertex status - UNKNOWN, BOUNDARY, INSIDE or OUTSIDE
	 */	
	public int getStatus()
	{
		return status;
	}
	
	
	//----------------------------------OTHERS--------------------------------------//
	
	/**
	 * Sets a vertex as being adjacent to it
	 * 
	 * @param adjacentVertex an adjacent vertex
	 */
	@SuppressWarnings("unchecked")
	public void addAdjacentVertex(Vertex adjacentVertex)
	{
		if(!adjacentVertices.contains(adjacentVertex))
		{
			adjacentVertices.add(adjacentVertex);
		} 
	}
	
	/**
	 * Sets the vertex status, setting equally the adjacent ones
	 * 
	 * @param status new status to be set
	 */
	public void mark(int status)
	{
		//mark vertex
		this.status = status;
		
		//mark adjacent vertices
		Vertex[] adjacentVerts = getAdjacentVertices();
		for(int i=0;i<adjacentVerts.length;i++)
		{
			if(adjacentVerts[i].getStatus()==Vertex.UNKNOWN)
			{
				adjacentVerts[i].mark(status);
			}
		}
	}
}