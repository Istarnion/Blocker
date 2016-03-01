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
    int camXXSign = 1;
    int camYXSign = 0;
    int camZXSign = 0;
    int camXYSign = 0;
    int camYYSign = 1;
    int camZYSign = 0;
    
    private final ShapeRenderer shapeRenderer;

    private final CubeRenderer cubeRenderer;
    
    private final GridRenderer gridRenderer;
    
    Direction rotating = Direction.NONE;
    float angle = 0;
    
    final float SWIPE_EPSILON = 10f;
    
    float theta = 0;    // A value fluctuating between 0 and 1. based on the function updateTheta()
    float time = 0;     // How much time has passed. Wraps around to 0 from 1. Used to calculate theta.
    
    int[] xCoords = new int[27];
    int[] yCoords = new int[27];
    int[] zCoords = new int[27];
    
    Direction playerDir = Direction.RIGHT;
    int playerLength = 1;
    
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
        
        updateTheta(delta);
        
        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeType.Line);
        gridRenderer.renderGrid(shapeRenderer, 3, 1);
        shapeRenderer.end();
        
        shapeRenderer.begin(ShapeType.Filled);
        for (int i=0; i<playerLength; i++) {
            cubeRenderer.render(shapeRenderer, Cell.PLAYER, xCoords[i], yCoords[i], zCoords[i]);
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
            // Rotation of the cube
            if (Gdx.input.isKeyJustPressed(Input.Keys.A) && rotating == Direction.NONE) {
                setCamPos(Direction.LEFT);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.D) && rotating == Direction.NONE) {
                setCamPos(Direction.RIGHT);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.W) && rotating == Direction.NONE) {
                setCamPos(Direction.UP);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.S) && rotating == Direction.NONE) {
                setCamPos(Direction.DOWN);
            }
            
            // Direction
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                playerDir = Direction.LEFT;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                playerDir = Direction.RIGHT;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                playerDir = Direction.UP;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                playerDir = Direction.DOWN;
            }
        }
        
        if (rotating == Direction.NONE && (main.touch || (!main.touch && Gdx.input.isButtonPressed(0)))) {
            if (Gdx.input.getDeltaX() > SWIPE_EPSILON && Gdx.input.getDeltaX() > Gdx.input.getDeltaY()) {
                setCamPos(Direction.RIGHT);
            }
            if (Gdx.input.getDeltaX() < -SWIPE_EPSILON && Gdx.input.getDeltaX() < Gdx.input.getDeltaY()) {
                setCamPos(Direction.LEFT);
            }
            if (Gdx.input.getDeltaY() > SWIPE_EPSILON && Gdx.input.getDeltaY() > Gdx.input.getDeltaX()) {
                setCamPos(Direction.DOWN);
            }
            if (Gdx.input.getDeltaY() < -SWIPE_EPSILON && Gdx.input.getDeltaY() < Gdx.input.getDeltaX()) {
                setCamPos(Direction.UP);
            }
        }
    }
    
    private void setCamPos(Direction dir) {
        rotating = dir;
        
        switch (dir) {
            case UP:
                if (camYYSign > 0) {
                    camYYSign = 0;
                    camZYSign = -1;
                }
                else if (camYYSign < 0) {
                    camYYSign = 0;
                    camZYSign = 1;
                }
                else if (camZYSign > 0) {
                    camYYSign = 1;
                    camZYSign = 0;
                }
                else if (camZYSign < 0) {
                    camYYSign = -1;
                    camZYSign = 0;
                }
                break;
            case DOWN:
                if (camYYSign > 0) {
                    camYYSign = 0;
                    camZYSign = 1;
                }
                else if (camYYSign < 0) {
                    camYYSign = 0;
                    camZYSign = -1;
                }
                else if (camZYSign > 0) {
                    camYYSign = -1;
                    camZYSign = 0;
                }
                else if (camZYSign < 0) {
                    camYYSign = 1;
                    camZYSign = 0;
                }
                break;
            case RIGHT:
                if (camXXSign > 0) {
                    camXXSign = 0;
                    camZXSign = 1;
                }
                else if (camXXSign < 0) {
                    camXXSign = 0;
                    camZXSign = -1;
                }
                else if (camZXSign > 0) {
                    camXXSign = -1;
                    camZXSign = 0;
                }
                else if (camZXSign < 0) {
                    camXXSign = 1;
                    camZXSign = 0;
                }
                break;
            case LEFT:
                if (camXXSign > 0) {
                    camXXSign = 0;
                    camZXSign = -1;
                }
                else if (camXXSign < 0) {
                    camXXSign = 0;
                    camZXSign = 1;
                }
                else if (camZXSign > 0) {
                    camXXSign = 1;
                    camZXSign = 0;
                }
                else if (camZXSign < 0) {
                    camXXSign = -1;
                    camZXSign = 0;
                }
                break;
            default:
                break;
        }
    }
    
    public void updateTheta(float delta) {
        time += delta*3f;
        if(time > 1.0) {
            time -= 1.0;
            updatePlayer();
        }
        
        double a = 1;
        double t1 = 0.1f;
        double t2 = 1;
        
        float prev = theta;
        
        if (time < t1) {
            theta = (float)(a * ((time+t1)/t1));
        }
        else {
            theta = (float)(a * ((t2-(time+t1))/(t2-t1)));
        }
    }
    
    private void updatePlayer() {
        for (int i=0; i<playerLength; i++) {
            switch (playerDir) {
                case RIGHT:
                    xCoords[playerLength] = xCoords[playerLength-1]+camXXSign;
                    yCoords[playerLength] = yCoords[playerLength-1]+camYXSign;
                    zCoords[playerLength] = zCoords[playerLength-1]+camZXSign;
                    break;
                case LEFT:
                    xCoords[playerLength] = xCoords[playerLength-1]-camXXSign;
                    yCoords[playerLength] = yCoords[playerLength-1]-camYXSign;
                    zCoords[playerLength] = zCoords[playerLength-1]-camZXSign;
                    break;
                case UP:
                    xCoords[playerLength] = xCoords[playerLength-1]+camXYSign;
                    yCoords[playerLength] = yCoords[playerLength-1]+camYYSign;
                    zCoords[playerLength] = zCoords[playerLength-1]+camZYSign;
                    break;
                case DOWN:
                    xCoords[playerLength] = xCoords[playerLength-1]-camXYSign;
                    yCoords[playerLength] = yCoords[playerLength-1]-camYYSign;
                    zCoords[playerLength] = zCoords[playerLength-1]-camZYSign;
                    break;
                default:
                    break;
            }
            
            if (xCoords[playerLength] > 1) xCoords[playerLength] = -1;
            if (xCoords[playerLength] < -1) xCoords[playerLength] = 1;
            if (yCoords[playerLength] > 1) yCoords[playerLength] = -1;
            if (yCoords[playerLength] < -1) yCoords[playerLength] = 1;
            if (zCoords[playerLength] > 1) zCoords[playerLength] = -1;
            if (zCoords[playerLength] < -1) zCoords[playerLength] = 1;
            
            xCoords[i] = xCoords[i+1];
            yCoords[i] = yCoords[i+1];
            zCoords[i] = zCoords[i+1];
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
