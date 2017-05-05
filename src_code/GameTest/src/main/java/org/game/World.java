/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.J3dPackage.Line3d;
import org.J3dPackage.Point3d;
import org.joml.Vector3f;
import org.lwjglb.engine.graph.Camera;
import org.lwjglb.engine.graph.Mesh;
        

/**
 *
 * @author Userr
 */
public final class World {
    private World(){}
    private static final Object chunkSynch=new Object();
    private static HashMap<String,chunk> loadedChunks=new HashMap();
    public static Camera player;
    public static void Setup() throws Exception{
        loadedChunks=new HashMap();
        Vector3f location=new Vector3f(0,2,0);
        Vector3f view=new Vector3f(0,0,0);
        player=new Camera(location,view);
        Vector3f position =World.player.getPosition();
        int x=(int)Math.floor(position.x/5);
        int y=(int)Math.floor(position.y/5);
        int z=(int)Math.floor(position.z/5);
        for(int i=x-2;i<=x+2;i++){
            for(int j=y-2;j<=y+2;j++){
                for(int l=z-2;l<=z+2;l++){
                    CreateChunk(new Point3d(x,y,z));
                    generateMesh(new Point3d(x,y,z));
                }
            }
        }
    }
    public static void LoadChunk(Point3d p){
        
    }
    public static void DiscardChunk(Point3d p){
        if(p==null) return;
        if(isChunkLoaded(p))
        synchronized(chunkSynch){
            loadedChunks.get("("+(int)p.x+";"+(int)p.y+";"+(int)p.z+")").cleanup();
            loadedChunks.remove("("+((int)p.x)+";"+((int)p.y)+";"+((int)p.z)+")");
            chunkSynch.notify();
        }
    }
    public static void redoChunk(Point3d p) throws Exception{
        synchronized(chunkSynch){
            loadedChunks.get("("+(int)p.x+";"+(int)p.y+";"+(int)p.z+")").cleanup();
            loadedChunks.get("("+(int)p.x+";"+(int)p.y+";"+(int)p.z+")").createMesh();
            chunkSynch.notify();
        }
    }
    public static void CreateChunk(Point3d p){
        synchronized(chunkSynch){
            loadedChunks.put("("+(int)p.x+";"+(int)p.y+";"+(int)p.z+")", new chunk(p));
            loadedChunks.get("("+(int)p.x+";"+(int)p.y+";"+(int)p.z+")").New();
            chunkSynch.notify();
        }
    }
    public static void SaveChunk(){
        
    }
    public static boolean isChunkLoaded(Point3d p){
        synchronized(chunkSynch){
            Set<String> keySet = loadedChunks.keySet();
            boolean returns=keySet.contains("("+(int)p.x+";"+(int)p.y+";"+(int)p.z+")");
            chunkSynch.notify();
            return returns;
        }
    }
    public static Mesh[] GetMeshes(){
        HashMap<String,chunk> loadedchunks;
        synchronized(chunkSynch){
            loadedchunks=(HashMap<String,chunk>) loadedChunks.clone();
            chunkSynch.notify();
        }
        if(!loadedchunks.isEmpty()){
            Set<Mesh> returns=new HashSet<>();
            String[] keys=loadedchunks.keySet().toArray(new String[loadedchunks.size()]);
            for (String key : keys) {
                if (loadedchunks.get(key).hasMesh()) {
                    returns.add(loadedchunks.get(key).getMesh());
                }
            }
            return returns.toArray(new Mesh[returns.size()]);
        }
        return new Mesh[]{};
    }
    public static void cleanup(){
        synchronized(chunkSynch){
            String[] keys=loadedChunks.keySet().toArray(new String[loadedChunks.size()]);
            for (String key : keys) {
                loadedChunks.get(key).cleanup();
            }
            chunkSynch.notify();
        }
    }
    public static Point3d getChunk(Point3d p){
        Point3d d=p.clone();
        d.x/=5;
        d.y/=5;
        d.z/=5;
        d.x=(int)Math.floor(d.x);
        d.y=(int)Math.floor(d.y);
        d.z=(int)Math.floor(d.z);
        return d;
    }
    public static boolean IsLoaded(Point3d p){
        Point3d d=getChunk(p);
        return isChunkLoaded(d);
    }
    public static boolean IsAir(Point3d p){
        Point3d d=getChunk(p);
        return getchunk(getChunk(p)).isAir(p);
    }

    public static void generateMesh(Point3d p) throws Exception {
        chunk ch = getchunk(p);
        if(ch!=null)
        ch.createMesh();
    }
    public static void readyMesh(Point3d p){
        if(isChunkLoaded(p)){
            getchunk(p).readyMesh();
        }
    }
    public static Point3d intersects(Point3d p, Line3d l) {
        return getchunk(getChunk(p)).intersects(p,l);
    }
    public static boolean hasAir(Point3d p) {
        return getchunk(getChunk(p)).hasAir(p);
    }
    public static chunk getchunk(Point3d p){
        synchronized(chunkSynch){
            chunk c=loadedChunks.get("("+(int)p.x+";"+(int)p.y+";"+(int)p.z+")");
            chunkSynch.notify();
            return c;
        }
    }
    public static void dig(Point3d p,double d){
        Point3d min=new Point3d(Math.floor(p.x-d/2),Math.floor(p.y-d/2),Math.floor(p.z-d/2));
        Point3d max=new Point3d(Math.ceil(p.x+d/2),Math.ceil(p.y+d/2),Math.ceil(p.z+d/2));
        for(int x=(int)min.x;x<=(int)max.x;x++){
            for(int y=(int)min.y;y<=(int)max.y;y++){
                for(int z=(int)min.z;z<=(int)max.z;z++){
                    getchunk(getChunk(new Point3d(x,y,z))).dig(new Point3d(x,y,z),p,d);
                }
            }
        }
    }
    public static void place(Point3d p,double d,int type){
        Point3d min=new Point3d(Math.floor(p.x-d/2),Math.floor(p.y-d/2),Math.floor(p.z-d/2));
        Point3d max=new Point3d(Math.ceil(p.x+d/2),Math.ceil(p.y+d/2),Math.ceil(p.z+d/2));
        for(int x=(int)min.x;x<=(int)max.x;x++){
            for(int y=(int)min.y;y<=(int)max.y;y++){
                for(int z=(int)min.z;z<=(int)max.z;z++){
                    getchunk(getChunk(new Point3d(x,y,z))).place(new Point3d(x,y,z),p,d,type);
                }
            }
        }
    }
}
