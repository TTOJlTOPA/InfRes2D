package com.poltora.infres2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;

public class InfRes2DMain extends ApplicationAdapter {

    @Override
    public void create() {
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.14f, 0.14f, 0.14f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {
    }
}
