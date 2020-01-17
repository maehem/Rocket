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
import java.util.Date;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author maehem
 */
public class ZoneAgriculture extends DialogPanel {

    private static final Logger LOGGER = Logger.getLogger(ZoneAgriculture.class.getName());

    private final Game game;
    private TextField saveAsFileName;

    public ZoneAgriculture(Game game, DialogLayer dialogLayer, double x, double y) {
        super("Save As", dialogLayer, x, y);
        this.game = game;
        
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);

        hbox.getChildren().add(saveAsGamePane());

        // Add more optons later.
        setContent(hbox);
        
        getDoneButton().addEventHandler(MouseEvent.MOUSE_CLICKED, (t) -> {
            LOGGER.finer("Save Game As DONE button clicked.");
        });

    }

    private VBox saveAsGamePane() {
        Label label = new Label("Save As:");
        saveAsFileName = new TextField(game.data.mapInfo.getName() + "_" + new Date().getTime());
        saveAsFileName.setPrefColumnCount(40);
//        colonyNameTextField.textProperty().addListener((o) -> {
//            game.game.data.mapInfo.name = colonyNameTextField.getText();
//        });
        
        
        VBox hb = new VBox();
        hb.getChildren().addAll(
                label, saveAsFileName
                
        );
        hb.setSpacing(10);

        return hb;
    }

//    @Override
//    public void doneHook() {
//        LOGGER.log(Level.CONFIG, "Saving Game as [{0}]", saveAsFileName.getText());
//    }
    
    
}
