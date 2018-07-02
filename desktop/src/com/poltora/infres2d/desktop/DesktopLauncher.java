package com.poltora.infres2d.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.poltora.infres2d.InfRes2DMain;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setTitle("InfRes2D");
        config.setWindowedMode(1280, 720);
        config.setResizable(true);
        config.setIdleFPS(30);
        config.useVsync(true);
        config.useOpenGL3(true, 3, 0);

        new Lwjgl3Application(new InfRes2DMain(), config);
    }
}
