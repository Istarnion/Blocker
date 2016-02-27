package com.holypoly.blocker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

/**
 *
 * @author istarnion
 */
public class GameScreen implements Screen {

    public Main main;

    public Cell[][][] grid;


    //private FitViewport view;
    public PerspectiveCamera cam;

    private ShapeRenderer shapeRenderer;

    private CubeRenderer cubeRenderer;
    
    private GridRenderer gridRenderer;
    
    public GameScreen(Main main) {
        super();
        this.main = main;

        cam = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.near = 0.1f;
        cam.far = 100;
        //view = new FitViewport(900, 500, cam);

        grid = new Cell[3][3][3];

        cam.position.set(0, 0, 15f);
        cam.lookAt(Vector3.Zero);
        cam.update();

        shapeRenderer = new ShapeRenderer();

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glBlendEquation(GL20.GL_FUNC_ADD);
        
        cubeRenderer = new CubeRenderer();
        gridRenderer = new GridRenderer();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            cam.rotateAround(new Vector3(0, 0, -5f), Vector3.Y, delta*-90);
            cam.update();
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            cam.rotateAround(new Vector3(0, 0, -5f), Vector3.Y, delta*90);
            cam.update();
        }

        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeType.Line);
        gridRenderer.renderGrid(shapeRenderer, 3, 5);
        shapeRenderer.end();
        
        shapeRenderer.begin(ShapeType.Filled);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                for (int k = 0; k < grid[0][0].length; k++) {
                    float x = i-1f;
                    float y = j-1f;
                    float z = k-1f;
                    if (i == 0 && j == 1 && k == 1) {
                        cubeRenderer.render(shapeRenderer, Cell.PLAYER, x*5, y*5, z*5, 0.9f);
                    }
                    if (i == 0 && j == 1 && k == 0) {
                        cubeRenderer.render(shapeRenderer, Cell.DANGER, x*5, y*5, z*5);
                    }
                    if (i == 2 && j == 0 && k == 0) {
                        cubeRenderer.render(shapeRenderer, Cell.DANGER, x*5, y*5, z*5);
                    }
                    if (i == 2 && j == 0 && k == 1) {
                        cubeRenderer.render(shapeRenderer, Cell.DANGER, x*5, y*5, z*5);
                    }
                    if (i == 1 && j == 2 && k == 1) {
                        cubeRenderer.render(shapeRenderer, Cell.DANGER, x*5, y*5, z*5);
                    }
                }
            }
        }
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        //view.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

}
