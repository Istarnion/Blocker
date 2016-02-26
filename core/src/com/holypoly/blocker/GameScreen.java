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
        shapeRenderer.identity();
        shapeRenderer.translate(-7.5f, -7.5f, -7.5f);
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                for (int z = 0; z < grid[0][0].length; z++) {
                    if (z == 1) {
                        shapeRenderer.setColor(1, 1, 1, 1);
                    }
                    else {
                        shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 0.1f);
                    }
                    shapeRenderer.box(x*5, y*5, z*5, 5, 5, 5);
                }
            }
        }
        shapeRenderer.end();

        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.identity();
        shapeRenderer.translate(-7.5f, -7.5f, -7.5f);
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                for (int z = 0; z < grid[0][0].length; z++) {
                        if (x == 0 && y == 1 && z == 1) {
                            shapeRenderer.setColor(0.7f, 0, 0, 0.5f);
                            shapeRenderer.box(x*5+0.5f, y*5+0.5f, z*5-0.5f, 4f, 4f, 4f);
                        }
                        if (x == 0 && y == 1 && z == 0) {
                            shapeRenderer.setColor(0f, 0, 0.7f, 0.5f);
                            shapeRenderer.box(x*5+0.25f, y*5+0.25f, z*5-0.25f, 4.5f, 4.5f, 4.5f);
                        }
                        if (x == 2 && y == 0 && z == 0) {
                            shapeRenderer.setColor(0f, 0, 0.7f, 0.5f);
                            shapeRenderer.box(x*5+0.25f, y*5+0.25f, z*5-0.25f, 4.5f, 4.5f, 4.5f);
                        }
                        if (x == 2 && y == 0 && z == 1) {
                            shapeRenderer.setColor(0f, 0, 0.7f, 0.5f);
                            shapeRenderer.box(x*5+0.25f, y*5+0.25f, z*5-0.25f, 4.5f, 4.5f, 4.5f);
                        }
                        if (x == 1 && y == 2 && z == 1) {
                            shapeRenderer.setColor(0f, 0, 0.7f, 0.5f);
                            shapeRenderer.box(x*5+0.25f, y*5+0.25f, z*5-0.25f, 4.5f, 4.5f, 4.5f);
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
