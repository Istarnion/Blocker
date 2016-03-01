package com.holypoly.blocker;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 *
 * @author istarnion
 */
public class GridRenderer {
    
    public Color faded = new Color(0.5f, 0.5f, 0.5f, 0.25f);
    
    public Color focus = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    
    public void renderGrid(ShapeRenderer r, int size, float cellSize) {
        r.setColor(faded);
        
        r.translate(-(size*cellSize)/2, -(size*cellSize)/2, -(size*cellSize)/2);
        
        for (int x=0; x<size+1; x++) {
            for (int y=0; y<size+1; y++) {
                // Z - direction lines
                r.line(x*cellSize, y*cellSize, 0, x*cellSize, y*cellSize, size*cellSize);
            }
        }
        for (int y=0; y<size+1; y++) {
            for (int z=0; z<size+1; z++) {
                // X - direction lines
                r.line(0, y*cellSize, z*cellSize, size*cellSize, y*cellSize, z*cellSize);
            }
        }
        for (int z=0; z<size+1; z++) {
            for (int x=0; x<size+1; x++) {
                // Y - direction lines
                r.line(x*cellSize, 0, z*cellSize, x*cellSize, size*cellSize, z*cellSize);
            }
        }
        
        r.identity();
    }
}
