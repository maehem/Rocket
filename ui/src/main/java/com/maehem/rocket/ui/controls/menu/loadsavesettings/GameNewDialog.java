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

import com.maehem.rocket.engine.data.Data;
import com.maehem.rocket.engine.game.Game;
import com.maehem.rocket.ui.controls.DialogLayer;
import com.maehem.rocket.ui.controls.DialogPanel;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author maehem
 */
public class GameNewDialog extends DialogPanel {

    private static final Logger LOGGER = Logger.getLogger(GameNewDialog.class.getName());
    private static final ResourceBundle MSG = ResourceBundle.getBundle("MessageBundle");

    private final Game game;
    private TextField colonyNameField;
    private TextField commanderNameField;
    private ToggleGroup diffGroup;
    private ToggleGroup startGroup;
    private ToggleGroup sizeGroup;

    public GameNewDialog(Game game, DialogLayer dialogLayer, double x, double y) {
        super(MSG.getString("DIALOG_LSS_TITLE_NEWGAME"), dialogLayer, x, y, true);
        this.game = game;

        LOGGER.fine(MSG.getString("MENU_LSS_LOGMSG_NEWGAME_DIALOG"));
        HBox hbox = new HBox();
        //hbox.setPrefSize(1200, 700);
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);

        hbox.getChildren().add(newGameSelectionPane());

        // Add more optons later.
        setContent(hbox);

        
        getDoneButton().addEventHandler(MouseEvent.MOUSE_CLICKED, (t) -> {
            LOGGER.finer("New Game DONE button clicked.");
            Data d = new Data();
            // Read all the Dialog settings and apply them here.
            d.mapInfo.setName(colonyNameField.getText());
            d.mapInfo.setFounder(commanderNameField.getText());
            d.mapInfo.setMoney((int) diffGroup.getSelectedToggle().getUserData());
            d.mapInfo.setYearFounded((int)startGroup.getSelectedToggle().getUserData());
            d.mapInfo.setMapSize((int)sizeGroup.getSelectedToggle().getUserData());
          
            d.initMap();
            d.setLoaded(true);
            game.setData(d);
        });

        // Start a timer to fade and then destroy this dialog.
    }

    private HBox newGameSelectionPane() {
        Rectangle image = new Rectangle(300, 300); // Will be the eye-catch image on right.

        HBox hb = new HBox();
        hb.setSpacing(30);
        VBox vb = new VBox(
                getColonyNamePanel(),
                getCommanderNamePanel(),
                getDifficultyLevelPanel(),
                getDateSizePanel()
        );
        vb.setSpacing(10);
        
        hb.getChildren().add(vb);

        hb.getChildren().add(image);

        return hb;
    }

    private Node getColonyNamePanel() {
        VBox box = new VBox();
        box.setPadding(new Insets(6));
        
        Text label = new Text("Colony Name:");
        colonyNameField = new TextField();
        
        box.getChildren().addAll(label, colonyNameField);
        return box;
    }

    private Node getCommanderNamePanel() {
        VBox box = new VBox();
        box.setPadding(new Insets(6));        
        
        Text label = new Text("Commander Name:");
        commanderNameField = new TextField();
        
        box.getChildren().addAll(label, commanderNameField);
        return box;
    }

    private Node getDifficultyLevelPanel() {
        VBox box = new VBox();
        box.setSpacing(6);
        box.setPadding(new Insets(6));        
        
        Text label = new Text("Difficulty:");
        RadioButton easyB = new RadioButton(  "Easy    CR 80000");
        RadioButton mediumB = new RadioButton("Medium  CR 50000");
        RadioButton hardB = new RadioButton(  "Hard    CR 20000");
        
        easyB.setUserData(80000);
        mediumB.setUserData(50000);
        hardB.setUserData(20000);
        
        diffGroup = new ToggleGroup();
        easyB.setToggleGroup(diffGroup);
        mediumB.setToggleGroup(diffGroup);
        hardB.setToggleGroup(diffGroup);
        
        easyB.setSelected(true);
        
        box.getChildren().addAll(label, easyB, mediumB, hardB);
        return box;
    }

    private Node getDateSizePanel() {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(6));
        hBox.setSpacing(30);
        
        VBox dateBox = new VBox();
        dateBox.setSpacing(6);
        dateBox.setPadding(new Insets(6));        
        
        Text startLabel = new Text("Start Date:");
        RadioButton early = new RadioButton(  "2030");
        RadioButton mid = new RadioButton("2100");
        RadioButton late = new RadioButton(  "2800");
        
        early.setUserData(2030);
        mid.setUserData(2100);
        late.setUserData(2800);
        
        startGroup = new ToggleGroup();
        early.setToggleGroup(startGroup);
        mid.setToggleGroup(startGroup);
        late.setToggleGroup(startGroup);
        
        early.setSelected(true);
        
        dateBox.getChildren().addAll(startLabel, early, mid, late);
        
        
        VBox sizeBox = new VBox();
        sizeBox.setSpacing(6);
        sizeBox.setPadding(new Insets(6));        
        
        Text sizeLabel = new Text("Map Size:");
        RadioButton small = new RadioButton(  "Small    16x16");
        RadioButton medium = new RadioButton( "Medium   32x32");
        RadioButton large = new RadioButton(  "Large    64x64");

        small.setUserData(16);
        medium.setUserData(32);
        large.setUserData(64);
        
        sizeGroup = new ToggleGroup();
        small.setToggleGroup(sizeGroup);
        medium.setToggleGroup(sizeGroup);
        large.setToggleGroup(sizeGroup);
        
        small.setSelected(true);
        
        
        
        sizeBox.getChildren().addAll(sizeLabel, small, medium, large);
        
        
        
        hBox.getChildren().addAll(dateBox, sizeBox);
        
        return hBox;
    }
}
