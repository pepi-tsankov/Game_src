/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game;

import java.util.HashMap;
import java.util.Vector;
import org.J3dPackage.Line3d;
import org.J3dPackage.Point3d;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.Texture;
import org.maths.Math3d;
import static org.game.Options.*;

/**
 *
 * @author Userr
 */
public class chunk {
    Mesh mesh=null;
    Point3d loc;
    private final HashMap<String,Cube> cubes;
    private boolean changed=false;
    chunk(Point3d loc){
        cubes=new HashMap();
        this.loc=loc;
    }
    public void cleanup() {
        if(mesh!=null)
        mesh.cleanUp();
        mesh=null;
    }
    public void New(){
        for(int x=(int)(loc.x*chunkSize);x<(int)((loc.x+1)*chunkSize);x++){
            for(int y=(int)(loc.y*chunkSize);y<(int)((loc.y+1)*chunkSize);y++){
                for(int z=(int)(loc.z*chunkSize);z<(int)((loc.z+1)*chunkSize);z++){
                    cubes.put("("+x+";"+y+";"+z+")", new Cube(this));
                    cubes.get("("+x+";"+y+";"+z+")").New(new Point3d(x,y,z));
                }
            }
        }
        readyMesh();
    }
    private float[] positions;
    private float[] texCoords;
    private int indices[];
    @SuppressWarnings("LocalVariableHidesMemberVariable")
    public void readyMesh(){
        String[] keys=cubes.keySet().toArray(new String[cubes.size()]);
        Vector<Float> positions;
        Vector<Float> texCoords;
        positions=new Vector();
        texCoords=new Vector();
        positions.clear();
        texCoords.clear();
        Cube c;
        for (String key : keys) {
            c = cubes.get(key);
            if(c.isChanged())c.setupVectors();
            positions.addAll(c.getPoints());
            texCoords.addAll(c.getTexture());
        }
        this.positions=Math3d.ConvertArrayFloat(positions.toArray(new Float[positions.size()]));
        this.texCoords=Math3d.ConvertArrayFloat(texCoords.toArray(new Float[texCoords.size()]));
        indices=new int[this.positions.length/3];
        for(int i=0;i<indices.length;i++){
            indices[i]=i;
        }
        changed=false;
    }
    public void createMesh() throws Exception{
        Texture t=new Texture("/textures/textures.png");
        mesh=new Mesh(positions,texCoords,indices,t);
    }
    public Mesh getMesh() {
        return mesh;
    }
    public boolean hasMesh(){
        return mesh!=null;
    }
    public void discardMesh(){
        if(mesh!=null)
        mesh.cleanUp();
        mesh=null;
    }

    boolean isAir(Point3d p) {
        return cubes.get("("+(int)p.x+";"+(int)p.y+";"+(int)p.z+")").isAir();
    }
    boolean hasAir(Point3d p) {
        return cubes.get("("+(int)p.x+";"+(int)p.y+";"+(int)p.z+")").hasAir();
    }

    public Point3d intersects(Point3d p, Line3d l) {
        if(cubes.get("("+(int)p.x+";"+(int)p.y+";"+(int)p.z+")").Crosses(l)){
            return cubes.get("("+(int)p.x+";"+(int)p.y+";"+(int)p.z+")").getCrossPoint(l);
        }
        return null;
    }

    void dig(Point3d p, Point3d p1, double d) {
        Cube c=cubes.get("("+(int)p.x+";"+(int)p.y+";"+(int)p.z+")");
        c.removeBound(Math3d.getSphereAt(p1, d));
        c.removeMaterials(Math3d.getSphereAt(p1, d));
        c.setChanged(true);
    }

    void place(Point3d p, Point3d p1, double d,int type) {
        Cube c=cubes.get("("+(int)p.x+";"+(int)p.y+";"+(int)p.z+")");
        c.addBound(Math3d.getSphereAt(p1, d));
        c.addMaterial(Math3d.getSphereAt(p1, d),type);
        c.setChanged(true);
    }
    
    public void setChanged(boolean b) {
        changed=b;
    }
    public boolean isChanged(){
        return changed;
    }
}
