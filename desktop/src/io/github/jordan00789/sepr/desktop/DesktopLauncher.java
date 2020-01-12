package io.github.jordan00789.sepr.desktop;


import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.jordan00789.sepr.Kroy;


public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Kroy-MENU");
		config.setWindowedMode(1280, 720);
		new Lwjgl3Application(new Kroy(), config);
	}
}
