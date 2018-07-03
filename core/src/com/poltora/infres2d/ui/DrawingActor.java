package com.poltora.infres2d.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.Disposable;
import com.poltora.infres2d.ResourceManager;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DrawingActor extends Widget implements Disposable {
    private List<Vector2> points;

    private ShapeRenderer renderer;
    private OrthographicCamera camera;
    private Vector2 offset;
    private float cameraSpeed;

    private Lock updateLock;

    public DrawingActor() {
        points = new LinkedList<>();
        renderer = new ShapeRenderer();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        offset = new Vector2();
        updateLock = new ReentrantLock();
        cameraSpeed = 3.0f;

        camera.position.set(0.0f, 0.0f, 0.0f);
        camera.update();
        renderer.getProjectionMatrix().set(camera.combined);
        renderer.updateMatrices();

        ResourceManager.INPUT_EXEC_SERVICE.scheduleAtFixedRate(() -> {
            try {
                updateLock.lock();
                camera.translate(offset);
                camera.update();
                renderer.getProjectionMatrix().set(camera.combined);
                renderer.updateMatrices();
            } finally {
                updateLock.unlock();
            }
        }, 0, 10, TimeUnit.MILLISECONDS);

        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                boolean handled;

                try {
                    updateLock.lock();
                    switch (keycode) {
                        case Input.Keys.W:
                            offset.y += cameraSpeed;
                            handled = true;
                            break;
                        case Input.Keys.S:
                            offset.y -= cameraSpeed;
                            handled = true;
                            break;
                        case Input.Keys.A:
                            offset.x -= cameraSpeed;
                            handled = true;
                            break;
                        case Input.Keys.D:
                            offset.x += cameraSpeed;
                            handled = true;
                            break;
                        default:
                            handled = false;
                    }
                } finally {
                    updateLock.unlock();
                }

                return handled;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                boolean handled;

                try {
                    updateLock.lock();
                    switch (keycode) {
                        case Input.Keys.W:
                            offset.y -= cameraSpeed;
                            handled = true;
                            break;
                        case Input.Keys.S:
                            offset.y += cameraSpeed;
                            handled = true;
                            break;
                        case Input.Keys.A:
                            offset.x += cameraSpeed;
                            handled = true;
                            break;
                        case Input.Keys.D:
                            offset.x -= cameraSpeed;
                            handled = true;
                            break;
                        default:
                            handled = false;
                    }
                } finally {
                    updateLock.unlock();
                }

                return handled;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                try {
                    updateLock.lock();
                    points.add(new Vector2(camera.position.x + x - camera.viewportWidth / 2.0f,
                            camera.position.y + y - camera.viewportHeight / 2.0f));
                } finally {
                    updateLock.unlock();
                }
                return false;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();

        try {
            updateLock.lock();
            renderer.begin(ShapeRenderer.ShapeType.Point);

            for (Vector2 point : points) {
                renderer.point(point.x, point.y, 0.0f);
            }

            renderer.end();
        } finally {
            updateLock.unlock();
        }

        batch.begin();
    }

    public void resize() {
        try {
            updateLock.lock();
            camera.viewportWidth = getWidth();
            camera.viewportHeight = getHeight();
            camera.update();
            renderer.getProjectionMatrix().set(camera.combined);
            renderer.updateMatrices();
        } finally {
            updateLock.unlock();
        }
    }

    @Override
    public void dispose() {
        points.clear();
        renderer.dispose();
    }
}
