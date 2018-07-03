package com.poltora.infres2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.poltora.infres2d.ui.MainUI;

public class InfRes2DMain extends ApplicationAdapter {
    private Stage stage;
    private MainUI mainUI;

    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());
        mainUI = new MainUI();

        Gdx.input.setInputProcessor(stage);

        stage.addActor(mainUI);
        mainUI.setKeyboardFocus(stage);
    }

    @Override
    public void render() {
        Gdx.gl30.glClearColor(0.14f, 0.14f, 0.14f, 1.0f);
        Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        mainUI.resize();
    }

    @Override
    public void dispose() {
        stage.dispose();
        ResourceManager.INPUT_EXEC_SERVICE.shutdown();
    }
}
