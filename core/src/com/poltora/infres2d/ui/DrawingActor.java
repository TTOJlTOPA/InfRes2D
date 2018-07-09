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
import com.poltora.infres2d.math.bezier.BezierCurve2D;
import com.poltora.infres2d.util.Vector2Byte;

import java.util.LinkedList;
import java.util.List;

public class DrawingActor extends Widget implements Disposable {
    private ShapeRenderer renderer;
    private OrthographicCamera camera;
    private Vector2Byte direction;
    private float cameraSpeed;

    private BezierCurve2D curve;

    public DrawingActor() {
        renderer = new ShapeRenderer();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        direction = new Vector2Byte();
        cameraSpeed = 300.0f;

        curve = new BezierCurve2D(new Vector2(0.0f, 0.0f), new Vector2(0.0f, 300.0f), new Vector2(500.0f, 250.0f));
        curve.calcDeCasteljauApprox(1.0f);

        camera.position.set(0.0f, 0.0f, 0.0f);
        camera.update();
        renderer.getProjectionMatrix().set(camera.combined);
        renderer.updateMatrices();

        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                boolean handled;

                switch (keycode) {
                    case Input.Keys.W:
                        direction.y++;
                        handled = true;
                        break;
                    case Input.Keys.S:
                        direction.y--;
                        handled = true;
                        break;
                    case Input.Keys.A:
                        direction.x--;
                        handled = true;
                        break;
                    case Input.Keys.D:
                        direction.x++;
                        handled = true;
                        break;
                    default:
                        handled = false;
                }

                return handled;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                boolean handled;

                switch (keycode) {
                    case Input.Keys.W:
                        direction.y--;
                        handled = true;
                        break;
                    case Input.Keys.S:
                        direction.y++;
                        handled = true;
                        break;
                    case Input.Keys.A:
                        direction.x++;
                        handled = true;
                        break;
                    case Input.Keys.D:
                        direction.x--;
                        handled = true;
                        break;
                    default:
                        handled = false;
                }

                return handled;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return false;
            }
        });
    }

    @Override
    public void act(float delta) {
        float speed;
        if ((direction.x | direction.y) != 0) {
            speed = (direction.x != 0 && direction.y != 0) ? cameraSpeed * delta * 0.707107f : cameraSpeed * delta;
            camera.translate(direction.x * speed, direction.y * speed);
            camera.update();
            renderer.getProjectionMatrix().set(camera.combined);
            renderer.updateMatrices();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();

        Gdx.gl30.glLineWidth(2.0f);

        renderer.begin(ShapeRenderer.ShapeType.Line);

        curve.draw(renderer);

        renderer.end();

        Gdx.gl30.glLineWidth(1.0f);

        batch.begin();
    }

    public void resize() {
        camera.viewportWidth = getWidth();
        camera.viewportHeight = getHeight();
        camera.update();
        renderer.getProjectionMatrix().set(camera.combined);
        renderer.updateMatrices();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
