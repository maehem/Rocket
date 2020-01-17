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

import com.maehem.rocket.engine.data.type.GameInfo.SPEED;
import com.maehem.rocket.engine.game.Game;
import com.maehem.rocket.engine.game.events.GameEvent;
import com.maehem.rocket.engine.game.events.GameListener;
import com.maehem.rocket.ui.controls.InfoBar2;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author maehem
 */
public class GameSpeedWidget extends VBox implements GameListener {

    private static final Logger LOGGER = Logger.getLogger(GameSpeedWidget.class.getName());

    private final static double GLYPH_WIDTH = 24;
    private final static String GLYPH_PATH = "/glyphs/turtle.png";

    public static final String SLOW_TXT = "Slow";
    public static final String MEDIUM_TXT = "Medium";
    public static final String FAST_TXT = "Fast";
    public static final String PLAID_TXT = "Plaid";

    public final ToggleGroup group;
    private ModalGlyph playGlyph;
    private ModalGlyph pauseGlyph;

    public GameSpeedWidget(Game game) {

        group = new ToggleGroup();
        
        setAlignment(Pos.CENTER);
        setSpacing(20);
        
        getChildren().addAll(playPauseSelector(game),speedSelector());
        
        game.addListener(this);
    }

    private HBox speedSelector() {
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        
        ToggleButton slowButton;
        ToggleButton mediumButton;
        ToggleButton fastButton;
        ToggleButton plaidButton;
        hb.setSpacing(2);

        CornerRadii cr = new CornerRadii(3);

//        tb1 = new ToggleButton("");
//        tb1.setMinWidth(32);
//        tb1.setId(PAUSE_TXT);
//        tb1.setTooltip(new Tooltip(PAUSE_TXT));
//        tb1.setBackground(new Background(new BackgroundFill(Color.RED, cr, Insets.EMPTY)));
//        tb1.setToggleGroup(group);
//        tb1.setSelected(true);
        slowButton = new ToggleButton("");
        slowButton.setMinWidth(32);
        slowButton.setId(SLOW_TXT);
        slowButton.setTooltip(new Tooltip(SLOW_TXT));
        slowButton.setBackground(new Background(new BackgroundFill(Color.ORANGE, cr, Insets.EMPTY)));
        slowButton.setToggleGroup(group);
        slowButton.setSelected(true);

        mediumButton = new ToggleButton("");
        mediumButton.setMinWidth(32);
        mediumButton.setId(MEDIUM_TXT);
        mediumButton.setTooltip(new Tooltip(MEDIUM_TXT));
        mediumButton.setBackground(new Background(new BackgroundFill(Color.YELLOW, cr, Insets.EMPTY)));
        mediumButton.setToggleGroup(group);

        fastButton = new ToggleButton("");
        fastButton.setMinWidth(32);
        fastButton.setId(FAST_TXT);
        fastButton.setTooltip(new Tooltip(FAST_TXT));
        fastButton.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, cr, Insets.EMPTY)));
        fastButton.setToggleGroup(group);

        plaidButton = new ToggleButton("");
        plaidButton.setMinWidth(32);
        plaidButton.setId(PLAID_TXT);
        plaidButton.setTooltip(new Tooltip(PLAID_TXT));
        try {
            Image image = new Image(GameSpeedWidget.class.getResource("/glyphs/plaid.png").openStream());
            BackgroundSize bs = new BackgroundSize(100, 100, true, true, false, true);
            BackgroundImage bis = new BackgroundImage(
                    image,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, bs
            );
            plaidButton.setBackground(new Background(bis));
        } catch (IOException ex) {
            Logger.getLogger(GameSpeedWidget.class.getName()).log(Level.SEVERE, null, ex);
        }

        plaidButton.setToggleGroup(group);

        hb.getChildren().addAll(createGlyph(GLYPH_PATH), slowButton, mediumButton, fastButton, plaidButton);

        updateToggleAppearance();

        // Change the speed toggle button appearance whenever a toggle's state changes.
        group.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
            if (group.getSelectedToggle() != null) {
                //System.out.println(group.getSelectedToggle().getUserData().toString());
                //LOGGER.info(group.getSelectedToggle().getUserData().toString());
                updateToggleAppearance();
            }
        });

        return hb;
    }

    private HBox playPauseSelector(Game game) {
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        
        Button playButton;
        Button pauseButton;

        playGlyph = new ModalGlyph("/glyphs/play-dark.png", "/glyphs/play-lit.png");
        playButton = new Button("", playGlyph);
        playButton.setLayoutX(170);
        playButton.setLayoutY(15);
        playButton.setOnAction((t) -> {
            game.setRunning(true);
        });

        pauseGlyph = new ModalGlyph("/glyphs/pause-dark.png", "/glyphs/pause-lit.png");
        pauseButton = new Button("", pauseGlyph);
        pauseButton.setLayoutX(120);
        pauseButton.setLayoutY(15);
        pauseButton.setOnAction((t) -> {
            game.setRunning(false);
        });

        hb.getChildren().addAll( pauseButton, playButton);
        
        return hb;
    }

    private StackPane createGlyph(String path) {
        try {
            URL glyphResource = InfoBar2.class.getResource(path);
            ImageView glyphImage = new ImageView(new Image(glyphResource.openStream()));
            glyphImage.setPreserveRatio(true);
            glyphImage.setFitWidth(GLYPH_WIDTH);
            //tabPopGlyph.getChildren().add(glyphImage);
            return new StackPane(glyphImage);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return new StackPane();
        }
    }

    private void updateToggleAppearance() {

        Light.Distant light = new Light.Distant();
        light.setAzimuth(-135.0f);
        light.setElevation(24);

        Lighting l = new Lighting();
        l.setLight(light);
        //l.setDiffuseConstant(5.8);
        //l.setSpecularConstant(2.0);
        l.setSpecularExponent(2.0);
        l.setSurfaceScale(5.0f);

        group.getToggles().forEach((tb) -> {
            if (tb.isSelected()) {
                ((ToggleButton) tb).setEffect(null);
            } else {
                ((ToggleButton) tb).setEffect(l);
            }
        });

    }

    @Override
    public void gameEvent(GameEvent e) {

        switch (e.type) {
            case DATA_LOADED:
                // Set the speed toggle based on the just-loaded game state data.
                group.selectToggle(
                        group.getToggles().get(e.getSource().data.mapInfo.speed.ordinal())
                );
                // Listen to the speed toggle buttons.
                group.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {                   
                    if (new_toggle != null) {
                        switch (((ToggleButton) new_toggle).getId()) {
                            case GameSpeedWidget.SLOW_TXT:
                                e.getSource().setGameSpeed(SPEED.SLOW);
                                break;
                            case GameSpeedWidget.MEDIUM_TXT:
                                e.getSource().setGameSpeed(SPEED.MEDIUM);
                                break;
                            case GameSpeedWidget.FAST_TXT:
                                e.getSource().setGameSpeed(SPEED.FAST);
                                break;
                            case GameSpeedWidget.PLAID_TXT:
                                e.getSource().setGameSpeed(SPEED.PLAID);
                                break;
                        }
                    }
                });
                break;

            case PAUSED:
                pauseGlyph.setValue(0.8);
                playGlyph.setValue(0.0);
                break;
            case RUNNING:
                pauseGlyph.setValue(0.0);
                playGlyph.setValue(0.8);
                break;
        }
    }
}
