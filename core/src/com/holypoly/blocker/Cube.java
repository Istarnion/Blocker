package com.holypoly.blocker;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 *
 * @author istarnion
 */
public class Cube {

    private final static float SIZE = 5.0f;
    
    public final static Color WHITE = new Color(0.9f, 0.9f, 0.9f, 1);
    public final static Color PINK = new Color(0.8f, 0, 0.75f, 0.3f);
    
    private Cube() {}
    
    public static void render(ShapeRenderer r, Cell cell, float x, float y, float z) {
        Cube.render(r, cell, x, y, z, 1.0f);
    }
    
    public static void render(ShapeRenderer r, Cell cell, float x, float y, float z, float scale) {
        switch(cell) {
            case PLAYER:
                r.setColor(WHITE);
                break;
            case DANGER:
                r.setColor(PINK);
                break;
            default:
                break;
        }
        
        
        float w = SIZE*scale;
        float h = SIZE*scale;
        float d = SIZE*scale;
        r.box(x-w/2, y-h/2, z-d/2, w, h, d);
    }
}
