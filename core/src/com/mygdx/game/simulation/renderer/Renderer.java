package com.mygdx.game.simulation.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Disposable;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.nio.FloatBuffer;

/**
 * Created by Fran on 3/14/2018.
 */
public class Renderer implements Disposable{

    float total;

    Camera camera;

    Shader shader;

    Transformation transformation;
    private static float FOV =(float)Math.toRadians(45f);
    OpenGLTextureManager openGLTextureManager;

    Model model;


    public Renderer() {
        openGLTextureManager = new OpenGLTextureManager();

        new GLProfiler(Gdx.graphics).enable();
        transformation = new Transformation();

        camera = new Camera();
        camera.movePosition(0,0,3);

        shader = new Shader(Gdx.files.internal("shaders/default.vert"), Gdx.files.internal("shaders/default.frag"));
        model = new Model(openGLTextureManager, "sphere.obj", shader);

        shader.use();

        try {
            shader.createUniform("diffuseTex");
            shader.createUniform("projection");
            shader.createUniform("modelView");
        } catch (Exception e) {
            e.printStackTrace();
        }



        Gdx.gl.glEnable(Gdx.gl.GL_DEPTH_TEST);
    }

    public Camera getCamera() {
        return camera;
    }

    public void render(float delta){
        Gdx.gl.glClearColor(0.0f,0.0f,0.0f,1.0f);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT | Gdx.gl.GL_DEPTH_BUFFER_BIT);

        total += delta*50;

        shader.use();
        Matrix4f projection = transformation.getProjectionMatrix(FOV, 800,600,0.01f,1000f);
        Matrix4f modelView = transformation.getModelViewMatrix(transformation.getViewMatrix(camera),new Vector3f(),new Vector3f(0,total*(float)Math.toRadians(50f),0),1f);


        shader.setMat4("projection", projection);
        shader.setMat4("modelView", modelView);

        this.model.render(modelView);

    }



    @Override
    public void dispose() {

    }
}
