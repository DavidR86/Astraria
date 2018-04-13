package com.mygdx.game.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.simulation.logic.Body;
import com.mygdx.game.simulation.renderer.Renderer;
import com.mygdx.game.simulation.renderer.Sphere;
import com.mygdx.game.simulation.renderer.Warehouse;

/**
 * Created by Fran on 4/12/2018.
 */
public class SaveFileManager {
    public static void loadGame(SimulationScreen simulationScreen, Renderer renderer, FileHandle file){
        simulationScreen.clearObjects();

        String str = file.readString();
        String part[] = str.replaceAll("\\r", "").split("\\n");

        String name;
        float radius;
        double mass;
        double vX, vY, vZ;
        double x, y, z;
        String texturePath;
        float r, g, b, a;
        float ar, ag, ab, aa;
        float temperature;

        if(part[0].contains("v3")){
            int i = 1;

            while(i < part.length - 2){
                String[] type = part[i].split(" ");
                i++;

                if(type.length<2){
                    continue;
                }

                if(type[1].equals("planet")){

                    name = part[i].substring(part[i].indexOf(':')+2);
                    i++;

                    radius = Float.parseFloat(part[i].split(" ")[1]);
                    i++;

                    mass = Double.parseDouble(part[i].split(" ")[1]);
                    i++;

                    String[] vel =part[i].split(" ");
                    vX = Double.parseDouble(vel[1]);
                    vY = Double.parseDouble(vel[2]);
                    vZ = Double.parseDouble(vel[3]);
                    i++;

                    String[] pos = part[i].split(" ");
                    x = Double.parseDouble(pos[1]);
                    y = Double.parseDouble(pos[2]);
                    z = Double.parseDouble(pos[3]);
                    i++;

                    texturePath = part[i].substring(part[i].indexOf(':')+2);
                    i++;

                    String[] orbitCol = part[i].split(" ");
                    r = Float.parseFloat(orbitCol[1]);
                    g = Float.parseFloat(orbitCol[2]);
                    b = Float.parseFloat(orbitCol[3]);
                    a = Float.parseFloat(orbitCol[4]);
                    i++;
                    Sphere s = new Sphere(renderer.getTransformation(),
                            Warehouse.getOpenGLTextureManager().addTexture(Gdx.files.internal(texturePath).path()));
                    simulationScreen.addObject(new SimulationObject(s, renderer.getPlanetShader(),
                            radius,name,renderer.getTransformation(),new Body(mass,x,y,z,vX,vY,vZ)));
                } else if (type[1].equals("star")){


                    name = part[i].substring(part[i].indexOf(':')+2);
                    i++;

                    radius = Float.parseFloat(part[i].split(" ")[1]);
                    i++;

                    mass = Double.parseDouble(part[i].split(" ")[1]);
                    i++;

                    String[] vel =part[i].split(" ");
                    vX = Double.parseDouble(vel[1]);
                    vY = Double.parseDouble(vel[2]);
                    vZ = Double.parseDouble(vel[3]);
                    i++;

                    String[] pos = part[i].split(" ");
                    x = Double.parseDouble(pos[1]);
                    y = Double.parseDouble(pos[2]);
                    z = Double.parseDouble(pos[3]);
                    i++;

                    texturePath = part[i].substring(part[i].indexOf(':')+2);
                    i++;

                    String[] orbitCol = part[i].split(" ");
                    r = Float.parseFloat(orbitCol[1]);
                    g = Float.parseFloat(orbitCol[2]);
                    b = Float.parseFloat(orbitCol[3]);
                    a = Float.parseFloat(orbitCol[4]);
                    i++;

                    temperature = Float.parseFloat(part[i].split(" ")[1]);
                    i++;

                    Sphere s = new Sphere(renderer.getTransformation(),
                            Warehouse.getOpenGLTextureManager().addTexture(Gdx.files.internal(texturePath).path()));
                    simulationScreen.addObject(new Star(s, renderer, radius, name, renderer.getTransformation(),
                            new Body(mass, x, y, z, vX, vY, vZ), temperature));
                } else if (type[1].equals("planet_atmo")){

                    name = part[i].substring(part[i].indexOf(':')+2);
                    i++;

                    radius = Float.parseFloat(part[i].split(" ")[1]);
                    i++;

                    mass = Double.parseDouble(part[i].split(" ")[1]);
                    i++;

                    String[] vel =part[i].split(" ");
                    vX = Double.parseDouble(vel[1]);
                    vY = Double.parseDouble(vel[2]);
                    vZ = Double.parseDouble(vel[3]);
                    i++;

                    String[] pos = part[i].split(" ");
                    x = Double.parseDouble(pos[1]);
                    y = Double.parseDouble(pos[2]);
                    z = Double.parseDouble(pos[3]);
                    i++;

                    texturePath = part[i].substring(part[i].indexOf(':')+2);
                    i++;

                    String[] orbitCol = part[i].split(" ");
                    r = Float.parseFloat(orbitCol[1]);
                    g = Float.parseFloat(orbitCol[2]);
                    b = Float.parseFloat(orbitCol[3]);
                    a = Float.parseFloat(orbitCol[4]);
                    i++;

                    String[] atmoCol = part[i].split(" ");
                    ar = Float.parseFloat(atmoCol[1]);
                    ag = Float.parseFloat(atmoCol[2]);
                    ab = Float.parseFloat(atmoCol[3]);
                    aa = Float.parseFloat(atmoCol[4]);
                    i++;

                    Sphere s = new Sphere(renderer.getTransformation(),
                            Warehouse.getOpenGLTextureManager().addTexture(Gdx.files.internal(texturePath).path()));

                    simulationScreen.addObject(new AtmospherePlanet(s, renderer.getPlanetAtmoShader(), radius, name,
                            renderer.getTransformation(), new Body(mass,x,y,z,vX,vY,vZ),renderer.getLightSourceManager(),
                            new Color(ar,ag,ab,aa)));
                }
            }
        }
    }
}