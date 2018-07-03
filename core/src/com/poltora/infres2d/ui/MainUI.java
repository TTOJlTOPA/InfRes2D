package com.poltora.infres2d.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MainUI extends Table {
    private DrawingActor drawingActor;

    public MainUI() {
        drawingActor = new DrawingActor();

        setFillParent(true);

        add(drawingActor).expand().fill();
    }

    public void setKeyboardFocus(Stage stage) {
        stage.setKeyboardFocus(drawingActor);
    }

    public void resize() {
        validate();
        drawingActor.resize();
    }
}
