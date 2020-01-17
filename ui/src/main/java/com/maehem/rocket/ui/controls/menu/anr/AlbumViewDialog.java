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
package com.maehem.rocket.ui.controls.menu.anr;

import com.maehem.rocket.engine.game.Game;
import com.maehem.rocket.ui.controls.DialogLayer;
import com.maehem.rocket.ui.controls.DialogPanel;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 * @author maehem
 */
public class AlbumViewDialog extends DialogPanel {

    private static final Logger LOGGER = Logger.getLogger(AlbumViewDialog.class.getName());
    private static final ResourceBundle MSG = ResourceBundle.getBundle("MessageBundle");

    public AlbumViewDialog(Game game, DialogLayer dialogLayer, double x, double y) {
        super(MSG.getString("MENU_ANR_ALBUM"), dialogLayer, x, y);

        VBox box = new VBox();
        box.setPadding(new Insets(10));
        box.setMinSize(600, 400);
        box.setPrefSize(600, 400);

        ScrollPane sp = new ScrollPane();
        sp.setContent(snapsViewPane());

        box.getChildren().addAll(pathInfoPane(), sp);

        // Add more optons later.
        setContent(box);

    }

    private Node pathInfoPane() {
        Text t = new Text("C:\\User\\MyDir\\RocketColony\\Album");
        
        return t;
    }

    private Node snapsViewPane() {
        FlowPane fp = new FlowPane();
        fp.setMinSize(550, 350);
        fp.setPrefSize(550, 350);
        fp.setVgap(4);
        fp.setHgap(4);
        fp.setPrefWrapLength(500); // preferred width allows for two columns        
        return fp;
    }
}
