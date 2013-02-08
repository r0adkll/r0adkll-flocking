package net.r0adkll.example.flocking;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "example";
		cfg.useGL20 = true;
		cfg.width = FlockMain.WIDTH;
		cfg.height = FlockMain.HEIGHT;
		
		new LwjglApplication(new FlockMain(), cfg);
	}
}
