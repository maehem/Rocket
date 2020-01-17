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

import com.maehem.rocket.engine.game.Game;
import com.maehem.rocket.ui.controls.DialogLayer;
import com.maehem.rocket.ui.controls.DialogPanel;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author maehem
 */
public class GameSettingsDialog extends DialogPanel {

    private static final Logger LOGGER = Logger.getLogger(GameSettingsDialog.class.getName());
    private static final ResourceBundle MSG = ResourceBundle.getBundle("MessageBundle");

    private final Game game;

    public GameSettingsDialog(Game game, DialogLayer dialogLayer, double x, double y) {
        super(MSG.getString("DIALOG_LSS_TITLE_SETTINGS"), dialogLayer, x, y);
        this.game = game;
        
        LOGGER.fine(MSG.getString("MENU_LSS_LOGMSG_SETTINGS_DIALOG"));
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.getChildren().add(nameDescriptionPane());

        // Add more optons later.
        setContent(hbox);
    }

    private VBox nameDescriptionPane() {
        Label colonyNameLabel = new Label("Colony Name:");
        TextField colonyNameTextField = new TextField(game.data.mapInfo.getName());
        colonyNameTextField.textProperty().addListener((o) -> {
            game.data.mapInfo.setName(colonyNameTextField.getText());
        });        
        
        Label founderLabel = new Label("Founder's Name:");
        TextField founderTextField = new TextField(game.data.mapInfo.getFounder());
        founderTextField.textProperty().addListener((o) -> {
            game.data.mapInfo.setFounder(founderTextField.getText());
        });

        Label descriptionLabel = new Label("Description:");
        TextArea descriptionTextArea = new TextArea();
        
        descriptionTextArea.setPrefRowCount(4);
        descriptionTextArea.setEditable(true);
        descriptionTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            game.data.mapInfo.setDescription(descriptionTextArea.getText());
        });
        descriptionTextArea.setText(game.data.mapInfo.getDescription());
        ScrollPane descriptionScrollPane = new ScrollPane(descriptionTextArea);
        descriptionScrollPane.setFitToWidth(true);
        descriptionScrollPane.setFitToHeight(true);
        descriptionScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        descriptionScrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        HBox.setHgrow(descriptionScrollPane, Priority.ALWAYS);

        VBox hb = new VBox();
        hb.getChildren().addAll(
                colonyNameLabel, colonyNameTextField,
                founderLabel, founderTextField,
                descriptionLabel, descriptionScrollPane
        );
        hb.setSpacing(10);

        return hb;
    }
}
