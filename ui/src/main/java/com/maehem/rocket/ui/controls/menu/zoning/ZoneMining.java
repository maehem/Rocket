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
package com.maehem.rocket.ui.controls.menu.zoning;

import com.maehem.rocket.engine.game.Game;
import com.maehem.rocket.ui.controls.DialogLayer;
import com.maehem.rocket.ui.controls.DialogPanel;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author maehem
 */
public class ZoneMining extends DialogPanel {

    private static final Logger LOGGER = Logger.getLogger(ZoneMining.class.getName());

    private final Game game;

    public ZoneMining(Game game, DialogLayer dialogLayer, double x, double y) {
        super("Save", dialogLayer, x, y);
        this.game = game;
        
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);

        hbox.getChildren().add(saveAsGamePane());

        // Add more optons later.
        setContent(hbox);
        
        getDoneButton().addEventHandler(MouseEvent.MOUSE_CLICKED, (t) -> {
            LOGGER.finer("Save Game DONE button clicked.");
        });
        
        
        // Start a timer to fade and then destroy this dialog.

    }

    private VBox saveAsGamePane() {
        Label label = new Label("Game has been Saved.");        
        
        VBox hb = new VBox();
        hb.getChildren().add(label);

        return hb;
    }

}
