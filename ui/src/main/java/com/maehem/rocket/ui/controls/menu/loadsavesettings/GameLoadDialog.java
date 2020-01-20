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
import com.maehem.rocket.engine.game.FileSystem;
import com.maehem.rocket.engine.game.Game;
import com.maehem.rocket.engine.game.GameStateFile;
import com.maehem.rocket.ui.controls.DialogLayer;
import com.maehem.rocket.ui.controls.DialogPanel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author maehem
 */
public class GameLoadDialog extends DialogPanel {

    private static final Logger LOGGER = Logger.getLogger(GameLoadDialog.class.getName());
    private static final ResourceBundle MSG = ResourceBundle.getBundle("MessageBundle");

    private final Game game;
    private ListView<String> list;

    public GameLoadDialog(Game game, DialogLayer dialogLayer, double x, double y) {
        super(MSG.getString("DIALOG_LSS_TITLE_LOAD"), dialogLayer, x, y, true);
        this.game = game;

        LOGGER.fine(MSG.getString("MENU_LSS_LOGMSG_LOAD_DIALOG"));
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);

        hbox.getChildren().add(loadGamePane());

        setContent(hbox);

        getDoneButton().setText(MSG.getString("DIALOG_LSS_TITLE_LOAD"));
        getDoneButton().addEventHandler(MouseEvent.MOUSE_CLICKED, (t) -> {
            String item = list.getSelectionModel().getSelectedItems().get(0);
            LOGGER.log(Level.FINER, "Load Game LOAD button clicked: {0}", item);
            doLoad(item);
        });
    }

    private VBox loadGamePane() {
        Label label = new Label("Saved Games");

        FileSystem fs = FileSystem.getInstance();
        File[] listFiles = fs.getSaveDir().listFiles(fs.getGameSaveFilter());
        ArrayList<String> filenames = new ArrayList<>();

        for (File f : listFiles) {
            filenames.add(f.getName());
        }
        list = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList(filenames);
        list.setItems(items);
        list.setOnMouseClicked((t) -> {
            if (t.getClickCount() == 2) {
                String selectedItem = list.getSelectionModel().getSelectedItem();
                if (selectedItem == null) {
                    return; // Blank area. Ignore clicks.
                }
                LOGGER.log(Level.FINEST, "Load Game Double Click on: {0}", selectedItem);

                // TODO:   If a game is already loaded, prompt user that
                // unsaved changes will be lost.
                doLoad(selectedItem);
                this.destroy();
            }
        });
        VBox hb = new VBox();
        hb.getChildren().addAll(
                label, list
        );
        hb.setSpacing(10);

        return hb;
    }

    private void doLoad(String fileName) {
        try {
            Data data = new Data();
            
            LOGGER.log(Level.INFO, "Load Game: {0}", fileName);
            FileSystem fs = FileSystem.getInstance();
            GameStateFile.load(fs.getInputStreamFor(fileName), data);
            if ( data.isLoaded() ) {
                data.mapInfo.setFileSaveName(fileName);
                game.setData(data);
            }
        } catch (IOException ex) {
            LOGGER.log( Level.SEVERE, ex.toString(), ex );
        } finally {
            
        }
    }
}
