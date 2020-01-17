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
package com.maehem.rocket.ui.controls;

import com.maehem.rocket.engine.game.Game;
import com.maehem.rocket.ui.controls.widgets.GameSpeedWidget;
import com.maehem.rocket.ui.controls.widgets.StatWidgetArea;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 *
 * @author maehem
 */
public class InfoBar2 extends HBox {

    private static final Logger LOGGER = Logger.getLogger(InfoBar2.class.getName());

    private final GameSpeedWidget gameSpeed;
    
    public static final double CORNER_ARC = 10;
    public static final double HEIGHT = 100;
    public static final double PADDING = 10;
    public static final double STUB_WIDTH = 24;
    private static final double POPPED_X = 0;
    private static final double POP_STEPS = 10;

    private final Game game;
    private final DialogLayer dialogLayer;
    private final StatWidgetArea statWidgetArea;
    private final StackPane tabPopGlyph;
    private final StackPane tabTuckGlyph;
    private double tuckedX = -100;

    private boolean popped = true;

    public InfoBar2(Game game, DialogLayer dialogLayer) {
        this.game = game;
        this.dialogLayer = dialogLayer;
        
        setBackground(new Background(
                new BackgroundFill(Color.DARKGRAY, 
                new CornerRadii(0.0, Toolbar.CORNER_ARC,Toolbar.CORNER_ARC, 0.0, false), 
                Insets.EMPTY
        )));
        setPadding(new Insets(PADDING));
        setSpacing(20);

        // Drop shadow
        DropShadow ds = new DropShadow();
        ds.setOffsetY(5);
        ds.setOffsetX(-5);
        ds.setRadius(30.0);
        ds.setColor(Color.BLACK);
        setEffect(ds);

        tabPopGlyph = createGlyph("/glyphs/chevron-right.png");
        tabTuckGlyph = createGlyph("/glyphs/chevron-left.png");
        tabTuckGlyph.setVisible(true);
        tabPopGlyph.setVisible(false);

        tabPopGlyph.setOnMouseClicked((t) -> {
            if (!popped) {
                pop();
            }
        });
        tabTuckGlyph.setOnMouseClicked((t) -> {
            if (popped) {
                tuck();
            }
        });

        // We don't know the width of the bar until after it's displayed
        // so update our tucked value whenever the bar content is changed.
        widthProperty().addListener((o) -> {
            tuckedX = -getWidth() + STUB_WIDTH + PADDING;
        });
        
        StackPane tab = new StackPane(tabPopGlyph,tabTuckGlyph);


        gameSpeed = new GameSpeedWidget(game);
        statWidgetArea = new StatWidgetArea(game);

        getChildren().addAll(gameSpeed,statWidgetArea,tab);
    }
    
    public static StackPane createGlyph(String path) {
        try {
            URL glyphResource = InfoBar2.class.getResource(path);
            ImageView glyphImage = new ImageView(new Image(glyphResource.openStream()));
            glyphImage.setPreserveRatio(true);
            glyphImage.setFitWidth(STUB_WIDTH);
            //tabPopGlyph.getChildren().add(glyphImage);
            return new StackPane(glyphImage);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return new StackPane();
        }
    }

    public final void pop() {
        if (popped) {
            return;
        }
        popped = true;
        tabTuckGlyph.setVisible(true);
        tabPopGlyph.setVisible(false);

        AnimationTimer anim = new AnimationTimer() {
            double dist = tuckedX;
            double incr = Math.abs(POPPED_X - tuckedX) / POP_STEPS;

            @Override
            public void handle(long l) {
                if (dist < Math.abs(POPPED_X) - incr) {
                    dist += incr;
                    setTranslateX(dist);
                } else {
                    setTranslateX(POPPED_X);
                    this.stop();
                }
            }
        };
        anim.start();
    }

    public void tuck() {
        if (!popped) {
            return;
        }
        popped = false;
        tabTuckGlyph.setVisible(false);
        tabPopGlyph.setVisible(true);

        AnimationTimer anim = new AnimationTimer() {
            double dist = 0;

            double incr = getWidth() / POP_STEPS;

            @Override
            public void handle(long l) {
                if (dist < Math.abs(tuckedX) - incr) {
                    dist += incr;
                    setTranslateX(-dist);
                } else {
                    setTranslateX(tuckedX);
                    this.stop();
                }
            }
        };
        anim.start();
    }
}
