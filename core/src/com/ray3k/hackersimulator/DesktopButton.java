/*
 * The MIT License
 *
 * Copyright 2017 Raymond Buckley.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.ray3k.hackersimulator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

public class DesktopButton extends Button {
    private DesktopButtonStyle style;
    private Image image;
    private Label label;
    private ClickListener clickListener;
    
    public DesktopButton(DesktopButtonStyle style) {
        super(new ButtonStyle());
        this.style = style;
        image = new Image(style.up);
        add(image).top().expand();
        
        row();
        LabelStyle labelStyle = new Label.LabelStyle(style.font, style.upFontColor);
        label = new Label(style.text, labelStyle);
        label.setWrap(true);
        label.setAlignment(Align.center);
        add(label).growX();
        
        clickListener = new ClickListener();
        this.addListener(clickListener);
    }
    
    public DesktopButton(String styleName, Skin skin) {
        this(skin.get(styleName, DesktopButtonStyle.class));
    }
    
    public DesktopButton(Skin skin) {
        this(skin.get(DesktopButtonStyle.class));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        
        if (clickListener.isVisualPressed()) {
            image.setDrawable(style.down);
            label.setColor(style.downFontColor);
        } else {
            image.setDrawable(style.up);
            label.setColor(style.upFontColor);
        }
    }
    
    public static class DesktopButtonStyle {
        public Drawable up, down;
        public Color upFontColor, downFontColor;
        public BitmapFont font;
        public String text;
        
        public DesktopButtonStyle() {
            
        }
        
        public DesktopButtonStyle(DesktopButtonStyle other) {
            up = other.up;
            down = other.down;
            upFontColor = other.upFontColor;
            downFontColor = other.downFontColor;
            font = other.font;
            text = other.text;
        }
    }
}
