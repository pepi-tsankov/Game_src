package org.lwjglb.game;

import org.J3DBool.Solid;
import org.J3dPackage.Line3d;
import org.J3dPackage.Point3d;
import org.J3dPackage.Vector3d;
import org.lwjglb.engine.GameEngine;
import org.lwjglb.engine.IGameLogic;
import org.maths.Math3d;
 
public class Main {
 
    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new DummyGame();
            GameEngine gameEng = new GameEngine("GAME", 600, 480, vSync, gameLogic);
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}