package com.holypoly.blocker;

import com.badlogic.gdx.Game;

public class Main extends Game {

    @Override
    public void create() {
        this.screen = new GameScreen(this);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        this.screen.dispose();
    }
}
