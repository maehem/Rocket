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
package com.maehem.rocket.ui.controls.menu.loadsavesettings;

import com.maehem.rocket.engine.game.FileSystem;
import com.maehem.rocket.engine.game.Game;
import com.maehem.rocket.engine.game.GameStateFile;
import com.maehem.rocket.ui.controls.DialogLayer;
import com.maehem.rocket.ui.controls.DialogPanel;
import java.io.IOException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
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
public class GameSaveAsDialog extends DialogPanel {

    private static final Logger LOGGER = Logger.getLogger(GameSaveAsDialog.class.getName());
    private static final ResourceBundle MSG = ResourceBundle.getBundle("MessageBundle");

    private final Game game;
    private TextField saveAsFileName;

    public GameSaveAsDialog(Game game, DialogLayer dialogLayer, double x, double y) {
        super(MSG.getString("DIALOG_LSS_TITLE_SAVEAS"), dialogLayer, x, y, true);
        this.game = game;
        
        LOGGER.fine(MSG.getString("MENU_LSS_LOGMSG_SAVEAS_DIALOG"));
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);

        hbox.getChildren().add(saveAsGamePane());

        // Add more optons later.
        setContent(hbox);
        
        getDoneButton().setText(MSG.getString("DIALOG_LSS_TITLE_SAVEAS"));
        getDoneButton().addEventHandler(MouseEvent.MOUSE_CLICKED, (t) -> {
            LOGGER.finer("Save Game As DONE button clicked.");
            try {
                // TODO  Pause game
                FileSystem fs = new FileSystem();
                GameStateFile.save(fs.getOutputStreamFor(saveAsFileName.getText()), game.data);
                // TODO restore previous running state.
            } catch (IOException ex) {
                LOGGER .log(Level.SEVERE, ex.toString(), ex);
            }
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
    
}
