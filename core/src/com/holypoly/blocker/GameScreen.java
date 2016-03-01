package com.holypoly.blocker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    public PerspectiveCamera cam;

    private final ShapeRenderer shapeRenderer;

    private final CubeRenderer cubeRenderer;
    
    private final GridRenderer gridRenderer;
    
    Direction rotating = Direction.NONE;
    float angle = 0;
    
    final float SWIPE_EPSILON = 10f;
    
    public GameScreen(Main main) {
        super();
        this.main = main;

        cam = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.near = 0.1f;
        cam.far = 100;

        grid = new Cell[3][3][3];
        
        cam.position.set(0, 0, 5f);
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

        handleInput();
        
        handleRotations(delta);
        
        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeType.Line);
        gridRenderer.renderGrid(shapeRenderer, 3, 1);
        shapeRenderer.end();
        
        shapeRenderer.begin(ShapeType.Filled);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                for (int k = 0; k < grid[0][0].length; k++) {
                    float x = i-1;
                    float y = j-1;
                    float z = k-1;
                    if (i == 0 && j == 0 && k == 0) {
                        cubeRenderer.render(shapeRenderer, Cell.PLAYER, x, y, z, 0.9f);
                    }
                    
                    if (i == 0 && j == 1 && k == 0) {
                        cubeRenderer.render(shapeRenderer, Cell.DANGER, x, y, z);
                    }
                    if (i == 2 && j == 0 && k == 0) {
                        cubeRenderer.render(shapeRenderer, Cell.DANGER, x, y, z);
                    }
                    if (i == 2 && j == 0 && k == 1) {
                        cubeRenderer.render(shapeRenderer, Cell.DANGER, x, y, z);
                    }
                    if (i == 1 && j == 2 && k == 1) {
                        cubeRenderer.render(shapeRenderer, Cell.DANGER, x, y, z);
                    }
                }
            }
        }
        shapeRenderer.end();
    }

    private void handleRotations(float delta) {
        if(rotating != Direction.NONE) {
            Vector3 axis = null;
            float sign = 1;
            switch(rotating) {
                case LEFT:
                    sign = 1;
                    axis = cam.up;
                    break;
                case RIGHT:
                    sign = -1;
                    axis = cam.up;
                    break;
                case UP:
                    sign = -1;
                    axis = new Vector3(cam.up);
                    axis.crs(cam.direction);
                    break;
                case DOWN:
                    sign = 1;
                    axis = new Vector3(cam.up);
                    axis.crs(cam.direction);
                    break;
                default:
                    break;
            }
            
            float angleToRotate = delta * 720f;
            angle += angleToRotate;
            if(angle > 90f) {
                angleToRotate -= angle-90f;
                angle = 0;
                rotating = Direction.NONE;
            }
            cam.rotateAround(Vector3.Zero, axis, angleToRotate*sign);
            
            cam.update();
        }
    }
    
    public void handleInput() {
        if (!main.touch) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.A) && rotating == Direction.NONE) {
                rotating = Direction.LEFT;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.D) && rotating == Direction.NONE) {
                rotating = Direction.RIGHT;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.W) && rotating == Direction.NONE) {
                rotating = Direction.UP;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.S) && rotating == Direction.NONE) {
                rotating = Direction.DOWN;
            }
        }
        
        if (rotating == Direction.NONE && (main.touch || (!main.touch && Gdx.input.isButtonPressed(0)))) {
            if (Gdx.input.getDeltaX() > SWIPE_EPSILON && Gdx.input.getDeltaX() > Gdx.input.getDeltaY()) {
                rotating = Direction.RIGHT;
            }
            if (Gdx.input.getDeltaX() < -SWIPE_EPSILON && Gdx.input.getDeltaX() < Gdx.input.getDeltaY()) {
                rotating = Direction.LEFT;
            }
            if (Gdx.input.getDeltaY() > SWIPE_EPSILON && Gdx.input.getDeltaY() > Gdx.input.getDeltaX()) {
                rotating = Direction.DOWN;
            }
            if (Gdx.input.getDeltaY() < -SWIPE_EPSILON && Gdx.input.getDeltaY() < Gdx.input.getDeltaX()) {
                rotating = Direction.UP;
            }
        }
    }
    
    @Override
    public void resize(int width, int height) {
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
