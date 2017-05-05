/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import org.J3DBool.*;
import org.J3dPackage.Line3d;
import org.J3dPackage.Point3d;
import org.J3dPackage.Vector3d;
import static org.game.Options.*;
import org.maths.*;
import org.maths.Math3d;
import static org.maths.Math3d.volumeOfTrianglePyramid;
/**
 *
 * @author Userr
 */
public class Cube {
    private static Vector3d yp=new Vector3d(0,1,0);
    private static Vector3d xp=new Vector3d(1,0,0);
    private static final double TOL = 1e-10f;
    //private Solid air;
    private Solid bound;
    private HashMap<Integer,Solid> Materials;
    private Point3d location;
    private Vector<Float> Points;
    private Vector<Float> Texture;
    private final chunk parent;
    private boolean change;
    Cube(chunk c){
        change=true;
        //air=new Solid();
        bound=new Solid();
        Materials=new HashMap<>();
        location=new Point3d();
        parent=c;
    }
    public void New(Point3d loc){
        if(loc.y>0){
            //air=Math3d.getCubeAt(loc, 1);
        }else if(loc.y<=0 && loc.y>-3){
            bound=Math3d.getCubeAt(loc, 1);
            Materials.put(grass, Math3d.getCubeAt(loc, 1));
        }else if(loc.y<=-3){
            bound=Math3d.getCubeAt(loc, 1);
            Materials.put(stone, Math3d.getCubeAt(loc, 1));
        }
        location=loc;
    }
    public void Load(Point3d loc){
        
    }
    public void Save(String save){
        
    }
    public void removeBound(Solid s){
        if(!bound.isEmpty())
        bound=new BooleanModeller(bound, s).getDifference();
    }
    public HashMap<Integer,Double> removeMaterials(Solid s){
        HashMap<Integer,Double> result=new HashMap<>();
        Integer[] materials=Materials.keySet().toArray(new Integer[Materials.size()]);
        for (Integer material : materials) {
            
            result.put(material, Math3d.VolumeOfMesh(new BooleanModeller(Materials.get(material), s).getIntersection()));
            Materials.put(material, new BooleanModeller(Materials.get(material), s).getDifference());
        }
        parent.setChanged(true);
        return result;
    }
    public Double quantityToAdd(Solid s){
        return Math3d.VolumeOfMesh(new BooleanModeller(new BooleanModeller(s, Math3d.getCubeAt(location, 1)).getIntersection(),bound).getDifference());
    }
    public void addBound(Solid s){
        if(bound.isEmpty()){
            bound=new BooleanModeller(s,Math3d.getCubeAt(location, 1)).getIntersection();
        }else{
            Solid s1=new BooleanModeller(s, Math3d.getCubeAt(location, 1)).getIntersection();
            if(!s1.isEmpty()){
                bound=new BooleanModeller(s1,bound).getUnion();
            }
        }
    }
    public void addMaterial(Solid s,Integer m){
        Solid mat=new Solid();
        if(Materials.containsKey(m)){
            mat=Materials.get(m);
            if(mat.isEmpty()){
                mat=new BooleanModeller(s,Math3d.getCubeAt(location, 1)).getIntersection();
            }else{
                Solid s1=new BooleanModeller(s, Math3d.getCubeAt(location, 1)).getIntersection();
                if(!s1.isEmpty()){
                    mat=new BooleanModeller(s1,mat).getUnion();
                }
            }
        }else{
            mat=new BooleanModeller(s,Math3d.getCubeAt(location, 1)).getIntersection();
        }
        parent.setChanged(true);
        Materials.put(m, mat);
    }
    /*
    public void addAir(Solid s){
        if(air.isEmpty()){
            air=new BooleanModeller(s, Math3d.getCubeAt(location, 1)).getIntersection();
        }else{
            air=new BooleanModeller(new BooleanModeller(air,s).getUnion(),Math3d.getCubeAt(location,1)).getIntersection();
        }
    }
    */
    public boolean Crosses(Line3d a){
        Point3d[] points=bound.getVertices();
        int[] indices=bound.getIndices();
        for(int i=0;i<(indices.length/3);i++){
            Point3d p1=points[indices[i*3]];
            Point3d p2=points[indices[i*3+1]];
            Point3d p3=points[indices[i*3+2]];
            if(equals(volumeOfTrianglePyramid(a.a, p1,p2,p3),(volumeOfTrianglePyramid(a.a, a.b,p1,p2)+volumeOfTrianglePyramid(a.a, a.b,p2,p3)+volumeOfTrianglePyramid(a.a, a.b,p1,p3))-volumeOfTrianglePyramid(a.b, p1,p2,p3))){
                return true;
            }
        }
        return false;
    }
    private boolean equals(double a,double b){
        return a<(b+TOL)&&a>(b-TOL);
    }
    public void setupVectors(){
        change=false;
        Points=new Vector();
        Texture=new Vector();
        Points.clear();
        Texture.clear();
        if(this.isAir()) return;
        boolean ret=this.hasAir();
        if((!ret) &&World.IsLoaded(new Point3d(location.x+1,location.y,location.z))&&World.hasAir(new Point3d(location.x+1,location.y,location.z))) ret=true;
        if((!ret) &&World.IsLoaded(new Point3d(location.x-1,location.y,location.z))&&World.hasAir(new Point3d(location.x-1,location.y,location.z))) ret=true;
        if((!ret) &&World.IsLoaded(new Point3d(location.x,location.y+1,location.z))&&World.hasAir(new Point3d(location.x,location.y+1,location.z))) ret=true;
        if((!ret) &&World.IsLoaded(new Point3d(location.x,location.y-1,location.z))&&World.hasAir(new Point3d(location.x,location.y-1,location.z))) ret=true;
        if((!ret) &&World.IsLoaded(new Point3d(location.x,location.y,location.z+1))&&World.hasAir(new Point3d(location.x,location.y,location.z+1))) ret=true;
        if((!ret) &&World.IsLoaded(new Point3d(location.x,location.y,location.z-1))&&World.hasAir(new Point3d(location.x,location.y,location.z-1))) ret=true;
        if(!ret) return;
        Integer[] keys =Materials.keySet().toArray(new Integer[Materials.size()]);
        int up=0;
        int other=0;
        for (Integer key : keys) {
            Points.addAll(Materials.get(key).getIndicedVertecies());
            for (int j = Texture.size()/2; j<(Points.size()/3); j+=3) {
                Point3d p1=new Point3d(Points.get(j*3),Points.get(j*3+1),Points.get(j*3+2));
                Point3d p2=new Point3d(Points.get((j+1)*3),Points.get((j+1)*3+1),Points.get((j+1)*3+2));
                Point3d p3=new Point3d(Points.get((j+2)*3),Points.get((j+2)*3+1),Points.get((j+2)*3+2));
                Vector3d n=Math3d.getNormal(p1, p2, p3);
                double ua=n.angle(yp);
                int x1 = (key*3+0) % textures;
                int y1 = (int) Math.floor((key*3+0) / (float)textures) % textures;
                int x2 = (key*3+1) % textures;
                int y2 = (int) Math.floor((key*3+1) / (float)textures) % textures;
                int x3 = (key*3+2) % textures;
                int y3 = (int) Math.floor((key*3+2) / (float)textures) % textures;
                if(ua<(Math.PI/4)){
                    Texture.add((float)((x1+0.5+((p1.x-location.x)*0.92))/textures));
                    Texture.add((float)((y1+0.5+((p1.z-location.z)*0.92))/textures));
                    Texture.add((float)((x1+0.5+((p2.x-location.x)*0.92))/textures));
                    Texture.add((float)((y1+0.5+((p2.z-location.z)*0.92))/textures));
                    Texture.add((float)((x1+0.5+((p3.x-location.x)*0.92))/textures));
                    Texture.add((float)((y1+0.5+((p3.z-location.z)*0.92))/textures));
                    up++;
                }else if(ua>(Math.PI*3/4)){
                    Texture.add((float)((x3+0.5+((p1.x-location.x)*0.92))/textures));
                    Texture.add((float)((y3+0.5+((p1.z-location.z)*0.92))/textures));
                    Texture.add((float)((x3+0.5+((p2.x-location.x)*0.92))/textures));
                    Texture.add((float)((y3+0.5+((p2.z-location.z)*0.92))/textures));
                    Texture.add((float)((x3+0.5+((p3.x-location.x)*0.92))/textures));
                    Texture.add((float)((y3+0.5+((p3.z-location.z)*0.92))/textures));
                }else{
                    n=new Vector3d(n.x,0,n.z);
                    n.normalize();
                    double xpa=n.angle(xp);
                    if(xpa>=(Math.PI/4)&&xpa<=(Math.PI*3/4)){
                        Texture.add((float)((x2+0.5+((p1.x-location.x)*0.92))/textures));
                        Texture.add((float)((y2+0.5+((p1.y-location.y)*0.92))/textures));
                        Texture.add((float)((x2+0.5+((p2.x-location.x)*0.92))/textures));
                        Texture.add((float)((y2+0.5+((p2.y-location.y)*0.92))/textures));
                        Texture.add((float)((x2+0.5+((p3.x-location.x)*0.92))/textures));
                        Texture.add((float)((y2+0.5+((p3.y-location.y)*0.92))/textures));
                    }else{
                        Texture.add((float)((x2+0.5+((p1.z-location.z)*0.92))/textures));
                        Texture.add((float)((y2+0.5+((p1.y-location.y)*0.92))/textures));
                        Texture.add((float)((x2+0.5+((p2.z-location.z)*0.92))/textures));
                        Texture.add((float)((y2+0.5+((p2.y-location.y)*0.92))/textures));
                        Texture.add((float)((x2+0.5+((p3.z-location.z)*0.92))/textures));
                        Texture.add((float)((y2+0.5+((p3.y-location.y)*0.92))/textures));
                    }
                }
            }
        }
    }
    public Vector<Float> getPoints(){
        return Points;
    }
    public Vector<Float> getTexture(){
        return Texture;
    }

    boolean isAir() {
        return bound.isEmpty();
    }
    boolean hasAir(){
        return Math3d.VolumeOfMesh(bound) <= (1.0d-TOL);
    }

    Point3d getCrossPoint(Line3d l) {
        return Math3d.lineSolidIntersection(bound, l);
    }
    public void setChanged(boolean b){
        change=b;
    }
    public boolean isChanged(){
        return change;
    }
}
