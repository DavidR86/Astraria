package com.mygdx.game.simulation.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.simulation.SimulationObject;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import org.joml.*;

import java.lang.Math;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Created by Fran on 3/14/2018.
 */
public class Renderer implements Disposable{

    private float total;

    private int screenWidth, screenHeight;

    private Camera camera;

    private Shader planetShader;

    private Shader starShader;

    private Transformation transformation;
    private static float FOV =(float)Math.toRadians(45f);
    private final OpenGLTextureManager openGLTextureManager;
    private final ModelManager modelManager;


    private LightSourceManager lightSourceManager;

    private final Vector3f temp;

    private final Matrix4f combined;


    public Renderer(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.combined = new Matrix4f();

        openGLTextureManager = new OpenGLTextureManager();
        this.modelManager = new ModelManager(openGLTextureManager);

        this.temp = new Vector3f();

        new GLProfiler(Gdx.graphics).enable();
        transformation = new Transformation();

        camera = new Camera(0, 0, 10);

        planetShader = new Shader(Gdx.files.internal("shaders/default.vert"), Gdx.files.internal("shaders/default.frag"));

        starShader =  new Shader(Gdx.files.internal("shaders/default.vert"), Gdx.files.internal("shaders/sunShader.frag"));


        lightSourceManager = new LightSourceManager(planetShader, camera, transformation);
        lightSourceManager.addLight(new PointLight(10,0,0));

        planetShader.use();

        try {
            planetShader.createUniform("diffuseTex");
            planetShader.createUniform("projection");
            planetShader.createUniform("modelView");
            planetShader.createUniform("og_farPlaneDistance");
            planetShader.createUniform("u_logarithmicDepthConstant");

        } catch (Exception e) {
            e.printStackTrace();
        }

        starShader.use();

        try {
            starShader.createUniform("projection");
            starShader.createUniform("modelView");
            starShader.createUniform("og_farPlaneDistance");
            starShader.createUniform("u_logarithmicDepthConstant");

        } catch (Exception e) {
            e.printStackTrace();
        }


        Gdx.gl.glEnable(Gdx.gl.GL_DEPTH_TEST);
    }

    public Camera getCamera() {
        return camera;
    }

    public void render(float delta, ArrayList<SimulationObject> toDraw){
        camera.update(delta);
        lightSourceManager.update();

        Gdx.gl.glClearColor(0.0f,0.0f,0.0f,1.0f);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT | Gdx.gl.GL_DEPTH_BUFFER_BIT);

        total += delta*50;

        planetShader.use();
        Matrix4f projection = transformation.getProjectionMatrix(FOV, screenWidth,screenHeight,1f,10000000f);
        combined.set(projection).mul(transformation.getViewMatrix(camera));

        planetShader.setFloat("og_farPlaneDistance", 10000000000f);
        planetShader.setFloat("u_logarithmicDepthConstant", 1f);
        planetShader.setMat4("projection", projection);

        starShader.use();
        starShader.setFloat("og_farPlaneDistance", 10000000000f);
        starShader.setFloat("u_logarithmicDepthConstant", 1f);
        starShader.setMat4("projection", projection);


        for(SimulationObject object : toDraw){
            object.render(camera);
        }

        //simulationObject.render(camera);
        //simulationObject2.render(camera);
    }

    public Vector3f projectPoint(Vector3f position){
        temp.set(position);
        temp.normalize();
        float res = temp.dot(camera.getFront());
        combined.project(position, new int[]{0,0,screenWidth,screenHeight}, position);

        if(res>0) {
            return position;
        } else {
            return position.set(-1,-1,-1);
        }
    }


    public void updateScreenSize(int width, int height){
        screenWidth = width;
        screenHeight = height;
    }


    public ModelManager getModelManager() {
        return modelManager;
    }

    public Shader getPlanetShader() {
        return planetShader;
    }

    public Transformation getTransformation() {
        return transformation;
    }

    @Override
    public void dispose() {

    }
}
