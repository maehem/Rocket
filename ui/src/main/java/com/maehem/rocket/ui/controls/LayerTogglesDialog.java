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

import com.maehem.rocket.engine.renderer.Graphics;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author maehem
 */
public class LayerTogglesDialog extends DialogPanel {
    private static final Logger LOGGER = Logger.getLogger(LayerTogglesDialog.class.getName());
    private static final ResourceBundle MSG = ResourceBundle.getBundle("MessageBundle");
    private final Graphics gfx;
    
    public LayerTogglesDialog(Graphics gfx, DialogLayer dialogLayer, double x, double y) {
        super(MSG.getString("DIALOG_LAYER_TOGGLE"), dialogLayer, x, y);
        
        this.gfx = gfx;
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10));
        hbox.setSpacing(30);
        VBox leftBox = new VBox(aboveGroundToggles(), undergroundToggles());
        leftBox.setSpacing(20);
        VBox rightBox = new VBox(layerViewToggles(), resetViewButton());
        rightBox.setSpacing(20);        
        hbox.getChildren().addAll(leftBox, rightBox);

        setContent(hbox);

        getDoneButton().addEventHandler(MouseEvent.MOUSE_CLICKED, (t) -> {
            LOGGER.log(Level.FINER, "{0} {1}", new Object[]{MSG.getString("DIALOG_LAYER_TOGGLE"),MSG.getString("DIALOG_DONE")});
            // Let's load something.
        });        
    }
    
    private Node aboveGroundToggles() {
        Button b = new Button("Above Ground");
        CheckBox trans = new CheckBox("Transportation");
        CheckBox power = new CheckBox("Power");
        CheckBox flora = new CheckBox("Flora");
        CheckBox zonedStructure = new CheckBox("Zoned Structures");
        CheckBox otherStructure = new CheckBox("Other Structures");
        CheckBox zones = new CheckBox("Zones");
        zones.setSelected(gfx.showZones);
        zones.setOnAction((t) -> {
            LOGGER.config("Layer Toggle - Zones changed.");
            gfx.showZones = zones.isSelected();
        });
        
        return new VBox(10, b,trans,power,flora,zonedStructure,otherStructure,zones);
    }
    
    private Node undergroundToggles() {
        Button b = new Button("Underground");
        CheckBox subways = new CheckBox("Subways");
        CheckBox water = new CheckBox("WaterPipes");
        
        return new VBox(10, b,subways,water);
    }
    
    private Node layerViewToggles() {
        Button b = new Button("Layer Views");
        RadioButton aura = new RadioButton("Aura");
        RadioButton polution = new RadioButton("Polution");
        RadioButton density = new RadioButton("Density");
        
        return new VBox(10, b,aura,density, polution);
    }
    
    private Node resetViewButton() {
        Button b = new Button("Reset View");
        
        return new VBox(10, b);
    }
    
}
