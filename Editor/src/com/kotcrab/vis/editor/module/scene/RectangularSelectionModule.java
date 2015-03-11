/*
 * Copyright 2014-2015 Pawel Pastuszak
 *
 * This file is part of VisEditor.
 *
 * VisEditor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VisEditor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VisEditor.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kotcrab.vis.editor.module.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.editor.scene.EditorEntity;

public class RectangularSelectionModule extends SceneModule {
	private CameraModule cameraModule;
	private EntityManipulatorModule entityManipulatorModule;

	private ShapeRenderer shapeRenderer;

	private Array<EditorEntity> entities;

	private Rectangle currentRect = null;
	private Rectangle rectToDraw = null;
	private Rectangle previousRectDrawn = new Rectangle();

	@Override
	public void added () {
		//must be added after EntityManipulatorModule
		entityManipulatorModule = sceneContainer.get(EntityManipulatorModule.class);
	}

	@Override
	public void init () {
		this.entities = scene.entities;
		cameraModule = sceneContainer.get(CameraModule.class);
		shapeRenderer = sceneContainer.get(RendererModule.class).getShapeRenderer();
	}

	@Override
	public void render (Batch batch) {
		if (rectToDraw != null) {
			Gdx.gl20.glEnable(GL20.GL_BLEND);

			shapeRenderer.setColor(0.11f, 0.63f, 0.89f, 1);
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.rect(rectToDraw.getX(), rectToDraw.getY(), rectToDraw.getWidth(), rectToDraw.getHeight());
			shapeRenderer.end();

			shapeRenderer.setColor(0.05f, 0.33f, 0.49f, 0.3f);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.rect(rectToDraw.getX(), rectToDraw.getY(), rectToDraw.getWidth(), rectToDraw.getHeight());
			shapeRenderer.end();
		}
	}

	public void findContainedComponents () {
		Array<EditorEntity> matchingEntities = new Array<>();

		for (EditorEntity entity : entities)
			if (rectToDraw.contains(entity.getBoundingRectangle())) matchingEntities.add(entity);

		entityManipulatorModule.resetSelection();
		matchingEntities.forEach(entityManipulatorModule::selectAppend);
	}

	@Override
	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		x = cameraModule.getInputX();
		y = cameraModule.getInputY();

		if (button == Buttons.LEFT && entityManipulatorModule.isTouchDownMouseOnEntity() == false ) {
			currentRect = new Rectangle(x, y, 0, 0);
			updateDrawableRect();
			return true;
		}

		return false;
	}

	@Override
	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
		x = cameraModule.getInputX();
		y = cameraModule.getInputY();

		if(rectToDraw != null) {
			if (!Gdx.input.isButtonPressed(Buttons.LEFT)) {
				findContainedComponents();
				rectToDraw = null;
			}
		}
	}

	@Override
	public void touchDragged (InputEvent event, float x, float y, int pointer) {
		x = cameraModule.getInputX();
		y = cameraModule.getInputY();

		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			currentRect.setSize(x - currentRect.x, y - currentRect.y);
			updateDrawableRect();
		}

	}

	private void updateDrawableRect () {
		float x = currentRect.x;
		float y = currentRect.y;
		float width = currentRect.width;
		float height = currentRect.height;

		// Make the width and height positive, if necessary.
		if (width < 0) {
			width = 0 - width;
			x = x - width + 1;
		}

		if (height < 0) {
			height = 0 - height;
			y = y - height + 1;
		}

		// Update rectToDraw after saving old value.
		if (rectToDraw != null) {
			previousRectDrawn.set(rectToDraw.x, rectToDraw.y, rectToDraw.width, rectToDraw.height);
			rectToDraw.set(x, y, width, height);
		} else {
			rectToDraw = new Rectangle(x, y, width, height);
		}
	}
}
