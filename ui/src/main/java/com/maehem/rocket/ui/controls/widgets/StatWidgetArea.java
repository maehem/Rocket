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

import com.maehem.rocket.engine.data.DataListener;
import com.maehem.rocket.engine.data.type.GameInfo;
import com.maehem.rocket.engine.game.Game;
import com.maehem.rocket.engine.game.events.GameEvent;
import com.maehem.rocket.engine.game.events.GameListener;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 *
 * @author maehem
 */
public class StatWidgetArea extends GridPane implements GameListener, DataListener {

    private static final Logger LOGGER = Logger.getLogger(StatWidgetArea.class.getName());

    public static final double CORNER_ARC = 10;
    private final StatWidget colonyName;
    private final StatWidget colonyPop;
    private final StatWidget colonyMoney;
    private final StatWidget colonyDate;
    private final Game game;
    private final TextTicker ticker;
    
    public StatWidgetArea(Game game) {
        this.game = game;
        
        colonyName = new StatWidget("/glyphs/colony-name.png", 280);
        colonyPop = new StatWidget("/glyphs/population.png", 140);
        colonyMoney = new StatWidget("/glyphs/money.png", 140);
        colonyDate = new StatWidget("/glyphs/date.png", 140);

        //gp.setGridLinesVisible(true);  // For debugging
        setVgap(5);
        setHgap(10);
        setPadding(new Insets(10));

        Light.Distant light = new Light.Distant();
        light.setAzimuth(-135.0f);
        Lighting l = new Lighting();
        l.setLight(light);
        l.setSurfaceScale(5.0f);

        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5), Insets.EMPTY)));
        setEffect(l);

        ticker = new TextTicker();
        //ticker.setPauseMode(!game.isRunning());
        
        add(colonyName, 0, 1);
        add(colonyPop, 1, 1);
        add(colonyMoney, 2, 1);
        add(colonyDate, 3, 1);
        add(ticker, 0, 0, 4, 1);
        
        game.addListener(this);
    }

    @Override
    public void gameEvent(GameEvent e) {
        switch (e.type) {
            case DATA_LOADED:
                GameInfo mapInfo = e.getSource().data.mapInfo;

                mapInfo.addDataChangeListener(GameInfo.PROP_NAME, this);
                mapInfo.addDataChangeListener(GameInfo.PROP_POPULATION, this);
                mapInfo.addDataChangeListener(GameInfo.PROP_TICKS_ELEAPSED, this);
                mapInfo.addDataChangeListener(GameInfo.PROP_MONEY, this);

                // Set up the values for the first time.
                colonyName.setText(mapInfo.getName());
                colonyMoney.setText(String.valueOf(mapInfo.getMoney()));
                colonyPop.setText(String.valueOf(mapInfo.getPopulation()));
                colonyDate.setText(e.getSource().getDate());

                break;

            case DATA_SAVED:
                break;
            case TICK:
                break;
            case SPEED:
                break;
            case RUNNING:
            case PAUSED:
                ticker.setPauseMode(!game.isRunning());
                break;
                
        }
    }

    @Override
    public void dataChange(String key, Object oldValue, Object newValue) {
        switch (key) {
            case GameInfo.PROP_NAME:
                colonyName.setText((String) newValue);
                break;
            case GameInfo.PROP_MONEY:
                colonyMoney.setText(String.valueOf(newValue));
            // TODO  Set Color Green=increase  Red = decrease.
                break;
            case GameInfo.PROP_POPULATION:
                colonyPop.setText(String.valueOf(newValue));
            // TODO Set color Green=increase  Red = decrease.
                break;
            case GameInfo.PROP_TICKS_ELEAPSED:
                colonyDate.setText(game.getDate());
                break;
        }
    }

    
}
