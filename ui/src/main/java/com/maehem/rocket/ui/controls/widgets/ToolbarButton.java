/*
    Licensed to the Apache Software Foundation (ASF) under one
    or more contributor license agreements.  See the NOTICE file
    distributed with this work for additional information
    regarding copyright ownership.  The ASF licenses this file
    to you under the Apache License, Version 2.0 (the
    "License"); you may not use this file except in compliance
    with the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.

*/
package com.maehem.rocket.ui.controls.widgets;

import com.maehem.rocket.ui.controls.Toolbar;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light.Distant;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author maehem
 */
public class ToolbarButton extends StackPane {

    private static final double ROUNDED = Toolbar.CORNER_ARC * 2;
    private Color dropShadowColor = new Color(0, 0, 0, 0.7);

    final Rectangle outerShape;

    public ToolbarButton(String hoverText, Image image, Color color, double w, double h) {
        setAccessibleText(hoverText);
        Tooltip tooltip = new Tooltip(hoverText);
        Tooltip.install(this, tooltip);
        
        outerShape = new Rectangle(w, h, color);
        outerShape.setArcHeight(ROUNDED);
        outerShape.setArcWidth(ROUNDED);

        Distant light = new Distant();
        light.setAzimuth(-135.0f);  //  TODO Create a global settings resource.

        Lighting l = new Lighting();
        l.setLight(light);
        l.setSpecularExponent(2.0);
        l.setSurfaceScale(5.0f);

        ImageView iv = new ImageView(image);
        iv.setPreserveRatio(true);
        iv.setFitWidth(w * 0.8);

        outerShape.setEffect(l);
        getChildren().addAll(outerShape, iv);
    }

    public Paint getPaint() {
        return outerShape.getFill();
    }

    public void setDropShadow(boolean set) {
        if (set) {
            // Drop shadow
            DropShadow ds = new DropShadow();
            ds.setRadius(20.0);
            ds.setColor(dropShadowColor);
            outerShape.setEffect(ds);
        } else {
            outerShape.setEffect(null);
        }
    }
    
    public void setDropShadowColor( Color c ) {
        dropShadowColor = c;
        setDropShadow(outerShape.getEffect() != null);
    }
}
