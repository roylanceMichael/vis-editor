
package pl.kotcrab.vis.editor.ui.components;

import pl.kotcrab.vis.editor.ui.Focusable;
import pl.kotcrab.vis.editor.ui.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/** A checkbox is a button that contains an image indicating the checked or unchecked state and a label.
 * @author Nathan Sweet
 * @author Paweł Pastuszak */
@SuppressWarnings("rawtypes")
public class VisCheckBox extends TextButton implements Focusable {
	// This class were copied from LibGDX, few lines were changed.

	private Image image;
	private Cell imageCell;
	private VisCheckBoxStyle style;

	private boolean drawBorder;

	public VisCheckBox (String text, Skin skin) {
		this(text, skin.get(VisCheckBoxStyle.class));
	}

	public VisCheckBox (String text, Skin skin, String styleName) {
		this(text, skin.get(styleName, VisCheckBoxStyle.class));
	}

	public VisCheckBox (String text, VisCheckBoxStyle style) {
		super(text, style);
		clearChildren();
		imageCell = add(image = new Image(style.checkboxOff));
		Label label = getLabel();
		add(label);
		label.setAlignment(Align.left);
		setSize(getPrefWidth(), getPrefHeight());

		addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				UI.focusManager.requestFocus(VisCheckBox.this);
				return false;
			}
		});
	}

	@Override
	public void setStyle (ButtonStyle style) {
		if (!(style instanceof VisCheckBoxStyle)) throw new IllegalArgumentException("style must be a VisCheckBoxStyle.");
		super.setStyle(style);
		this.style = (VisCheckBoxStyle)style;
	}

	/** Returns the checkbox's style. Modifying the returned style may not have an effect until {@link #setStyle(ButtonStyle)} is
	 * called. */
	@Override
	public CheckBoxStyle getStyle () {
		return style;
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		Drawable checkbox = null;
		if (isDisabled()) {
			if (isChecked() && style.checkboxOnDisabled != null)
				checkbox = style.checkboxOnDisabled;
			else
				checkbox = style.checkboxOffDisabled;
		}
		if (checkbox == null) {
			if (isChecked()) {
				if (isOver())
					checkbox = style.checkboxOnOver;
				else
					checkbox = style.checkboxOn;
			} else {
				if (isOver())
					checkbox = style.checkboxOver;
				else
					checkbox = style.checkboxOff;
			}
		}
		image.setDrawable(checkbox);
		super.draw(batch, parentAlpha);

		Vector2 pos = image.localToParentCoordinates(new Vector2(0, 0));
		if (drawBorder) style.focusBorder.draw(batch, getX(), getY() + image.getWidth() / 3, image.getWidth(), image.getHeight());
	}

	public Image getImage () {
		return image;
	}

	public Cell getImageCell () {
		return imageCell;
	}

	static public class VisCheckBoxStyle extends CheckBoxStyle {
		public Drawable checkboxOnOver;
		public Drawable focusBorder;

		public VisCheckBoxStyle () {
			super();
		}

		public VisCheckBoxStyle (Drawable checkboxOff, Drawable checkboxOn, BitmapFont font, Color fontColor) {
			super(checkboxOff, checkboxOn, font, fontColor);
		}

		public VisCheckBoxStyle (VisCheckBoxStyle style) {
			super(style);
			this.checkboxOnOver = style.checkboxOnOver;
			this.focusBorder = style.focusBorder;
		}
	}

	@Override
	public void focusLost () {
		drawBorder = false;
	}

	@Override
	public void focusGained () {
		drawBorder = true;
	}
}
