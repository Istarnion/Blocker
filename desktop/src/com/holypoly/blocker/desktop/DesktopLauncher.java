package com.holypoly.blocker.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.holypoly.blocker.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 900;
        config.height = 500;
        config.resizable = false;
		new LwjglApplication(new Main(), config);
	}
}
